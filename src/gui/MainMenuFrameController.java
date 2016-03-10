package gui;

import domein.DomeinController;
import domein.gebruiker.Gebruiker;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.ConfigView;
import shared.MateriaalView;
import shared.ReservatieLijnView;
import shared.ReservatieView;

public class MainMenuFrameController extends BorderPane {

    private DomeinController domCon;
    private ReservatieBoxController rbc;
    private ReservatieView geselecteerdeReservatie;
    private int geselecteerdeRij;

    @FXML
    private VBox materialenBox;
    @FXML
    private Label lblWelkomInfo;
    @FXML
    private Label lblEmailInfo;
    @FXML
    private TextField txfZoekMateriaalFilter;
    @FXML
    private TableView<Gebruiker> tvBeheerders;
    @FXML
    private TableColumn<Gebruiker, String> clNaam;
    @FXML
    private TableColumn<Gebruiker, String> clVoornaam;
    @FXML
    private TableColumn<Gebruiker, String> clEmail;
    @FXML
    private Button btnStelAanAlsBeheerder;
    @FXML
    private Button btnVerwijderBeheerder;
    @FXML
    private TableView<ReservatieView> tvReservaties;
    @FXML
    private TableColumn<ReservatieView, LocalDateTime> tcOphaalmoment;
    @FXML
    private TableColumn<ReservatieView, String> tcIndienmoment;
    @FXML
    private TableColumn<ReservatieView, String> tcLener;
    @FXML
    private TableColumn<ReservatieView, String> tcMaterialen;
    @FXML
    private VBox boxReservatieLijn;
    @FXML
    private Label lblLenerNaam;
    @FXML
    private Label lblOphaalmoment;
    @FXML
    private Label lblIndienmoment;
    @FXML
    private Button btnVoegResevatieToe;
    @FXML
    private TextField txfZoekReservatie;
    @FXML
    private DatePicker dtmStartDatum;
    @FXML
    private Button btnZoekReservatie;
    @FXML
    private DatePicker dtmEindDatum;
    @FXML
    private Button btnVerwijderReservatie;
    @FXML
    private Button btnWijzigReservatieDetails;
    @FXML
    private TextField txfOphaalmoment;
    @FXML
    private TextField txfIndienmoment;
    @FXML
    private Label lblTotWijzigDetailsReservatie;
    @FXML
    private Label lblDetailOphaalmoment;
    @FXML
    private Label lblDetailIndienmoment;
    @FXML
    private Button btnBevestigWijzigingDetails;
    @FXML
    private Button btnAnnuleerWijzigingDetails;
    @FXML
    private Label lblStatus;
    @FXML
    private Label lblReservatiemoment;
    @FXML
    private Tab materiaalBeheerTab;
    @FXML
    private TabPane tabPane;
    @FXML
    private Button btnReservatieOpgehaald;
    @FXML
    private DatePicker dpOphaalmoment;
    @FXML
    private DatePicker dpIndienmoment;
    @FXML
    private Button btnVoegReservatieLijnToe;
    @FXML
    private Button btnInstellingenOpslaan;
    @FXML
    private TextField txfInstellingenOphaaltijd;
    @FXML
    private TextField txfInstellingenIndientijd;
    @FXML
    private Label lblInstellingenMessage;

    private ConfigView configView;
    @FXML
    private ComboBox<String> cbInstellingenOphaalDag;
    @FXML
    private ComboBox<String> cbInstellingenIndienDag;

    private Color colorSucces = Color.web("#04B431");
    private Color colorError = Color.web("FF0000");

    public MainMenuFrameController(DomeinController domCon) {
        this.domCon = domCon;
        this.configView = domCon.geefConfigView();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenuFrame.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        setupWelkomEnEmailLabels();
        setupMaterials(domCon.geefAlleMaterialen());

        initialiseerTableViewBeheerders();

        initialiseerTableViewReservaties();

        initialiseerInstellingen();
    }

    private void setupMaterials(List<MateriaalView> materials) {
        materialenBox.getChildren().clear();
        materials.stream().forEach(mv -> materialenBox.getChildren().add(new MateriaalBoxController(mv, domCon)));
    }

