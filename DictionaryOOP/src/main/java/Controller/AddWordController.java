/*package Controller;

import Alerts.Alerts;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddWordController implements Initializable{
  @FXML
  private Button addBtn;

  @FXML
  private TextField wordInput;

  @FXML
  private TextArea explanationInput;

  @FXML
  private Label successAlert;
  private Alerts alerts = new Alerts();
  private void resetInput() {
    wordInput.setText("");
    explanationInput.setText("");
  }

  private void showSuccessAlert() {
    successAlert.setVisible(true);
    dictionaryManagement.setTimeout(() -> successAlert.setVisible(false), 1500);
  }
  public void initialize(URL url, ResourceBundle resourceBundle) {
    dictionaryManagement.insertFromFile(dictionary, path);
    if (explanationInput.getText().isEmpty() || wordInput.getText().isEmpty()) addBtn.setDisable(true);

    wordInput.setOnKeyTyped(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent keyEvent) {
        if (explanationInput.getText().isEmpty() || wordInput.getText().isEmpty()) addBtn.setDisable(true);
        else addBtn.setDisable(false);
      }
    });

    explanationInput.setOnKeyTyped(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent keyEvent) {
        if (explanationInput.getText().isEmpty() || wordInput.getText().isEmpty()) addBtn.setDisable(true);
        else addBtn.setDisable(false);
      }
    });

    successAlert.setVisible(false);
  }
  @FXML
  private void handleClickAddBtn() {
    Alert alertConfirmation = alerts.alertConfirmation("Xác nhận", "Bạn chắc chắn muốn thêm từ vựng này?");
    Optional<ButtonType> option = alertConfirmation.showAndWait();
    String englishWord = wordInput.getText().trim();
    String meaning = explanationInput.getText().trim();

    if (option.get() == ButtonType.OK) {
      Word word = new Word(englishWord, meaning);
      if (dictionary.contains(word)) {
        int indexOfWord = dictionaryManagement.searchWord(dictionary, englishWord);
        Alert selectionAlert = alerts.alertConfirmation("Oops!", "Từ này đã tồn tại.\nBạn có muốn thay thế hoặc bổ sung định nghĩa vừa nhập cho định nghĩa cũ không?");
        selectionAlert.getButtonTypes().clear();
        ButtonType replaceBtn = new ButtonType("Thay thế");
        ButtonType insertBtn = new ButtonType("Bổ sung");
        ButtonType cancelBtn = new ButtonType("Hủy");
        selectionAlert.getButtonTypes().addAll(replaceBtn, insertBtn, cancelBtn);
        Optional<ButtonType> selection = selectionAlert.showAndWait();

        if (selection.get() == replaceBtn) {
          dictionary.get(indexOfWord).setWordExplain(meaning);
          dictionaryManagement.exportToFile(dictionary, path);
          showSuccessAlert();
        }
        if (selection.get() == insertBtn) {
          String oldMeaning = dictionary.get(indexOfWord).getWordExplain();
          dictionary.get(indexOfWord).setWordExplain(oldMeaning + "\n= " + meaning);
          dictionaryManagement.exportToFile(dictionary, path);
          showSuccessAlert();
        }
        if (selection.get() == cancelBtn) alerts.showAlertInfo("Thông báo", "Không có sự thay đổi nào!");
      } else {
        dictionary.add(word);
        dictionaryManagement.addWord(word, path);
        showSuccessAlert();
      }
      addBtn.setDisable(true);
      resetInput();
    } else if (option.get() == new ButtonType("Hủy")) alerts.showAlertInfo("Thông báo", "Không có sự thay đổi nào!");
  }
}
*/