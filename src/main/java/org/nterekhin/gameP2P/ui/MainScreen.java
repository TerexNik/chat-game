package org.nterekhin.gameP2P.ui;

import org.nterekhin.gameP2P.server.ServerManager;

import javax.swing.*;
import java.awt.*;

/**
 * This is mainScreen class.
 * It consists of server side buttons and client side buttons
 * You can connect to other instances if you know their port
 * <p>
 * All important functions you can find in UIActions.class
 * Some of Button logic is too simple to move them there so it remains here
 */
public class MainScreen extends JFrame {
    public MainScreen() {
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
        exitApplication.addActionListener(e -> ServerManager.getInstance().shutdownApplication());
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

        JButton createNewServer = new JButton("Create new server");
        createNewServer.addActionListener(e ->
                UIActions.createNewServer(
                        MainScreen.this,
                        getPort(portField.getText()))
        );
        gbc.gridx = 2;
        centerPanel.add(createNewServer, gbc);


        JButton stopServer = new JButton("Stop Server");
        stopServer.addActionListener(e -> UIActions.stopServer());
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
        createNewClient.addActionListener(event ->
                UIActions.createNewClient(
                        MainScreen.this,
                        getPort(portField.getText())));
        gbc.gridx = 2;
        centerPanel.add(createNewClient, gbc);
    }

    private int getPort(String port) {
        int result = Integer.parseInt(port);
        if (result < 1024 || result > 65535) {
            return 4440;
        } else {
            return result;
        }

    }
}
