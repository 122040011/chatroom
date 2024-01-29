package com.example.chatroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client2 implements Runnable {

    private Socket socket2;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private chatPageController controller;

    public Client2(chatPageController controller){
        this.controller = controller;
    }

    @Override
    public void run() {
        try{
            //establish connection
            socket2 = new Socket("localhost", 7640);
            System.out.println(socket2.getPort());
            System.out.println(socket2.getLocalPort());
            objectOutputStream = new ObjectOutputStream(socket2.getOutputStream());
            objectInputStream = new ObjectInputStream(socket2.getInputStream());
            InputHandler inputHandler = new InputHandler();
            //calls the thread to listen for messages
            Thread thread = new Thread(inputHandler);
            thread.start();

        }catch (IOException e){e.printStackTrace();}
    }
    public void sendMessage(Message message) throws IOException {
        objectOutputStream.writeObject(message);
    }
//listen for message
    class InputHandler implements Runnable{
        @Override
        public void run(){
            try {
                while(socket2.isConnected()){
                    Message message = (Message) objectInputStream.readObject();
                    if(message!=null)
                    {
                        controller.addMessage(message);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }


        }



    }
}
