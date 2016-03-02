/*
 * All rights reserved.
 */
package gui;

import domein.DomeinController;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

    private DomeinController domCon;
    private MateriaalToevoegenController parent;
    private boolean isLeerGroep;
    @FXML
    private Text lblGroep;
    @FXML
    private ComboBox<String> cmbVerwijder;
    @FXML
    private Button btnVerwijder;
    @FXML
    private Label lblVerwijder;
    @FXML
    private TextField txfVoegToe;
    @FXML
    private Button btnVoegToe;
    @FXML
    private Label lblVoegToe;
    @FXML
    private Button btnGaTerug;

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
        if (isLeergroep) {
            txfVoegToe.setPromptText("leergebied...");
            lblGroep.setText("Beheren leergebieden");
            cmbVerwijder.setPromptText("-- selecteer leergebied --");
        }
        lblVoegToe.setVisible(false);
        lblVerwijder.setVisible(false);
        setupGroepen();
    }

    private void setupGroepen() {
        List<String> groepen;
        if (isLeerGroep) {
            groepen = domCon.geefAlleLeergebieden();
        } else {
            groepen = domCon.geefAlleDoelgroepen();
        }
        cmbVerwijder.getItems().clear();
        cmbVerwijder.getItems().addAll(groepen);
    }

    @FXML
    private void onActionCmbVerwijder(ActionEvent event) {
        lblVoegToe.setVisible(false);
        lblVerwijder.setVisible(false);
    }

    @FXML
    private void onActionBtnVerwijder(ActionEvent event) {
        String teVerwijderenGroep = cmbVerwijder.selectionModelProperty().get().getSelectedItem();
        try {
            domCon.verwijderGroep(teVerwijderenGroep, isLeerGroep);
            lblVerwijder.setText(String.format("\"%s\" is succesvol verwijderd.",teVerwijderenGroep));
            setupGroepen();
        } catch (IllegalArgumentException e) {
            lblVerwijder.setText(e.getMessage());
        }
        lblVerwijder.setVisible(true);
    }

    @FXML
    private void onKeyPressedTxfGroep(KeyEvent event) {
        lblVoegToe.setVisible(false);
        lblVerwijder.setVisible(false);
    }

    @FXML
    private void onActionTxfGroep(ActionEvent event) {
        onActionBtnVoegToe(event);
    }

    @FXML
    private void onActionBtnVoegToe(ActionEvent event) {
        String toetevoegenGroep = txfVoegToe.getText();
        try{
            domCon.voegGroepToe(toetevoegenGroep, isLeerGroep);
            lblVoegToe.setText(String.format("\"%s\" is succesvol toegevoegd.",toetevoegenGroep));
            setupGroepen();
        }catch(IllegalArgumentException e){
            lblVoegToe.setText(e.getMessage());
        }
        lblVoegToe.setVisible(true);
    }

    @FXML
    private void onActionBtnGaTerug(ActionEvent event) {
        Stage stage = (Stage) lblGroep.getScene().getWindow();
        parent.refreshGroepen();
        stage.close();
    }

}

/*
    @FXML
    private Text lblGroep;
    @FXML
    private TextField txfGroep;
    @FXML
    private Label lblInvalidGroep;

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
    
*/
