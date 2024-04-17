package org.nterekhin.game.client;

import org.nterekhin.game.ui.PlayerUI;
import org.nterekhin.game.util.IOFunction;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Helper class that manages opened Player's UI
 * Singleton
 */
public final class PlayerUIManager {

    private static final PlayerUIManager instance = new PlayerUIManager();
    private final List<PlayerUI> playerUIs;
    private ExecutorService clientPool;

    private PlayerUIManager() {
        playerUIs = new ArrayList<>();
    }

    public void startClient(int port) throws IOException {
        clientPool = Executors.newCachedThreadPool();
        Socket socket = new Socket("127.0.0.1", port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        PlayerUI playerUI = new PlayerUI(out);

        calculateWindowLocation(playerUI);
        setUpClosingProcedure(playerUI, socket.getLocalPort());
        playerUI.setVisible(true);

        Thread listenerThread = buildClientThread(socket, playerUI);
        clientPool.execute(listenerThread);
        playerUIs.add(playerUI);
    }

    // Build client threads that will read messages form the socket and input them into chatArea
    private Thread buildClientThread(Socket socket, PlayerUI playerUI) {
        return new Thread(() -> IOFunction.executeWithLogOnIOException(() -> {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            while (!socket.isClosed() && (message = in.readLine()) != null) {
                playerUI.appendMessage(message);
            }
        }));
    }

    // Setting position of the window close to previously opened window
    private void calculateWindowLocation(PlayerUI playerUI) {
        if (!playerUIs.isEmpty()) {
            playerUI.setLocation(400 * playerUIs.size(), 0);
        }
    }

    // Setting closing operation for close socket and streams on window close
    private void setUpClosingProcedure(PlayerUI playerUI, int localPort) {
        playerUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        playerUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PlayerManager.getInstance().disconnectByPort(localPort);
                playerUIs.remove(playerUI);
            }
        });
    }

    public void clear() {
        playerUIs.forEach(JFrame::dispose);
        playerUIs.clear();
    }

    // Gracefully close all opened UI windows and close execution pool
    public void shutdown() {
        clear();
        clientPool.shutdown();
        clientPool = null;
    }

    public static PlayerUIManager getInstance() {
        return instance;
    }
}
