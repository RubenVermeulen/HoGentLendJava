/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import shared.MateriaalView;
import shared.ReservatieView;
import shared.ReservatieLijnView;

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
    private TextField txfAantal;

    private final DomeinController dc;
    @FXML
    private TextField txfOphaalhhmm;
    @FXML
    private TextField txfIndienhhmm;

    public ReservatieToevoegenController(DomeinController dc) {
        this.dc = dc;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReservatieToevoegen.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setupAlleMaterialen();
    }

    @FXML
    private void btngaTerugOnAction(ActionEvent event) {
        Stage stage = (Stage) getScene().getWindow();
        Scene scene = new Scene(new MainMenuFrameController(dc));
        stage.setScene(scene);
    }

    @FXML
    private void btnReservatieToevoegenOnAction(ActionEvent event) {

        String emailLener = txfEmailadres.getText().trim();
        LocalDateTime ophaalmoment = null;
        LocalDateTime indienmoment = null;
        LocalDateTime reservatiemoment = LocalDateTime.now();

        LocalDate ophMoment = dpOphaalmoment.getValue();
        LocalDate indMoment = dpIndienmoment.getValue();
        StringBuffer indienHHmm = new StringBuffer(txfIndienhhmm.getText().trim());
        StringBuffer ophaalHHmm = new StringBuffer(txfIndienhhmm.getText().trim());

        int indienHh = Integer.parseInt(indienHHmm.substring(0, 1));
        int indienMm = Integer.parseInt(indienHHmm.substring(3, 4));
        int ophaalHh = Integer.parseInt(ophaalHHmm.substring(0, 1));
        int ophaalMm = Integer.parseInt(ophaalHHmm.substring(3, 4));

        ophaalmoment = ophMoment.atTime(indienHh, indienMm);
        indienmoment = indMoment.atTime(ophaalHh, ophaalMm);

        MateriaalView materiaalView = combMaterialen.getValue();
        int aantal = Integer.parseInt(txfAantal.getText());

        List<ReservatieLijnView> reservatieLijnen = new ArrayList<>();

        reservatieLijnen.add(new ReservatieLijnView(ophaalmoment, indienmoment, materiaalView, aantal));

        ReservatieView rv = new ReservatieView(emailLener, ophaalmoment, indienmoment, reservatiemoment, reservatieLijnen);

        try {
            dc.voegReservatieToe(rv);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage()); //moet nog label worden

        }

        Stage stage = (Stage) getScene().getWindow();
        Scene scene = new Scene(new MainMenuFrameController(dc));
        stage.setScene(scene);
    }

    private void setupAlleMaterialen() {
        combMaterialen.getItems().clear();
        combMaterialen.getItems().addAll(dc.geefAlleMaterialen());
    }

}
