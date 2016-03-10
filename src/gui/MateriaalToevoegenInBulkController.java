/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import exceptions.BulkToevoegenMisluktException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MateriaalToevoegenInBulkController extends BorderPane {

    @FXML
    private TextField urlCsv;
    @FXML
    private Button kiesCsvKnop;
    @FXML
    private Button gaTerugKnop;
    @FXML
    private Button voegToeKnop;

    DomeinController domCon;
    @FXML
    private Label lblError;

    public MateriaalToevoegenInBulkController(DomeinController domCon) {
        this.domCon = domCon;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MateriaalToevoegenInBulk.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void csvbestandKiezenOnAction(ActionEvent event) {
        kiesCsvbestand();

    }

    @FXML
    private void gaTerugOnAction(ActionEvent event) {
        gaterugNaarmenu();

    }

    @FXML
    private void voegMateriaalToeOnAction(ActionEvent event) {
      
            try {
                controlerenOfCsvFileIsIngevuld();
                domCon.voegMaterialenToeInBulk(urlCsv.getText());
            } catch (BulkToevoegenMisluktException|IllegalArgumentException e) {
                lblError.setText(e.getMessage());
                return;
            }
            gaterugNaarmenu();
        

    }

    private void kiesCsvbestand() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open csv bestand");
        File file = fileChooser.showOpenDialog(new Stage());
        urlCsv.setText(file.getPath());
    }

    private void gaterugNaarmenu() {
        Stage stage = (Stage) getScene().getWindow();
        Scene scene = new Scene(new MainMenuFrameController(domCon));
        stage.setScene(scene);

    }

    private void controlerenOfCsvFileIsIngevuld() {

        if (urlCsv.getText().isEmpty() || (!urlCsv.getText().endsWith("csv"))) {
            throw new IllegalArgumentException("Er is nog geen csv file gekozen.");

        }

    }
}
