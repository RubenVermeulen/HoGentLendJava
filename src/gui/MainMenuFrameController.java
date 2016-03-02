package gui;

import domein.DomeinController;
import domein.Gebruiker;
import java.io.IOException;
import java.util.ArrayList;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import shared.MateriaalView;
import shared.ReservatieView;

public class MainMenuFrameController extends BorderPane {

    private DomeinController domCon;

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
    private TableView<?> tvReservaties;
    @FXML
    private TableColumn<?, ?> tcOphaalmoment;
    @FXML
    private TableColumn<?, ?> tcIndienmoment;
    @FXML
    private TableColumn<?, ?> tcLener;
    @FXML
    private TableColumn<?, ?> tcMaterialen;

    public MainMenuFrameController(DomeinController domCon) {
        this.domCon = domCon;

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
        if ( ! domCon.getAangemelde().isHoofdbeheerder()) {
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
        tvBeheerders.setItems(domCon.geefAlleBeheerders());
    }
    
    private void promptBeheerderToevoegen() {
        Scene promptScene = new Scene(new VoegBeheerderToeBoxController(domCon, this), 300, 200);
        Stage prompt = new Stage();
        prompt.initModality(Modality.APPLICATION_MODAL);
        prompt.initOwner(getScene().getWindow());
        prompt.setScene(promptScene);
        prompt.show();
    }
    
    private void initialiseerTableViewReservaties(){
        
        List<ReservatieView> reservaties = domCon.geefAlleReservaties();
        
        tvReservaties.setPlaceholder(new Label("Er zijn nog geen reservaties."));
        
        ObservableList<List<String>> ol = FXCollections.observableArrayList();
        
        tcOphaalmoment.setCellValueFactory(new PropertyValueFactory<>("ophaalmoment"));
        tcIndienmoment.setCellValueFactory(new PropertyValueFactory<>("indienmoment"));
        tcLener.setCellValueFactory(new PropertyValueFactory<>("lener"));
        tcMaterialen.setCellValueFactory(new PropertyValueFactory<>("materialen"));
        
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
        System.out.println(txfZoekMateriaalFilter.getText());
        List<MateriaalView> mvs = domCon.geefMaterialenMetFilter(txfZoekMateriaalFilter.getText());
        System.out.println(mvs);
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

                System.out.println("Beheerder verwijderd");
            }
        }

    }

}
