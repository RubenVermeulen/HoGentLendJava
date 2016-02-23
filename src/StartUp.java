
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
                passwordEncryptor.encryptPassword("rubsup"), true, false);
        Gebruiker user2 = new Gebruiker("Sven", "Dedeene", "sven.dedeene.v6035@student.hogent.be",
                passwordEncryptor.encryptPassword("sfonk"), false, true);
        Gebruiker user3 = new Gebruiker("Alexander", "Van Damme", "alexander.vandamme.v7672@student.hogent.be",
                passwordEncryptor.encryptPassword("alosk"), true, true);
        Gebruiker user4 = new Gebruiker("Xander", "Berkein", "xander.berkein.v7629@student.hogent.be",
                passwordEncryptor.encryptPassword("czandor"), false, false);

        em.persist(user);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);

        // Materialen aanmaken
        Firma firma = new Firma("Goaty Enterprise");
        firma.setEmail("info@goatyenterprise.be");

        em.persist(firma);

        Materiaal[] materialen = {
            new Materiaal("Wereldbol", 10)
            .setAantalOnbeschikbaar(0)
            .setArtikelnummer("B54879")
            .setBeschrijving("Deze mooie wereldbol met verlichting heeft 25 cm doorsnee en werkt op stroom.")
            .setDoelgroepen("Liefhebbers voor wereldkennis, Schatzoekers")
            .setFirma(firma)
            .setFoto("default_materiaal_img.jpg")
            .setLeergebieden("Aardrijkskunde")
            .setPlaats("Lokaal B4.035")
            .setPrijs(19.99)
            .setUitleenbaarheid(true),
            new Materiaal("Atlas Noord Holland", 5)
            .setAantalOnbeschikbaar(2)
            .setArtikelnummer("A12345")
            .setBeschrijving("Zou u wel eens een tijdreis willen maken tussen de provincie Noord-Holland van nu en het Noord-Holland van 1959? Hoe zag uw provincie er toen uit, hoe zag uw dorp of stad er uit? En wat is er allemaal veranderd in die 50 jaar? Met de Topografische DubbelAtlas van Utrecht ziet u het - letterlijk - in één oogopslag.")
            .setDoelgroepen("Liefhebbers voor wereldkennis, Schatzoekers")
            .setFirma(firma)
            .setFoto("default_materiaal_img.jpg")
            .setLeergebieden("Aardrijkskunde")
            .setPlaats("Lokaal B4.035")
            .setPrijs(49.99)
            .setUitleenbaarheid(true),
            new Materiaal("Wetenschappelijke rekenmachine", 10)
            .setAantalOnbeschikbaar(0)
            .setArtikelnummer("A64645")
            .setBeschrijving("De wetenschappelijke rekenmachine FX-92 van Casio is de perfecte metgezel voor op school.")
            .setDoelgroepen("Wiskunde fanaten, wetenschappers")
            .setFirma(firma)
            .setFoto("default_materiaal_img.jpg")
            .setLeergebieden("Wiskunde, Chemie, Fysica, Economie")
            .setPlaats("Lokaal B4.035")
            .setPrijs(27.95)
            .setUitleenbaarheid(false),
            new Materiaal("Wereldbol", 10)
            .setAantalOnbeschikbaar(0)
            .setArtikelnummer("B54879")
            .setBeschrijving("Deze mooie wereldbol met verlichting heeft 25 cm doorsnee en werkt op stroom.")
            .setDoelgroepen("Liefhebbers voor wereldkennis, Schatzoekers")
            .setFirma(firma)
            .setFoto("default_materiaal_img.jpg")
            .setLeergebieden("Aardrijkskunde")
            .setPlaats("Lokaal B4.035")
            .setPrijs(19.99)
            .setUitleenbaarheid(true),
            new Materiaal("Atlas Noord Holland", 5)
            .setAantalOnbeschikbaar(2)
            .setArtikelnummer("A12345")
            .setBeschrijving("Zou u wel eens een tijdreis willen maken tussen de provincie Noord-Holland van nu en het Noord-Holland van 1959? Hoe zag uw provincie er toen uit, hoe zag uw dorp of stad er uit? En wat is er allemaal veranderd in die 50 jaar? Met de Topografische DubbelAtlas van Utrecht ziet u het - letterlijk - in één oogopslag.")
            .setDoelgroepen("Liefhebbers voor wereldkennis, Schatzoekers")
            .setFirma(firma)
            .setFoto("default_materiaal_img.jpg")
            .setLeergebieden("Aardrijkskunde")
            .setPlaats("Lokaal B4.035")
            .setPrijs(49.99)
            .setUitleenbaarheid(true),
            new Materiaal("Wetenschappelijke rekenmachine", 10)
            .setAantalOnbeschikbaar(0)
            .setArtikelnummer("A64645")
            .setBeschrijving("De wetenschappelijke rekenmachine FX-92 van Casio is de perfecte metgezel voor op school.")
            .setDoelgroepen("Wiskunde fanaten, wetenschappers")
            .setFirma(firma)
            .setFoto("default_materiaal_img.jpg")
            .setLeergebieden("Wiskunde, Chemie, Fysica, Economie")
            .setPlaats("Lokaal B4.035")
            .setPrijs(27.95)
            .setUitleenbaarheid(false),};

        for (Materiaal m : materialen) {
            em.persist(m);
        }

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
