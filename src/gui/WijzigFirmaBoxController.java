/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import domein.firma.Firma;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import shared.MateriaalView;

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
        verbergLabels();
    }

    @FXML
    private void onKeyPressedTxfEmailFirma(KeyEvent event) {
        verbergLabels();
    }

    @FXML
    private void onActionTxfEmailFirma(ActionEvent event) {
        onActionBtnOpslaan(event);
    }

    @FXML
    private void onActionBtnOpslaan(ActionEvent event) {
        String naamFirma = txfNaamFirma.getText();
        String emailFirma = txfEmailFirma.getText();
        String oudeNaamFirma = firma.getNaam();
        String message = "";

        // Wijzigt de huidige materiaal view
        MateriaalView mv = parent.geefMateriaalView();
        mv.setFirma(naamFirma);
        mv.setEmailFirma(emailFirma);

        try {
            // Wijzigt de firma voor alle materialen die dezelfde firma gebruiken
            dc.wijzigFirmas(firma, naamFirma, emailFirma);

            parent.setupFirmas();

            if (oudeNaamFirma.equals(naamFirma)) {
                parent.setLblWijzig(String.format("De firma \"%s\" is succesvol gewijzigd.", oudeNaamFirma));
            } else {
                parent.setLblWijzig(String.format("De firma \"%s\" is succesvol gewijzigd naar \"%s\".", oudeNaamFirma, naamFirma));
            }

            parent.toontLblWijzig();

            onActionBtnAnnuleer(event);
        } catch (IllegalArgumentException e) {
            lblMessage.setText(e.getMessage());
            lblMessage.setVisible(true);
        }
    }

    @FXML
    private void onActionBtnAnnuleer(ActionEvent event) {
        Stage stage = (Stage) lblTitel.getScene().getWindow();
        stage.close();
    }

    private void verbergLabels() {
        lblMessage.setVisible(false);
    }
}
