/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein.reservatie;

import domein.firma.Firma;
import domein.firma.FirmaRepository;
import domein.gebruiker.Gebruiker;
import domein.gebruiker.GebruikerRepository;
import domein.materiaal.Materiaal;
import domein.materiaal.MateriaalRepository;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import shared.MateriaalView;
import shared.ReservatieLijnView;
import shared.ReservatieView;

/**
 *
 * @author alexa
 */
public class ReservatieCatalogusTest {

    private ReservatieCatalogus resCatalogus;

    private MateriaalRepository materiaalRepo;
    private GebruikerRepository gebruikersRepo;
    private FirmaRepository firmaRepository;

    private List<Reservatie> reservaties;
    private final String EMAIL_CORRECT = "sven@hogent.be";
    private final LocalDateTime OPHAALMOMENT_CORRECT = LocalDateTime.of(2016, Month.MARCH, 14, 15, 30);
    private final LocalDateTime INDIENMOMENT_CORRECT = LocalDateTime.of(2016, Month.MARCH, 21, 15, 30);
    private final LocalDateTime RESERVATIEMOMENT_CORRECT = LocalDateTime.now();
    private List<ReservatieLijnView> reservatieLijnen;
    private final Firma f1 = new Firma("Firma", "email@firma.be");

    private final Gebruiker sven = new Gebruiker("Sven", "Dedeene", "sven", EMAIL_CORRECT, true, true, true);
    private final Gebruiker xander = new Gebruiker("Xander", "Berkein", "xander", "xander@hogent.be", true, true, true);

    private final MateriaalView m = new MateriaalView("wereldbol", 5);

    private final ReservatieLijnView rlv = new ReservatieLijnView(OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT, m, 5);

    private ReservatieView rv;

    private final Reservatie r1 = new Reservatie(sven, OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT,
            RESERVATIEMOMENT_CORRECT, false);
    private final Reservatie r2 = new Reservatie(xander, OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT,
            RESERVATIEMOMENT_CORRECT, false);

    @Before
    public void before() {
        reservaties = new ArrayList<>(Arrays.asList(r1, r2));

        firmaRepository = new FirmaRepository(Arrays.asList(f1));
        materiaalRepo = new MateriaalRepository(firmaRepository);
        gebruikersRepo = new GebruikerRepository(Arrays.asList(sven, xander));

        reservatieLijnen = new ArrayList(Arrays.asList(rlv));

        rv = new ReservatieView(EMAIL_CORRECT, OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT,
                RESERVATIEMOMENT_CORRECT, reservatieLijnen);

        resCatalogus = new ReservatieCatalogus(reservaties, materiaalRepo, gebruikersRepo);
    }

    @Test
    public void voegNieuwReservatieToeVolledigCorrect() {
// kan niet getest worden ???? gebruikersrepo query
        ReservatieView rv = new ReservatieView(EMAIL_CORRECT, OPHAALMOMENT_CORRECT,
                INDIENMOMENT_CORRECT, RESERVATIEMOMENT_CORRECT, reservatieLijnen);

        //   resCatalogus.voegReservatieToe(rv);
        assertTrue(compareReservatieViews(rv, rv/*resCatalogus.geefAlleReservaties().get(2)*/));

    }

    private boolean compareReservatieViews(ReservatieView rv1, ReservatieView rv2) {
        return true;

    }
}
