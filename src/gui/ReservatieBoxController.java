/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import shared.MateriaalView;
import shared.ReservatieLijnView;
import shared.ReservatieView;
import util.ImageUtil;

/**
 * FXML Controller class
 *
 * @author Xander
 */
public class ReservatieBoxController extends GridPane {

    private DomeinController dc;
    private ReservatieView rv;
    private ReservatieLijnView rlv;
    private MateriaalView mv;

    @FXML
    private ImageView imgvFoto;
    @FXML
    private Label lblNaam;
    @FXML
    private Label lblAantal;
    @FXML
    private Label lblCode;
    @FXML
    private Label lblLocatie;
    @FXML
    private Label lblOphaalmoment;
    @FXML
    private Button btnWijzig;
    @FXML
    private Button btnVerwijder;
    @FXML
    private Label lblIndienmoment;
    @FXML
    private Label lblAantalGereserveerd;
    @FXML
    private Button btnBekijk;
    @FXML
    private Button btnBevestigWijziging;
    @FXML
    private TextField txfGereserveerd;
    @FXML
    private Label lblAantalGereserveerdWijzig;
    @FXML
    private DatePicker dpOphaalmoment;
    @FXML
    private DatePicker dpIndienmoment;
    @FXML
    private TextField txfOphaalmoment;
    @FXML
    private TextField txfIndienmoment;
    @FXML
    private Button btnAnnuleerWijziging;

    private MainMenuFrameController parentController;

    ReservatieBoxController(ReservatieLijnView rlv, ReservatieView rv, DomeinController dc, MainMenuFrameController parentController) {
        this.dc = dc;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReservatieBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        this.parentController = parentController;

        btnBevestigWijziging.setVisible(false);
        btnAnnuleerWijziging.setVisible(false);
        this.rv = rv;
        setupReservaties(rlv);
    }

    private void setupReservaties(ReservatieLijnView rlv) {
        this.rlv = rlv;
        this.mv = rlv.getMateriaal();

        lblNaam.setText(mv.getNaam());
        int beschikbaar = mv.getAantal() - mv.getAantalOnbeschikbaar();
        lblAantal.setText(String.format("Nog %d van de %d beschikbaar", beschikbaar, mv.getAantal()));

        imgvFoto.setImage(ImageUtil.byteArrayToImage(mv.getFotoBytes()));

        if (isNotEmpty(mv.getArtikelNummer())) {
            lblCode.setText(mv.getArtikelNummer());
        }
        if (isNotEmpty(mv.getPlaats())) {
            lblLocatie.setText(mv.getPlaats());
        }

        int conflict = dc.heeftConflicten(rlv, rv.getReservatiemoment());
        if (conflict < 0) {
            lblAantal.setText(String.format("Conflict! Slechts %d beschikbaar", rlv.getAantal() + conflict));
            if (rlv.getAantal() + conflict < 0) {
                lblAantal.setText("Conflict! Geen beschikbaar!");
            }
            lblAantal.setTextFill(Color.web("#d70000"));
            lblAantalGereserveerd.setTextFill(Color.web("#d70000"));
        }
        lblAantalGereserveerd.setText(String.valueOf(rlv.getAantal()) + " gereserveerd");
        lblOphaalmoment.setText(rlv.getOphaalmomentAlsString());
        lblIndienmoment.setText(rlv.getIndienmomentAlsString());

    }

    private boolean isNotEmpty(String string) {
        return string != null && !string.trim().isEmpty();
    }

    private boolean isNotEmpty(List<String> strings) {
        return strings != null && !strings.isEmpty();
    }

    @FXML
    private void onBtnWijzig(ActionEvent event) {

        setVisibleBewerken(true);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        txfGereserveerd.setText(String.valueOf(rlv.getAantal()));
        dpOphaalmoment.setValue(rlv.getOphaalmoment().toLocalDate());
        txfOphaalmoment.setText(rlv.getOphaalmoment().format(formatter));
        dpIndienmoment.setValue(rlv.getIndienmoment().toLocalDate());
        txfIndienmoment.setText(rlv.getIndienmoment().format(formatter));

    }

