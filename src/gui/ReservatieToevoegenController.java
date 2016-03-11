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
import javafx.scene.control.Label;
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
    @FXML
    private Label lblError;

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

        verbergError();
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
if (emailLener == null||emailLener.isEmpty()) {
            lblError.setText("Je moet een e-mailadres invullen.");
            lblError.setVisible(true);
            return;
        }
        LocalDateTime ophaalmoment = null;
        LocalDateTime indienmoment = null;
        LocalDateTime reservatiemoment = LocalDateTime.now();

        LocalDate ophMoment = dpOphaalmoment.getValue();
        if (ophMoment == null) {
            lblError.setText("Je moet een ophaaldatum selecteren.");
            lblError.setVisible(true);
            return;
        }

        LocalDate indMoment = dpIndienmoment.getValue();
        if (indMoment == null) {
            lblError.setText("Je moet een indiendatum selecteren.");
            lblError.setVisible(true);
            return;
        }

        StringBuffer indienHHmm = new StringBuffer(txfIndienhhmm.getText().trim());

        int indienHh = -1;
        int indienMm = -1;
        try {
            if (indienHHmm == null) {
                throw new IllegalArgumentException("Je moet een indienmoment invullen.");
            }
            indienHh = Integer.parseInt(indienHHmm.substring(0, 2));
            indienMm = Integer.parseInt(indienHHmm.substring(3, 5));
        } catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
            if (e.getClass().getSimpleName().equals("StringIndexOutOfBoundsException")) {
                lblError.setText("Je moet een indienmoment invullen.");
                lblError.setVisible(true);
                System.out.println("show error");
            } else {

                lblError.setText(e.getMessage());
                lblError.setVisible(true);
                System.out.println("show error");
            }
            return;
        }

        StringBuffer ophaalHHmm = new StringBuffer(txfOphaalhhmm.getText().trim());

        int ophaalHh = -1;
        int ophaalMm = -1;
        try {
            if (ophaalHHmm == null) {
                throw new IllegalArgumentException("Je moet een ophaalmoment invullen.");

            }
            ophaalHh = Integer.parseInt(ophaalHHmm.substring(0, 2));
            ophaalMm = Integer.parseInt(ophaalHHmm.substring(3, 5));
        } catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
            if (e.getClass().getSimpleName().equals("StringIndexOutOfBoundsException")) {

                lblError.setText("Je moet een ophaalmoment invullen.");
                lblError.setVisible(true);
                System.out.println("show error");
            } else {

                lblError.setText(e.getMessage());
                lblError.setVisible(true);
                System.out.println("show error");
            }
            return;
        }

        try {
            if (!(ophaalHh >= 0 && ophaalHh < 24 && ophaalMm >= 0 && ophaalMm < 60)) {
                throw new IllegalArgumentException("Er is geen geldig ophaalmoment ingevuld.");
            }
            ophaalmoment = ophMoment.atTime(ophaalHh, ophaalMm);

        } catch (IllegalArgumentException e) {

            lblError.setText(e.getMessage());
            lblError.setVisible(true);
            System.out.println("show error");
            return;

        }
        try {
            if (!(indienHh >= 0 && indienHh < 24 && indienMm >= 0 && indienMm < 60)) {
                throw new IllegalArgumentException("Er is geen geldig indienmoment ingevuld.");
            }

            indienmoment = indMoment.atTime(indienHh, indienMm);
        } catch (IllegalArgumentException e) {

            lblError.setText(e.getMessage());
            lblError.setVisible(true);
            System.out.println("show error");
            return;
        }

        if (combMaterialen.getValue() == null) {
            lblError.setText("Je moet een materiaal selecteren.");
            lblError.setVisible(true);
            return;
        }

        MateriaalView materiaalView = combMaterialen.getValue();

        int aantal = -1;

        try {
            if (txfAantal.getText().isEmpty()) {
                throw new IllegalArgumentException("Er moet een aantal ingevuld worden.");
                
            }
            aantal = Integer.parseInt(txfAantal.getText());
        } catch (IllegalArgumentException e) {

            lblError.setText(e.getMessage());
            lblError.setVisible(true);
            System.out.println("show error");
            return;
        }

        List<ReservatieLijnView> reservatieLijnen = new ArrayList<>();

        reservatieLijnen.add(new ReservatieLijnView(ophaalmoment, indienmoment, materiaalView, aantal));

        ReservatieView rv = new ReservatieView(emailLener, ophaalmoment, indienmoment, reservatiemoment, reservatieLijnen);

        try {
            dc.voegReservatieToe(rv);
            Stage stage = (Stage) getScene().getWindow();
            Scene scene = new Scene(new MainMenuFrameController(dc));
            stage.setScene(scene);
            //System.out.println("ayy");   ga hier naar een de tab van reservatiebeheer

        } catch (IllegalArgumentException e) {

            lblError.setText(e.getMessage());
            lblError.setVisible(true);
            System.out.println("show error");
        }

    }

    public void verbergError() {
        lblError.setVisible(false);
        System.out.println("Verberg error");
    }

    private void setupAlleMaterialen() {
        combMaterialen.getItems().clear();

        combMaterialen.getItems().addAll(dc.geefAlleMaterialen());

    }

}
