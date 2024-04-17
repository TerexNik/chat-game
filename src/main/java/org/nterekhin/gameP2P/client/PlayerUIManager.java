package org.nterekhin.gameP2P.client;

import org.nterekhin.gameP2P.ui.PlayerUI;

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

        // Setting position of the window close to previously opened window
        if (!playerUIs.isEmpty()) {
            playerUI.setLocation(400 * playerUIs.size(), 0);
        }

        // Setting closing operation for close socket and streams on window close
        playerUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        playerUI.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PlayerManager.getInstance().disconnectByPort(socket.getLocalPort());
                playerUIs.remove(playerUI);
            }
        });
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
        playerUIs.forEach(JFrame::dispose);
        playerUIs.clear();
    }

    public void closeByNickname(String nickname) {

    }

    public static PlayerUIManager getInstance() {
        return instance;
    }
}
