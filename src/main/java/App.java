import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sun.glass.ui.Window;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static com.sun.jna.platform.win32.WinUser.GWL_STYLE;

public class App implements Initializable {
    @FXML
    AnchorPane ap;
    private double x = 0;
    private double y = 0;
    public void initialize(URL url, ResourceBundle rb) {
        ExecutorService threadpool = Executors.newCachedThreadPool();
        ListeningExecutorService service = MoreExecutors.listeningDecorator(threadpool);
        service.submit(()-> {
            try {
                Thread.sleep(3000);
                Platform.runLater(()-> {
                    Stage stage = new Stage();
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("/FX/DictUI.fxml"));
                    } catch (IOException ex) {
                        Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
                    }
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
                    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                    stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                    stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                    ap.getScene().getWindow().hide();
                    stage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
                        long lhwnd = Window.getWindows().get(0).getNativeWindow();
                        Pointer lpVoid = new Pointer(lhwnd);
                        HWND hwnd = new HWND(lpVoid);
                        final User32 user32 = User32.INSTANCE;
                        int oldStyle = user32.GetWindowLong(hwnd, GWL_STYLE);
                        @Override
                        public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                            if (t1.booleanValue() == true) {
                                int newStyle = oldStyle | 0x00020000 | 0x00C00000;
                                user32.SetWindowLong(hwnd, GWL_STYLE, newStyle);
                            } else if (t1.booleanValue() == false) {
                                int newStyle = oldStyle | 0x00020000;
                                user32.SetWindowLong(hwnd, GWL_STYLE, newStyle);
                            }
                        }
                    });
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
