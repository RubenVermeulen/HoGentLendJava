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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import shared.MateriaalView;
import shared.ReservatieView;

/**
 * FXML Controller class
 *
 * @author Xander
 */
public class VoegReservatieLijnToeBoxController extends GridPane {

    DomeinController dc;
    ReservatieView rv;
    MainMenuFrameController parent;
    
    @FXML
    private DatePicker dpOphaalmoment;
    @FXML
    private TextField txfIndienmoment;
    @FXML
    private Button btnVoegReservatieLijnToe;
    @FXML
    private TextField txfAantal;
    @FXML
    private Button btnAnnuleer;
    @FXML
    private ComboBox<MateriaalView> cbMaterialen;
    @FXML
    private DatePicker dpIndienmoment;
    
    VoegReservatieLijnToeBoxController(DomeinController dc, ReservatieView rv, MainMenuFrameController parent) {
        this.dc = dc;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VoegReservatieLijnToeBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        this.parent = parent;
        this.rv = rv;
        setupVoegReseravtieLijnToeBox(rv);
        
    }
    
    private void setupVoegReseravtieLijnToeBox(ReservatieView rv) {
        cbMaterialen.getItems().clear();
        cbMaterialen.getItems().addAll(dc.geefAlleMaterialen());
        
        dpOphaalmoment.setValue(rv.getOphaalmoment().toLocalDate());
        dpIndienmoment.setValue(rv.getOphaalmoment().toLocalDate());
    }

    @FXML
    private void onActionBtnVoegReservatieLijnToe(ActionEvent event) {
    }

    @FXML
    private void onActionBtnAnnuleer(ActionEvent event) {
    }

    

}
