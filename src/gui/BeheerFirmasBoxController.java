/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import domein.firma.Firma;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import shared.MateriaalView;

/**
 * FXML Controller class
 *
 * @author ruben
 */
public class BeheerFirmasBoxController extends VBox {

    @FXML
    private Text lblTitel;
    @FXML
    private ComboBox<String> cmbVerwijder;
    @FXML
    private Button btnVerwijder;
    @FXML
    private Label lblVerwijder;
    @FXML
    private TextField txfNaamFirma;
    @FXML
    private TextField txfEmailFirma;
    @FXML
    private Button btnVoegToe;
    @FXML
    private Label lblVoegToe;
    @FXML
    private Button btnGaTerug;
    
    @FXML
    private ComboBox<String> cmbWijzig;
    @FXML
    private Label lblWijzig;
    @FXML
    private Button btnWijzig;
    
    private DomeinController dc;
    private MateriaalToevoegenController parent;
    private String laatstToegevoegdeFirma;
    private Color colorSucces = Color.web("#04B431");
    private Color colorError = Color.web("FF0000");

    public BeheerFirmasBoxController(DomeinController dc, MateriaalToevoegenController parent) {
        this.dc = dc;
        this.parent = parent;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BeheerFirmasBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        verbergLabels();
        
        // Initialiseer de lijst met firma's
        setupFirmas();
    }
    
    @FXML
    private void onActionCmbVerwijder(ActionEvent event) {
        verbergLabels();
    }

    @FXML
    private void onActionBtnVerwijder(ActionEvent event) {
        String geselecteerdeFirma = cmbVerwijder.getValue();
        
        try {
            dc.verwijderFirma(geselecteerdeFirma);
            
            // Zet tekst kleur
            lblVerwijder.setTextFill(colorSucces);
            
            lblVerwijder.setText(String.format("De firma \"%s\" is succesvol verwijderd.",geselecteerdeFirma));
            
            // Herinitialiseer de lijst met firma's
            setupFirmas();
        }
        catch (IllegalArgumentException e) {
            // Zet tekst kleur
            lblVerwijder.setTextFill(colorError);
            
            lblVerwijder.setText(e.getMessage());   
        }
        
        lblVerwijder.setVisible(true);
    }

    @FXML
    private void onActionTxfEmailFirma(ActionEvent event) {
        onActionBtnVoegToe(event);
    }

    @FXML
    private void onActionBtnVoegToe(ActionEvent event) {
        try{
            String naamFirma = txfNaamFirma.getText();
            
            dc.voegFirmaToe(naamFirma, txfEmailFirma.getText());
            
            // Zet tekst kleur
            lblVoegToe.setTextFill(colorSucces);
            
            lblVoegToe.setText(String.format("De firma \"%s\" is succesvol toegevoegd.", naamFirma));
            
            // Zet attribuut laatstToegevoegdeFirma, hierdoor zal bij het teruggaan naar het hoofdscherm
            // de firma automatisch al gekozen worden.
            laatstToegevoegdeFirma = naamFirma;
            
            // Herinitialiseer de lijst met firma's
            setupFirmas();
            
            // Text fields weer leeg maken
            txfNaamFirma.setText("");
            txfEmailFirma.setText("");
        }catch(IllegalArgumentException e){
            // Zet tekst kleur
            lblVoegToe.setTextFill(colorError);
            
            lblVoegToe.setText(e.getMessage());
        }
        
        lblVoegToe.setVisible(true);
    }

    @FXML
    private void onActionBtnGaTerug(ActionEvent event) {
        Stage stage = (Stage) lblTitel.getScene().getWindow();
        
        parent.refreshFirmas(laatstToegevoegdeFirma);
        stage.close();
    }
    
    @FXML
    private void onKeyPressedTxfEmailFirma(KeyEvent event) {
        verbergLabels();
    }
    
    public void setupFirmas() {
        List<String> firmas = dc.geefAlleFirmas();
        
        cmbVerwijder.getItems().clear();
        cmbVerwijder.getItems().addAll(firmas);
        
        cmbWijzig.getItems().clear();
        cmbWijzig.getItems().addAll(firmas);
    }

    @FXML
    private void onKeyPressedTxfNaamFirma(KeyEvent event) {
        verbergLabels();
    }

    @FXML
    private void onActionBtnWijzig(ActionEvent event) {
        String geselecteerdeFirma = cmbWijzig.getValue();
        
        try {
            if (geselecteerdeFirma == null)
                throw new IllegalArgumentException("Je hebt geen firma geselecteerd.");
            
            Optional<Firma> firma = dc.geefFirma(geselecteerdeFirma);
            
            if ( ! firma.isPresent()) 
                throw new IllegalArgumentException("De firma kon niet worden gevonden.");
            
            promptFirmaWijzigen(firma.get());
        }
        catch (IllegalArgumentException e) {
            // Zet tekst kleur
            lblWijzig.setTextFill(colorError);
            
            lblWijzig.setText(e.getMessage());
            
            lblWijzig.setVisible(true);
        }  
    }

    private void verbergLabels() {
        lblVoegToe.setVisible(false);
        lblVerwijder.setVisible(false);
        lblWijzig.setVisible(false);
    }

    @FXML
    private void onActionCmbWijzig(ActionEvent event) {
        verbergLabels();
    }
    
    private void promptFirmaWijzigen(Firma firma) {
        Scene promptScene = new Scene(new WijzigFirmaBoxController(dc, this, firma), 400, 300);
        Stage prompt = new Stage();
        prompt.initModality(Modality.APPLICATION_MODAL);
        prompt.initOwner(getScene().getWindow());
        prompt.setScene(promptScene);
        prompt.show();
        
        // Wanneer gebruiker de window aflsuit via het kruisje in de rechterbovenhoek worden de firma's opnieuw geladen
        // ,want het is mogelijk dat de gebruiker een firma heeft verwijderd.
        prompt.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
              setupFirmas();
          }
        });   
    }
    
    public MateriaalView geefMateriaalView() {
        return parent.getMv();
    }
    
    public void wijzigMateriaalView(MateriaalView mv) {
        parent.setMv(mv);
    }
    
    public void setLblWijzig(String message) {
        lblWijzig.setText(message);
    }
    
    public void toontLblWijzig() {
        lblWijzig.setTextFill(colorSucces);
        lblWijzig.setVisible(true);
    }
}
