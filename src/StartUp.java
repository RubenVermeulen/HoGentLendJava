import domein.DomeinController;
import domein.Firma;
import domein.Gebruiker;
import domein.ReservatieLijn;
import domein.Groep;
import domein.Materiaal;
import domein.Reservatie;
import gui.LoginFrameController;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.jasypt.util.password.StrongPasswordEncryptor;
import util.JPAUtil;

public class StartUp extends Application {// test xd

    @Override
    public void start(Stage stage) {
        // Database test
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        // Gebruikers aanmaken
        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

        Gebruiker user = new Gebruiker("Ruben", "Vermeulen", "ruben@hogent.be",
                passwordEncryptor.encryptPassword("ruben"), false, false, true);
        Gebruiker user2 = new Gebruiker("Sven", "Dedeene", "sven@hogent.be",
                passwordEncryptor.encryptPassword("sven"), true, false, false);
        Gebruiker user3 = new Gebruiker("Alexander", "Van Damme", "alexander@hogent.be",
                passwordEncryptor.encryptPassword("alexander"), true, true, false);
        Gebruiker user4 = new Gebruiker("Xander", "Berkein", "xander@hogent.be",
                passwordEncryptor.encryptPassword("xander"), false, true, true);
        Gebruiker user5 = new Gebruiker("Nieuwe", "Beheerder", "nieuwe.beheerder@hogent.be",
                passwordEncryptor.encryptPassword(""), true, false, true);

        em.persist(user);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);
        em.persist(user5);
        em.getTransaction().commit();
        em.getTransaction().begin();
        // Materialen aanmaken
        Firma f1 = new Firma("Goaty Enterprise");
        f1.setEmail("info@goatyenterprise.be");
        
        Firma f2 = new Firma("Deckers Boeken");
        f2.setEmail("info@deckersboeken.be");
        
        em.persist(f1);
        em.persist(f2);
        em.getTransaction().commit();
        
        em.getTransaction().begin();
        Groep leergroep = new Groep("Lichamelijke opvoeding",true);
        em.persist(leergroep);
        
        Groep doelgroep = new Groep("Kleuteronderwijs", false);
        em.persist(doelgroep);
        
        leergroep = new Groep("Biologie",true);
        em.persist(leergroep);
        
        doelgroep = new Groep("Lager onderwijs", false);
        em.persist(doelgroep);
        leergroep = new Groep("Aardrijkskunde",true);
        em.persist(leergroep);
        
        List<Groep> doelgroepen2 = new ArrayList<Groep>();
        doelgroepen2.add(doelgroep);
        
        doelgroep = new Groep("Secundair onderwijs", false);
        em.persist(doelgroep);
        
        List<Groep> leergroepen = new ArrayList<Groep>();
        leergroepen.add(leergroep);
        
        List<Groep> doelgroepen = new ArrayList<Groep>();
        doelgroepen.add(doelgroep);
        
        
        em.getTransaction().commit();
        
        em.getTransaction().begin();
        Materiaal[] materialen = {
            new Materiaal("Wereldbol", 10)
            .setAantalOnbeschikbaar(0)
            .setArtikelnummer("B54879")
            .setBeschrijving("Deze mooie wereldbol met verlichting heeft 25 cm doorsnee en werkt op stroom.")
            .setDoelgroepen(doelgroepen2)
            .setFirma(f1)
            .setFoto("temp_wereldbol.jpg")
            .setLeergebieden(leergroepen)
            .setPlaats("Lokaal B4.035")
            .setPrijs(19.99)
            .setUitleenbaarheid(true),
            new Materiaal("Atlas Noord Holland", 5)
            .setAantalOnbeschikbaar(2)
            .setArtikelnummer("A12345")
            .setBeschrijving("Zou u wel eens een tijdreis willen maken tussen de provincie Noord-Holland van nu en het Noord-Holland van 1959? Hoe zag uw provincie er toen uit, hoe zag uw dorp of stad er uit? En wat is er allemaal veranderd in die 50 jaar? Met de Topografische DubbelAtlas van Utrecht ziet u het - letterlijk - in één oogopslag.")
            .setDoelgroepen(doelgroepen)
            .setFirma(f1)
            .setFoto("temp_atlas.jpg")
            .setLeergebieden(leergroepen)
            .setPlaats("Lokaal B4.035")
            .setPrijs(49.99)
            .setUitleenbaarheid(true),
            new Materiaal("Wetenschappelijke rekenmachine", 10)
            .setAantalOnbeschikbaar(0)
            .setArtikelnummer("A64645")
            .setBeschrijving("De wetenschappelijke rekenmachine FX-92 van Casio is de perfecte metgezel voor op school.")
            .setDoelgroepen(doelgroepen)
            .setFirma(f1)
            .setFoto("temp_rekenmachine.jpg")
            .setLeergebieden(leergroepen)
            .setPlaats("Lokaal B4.035")
            .setPrijs(27.95)
            .setUitleenbaarheid(false),
            new Materiaal("Wereldbol2", 10)
            .setAantalOnbeschikbaar(0)
            .setArtikelnummer("B54879")
            .setBeschrijving("Deze mooie wereldbol met verlichting heeft 25 cm doorsnee en werkt op stroom.")
            .setDoelgroepen(doelgroepen)
            .setFirma(f1)
            .setFoto("temp_wereldbol.jpg")
            .setLeergebieden(leergroepen)
            .setPlaats("Lokaal B4.035")
            .setPrijs(19.99)
            .setUitleenbaarheid(true),
            new Materiaal("Atlas Noord Holland2", 5)
            .setAantalOnbeschikbaar(2)
            .setArtikelnummer("A12345")
            .setBeschrijving("Zou u wel eens een tijdreis willen maken tussen de provincie Noord-Holland van nu en het Noord-Holland van 1959? Hoe zag uw provincie er toen uit, hoe zag uw dorp of stad er uit? En wat is er allemaal veranderd in die 50 jaar? Met de Topografische DubbelAtlas van Utrecht ziet u het - letterlijk - in één oogopslag.")
            .setDoelgroepen(doelgroepen)
            .setFirma(f1)
            .setFoto("temp_atlas.jpg")
            .setLeergebieden(leergroepen)
            .setPlaats("Lokaal B4.035")
            .setPrijs(49.99)
            .setUitleenbaarheid(true),
            new Materiaal("Wetenschappelijke rekenmachine2", 10)
            .setAantalOnbeschikbaar(0)
            .setArtikelnummer("A64645")
            .setBeschrijving("De wetenschappelijke rekenmachine FX-92 van Casio is de perfecte metgezel voor op school.")
            .setDoelgroepen(doelgroepen)
            .setFirma(f1)
            .setFoto("temp_rekenmachine.jpg")
            .setLeergebieden(leergroepen)
            .setPlaats("Lokaal B4.035")
            .setPrijs(27.95)
            .setUitleenbaarheid(false) };

