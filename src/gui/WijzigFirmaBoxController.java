/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import domein.firma.Firma;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ruben
 */
public class WijzigFirmaBoxController extends VBox {

    @FXML
    private Text lblTitel;
    @FXML
    private TextField txfNaamFirma;
    @FXML
    private TextField txfEmailFirma;
    @FXML
    private Label lblMessage;
    @FXML
    private Button btnOpslaan;
    @FXML
    private Button btnAnnuleer;
    
    private DomeinController dc;
    private BeheerFirmasBoxController parent;
    private Firma firma;
    
    
    public WijzigFirmaBoxController(DomeinController dc, BeheerFirmasBoxController parent, Firma firma) {
        this.dc = dc;
        this.parent = parent;
        this.firma = firma;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WijzigFirmaBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        txfNaamFirma.setText(firma.getNaam());
        txfEmailFirma.setText(firma.getEmail());
    }
    
    @FXML
    private void onKeyPressedTxfNaamFirma(KeyEvent event) {
    }

    @FXML
    private void onKeyPressedTxfEmailFirma(KeyEvent event) {
    }

    @FXML
    private void onActionTxfEmailFirma(ActionEvent event) {
    }

    @FXML
    private void onActionBtnOpslaan(ActionEvent event) {
    }

    @FXML
    private void onActionBtnAnnuleer(ActionEvent event) {
        Stage stage = (Stage) lblTitel.getScene().getWindow();        
        stage.close();
    }
    
}
