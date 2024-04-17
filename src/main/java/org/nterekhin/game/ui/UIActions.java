package org.nterekhin.game.ui;

import org.nterekhin.game.client.PlayerUIManager;
import org.nterekhin.game.server.ServerManager;

import javax.swing.*;
import java.io.IOException;
import java.net.BindException;

/**
 * Class for convenience for functions bigger than 1 line from MainScreen actionListeners
 */
public class UIActions {
    public static void createNewClient(MainScreen mainScreen, int port) {
        try {
            PlayerUIManager.getInstance().startClient(port);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainScreen,
                    "Something went wrong, check that server is running on port: " + port);
        }
    }

    public static void createNewServer(MainScreen mainScreen, int port) {
        try {
            if (!ServerManager.getInstance().isServerRunning()) {
                ServerManager.getInstance().startServer(port);
                JOptionPane.showMessageDialog(mainScreen, "Server started on port " + port);
            } else {
                JOptionPane.showMessageDialog(mainScreen, "Server is already started");
            }
        } catch (BindException e) {
            JOptionPane.showMessageDialog(mainScreen,
                    "Server is already started or port is occupied try to create client"
            );
        }
    }

    public static void stopServer() {
        PlayerUIManager.getInstance().clear();
        ServerManager.getInstance().shutdownServer();
    }
}
