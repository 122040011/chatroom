package com.example.chatroom;

import javafx.scene.layout.VBox;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Archiver {

    public static void archive(VBox vbox) throws IOException, SQLException {
        File file = new File("/com/example/asdkajhskd/MySQLTemp");
        String path = file.getAbsolutePath();
        FileOutputStream outFile = new FileOutputStream(path);
        ObjectOutputStream objectOut = new ObjectOutputStream(outFile);
        objectOut.writeObject(vbox);
        objectOut.close();

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/loginInfoSchema", "root", "sandisandi123");



    }






}
