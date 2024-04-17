package org.nterekhin.gameP2P.ui;

import org.nterekhin.gameP2P.client.PlayerUIManager;
import org.nterekhin.gameP2P.server.ServerManager;

import javax.swing.*;
import java.io.IOException;

public class UIActions {
    public static void createNewClient(MainScreen mainScreen, int port) {
        try {
            if (!ServerManager.getInstance().isServerRunning()) {
                JOptionPane.showMessageDialog(mainScreen, "Server is not running");
            }
            PlayerUIManager.getInstance().startClient(port);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainScreen,
                    "Something went wrong, check that server is running on port: " + port);
        }
    }

    public static void createNewServer(MainScreen mainScreen, int port) {
        if (ServerManager.getInstance().isServerRunning()) {
            JOptionPane.showMessageDialog(mainScreen, "Server is already started");
            return;
        }
        ServerManager.getInstance().startServer(port);
        JOptionPane.showMessageDialog(mainScreen, "Server started on port " + port);
    }
}
