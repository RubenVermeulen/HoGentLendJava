import domein.DomeinController;
import gui.LoginFrameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartUp extends Application {

    @Override
    public void start(Stage stage) {
        DomeinController domCont = new DomeinController();
        Scene scene = new Scene(new LoginFrameController(domCont));
        scene.getStylesheets().add("/gui/styles.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String... args) {
        Application.launch(StartUp.class, args);
    }
}
