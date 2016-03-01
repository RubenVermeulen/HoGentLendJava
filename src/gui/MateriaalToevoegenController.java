package gui;

import domein.DomeinController;
import domein.Materiaal;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import shared.MateriaalView;

public class MateriaalToevoegenController extends BorderPane {

    private DomeinController dc;
    private MainMenuFrameController mmfc;
    private MateriaalView mv;

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
    private CheckComboBox<String> doelgroepen;
    @FXML
    private CheckComboBox<String> leergroepen;
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
    @FXML
    private Label lblTitel;
    @FXML
    private Label lblErrorMessage;
    @FXML
    private ImageView imgErrorMessage;

    public MateriaalToevoegenController(DomeinController dc, MateriaalView mv) {
        this.dc = dc;
        this.mv = mv;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MateriaalToevoegen.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Voorkomt een horizontale balk
        beschrijving.setWrapText(true);
        
        // om de fxml duidelijker te maken laat ik errormessage daar op visible staan
        verbergError();

        setupAlleGroepen();

        // Wijzig labels en vul velden in wanneer we een materiaal willen wijzigen
        if (mv != null) {
            initialiseerMateriaalWijzigen(mv);
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
        if (mv == null) {
            voegMateriaalToe();
        } else {
            wijzigMateriaal();
        }
    }

    private void kiesFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open afbeelding bestand");
        File file = fileChooser.showOpenDialog(new Stage());
        urlFoto.setText(file.getPath());

        // Laad afbeelding dynamisch in voor preview - nog geen controles of het een afbeelding is of niet
//        previewFoto.setImage(new Image("file:///" + file.getAbsolutePath()));
    }

    private void gaTerug() {
        Stage stage = (Stage) getScene().getWindow();
        Scene scene = new Scene(new MainMenuFrameController(dc));
        stage.setScene(scene);
    }

    private void setupAlleGroepen() {
        List<String> doelGroepenStr = dc.geefAlleDoelgroepen();
        List<String> leergebiedenStr = dc.geefAlleLeergebieden();
        doelgroepen.getItems().addAll(doelGroepenStr);
        leergroepen.getItems().addAll(leergebiedenStr);
    }

    public void refreshGroepen() {
        List<String> doelGroepenStr = dc.geefAlleDoelgroepen();
        List<String> leergebiedenStr = dc.geefAlleLeergebieden();

        List<String> selectedDoelen = new ArrayList(doelgroepen.getCheckModel().getCheckedItems());
        List<String> selectedLeren = new ArrayList(leergroepen.getCheckModel().getCheckedItems());

        doelgroepen.getItems().clear();
        leergroepen.getItems().clear();

        doelgroepen.getItems().addAll(doelGroepenStr);
        leergroepen.getItems().addAll(leergebiedenStr);

        for (String doel : selectedDoelen) {
            if (doelGroepenStr.contains(doel)) {
                doelgroepen.getCheckModel().check(doel);
            }
        }
        for (String leer : selectedLeren) {
            if (leergebiedenStr.contains(leer)) {
                leergroepen.getCheckModel().check(leer);
            }
        }
    }

    private void voegMateriaalToe() {
        try {

//            String deNaam = naam.getText().trim();
//            // TODO: catch Numberformatexception
//
//            int hetAantal = Integer.parseInt(aantal.getText());
//
//            MateriaalView matView = new MateriaalView(deNaam, hetAantal);
//            if (onbeschikbaar.getText() != null && !onbeschikbaar.getText().isEmpty()) {
//                matView.setAantalOnbeschikbaar(Integer.parseInt(onbeschikbaar.getText()));
//            }
//
//            matView.setArtikelNummer(artikelcode.getText());
//
//            matView.setDoelgroepen(new ArrayList(doelgroepen.getCheckModel().getCheckedItems()));
//
//            matView.setEmailFirma(emailfirma.getText());
//
//            matView.setFirma(firma.getText());
//            matView.setFotoUrl(urlFoto.getText());
//
//            matView.setLeergebieden(new ArrayList(leergroepen.getCheckModel().getCheckedItems()));
//
//            matView.setOmschrijving(beschrijving.getText());
//            matView.setPlaats(locatie.getText());
//            if (prijs.getText() != null && !prijs.getText().isEmpty()) {
//                matView.setPrijs(Double.parseDouble(prijs.getText()));
//            }
//            matView.setUitleenbaarheid(beschikbaarheid.isSelected());
//
//            dc.voegMateriaalToe(matView);
//            gaTerug();

            wijzigMateriaalView(true);


        } catch (IllegalArgumentException e) {

            lblErrorMessage.setVisible(true);
            imgErrorMessage.setVisible(true);
            
            switch (e.getMessage()) {
                case "naam":
                    errorNaam.setVisible(true);
                    lblErrorMessage.setText("Het materiaal moet een (unieke) naam hebben.");
                    break;
                case "aantal":
                    errorAantal.setVisible(true);
                    lblErrorMessage.setText("Het aantal materialen moet groter zijn dan 0.");
                    break;
                case "emailFirma":
                    errorEmailfirma.setVisible(true);
                    lblErrorMessage.setText("Firma heeft geen geldig emailadres (vb: firma@hotmail.com");
                    break;
                case "prijs":
                    errorPrijs.setVisible(true);
                    lblErrorMessage.setText("Prijs moet groter zijn dan 0.");
                    break;
                case "onbeschikbaar":
                    lblErrorMessage.setText("Aantal onbeschikbare materialen moet groter zijn dan 0.");
                    errorOnbeschikbaar.setVisible(true);
            }
            
            naam.getParent().requestFocus();

        }
    }

