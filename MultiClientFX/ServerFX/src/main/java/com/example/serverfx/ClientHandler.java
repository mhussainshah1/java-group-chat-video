package com.example.serverfx;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ClientHandler implements Runnable {
    public static List<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private Consumer<Serializable> onReceiveCallback;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    public ClientHandler(Socket socket, Consumer<Serializable> onReceiveCallback) {
        try {
            this.socket = socket;
            this.onReceiveCallback = onReceiveCallback;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);
            onReceiveCallback.accept("SERVER: " + clientUserName + " has entered the chat!");
            broadcastMessage("SERVER: " + clientUserName + " has entered the chat!");
        } catch (IOException e) {
            closeEverything();
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                String messageFromClient = bufferedReader.readLine();
                onReceiveCallback.accept(messageFromClient);
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                //e.printStackTrace();
                System.err.println("Error receiving from the client" + e);
                closeEverything();
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (messageToSend.startsWith("SERVER: ") ||
                        !clientHandler.clientUserName.equals(clientUserName)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything();
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        onReceiveCallback.accept("SERVER: " + clientUserName + " has left the chat!");
        broadcastMessage("SERVER: " + clientUserName + " has left the chat!");
    }

    private void closeEverything() {
        removeClientHandler();
        try {
            if (bufferedWriter != null) bufferedWriter.close();
            if (bufferedReader != null) bufferedReader.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
