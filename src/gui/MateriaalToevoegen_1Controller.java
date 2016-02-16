/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import shared.MateriaalView;

/**
 * FXML Controller class
 *
 * @author Demo
 */
public class MateriaalToevoegen_1Controller extends BorderPane {

    private DomeinController dc;

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

    /**
     * Initializes the controller class.
     *
     * @param dc domeincontroller uit het domein
     */
    public MateriaalToevoegen_1Controller(DomeinController dc) {
        this.dc = dc;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginFrame.fxml"));
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void gaTerug() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void voegMateriaalToe() {

        String deNaam = naam.getText().trim();
        int hetAantal = Integer.parseInt(aantal.getText());

        MateriaalView matView = new MateriaalView(deNaam, hetAantal);
        
        
        
        
        
        
        dc.voegMateriaalToe(matView);

    }

}