    @FXML
    private void onBtnActionDoelgroepen(ActionEvent event) {
        promptGroepToevoegen(false);
    }

    @FXML
    private void onBtnActionLeergroepen(ActionEvent event) {
        promptGroepToevoegen(true);
    }

    private void promptGroepToevoegen(boolean isLeergroep) {
        Scene promptScene = new Scene(new VoegGroepToeBoxController(dc, this, isLeergroep), 300, 200);
        Stage prompt = new Stage();
        prompt.initModality(Modality.APPLICATION_MODAL);
        prompt.initOwner(getScene().getWindow());
        prompt.setScene(promptScene);
        prompt.show();
    }

    public void initialiseerMateriaalWijzigen(MateriaalView mv) {
        System.out.println(mv);

        // Labels wijzigen
        lblTitel.setText("Wijzig materiaal");
        voegToeKnop.setText("Wijzig materiaal");

        // Velden aanvullen
        naam.setText(mv.getNaam());
        artikelcode.setText(mv.getArtikelNummer());
        aantal.setText(Integer.toString(mv.getAantal()));
        onbeschikbaar.setText(Integer.toString(mv.getAantalOnbeschikbaar()));
        prijs.setText(Double.toString(mv.getPrijs()));
        locatie.setText(mv.getPlaats());
        beschrijving.setText(mv.getOmschrijving());
        firma.setText(mv.getFirma());
        emailfirma.setText(mv.getEmailFirma());
        beschikbaarheid.setSelected(mv.isUitleenbaarheid());
        urlFoto.setText(mv.getFotoUrl());

        if (!mv.getFotoUrl().isEmpty()) {
            InputStream ins = getClass().getResourceAsStream("/images/" + String.valueOf(mv.getFotoUrl()));
            if (ins == null) {
                System.out.println("input stream is null :((((" + "/images/" + String.valueOf(mv.getFotoUrl()));
            }
            if (ins != null) {
                previewFoto.setImage(new Image(ins));
            }
        }

        // Doelgroepen checken
        for (String doelgroep : doelgroepen.getItems()) {
            for (String d : mv.getDoelgroepen()) {
                if (doelgroep.equals(d)) {
                    doelgroepen.getCheckModel().check(doelgroep);
                }
            }
        }

        // Leergebieden checken
        for (String leergebied : leergroepen.getItems()) {
            for (String l : mv.getLeergebieden()) {
                if (leergebied.equals(l)) {
                    leergroepen.getCheckModel().check(leergebied);
                }
            }
        }
    }

