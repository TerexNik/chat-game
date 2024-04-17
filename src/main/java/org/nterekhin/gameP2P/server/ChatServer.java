package org.nterekhin.gameP2P.server;

import org.nterekhin.gameP2P.client.PlayerManager;
import org.nterekhin.gameP2P.util.IOFunction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements Runnable {

    // Flag for execution
    private volatile boolean running = true;
    private final ServerSocket serverSocket;

    public ChatServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (running) {
                Socket socket = serverSocket.accept();
                PlayerManager.getInstance().createPlayerHandler(socket);
            }
        } catch (IOException e) {
            shutdown();
        }
    }

    public void shutdown() {
        IOFunction.executeWithLogOnIOException(() -> {
            running = false;
            if (!serverSocket.isClosed()) {
                serverSocket.close();
            }
            PlayerManager.getInstance().clear();
        });
    }
}
