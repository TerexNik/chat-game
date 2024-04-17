package org.nterekhin.gameP2P.client;

import org.nterekhin.gameP2P.ui.PlayerUI;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        playerUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        playerUI.setVisible(true);

        Thread listenerThread = buildClientThread(socket, playerUI);
        clientPool.execute(listenerThread);
        playerUIs.add(playerUI);
    }

    private Thread buildClientThread(Socket socket, PlayerUI playerUI) {
        return new Thread(() -> {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while (!socket.isClosed() && (message = in.readLine()) != null) {
                    playerUI.appendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void close() {
        clientPool.shutdown();
        clientPool = null;
        clearClients();
    }

    public void clearClients() {
        playerUIs.forEach(JFrame::dispose);
        playerUIs.clear();
    }

    public static PlayerUIManager getInstance() {
        return instance;
    }
}
