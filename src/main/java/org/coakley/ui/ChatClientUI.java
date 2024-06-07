package org.coakley.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.coakley.client.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatClientUI extends JFrame {
    private static final Logger logger = LogManager.getLogger(ChatClientUI.class);
    private JTextArea messageArea;
    private JTextField textField;
    private JButton exitButton;
    private ChatClient client;

    public ChatClientUI(){
        super("Chat App");
        setSize(500, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Color backgroundColor = new Color(60,60,60);
        Color buttonColor = new Color(75,75,75);
        Color textColor = new Color(250,250,250);
        Color messageColor = new Color(75,75,75);
        Font textFont = new Font("Roboto Mono", Font.PLAIN, 14);
        Font buttonFont = new Font("Roboto Mono", Font.BOLD,12);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setForeground(textColor);
        messageArea.setBackground(backgroundColor);
        messageArea.setFont(textFont);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        String name = JOptionPane.showInputDialog(this, "Enter you name: ", "Name Entry", JOptionPane.PLAIN_MESSAGE);
        this.setTitle("Chat App - " + name);

        textField = new JTextField();
        textField.setFont(textFont);
        textField.setForeground(Color.BLACK);
        textField.setBackground(Color.WHITE);

        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "]" + name + ": " + textField.getText();
                client.sendMessage(message);
                textField.setText("");
            }
        });

        exitButton = new JButton("Exit");
        exitButton.setForeground(Color.BLACK);
        exitButton.setBackground(buttonColor);
        exitButton.setFont(buttonFont);
        exitButton.addActionListener(e -> {
            String departureMessage = name + " has left the chat.";
            client.sendMessage(departureMessage);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie){
                Thread.currentThread().interrupt();
            }
            System.exit(0);
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.add(textField, BorderLayout.CENTER);
        bottomPanel.add(exitButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        try {
            this.client = new ChatClient("127.0.0.1",2000, this::onMessageReceived);
            client.startClient();
        } catch (IOException e){
            logger.error("Error Connecting the server", e);
            JOptionPane.showMessageDialog(this,"Error connecting the server", "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

    }
    private void onMessageReceived(String message){
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ChatClientUI().setVisible(true);
        });
    }
}