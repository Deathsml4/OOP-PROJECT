package Alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;

public class Alerts {
    public void style(Alert alert){
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/FX/Dialog.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("/Sauce/5373110.png"));
        stage.initStyle(StageStyle.UNIFIED);
    }
    public void showAlertInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.setGraphic(new ImageView("/Sauce/information.png"));
        style(alert);
        alert.showAndWait();
    }
    public Alert alertConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.setGraphic(new ImageView("/Sauce/help.png"));
        style(alert);
        return alert;
    }
    public Alert alertWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.setGraphic(new ImageView("/Sauce/attention.png"));
        style(alert);
        return alert;
    }
}
