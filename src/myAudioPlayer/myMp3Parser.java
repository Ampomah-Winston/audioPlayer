/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myAudioPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author WYNXTYN PROAUTOTYPE
 */
public class myMp3Parser {

    String path;

    myMp3Parser(String path) {
        this.path = path;
    }

    public Metadata parseThis() {
        Metadata metadata = new Metadata();
        try {
            InputStream input = new FileInputStream(new File(path));
            ContentHandler handler = new DefaultHandler();            
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();

        } catch (Exception e) {
        }
        return metadata;
    }
}
