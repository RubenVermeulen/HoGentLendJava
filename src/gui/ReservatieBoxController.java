/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.DomeinController;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import shared.MateriaalView;
import shared.ReservatieLijnView;

/**
 * FXML Controller class
 *
 * @author Xander
 */
public class ReservatieBoxController extends GridPane{

    private DomeinController dc;
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
    private Label lblAantalBeschikbaar;
    @FXML
    private Button btnWijzig;
    @FXML
    private Button btnVerwijder;
    @FXML
    private Label lblIndienmoment;

    ReservatieBoxController(ReservatieLijnView rlv, DomeinController dc) {
        this.dc = dc;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReservatieBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        setupReservaties(rlv);
    }



    private void setupReservaties(ReservatieLijnView rlv) {
        this.rlv = rlv;
        this.mv = rlv.getMateriaal();
        
        lblNaam.setText(mv.getNaam());
        int beschikbaar = mv.getAantal() - mv.getAantalOnbeschikbaar();
        lblAantal.setText(String.format("%d van de %d beschikbaar", beschikbaar, mv.getAantal()));

        if (isNotEmpty(mv.getFotoUrl())) {
            InputStream ins = getClass().getResourceAsStream("/images/"+String.valueOf(mv.getFotoUrl()));
            if (ins == null)
                System.out.println("input stream is null :((((" + "/images/"+String.valueOf(mv.getFotoUrl()));
            if (ins != null)
                imgvFoto.setImage(new Image(ins));
        }
        if (isNotEmpty(mv.getArtikelNummer())) {
            lblCode.setText(mv.getArtikelNummer());
        }
        if (isNotEmpty(mv.getPlaats())) {
            lblLocatie.setText(mv.getPlaats());
        }
        
//        if (isNotEmpty(mv.getDoelgroepen())) {
//            lblDoelGroepen.setText(mv.getDoelgroepen().stream().collect(Collectors.joining(", ")));
//        }
//        
//        if (isNotEmpty(mv.getLeergebieden())) {
//            lblLeergebieden.setText(mv.getLeergebieden().stream().collect(Collectors.joining(", ")));
//        }
//        
//        if (isNotEmpty(mv.getFirma())) {
//            lblFirmaNaam.setText(mv.getFirma());
//        }
//        if (isNotEmpty(mv.getEmailFirma())) {
//            lblEmailFirma.setText(mv.getEmailFirma());
//        }
    }

    private boolean isNotEmpty(String string) {
        return string != null && !string.trim().isEmpty();
    }
    
    private boolean isNotEmpty(List<String> strings) {
        return strings != null && !strings.isEmpty();
    }

    @FXML
    private void onBtnWijzig(ActionEvent event) {
    }

    @FXML
    private void onBtnVerwijder(ActionEvent event) {
    }
        
    
    
}
