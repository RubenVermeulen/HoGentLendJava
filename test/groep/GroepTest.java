package groep;

import domein.groep.Groep;
import org.junit.Assert;
import org.junit.Test;

public class GroepTest {

    private static final String GELDIGE_GROEP_NAAM = "Geldige Groep Naam";

    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void testSetGroepMagNietLeegNullOfWhiteZijn() {
        final String[] LEGE_STRINGS = new String[]{null, "", " ", "\t "};
        Groep groep = new Groep(GELDIGE_GROEP_NAAM, false);
        for (String str : LEGE_STRINGS) {
            try {
                groep.setGroep(str);
                Assert.fail("De groep accepteerde een lege groepnaam string.");
            } catch (IllegalArgumentException e) {
            }
            try {
                new Groep(str, false);
                Assert.fail("De groep accepteerde een lege groepnaam string.");
            } catch (IllegalArgumentException e) {
            }
        }
    }

    @Test
    public void testContainsFilterReturnsCorrectResult() {
        final String groepNaam = "De groep naam.";
        final String[] GELDIGE_FILTERS = new String[]{"De", " groEP", "p n", "de groep", null, ""};
        final String[] ONGELDIGE_FILTERS = new String[]{"de.", "greop", "groepnaam", "\t\t"};
        Groep groep = new Groep(groepNaam, false);
        verifyContainsFilterResult(groep, true, GELDIGE_FILTERS);
        verifyContainsFilterResult(groep, false, ONGELDIGE_FILTERS);
    }

    private void verifyContainsFilterResult(Groep groepToTest, boolean expectedResult, String[] filtersToTest) {
        for (String filter : filtersToTest) {
            Assert.assertEquals(expectedResult, groepToTest.containsFilter(filter));
        }
    }

}
