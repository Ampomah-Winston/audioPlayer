/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myAudioPlayer;

import audio.*;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

/**
 *
 * @author WYNXTYN PROAUTOTYPE
 */
public class MyAudioPlayer_main extends Application {

    public static Stage stage = null;
    public Point2D anchorPt;
    public Point2D previousPos = new Point2D(0, 0);

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.stage = primaryStage; 
        Parent root = FXMLLoader.load(getClass().getResource("myAudioPlayer.fxml"));
        Scene scene = new Scene(root);
       
        
        this.stage.setTitle("My Music Player");     
        this.stage.initStyle(StageStyle.TRANSPARENT);
        this.stage.setScene(scene);
        this.stage.show();

        this.stage.addEventHandler(WindowEvent.WINDOW_SHOWN, (WindowEvent t) -> {
            previousPos = new Point2D(stage.getX(), stage.getY());

        });

        initMovablePlayer();

        scene.setOnMouseClicked(e -> {
            anchorPt = new Point2D(e.getSceneX(), e.getSceneY());
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setContentText(String.valueOf("X: " + anchorPt.getX()) + "Y: " + anchorPt.getY());
//            alert.show();
        });

        // set the current location
        scene.setOnMouseReleased(mouseEvent
                -> previousPos = new Point2D(previousPos.getX(),
                        previousPos.getY())
        );

        scene.setOnMouseDragged(e -> {
            stage.setX(previousPos.getX()
                    + e.getScreenX()
                    - anchorPt.getX()
            );
            stage.setY(previousPos.getY()
                    + e.getScreenY()
                    - anchorPt.getY()
            );
        });

    }

    private void initMovablePlayer() {
        Scene scene = stage.getScene();
        // starting initial anchor point
        scene.setOnMousePressed(mouseEvent
                -> anchorPt = new Point2D(mouseEvent.getScreenX(),
                        mouseEvent.getScreenY()));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
