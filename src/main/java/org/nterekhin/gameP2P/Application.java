package org.nterekhin.gameP2P;

import org.nterekhin.gameP2P.client.PlayerUIManager;
import org.nterekhin.gameP2P.config.ServerConfigProperties;
import org.nterekhin.gameP2P.server.ServerManager;
import org.nterekhin.gameP2P.ui.MainScreen;

import javax.swing.*;
import java.io.IOException;
import java.net.BindException;

public class Application {

    public static void main(String[] args) {
        MainScreen app = new MainScreen();
        app.setLocationRelativeTo(null);
        handleConfiguration(ServerConfigProperties.getInstance(), app);
        SwingUtilities.invokeLater(() -> app.setVisible(true));
    }

    /**
     * We decide if we need to start server and Players on start
     *
     * @param config configuration from config.properties file
     * @param app    screen on which alert will be shown if something went wrong
     */
    private static void handleConfiguration(ServerConfigProperties config, MainScreen app) {
        if (config.getCreateServer()) {
            if (!ServerManager.getInstance().isServerRunning()) {
                try {
                    ServerManager.getInstance().startServer(4440);
                } catch (BindException e) {
                    JOptionPane.showMessageDialog(app, "Server is already started");
                }
            }
            int createPlayers = config.getCreatePlayers();
            if (createPlayers > 0 && createPlayers < 10) {
                for (int i = 0; i < createPlayers; i++) {
                    try {
                        PlayerUIManager.getInstance().startClient(4440);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(app,
                                "Something went wrong, check that server is running on port: " + 4440);
                    }
                }
            }
        }
    }
}
