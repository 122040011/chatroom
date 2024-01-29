package com.example.chatroom;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class test {
    {


        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(test.class.getResource("../../../../../../target/classes/com/example/chatroom/chatPage.fxml"));
            Scene scene;
            scene = new Scene(fxmlLoader.load(), 1000, 1000);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
