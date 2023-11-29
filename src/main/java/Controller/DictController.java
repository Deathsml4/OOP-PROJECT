package Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DictController implements Initializable {
    @FXML
    private Tooltip tooltip1, tooltip2, tooltip3;

    @FXML
    private Button addWordBtn, GgAPIBtn, searchMenuBtn, gameBtn, exitBtn, minBtn;

    @FXML
    public AnchorPane container,anchor;
    public void setNode(Node node) {
        container.getChildren().clear();
        container.getChildren().add(node);
    }
    @FXML
    public void showComponent(String path) {
        try {
            AnchorPane component = FXMLLoader.load(getClass().getResource(path));
            setNode(component);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialize (URL url, ResourceBundle resourceBundle){
        showComponent("/FX/SearchUI.fxml");
    searchMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        showComponent("/FX/SearchUI.fxml");
      }
    });
    addWordBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        showComponent("/FX/AddWordUI.fxml");
      }
    });

    GgAPIBtn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        showComponent("/FX/GoogleTranslateUI.fxml");
      }
    });
    gameBtn.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {showComponent("/FX/GameMenuUI.fxml");}
    });
        tooltip1.setShowDelay(Duration.seconds(0.5));
        tooltip2.setShowDelay(Duration.seconds(0.5));
        tooltip3.setShowDelay(Duration.seconds(0.5));
        minBtn.setOnMouseClicked(e -> {
            Stage stage = (Stage) anchor.getScene().getWindow();
            stage.setIconified(true);
        });
        exitBtn.setOnMouseClicked(e -> {
            System.exit(0);
        });
    }
}
