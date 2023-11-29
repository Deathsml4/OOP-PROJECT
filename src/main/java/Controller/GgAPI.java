package Controller;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.voicerss.tts.AudioCodec;
import com.voicerss.tts.AudioFormat;
import com.voicerss.tts.Languages;
import com.voicerss.tts.VoiceParameters;
import com.voicerss.tts.VoiceProvider;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import APInLib.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.sound.sampled.*;


public class GgAPI implements Initializable{
    private String langFrom = "en";
    private String langTo = "vi";
    private boolean isLangToVi = true;
    @FXML
    private TextArea langFromField, langToField;

    @FXML
    private Button translateBtn, soundBtn1, soundBtn2;

    @FXML
    private Label enLabel, viLabel;
    @FXML
    private AnchorPane frame;
    @FXML
    private ImageView loading;
    private TransEnViSwitch trans = new TransEnViSwitch();
    private EnTTSLib enTTS = new EnTTSLib();
    private VieTTSAPI vieTTS = new VieTTSAPI();
    @FXML
    public void handleOnClickTranslateBtn(){
        ExecutorService threadpool = Executors.newCachedThreadPool();
        ListeningExecutorService service = MoreExecutors.listeningDecorator(threadpool);
        service.submit(()-> {
        String text = langFromField.getText();
        trans.build(text, langTo, langFrom);
        langToField.setText(trans.executer());
        loading.setVisible(false);
        if(isLangToVi) soundBtn2.setDisable(false);
        else soundBtn1.setDisable(false);
        });
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loading.setVisible(false);
        langFromField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (langFromField.getText().trim().isEmpty()) {
                    translateBtn.setDisable(true);
                    soundBtn1.setDisable(true);
                    langToField.setText("");
                    soundBtn2.setDisable(true);
                }
                else {
                    translateBtn.setDisable(false);
                    if(isLangToVi) soundBtn1.setDisable(false);
                    else soundBtn2.setDisable(false);
                }
            }
        });
        translateBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loading.setVisible(true);
                handleOnClickTranslateBtn();
            }
        });
        translateBtn.setDisable(true);
        soundBtn1.setDisable(true);
        soundBtn2.setDisable(true);
        langToField.setEditable(false);
    }
    @FXML
    private void handleOnClickSwitchToggle() {
        langFromField.clear();
        langToField.clear();
        soundBtn1.setDisable(true);
        soundBtn2.setDisable(true);
        translateBtn.setDisable(true);
        if (isLangToVi) {
            enLabel.setLayoutX(479);
            viLabel.setLayoutX(109);
            soundBtn1.setLayoutX(365.5);
            soundBtn2.setLayoutX(14);
            langFrom = "vi";
            langTo = "en";
        } else {
            enLabel.setLayoutX(130);
            viLabel.setLayoutX(460.5);
            soundBtn1.setLayoutX(14);
            soundBtn2.setLayoutX(365.5);
            langFrom = "en";
            langTo = "vi";
        }
        isLangToVi = !isLangToVi;
    }
    @FXML
    private void handleClickSoundBtn1(){
        String text;
        if(isLangToVi) {text = langFromField.getText();}
        else text = langToField.getText();
        enTTS.build(text);
        enTTS.executer();
    }
    @FXML
    public void handleClickSoundBtn2(){
        String text;
        if(isLangToVi) {text = langToField.getText();}
        else text = langFromField.getText();
        vieTTS.build(text);
        vieTTS.executer();
    }
}
