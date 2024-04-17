package org.nterekhin.gameP2P.server;

import org.nterekhin.gameP2P.client.PlayerManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Server class
public class ChatServer implements Runnable {

    private volatile boolean running = true;
    private final ServerSocket serverSocket;
    private final PlayerManager playerManager = PlayerManager.getInstance();

    public ChatServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                playerManager.createPlayerHandler(socket);
            } catch (IOException e) {
                shutdown();
            }
        }
    }

    public void shutdown() {
        running = false;
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
            playerManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
