/*
 * All rights reserved.
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
 * @author Sven
 */
public class VoegGroepToeBoxController extends VBox {

    @FXML
    private Text lblGroep;
    @FXML
    private TextField txfGroep;
    @FXML
    private Label lblInvalidGroep;

    private DomeinController domCon;
    private MateriaalToevoegenController parent;
    private boolean isLeerGroep;
    
    public VoegGroepToeBoxController(DomeinController domCon, MateriaalToevoegenController parent, boolean isLeergroep) {
        this.domCon = domCon;
        this.parent = parent;
        this.isLeerGroep = isLeergroep;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("VoegGroepToeBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        if (isLeergroep){
            txfGroep.setPromptText("leergebied...");
            lblGroep.setText("Toevoegen leergebied");
        }
    } 

    @FXML
    private void onActionTxfGroep(ActionEvent event) {
        onActionBtnVoegToe(event);
    }

    @FXML
    private void onActionBtnVoegToe(ActionEvent event) {
        Stage stage = (Stage) lblGroep.getScene().getWindow();
        try{
            domCon.voegGroepToe(txfGroep.getText(), isLeerGroep);
        }catch(IllegalArgumentException e){
            lblInvalidGroep.setText(e.getMessage());
            lblInvalidGroep.setVisible(true);
            return;
        }
        
        parent.refreshGroepen();
        stage.close();
    }

    @FXML
    private void onActionAnnuleer(ActionEvent event) {
        Stage stage = (Stage) lblGroep.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onKeyPressedTxfGroep(KeyEvent event) {
        lblInvalidGroep.setVisible(false);
    }
    
}
