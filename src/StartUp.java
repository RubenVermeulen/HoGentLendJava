
import domein.DomeinController;
import domein.config.Config;
import domein.firma.Firma;
import domein.gebruiker.Gebruiker;
import domein.reservatie.ReservatieLijn;
import domein.groep.Groep;
import domein.materiaal.Materiaal;
import domein.reservatie.Reservatie;
import gui.LoginFrameController;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.jasypt.util.password.StrongPasswordEncryptor;
import util.ImageUtil;
import util.JPAUtil;

/**
 * 
 * TODO:
 * .png en .jpg bestanden voor image select
 * .csv bstanden voor bulk
 * aantal mag niet negatief zijn bij toevoegen van het materiaal reservatie
 * aantal onbeschikbaar geeft geen conflicten
 * filter voor alle conflicten te kunnen zien
 * 
 */

public class StartUp extends Application {// test xd

    @Override
    public void start(Stage stage) {

        // Database test
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

//        // Gebruikers aanmaken
//        StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
//
//        Gebruiker user = new Gebruiker("Ruben", "Vermeulen", "ruben@hogent.be",
//                passwordEncryptor.encryptPassword("ruben"), false, false, false);// lector
//        Gebruiker user2 = new Gebruiker("Sven", "Dedeene", "sven@hogent.be",
//                passwordEncryptor.encryptPassword("sven"), true, false, false);// hoofdbeheerder
//        Gebruiker user3 = new Gebruiker("Alexander", "Van Damme", "alexander@hogent.be",
//                passwordEncryptor.encryptPassword("alexander"), false, true, true);// beheerder en lector
//        Gebruiker user4 = new Gebruiker("Xander", "Berkein", "xander@hogent.be",
//                passwordEncryptor.encryptPassword("xander"), false, true, true); // beheerder en lector
//        Gebruiker user5 = new Gebruiker("Nieuwe", "Beheerder", "nieuwe.beheerder@hogent.be",
//                passwordEncryptor.encryptPassword(""), false, true, true); // beheerder
//
//        Gebruiker user6 = new Gebruiker("Jan", "Jansens", "jan.jansen.v8254@student.hogent.be",
//                passwordEncryptor.encryptPassword(""), false, false, false);
//        Gebruiker user7 = new Gebruiker("Tom", "Waes", "tom.waes.v7220@student.hogent.be",
//                passwordEncryptor.encryptPassword(""), false, false, false);
//        Gebruiker user8 = new Gebruiker("Sofie", "Devreeze", "sofie.devreeze.v8102@student.hogent.be",
//                passwordEncryptor.encryptPassword(""), false, false, false);
//
//        em.persist(user);
//        em.persist(user2);
//        em.persist(user3);
//        em.persist(user4);
//        em.persist(user5);
//        em.persist(user6);
//        em.persist(user7);
//        em.persist(user8);
//        em.getTransaction().commit();
//        em.getTransaction().begin();
//        // Materialen aanmaken
//        Firma f1 = new Firma("Goaty Enterprise");
//        f1.setEmail("info@goatyenterprise.be");
//
//        Firma f2 = new Firma("Deckers Boeken");
//        f2.setEmail("info@deckersboeken.be");
//
//        em.persist(f1);
//        em.persist(f2);
//        em.getTransaction().commit();
//
//        em.getTransaction().begin();
//        Groep leergroep = new Groep("Lichamelijke opvoeding", true);
//        em.persist(leergroep);
//
//        Groep doelgroep = new Groep("Kleuteronderwijs", false);
//        em.persist(doelgroep);
//
//        leergroep = new Groep("Biologie", true);
//        em.persist(leergroep);
//
//        doelgroep = new Groep("Lager onderwijs", false);
//        em.persist(doelgroep);
//        leergroep = new Groep("Aardrijkskunde", true);
//        em.persist(leergroep);
//
//        List<Groep> doelgroepen2 = new ArrayList<Groep>();
//        doelgroepen2.add(doelgroep);
//
//        doelgroep = new Groep("Secundair onderwijs", false);
//        em.persist(doelgroep);
//
//        List<Groep> leergroepen = new ArrayList<Groep>();
//        leergroepen.add(leergroep);
//
//        List<Groep> doelgroepen = new ArrayList<Groep>();
//        doelgroepen.add(doelgroep);
//
//        em.getTransaction().commit();
//
//        em.getTransaction().begin();
//        Materiaal[] materialen = {
//            new Materiaal("Wereldbol", 10)
//            .setAantalOnbeschikbaar(0)
//            .setArtikelnummer("B54879")
//            .setBeschrijving("Deze mooie wereldbol met verlichting heeft 25 cm doorsnee en werkt op stroom.")
//            .setDoelgroepen(doelgroepen2)
//            .setFirma(f1)
//            .setFotoBytes(ImageUtil.imageFileToByteArray(ImageUtil.getResourceAsFile("/images/temp_wereldbol.jpg")))
//            .setLeergebieden(leergroepen)
//            .setPlaats("Lokaal B4.035")
//            .setPrijs(19.99)
//            .setUitleenbaarheid(true),
//            new Materiaal("Atlas Noord Holland", 5)
//            .setAantalOnbeschikbaar(2)
//            .setArtikelnummer("A12345")
//            .setBeschrijving("Zou u wel eens een tijdreis willen maken tussen de provincie Noord-Holland van nu en het Noord-Holland van 1959? Hoe zag uw provincie er toen uit, hoe zag uw dorp of stad er uit? En wat is er allemaal veranderd in die 50 jaar? Met de Topografische DubbelAtlas van Utrecht ziet u het - letterlijk - in één oogopslag.")
//            .setDoelgroepen(doelgroepen)
//            .setFirma(f1)
//            .setFotoBytes(ImageUtil.imageFileToByteArray(ImageUtil.getResourceAsFile("/images/temp_atlas.jpg")))
//            .setLeergebieden(leergroepen)
//            .setPlaats("Lokaal B4.035")
//            .setPrijs(49.99)
//            .setUitleenbaarheid(true),
//            new Materiaal("Wetenschappelijke rekenmachine", 8)
//            .setAantalOnbeschikbaar(0)
//            .setArtikelnummer("A64645")
//            .setBeschrijving("De wetenschappelijke rekenmachine FX-92 van Casio is de perfecte metgezel voor op school.")
//            .setDoelgroepen(doelgroepen)
//            .setFirma(f1)
//            .setFotoBytes(ImageUtil.imageFileToByteArray(ImageUtil.getResourceAsFile("/images/temp_rekenmachine.jpg")))
//            .setLeergebieden(leergroepen)
//            .setPlaats("Lokaal B4.035")
//            .setPrijs(27.95)
//            .setUitleenbaarheid(false),
//            new Materiaal("Wereldbol2", 10)
//            .setAantalOnbeschikbaar(0)
//            .setArtikelnummer("B54879")
//            .setBeschrijving("Deze mooie wereldbol met verlichting heeft 25 cm doorsnee en werkt op stroom.")
//            .setDoelgroepen(doelgroepen)
//            .setFirma(f1)
//            .setFotoBytes(ImageUtil.imageFileToByteArray(ImageUtil.getResourceAsFile("/images/temp_wereldbol.jpg")))
//            .setLeergebieden(leergroepen)
//            .setPlaats("Lokaal B4.035")
//            .setPrijs(19.99)
//            .setUitleenbaarheid(true),
//            new Materiaal("Atlas Noord Holland2", 5)
//            .setAantalOnbeschikbaar(2)
//            .setArtikelnummer("A12345")
//            .setBeschrijving("Zou u wel eens een tijdreis willen maken tussen de provincie Noord-Holland van nu en het Noord-Holland van 1959? Hoe zag uw provincie er toen uit, hoe zag uw dorp of stad er uit? En wat is er allemaal veranderd in die 50 jaar? Met de Topografische DubbelAtlas van Utrecht ziet u het - letterlijk - in één oogopslag.")
//            .setDoelgroepen(doelgroepen)
//            .setFirma(f1)
//            .setFotoBytes(ImageUtil.imageFileToByteArray(ImageUtil.getResourceAsFile("/images/temp_atlas.jpg")))
//            .setLeergebieden(leergroepen)
//            .setPlaats("Lokaal B4.035")
//            .setPrijs(49.99)
//            .setUitleenbaarheid(true),
//            new Materiaal("Wetenschappelijke rekenmachine2", 10)
//            .setAantalOnbeschikbaar(0)
//            .setArtikelnummer("A64645")
//            .setBeschrijving("De wetenschappelijke rekenmachine FX-92 van Casio is de perfecte metgezel voor op school.")
//            .setDoelgroepen(doelgroepen)
//            .setFirma(f1)
//            .setFotoBytes(ImageUtil.imageFileToByteArray(ImageUtil.getResourceAsFile("/images/temp_rekenmachine.jpg")))
//            .setLeergebieden(leergroepen)
//            .setPlaats("Lokaal B4.035")
//            .setPrijs(27.95)
//            .setUitleenbaarheid(false)};
//
//        for (Materiaal m : materialen) {
//            em.persist(m);
//        }
//
//        em.getTransaction().commit();
//
//        // Reservaties
//        //==================================
//        // Reservatie 1
//        Reservatie r1 = new Reservatie(user, LocalDateTime.of(2016, 3, 21, 10, 30), LocalDateTime.of(2016, 3, 25, 10, 30),
//                LocalDateTime.of(2016, 3, 10, 10, 50));
//
//        ReservatieLijn[] lijnenVoorReservatie1 = {
//            new ReservatieLijn(materialen[0], 1, LocalDateTime.of(2016, 3, 21, 10, 30), LocalDateTime.of(2016, 3, 25, 10, 30)),
//            new ReservatieLijn(materialen[1], 1, LocalDateTime.of(2016, 3, 21, 10, 30), LocalDateTime.of(2016, 3, 25, 10, 30))};
//
//        lijnenVoorReservatie1[0].setReservatie(r1);
//        lijnenVoorReservatie1[1].setReservatie(r1);
//
//        r1.setReservatielijnen(Arrays.asList(lijnenVoorReservatie1));
//
//        // Reservatie 2 
//        Reservatie r2 = new Reservatie(user7, LocalDateTime.of(2016, 3, 14, 10, 30), LocalDateTime.of(2016, 3, 18, 18, 30),
//                LocalDateTime.of(2016, 3, 2, 20, 35));
//
//        ReservatieLijn[] lijnenVoorReservatie2 = {
//            new ReservatieLijn(materialen[2], 5, LocalDateTime.of(2016, 3, 14, 10, 30), LocalDateTime.of(2016, 3, 18, 18, 30))};
//
//        lijnenVoorReservatie2[0].setReservatie(r2);
//
//        r2.setReservatielijnen(Arrays.asList(lijnenVoorReservatie2));
//
//        // Reservatie 3
//        Reservatie r3 = new Reservatie(user6, LocalDateTime.of(2016, 3, 14, 10, 30), LocalDateTime.of(2016, 3, 18, 16, 30),
//                LocalDateTime.of(2016, 3, 3, 20, 10));
//
//        ReservatieLijn[] lijnenVoorReservatie3 = {
//            new ReservatieLijn(materialen[2], 5, LocalDateTime.of(2016, 3, 14, 10, 30), LocalDateTime.of(2016, 3, 18, 16, 30)),
//            new ReservatieLijn(materialen[1], 3, LocalDateTime.of(2016, 3, 14, 10, 30), LocalDateTime.of(2016, 3, 18, 16, 30))};
//
//        lijnenVoorReservatie3[0].setReservatie(r3);
//        lijnenVoorReservatie3[1].setReservatie(r3);
//
//        r3.setReservatielijnen(Arrays.asList(lijnenVoorReservatie3));
//
//        // Reservatie 4
//        Reservatie r4 = new Reservatie(user4, LocalDateTime.of(2016, 3, 16, 10, 30), LocalDateTime.of(2016, 3, 18, 18, 30),
//                LocalDateTime.of(2016, 3, 12, 20, 10));
//
//        ReservatieLijn[] lijnenVoorReservatie4 = {
//            new ReservatieLijn(materialen[2], 2, LocalDateTime.of(2016, 3, 16, 10, 30), LocalDateTime.of(2016, 3, 18, 18, 30))};
//
//        lijnenVoorReservatie4[0].setReservatie(r4);
//
//        r4.setReservatielijnen(Arrays.asList(lijnenVoorReservatie4));
//
//        // Reservatie 5
//        Reservatie r5 = new Reservatie(user8, LocalDateTime.of(2016, 4, 11, 10, 30), LocalDateTime.of(2016, 4, 15, 16, 30),
//                LocalDateTime.of(2016, 3, 5, 21, 50));
//        
//        ReservatieLijn[] lijnenVoorReservatie5 = {
//            new ReservatieLijn(materialen[0], 3, LocalDateTime.of(2016, 4, 11, 10, 30), LocalDateTime.of(2016, 4, 15, 16, 30))
//        };
//                
//        lijnenVoorReservatie5[0].setReservatie(r5);
//        
//        r5.setReservatielijnen(Arrays.asList(lijnenVoorReservatie5));
//                
//        // Reservatielijnen
//        //==================================
//        em.getTransaction().begin();
//
//        em.persist(r1);
//        em.persist(r2);
//        em.persist(r3);
//        em.persist(r4);
//        em.persist(r5);
//
//        em.persist(lijnenVoorReservatie1[0]);
//        em.persist(lijnenVoorReservatie1[1]);
//
//        em.persist(lijnenVoorReservatie2[0]);
//
//        em.persist(lijnenVoorReservatie3[0]);
//        em.persist(lijnenVoorReservatie3[1]);
//
//        em.persist(lijnenVoorReservatie4[0]);
//        
//        em.persist(lijnenVoorReservatie5[0]);
//
//        em.getTransaction().commit();
//
//        // Config
//        //==================================
//        Config c = new Config();
//        c.setStandaardIndientijd(LocalTime.NOON);
//        c.setStandaardOphaaltijd(LocalTime.MIDNIGHT);
//        c.setStandaardOphaalDag("maandag");
//        c.setStandaardIndienDag("vrijdag");
//        c.setLeentermijn(1);
//
//        em.getTransaction().begin();
//        em.persist(c);
//        em.getTransaction().commit();
//
//        em.close();

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
