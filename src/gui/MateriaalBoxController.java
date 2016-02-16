package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import shared.MateriaalView;

public class MateriaalBoxController extends VBox {

    @FXML
    private ImageView imgvFoto;
    @FXML
    private Label lblNaam;
    @FXML
    private Label lblCode;
    @FXML
    private Label lblAantal;
    @FXML
    private TextArea txtaBeschrijving;
    @FXML
    private Label lblPrijs;
    @FXML
    private Label lblLocatie;
    @FXML
    private Button btnBewerk;
    @FXML
    private Button btnVerwijder;
    @FXML
    private Button btnDetail;
    @FXML
    private GridPane materiaalDetailsBox;
    @FXML
    private Label lblDoelGroepen;
    @FXML
    private Label lblLeergebieden;
    @FXML
    private Label lblFirmaNaam;
    @FXML
    private Label lblEmailFirma;
    @FXML
    private CheckBox ckbBeschikbaar;

    private MateriaalView mv;

    public MateriaalBoxController(MateriaalView mv) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MateriaalBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        setupMateriaal(mv);
        getChildren().remove(this.materiaalDetailsBox);
    }

    private void setupMateriaal(MateriaalView mv) {
        this.mv = mv;

        lblNaam.setText(mv.getNaam());
        int beschikbaar = mv.getAantal() - mv.getAantalOnbeschikbaar();
        lblAantal.setText(String.format("%d van de %d beschikbaar", beschikbaar, mv.getAantal()));
        if (mv.getPrijs() != -1.0d) {
            lblPrijs.setText(String.format("€ %.2f per stuk", mv.getPrijs()));
        }

        if (isNotEmpty(mv.getOmschrijving())) {
            txtaBeschrijving.setText(mv.getOmschrijving());
        }
        if (isNotEmpty(mv.getFotoUrl())) {
            // TODO: werkt nog niet
            File file = new File("../images/"+mv.getFotoUrl());
            imgvFoto.setImage(new Image(file.toURI().toString()));
        }
        if (isNotEmpty(mv.getArtikelNummer())) {
            lblCode.setText(mv.getArtikelNummer());
        }
        if (isNotEmpty(mv.getPlaats())) {
            lblLocatie.setText(mv.getPlaats());
        }
        ckbBeschikbaar.setSelected(mv.isUitleenbaarheid());
//        List<String> doelgroepen = mv.getDoelgroepen();
//        if (doelgroepen != null && !doelgroepen.isEmpty()) {
//            lblDoelGroepen.setText(doelgroepen.stream().collect(Collectors.joining(", ")));
//        }
//        List<String> leergebieden = mv.getLeergebieden();
//        if (leergebieden != null && !leergebieden.isEmpty()) {
//            lblLeergebieden.setText(leergebieden.stream().collect(Collectors.joining(", ")));
//        }
        if (isNotEmpty(mv.getFirma())) {
            lblFirmaNaam.setText(mv.getFirma());
        }
        if (isNotEmpty(mv.getEmailFirma())) {
            lblEmailFirma.setText(mv.getEmailFirma());
        }
    }

    private boolean isNotEmpty(String string) {
        return string != null && !string.trim().isEmpty();
    }

    @FXML
    private void onDetailsAction() {
        ObservableList<Node> children = getChildren();
        if (children.contains(materiaalDetailsBox)) {
            children.remove(materiaalDetailsBox);
        } else {
            children.add(materiaalDetailsBox);
        }
    }

    @FXML
    private void onBeschikbaarAction(ActionEvent event) {
        ckbBeschikbaar.setSelected(mv.isUitleenbaarheid());
    }

}
