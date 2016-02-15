package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
    private RadioButton rdbBeschikbaar;
    @FXML
    private Button btnBewerk;
    @FXML
    private Button btnVerwijder;
    @FXML
    private Button btnDetail;
    @FXML
    private GridPane materiaalDetailsBox;

    public MateriaalBoxController(BorderPane parent, String naam, String beschrijving){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MateriaalBox.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        this.lblNaam.setText(naam);
        this.txtaBeschrijving.setText(beschrijving);
        getChildren().remove(this.materiaalDetailsBox);
    }
    
    @FXML
    private void onDetailsAction(){
        ObservableList<Node> children = getChildren();
        if (children.contains(materiaalDetailsBox)){
            children.remove(materiaalDetailsBox);
        }else{
            children.add(materiaalDetailsBox);
        }
    }
    
}
