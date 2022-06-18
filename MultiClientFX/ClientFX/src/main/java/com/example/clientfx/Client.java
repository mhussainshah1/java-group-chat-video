package com.example.clientfx;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {
    private Socket socket;
    private String userName;
    private Consumer<Serializable> onReceiveCallback;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(Socket socket, String userName, Consumer<Serializable> onReceiveCallback) {
        try {
            this.socket = socket;
            this.userName = userName;
            this.onReceiveCallback = onReceiveCallback;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            onReceiveCallback.accept("Error creating client.");
            e.printStackTrace();
            closeEverything();
        }
    }

    public void sendMessage(String messageToSend) {
        try {
            if (socket.isConnected()) {
                bufferedWriter.write(messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            onReceiveCallback.accept("Error sending message to client");
            closeEverything();
        }
    }

    public void listenForMessage() {
        new Thread(() -> {
            String messageFromServer;
            while (socket.isConnected()) {
                try {
                    messageFromServer = bufferedReader.readLine();
                    onReceiveCallback.accept(messageFromServer);
                } catch (IOException e) {
                    e.printStackTrace();
                    onReceiveCallback.accept("Error receiving from the Server " + e);
                    closeEverything();
                    break;
                }
            }
        }).start();
    }

    private void closeEverything() {
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
