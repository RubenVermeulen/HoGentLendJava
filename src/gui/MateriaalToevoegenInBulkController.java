/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
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
        Stage stage = (Stage) getScene().getWindow();
        Scene scene = new Scene(new MainMenuFrameController(domCon));
        stage.setScene(scene);

        
    }

    @FXML
    private void voegMateriaalToeOnAction(ActionEvent event) {
        System.out.println("ayy");
            domCon.voegMaterialenToeInBulk(urlCsv.getText());
            
            
        
    }
        private void kiesCsvbestand() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open csv bestand");
        File file = fileChooser.showOpenDialog(new Stage());
        urlCsv.setText(file.getPath());
    }

}
