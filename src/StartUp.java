import domein.DomeinController;
import domein.Gebruiker;
import gui.LoginFrameController;
import gui.MainMenuFrameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.jasypt.util.password.StrongPasswordEncryptor;
import util.JPAUtil;

public class StartUp extends Application {

    @Override
    public void start(Stage stage) {        
        // Database test
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        
        em.getTransaction().begin();
        
        
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        
        Gebruiker user = new Gebruiker("Ruben", "Vermeulen", "ruben.vermeulen.v4419@student.hogent.be",
                passwordEncryptor.encryptPassword("rubsup"));
        Gebruiker user2 = new Gebruiker("Sven", "Dedeene", "sven.dedeene.v6035@student.hogent.be",
                passwordEncryptor.encryptPassword("sfonk"));
         Gebruiker user3 = new Gebruiker("Alexander", "Van Damme", "alexander.vandamme.v7672@student.hogent.be",
                passwordEncryptor.encryptPassword("alosk"));
        
        
        
        em.persist(user);
        em.persist(user2);
        em.persist(user3);
        
        em.getTransaction().commit();
        
        em.close();
        // niet sluiten want de domein laag heeft nu al andere acties met de emf
//        emf.close();
//        System.out.println("Closed");

        DomeinController domCont = new DomeinController();
        Scene scene = new Scene(new LoginFrameController(domCont));
//        Scene scene = new Scene(new MainMenuFrameController(domCont));
        scene.getStylesheets().add("/gui/styles.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String... args) {
        Application.launch(StartUp.class, args);
    }
}
