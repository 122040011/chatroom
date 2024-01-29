package com.example.chatroom;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class sceneChanger {
    public static void changeScene(ActionEvent event, String newFXML, String username, int height, int width){
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(sceneChanger.class.getResource("/com/example/chatroom/chatPage.fxml"));
            Parent a = fxmlLoader.load();
            chatPageController controller = fxmlLoader.getController();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(a);
            stage.setMaxHeight(height);
            stage.setMinHeight(height);
            stage.setMaxWidth(width);
            stage.setMinWidth(width);
            stage.setScene(scene);
            stage.show();
            controller.setUsername(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeSceneLogout(Node node, String newFXML, String username, int height, int width){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(sceneChanger.class.getResource(newFXML));
            Stage stage = (Stage) (node.getScene().getWindow());
            stage.close();
            Stage login = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setMaxHeight(height);
            stage.setMinHeight(height);
            stage.setMaxWidth(width);
            stage.setMinWidth(width);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
