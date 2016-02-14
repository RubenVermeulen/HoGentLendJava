import domein.DomeinController;
import domein.Gebruiker;
import gui.LoginFrameController;
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
        Scene scene = new Scene(new LoginFrameController(domCont));
//        Scene scene = new Scene(new MainMenuFrameController(domCont));
        scene.getStylesheets().add("/gui/styles.css");
        stage.setScene(scene);
        stage.show();
        
        // Database test
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        
        Gebruiker user = new Gebruiker("Ruben", "Vermeulen", "ruben.vermeulen.v4419@student.hogent.be");
        
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
