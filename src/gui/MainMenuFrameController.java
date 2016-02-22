package gui;

import domein.DomeinController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    @FXML
    private TextField txfZoekMateriaalFilter;

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
        setupMaterials(domCon.geefAlleMaterialen());
        // TEMPORARY_setupTemporaryDemoMaterials();
    }

//    public void TEMPORARY_setupTemporaryDemoMaterials() {
//        List<MateriaalBoxController> controls = new ArrayList<MateriaalBoxController>();
//
//        MateriaalView mv1 = new MateriaalView("Wereldbollen", 3)
//                .setAantalOnbeschikbaar(1).setArtikelNummer("P25DL DUTCH")
//                .setDoelgroepen(Arrays.asList("Liefhebbers voor wereldkennis", "Schatzoekers"))
//                .setEmailFirma("contact@worldbolmakers.com")
//                .setFirma("WorldBolMakers")
//                .setFotoUrl("temp_wereldbol.jpg")
//                .setLeergebieden(Arrays.asList("Aardrijkskunde"))
//                .setOmschrijving("Deze mooie wereldbol met verlichting heeft 25 cm doorsnee en werkt op stroom.")
//                .setPlaats("Lokaal B4.035")
//                .setPrijs(32.25d)
//                .setUitleenbaarheid(true);
//        MateriaalView mv2 = new MateriaalView("Boeken", 1)
//                .setAantalOnbeschikbaar(0).setArtikelNummer("1582-2010")
//                .setDoelgroepen(Arrays.asList("Boeken lezers"))
//                .setEmailFirma("contact@boeken.com")
//                .setFirma("BoekMakers")
//                .setFotoUrl("temp_boek.gif")
//                .setLeergebieden(Arrays.asList("Nederlands"))
//                .setOmschrijving("In Het verdwijnen van Robbert speelt Welagen een geestig spel, vol zelfspot, met zijn eigen schrijversbestaan. De Robbert die verdwijnt, heeft namelijk als achternaam Welagen en is een 25-jarige schrijver die net als de auteur een roman heeft geschreven die Lipari heet, ook in de fictieve werkelijkheid bekroond met een debuutprijs.\n")
//                .setPlaats("Lokaal C2.010")
//                .setPrijs(19.99d)
//                .setUitleenbaarheid(false);
//        for (int i = 0; i < 4; i++) {
//            controls.add(new MateriaalBoxController(mv1));
//            controls.add(new MateriaalBoxController(mv2));
//        }
//
//        materialenBox.getChildren().addAll(controls);
////        final int aantalDemoMaterials = 14;
////        Node[] materialNodes = new Node[aantalDemoMaterials];
////        for (int i = 0; i < aantalDemoMaterials; i++) {
////            materialNodes[i] = new MateriaalBoxController(new MateriaalView("Materiaal Naam " + i, i));
////        }
//    }
    private void setupMaterials(List<MateriaalView> materials) {
        materialenBox.getChildren().clear();
        materials.stream().forEach(mv -> materialenBox.getChildren().add(new MateriaalBoxController(mv, domCon)));
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
        stage.show();
    }

    @FXML
    private void onActionVoegMateriaalToe(ActionEvent event){
        Stage stage = (Stage) getScene().getWindow();
        Scene scene = new Scene(new MateriaalToevoegenController(domCon));
        stage.setScene(scene);        
    }
    
    @FXML
    private void menuActionAfsluiten(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void onBtnZoekMateriaalAction(ActionEvent event) {
        System.out.println(txfZoekMateriaalFilter.getText());
        List<MateriaalView> mvs = domCon.geefMaterialenMetFilter(txfZoekMateriaalFilter.getText());
        System.out.println(mvs);
        setupMaterials(mvs);
        System.out.println("ayy");
    }

}
