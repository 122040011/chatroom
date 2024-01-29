package com.example.chatroom;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.io.Serializable;

public class serializeImage implements Serializable {

    //convert image to int array (serializable) to be able to send through socket
public static int[][] convertImage(Image image){

    int h = (int) image.getHeight();
    int w = (int) image.getWidth();
    int[][] output = new int[w][h];

    PixelReader r = image.getPixelReader();
    for (int i=0;i<w;i++) {
        for (int j=0;j<h;j++) {
                output[i][j] = r.getArgb(i, j);
        }
    }

    return output;
}
//after received by client, construct image
public static Image getImage(int[][] input){
    int w = input.length;
    int h = input[0].length;
    WritableImage image = new WritableImage(w, h);

    PixelWriter writer = image.getPixelWriter();
    for (int i = 0; i < w; i++) {
        for (int j = 0; j < h; j++) {
            writer.setArgb(i, j, input[i][j]);
        }
    }

    return image;



}



}
