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
public class VoegFirmaToeBoxController extends VBox {

    @FXML
    private TextField txfNaamFirma;
    @FXML
    private TextField txfEmailFirma;
    @FXML
    private Label lblInvalidFirma; 
    
    private DomeinController dc;
    private MateriaalToevoegenController parent;
    @FXML
    private Text lblTitel;

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
    }


    @FXML
    private void onKeyPressedTxfEmailFirma(KeyEvent event) {
        lblInvalidFirma.setVisible(false);
    }

    @FXML
    private void onActionTxfEmailFirma(ActionEvent event) {
        onActionBtnVoegToe(event);
    }

    @FXML
    private void onActionAnnuleer(ActionEvent event) {
        Stage stage = (Stage) lblTitel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onActionBtnVoegToe(ActionEvent event) {
        Stage stage = (Stage) lblTitel.getScene().getWindow();
        
        try{
            dc.voegFirmaToe(txfNaamFirma.getText(), txfEmailFirma.getText());
        }catch(IllegalArgumentException e){
            lblInvalidFirma.setText(e.getMessage());
            lblInvalidFirma.setVisible(true);
            return;
        }
        
        parent.refreshFirmas(txfNaamFirma.getText());
        stage.close();
    }
    
}
