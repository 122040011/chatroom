package com.example.chatroom;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class showLogin extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        Parent parent;
        FXMLLoader fxmlLoader = new FXMLLoader(showLogin.class.getResource("/com/example/chatroom/login.fxml"));
        parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        stage.setMinHeight(387);
        stage.setMaxHeight(387);
        stage.setMinWidth(608);
        stage.setMaxWidth(608);
        stage.setTitle("MeChat Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //starts the program
        System.out.println("Starting...");
        launch();
    }
}