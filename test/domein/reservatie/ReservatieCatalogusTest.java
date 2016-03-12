package domein.reservatie;

import domein.firma.Firma;
import domein.firma.FirmaRepository;
import domein.gebruiker.Gebruiker;
import domein.gebruiker.GebruikerRepository;
import domein.materiaal.Materiaal;
import domein.materiaal.MateriaalRepository;
import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import shared.MateriaalView;
import shared.ReservatieLijnView;
import shared.ReservatieView;
import util.ImageUtil;

/**
 *
 * @author alexa
 */
public class ReservatieCatalogusTest {

    private ReservatieCatalogus resCatalogus;

    private MateriaalRepository materiaalRepo;
    private GebruikerRepository gebruikersRepo;
    private FirmaRepository firmaRepository;

    @Mock
    private GebruikerRepository gebruikersRepoDummy;

    private List<Reservatie> reservaties;
    private final String EMAIL_CORRECT = "sven@hogent.be";
    private final LocalDateTime OPHAALMOMENT_CORRECT = LocalDateTime.of(2016, Month.MARCH, 14, 15, 30);
    private final LocalDateTime INDIENMOMENT_CORRECT = LocalDateTime.of(2016, Month.MARCH, 21, 15, 30);
    private final LocalDateTime OPHAALMOMENT_VEELLATER = LocalDateTime.of(2017, Month.MARCH, 21, 15, 30);
    private final LocalDateTime INDIENMOMENT_VEELLATER = LocalDateTime.of(2017, Month.MARCH, 21, 15, 30);
    private final LocalDateTime RESERVATIEMOMENT_CORRECT = LocalDateTime.now();
    private final LocalDateTime RESERVATIEMOMENT_VEELLATER = LocalDateTime.of(2017, Month.MARCH, 12, 15, 30);

    private List<ReservatieLijnView> reservatieLijnen;
    private final Firma f1 = new Firma("Firma", "email@firma.be");

    private final Gebruiker sven = new Gebruiker("Sven", "Dedeene", "sven", EMAIL_CORRECT, true, true, true);
    private final Gebruiker xander = new Gebruiker("Xander", "Berkein", "xander", "xander@hogent.be", true, true, true);

    private final MateriaalView mv = new MateriaalView("wereldbol", 5);
     private MateriaalView mv2 = new MateriaalView("Geodriehoek", 15);

    private final long id1 = 78;
    private final long id2 = 79;
    private final long m1id = 88;
    private final long nul = 0;
    private Materiaal m1 = new Materiaal("Wereldbol", 5);
    private Materiaal m2 = new Materiaal("Geodriehoek", 15);

    private File tempFotoFile;
    private ReservatieLijnView rlv;
    private ReservatieLijnView rlv2;
    private ReservatieLijnView rlv3;

    private ReservatieView rv;

    private final Reservatie r1 = new Reservatie(sven, OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT,
            RESERVATIEMOMENT_CORRECT, false);
    private final Reservatie r2 = new Reservatie(xander, OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT,
            RESERVATIEMOMENT_CORRECT, false);

    private Optional<Gebruiker> deLenerOpt;

    private ReservatieLijn rl;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        m1.setId(m1id);
        m2.setId(99);
        tempFotoFile = ImageUtil.getResourceAsFile("/images/default_materiaal_img.png");

        rl = new ReservatieLijn(m1, 2, OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT);

        r1.setReservatielijnen(Arrays.asList(rl));
        r1.setId(id1);
        r2.setReservatielijnen(Arrays.asList(rl));
        r2.setId(id2);

        mv.setAantalOnbeschikbaar(2)
                .setDoelgroepen(Arrays.asList("Doelgroep1", "doelgroep2"))
                .setNewFotoUrl(tempFotoFile.getPath())
                .setFirma("Firma")
                .setEmailFirma("email@firma.be")
                .setLeergebieden(Arrays.asList("Leergebied1", "Leergebied2"))
                .setOmschrijving("Omschrijving")
                .setPlaats("Plaats")
                .setPrijs(2.22)
                .setUitleenbaarheid(true);
        mv2.setAantalOnbeschikbaar(2)
                .setDoelgroepen(Arrays.asList("Doelgroep1", "doelgroep2"))
                .setNewFotoUrl(tempFotoFile.getPath())
                .setFirma("Firma")
                .setEmailFirma("email@firma.be")
                .setLeergebieden(Arrays.asList("Leergebied1", "Leergebied2"))
                .setOmschrijving("Omschrijving")
                .setPlaats("Plaats")
                .setPrijs(2.22)
                .setUitleenbaarheid(true);
        
        rlv = new ReservatieLijnView(OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT, mv, 5);
        rlv2 = new ReservatieLijnView(OPHAALMOMENT_VEELLATER, INDIENMOMENT_VEELLATER, mv, 5);
        rlv3 = new ReservatieLijnView(OPHAALMOMENT_VEELLATER, INDIENMOMENT_VEELLATER, mv, 5);

