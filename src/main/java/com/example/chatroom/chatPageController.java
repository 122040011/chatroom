package com.example.chatroom;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;


public class chatPageController implements Initializable {

    private String username;

    @FXML
    private TextField messageTextField;

    @FXML
    private Button buttonSend;

    @FXML
    private Button pictureButton;

    @FXML
    private Label labelUsername;

    @FXML
    private MenuButton more;

    @FXML
    private MenuItem buttonLogout;


    @FXML
    private VBox messagePanel;

    @FXML
    private VBox userVBox;

    @FXML
    private MenuItem archive;

    @FXML
    private MenuItem viewArchiveButton;

    @FXML
    private MenuItem smileyButton;

    @FXML
    private MenuItem cryingButton;

    @FXML
    private MenuItem heartButton;

    @FXML
    private MenuItem angryButton;

    @FXML
    private MenuItem surprisedButton;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Creates Connection to Find User and Create Userlist
        Connection connection;
        ResultSet resultTable;
        PreparedStatement insertLoginInfo;
        PreparedStatement checkUser;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/loginInfoSchema", "root", "sandisandi123");
            checkUser = connection.prepareStatement("SELECT * FROM loginInfo");
            resultTable = checkUser.executeQuery();

            resultTable.next();
            while(!resultTable.isAfterLast()){

                String userToAdd = resultTable.getString("username");
                HBox userLine = new HBox();
                Label userLabel = new Label();
                userLabel.setText(userToAdd);
                userLine.getChildren().add(userLabel);
                userVBox.getChildren().add(userLine);
                resultTable.next();

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Creates Client Thread to Send and Listen for messages
        Client2 client2 = new Client2(this);////10.30.118.15.
            Thread t = new Thread(client2);
            t.start();
            System.out.println("Client made");


        //Send Messages (String TEXT)
        buttonSend.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
            String text = messageTextField.getText();
            messageTextField.clear();
            if (!text.isEmpty()){
                addTextToVBox(text, username, Pos.CENTER_RIGHT);
                Message message = new Message(username);
                message.addString(text);
                try {
                    client2.sendMessage(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            }
        });

        //Send Picture
        pictureButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    String path = messageTextField.getText();
                    messageTextField.clear();
                    Message message = new Message(username);
                    Image image = new Image(path);
                    if(image!=null){
                        int[][] imageToSend = serializeImage.convertImage(image);
                        message.addImage(imageToSend);
                        addImageToVBox(image, username, Pos.CENTER_RIGHT);
                        client2.sendMessage(message);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        //Logout Button. Ends Thread. Changes scene to login page.
        buttonLogout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //client2.close();
                t.interrupt();
                sceneChanger.changeSceneLogout(buttonSend, "/com/example/chatroom/login.fxml", null, 387, 608);

            }
        });

        archive.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("archiving");
                Connection connection2;
                PreparedStatement preparedStatement;
                ResultSet resultSet;
                int id;
                String sender = null;



                try {
                    connection2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/loginInfoSchema", "root", "sandisandi123");
                    preparedStatement = connection2.prepareStatement("SELECT * FROM archivedMessage ");
                    resultSet = preparedStatement.executeQuery();
                    resultSet.last();
                    int lastID = resultSet.getInt("id");
                    id = lastID+1;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                VBox sendPanel = messagePanel;
                List<Node> messageLineList = sendPanel.getChildren();
                for (Node messageLine : messageLineList){
                    List<Node> hBoxList = ((VBox) messageLine).getChildren();

                    for(Node individualHBox : hBoxList){
                        List<Node> hBoxList2 = ((HBox)individualHBox).getChildren();
                        for(Node hBox : hBoxList2){
                            System.out.println(hBox.getClass().getName());
                            if(hBox.getClass().getName().equals("javafx.scene.control.Label")){
                                Label usernameLabel = (Label) hBox;
                                sender = usernameLabel.getText();
                            }
                            else if(hBox.getClass().getName().equals("javafx.scene.image.ImageView")){
                                String text = "//Image is not archivable//";

                                try {
                                    preparedStatement = connection2.prepareStatement("INSERT INTO archivedMessage (id, username, sender, text) values (?, ?, ?, ?)");
                                    preparedStatement.setInt(1, id);
                                    preparedStatement.setString(2, username);
                                    preparedStatement.setString(3, sender);
                                    preparedStatement.setString(4, text);
                                    preparedStatement.executeUpdate();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }



                            }
                            else if(hBox.getClass().getName().equals("javafx.scene.text.TextFlow")){
                                String text = null;
                                TextFlow textFlow = (TextFlow) hBox;
                                List<Node> textFlowMessage = textFlow.getChildren();
                                for(Node textMessage : textFlowMessage){
                                    text = ((Text) textMessage).getText();
                                }

                                try {
                                    preparedStatement = connection2.prepareStatement("INSERT INTO archivedMessage (id, username, sender, text) values (?, ?, ?, ?)");
                                    preparedStatement.setInt(1, id);
                                    preparedStatement.setString(2, username);
                                    preparedStatement.setString(3, sender);
                                    preparedStatement.setString(4, text);
                                    preparedStatement.executeUpdate();

                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                        }


                        }
                    }
                }
                String systemMessage = "Your Message has been archived with the ID: " + id;
                addTextToVBox(systemMessage, "System", Pos.CENTER_LEFT);



            }
        });
        viewArchiveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();

                FXMLLoader fxmlLoader = new FXMLLoader(chatPageController.class.getResource("/com/example/chatroom/archivedConversationPage.fxml"));
                Parent parent = null;
                try {
                    parent = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                archivedConversationController controller = fxmlLoader.getController();


                Scene scene = new Scene(parent);
                int height = 420;
                int width = 460;
                stage.setMinHeight(height);
                stage.setMaxHeight(height);
                stage.setMinWidth(width);
                stage.setMaxWidth(width);
                stage.setTitle("View Archived Conversations");
                stage.setScene(scene);
                stage.show();
                controller.setUsername(username);



            }
        });

        smileyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addStringToTextField("\uD83D\uDE04");
            }
        });

        cryingButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addStringToTextField("\uD83D\uDE22");
            }
        });

        heartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addStringToTextField("\uD83D\uDE0D");
            }
        });

        angryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addStringToTextField("\uD83D\uDE20\t");
            }
        });

        surprisedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addStringToTextField("\uD83D\uDE31");

            }
        });

    }




    //Create a line of text
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
        if(messageUsername.equals(this.username)){
            messageBubble.setStyle("-fx-background-color: #f8d5a6" + "; -fx-background-radius: 10px");
        }
        else{
            messageBubble.setStyle("-fx-background-color: #ffffff" + "; -fx-background-radius: 10px");
        }


        textLine.getChildren().add(messageBubble);
        textLine.setAlignment(position);
        messageLine.getChildren().add(textLine);
        //add to messagePanel
        messagePanel.getChildren().add(messageLine);
    }

    //Create a line of image + username
    public void addImageToVBox(Image image, String messageUsername, Pos position){
        VBox messageLine = new VBox();
        HBox imageLine = new HBox();
        HBox userLabelLine = new HBox();
        Label userLabel = new Label();

        messageLine.setPadding(new Insets(5, 5, 15, 5));
        userLabel.setText(messageUsername);
        userLabel.setPadding(new Insets(5));
        userLabelLine.getChildren().add(userLabel);
        userLabelLine.setAlignment(position);
        messageLine.getChildren().add(userLabelLine);

        //Create imageView frame
        ImageView frame = new ImageView(image);
        frame.setFitWidth(300);
        frame.setPreserveRatio(true);
        imageLine.getChildren().add(frame);

        imageLine.setAlignment(position);
        messageLine.getChildren().add(imageLine);
        //add to messagePanel
        messagePanel.getChildren().add(messageLine);


    }

    public void addMediaToVBox(Media media, String messageUsername, Pos position){
        VBox messageLine = new VBox();
        HBox mediaLine = new HBox();
        HBox userLabelLine = new HBox();
        Label userLabel = new Label();

        messageLine.setPadding(new Insets(5, 5, 15, 5));
        userLabel.setText(messageUsername);
        userLabel.setPadding(new Insets(5));
        userLabelLine.getChildren().add(userLabel);
        userLabelLine.setAlignment(position);
        messageLine.getChildren().add(userLabelLine);

        //Create imageView frame
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(50);
        mediaView.setPreserveRatio(true);
        mediaLine.getChildren().add(mediaView);

        mediaLine.setAlignment(position);
        messageLine.getChildren().add(mediaLine);
        //add to messagePanel
        messagePanel.getChildren().add(messageLine);
    }

//sets username for class
    public void setUsername(String username){
        this.username = username;
        labelUsername.setText(username);
    }

    //receives message from client. Displays using addTextToVBox & addImageToVBox method.
    public void addMessage(Message message){
        String messageUsername = message.getUsername();
        String messageText = message.getText();
        Media messageMedia = message.getMedia();

        if (messageText!=null){
            Platform.runLater(()-> addTextToVBox(messageText, messageUsername, Pos.CENTER_LEFT));
        }
        else if(message.getImage()!=null){
            Image messageImage = serializeImage.getImage(message.getImage());
            Platform.runLater(()-> addImageToVBox(messageImage, messageUsername, Pos.CENTER_LEFT));
        }
        else if(messageMedia!=null){
            Platform.runLater(()-> addMediaToVBox(messageMedia, messageUsername, Pos.CENTER_LEFT));
        }
    }

    public void addStringToTextField(String string){
        String currentString = messageTextField.getText();
        String newString = currentString + string;
        messageTextField.setText(newString);
    }

}