    private void setupWelkomEnEmailLabels() {
        String[] gebruikerInfo = domCon.geefGegevensAangemeldeGebruiker();
        lblWelkomInfo.setText(String.format("Welkom %s %s!", gebruikerInfo[0], gebruikerInfo[1]));
        lblEmailInfo.setText(String.format("Email: %s", gebruikerInfo[2]));
    }

    private void initialiseerTableViewBeheerders() {
        // Wijzig standaard tekst bij lege tabel
        tvBeheerders.setPlaceholder(new Label("Er zijn nog geen beheerders"));

        // Disable knoppen als je geen hoofdbeheerder bent
        if (!domCon.isAangemeldeHoofdbeheerder()) {
            btnStelAanAlsBeheerder.setDisable(true);
            btnVerwijderBeheerder.setDisable(true);
        }

        // Koppel een kolom aan een attribuut
        clNaam.setCellValueFactory(new PropertyValueFactory<>("achternaam"));
        clVoornaam.setCellValueFactory(new PropertyValueFactory<>("voornaam"));
        clEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        vulTableViewOpMetBeheerders();
    }

    public void vulTableViewOpMetBeheerders() {
        // TableView opvullen met data
        tvBeheerders.setItems(domCon.geefObservableListBeheerdersZonderHoofdBeheerders());
    }

    private void promptBeheerderToevoegen() {
        Scene promptScene = new Scene(new VoegBeheerderToeBoxController(domCon, this), 300, 200);
        Stage prompt = new Stage();
        prompt.initModality(Modality.APPLICATION_MODAL);
        prompt.initOwner(getScene().getWindow());
        prompt.setScene(promptScene);
        prompt.show();
    }

    protected void initialiseerTableViewReservaties() {
        setupTableViewReservaties(domCon.geefAlleReservaties());
    }

    protected void initialiseerTableViewReservatiesMetFilter() {
        setupTableViewReservaties(
                domCon.geefAlleReservatiesMetFiler(
                        txfZoekReservatie.getText(),
                        dtmStartDatum.getValue() == null ? null : LocalDateTime.of(dtmStartDatum.getValue(), LocalTime.of(0, 0)),
                        dtmEindDatum.getValue() == null ? null : LocalDateTime.of(dtmEindDatum.getValue(), LocalTime.of(0, 0))
                )
        );
    }

