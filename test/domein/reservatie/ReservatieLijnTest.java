/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein.reservatie;

import domein.materiaal.Materiaal;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author alexa
 */
public class ReservatieLijnTest {

    private Long id;
    private LocalDateTime ophaalmoment;
    private LocalDateTime indienmoment;
    private Materiaal materiaal;
    private int aantal;
    

    private ReservatieLijn reservatieLijn;

    private Materiaal m1 = new Materiaal("Wereldbol", 5);
    private final long m1id = 88;

    private final LocalDateTime OPHAALMOMENT_CORRECT = LocalDateTime.of(2016, Month.MARCH, 14, 15, 30);
    private final LocalDateTime INDIENMOMENT_CORRECT = LocalDateTime.of(2016, Month.MARCH, 21, 15, 30);
    private final LocalDateTime FILTER_OPHAALMOMENT_CORRECT = LocalDateTime.of(2015, Month.MARCH, 14, 15, 30);
    private final LocalDateTime FILTER_INDIEN_CORRECT = LocalDateTime.of(2017, Month.MARCH, 14, 15, 30);
    private final LocalDateTime FILTER_OPHAALMOMENT_GEENOVERLAPPING = LocalDateTime.of(2018, Month.MARCH, 14, 15, 30);
    private final LocalDateTime FILTER_INDIEN_GEENOVERLAPPING = LocalDateTime.of(2018, Month.MARCH, 14, 15, 30);
    private final LocalDateTime OPHAALMOMENT_TUSSEN = LocalDateTime.of(2016, Month.MARCH, 17, 15, 30);
    private final LocalDateTime INDIENMOMENT_TUSSEN = LocalDateTime.of(2016, Month.MARCH, 19, 15, 30);

    @Before
    public void before() {
        m1.setId(m1id);
        materiaal = m1;
        aantal = 3;

        ophaalmoment = OPHAALMOMENT_CORRECT;
        indienmoment = INDIENMOMENT_CORRECT;

        reservatieLijn = new ReservatieLijn(materiaal, aantal, ophaalmoment, indienmoment);
    }

    @Test
    public void containsFilterVolledigCorrect() {
        String sFilter = "WERELDBOL";

        assertTrue(reservatieLijn.containsFilter(sFilter, FILTER_OPHAALMOMENT_CORRECT, FILTER_INDIEN_CORRECT));
    }

    @Test
    public void containsFilterUpperEnLowerCase() {
        String sFilter = "wErElDbOl";

        assertTrue(reservatieLijn.containsFilter(sFilter, FILTER_OPHAALMOMENT_CORRECT, FILTER_INDIEN_CORRECT));
    }
    
    @Test
    public void containsFilterFoutMateriaal() {
        String sFilter = "GEODRIEHOEK";

        assertFalse(reservatieLijn.containsFilter(sFilter, FILTER_OPHAALMOMENT_CORRECT, FILTER_INDIEN_CORRECT));
    }
    
    @Test
    public void containsFilterGeenFilter() {
        String sFilter = "";

        assertTrue(reservatieLijn.containsFilter(sFilter, FILTER_OPHAALMOMENT_CORRECT, FILTER_INDIEN_CORRECT));
    }
    
    @Test
    public void containsFilterGeenOverlapping() {
        String sFilter = "WERELDBOL";

        assertFalse(reservatieLijn.containsFilter(sFilter, FILTER_OPHAALMOMENT_GEENOVERLAPPING, FILTER_INDIEN_GEENOVERLAPPING));
    }
    
    @Test
    public void containsFilterTussenReservatiedatums() {
        String sFilter = "WERELDBOL";

        assertTrue(reservatieLijn.containsFilter(sFilter, OPHAALMOMENT_TUSSEN, INDIENMOMENT_TUSSEN));
    }
    
    @Test
    public void containsFilterGeenFilterdatums() {
        String sFilter = "WERELDBOL";

        assertTrue(reservatieLijn.containsFilter(sFilter, null, null));
    }
    
    @Test
    public void containsFilterFilterHetzelfdeAlsReservatiedatums() {
        String sFilter = "WERELDBOL";

        assertTrue(reservatieLijn.containsFilter(sFilter, OPHAALMOMENT_CORRECT, INDIENMOMENT_CORRECT));
    }
    
    
    
}
