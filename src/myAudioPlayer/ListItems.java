/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myAudioPlayer;

import java.io.File;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;

/**
 *
 * @author WYNXTYN PROAUTOTYPE
 */
public class ListItems extends HBox {

    private ImageView track_imageView = new ImageView();
    private GridPane gridPane = new GridPane();
    private Label track_title, track_genre, track_artist, track_album, track_format = new Label();
    Pane pane = new Pane();
    Pane Spacepane = new Pane();

    private String artist;
    private String title;
    private String album;
    private String genre;
    private String format;
    private String location;
    private String image_location;

    public String getImage_location() {
        return image_location;
    }

    public void setImage_location(String image_location) {
        this.image_location = image_location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    
    
    public ListItems(String image_location,String artist, String title, String album, String genre, String format, String location) {
        pane = new Pane();
        Spacepane = new Pane();
        Spacepane.setPrefWidth(10);

        this.image_location = image_location;
        this.artist = artist;
        this.title = title;
        this.album = album;
        this.genre = genre;
        this.format = format;
        this.location = location;

        Image image = new Image("myAudioPlayer/headphones.png");
        
        track_imageView = new ImageView(image);
        track_imageView.setFitHeight(150);
        track_imageView.setFitWidth(150);
//        track_imageView.setPreserveRatio(true);
        pane.setStyle("-fx-border-color:lightblue;");
        pane.getChildren().add(track_imageView);

        track_title = new Label(title);
        track_title.setWrapText(true);
        track_title.setFont(Font.font("Comic Sans MS", 15));        
        
        track_genre = new Label(genre);
        track_genre.setWrapText(true);
         track_genre.setFont(Font.font("Comic Sans MS", 15)); 
         
        track_artist = new Label(artist);
        track_artist.setWrapText(true);
          track_artist.setFont(Font.font("Comic Sans MS", 15)); 
        
        track_album = new Label(album);
        track_album.setWrapText(true);
         track_album.setFont(Font.font("Comic Sans MS", 15)); 
        
        track_format = new Label(format);
        track_format.setWrapText(true);
         track_format.setFont(Font.font("Comic Sans MS", 15)); 

        gridPane = new GridPane();
//        gridPane.setStyle("-fx-border-color:black;");
        gridPane.setHgap(10);
        gridPane.setVgap(10);
//        gridPane.setGridLinesVisible(true);
        
        
        gridPane.addRow(0, track_title);
        gridPane.addRow(1, track_artist);
        gridPane.addRow(2, track_album);
        gridPane.addRow(3, track_genre);
        gridPane.addRow(4, track_format);

        getChildren().addAll(this.pane, this.gridPane);

    }

    public ImageView getTrack_imageView() {
        return track_imageView;
    }

    public void setTrack_imageView(ImageView track_imageView) {
        this.track_imageView = track_imageView;
    }

    public Label getTrack_title() {
        return track_title;
    }

    public void setTrack_title(Label track_title) {
        this.track_title = track_title;
    }

    public Label getTrack_genre() {
        return track_genre;
    }

    public void setTrack_genre(Label track_genre) {
        this.track_genre = track_genre;
    }

    public Label getTrack_artist() {
        return track_artist;
    }

    public void setTrack_artist(Label track_artist) {
        this.track_artist = track_artist;
    }

    public Label getTrack_album() {
        return track_album;
    }

    public void setTrack_album(Label track_album) {
        this.track_album = track_album;
    }

    public Label getTrack_format() {
        return track_format;
    }

    public void setTrack_format(Label track_format) {
        this.track_format = track_format;
    }

}
