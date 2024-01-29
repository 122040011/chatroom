module com.example.chatroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires javafx.media;
    requires javafx.swing;


    opens com.example.chatroom to javafx.fxml;
    exports com.example.chatroom;
}