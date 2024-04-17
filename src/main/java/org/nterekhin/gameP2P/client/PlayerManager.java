package org.nterekhin.gameP2P.client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class PlayerManager {

    private static final PlayerManager instance = new PlayerManager();
    private static final String MESSAGE_FORMAT = "message%s, nickname: %s, Message counter: %d";

    private final List<PlayerHandler> playerHandlers;
    private final ExecutorService pool;

    private PlayerManager() {
        this.playerHandlers = new ArrayList<>();
        this.pool = Executors.newCachedThreadPool();
    }

    public void createPlayerHandler(Socket socket) {
        try {
            PlayerHandler clientHandler = new PlayerHandler(socket);
            playerHandlers.add(clientHandler);
            pool.execute(clientHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastServerMessage(String message) {
        playerHandlers.forEach(player -> player.sendMessage("Server: " + message));
    }

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

    public void disconnectByPort(int port) {
        playerHandlers.stream().filter(handler -> port == handler.getSocket().getPort())
                .findFirst().ifPresent(handler -> {
                    if (handler.getPlayerState().getNickname() != null) {
                        handler.disconnect();
                    } else {
                        handler.close();
                    }
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

    public void close() {
        pool.shutdown();
        playerHandlers.forEach(PlayerHandler::close);
    }

    public static PlayerManager getInstance() {
        return instance;
    }
}
