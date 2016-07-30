package domein.gebruiker;

import domein.groep.Groep;
import org.junit.Assert;
import org.junit.Test;

public class GebruikerTest {

    @Test
    public void testEmailWordLowerCase(){
        final String emailUpper = "EEN@Email.Com";
        final String emailLower = "een@email.com";
        Gebruiker gebruiker = new Gebruiker(null, null, emailUpper, false, false, false);
        Assert.assertEquals(emailLower, gebruiker.getEmail());
    }
    
    @Test
    public void testContainsFilterReturnsCorrectResult() {
        final String email = "een@email.com";
        final String voornaam = "Sven";
        final String naam = "Dedeene";
        final String[] GELDIGE_FILTERS = new String[]{
            "een@", "EmaIL", "sven", "VEN", "Ded", "DEen", null, ""};
        final String[] ONGELDIGE_FILTERS = new String[]{"eeen", "een willekeurige string", "ddeene", "\t\t"};
        Gebruiker gebruiker = new Gebruiker(voornaam, naam, email, false, false, false);
        verifyContainsFilterResult(gebruiker, true, GELDIGE_FILTERS);
        verifyContainsFilterResult(gebruiker, false, ONGELDIGE_FILTERS);
    }

    private void verifyContainsFilterResult(Gebruiker gebruikerToTest, boolean expectedResult, String[] filtersToTest) {
        for (String filter : filtersToTest) {
            Assert.assertEquals(expectedResult, gebruikerToTest.containsFilter(filter));
        }
    }
}
