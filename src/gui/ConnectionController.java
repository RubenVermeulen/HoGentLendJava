package gui;

import domein.DomeinController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ConnectionController extends BorderPane {

    @FXML
    private Text txtStatus;

    @FXML
    private Button btnContinue;
    
    private DomeinController dc;
    private Color colorSucces = Color.web("#04B431");
    private Color colorNeutral = Color.web("#000000");
    private Thread t;
     
    public ConnectionController(DomeinController dc) {

        this.dc = dc;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Connection.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        t = new Thread(new TestConnection());
        
        t.setDaemon(true);
        t.start();
        
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void btnContinueAction(ActionEvent event) {
        t.stop();
        
//        Stage stage = dc.getPrimaryStage();
//        Scene scene = new Scene(new LoginFrameController(this.dc));
//        stage.setScene(scene);
//        stage.show();

        Stage stage = (Stage) btnContinue.getScene().getWindow();
        stage.close();
    }
    
    class TestConnection implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                
                while (true) {
                    Thread.sleep(1000);
                    
                    if (dc.isConnectionAlive()) {
                        txtStatus.setText("Verbonden met de server");
                        txtStatus.setFill(colorSucces);
                        btnContinue.setDisable(false);
                    }
                    else {
                        txtStatus.setText("Verbinding maken met de server . . .");
                        txtStatus.setFill(colorNeutral);
                        btnContinue.setDisable(true);
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
