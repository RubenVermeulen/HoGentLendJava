package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import config.DatabaseConfig;
import domein.DomeinController;

public class CheckDatabaseConnection implements Runnable {

    private final int interval = 1000;    
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

            conn = DriverManager.getConnection(DatabaseConfig.getPersistenceMap().get("javax.persistence.jdbc.url"), 
                    DatabaseConfig.getPersistenceMap().get("javax.persistence.jdbc.user"), 
                    DatabaseConfig.getPersistenceMap().get("javax.persistence.jdbc.password")
            );
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
