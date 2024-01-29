package com.example.chatroom;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;

import javax.swing.*;
import java.io.Serializable;


public class Message implements Serializable {

    //packages the information sent
    private String username;
    private String text;
    private int[][] image;
    private Media media;

    //constructor
    public Message(String username){
        this.username = username;
    }


    //methods
    public String getText(){return text;}

    public void addString(String text){
        this.text = text;
    }

    public void addImage(int[][] image){
        this.image = image;
    }

    public void addMedia(Media media){
        this.media = media;
    }


    public String getUsername(){
        return username;
    }
    public int[][] getImage(){ return image;}
    public Media getMedia(){ return media;}

}
