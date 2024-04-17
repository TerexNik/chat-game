package org.nterekhin.game.ui;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;


/**
 * Player UI class
 * Contains from chat area and input area
 */
public class PlayerUI extends JFrame {
    private final JTextArea chatArea;
    private final JTextField inputField;

    public PlayerUI(PrintWriter out) {
        setTitle("Chat Client");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.addActionListener(e -> {
            String message = inputField.getText();
            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(PlayerUI.this, "Empty messages is not allowed");
            }
            out.println(message);
            inputField.setText("");
        });
        add(inputField, BorderLayout.SOUTH);
    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n");
    }
}
