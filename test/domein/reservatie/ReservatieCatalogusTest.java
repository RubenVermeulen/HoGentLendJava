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
    private final LocalDateTime RESERVATIEMOMENT_CORRECT = LocalDateTime.now();
    private List<ReservatieLijnView> reservatieLijnen;
    private final Firma f1 = new Firma("Firma", "email@firma.be");

    private final Gebruiker sven = new Gebruiker("Sven", "Dedeene", "sven", EMAIL_CORRECT, true, true, true);
    private final Gebruiker xander = new Gebruiker("Xander", "Berkein", "xander", "xander@hogent.be", true, true, true);

    private final MateriaalView mv = new MateriaalView("wereldbol", 5);

    private final long id1 = 78;
    private final long id2 = 79;
    private final long m1id = 88;
    private Materiaal m1 = new Materiaal("Wereldbol", 5);
    private File tempFotoFile;
    private final ReservatieLijnView rlv = new ReservatieLijnView(OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT, mv, 5);

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
        tempFotoFile = ImageUtil.getResourceAsFile("/images/default_materiaal_img.png");
        rl = new ReservatieLijn(m1, 2, OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT);
        r1.setReservatielijnen(Arrays.asList(rl));
        r1.setId(id1);
        r2.setReservatielijnen(Arrays.asList(rl));
        r2.setId(id2);
        reservaties = new ArrayList<>(Arrays.asList(r1, r2));

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

        firmaRepository = new FirmaRepository(Arrays.asList(f1));
        materiaalRepo = new MateriaalRepository(firmaRepository);
        //   gebruikersRepo = new GebruikerRepository(Arrays.asList(sven, xander));

        deLenerOpt = Optional.of(sven);
        reservatieLijnen = new ArrayList(Arrays.asList(rlv));

        rv = new ReservatieView(EMAIL_CORRECT, OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT,
                RESERVATIEMOMENT_CORRECT, reservatieLijnen);

        resCatalogus = new ReservatieCatalogus(reservaties, materiaalRepo, gebruikersRepoDummy);

    }

    @Test
    public void voegNieuwReservatieToeVolledigCorrect() {
        Mockito.when(
                gebruikersRepoDummy.geefGebruikerViaEmail(EMAIL_CORRECT)).
                thenReturn(deLenerOpt);

        ReservatieView rv = new ReservatieView(EMAIL_CORRECT, OPHAALMOMENT_CORRECT,
                INDIENMOMENT_CORRECT, RESERVATIEMOMENT_CORRECT, reservatieLijnen);

        rv.setId(id1);

        resCatalogus.voegReservatieToe(rv);
        
        assertTrue(compareReservatieViews(rv, resCatalogus.geefAlleReservaties().get(2)));

    }

    public void verwijderReservatieVolledigCorrect() {
        assertEquals(r1, resCatalogus.verwijderReservatie(rv));

    }

    private boolean compareReservatieViews(ReservatieView rv1, ReservatieView rv2) {
        return (rv1.getEmailLener().equals(rv2.getEmailLener())
                && rv1.getOphaalmoment() == rv2.getOphaalmoment()
                && rv1.getIndienmoment() == rv2.getIndienmoment()
                && rv1.getReservatieLijnen().toString().equals(rv2.getReservatieLijnen().toString()));

    }
}