        for (Materiaal m : materialen) {
            em.persist(m);
        }

        em.getTransaction().commit();
        
        em.getTransaction().begin();
        
        List<ReservatieLijn> reservatieLijnen = new ArrayList<>();
        List<ReservatieLijn> reservatieLijnen2 = new ArrayList<>();
        List<ReservatieLijn> reservatieLijnen3 = new ArrayList<>();
        
        ReservatieLijn gereserveerdMateriaal1 = 
                new ReservatieLijn(materialen[0], 1, LocalDateTime.now(), LocalDateTime.of(2016, 5, 2, 20, 10));
        ReservatieLijn gereserveerdMateriaal2 = 
                new ReservatieLijn(materialen[1], 3, LocalDateTime.of(2016, 2, 3, 10, 30), LocalDateTime.of(2016, 2, 10, 10, 30));
        
        ReservatieLijn gereserveerdMateriaal3 = 
                new ReservatieLijn(materialen[2], 5, LocalDateTime.of(2016, 3, 10, 10, 30), LocalDateTime.of(2016, 3, 17, 18, 30));
        
        ReservatieLijn gereserveerdMateriaal4 =
                new ReservatieLijn(materialen[4], 4, LocalDateTime.of(2016, 3, 28, 10, 30), LocalDateTime.of(2016, 4, 5, 18, 30));
        ReservatieLijn gereserveerdMateriaal5 =
                new ReservatieLijn(materialen[5], 3, LocalDateTime.of(2016, 3, 28, 10, 30), LocalDateTime.of(2016, 4, 5, 18, 30));
        
        reservatieLijnen.add(gereserveerdMateriaal1);
        reservatieLijnen.add(gereserveerdMateriaal2);
        
        reservatieLijnen2.add(gereserveerdMateriaal3);
        
        reservatieLijnen3.add(gereserveerdMateriaal4);
        reservatieLijnen3.add(gereserveerdMateriaal5);
        
        for (ReservatieLijn gm : reservatieLijnen)
            em.persist(gm);
        
        for(ReservatieLijn gm : reservatieLijnen2)
            em.persist(gm);
        
        for (ReservatieLijn gm : reservatieLijnen3)
            em.persist(gm);
        
        
        Reservatie reservatie = 
                new Reservatie(user, LocalDateTime.of(2016, 2, 3, 10, 30), LocalDateTime.of(2016, 2, 10, 10, 30), reservatieLijnen);

        em.persist(reservatie);
        
        Reservatie reservatie2 =
                new Reservatie(user2, LocalDateTime.of(2016, 3, 10, 10, 30), LocalDateTime.of(2016, 3, 17, 18, 30), reservatieLijnen2);
        
        em.persist(reservatie2);
        
        Reservatie reservatie3 =
                new Reservatie(user3, LocalDateTime.of(2016, 3, 28, 10, 30), LocalDateTime.of(2016, 4, 5, 18, 30), reservatieLijnen3);
        
        em.persist(reservatie3);
        
        em.getTransaction().commit();
        
        em.close();
        // niet sluiten want de domein laag heeft nu al andere acties met de emf
//        emf.close();
//        System.out.println("Closed");
        
        DomeinController domCont = new DomeinController();
  //      domCont.geefAlleReservaties().toString();
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
