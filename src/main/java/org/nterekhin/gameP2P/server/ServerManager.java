package org.nterekhin.gameP2P.server;

import org.nterekhin.gameP2P.client.PlayerManager;
import org.nterekhin.gameP2P.client.PlayerUIManager;
import org.nterekhin.gameP2P.eventBus.EventBus;

import java.io.IOException;
import java.net.BindException;

/**
 * Helper class that helps with server state changes
 * Singleton
 */
public final class ServerManager {
    private static final ServerManager instance = new ServerManager();
    private Thread serverThread;
    private ChatServer chatServer;

    public void startServer(int port) throws BindException {
        if (chatServer != null && isServerRunning()) {
            return;
        }
        try {
            chatServer = new ChatServer(port);
            serverThread = new Thread(chatServer);
            serverThread.start();
            EventBus.getInstance().setUpEventBusForServer();
            System.out.println("Chat server running on port " + port);
        } catch (BindException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            shutdownServer();
        }
    }

    // Shuts server down, but keeps Application alive
    public void shutdownServer() {
        if (chatServer != null) {
            serverThread.interrupt();
            chatServer.shutdown();
        }
        EventBus.getInstance().clearUpEventBusFromServerListeners();
        System.out.println("Chat server shut down");
    }

    // Full shutdown with Application exit
    public void shutdownApplication() {
        if (chatServer != null) {
            serverThread.interrupt();
            chatServer.shutdown();
        }
        PlayerUIManager.getInstance().shutdown();
        PlayerManager.getInstance().shutdown();
        chatServer = null;
        EventBus.getInstance().clearUpEventBusFromServerListeners();
        System.out.println("Chat server shut down");
        System.exit(0);
    }

    public boolean isServerRunning() {
        return serverThread != null && serverThread.isAlive();
    }

    public static ServerManager getInstance() {
        return instance;
    }
}
