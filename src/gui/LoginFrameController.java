package gui;

import domein.DomeinController;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class LoginFrameController extends BorderPane {

    private DomeinController dc;
    
    @FXML
    private ImageView imgBackground;
    @FXML
    private ImageView imgLogo;
    @FXML
    private Label lblGeefIn;
    @FXML
    private TextField txfEmailadres;
    @FXML
    private Button btnAanmelden;
    @FXML
    private Label lblGeenAccount;
    @FXML
    private Button btnSlot;
    @FXML
    private PasswordField pfWachtwoord;
    @FXML
    private Label lblIncorrect;
    @FXML
    private TextField txfWachtwoord;
    
    public LoginFrameController(DomeinController dc){
        
        this.dc = dc;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginFrame.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        txfWachtwoord.setVisible(false);
        
        //log in op enter press
        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    meldAan();
                }
            }
        });     
        
    }
    
    @FXML
    private void btnAanmeldenOnAction(ActionEvent event) {
        meldAan();
    }
    
    @FXML
    private void btnSlotOnAction(MouseEvent event) {
        txfWachtwoord.setText(pfWachtwoord.getText());
        toggleWachtwoordVisibility(true, false, "🔓");// open slot symbool
    }

    @FXML
    private void btnSlotOnRelease(MouseEvent event) {
        pfWachtwoord.setText(txfWachtwoord.getText());
        toggleWachtwoordVisibility(false, true, "🔒");// gesloten slot symbool
    }

    @FXML
    private void txfEmailadresOnKey(KeyEvent event) {
        lblIncorrect.setVisible(false);
    }

    @FXML
    private void pfWachtwoordOnKey(KeyEvent event) {
        lblIncorrect.setVisible(false);
    }
    
    private void toggleWachtwoordVisibility(boolean txfB, boolean pfB, String symbool){
        txfWachtwoord.setVisible(txfB);
        pfWachtwoord.setVisible(pfB);
        btnSlot.setText(symbool);
    }
    
    private void meldAan() {
        try {
            String email = txfEmailadres.getText().trim();
            String wachtwoord = pfWachtwoord.getText().trim();

            if (!dc.meldAan(email, wachtwoord)){
                throw new IllegalArgumentException("Emailadres of wachtwoord is incorrect. Gelieve opnieuw te proberen.");
            }
            
            System.out.println("GELUKT!");
            new MainMenuFrameController(this.dc);

            
        } catch (Exception e) {
            pfWachtwoord.setText("");
            lblIncorrect.setVisible(true);
        }
    }
    
}
