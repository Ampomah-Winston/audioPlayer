/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myAudioPlayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author WYNXTYN PROAUTOTYPE
 */
public class MyAudioPlayer_Controller implements Initializable {

    @FXML
    private JFXListView<ListItems> list_all_music;

    ObservableList<ListItems> real_items = null;
    private boolean isRandom;
    @FXML
    private MediaView mediaViewer;
    @FXML
    private Pane mediaViewer_Container;
    @FXML
    private JFXSlider seek_slider;
    @FXML
    private JFXButton btn_play;
    @FXML
    private JFXButton btn_stop;

    private ChangeListener<Duration> progressListener;
    @FXML
    private JFXSlider volumer_slider;

    int currentPosition = 0;
    int list_size = 0;
    @FXML
    private CheckBox chk_repeat;
    @FXML
    private CheckBox chk_shuffle;
    @FXML
    private ProgressBar progressLoader;

    @Override

    public void initialize(URL url, ResourceBundle rb) {

        progressListener = (observable, oldValue, newValue) -> {
            seek_slider.setValue(newValue.toSeconds());
        };

        //add seeking context to the slider
        seek_slider.valueProperty().addListener((observable) -> {
            if (seek_slider.isValueChanging()) {

                double dur = seek_slider.getValue() * 1000;
                mediaPlayer.seek(Duration.millis(dur));
            }
        });

        //add volume control through a listener
        volumer_slider.setMin(0);
        volumer_slider.setMax(10);
        volumer_slider.setValue(volumer_slider.getMax() / 2);
        volumer_slider.valueProperty().addListener((observable) -> {
            if (volumer_slider.isValueChanging()) {
                if (mediaPlayer != null) {
                    double actual_volume = volumer_slider.getValue() / 10;
                    mediaPlayer.volumeProperty().set(actual_volume);
                }
            }

        });

    }

