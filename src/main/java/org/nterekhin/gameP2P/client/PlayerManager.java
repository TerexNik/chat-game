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
            if (state.getNickname().equals(nickname) && !message.equals(" connected")) {
                playerHandler.sendMessage(String.format(MESSAGE_FORMAT,
                        message,
                        nickname,
                        state.getMessageCounter()));
            } else {
                playerHandler.sendMessage(nickname + message);
            }
        }
    }

    public void close() {
        pool.shutdown();
        clearPlayerHandlers();
    }

    public void clearPlayerHandlers() {
        playerHandlers.forEach(PlayerHandler::shutdown);
        playerHandlers.clear();
    }

    public static PlayerManager getInstance() {
        return instance;
    }
}
