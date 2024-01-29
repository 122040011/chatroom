package com.example.chatroom;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class loginController implements Initializable {


    @FXML
    private Label displayInfo;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String password = passwordTextField.getText();
                String username = usernameTextField.getText();
                if(!password.isEmpty() && !username.isEmpty()){
                    {
                        Connection connection;
                        ResultSet resultTable;
                        PreparedStatement insertLoginInfo;
                        PreparedStatement checkUser;

                        try{
                            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/loginInfoSchema", "root", "sandisandi123");
                            checkUser = connection.prepareStatement("SELECT * FROM loginInfo WHERE username = ?");
                            checkUser.setString(1, username);
                            resultTable = checkUser.executeQuery();
                            if (resultTable.isBeforeFirst()){
                                System.out.println("User already exists");
                                displayInfo.setText("User already exists");
                                resultTable.close();
                                checkUser.close();
                            }
                            else{
                                insertLoginInfo = connection.prepareStatement("INSERT INTO loginInfo (username, password, profilePictureNumber) values (?, ?, 0)");
                                insertLoginInfo.setString(1,username);
                                insertLoginInfo.setString(2, password);
                                insertLoginInfo.executeUpdate();
                                System.out.println("Signed up!");
                                resultTable.close();
                                checkUser.close();
                                insertLoginInfo.close();
                                sceneChanger.changeScene(actionEvent, "/com/example/chatroom/chatPage.fxml", username, 585, 795);
                                try{
                                    resultTable.close();
                                    checkUser.close();
                                    insertLoginInfo.close();
                                }catch (SQLException e) {e.printStackTrace();}
                            }

                        } catch (SQLException e) {e.printStackTrace();}


                    }
                }
                else {
                    displayInfo.setText("Username or Password is Empty");
                }




            }
        });


        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String password = passwordTextField.getText();
                String username = usernameTextField.getText();
                if(!password.isEmpty() && !username.isEmpty()){
                    {
                        Connection connection;
                        ResultSet resultTable;
                        PreparedStatement searchUsername;

                        try{
                            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/loginInfoSchema", "root", "sandisandi123");
                            searchUsername = connection.prepareStatement("SELECT password FROM loginInfo where username = ?");
                            searchUsername.setString(1, username);
                            resultTable = searchUsername.executeQuery();
                            if(!resultTable.isBeforeFirst()){
                                //user not found
                                System.out.println("User not found");
                                displayInfo.setText("User not found");
                                resultTable.close();
                                searchUsername.close();

                            }
                            else{
                                resultTable.next();
                                String registeredPassword = resultTable.getString("password");
                                if(registeredPassword.equals(password)){
                                    System.out.println("Logged in!");
                                    resultTable.close();
                                    searchUsername.close();
                                    sceneChanger.changeScene(actionEvent, "/com/example/chatroom/chatPage.fxml", username, 585, 795);
                                }
                                else{
                                    resultTable.close();
                                    searchUsername.close();
                                    System.out.println("Password is incorrect");
                                    displayInfo.setText("Password is incorrect");
                                }
                            }
                        } catch (SQLException e) {e.printStackTrace();}


                    }
                }
                else {
                    displayInfo.setText("Username or Password is Empty");
                }

            }

        });







    }
}
