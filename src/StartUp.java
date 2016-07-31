
import domein.DomeinController;
import gui.LoginFrameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * 
 * TODO:
 * .png en .jpg bestanden voor image select
 * .csv bstanden voor bulk
 * aantal mag niet negatief zijn bij toevoegen van het materiaal reservatie
 * aantal onbeschikbaar geeft geen conflicten
 * filter voor alle conflicten te kunnen zien
 * 
 */

public class StartUp extends Application {// test xd

    @Override
    public void start(Stage stage) {
        DomeinController domCont = new DomeinController();
        
        domCont.setPimaryStage(stage);
        
        Scene scene = new Scene(new LoginFrameController(domCont));
        scene.getStylesheets().add("/gui/styles.css");
        stage.setScene(scene);
        stage.setTitle("HoGentLend");
        stage.getIcons().add(new Image("/images/HoGentLendIcon.png"));
        stage.show();
    }

    public static void main(String... args) {
        Application.launch(StartUp.class, args);
    }
}
