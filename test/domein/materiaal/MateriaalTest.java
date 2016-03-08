package domein.materiaal;

import domein.firma.Firma;
import domein.groep.Groep;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class MateriaalTest {

    /**
     * Filterbare attributen: - Naam -Beschrijving -Plaats -Leergebieden
     * -Doelgroepen -Firma -ArtikelNummer
     *
     */
    @Test
    public void testContainsFilterGeeftJuistResultaat() {
        Materiaal mat = new Materiaal("Naam Van Materiaal", 1)
                .setBeschrijving("De beschrijving van het materiaal.")
                .setPlaats("De plaats van materiaal")
                .setLeergebieden(new ArrayList<>(Arrays.asList(new Groep("Een Leergebied", true))))
                .setDoelgroepen(new ArrayList(Arrays.asList(new Groep("Een Doelgroep", false))))
                .setFirma(new Firma("Firma Naam", "contact@firma.be"));
        final String[] VALID_FILTERS = new String[]{
            "naam van",
            "de beschrijving",
            "de plaats",
            "een leergebied",
            "een doelgroep",
            "firma naam",
            "firma.be",
            null,
            ""
        };
        final String[] NOT_VALID_FILTERS = new String[]{
            "invalid",
            "komt niet voor",
            "\t  \t",
            "doelgroepp",
            "contakt",
            "material"
        };
        verifyContainsFilterResult(mat, true, VALID_FILTERS);
        verifyContainsFilterResult(mat, false, NOT_VALID_FILTERS);
    }

    private void verifyContainsFilterResult(Materiaal materiaalToTest, boolean expectedResult, String[] filtersToTest) {
        for (String filter : filtersToTest) {
            Assert.assertEquals(expectedResult, materiaalToTest.containsFilter(filter));
        }
    }
}
