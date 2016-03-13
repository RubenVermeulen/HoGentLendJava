/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein.reservatie;

import domein.gebruiker.Gebruiker;
import domein.materiaal.Materiaal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import shared.ReservatieLijnView;

/**
 *
 * @author alexa
 */
public class ReservatieTest {

    private Reservatie r1;

    private Long id;
    private Gebruiker lener;
    private LocalDateTime ophaalmoment;
    private LocalDateTime indienmoment;
    private LocalDateTime reservatiemoment;
    private boolean opgehaald;
    private List<ReservatieLijn> reservatieLijnen;

    private final Gebruiker sven = new Gebruiker("Sven", "Dedeene", "sven@hogent.be", "sven", true, true, true);
    private final LocalDateTime OPHAALMOMENT_CORRECT = LocalDateTime.of(2016, Month.MARCH, 14, 15, 30);
    private final LocalDateTime INDIENMOMENT_CORRECT = LocalDateTime.of(2016, Month.MARCH, 21, 15, 30);

    private final LocalDateTime FILTER_OPHAALMOMENT_CORRECT = LocalDateTime.of(2015, Month.MARCH, 14, 15, 30);
    private final LocalDateTime FILTER_INDIEN_CORRECT = LocalDateTime.of(2017, Month.MARCH, 14, 15, 30);

    private final LocalDateTime FILTER_OPHAALMOMENT_GEENOVERLAPPING = LocalDateTime.of(2018, Month.MARCH, 14, 15, 30);
    private final LocalDateTime FILTER_INDIEN_GEENOVERLAPPING = LocalDateTime.of(2018, Month.MARCH, 14, 15, 30);

    private final LocalDateTime OPHAALMOMENT_TUSSEN = LocalDateTime.of(2016, Month.MARCH, 17, 15, 30);
    private final LocalDateTime INDIENMOMENT_TUSSEN = LocalDateTime.of(2016, Month.MARCH, 19, 15, 30);

    private Materiaal m1 = new Materiaal("Wereldbol", 5);
    private final long m1id = 88;

    @Before
    public void before() {
        lener = sven;
        ophaalmoment = OPHAALMOMENT_CORRECT;
        indienmoment = INDIENMOMENT_CORRECT;
        reservatiemoment = LocalDateTime.now();
        opgehaald = false;

        m1.setId(m1id);

        reservatieLijnen = Arrays.asList(new ReservatieLijn(m1, 2, OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT));

        r1 = new Reservatie(lener, ophaalmoment, indienmoment, reservatiemoment, opgehaald, reservatieLijnen);
    }

    @Test
    public void containsFilterVolledigCorrect() {
        String sFilter = "SVEN";

        assertTrue(r1.containsFilter(sFilter, FILTER_OPHAALMOMENT_CORRECT, FILTER_INDIEN_CORRECT));
    }

    @Test
    public void containsFilterLowerEnUpppercase() {
        String sFilter = "sVen";

        assertTrue(r1.containsFilter(sFilter, FILTER_OPHAALMOMENT_CORRECT, FILTER_INDIEN_CORRECT));
    }

    @Test
    public void containsFilterFouteGebruiker() {
        String sFilter = "XANDER";

        assertFalse(r1.containsFilter(sFilter, FILTER_OPHAALMOMENT_CORRECT, FILTER_INDIEN_CORRECT));
    }


    
    public void containsFilterGeenFilter() {
        String sFilter = "";

        assertTrue(r1.containsFilter(sFilter, FILTER_OPHAALMOMENT_CORRECT, FILTER_INDIEN_CORRECT));
    }


    @Test
    public void containsFilterGeenOverlapping() {
        String sFilter = "SVEN";

        assertFalse(r1.containsFilter(sFilter, FILTER_OPHAALMOMENT_GEENOVERLAPPING, FILTER_INDIEN_GEENOVERLAPPING));
    }

    @Test
    public void containsFilterTussenReservatiedatums() {
        String sFilter = "SVEN";

        assertTrue(r1.containsFilter(sFilter, OPHAALMOMENT_TUSSEN, INDIENMOMENT_TUSSEN));
    }

    @Test
    public void containsFilterFilterDatumHetzelfdeAlsReservatieDatum() {
        String sFilter = "SVEN";

        assertTrue(r1.containsFilter(sFilter, OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT));
    }

    @Test
    public void containsFilterGeenFilterDatum() {
        String sFilter = "SVEN";

        assertTrue(r1.containsFilter(sFilter, null, null));
    }

    @Test
    public void containsFilterFilterenOpEmail() {
        String sFilter = "sven@hogent.be";

        assertTrue(r1.containsFilter(sFilter, FILTER_OPHAALMOMENT_CORRECT, FILTER_INDIEN_CORRECT));
    }

}
