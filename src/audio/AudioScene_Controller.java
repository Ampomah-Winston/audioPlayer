/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audio;

import com.jfoenix.controls.JFXSlider;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * FXML Controller class
 *
 * @author WYNXTYN PROAUTOTYPE
 */
public class AudioScene_Controller implements Initializable {

    Point2D currentPos2D = new Point2D(0, 0);
    Point2D previousPos = new Point2D(0, 0);
    Scene scenery = audioPlayer_main.stage.getScene();
    Stage stage = audioPlayer_main.stage;
    private MediaPlayer mediaPlayer;
    private ChangeListener<Duration> progressListener;
    private ChangeListener<DoubleProperty> volumeer;
    private Label lbl_x;
    private Label lbl_y;
    @FXML
    private Button btn_play;
    @FXML
    private Button btn_pause;
    @FXML
    private Button btn_stop;
    @FXML
    private MediaView mediaViewer;
    @FXML
    private JFXSlider progressSlider;
    @FXML
    private JFXSlider volumeSlider;
    @FXML
    private Pane mediaView_Container;
    @FXML
    private ListView<String> list_musics;
    private boolean isRandom;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        previousPos = new Point2D(stage.getX(), stage.getX());
        mediaViewer.setMediaPlayer(mediaPlayer);

        progressListener = ((observable, oldValue, newValue) -> {
            progressSlider.setValue(newValue.toSeconds());
        });
         seeking();
//        mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());
//        volumeSlider.valueProperty().bind( mediaPlayer.volumeProperty());
    }

    @FXML
    private void Main_Stage_MouseClicked(MouseEvent event) {
        currentPos2D = new Point2D(event.getSceneX(), event.getSceneY());
    }

    private Node createApplicationArea() {
        Rectangle applicationArea = new Rectangle();
        applicationArea.widthProperty().bind(scenery.widthProperty());
        applicationArea.heightProperty().bind(scenery.heightProperty());
        return applicationArea;
    }

    @FXML
    private void MainMouseDragged(MouseEvent event) {

    }

    @FXML
    private void Main_Drag_Over(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles() || db.hasUrl()) {
            event.acceptTransferModes(TransferMode.LINK);
        } else {
            event.consume();
        }
    }

    @FXML
    private void Main_Drag_Dropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        String filepath = "";
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            if (db.getFiles().size() > 0) {
                try {
                    URL url = db.getFiles().get(0).toURI().toURL();
                    filepath = url.toString();
                    playMedia(filepath);
                } catch (MalformedURLException e) {
                }
            }

        } else {
            //handle playMedia from host or jar
            playMedia(db.getUrl());
            success = true;
        }
        event.setDropCompleted(true);
        event.consume();
    }

    @FXML
    private void play(ActionEvent event) {
        mediaPlayer.play();
    }

    @FXML
    private void pause(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @FXML
    private void stop(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    private void playMedia(String filepath) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.setOnPaused(null);
            mediaPlayer.setOnPlaying(null);
            mediaPlayer.setOnReady(null);
            mediaPlayer.currentTimeProperty().removeListener(progressListener);
            mediaPlayer.setAudioSpectrumListener(null);
        }
        Media media = new Media(filepath);
        for (String s : media.getMetadata().keySet()) {
            //System.out.println(s);
        }
        mediaPlayer = new MediaPlayer(media);
       
        // as the media is playing move the slider for progress
        mediaPlayer.currentTimeProperty().addListener(progressListener);
        mediaPlayer.setOnReady(() -> {
            progressSlider.setValue(0);
            progressSlider.setMax(mediaPlayer.getMedia().getDuration().toSeconds());
            mediaPlayer.play();
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            if (list_musics.getItems().size() >= 2) {
                int size = list_musics.getItems().size();
                isRandom = false;
                int currentPosition = 0;
                if (isRandom == false) {
                    if (!list_musics.getSelectionModel().isEmpty()) {
                        try {
                            currentPosition = list_musics.getSelectionModel().getSelectedIndex();
                            currentPosition++;
                            list_musics.getSelectionModel().select(currentPosition);
                            if (!(currentPosition >= size)) {
                                
                                String playThis = list_musics.getItems().get(currentPosition);
                                String filePath = new File(playThis).toURI().toURL().toString();
                                playMedia(filePath);
                            } else {
                                    currentPosition = 0;                     
                                    String playThis = list_musics.getItems().get(currentPosition);
                                    String filePath = new File(playThis).toURI().toURL().toString();
                                    playMedia(filePath);
                                    currentPosition++;                          
                            }
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(AudioScene_Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {

                    }
                }
            }
        });

        // setup visualization (circle container)
        mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
            // mediaView_Container.getChildren().clear();
            int i = 0;
            //int x = 10;
            double y = mediaViewer.getFitHeight() / 3;
            double x = mediaViewer.getFitWidth() / 4;

            Random rand = new Random(System.currentTimeMillis());
            // Build random colored circles
            for (float phase : phases) {
                int red = rand.nextInt(255);
                int green = rand.nextInt(255);
                int blue = rand.nextInt(255);
                Circle circle = new Circle(10);

                circle.setCenterX(x + i);
                circle.setCenterY(y + (phase * 100));
                circle.setFill(Color.rgb(red, green, blue, .70));
                mediaView_Container.getChildren().add(circle);
                i += 3;
                fadeOut(mediaView_Container, circle, i);
            }
        });
    }

    public void fadeOut(Pane container, Node node, int seconds) {
        Duration duration = Duration.seconds(seconds);

        if (duration.greaterThanOrEqualTo(Duration.seconds(20))) {

            if (container.getChildren().size() >= 100) {
                container.getChildren().remove(1, 29);
            }
            // System.out.println(container.getChildren().size());
        }
    }

    public void seeking() {
        volumeSlider.valueProperty().addListener(e ->{
            if (volumeSlider.isValueChanging()) {
                volumeSlider.valueProperty().bind(mediaPlayer.volumeProperty());
            }
        });
    }

    @FXML
    private void list_on_mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            if (!list_musics.getSelectionModel().isEmpty()) {
                int i = list_musics.getSelectionModel().getSelectedIndex();
                if (list_musics.getSelectionModel().isSelected(i)) {
                    String filePath = list_musics.getSelectionModel().getSelectedItem();
                    try {
                        File file = new File(filePath);
                        filePath = file.toURI().toURL().toString();
                        playMedia(filePath);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(AudioScene_Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    @FXML
    private void list_drag_over(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.LINK);
        } else {
            event.consume();
        }
    }

    @FXML
    private void list_drag_drop(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            if (db.getFiles().size() > 0) {
                File file = db.getFiles().get(0);
                if (file.isDirectory()) {
                    sc.println("is Directory");
                    try ( Stream<Path> walk = Files.walk(Paths.get(file.getAbsolutePath()))) {
                        List<String> result = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
//                        List<String> result = walk.map(x -> x.toString())
//                                .filter(f -> f.endsWith(".mp3")).collect(Collectors.toList());
                        for (String s : result) {
                            list_musics.getItems().add(s);
                        }
                    } catch (Exception e) {
                    }
                } else {
                    sc.println("is not a directory");
                }
            } else {

            }
        }
    }
}
