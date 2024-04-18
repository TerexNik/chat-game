package org.nterekhin.game.client;

import org.nterekhin.game.config.ServerConfigProperties;
import org.nterekhin.game.eventBus.EventBus;
import org.nterekhin.game.eventBus.event.MessagesLimitEvent;
import org.nterekhin.game.util.IOFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Helper class that can create PlayerHandlers and communicate with them
 * Singleton not final for mocks in tests
 */
public class PlayerManager {

    private static final PlayerManager instance = new PlayerManager();
    private static final String MESSAGE_FORMAT = "%s%s, Message counter: %d";

    private final List<PlayerHandler> playerHandlers;
    private final ExecutorService pool;

    private PlayerManager() {
        this.playerHandlers = Collections.synchronizedList(new ArrayList<>());
        this.pool = Executors.newCachedThreadPool();
    }

    public void createPlayerHandler(PlayerHandler handler) {
        IOFunction.executeWithLogOnIOException(() -> {
            playerHandlers.add(handler);
            pool.execute(handler);
        });
    }

    // Used when we need to tell all players something form Server
    public void broadcastServerMessage(String message) {
        playerHandlers.forEach(player -> player.sendMessage("Server: " + message));
    }

    /**
     * Used for chat send message
     *
     * @param state state of player that send message
     */
    public void broadcast(PlayerState state, String message) {
        String nickname = state.getNickname();
        synchronized (playerHandlers) {
            PlayerHandler sender = playerHandlers.stream().filter(handler -> nickname != null && nickname.equals(handler.getPlayerState().getNickname())).findFirst().get();
            if (message != null && !message.isEmpty()) {
                if (!isEnoughPlayersOnline()) {
                    sender.sendMessage("Please wait until at least 2 players will be online");
                }
                playerHandlers.stream().filter(handler -> !handler.equals(sender)).forEach(handler -> {
                    sender.sendMessage(String.format(MESSAGE_FORMAT,
                            handler.getPlayerState().getNickname(),
                            message,
                            state.getMessageCounter()));
                    checkStopCondition(state.incrementMessageCounter());
                    handler.sendMessage(nickname + message);
                });
            }
        }
    }

    private void checkStopCondition(int messageCounter) {
        if (messageCounter >= ServerConfigProperties.getInstance().getMessageLimit() + 1) {
            EventBus.getInstance().postEvent(new MessagesLimitEvent());
        }
    }

    // Used by UI to close corresponding connection on window::dispose
    public void disconnectByPort(int port) {
        playerHandlers.stream().filter(handler -> port == handler.getSocket().getPort())
                .findFirst().ifPresent(handler -> {
                    if (handler.getPlayerState().getNickname() != null) {
                        handler.disconnect();
                    } else {
                        handler.close();
                    }
                    playerHandlers.remove(handler);
                });
    }

    public void disconnectByNickname(String nickname) {
        playerHandlers.stream().filter(handler ->
                nickname != null && nickname.equals(handler.getPlayerState().getNickname())
        ).findFirst().ifPresent(handler -> {
            handler.close();
            playerHandlers.remove(handler);
        });
    }

    public boolean isEnoughPlayersOnline() {
        return playerHandlers.size() > 1;
    }

    public boolean verifyNickname(String nickname) {
        return nickname != null && !nickname.isEmpty()
                && playerHandlers.stream().map(PlayerHandler::getPlayerState)
                .map(PlayerState::getNickname)
                .noneMatch(nickname::equals);
    }

    public void clear() {
        synchronized (playerHandlers) {
            playerHandlers.forEach(PlayerHandler::close);
            playerHandlers.clear();
        }
    }

    // Gracefully close all opened handlers and stop executionPool
    public void shutdown() {
        clear();
        pool.shutdown();
    }

    public static PlayerManager getInstance() {
        return instance;
    }
}
