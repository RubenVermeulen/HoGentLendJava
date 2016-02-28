/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alexa
 */
public class MateriaalToevoegenInBulkController extends BorderPane {

    private DomeinController domCon;

    @FXML
    private TextField urlExcel;
    @FXML
    private Button kiesFotoKnop;
    @FXML
    private Button gaTerugKnop;
    @FXML
    private Button voegToeKnop;

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
    private void excelbestandKiezenOnAction(ActionEvent event) {
    kiesExcelbestand();
    
    }

    @FXML
    private void gaTerugOnAction(ActionEvent event) {
        
        Stage stage = (Stage) getScene().getWindow();
        Scene scene = new Scene(new MainMenuFrameController(domCon));
        stage.setScene(scene);
    }

    @FXML
    private void voegMateriaalToeOnAction(ActionEvent event) {
    domCon.voegMaterialenToeInBulk(urlExcel.getText());
    
    }

    
    private void kiesExcelbestand() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open afbeelding bestand");
        File file = fileChooser.showOpenDialog(new Stage());
        urlExcel.setText(file.getPath());
    }
    
    
    
}
