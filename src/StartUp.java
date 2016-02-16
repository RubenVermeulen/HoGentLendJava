import domein.DomeinController;
import domein.Firma;
import domein.Gebruiker;
import domein.Materiaal;
import gui.LoginFrameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        
        // Gebruikers aanmaken
     
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
        
        Gebruiker user = new Gebruiker("Ruben", "Vermeulen", "ruben.vermeulen.v4419@student.hogent.be",
                passwordEncryptor.encryptPassword("rubsup"));
        Gebruiker user2 = new Gebruiker("Sven", "Dedeene", "sven.dedeene.v6035@student.hogent.be",
                passwordEncryptor.encryptPassword("sfonk"));
         Gebruiker user3 = new Gebruiker("Alexander", "Van Damme", "alexander.vandamme.v7672@student.hogent.be",
                passwordEncryptor.encryptPassword("alosk"));
         Gebruiker user4 = new Gebruiker("Xander", "Berkein", "xander.berkein.v7629@student.hogent.be",
                passwordEncryptor.encryptPassword("czandor"));
        
        em.persist(user);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);
        
        // Materialen aanmaken
        
        Firma firma = new Firma("Goaty Enterprise", "info@goatyenterprise.be");
        
        em.persist(firma);
        
        Materiaal[] materialen = {
            new Materiaal(1, firma, "default_materiaal_img.jpg", "Wereldbol", "Deze mooie wereldbol met verlichting heeft 25 cm doorsnee en werkt op stroom.", "B54879", 19.99, 10, 0, true, "Lokaal B4.035", "Liefhebbers voor wereldkennis, Schatzoekers", "Aardrijkskunde"),
            new Materiaal(2, firma, "default_materiaal_img.jpg", "Atlas Noord Holland", "Zou u wel eens een tijdreis willen maken tussen de provincie Noord-Holland van nu en het Noord-Holland van 1959? Hoe zag uw provincie er toen uit, hoe zag uw dorp of stad er uit? En wat is er allemaal veranderd in die 50 jaar? Met de Topografische DubbelAtlas van Utrecht ziet u het - letterlijk - in één oogopslag.", "A12345", 49.99, 2, 1, true, "Lokaal B4.035", "Liefhebbers voor wereldkennis, Schatzoekers", "Aardrijkskunde"),
            new Materiaal(3, firma, "default_materiaal_img.jpg", "Wetenschappelijke rekenmachine", "De wetenschappelijke rekenmachine FX-92 van Casio is de perfecte metgezel voor op school.", "A64645", 27.95, 5, 0, true, "Lokaal B3.032", "Wiskunde fanaten, wetenschappers", "Wiskunde, Chemie, Fysica, Economie")
        };
        
        for (Materiaal m : materialen)
            em.persist(m);
        
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
        stage.setTitle("HoGentLend");
        stage.getIcons().add(new Image("/images/HoGentLendIcon.png"));
        stage.show();
    }

    public static void main(String... args) {
        Application.launch(StartUp.class, args);
    }
}
