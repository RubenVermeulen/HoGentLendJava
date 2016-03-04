/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ruben
 */
public class VoegFirmaToeBoxController extends VBox {

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
    
    private DomeinController dc;
    private MateriaalToevoegenController parent;
    private String laatstToegevoegdeFirma;

    public VoegFirmaToeBoxController(DomeinController dc, MateriaalToevoegenController parent) {
        this.dc = dc;
        this.parent = parent;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VoegFirmaToeBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        lblVoegToe.setVisible(false);
        lblVerwijder.setVisible(false);
        
        // Initialiseer de lijst met firma's
        setupFirmas();
    }
    
    @FXML
    private void onActionCmbVerwijder(ActionEvent event) {
        lblVoegToe.setVisible(false);
        lblVerwijder.setVisible(false);
    }

    @FXML
    private void onActionBtnVerwijder(ActionEvent event) {
        String geselecteerdeFirma = cmbVerwijder.getValue();
        
        try {
            dc.verwijderFirma(geselecteerdeFirma);
            
            // Zet tekst kleur
            lblVerwijder.setTextFill(Color.web("#04B431"));
            
            lblVerwijder.setText(String.format("De firma \"%s\" is succesvol verwijderd.",geselecteerdeFirma));
            
            // Herinitialiseer de lijst met firma's
            setupFirmas();
        }
        catch (IllegalArgumentException e) {
            // Zet tekst kleur
            lblVerwijder.setTextFill(Color.web("#FF0000"));
            
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
            lblVoegToe.setTextFill(Color.web("#04B431"));
            
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
            lblVoegToe.setTextFill(Color.web("#FF0000"));
            
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
        lblVoegToe.setVisible(false);
        lblVerwijder.setVisible(false);
    }
    
    private void setupFirmas() {
        cmbVerwijder.getItems().clear();
        cmbVerwijder.getItems().addAll(dc.geefAlleFirmas());
    }

//    @FXML
//    private TextField txfNaamFirma;
//    @FXML
//    private TextField txfEmailFirma;
//    @FXML
//    private Label lblInvalidFirma; 
//    
//    private DomeinController dc;
//    private MateriaalToevoegenController parent;
//    @FXML
//    private Text lblTitel;
//
//    public VoegFirmaToeBoxController(DomeinController dc, MateriaalToevoegenController parent) {
//        this.dc = dc;
//        this.parent = parent;
//        
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("VoegFirmaToeBox.fxml"));
//        loader.setRoot(this);
//        loader.setController(this);
//        
//        try {
//            loader.load();
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//
//    @FXML
//    private void onKeyPressedTxfEmailFirma(KeyEvent event) {
//        lblInvalidFirma.setVisible(false);
//    }
//
//    @FXML
//    private void onActionTxfEmailFirma(ActionEvent event) {
//        onActionBtnVoegToe(event);
//    }
//
//    @FXML
//    private void onActionAnnuleer(ActionEvent event) {
//        Stage stage = (Stage) lblTitel.getScene().getWindow();
//        stage.close();
//    }
//
//    @FXML
//    private void onActionBtnVoegToe(ActionEvent event) {
//        Stage stage = (Stage) lblTitel.getScene().getWindow();
//        
//        try{
//            dc.voegFirmaToe(txfNaamFirma.getText(), txfEmailFirma.getText());
//        }catch(IllegalArgumentException e){
//            lblInvalidFirma.setText(e.getMessage());
//            lblInvalidFirma.setVisible(true);
//            return;
//        }
//        
//        parent.refreshFirmas(txfNaamFirma.getText());
//        stage.close();
//    }

    @FXML
    private void onKeyPressedTxfNaamFirma(KeyEvent event) {
    }

    
    
}
