package com.example.chatroom;


import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server implements Runnable {

    private static ArrayList<ClientHandler> users;

    public Server() {
        users = new ArrayList<>();
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(7640);
            while (true) {
                System.out.println("Creating Thread, Waiting to Accept");
                Socket socket = serverSocket.accept();
                System.out.println(socket.getLocalPort());
                System.out.println(socket.getPort());
                ClientHandler clientHandler = new ClientHandler(socket);
                users.add(clientHandler);
                System.out.println("User Connected");
                Thread t = new Thread(clientHandler);
                t.start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    class ClientHandler implements Runnable {

        private String username;

        private Socket socket;

        private ObjectOutputStream objectOutputStream;

        private ObjectInputStream objectInputStream;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());

                while (socket.isConnected()) {
                    Message message = (Message) objectInputStream.readObject();
                    if (message != null) {
                        broadcast(message, this);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        public void broadcast(Message message, ClientHandler clientHandler) throws IOException {
            for (ClientHandler clientHandlers : users) {
                if (clientHandlers != clientHandler){
                    clientHandlers.objectOutputStream.writeObject(message);
                }

            }
        }


    }
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}