    public void wijzigMateriaal() {
        try {
//            // TODO: catch Numberformatexception
//            
//            mv.setNaam(naam.getText().trim());
//            mv.setAantal(Integer.parseInt(aantal.getText()));
//            
//            if (onbeschikbaar.getText() != null && ! onbeschikbaar.getText().isEmpty()) {
//                mv.setAantalOnbeschikbaar(Integer.parseInt(onbeschikbaar.getText()));
//            }
//
//            mv.setArtikelNummer(artikelcode.getText());
//
//            mv.setDoelgroepen(new ArrayList(doelgroepen.getCheckModel().getCheckedItems()));
//
//            mv.setEmailFirma(emailfirma.getText());
//
//            mv.setFirma(firma.getText());
//            mv.setFotoUrl(urlFoto.getText());
//            
//            mv.setLeergebieden(new ArrayList(leergroepen.getCheckModel().getCheckedItems()));
//
//            mv.setOmschrijving(beschrijving.getText());
//            mv.setPlaats(locatie.getText());
//            
//            if (prijs.getText() != null && !prijs.getText().isEmpty()) {
//                mv.setPrijs(Double.parseDouble(prijs.getText()));
//            }
//            mv.setUitleenbaarheid(beschikbaarheid.isSelected());
//
//            dc.wijzigMateriaal(mv);
//            gaTerug();

            wijzigMateriaalView(false);

        } catch (IllegalArgumentException e) {

            lblErrorMessage.setVisible(true);
            imgErrorMessage.setVisible(true);
            
            switch (e.getMessage()) {
                case "naam":
                    errorNaam.setVisible(true);
                    lblErrorMessage.setText("Het materiaal moet een (unieke) naam hebben.");
                    break;
                case "aantal":
                    errorAantal.setVisible(true);
                    lblErrorMessage.setText("Het aantal materialen moet groter zijn dan 0.");
                    break;
                case "emailFirma":
                    errorEmailfirma.setVisible(true);
                    lblErrorMessage.setText("Firma heeft geen geldig emailadres (vb: firma@hotmail.com");
                    break;
                case "prijs":
                    errorPrijs.setVisible(true);
                    lblErrorMessage.setText("Prijs moet groter zijn dan 0.");
                    break;
                case "onbeschikbaar":
                    lblErrorMessage.setText("Aantal onbeschikbare materialen moet groter zijn dan 0.");
                    errorOnbeschikbaar.setVisible(true);
            }
            
            naam.getParent().requestFocus();

        }
    }
    

    public void wijzigMateriaalView(boolean isMateriaalToevoegen) {
        MateriaalView matView;
            
        String deNaam = naam.getText().trim();
        
        //manier om de exceptions in de volgorde van de inputvelden te laten werpen
        //nu zal er eerst gechecked worden of de naam wel is ingevuld, alvorens het aantal te checken
        int hetAantal = -1;

        if (aantal.getText() != null && !aantal.getText().isEmpty()){
            hetAantal = Integer.parseInt(aantal.getText());
        }

        if (isMateriaalToevoegen) {
            matView = new MateriaalView(deNaam, hetAantal);
        } else {
            matView = this.mv;

            matView.setNaam(deNaam);
            matView.setAantal(hetAantal);
        }

        if (onbeschikbaar.getText() != null && !onbeschikbaar.getText().isEmpty()) {
            matView.setAantalOnbeschikbaar(Integer.parseInt(onbeschikbaar.getText()));
        }

        matView.setArtikelNummer(artikelcode.getText());

        matView.setDoelgroepen(new ArrayList(doelgroepen.getCheckModel().getCheckedItems()));

        matView.setEmailFirma(emailfirma.getText());

        matView.setFirma(firma.getText());
        matView.setFotoUrl(urlFoto.getText());

        matView.setLeergebieden(new ArrayList(leergroepen.getCheckModel().getCheckedItems()));

        matView.setOmschrijving(beschrijving.getText());
        matView.setPlaats(locatie.getText());
        if (prijs.getText() != null && !prijs.getText().isEmpty()) {
            matView.setPrijs(Double.parseDouble(prijs.getText()));
        }
        
        matView.setUitleenbaarheid(beschikbaarheid.isSelected());

        if (isMateriaalToevoegen) // We voegen een nieuwe materiaal toe
        {
            dc.voegMateriaalToe(matView);
        } else // We wijzigen een bestaand materiaal
        {
            dc.wijzigMateriaal(matView);
        }

        gaTerug();
    }

    
    //Wanneer de gebruiker opnieuw typt na de error in het vakje waarin de error verscheen,
    //wordt de error weer verborgen
    
    @FXML
    private void naamOnKey(KeyEvent event) {
        verbergError();
        errorNaam.setVisible(false);
    }

    @FXML
    private void aantalOnKey(KeyEvent event) {
        verbergError();
        errorAantal.setVisible(false);
    }

    @FXML
    private void prijsOnKey(KeyEvent event) {
        verbergError();
        errorPrijs.setVisible(false);
    }

    @FXML
    private void onbeschikbaarOnKey(KeyEvent event) {
        verbergError();
        errorOnbeschikbaar.setVisible(false);
    }
    
    @FXML
    private void firmaOnKey(KeyEvent event) {
        verbergError();
    }

    @FXML
    private void emailOnKey(KeyEvent event) {
        verbergError();
        errorEmailfirma.setVisible(false);
    }
    
    public void verbergError(){
        lblErrorMessage.setVisible(false);
        imgErrorMessage.setVisible(false);
    }

    
}
