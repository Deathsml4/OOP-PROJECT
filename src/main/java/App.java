import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class App extends Application {
  private double x = 0;
  private double y = 0;
  @Override
  public void start(final Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/FX/DictUI.fxml"));
    stage.setTitle(("Dictionary by @deathsml"));
    stage.initStyle(StageStyle.TRANSPARENT);
    root.setOnMousePressed(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
      }
    });

    root.setOnMouseDragged(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
      }
    });
    Scene scene = new Scene(root);
    scene.setFill(Color.TRANSPARENT);
    stage.setScene(scene);
    stage.getIcons().add(new Image("/Sauce/5373110.png"));
    stage.show();
  }
  public static void main(String[] args) {launch(args);}
}
