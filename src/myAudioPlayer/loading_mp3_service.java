/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myAudioPlayer;

import java.io.File;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.tika.metadata.Metadata;

/**
 *
 * @author WYNXTYN PROAUTOTYPE
 */
public class loading_mp3_service extends Service<ObservableList<ListItems>> {

    File[] fileList = null;
    ObservableList<ListItems> lastValue = FXCollections.observableArrayList();

    loading_mp3_service(File[] fileList) {
        this.fileList = fileList;
    }

    @Override
    protected Task<ObservableList<ListItems>> createTask() {
        return new Task<ObservableList<ListItems>>() {
            @Override
            protected ObservableList<ListItems> call() throws Exception {
                int i = 0;
                for (File filer : fileList) {
                    String onlyMp3 = filer.getAbsolutePath();
                    if (onlyMp3.endsWith(".mp3")) {
                        updateProgress(i + 1, fileList.length);
                        i++;
                        ListItems e = null;
                        myMp3Parser mp3Parser = new myMp3Parser(filer.getAbsolutePath());
                        Metadata mData = mp3Parser.parseThis();
                        e = new ListItems("", "Artist: " + mData.get("xmpDM:artist"), "Title: " + mData.get("title"), "Album: " + mData.get("xmpDM:album"), "Genre: " + mData.get("xmpDM:genre"), "Format: " + mData.get("xmpDM:audioCompressor"), filer.getAbsolutePath());

                        String hex = String.format("#%02x%02x%02x", new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
                        e.setStyle("-fx-background-color:" + hex + ";");
                       
                        lastValue.add(e);
                        updateValue(lastValue);
                        Thread.sleep(200);
                    }
                }
                return lastValue;
            }
        };
    }

}
