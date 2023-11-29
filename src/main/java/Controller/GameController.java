package Controller;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import APInLib.TransEnViSwitch;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import DictionaryCore.GameCore;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static javafx.util.Duration.seconds;

public class GameController implements Initializable{
    @FXML
    private Pane timer, quiz;
    @FXML
    private Button playBtn, nextBtn, optionA, optionB, optionC, optionD, transBtn;
    @FXML
    private Label question, scoreLb, attempLb, endGameLb;
    @FXML
    private ImageView refresh, play, loading;
    private GameCore game = new GameCore();
    private static final Integer STARTTIME = 25;
    private IntegerProperty timeSeconds =
            new SimpleIntegerProperty(STARTTIME*100);
    private boolean act = true;
    private boolean newScoreCheck = false;
    String ret1,ret2,ret3,ret4,ret5;
    private final double MAX_QUEST_WIDTH = 708;
    private final double questFontSize = 14;
    private final Font questFont = Font.font("System", FontWeight.BOLD, questFontSize);
    private TransEnViSwitch trans1 = new TransEnViSwitch();
    private TransEnViSwitch trans2 = new TransEnViSwitch();
    private TransEnViSwitch trans3 = new TransEnViSwitch();
    private TransEnViSwitch trans4 = new TransEnViSwitch();
    private TransEnViSwitch trans5 = new TransEnViSwitch();
    private ExecutorService threadpool = Executors.newCachedThreadPool();
    private ListeningExecutorService service = MoreExecutors.listeningDecorator(threadpool);
  private Timeline timeline = new Timeline();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        quiz.setVisible(false);
        nextBtn.setDisable(true);
        endGameLb.setVisible(false);
        game.setHighScore(game.getHigh());
        scoreLb.setText(String.format(" Điểm cao: %d", game.getHighScore()));
        attempLb.setVisible(false);
        loading.setVisible(false);
        transBtn.setVisible(false);
        timer.getStylesheets().add("/FX/Dress.css");
        optionA.getStylesheets().add("/FX/Dress.css");
        optionB.getStylesheets().add("/FX/Dress.css");
        optionC.getStylesheets().add("/FX/Dress.css");
        optionD.getStylesheets().add("/FX/Dress.css");
        scoreLb.getStylesheets().add("/FX/Dress.css");
        attempLb.getStylesheets().add("/FX/Dress.css");
        scoreLb.setId("score-label");
        attempLb.setId("score-label");
        timer.setId("game-timer");
        transBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    handleOnClickTrans();
            }
        });
        optionA.setOnAction(
        new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent actionEvent) {
            if (act) {
                question.setText(updateQuestion());
                if (game.choose('a')) updateScore();
                else updateAttemp();
                if(game.getAttemp()>0) nextBtn.setDisable(false);
            }
            handleOnClickChoice();
          }
        });
        optionB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (act) {
                    question.setText(updateQuestion());
                    if (game.choose('b')) updateScore();
                    else updateAttemp();
                    if(game.getAttemp()>0) nextBtn.setDisable(false);
                }
                handleOnClickChoice();
            }
        });
        optionC.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (act) {
                    question.setText(updateQuestion());
                    if (game.choose('c')) updateScore();
                    else updateAttemp();
                    if(game.getAttemp()>0) nextBtn.setDisable(false);
                }
                handleOnClickChoice();
            }
        });
        optionD.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (act) {
                    question.setText(updateQuestion());
                    if (game.choose('d')) updateScore();
                    else updateAttemp();
                    if(game.getAttemp()>0) nextBtn.setDisable(false);
                }
                handleOnClickChoice();
            }
        });
    }
    public void timerAct(int in ){
        if(in==1){
            service.submit(
                    () -> {
                        act = true;
                        timer.setId("game-timer");
                        timeSeconds.set((STARTTIME)*100);
                        timer.prefWidthProperty().bind(timeSeconds.multiply(732.5).divide(STARTTIME*100));
                        timeline.getKeyFrames().add(new KeyFrame(seconds(STARTTIME), new KeyValue(timeSeconds,0)));
                        timeline.playFromStart();
                        timeline.setOnFinished(event -> {
                            question.setText(updateQuestion());
                            handleOnClickChoice();
                            updateAttemp();
                        });
                        while (act) {
                            if (timer.getPrefWidth() <= 732.5 * 0.3) {
                                timer.setId("game-timer-last");
                                break;
                            }
                        }
                    });
        }
        else {
            act = false;
            timeline.stop();
        }
    }
    public void startQuiz(){
        game.fullCheck();
        game.initQuestion();
        optionA.setId("game-word-button");
        optionB.setId("game-word-button");
        optionC.setId("game-word-button");
        optionD.setId("game-word-button");
        optionA.setText(game.getOption_a());
        optionB.setText(game.getOption_b());
        optionC.setText(game.getOption_c());
        optionD.setText(game.getOption_d());
        question.setFont(questFont);
        question.setText(game.getQuestion());
        transBtn.setDisable(true);
        timerAct(1);
    }

    public void updateScore(){
        game.setScore();
        scoreLb.setText(String.format(" Điểm: %d", game.getScore()));
           /* scoreLb.setStyle("-fx-background-color:  #CAF7D8");
            scoreLb.setStyle("-fx-text-fill: #009432");*/
        new Thread(()->{
        Timeline flash = new Timeline(
                new KeyFrame(Duration.seconds(0.5), new KeyValue(scoreLb.textFillProperty(), Paint.valueOf("#009432FF"))),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(question.textFillProperty(), Paint.valueOf("#009432FF"))),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(scoreLb.idProperty(), "win-label" )),
                new KeyFrame(Duration.seconds(1.0),new KeyValue(scoreLb.textFillProperty(), Paint.valueOf("#257273"))),
                new KeyFrame(Duration.seconds(1.0),new KeyValue(question.textFillProperty(), Paint.valueOf("#ecf0f1"))),
                new KeyFrame(Duration.seconds(1.0), new KeyValue(scoreLb.idProperty(), "score-label" )));
        flash.play();
        }).start();
        newScoreCheck =game.updateHighScore();
    }
    public void updateAttemp(){
        game.setAttemp();
        attempLb.setText(String.format(" Lần thử:  %d", game.getAttemp()));
        new Thread(()->{
        Timeline flashred = new Timeline(
                new KeyFrame(Duration.seconds(0.5), new KeyValue(attempLb.textFillProperty(), Paint.valueOf("#b22a00"))),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(question.textFillProperty(), Paint.valueOf("#b22a00"))),
                new KeyFrame(Duration.seconds(0.1), new KeyValue(attempLb.idProperty(), "lose-label" )),
                new KeyFrame(Duration.seconds(1.0),new KeyValue(attempLb.textFillProperty(), Paint.valueOf("#257273"))),
                new KeyFrame(Duration.seconds(1.0),new KeyValue(question.textFillProperty(), Paint.valueOf("#ecf0f1"))),
                new KeyFrame(Duration.seconds(1.0), new KeyValue(attempLb.idProperty(), "score-label" )));
        flashred.play();
        }).start();
        if(game.getAttemp()==0){gameOver();}
    }
    public String updateQuestion(){
        String ret;
        if(game.getQuestion().indexOf("________")!=-1){
            ret = game.getQuestion().replace("________",game.getAnswer().toLowerCase());
        } else if(game.getQuestion().indexOf("______")!=0) ret = game.getQuestion().replace("______", game.getAnswer().toLowerCase());
        else ret = game.getQuestion().replace("______", game.getAnswer());
        return ret;
    }
    public String capitalize(String s){
        return s.substring(0,1).toUpperCase()+s.substring(1);
    }
    public void handleOnClickPlay(){
        game.resetStats();
        scoreLb.setPrefWidth(62);
        scoreLb.setText(String.format(" Điểm: %d", game.getScore()));
        attempLb.setText(String.format(" Lần thử:  %d", game.getAttemp()));
        endGameLb.setVisible(false);
        startQuiz();
        playBtn.setVisible(false);
        nextBtn.setDisable(false);
        attempLb.setVisible(true);
        quiz.setVisible(true);
        transBtn.setVisible(true);
    }
    public void handleOnClickNext(){
        if(act) {
            game.setAttemp();
            attempLb.setText(String.format(" Lần thử:  %d", game.getAttemp()));
            new Thread(()->{
                Timeline flashred = new Timeline(
                        new KeyFrame(Duration.seconds(0.5), new KeyValue(attempLb.textFillProperty(), Paint.valueOf("#b22a00"))),
                        new KeyFrame(Duration.seconds(0.1), new KeyValue(attempLb.idProperty(), "lose-label" )),
                        new KeyFrame(Duration.seconds(1.0),new KeyValue(attempLb.textFillProperty(), Paint.valueOf("#257273"))),
                        new KeyFrame(Duration.seconds(1.0), new KeyValue(attempLb.idProperty(), "score-label" )));
                flashred.play();
            }).start();
        }
        if(game.getAttemp()==1) nextBtn.setDisable(true);
        startQuiz();
    }
    public void setChoiceShadow(Button btn, char c){
        if(game.choose(c)){
            btn.setId("game-word-button-true");
        }
        else{
            btn.setId("game-word-button-false");
        }
    }
    public void handleOnClickChoice(){
        setChoiceShadow(optionA, 'a');
        setChoiceShadow(optionB, 'b');
        setChoiceShadow(optionC, 'c');
        setChoiceShadow(optionD, 'd');
        timerAct(0);
        transBtn.setDisable(false);
    }
    public void gameOver(){
        refresh = new ImageView("/Sauce/refresh-2-64.png");
        refresh.setFitHeight(19);
        refresh.setFitWidth(18);
        playBtn.setGraphic(refresh);
        playBtn.setVisible(true);
        endGameLb.getStylesheets().add("/FX/Dress.css");
        if (!newScoreCheck) {
        endGameLb.setText("Vấn đề kĩ năng! Thử lại nào!");
        endGameLb.setPrefWidth(185);
        endGameLb.setLayoutX(328.6);
        endGameLb.setId("lose-label");
        endGameLb.setVisible(true);
            }
        else{
            endGameLb.setText("Quá đét! Kỷ lục mới!");
            endGameLb.setPrefWidth(145);
            endGameLb.setLayoutX(348.999);
            endGameLb.setId("win-label");
            endGameLb.setVisible(true);
        }
    }
    public void handleOnClickQuit(){
        play = new ImageView("/Sauce/Daco_4131967.png");
        play.setFitWidth(15);
        play.setFitHeight(18);
        playBtn.setGraphic(play);
        quiz.setVisible(false);
        nextBtn.setDisable(true);
        endGameLb.setVisible(false);
        scoreLb.setText(String.format(" Điểm cao:  %d", game.getHighScore()));
        attempLb.setVisible(false);
        timer.setId("game-timer");
    }
    public void fixQuestFont(String text){
            Text tmpText = new Text(text);
            tmpText.setFont(questFont);
            double textWidth = tmpText.getLayoutBounds().getWidth();
            if (textWidth <= MAX_QUEST_WIDTH) {
                question.setFont(questFont);
            } else {
                double newFontSize = questFontSize * MAX_QUEST_WIDTH / textWidth;
                question.setFont(Font.font("System", FontWeight.BOLD ,newFontSize));
            }
    }
    @FXML
    public void handleOnClickTrans(){
        trans1.build(question.getText(), "vi", "en");
        trans2.build(optionA.getText(), "vi", "en");
        trans3.build(optionB.getText(), "vi", "en");
        trans4.build(optionC.getText(), "vi", "en");
        trans5.build(optionD.getText(), "vi", "en");
        service.submit(
        () -> {
          loading.setVisible(true);
          ret1 = trans1.executer();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(
              new Runnable() {
                @Override
                public void run() {
                    fixQuestFont(ret1);
                    question.setText(question.getText() + "\n" + capitalize(ret1));
                    optionA.setText(optionA.getText() + "\n" + capitalize(ret2));
                    optionB.setText(optionB.getText() + "\n" + capitalize(ret3));
                    optionC.setText(optionC.getText() + "\n" + capitalize(ret4));
                    optionD.setText(optionD.getText() + "\n" + capitalize(ret5));
                }
              });
          loading.setVisible(false);
        });
        service.submit(()->{
            ret2 = trans2.executer();
        });
        service.submit(()->{
            ret3 = trans3.executer();
        });
        service.submit(()->{
            ret4 = trans4.executer();
        });
        service.submit(()->{
            ret5 = trans5.executer();
        });
    }
}