        firmaRepository = new FirmaRepository(Arrays.asList(f1));

        //   gebruikersRepo = new GebruikerRepository(Arrays.asList(sven, xander));
        deLenerOpt = Optional.of(sven);
        reservatieLijnen = new ArrayList(Arrays.asList(rlv));

        rv = new ReservatieView(EMAIL_CORRECT, OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT,
                RESERVATIEMOMENT_CORRECT, reservatieLijnen);

        rv.setId(78);
        reservaties = new ArrayList<>(Arrays.asList(r1, r2));
        materiaalRepo = new MateriaalRepository(firmaRepository);
        resCatalogus = new ReservatieCatalogus(reservaties, materiaalRepo, gebruikersRepoDummy);

    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwReservatieToeRvNull() {
        resCatalogus.voegReservatieToe(null);

    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwReservatieToeRvGeenEmail() {

        ReservatieView rvGeenEmail = new ReservatieView("", OPHAALMOMENT_CORRECT,
                INDIENMOMENT_CORRECT, RESERVATIEMOMENT_CORRECT, reservatieLijnen);
        resCatalogus.voegReservatieToe(rvGeenEmail);

    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwReservatieToeEmailNull() {

        ReservatieView rvGeenEmail = new ReservatieView(null, OPHAALMOMENT_CORRECT,
                INDIENMOMENT_CORRECT, RESERVATIEMOMENT_CORRECT, reservatieLijnen);
        resCatalogus.voegReservatieToe(rvGeenEmail);

    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwReservatieToeRvGeenOphaalmoment() {

        ReservatieView rvGeenReservatieLijnen = new ReservatieView(EMAIL_CORRECT, OPHAALMOMENT_CORRECT,
                INDIENMOMENT_CORRECT, RESERVATIEMOMENT_CORRECT, null);
        resCatalogus.voegReservatieToe(rvGeenReservatieLijnen);

    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwReservatieToeRvIndienmomentVoorOphaalmoment() {
        Mockito.when(
                gebruikersRepoDummy.geefGebruikerViaEmail(EMAIL_CORRECT)).
                thenReturn(deLenerOpt);
        ReservatieView rvGeenReservatieLijnen = new ReservatieView(EMAIL_CORRECT, OPHAALMOMENT_CORRECT,
                RESERVATIEMOMENT_CORRECT, INDIENMOMENT_CORRECT, reservatieLijnen);

        resCatalogus.voegReservatieToe(rvGeenReservatieLijnen);

    }

    @Test
    public void voegNieuwReservatieToeVolledigCorrect() {
        Mockito.when(
                gebruikersRepoDummy.geefGebruikerViaEmail(EMAIL_CORRECT)).
                thenReturn(deLenerOpt);

        ReservatieView rv = new ReservatieView(EMAIL_CORRECT, OPHAALMOMENT_CORRECT,
                INDIENMOMENT_CORRECT, RESERVATIEMOMENT_CORRECT, reservatieLijnen);

        rv.setId(id1);
        int aantalRes = reservaties.size();
        resCatalogus.voegReservatieToe(rv);

        // assertTrue(compareReservatieViews(rv, resCatalogus.geefAlleReservaties().get(2)));
        // assertEquals(rv.getEmailLener(), reservaties.get(2).getLener().getEmail());
        assertEquals(aantalRes + 1, reservaties.size());
        
        
    }

    @Test
    public void verwijderReservatieVolledigCorrect() {
        System.out.println("r1 id: " + r1.toString());
        System.out.println("rv id: " + rv.toString());

        assertEquals(r1, resCatalogus.verwijderReservatie(rv));

    }

    @Test(expected = IllegalArgumentException.class)
    public void verwijderReservatieRvNull() {

        resCatalogus.verwijderReservatie(null);

    }

    @Test
    public void geefReservatie() {
        assertEquals(r1, resCatalogus.geefReservatie(r1.getId()).get());
    }

    @Test
    public void heeftConflictenVolledigCorrect() {

        int conflicten = resCatalogus.heeftConflicten(rlv, RESERVATIEMOMENT_CORRECT);

        assertEquals(3, conflicten);
    }

    @Test(expected = IllegalArgumentException.class)
    public void heeftConflictenRlvNull() {

        resCatalogus.heeftConflicten(null, RESERVATIEMOMENT_CORRECT);
    }

    

    private boolean compareReservatieViews(ReservatieView rv1, ReservatieView rv2) {
        return (rv1.getEmailLener().equals(rv2.getEmailLener())
                && rv1.getOphaalmoment() == rv2.getOphaalmoment()
                && rv1.getIndienmoment() == rv2.getIndienmoment()
                && rv1.getReservatieLijnen().toString().equals(rv2.getReservatieLijnen().toString()));

    }
}
