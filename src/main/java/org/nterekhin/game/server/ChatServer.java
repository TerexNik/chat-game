package org.nterekhin.game.server;

import org.nterekhin.game.client.PlayerManager;
import org.nterekhin.game.eventBus.EventBus;
import org.nterekhin.game.eventBus.event.PlayerConnectedEvent;
import org.nterekhin.game.util.IOFunction;

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
                EventBus.getInstance().postEvent(new PlayerConnectedEvent(socket));
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

    public boolean isRunning() {
        return running && !serverSocket.isClosed();
    }
}
