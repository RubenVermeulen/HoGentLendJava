package gui;

import domein.DomeinController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class MainMenuFrameController extends GridPane {

    private DomeinController domCon;
    
    public MainMenuFrameController(DomeinController domCon){
        this.domCon = domCon;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuFrame.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @FXML
    private void onVoegMateriaalToeAction(ActionEvent event) {
        
    }
}
