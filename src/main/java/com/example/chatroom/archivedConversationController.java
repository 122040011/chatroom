package com.example.chatroom;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class archivedConversationController implements Initializable {

    private String username1;

    @FXML
    private Button loadButton;

    @FXML
    private TextField VBoxNoTextField;

    @FXML
    private Button backButton;

    @FXML
    private VBox displayVBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                displayVBox.getChildren().clear();

                int conversationID = Integer.parseInt(VBoxNoTextField.getText());

                Connection connection = null;
                ResultSet resultTable = null;
                PreparedStatement findMessage = null;

            //id, username, sender, text, image


                try {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/loginInfoSchema", "root", "sandisandi123");
                    findMessage= connection.prepareStatement("SELECT * FROM archivedMessage WHERE id = ?");
                    findMessage.setInt(1, conversationID);

                    resultTable = findMessage.executeQuery();
                    resultTable.next();
                    while(!resultTable.isAfterLast()){

                        String textMessage = resultTable.getString("text");
                        String sender = resultTable.getString("sender");
                        //TODO picture


                        if(textMessage!=null){
                            if(sender.equals(username1)){
                                addTextToVBox(textMessage, sender, Pos.CENTER_RIGHT);
                            }
                            else{
                                addTextToVBox(textMessage, sender, Pos.CENTER_LEFT);
                            }
                        }
                        //TODO IMAGE (if image!=null)
                        resultTable.next();
                    }

                    resultTable.close();
                    findMessage.close();


                    //TODO read file n insert vbox


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


            }
        });

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        });




    }


    public void setUsername(String username){
        this.username1 = username;
    }

    public void addTextToVBox(String text, String messageUsername, Pos position){
        //Create HBox
        VBox messageLine = new VBox();
        HBox textLine = new HBox();
        HBox userLabelLine = new HBox();
        Label userLabel = new Label();


        messageLine.setPadding(new Insets(5, 5, 15, 5));
        userLabel.setText(messageUsername);
        userLabel.setPadding(new Insets(5));
        userLabelLine.getChildren().add(userLabel);
        userLabelLine.setAlignment(position);
        messageLine.getChildren().add(userLabelLine);

        //Create text from textflow
        TextFlow messageBubble = new TextFlow(new Text(text));
        messageBubble.setLineSpacing(5);
        messageBubble.setMaxWidth(400);
        messageBubble.setPadding(new Insets(3));
        messageBubble.setStyle("-fx-background-color: #f8d5a6" + "; -fx-background-radius: 10px");


        textLine.getChildren().add(messageBubble);
        textLine.setAlignment(position);
        messageLine.getChildren().add(textLine);
        //add to messagePanel
        displayVBox.getChildren().add(messageLine);
    }

}
