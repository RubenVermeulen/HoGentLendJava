package gui;

import domein.DomeinController;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainMenuFrameController extends BorderPane {

    private DomeinController domCon;

    @FXML
    private VBox materialenBox;

    public MainMenuFrameController(DomeinController domCon) {
        this.domCon = domCon;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuFrame.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        setupTemporaryDemoMaterials();

    }

    public void setupTemporaryDemoMaterials() {
        final int aantalDemoMaterials = 14;
        Node[] materialNodes = new Node[aantalDemoMaterials];
        for (int i = 0; i < aantalDemoMaterials; i++) {
            materialNodes[i] = new MateriaalBoxController(this, "Materiaal Naam " + i, "De beschrijving van het materiaal " + i);
        }
        materialenBox.getChildren().addAll(materialNodes);
    }

}