    @FXML
    private void list_music_on_Drag_Over(DragEvent event) throws MalformedURLException {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.LINK);
//            String filer = db.getFiles().get(0).toURI().toURL().toString();
//            if (filer.endsWith(".mp3")) {//|| filer.endsWith(".jpg")
//               
//            }
        } else {
            return;
        }
    }

    @FXML
    private void list_music_on_Drag_Dropped(DragEvent event) throws MalformedURLException {
        Dragboard db = event.getDragboard();
        //String image = getClass().getResource("headphones.png").toExternalForm();
        String image = "/headphones.png";
        if (list_all_music.getItems().size() > 0) {
            if (mediaPlayer != null) {
                if (mediaPlayer.getStatus() == Status.PLAYING) {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                    list_all_music.getItems().clear();
                }
            }
        }
        if (db.hasFiles()) {
            if (db.getFiles().size() > 0) {
                File file = db.getFiles().get(0);
                if (file.isDirectory()) {
                    File[] fileList = file.listFiles();
                    Service<ObservableList<ListItems>> server = new loading_mp3_service(fileList);

                    progressLoader.progressProperty().bind(server.progressProperty());
                    progressLoader.visibleProperty().bind(server.stateProperty().isEqualTo(State.RUNNING));
                    server.setOnRunning(e -> {
//                        list_all_music.getItems().add(server.get);
                    });
                    server.setOnSucceeded(e -> {
                        list_all_music.setItems(server.getValue());
                    });
                    server.start();

                } else {
                    String OtherFile = file.getAbsolutePath();
                    playMedia(OtherFile);
                }
            }
        } else {
            System.out.println(image + "else 2");
        }

    }

    MediaPlayer mediaPlayer = null;

    private void playMedia(String filepath) throws MalformedURLException {

        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.setOnPaused(null);
            mediaPlayer.setOnPlaying(null);
            mediaPlayer.setOnReady(null);
            mediaPlayer.setAudioSpectrumListener(null);
        }
        File file = new File(filepath);
        filepath = file.toURI().toURL().toString();
        Media media = new Media(filepath);

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(volumer_slider.getValue());
        mediaViewer.setMediaPlayer(mediaPlayer);

        mediaPlayer.currentTimeProperty().addListener(progressListener);

        mediaPlayer.setOnReady(() -> {
            seek_slider.setValue(0);
            seek_slider.setMax(mediaPlayer.getMedia().getDuration().toSeconds());
            mediaPlayer.setVolume(volumer_slider.getValue());
            mediaPlayer.play();

        });

        Random rn = new Random(list_size);
        mediaPlayer.setOnEndOfMedia(() -> {
            if (!isRandom) {
                if (currentPosition == list_size) {
                    currentPosition = 0;
                    String location = list_all_music.getItems().get(currentPosition).getLocation();
                    try {
                        playMedia(location);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(MyAudioPlayer_Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    currentPosition++;
                    String location = list_all_music.getItems().get(currentPosition).getLocation();
                    try {
                        playMedia(location);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(MyAudioPlayer_Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                currentPosition = new Random().nextInt(list_size);
                String location = list_all_music.getItems().get(currentPosition).getLocation();
                try {
                    playMedia(location);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(MyAudioPlayer_Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // setup visualization (circle container)
//        mediaPlayer.setAudioSpectrumListener((timestamp, duration, magnitudes, phases) -> {
//            // mediaView_Container.getChildren().clear();
//            int i = 0;
//            //int x = 10;
//            double y = mediaViewer.getFitHeight() / 3;
//            double x = mediaViewer.getFitWidth() / 4;
//            
//            Random rand = new Random(System.currentTimeMillis());
//            // Build random colored circles
//            for (float phase : phases) {
//                int red = rand.nextInt(255);
//                int green = rand.nextInt(255);
//                int blue = rand.nextInt(255);
//                Circle circle = new Circle(10);
//                
//                circle.setCenterX(x + i);
//                circle.setCenterY(y + (phase * 100));
//                circle.setFill(Color.rgb(red, green, blue, .70));
//                mediaViewer_Container.getChildren().add(circle);
//                i += 3;
//                fadeOut(mediaViewer_Container, circle, i);
//            }
//        });
    }

    private void fadeOut(Pane mediaViewer_Container, Circle circle, int seconds) {
        Duration duration = Duration.seconds(seconds);

        if (duration.greaterThanOrEqualTo(Duration.seconds(20))) {

            if (mediaViewer_Container.getChildren().size() >= 100) {
                mediaViewer_Container.getChildren().remove(1, 29);
            }
            // System.out.println(container.getChildren().size());
        }
    }

    @FXML
    private void list_music_mouse_clicked(MouseEvent event) throws MalformedURLException {
        if (event.getClickCount() == 2) {
            currentPosition = list_all_music.getSelectionModel().getSelectedIndex();
            if (list_all_music.getSelectionModel().isSelected(currentPosition)) {
                String location = list_all_music.getItems().get(currentPosition).getLocation();
                String fileLocation = new File(location).getAbsolutePath();
                btn_play.setText("Pause");
                playMedia(fileLocation);
            }
        }
    }

    @FXML
    private void play_pause(ActionEvent event) {
        if (mediaPlayer != null) {
            if (btn_play.getText().contentEquals("Play")) {
                if (list_all_music.getItems().size() > 0) {
                    mediaPlayer.play();
                    btn_play.setText("Pause");
                }

            } else {
                mediaPlayer.pause();
                btn_play.setText("Play");
            }
        }
    }

    @FXML
    private void stop(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            btn_play.setText("Play");
        }
    }

    @FXML
    private void seek_slider_on_mouse_drag_Released(MouseEvent event) {
//        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
//            mediaPlayer.pause();
//        }
    }

    @FXML
    private void seek_slider_on_mouse_drag_Pressed(MouseEvent event) {
//        if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
//            mediaPlayer.play();
//        }
    }

    @FXML
    private void shuffling(ActionEvent event) {
        if (isRandom == false) {
            isRandom = true;
        } else {
            isRandom = false;
        }
    }

    @FXML
    private void repeat(ActionEvent event) {

    }

    @FXML
    private void media_viewer_on_mouse_click(MouseEvent event) {
        Scene theScene = null;
        MediaView thisMediaView = new MediaView();
        thisMediaView.setMediaPlayer(mediaViewer.getMediaPlayer());
//        thisMediaView.getMediaPlayer().seek(mediaViewer.getMediaPlayer().getCurrentTime());
        Pane pane = new Pane(thisMediaView);
        pane.setPrefHeight(mediaViewer.getFitHeight());
        pane.setPrefWidth(mediaViewer.getFitWidth());
        pane.widthProperty().add(thisMediaView.fitWidthProperty());
        pane.heightProperty().add(thisMediaView.fitHeightProperty());
        
        
        theScene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(theScene);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.show();
    }
}
