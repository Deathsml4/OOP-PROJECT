package Controller;

import DictionaryCore.HangManCore;
import APInLib.TransEnViSwitch;
import Controller.DictController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HangManController implements Initializable{
    @FXML
    private Button hintBtn, replayBtn, transBtn;
    @FXML
    private Label textWord, endGameLb, hintLb;
    @FXML
    private AnchorPane ap;
    @FXML
    private Button btnA, btnB, btnC, btnD, btnE, btnF, btnG, btnH, btnI, btnJ, btnK, btnL, btnM,
            btnN, btnO, btnP, btnQ, btnR, btnS, btnT, btnU, btnV, btnW, btnX, btnY, btnZ;
    @FXML
    private Button can1, can2, can3, can4, can5, can6, can7, can8;
    @FXML
    private ImageView loading;
    private final double MAX_WORD_WIDTH = 745;
    private final double wordFontSize = 60;
    private final double transFontSize = 45;
    private final Font wordFont = Font.loadFont(getClass()
            .getResourceAsStream("/Font/MTO Astro City.ttf"), wordFontSize);
    private final Font transFont = Font.loadFont(getClass()
            .getResourceAsStream("/Font/MTO Astro City.ttf"), transFontSize);
    private final Font labelFont = Font.loadFont(getClass()
            .getResourceAsStream("/Font/MTO Astro City.ttf"), 38);
    private HangManCore hangMan = new HangManCore();
    private TransEnViSwitch trans = new TransEnViSwitch();
    public void initialize(URL url, ResourceBundle resourceBundle){
        replayBtn.setVisible(false);
        transBtn.setVisible(false);
        endGameLb.setVisible(false);
        loading.setVisible(false);
        hintLb.getStylesheets().add("/FX/Dress.css");
        hintLb.setId("hint-label");
        hangMan.init();
        endGameLb.setFont(labelFont);
        hintLb.setText(String.valueOf(hangMan.getSuggestsRemain()));
        fixWordFont(hangMan.displayCurrentWord(), wordFont);
        textWord.setText(hangMan.displayCurrentWord());
    }
    public void fixWordFont(String text, Font defont){
        Text tmpText = new Text(text);
        tmpText.setFont(defont);
        double textWidth = tmpText.getLayoutBounds().getWidth();
        if (textWidth <= MAX_WORD_WIDTH) {
            textWord.setFont(defont);
        } else {
            double newFontSize = wordFontSize * MAX_WORD_WIDTH / textWidth;
            textWord.setFont(Font.font(defont.getFamily(),newFontSize));
        }
    }
    public void loseScene(){
        endGameLb.setText("Vấn đề kĩ năng!");
        endGameLb.setStyle("-fx-text-fill: #b22a00;");
        endGameLb.setVisible(true);
        replayBtn.setVisible(true);
        transBtn.setVisible(true);
        hintBtn.setDisable(true);
        textWord.setText(hangMan.getAnswer());
        for (char c = 'A'; c <= 'Z'; c++) {
            String buttonId = "btn" + c;
            Button button = (Button) ap.lookup("#" + buttonId);
            button.setDisable(true);
        }
    }
    public void winScene(){
        endGameLb.setText("Quá đét!");
        endGameLb.setStyle("-fx-text-fill: #009432;");
        endGameLb.setVisible(true);
        replayBtn.setVisible(true);
        transBtn.setVisible(true);
        hintBtn.setDisable(true);
        for (char c = 'A'; c <= 'Z'; c++) {
            String buttonId = "btn" + c;
            Button button = (Button) ap.lookup("#" + buttonId);
            button.setDisable(true);
        }
    }
    public void flash(int in, int mode){
        String color1,color2;
        if(in==1) {color1 = "#009432";}
        else if(in==0) {color1 = "#b22a00";}
        else color1 = "#ffe700";
        if(mode == 0){
            color2 = color1;
        } else color2 = "#faf2c3";
        new Thread(()->{
            Timeline flash = new Timeline(
                    new KeyFrame(Duration.seconds(0.5), new KeyValue(textWord.textFillProperty(), Paint.valueOf(color1))),
                    new KeyFrame(Duration.seconds(1.0),new KeyValue(textWord.textFillProperty(), Paint.valueOf(color2))));
            flash.play();
        }).start();
    }
    @FXML
    public void handleButtonClick(ActionEvent event){
        Button clicked = (Button) event.getSource();
        if(!hangMan.guess(clicked.getText().charAt(0)).equals("")){
            fixWordFont(hangMan.guess(clicked.getText().charAt(0)), wordFont);
            textWord.setText(hangMan.guess(clicked.getText().charAt(0)));
            clicked.setStyle("-fx-effect: dropshadow(three-pass-box, rgb(153, 217, 140), 15, 0, 0, 0);");
            clicked.setDisable(true);
            if(hangMan.win()) {
                flash(1, 0);
                winScene();
            } else flash(1 , 1);
        } else {
            clicked.setStyle("-fx-effect: dropshadow(three-pass-box, rgb(178, 42, 0), 15, 0, 0, 0);");
            clicked.setDisable(true);
            Button can = (Button) ap.lookup("#can" + String.valueOf(hangMan.getLife()+1));
            can.setVisible(false);
            if(hangMan.getLife()==0) {
                loseScene();
                flash(0, 0);
            } else flash(0, 1);
        }
    }
    @FXML
    public void handleOnHintClick(){
        String ret = hangMan.suggest();
        hintLb.setText(String.valueOf(hangMan.getSuggestsRemain()));
        fixWordFont(ret, wordFont);
        textWord.setText(ret);
        Button button = (Button) ap.lookup("#btn" + hangMan.getHintWord());
        button.setStyle("-fx-effect: dropshadow(three-pass-box, rgb(153, 217, 140), 15, 0, 0, 0);");
        button.setDisable(true);
        if(hangMan.win()) {
            flash(1, 0);
            winScene();
        } else flash(2, 1);
        if(hangMan.getSuggestsRemain()==0) {
            hintBtn.setDisable(true);
            hintLb.setVisible(false);
        }
    }
    @FXML
    public void handleOnReplayClick(){
        for (char c = 'A'; c <= 'Z'; c++) {
            Button button = (Button) ap.lookup("#btn" + String.valueOf(c));
            button.setStyle("-fx-effect: dropshadow(three-pass-box, rgb(0,0,0,0.4), 5, 0, 0, 0);");
            button.setDisable(false);
        }
        for (int i = 1; i<= 8; i++) {
            Button button = (Button) ap.lookup("#can" + String.valueOf(i));
            button.setVisible(true);
        }
        hangMan.init();
        replayBtn.setVisible(false);
        transBtn.setVisible(false);
        hintBtn.setDisable(false);
        hintLb.setVisible(true);
        endGameLb.setVisible(false);
        hintLb.setText(String.valueOf(hangMan.getSuggestsRemain()));
        textWord.setStyle("-fx-text-fill: #faf2c3;");
        fixWordFont(hangMan.displayCurrentWord(), wordFont);
        textWord.setText(hangMan.displayCurrentWord());
    }
    @FXML
    public void handleOnTransClick(){
        trans.build(textWord.getText(), "vi", "en");
        loading.setVisible(true);
    new Thread(
            () -> {
              String ret = trans.executer();
              Platform.runLater(
                  new Runnable() {
                    @Override
                    public void run() {
                      fixWordFont(ret, transFont);
                      loading.setVisible(false);
                      if(hangMan.getLife()==0){
                          textWord.setStyle("-fx-text-fill: #b22a00;");
                      } else textWord.setStyle("-fx-text-fill: #009432;");
                      textWord.setText(textWord.getText() + "\n" + ret);
                    }
                  });
            })
        .start();
    }
}
