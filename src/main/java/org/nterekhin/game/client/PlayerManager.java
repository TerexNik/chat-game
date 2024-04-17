package org.nterekhin.game.client;

import org.nterekhin.game.util.IOFunction;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Helper class that can create PlayerHandlers and communicate with them
 * Singleton
 */
public final class PlayerManager {

    private static final PlayerManager instance = new PlayerManager();
    private static final String MESSAGE_FORMAT = "message%s, nickname: %s, Message counter: %d";

    private final List<PlayerHandler> playerHandlers;
    private final ExecutorService pool;

    private PlayerManager() {
        this.playerHandlers = Collections.synchronizedList(new ArrayList<>());
        this.pool = Executors.newCachedThreadPool();
    }

    public void createPlayerHandler(Socket socket) {
        IOFunction.executeWithLogOnIOException(() -> {
            PlayerHandler clientHandler = new PlayerHandler(socket, new PlayerState());
            playerHandlers.add(clientHandler);
            pool.execute(clientHandler);
        });
    }

    // Used when we need to tell all players something form Server
    public void broadcastServerMessage(String message) {
        playerHandlers.forEach(player -> player.sendMessage("Server: " + message));
    }

    /**
     * Used for chat send message
     *
     * @param nickname nickname of the player that sent message
     */
    public void broadcast(String nickname, String message) {
        for (PlayerHandler playerHandler : playerHandlers) {
            PlayerState state = playerHandler.getPlayerState();
            if (nickname != null && nickname.equals(state.getNickname())) {
                playerHandler.sendMessage(String.format(MESSAGE_FORMAT,
                        message,
                        nickname,
                        state.getMessageCounter()));
            } else {
                playerHandler.sendMessage(nickname + message);
            }
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