    @FXML
    private void onBtnVerwijder(ActionEvent event) {

        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                String.format("Ben je zeker dat je het materiaal %s uit de reservatie van %s wilt verwijderen?",
                        mv.getNaam(), rv.getLener()),
                ButtonType.CANCEL,
                ButtonType.OK);

        alert.setTitle("Opgelet");
        alert.setHeaderText("Opgelet");

        ImageView iv = new ImageView(ImageUtil.byteArrayToImage(mv.getFotoBytes()));
        iv.setFitHeight(70);
        iv.setFitWidth(70);
        iv.setPreserveRatio(true);
        alert.setGraphic(iv);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            
            
            rv.getReservatieLijnen().remove(rlv);
            dc.wijzigReservatie(rv);
            parentController.initialiseerTableViewReservaties();
//            ((VBox) getParent()).getChildren().remove(this);
        }

    }

    @FXML
    private void onBtnBekijk(ActionEvent event) {
        parentController.zetOpMateriaal(mv.getNaam());
    }

    @FXML
    private void onBtnBevestigWijziging(ActionEvent event) {
        int aantalRlv = rlv.getAantal();
        rlv.setOphaalmoment(convertToLocalDateTime(dpOphaalmoment.getValue(), txfOphaalmoment.getText()));
        rlv.setIndienmoment(convertToLocalDateTime(dpIndienmoment.getValue(), txfIndienmoment.getText()));
        if (txfGereserveerd.getText() != null && !txfGereserveerd.getText().isEmpty()) {
            int aantalTxf = Integer.parseInt(txfGereserveerd.getText());
            if(aantalRlv != aantalTxf){
                mv.setAantalOnbeschikbaar(mv.getAantalOnbeschikbaar()-aantalRlv+aantalTxf);
                rlv.setAantal(aantalTxf);
            }
        }
        try {
            dc.wijzigReservatie(rv);
            setVisibleBewerken(false);
            setupReservaties(rlv);
            parentController.initialiseerTableViewReservaties();
        } catch (IllegalArgumentException e) {
            Alert informationAlert = new Alert(Alert.AlertType.ERROR);

            informationAlert.setTitle("Opgelet");
            informationAlert.setHeaderText("Opgelet");
            informationAlert.setContentText(e.getMessage());

            informationAlert.showAndWait();

        }

    }

    @FXML
    private void onBtnAnnuleerWijziging(ActionEvent event) {
        setVisibleBewerken(false);
    }

    public void setVisibleBewerken(boolean b) {
        btnBekijk.setVisible(!b);
        btnVerwijder.setVisible(!b);
        btnWijzig.setVisible(!b);
        btnBevestigWijziging.setVisible(b);
        btnAnnuleerWijziging.setVisible(b);

        txfGereserveerd.setVisible(b);
        lblAantalGereserveerdWijzig.setVisible(b);
        dpOphaalmoment.setVisible(b);
        txfOphaalmoment.setVisible(b);
        dpIndienmoment.setVisible(b);
        txfIndienmoment.setVisible(b);

        lblAantalGereserveerd.setVisible(!b);
        lblOphaalmoment.setVisible(!b);
        lblIndienmoment.setVisible(!b);
    }

    private LocalDateTime convertToLocalDateTime(LocalDate datum, String tijd) {
        if (!tijd.contains(":")) {
            throw new IllegalArgumentException("Tijd moet er als volgt uit zien: uur:minuten");
        }
        int uur = Integer.parseInt(tijd.substring(0, tijd.indexOf(":")));
        int minuten = Integer.parseInt(tijd.substring(tijd.indexOf(":") + 1, tijd.length()));
        System.out.println("dit zoek ik");
        System.out.println(uur + ":" + minuten);
        System.out.println(datum.toString());
        LocalTime time = LocalTime.of(uur, minuten);
        return LocalDateTime.of(datum, time);
    }

}
