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
public class VoegBeheerderToeBoxController extends VBox {

    @FXML
    private TextField txfEmail;
    
    private DomeinController dc;
    private MainMenuFrameController parent;
    
    @FXML
    private Text lblBeheerder;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblInvalidEmail;

    public VoegBeheerderToeBoxController(DomeinController domCon, MainMenuFrameController parent) {
        this.dc = domCon;
        this.parent = parent;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VoegBeheerderToeBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }  


    @FXML
    private void onActionTxfEmail(ActionEvent event) {
        onActionBtnVoegToe(event);
    }

    @FXML
    private void onActionAnnuleer(ActionEvent event) {
        Stage stage = (Stage) lblBeheerder.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onActionBtnVoegToe(ActionEvent event) {
        Stage stage = (Stage) lblBeheerder.getScene().getWindow();
        
        try{
            dc.stelAanAlsBeheerder(txfEmail.getText());
        }catch(IllegalArgumentException e){
            lblInvalidEmail.setText(e.getMessage());
            lblInvalidEmail.setVisible(true);
            return;
        }
        parent.vulTableViewOpMetBeheerders();
        stage.close();
    }

    @FXML
    private void onKeyPressedTxfEmail(KeyEvent event) {
        lblInvalidEmail.setVisible(false);
    }
    
}
