package org.nterekhin.gameP2P.ui;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;


// UI class
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
            out.println(message);
            inputField.setText("");
        });
        add(inputField, BorderLayout.SOUTH);
    }

    public void appendMessage(String message) {
        chatArea.append(message + "\n");
    }
}