    protected void setupTableViewReservaties(List<ReservatieView> reservaties) {
        tvReservaties.setPlaceholder(new Label("Er zijn geen reservaties."));

        ObservableList<ReservatieView> observableList = FXCollections.unmodifiableObservableList(
                FXCollections.observableArrayList(reservaties.stream().collect(Collectors.toList())
                ));

        tcOphaalmoment.setCellValueFactory(new PropertyValueFactory<>("ophaalmomentAlsString"));
        tcIndienmoment.setCellValueFactory(new PropertyValueFactory<>("indienmomentAlsString"));
        tcLener.setCellValueFactory(new PropertyValueFactory<>("lener"));
        tcMaterialen.setCellValueFactory(new PropertyValueFactory<>("reservatieLijnenAlsString"));

        tvReservaties.setItems(observableList);
        
//        for(ReservatieView rv : tvReservaties.getItems()){
//            for(ReservatieLijnView rlv : rv.getReservatieLijnen()){
//                if(rlv.getMateriaal().getAantalOnbeschikbaar()>rlv.getMateriaal().getAantal()){
//                    tvReservaties.getSelectionModel().;
//                }
//        }

        if (geselecteerdeReservatie != null) {
            tvReservaties.getSelectionModel().select(geselecteerdeRij);
        }

        tvReservaties.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                setupReservatieLijnen(newSelection);
                geselecteerdeReservatie = newSelection;
                geselecteerdeRij = tvReservaties.getSelectionModel().getSelectedIndex();
                setVisibilityWijzigDetailsMateriaal(false);
            } else {
                boxReservatieLijn.getChildren().clear();
                lblIndienmoment.setText("-");
                lblOphaalmoment.setText("-");
                lblReservatiemoment.setText("-");
                lblLenerNaam.setText("-");
                lblStatus.setText("-");
            }
        });

    }

    private void setupReservatieLijnen(ReservatieView rv) {
        
        //Reservatiedetails
        lblLenerNaam.setText(rv.getLener());
        lblOphaalmoment.setText(rv.getOphaalmomentAlsString());
        lblIndienmoment.setText(rv.getIndienmomentAlsString());
        lblReservatiemoment.setText(rv.getReservatiemomentAlsString());
        lblStatus.setTextFill(Color.web("#000000"));

        boolean status = rv.isOpgehaald();
        if (status == true) {
            lblStatus.setText("Opgehaald.");
        } else {
            lblStatus.setText("Nog niet opgehaald");
        }
        if(rv.isOpgehaald()){
            btnReservatieOpgehaald.setText("Markeer reservatie als nog niet opgehaald");
        }
        else{
            btnReservatieOpgehaald.setText("Markeer reservatie als opgehaald");
        }
        
        
        
        //ReservatieBox
        List<ReservatieLijnView> rlv = rv.getReservatieLijnen();
        boxReservatieLijn.getChildren().clear();
        rlv.stream().forEach(rl -> boxReservatieLijn.getChildren().add(new ReservatieBoxController(rl, rv, domCon, this)));

    }

    public void setConflict(ReservatieLijnView rlv) {
        lblStatus.setText("Conflict! Niet alle materialen kunnen worden opgehaald!");
        lblStatus.setTextFill(Color.web("#d70000"));
    }

    @FXML
    private void menuActionUitloggen(ActionEvent event) {
        domCon.meldAf();
        Stage stage = (Stage) getScene().getWindow();
        Scene scene = new Scene(new LoginFrameController(domCon));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onActionVoegMateriaalToe(ActionEvent event) {
        Stage stage = (Stage) getScene().getWindow();
        Scene scene = new Scene(new MateriaalToevoegenController(domCon, null));
        stage.setScene(scene);
    }

    @FXML
    private void onActionVoegToeInBulk(ActionEvent event) {
        Stage stage = (Stage) getScene().getWindow();
        Scene scene = new Scene(new MateriaalToevoegenInBulkController(domCon));
        stage.setScene(scene);
    }

    @FXML
    private void menuActionAfsluiten(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void onBtnZoekMateriaalAction(ActionEvent event) {
        List<MateriaalView> mvs = domCon.geefMaterialenMetFilter(txfZoekMateriaalFilter.getText());
        setupMaterials(mvs);
    }

    @FXML
    private void onActionTxfZoekMateriaalFilter(ActionEvent event) {
        onBtnZoekMateriaalAction(event);
    }

    @FXML
    private void onActionBtnStelAanAlsBeheerder(ActionEvent event) {
        promptBeheerderToevoegen();
    }

    @FXML
    private void onActionBtnVerwijderBeheerder(ActionEvent event) {
        Gebruiker geselecteerdeBeheerder = tvBeheerders.getSelectionModel().getSelectedItem();

        if (geselecteerdeBeheerder == null) {
            Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);

            informationAlert.setTitle("Informatie");
            informationAlert.setHeaderText("Informatie");
            informationAlert.setContentText("Je hebt geen beheerder geselecteerd om te verwijderen.");

            informationAlert.showAndWait();
        } else {
            Alert alert = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    String.format("Ben je zeker dat je de beheerder \"%s\" wilt verwijderen?", geselecteerdeBeheerder.getEmail()),
                    ButtonType.CANCEL,
                    ButtonType.OK);

            alert.setTitle("Opgelet");
            alert.setHeaderText("Opgelet");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                domCon.verwijderBeheerder(geselecteerdeBeheerder);

                vulTableViewOpMetBeheerders();
            }
        }

    }

    @FXML
    private void onActionBtnVoegReservatieToe(ActionEvent event) {
        Stage stage = (Stage) getScene().getWindow();
        Scene scene = new Scene(new ReservatieToevoegenController(domCon));
        stage.setScene(scene);

    }

    @FXML
    private void onActionTxfZoekReservatie(ActionEvent event) {
        onActionBtnZoekReservatie(event);
    }

    @FXML
    private void onActionDtmStartDatum(ActionEvent event) {
        onActionBtnZoekReservatie(event);
    }

    @FXML
    private void onActionBtnZoekReservatie(ActionEvent event) {
        geselecteerdeReservatie = null;
        applyDatePickerValue(dtmStartDatum);
        applyDatePickerValue(dtmEindDatum);
        initialiseerTableViewReservatiesMetFilter();
    }

    @FXML
    private void onActionDtmEindDatum(ActionEvent event) {
        onActionBtnZoekReservatie(event);
    }

    @FXML
    private void onActionBtnVerwijderReservatie(ActionEvent event) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                String.format("Ben je zeker dat je de reservatie van %s wilt verwijderen?",
                        geselecteerdeReservatie.getLener()),
                ButtonType.CANCEL,
                ButtonType.OK);

        alert.setTitle("Opgelet");
        alert.setHeaderText("Opgelet");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            domCon.verwijderReservatie(geselecteerdeReservatie);

            initialiseerTableViewReservaties();
        }
    }

    @FXML
    private void onActionBtnWijzigReservatieDetails(ActionEvent event) {
        setVisibilityWijzigDetailsMateriaal(true);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        dpOphaalmoment.setValue(geselecteerdeReservatie.getOphaalmoment().toLocalDate());
        txfOphaalmoment.setText(geselecteerdeReservatie.getOphaalmoment().format(formatter));
        dpIndienmoment.setValue(geselecteerdeReservatie.getIndienmoment().toLocalDate());
        txfIndienmoment.setText(geselecteerdeReservatie.getIndienmoment().format(formatter));
    }

    private void setVisibilityWijzigDetailsMateriaal(boolean b) {
        btnWijzigReservatieDetails.setVisible(!b);
        btnVoegReservatieLijnToe.setVisible(!b);
        btnVerwijderReservatie.setVisible(!b);

        btnBevestigWijzigingDetails.setVisible(b);
        btnAnnuleerWijzigingDetails.setVisible(b);

        lblDetailOphaalmoment.setVisible(!b);
        lblDetailIndienmoment.setVisible(!b);
        lblOphaalmoment.setVisible(!b);
        lblIndienmoment.setVisible(!b);

        dpOphaalmoment.setVisible(b);
        dpIndienmoment.setVisible(b);
        txfOphaalmoment.setVisible(b);
        txfIndienmoment.setVisible(b);
        lblTotWijzigDetailsReservatie.setVisible(b);
    }

    @FXML
    private void onActionBtnBevestigWijzigingDetails(ActionEvent event) {
        applyDatePickerValue(dpOphaalmoment);
        applyDatePickerValue(dpIndienmoment);
        geselecteerdeReservatie.setOphaalmoment(convertToLocalDateTime(dpOphaalmoment.getValue(), txfOphaalmoment.getText()));
        geselecteerdeReservatie.setIndienmoment(convertToLocalDateTime(dpIndienmoment.getValue(), txfIndienmoment.getText()));
        try {
            domCon.wijzigReservatie(geselecteerdeReservatie);
            setVisibilityWijzigDetailsMateriaal(false);
            initialiseerTableViewReservaties();
            setupReservatieLijnen(geselecteerdeReservatie);
        } catch (IllegalArgumentException e) {
            Alert informationAlert = new Alert(Alert.AlertType.ERROR);

            informationAlert.setTitle("Opgelet");
            informationAlert.setHeaderText("Opgelet");
            informationAlert.setContentText(e.getMessage());

            informationAlert.showAndWait();

        }

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

    @FXML
    private void onActionBtnAnnuleerWijzigingDetails(ActionEvent event) {
        setVisibilityWijzigDetailsMateriaal(false);
    }

    public void zetOpMateriaal(String materialName) {
        tabPane.getSelectionModel().select(materiaalBeheerTab);
        txfZoekMateriaalFilter.setText(materialName);
        onBtnZoekMateriaalAction(null);
    }

    @FXML
    private void onActionBtnReservatieOpgehaald(ActionEvent event) {
        geselecteerdeReservatie.setOpgehaald(!geselecteerdeReservatie.isOpgehaald());
        domCon.wijzigReservatie(geselecteerdeReservatie);
        initialiseerTableViewReservaties();
        setupReservatieLijnen(geselecteerdeReservatie);
    }

    @FXML
    protected void onActionVoegReservatieLijnToe(ActionEvent event) {
        Scene promptScene = new Scene(new VoegReservatieLijnToeBoxController(domCon, geselecteerdeReservatie, this)); //,width, hoogte);
        Stage prompt = new Stage();
        prompt.initModality(Modality.APPLICATION_MODAL);
        prompt.initOwner(getScene().getWindow());
        prompt.setScene(promptScene);
        prompt.show();
    }

    @FXML
    protected void onBtnActionToonAlle(ActionEvent event) {
        txfZoekReservatie.setText("");
        dtmStartDatum.setValue(null);
        dtmEindDatum.setValue(null);
        initialiseerTableViewReservatiesMetFilter();
    }

    private void applyDatePickerValue(DatePicker datumPick) {
        datumPick.setValue(datumPick.getConverter().fromString(datumPick.getEditor().getText()));
    }

    @FXML
    private void onActionBtnInstellingenOpslaan(ActionEvent event) {
        try {
            configView.setStandaardOphaalDag(cbInstellingenOphaalDag.getValue());
            configView.setStandaardOphaaltijd(convertToLocalDateTime(txfInstellingenOphaaltijd.getText(), "standaard ophaaltijd"));
            configView.setStandaardIndienDag(cbInstellingenIndienDag.getValue());
            configView.setStandaardIndientijd(convertToLocalDateTime(txfInstellingenIndientijd.getText(), "standaard indientijd"));

            domCon.saveConfig(configView);

            lblInstellingenMessage.setTextFill(colorSucces);
            lblInstellingenMessage.setText("De instellingen zijn succesvol opgeslagen.");

            initialiseerInstellingen();
        } catch (NumberFormatException e) {
            lblInstellingenMessage.setTextFill(colorError);
            lblInstellingenMessage.setText("Tijd moet er als volgt uit zien: uur:minuten");
        } catch (IllegalArgumentException e) {
            lblInstellingenMessage.setTextFill(colorError);
            lblInstellingenMessage.setText(e.getMessage());
        } catch (DateTimeException e) {
            lblInstellingenMessage.setTextFill(colorError);
            lblInstellingenMessage.setText("Eén van de velden bevat geen geldige tijd.");
        }

        lblInstellingenMessage.setVisible(true);
    }

    private void initialiseerInstellingen() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        txfInstellingenIndientijd.setText(configView.getStandaardIndientijd().format(formatter));
        txfInstellingenOphaaltijd.setText(configView.getStandaardOphaaltijd().format(formatter));

        List<String> dagen = new ArrayList<>(Arrays.asList("maandag", "dinsdag", "woensdag", "donderdag", "vrijdag"));

        cbInstellingenOphaalDag.getItems().clear();
        cbInstellingenOphaalDag.getItems().addAll(dagen);
        cbInstellingenIndienDag.getItems().clear();
        cbInstellingenIndienDag.getItems().addAll(dagen);

        cbInstellingenOphaalDag.getSelectionModel().select(configView.getStandaardOphaalDag());
        cbInstellingenIndienDag.getSelectionModel().select(configView.getStandaardIndienDag());
    }

    private LocalTime convertToLocalDateTime(String tijd, String veld) {
        if (tijd.isEmpty()) {
            throw new IllegalArgumentException(String.format("Het veld %s mag niet leeg zijn.", veld));
        }

        if (!tijd.contains(":")) {
            throw new IllegalArgumentException("Tijd moet er als volgt uit zien: uur:minuten");
        }

        int uur = Integer.parseInt(tijd.substring(0, tijd.indexOf(":")));
        int minuten = Integer.parseInt(tijd.substring(tijd.indexOf(":") + 1, tijd.length()));
        LocalTime time = LocalTime.of(uur, minuten);
        return time;
    }

    @FXML
    private void onKeyPressedTxfInstellingenOphaaltijd(KeyEvent event) {
        verbergInstellingenLabel();
    }

    @FXML
    private void onKeyPressedTxfInstellingenIndientijd(KeyEvent event) {
        verbergInstellingenLabel();
    }

    @FXML
    private void onActionCbInstellingenOphaalDag(ActionEvent event) {
        verbergInstellingenLabel();
    }

    @FXML
    private void onActionCbInstellingenIndiendag(ActionEvent event) {
        verbergInstellingenLabel();
    }

    private void verbergInstellingenLabel() {
        lblInstellingenMessage.setVisible(false);
    }
}
