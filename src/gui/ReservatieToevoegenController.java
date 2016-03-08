/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import shared.MateriaalView;

/**
 * FXML Controller class
 *
 * @author alexa
 */
public class ReservatieToevoegenController extends BorderPane {

    @FXML
    private TextField txfEmailadres;
    @FXML
    private DatePicker dpOphaalmoment;
    @FXML
    private DatePicker dpIndienmoment;
    @FXML
    private ComboBox<MateriaalView> combMaterialen;

    
    

    @FXML
    private void btngaTerugOnAction(ActionEvent event) {
    }

    @FXML
    private void btnReservatieToevoegenOnAction(ActionEvent event) {
    }
    
}
