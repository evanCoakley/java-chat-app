package org.coakley.client;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private Consumer<String> onMessageReceived;

    public ChatClient(String serverAddress, int serverPort, Consumer<String> onMessageReceived) throws IOException {
        this.socket = new Socket(serverAddress,serverPort);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.onMessageReceived = onMessageReceived;
    }
    public void sendMessage(String msg){
        out.println(msg);
    }
    public void startClient(){
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null){
                    onMessageReceived.accept(line);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }).start();
    }

}

