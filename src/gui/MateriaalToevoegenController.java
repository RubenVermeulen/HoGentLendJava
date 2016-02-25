package gui;

import domein.DomeinController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import shared.MateriaalView;

public class MateriaalToevoegenController extends BorderPane {

    private DomeinController dc;
    private MainMenuFrameController mmfc;

    @FXML
    private ImageView previewFoto;
    @FXML
    private TextField urlFoto;
    @FXML
    private Button kiesFotoKnop;
    @FXML
    private TextField naam;
    @FXML
    private TextField artikelcode;
    @FXML
    private TextField aantal;
    @FXML
    private TextField prijs;
    @FXML
    private TextField locatie;
    @FXML
    private TextArea beschrijving;
    @FXML
    private TextField doelgroepen;
    @FXML
    private TextField leergroepen;
    @FXML
    private TextField firma;
    @FXML
    private TextField emailfirma;
    @FXML
    private CheckBox beschikbaarheid;
    @FXML
    private Button gaTerugKnop;
    @FXML
    private Button voegToeKnop;
    @FXML
    private TextField onbeschikbaar;
    @FXML
    private ImageView errorNaam;
    @FXML
    private ImageView errorAantal;
    @FXML
    private ImageView errorOnbeschikbaar;
    @FXML
    private ImageView errorPrijs;
    @FXML
    private ImageView errorEmailfirma;

    public MateriaalToevoegenController(DomeinController dc) {
        this.dc = dc;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MateriaalToevoegen.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void fotoKiezenOnAction(ActionEvent event) {
        kiesFoto();
    }

    @FXML
    private void gaTerugOnAction(ActionEvent event) {
        gaTerug();
    }

    @FXML
    private void voegMateriaalToeOnAction(ActionEvent event) {
        voegMateriaalToe();
    }

    private void kiesFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open afbeelding bestand");
        File file = fileChooser.showOpenDialog(new Stage());
        urlFoto.setText(file.getPath());
    }

    private void gaTerug() {
        Stage stage = (Stage) getScene().getWindow();
        Scene scene = new Scene(new MainMenuFrameController(dc));
        stage.setScene(scene);
    }

    private void voegMateriaalToe() {
        String deNaam = naam.getText().trim();
        // TODO: catch Numberformatexception

        int hetAantal = Integer.parseInt(aantal.getText());

        try {

            MateriaalView matView = new MateriaalView(deNaam, hetAantal);
            if (onbeschikbaar.getText() != null && !onbeschikbaar.getText().isEmpty()) {
                matView.setAantalOnbeschikbaar(Integer.parseInt(onbeschikbaar.getText()));
            }

            matView.setArtikelNummer(artikelcode.getText());

            matView.setDoelgroepen(doelgroepen.getText());

            matView.setEmailFirma(emailfirma.getText());

            matView.setFirma(firma.getText());
            matView.setFotoUrl(urlFoto.getText());
            matView.setLeergebieden(leergroepen.getText());
            matView.setOmschrijving(beschrijving.getText());
            matView.setPlaats(locatie.getText());
            if (prijs.getText() != null && !prijs.getText().isEmpty()) {
                matView.setPrijs(Double.parseDouble(prijs.getText()));
            }
            matView.setUitleenbaarheid(beschikbaarheid.isSelected());

            dc.voegMateriaalToe(matView);
            gaTerug();

        } catch (IllegalArgumentException e) {
            
           switch(e.getMessage()){
               case "naam" : errorNaam.setVisible(true);
               break;
               case "aantal" : errorAantal.setVisible(true);
               break;
               case "emailFirma" : errorEmailfirma.setVisible(true);
               break;
               case "prijs" : errorPrijs.setVisible(true);
               break;
               case "onbeschikbaar" : errorOnbeschikbaar.setVisible(true);
           }
            
        }
    }

}
