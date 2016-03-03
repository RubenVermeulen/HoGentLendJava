/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
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
        if (controlerenOfCsvFileIsIngevuld()) {
            domCon.voegMaterialenToeInBulk(urlCsv.getText());
            gaterugNaarmenu();
        }

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

    private boolean controlerenOfCsvFileIsIngevuld() {
        boolean isIngevuld = false;
        
      
        
        
        
        
        
        if (urlCsv.getText().isEmpty()||(!urlCsv.getText().endsWith("csv")) ){
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    String.format("Er is nog geen csvfile gekozen."),
                    ButtonType.OK);

            alert.setTitle("Opgelet");
            alert.setHeaderText("Opgelet");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                ((VBox) getParent()).getChildren().remove(this);
            }

            /*
            
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(VBoxBuilder.create().
                    children(new Text("Er is nog geen csvfile gekozen.")).padding(new Insets(30)).build()));
            dialogStage.show();*/
        } else {
            isIngevuld = true;
        }
        return isIngevuld;
    }

}
