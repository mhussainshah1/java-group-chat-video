package com.example.serverfx;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {
    private ServerSocket serverSocket;
    private Consumer<Serializable> onReceiveCallback;
    private ClientHandler clientHandler;

    public Server(ServerSocket serverSocket, Consumer<Serializable> onReceiveCallback) {
        this.serverSocket = serverSocket;
        this.onReceiveCallback = onReceiveCallback;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                this.clientHandler = new ClientHandler(socket, onReceiveCallback);
                Thread ClientThread = new Thread(clientHandler);
                ClientThread.start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }      
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String data) {
        if (clientHandler != null)
            clientHandler.broadcastMessage(data);
    }
}