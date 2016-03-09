/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import shared.MateriaalView;
import shared.ReservatieLijnView;
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
    @FXML
    private Label lblTitel;
    @FXML
    private TextField txfOphaalmoment;
    @FXML
    private Label lblError;
    
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
        lblTitel.setText(String.format("Voeg materiaal toe aan de reservatie van", rv.getLener()));
        
        cbMaterialen.getItems().clear();
        cbMaterialen.getItems().addAll(dc.geefAlleMaterialen());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        
        dpOphaalmoment.setValue(rv.getOphaalmoment().toLocalDate());
        txfOphaalmoment.setText(rv.getOphaalmoment().format(formatter));
        dpIndienmoment.setValue(rv.getIndienmoment().toLocalDate());
        txfIndienmoment.setText(rv.getIndienmoment().format(formatter));
        
        
    }

    @FXML
    private void onActionBtnVoegReservatieLijnToe(ActionEvent event) {
        dpOphaalmoment.setValue(dpOphaalmoment.getConverter().fromString(dpOphaalmoment.getEditor().getText()));
        dpIndienmoment.setValue(dpIndienmoment.getConverter().fromString(dpIndienmoment.getEditor().getText()));
        
        try{
        LocalDateTime ophaalmoment = convertToLocalDateTime(dpOphaalmoment.getValue(), txfOphaalmoment.getText());
        LocalDateTime indienmoment = convertToLocalDateTime(dpIndienmoment.getValue(), txfIndienmoment.getText());
        
        
        if(cbMaterialen.getValue() == null)
            throw new IllegalArgumentException("Gelieve een materiaal te selecteren!");
        
        if (txfAantal.getText() == null || txfAantal.getText().isEmpty()) {
            throw new IllegalArgumentException("Aantal moet ingevuld zijn!");
        }
        
        int aantal = Integer.parseInt(txfAantal.getText());
        MateriaalView mv = cbMaterialen.getValue();
        
        ReservatieLijnView rlv = new ReservatieLijnView(ophaalmoment, indienmoment, mv, aantal);
        rv.getReservatieLijnen().add(rlv);
        dc.wijzigReservatie(rv);
        }
        catch(IllegalArgumentException e){
            lblError.setText(e.getMessage());
            lblError.setVisible(true);
            return;
        }
        
        parent.initialiseerTableViewReservaties();
        Stage stage = (Stage) lblTitel.getScene().getWindow();
        stage.close();
        
    }

    @FXML
    private void onActionBtnAnnuleer(ActionEvent event) {
        Stage stage = (Stage) lblTitel.getScene().getWindow();
        stage.close();
    }
    
    private LocalDateTime convertToLocalDateTime(LocalDate datum, String tijd) {
        if (!tijd.contains(":")) {
            throw new IllegalArgumentException("Tijd moet er als volgt uit zien: uur:minuten");
        }
        int uur = Integer.parseInt(tijd.substring(0, tijd.indexOf(":")));
        int minuten = Integer.parseInt(tijd.substring(tijd.indexOf(":") + 1, tijd.length()));
        LocalTime time = LocalTime.of(uur, minuten);
        return LocalDateTime.of(datum, time);
    }

    

}
