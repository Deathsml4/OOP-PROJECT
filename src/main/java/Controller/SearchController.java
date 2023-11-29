package Controller;
import Alerts.Alerts;
import InsertApp.InsertApp;
import Trie.Trie;
import APInLib.EnTTSLib;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.voicerss.tts.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchController implements Initializable{
  @FXML
  private TextField searchTerm;

  @FXML
  private Button cancelBtn, saveBtn, delHistory;

  @FXML
  private Label englishWord, headerList, notAvailableAlert;

  @FXML
  private TextArea explanation;

  @FXML
  private ListView<String> listResults;

  @FXML
  private Pane headerOfExplanation;
  private Alerts alerts = new Alerts();
  private String selectedWord;
  private boolean searchCheck = false;
  private String firstWordList;
  private Trie trie = new Trie();
  private InsertApp app = new InsertApp();
  private EnTTSLib enTTS = new EnTTSLib();
  private ObservableList<String> list = FXCollections.observableArrayList();
  private ObservableList<String> memList = FXCollections.observableArrayList();
  private ObservableList<String> historyList = FXCollections.observableArrayList();
  private String htmlParse(String htmlString){
    String htmlStr="";
    if(htmlString.indexOf("<C><F><I><N><Q>")==-1){
      return htmlString;
    } else {
      int i;
      if(htmlString.indexOf("/<br />")!=-1){
        i = htmlString.indexOf("/");
      } else {
        i = htmlString.indexOf("br")+5;
      }
      htmlStr = htmlString.substring(i);
    }
      Document jsoupDoc = Jsoup.parse(htmlStr);
      Document.OutputSettings outputSettings = new Document.OutputSettings();
      outputSettings.prettyPrint(false);
      jsoupDoc.outputSettings(outputSettings);
      jsoupDoc.select("br").before("\\n");
      String str = jsoupDoc.html().replaceAll("\\\\n", "\n");
      String strWithNewLines = Jsoup.clean(str, "", Whitelist.none(), outputSettings);
      return strWithNewLines;
  }
  private void setHistory(){
    if(app.getHistory().isEmpty()) {
      headerList.setText("Danh sách từ vựng");
      delHistory.setVisible(false);
      historyList = trie.showAllWord();
      listResults.setItems(historyList);
      englishWord.setText(historyList.get(0));
      explanation.setText(htmlParse(app.wordCheck(historyList.get(0))));
    }
    else {
      headerList.setText("Lịch sử tìm kiếm");
      delHistory.setVisible(true);
      historyList = app.getHistory();
      FXCollections.reverse(historyList);
      listResults.setItems(historyList);
      englishWord.setText(historyList.get(0));
      explanation.setText(htmlParse(app.wordCheck(historyList.get(0))));
    }
  }
  @FXML
  private void handleOnClickDelHistory(){
    app.deleteAllHis();
    setHistory();
  }
  private void setListRes(boolean check) {
    if (!check) {
      setHistory();
    }
    else {
      headerList.setText("Kết quả liên quan");
      delHistory.setVisible(false);
      list.addAll(memList);
      listResults.setItems(list);
      englishWord.setText(firstWordList);
      explanation.setText(htmlParse(app.wordCheck(firstWordList)));
    }
  }
  @FXML
  private void handleOnKeyTyped() {
    list.clear();
    String searchKey = searchTerm.getText().trim();
    list = trie.search(searchKey);
    if (list.isEmpty()) {
      notAvailableAlert.setVisible(true);
      setListRes(searchCheck);
    } else {
      notAvailableAlert.setVisible(false);
      delHistory.setVisible(false);
      headerList.setText("Kết quả tìm kiếm");
      listResults.setItems(list);
      firstWordList = list.get(0);
      memList.clear();
      memList.addAll(list);
      searchCheck = true;
    }
  }
  public void initialize(URL url, ResourceBundle resourceBundle) {
    app.insertToTrie(trie);
    setListRes(false);
    searchTerm.setOnKeyTyped(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent keyEvent) {
        if (searchTerm.getText().isEmpty()) {
          cancelBtn.setVisible(false);
          searchCheck = false;
          setListRes(searchCheck);
        } else {
          cancelBtn.setVisible(true);
          handleOnKeyTyped();
        }
      }
    });
    cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        searchTerm.clear();
        notAvailableAlert.setVisible(false);
        cancelBtn.setVisible(false);
        searchCheck = false;
        setListRes(searchCheck);
      }
    });
    explanation.setEditable(false);
    saveBtn.setVisible(false);
    cancelBtn.setVisible(false);
    notAvailableAlert.setVisible(false);
  }
  @FXML
  private void handleMouseClickAWord(MouseEvent arg0) {
    selectedWord = listResults.getSelectionModel().getSelectedItem();
    if (selectedWord != null) {
      englishWord.setText(selectedWord);
      explanation.setText(htmlParse(app.wordCheck(selectedWord)));
      app.insertToHistory(selectedWord);
      headerOfExplanation.setVisible(true);
      explanation.setVisible(true);
      explanation.setEditable(false);
      saveBtn.setVisible(false);
    }
  }
  @FXML
  private void handleClickEditBtn() {
    explanation.setEditable(true);
    saveBtn.setVisible(true);
  }
  @FXML
  private void handleClickSoundBtn() {
    String text = englishWord.getText();
    enTTS.build(text);
    enTTS.executer();
  }
  @FXML
  private void handleClickSaveBtn() {
    Alert alertConfirmation = alerts.alertConfirmation("Xác nhận", "Bạn chắc chắn muốn cập nhật định nghĩa của từ vựng này?");
    Optional<ButtonType> option = alertConfirmation.showAndWait();
    if (option.get() == ButtonType.OK) {
      app.replace(englishWord.getText(), explanation.getText());
      alerts.showAlertInfo("Thông báo", "Cập nhập thành công!");
    } else if (option.get() == new ButtonType("Hủy")) {
        alerts.showAlertInfo("Thông báo", "Không có sự thay đổi nào!");
      }
    saveBtn.setVisible(false);
    explanation.setEditable(false);
  }

  private void refreshAfterDeleting() {
    for (int i = 0; i < list.size(); i++)
      if (list.get(i).equals(englishWord.getText())) {
        list.remove(i);
        break;
      }
    listResults.setItems(list);
    headerOfExplanation.setVisible(false);
    explanation.setVisible(false);
  }
  @FXML
  private void handleClickDeleteBtn() {
    Alert alertWarning = alerts.alertWarning("Cảnh báo", "Bạn chắc chắn muốn xóa từ vựng này?");
    alertWarning.getButtonTypes().add(new ButtonType("Hủy"));
    Optional<ButtonType> option = alertWarning.showAndWait();
    if (option.get() == ButtonType.OK) {
      app.delete(englishWord.getText());
      refreshAfterDeleting();
      trie.delete(englishWord.getText());
      alerts.showAlertInfo("Thông báo", "Xóa từ vựng thành công!");
    } else alerts.showAlertInfo("Thông báo", "Không có sự thay đổi nào!");
  }
}
