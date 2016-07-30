package domein;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

public class CheckDatabaseConnection implements Runnable {

    private final int interval = 1000;
    private final String userName = "remote";
    private final String password = "remote";
    private final String connectionString = "jdbc:sqlserver://192.168.56.101\\SQLEXPRESS:1433;databaseName=HoGentLend";
    
    private Connection conn;
    private Statement stmt;
    private DomeinController dc;
    
    private boolean isBooting = true;

    public CheckDatabaseConnection(DomeinController dc) {
        this.dc = dc;
    }

    @Override
    public void run() {
        createConnection();

        while (true) {
            try {
                stmt.executeQuery("SELECT 1");
                dc.setConnectionAlive(true);
            } catch (SQLException ex) {
                if (dc.isConnectionAlive()) {
                    promptLostConnection();
                }

                dc.setConnectionAlive(false);
                createConnection();
            }

            try {
                Thread.sleep(interval);
            } catch (InterruptedException ex) {
                Logger.getLogger(CheckDatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void createConnection() {
        try {
            if (isBooting) {
                promptLostConnection();
                isBooting = false;
            }

            conn = DriverManager.getConnection(connectionString, userName, password);
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            createConnection();
        }
    }

    private void promptLostConnection() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                dc.promptLostConnection();
            }
        });
    }
}
