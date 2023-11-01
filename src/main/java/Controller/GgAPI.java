package Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import java.net.URLEncoder;

public class GgAPI implements Initializable{
  private String langFrom = "en";
  private String langTo = "vi";
  private boolean isLangToVi = true;
  @FXML
  private TextArea langFromField, langToField;

  @FXML
  private Button translateBtn;

  @FXML
  private Label enLabel, viLabel;
  @FXML
  private void handleOnClickTranslateBtn() throws IOException {
      String text = langFromField.getText();
      String urlStr = "https://script.google.com/macros/s/AKfycbzhA3frpIsUaSJaamXOFu84mtT194zjgvllttWHBij2et_ixDAlK8TIVvIFw0sjIcgePA/exec" +
          "?q=" + URLEncoder.encode(text, "UTF-8") +
          "&target=" + langTo +
          "&source=" + langFrom;
      URL url = new URL(urlStr);
      StringBuilder response = new StringBuilder();
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestProperty("User-Agent", "Mozilla/5.0");
      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      langToField.setText(response.toString());
  }
  public void initialize(URL url, ResourceBundle resourceBundle) {
    langFromField.setOnKeyTyped(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent keyEvent) {
        if (langFromField.getText().trim().isEmpty()) translateBtn.setDisable(true);
        else translateBtn.setDisable(false);
      }
    });
    translateBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        try {
          handleOnClickTranslateBtn();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    translateBtn.setDisable(true);
    langToField.setEditable(false);
  }
  @FXML
  private void handleOnClickSwitchToggle() {
    langFromField.clear();
    langToField.clear();
    if (isLangToVi) {
      enLabel.setLayoutX(479);
      viLabel.setLayoutX(109);
      langFrom = "vi";
      langTo = "en";
    } else {
      enLabel.setLayoutX(130);
      viLabel.setLayoutX(460.5);
      langFrom = "en";
      langTo = "vi";
    }
    isLangToVi = !isLangToVi;
  }
}
