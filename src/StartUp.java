import domein.DomeinController;
import domein.Gebruikers;
import gui.MainMenuFrameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import util.JPAUtil;

public class StartUp extends Application {

    @Override
    public void start(Stage stage) {
        DomeinController domCont = new DomeinController();
        //Scene scene = new Scene(new LoginFrameController(domCont));
        MainMenuFrameController mmfc = new MainMenuFrameController(domCont);
        Scene scene = new Scene(mmfc);
        scene.getStylesheets().add("/gui/styles.css");
        stage.setScene(scene);
        stage.show();
        // Prefered resolution: 1280 x 768
        
        mmfc.setupTemporaryDemoMaterials();
        
        // Database test
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        
        Gebruikers user = new Gebruikers("Ruben", "Vermeulen", "ruben.vermeulen.v4419@student.hogent.be");
        
        em.persist(user);
        
        em.getTransaction().commit();
        
        em.close();
        emf.close();
        System.out.println("Closed");
    }

    public static void main(String... args) {
        Application.launch(StartUp.class, args);
    }
}
