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
    private OutputStream outputStream;
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
        try {
            handleClientSocket();
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("Error receiving from the client" + e);
            closeEverything();
        }
    }

    private void handleClientSocket() throws IOException {
        this.outputStream = socket.getOutputStream();
        while (socket.isConnected()) {
            String messageFromClient = bufferedReader.readLine();
            String[] tokens = messageFromClient.split(" ");//StringUtils.split(line);
            /*if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                if ("logoff".equalsIgnoreCase(cmd) || "quit".equalsIgnoreCase(cmd)) {
                    handleLogoff();
                    break;
                } else if ("login".equalsIgnoreCase(cmd)) {
                    handleLogin(outputStream, tokens);
                } else if ("msg".equalsIgnoreCase(cmd)) {
                    String[] tokensMsg = messageFromClient.split(" ", 3); //StringUtils.split(line, null, 3);
                    handleMessage(tokensMsg);
                } else if ("join".equalsIgnoreCase(cmd)) {
                    handleJoin(tokens);
                } else if ("leave".equalsIgnoreCase(cmd)) {
                    handleLeave(tokens);
                } else {
                    String msg = "unknown " + cmd + "\r\n";
                    outputStream.write(msg.getBytes());*/
                    onReceiveCallback.accept(messageFromClient);
                    broadcastMessage(messageFromClient);
               /* }
            }*/
        }
    }

  /*  private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String login = tokens[1];
            String password = tokens[2];

//            boolean found = login.equals("guest") && password.equals("guest") || (login.equals("jim") && password.equals("jim"));
            boolean found = DBUtils.logInUser(login, password);
            if (found) {
                String msg = "ok login\r\n";
                outputStream.write(msg.getBytes());
                this.login = login;
                System.out.println("User logged in successfully: " + login);



                //send current user all other online logins
                for (ClientHandler worker : clientHandlers) {
                    if (worker.getLogin() != null) {
                        if (!login.equals(worker.getLogin())) {
                            String msg2 = "online " + worker.getLogin() + "\r\n";
                            send(msg2);
                        }
                    }
                }

                //send other online users current user's status
                String onlineMsg = "online " + login + "\r\n";
                for (ClientHandler worker : clientHandlers) {
                    if (!login.equals(worker.getLogin())) {
                        worker.send(onlineMsg);
                    }
                }
            } else {
                String msg = "error login\r\n";
                outputStream.write(msg.getBytes());
                System.err.println("Login failed for " + login);
            }
        }
    }*/

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
