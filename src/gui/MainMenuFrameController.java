package gui;

import domein.DomeinController;
import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import shared.MateriaalView;

public class MainMenuFrameController extends BorderPane {

    private DomeinController domCon;

    @FXML
    private VBox materialenBox;
    @FXML
    private Label lblWelkomInfo;
    @FXML
    private Label lblEmailInfo;

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

        setupWelkomEnEmailLabels();

        TEMPORARY_setupTemporaryDemoMaterials();
    }

    public void TEMPORARY_setupTemporaryDemoMaterials() {
        final int aantalDemoMaterials = 14;
        Node[] materialNodes = new Node[aantalDemoMaterials];
        for (int i = 0; i < aantalDemoMaterials; i++) {
            materialNodes[i] = new MateriaalBoxController(this, new MateriaalView("Materiaal Naam " + i, i));
        }
        materialenBox.getChildren().addAll(materialNodes);
    }

    private void setupWelkomEnEmailLabels() {
        String[] gebruikerInfo = domCon.geefGegevensAangemeldeGebruiker();
        lblWelkomInfo.setText(String.format("Welkom %s %s!", gebruikerInfo[0], gebruikerInfo[1]));
        lblEmailInfo.setText(String.format("Email: %s", gebruikerInfo[2]));
    }

    @FXML
    private void menuActionUitloggen(ActionEvent event) {
        domCon.meldAf();
        Stage stage = (Stage) getScene().getWindow();
        Scene scene = new Scene(new LoginFrameController(domCon));
        stage.setScene(scene);
        stage.setTitle("Default");
        stage.show();
    }

    @FXML
    private void menuActionAfsluiten(ActionEvent event) {
        Platform.exit();
    }

}
