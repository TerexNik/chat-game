package org.nterekhin.gameP2P.ui;

import org.nterekhin.gameP2P.client.PlayerUIManager;
import org.nterekhin.gameP2P.server.ServerManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainScreen extends JFrame {

    private final ServerManager serverManager;

    public MainScreen(ServerManager serverManager) {
        this.serverManager = serverManager;
        setTitle("360T Assignment");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Panel to hold components in the center
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        setUpServerSideButtons(centerPanel, gbc);
        setUpClientSideButtons(centerPanel, gbc);

        JButton exitApplication = new JButton("exit");
        exitApplication.addActionListener(e -> shutdownApplication());
        gbc.gridx = 1;
        gbc.gridy = 3;
        centerPanel.add(exitApplication, gbc);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void setUpServerSideButtons(JPanel centerPanel, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 0;
        JTextArea textArea = new JTextArea();
        textArea.setText("Please select port for server");
        textArea.setEditable(false);
        centerPanel.add(textArea, gbc);

        gbc.gridx = 1;
        JTextField portField = new JTextField();
        portField.setText("4440");
        centerPanel.add(portField, gbc);

        JButton createNewServer = new JButton("Create new Server");
        createNewServer.addActionListener(e -> {
            if (ServerManager.getInstance().isServerRunning()) {
                JOptionPane.showMessageDialog(MainScreen.this, "Server is already started");
                return;
            }
            int port = getPort(portField.getText());
            serverManager.startServer(port);
            JOptionPane.showMessageDialog(MainScreen.this, "Server started on port " + port);
        });
        gbc.gridx = 2;
        centerPanel.add(createNewServer, gbc);


        JButton stopServer = new JButton("Stop Server");
        stopServer.addActionListener(e -> serverManager.shutdownServer());
        gbc.gridx = 3;
        centerPanel.add(stopServer, gbc);
    }

    private void setUpClientSideButtons(JPanel centerPanel, GridBagConstraints gbc) {
        gbc.gridx = 0;
        gbc.gridy = 1;
        JTextArea textArea = new JTextArea();
        textArea.setText("Please select port for client");
        textArea.setEditable(false);
        centerPanel.add(textArea, gbc);

        gbc.gridx = 1;
        JTextField portField = new JTextField();
        portField.setText("4440");
        centerPanel.add(portField, gbc);


        JButton createNewClient = new JButton("Create new Client");
        createNewClient.addActionListener(event -> {
            try {
                int port = getPort(portField.getText());
                PlayerUIManager.getInstance().startClient(port);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(MainScreen.this,
                        "Something went wrong, check that server is running on port: " + portField.getText());
            }
        });
        gbc.gridx = 2;
        centerPanel.add(createNewClient, gbc);
    }

    private int getPort(String port) {
        int result = Integer.parseInt(port);
        if (result < 1024 || result > 65535) {
            return result;
        } else {
            return 4440;
        }

    }

    private void shutdownApplication() {
        serverManager.shutdownServer();
        this.dispose();
        System.exit(0);
    }
}
