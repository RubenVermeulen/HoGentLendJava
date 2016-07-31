package domein.firma;

import org.junit.Assert;
import org.junit.Test;

public class FirmaTest {

    final String GELDIG_FIRMA_NAAM = "Geldige Firma Naam";
    final String GELDIG_EMAIL = "geldig@email.com";

    @Test(expected = IllegalArgumentException.class)
    public void testNaamVanFirmaMagNietNullZijn() {
        maakFirmaMetNaam(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNaamVanFirmaMagNietLeegZijn() {
        maakFirmaMetNaam("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNaamVanFirmaMagNietWitRuimteZijn() {
        maakFirmaMetNaam("   ");
    }

    @Test
    public void testSetEmailVanFirmaMagLeegNullOfWitRuimteZijn() {
        Firma firma = new Firma(GELDIG_FIRMA_NAAM);
        firma.setEmail("");
        firma.setEmail("   ");
        firma.setEmail(null);
    }

    @Test
    public void testSetEmailIndienEmailIsNietLeegMagNietOngeldigEmailZijn() {
        Firma firma = new Firma(GELDIG_FIRMA_NAAM);
        final String[] ONGELDIGE_EMAILS = new String[]{
            "ongeldig@email",
            "@ongeldig.email",
            "ongeldige@email.email.email123",
            "ongeldige@@@email.email.email123",
            "onge   ldige  @email.emai"
        };

        for (String ongeldigEmail : ONGELDIGE_EMAILS) {
            try {
                firma.setEmail(ongeldigEmail);
                Assert.fail("De firma kreeg een ongeldige email zonder een exceptie te gooien.");
            } catch (IllegalArgumentException e) {
            }
        }
    }

    @Test
    public void testSetEmailIndienEmailGeldigIsLukt() {
        Firma firma = new Firma(GELDIG_FIRMA_NAAM);
        firma.setEmail(GELDIG_EMAIL);
    }

    @Test
    public void testContainsFilter() {
        final String NAAM = "de firma NAAM";
        final String EMAIL = "ContaCT@email.be";
        final String[] GELDIGE_FILTERS = new String[]{
            "NAAM",
            "naam",
            "de firm",
            "contact@",
            "Be",
            "",
            null,
            " "
        };
        final String[] ONGELDIGE_FILTERS = new String[]{
            "firma1",
            " @email",
            "firmanaam",
            "een willekeurige zin"
        };

        Firma firma = new Firma(NAAM);
        firma.setEmail(EMAIL);

        pasFiltersToeMetVerwachtReturn(firma, GELDIGE_FILTERS, true);
        pasFiltersToeMetVerwachtReturn(firma, ONGELDIGE_FILTERS, false);
    }

    @Test
    public void testGetNaamVanFirmaMetGeldigeNaamGeeftDeGeldigeNaamTerug() {
        Firma firma = new Firma(GELDIG_FIRMA_NAAM);
        Assert.assertEquals(GELDIG_FIRMA_NAAM, firma.getNaam());
    }

    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    private void maakFirmaMetNaam(String firmaNaam) {
        new Firma(firmaNaam);
    }

    private void pasFiltersToeMetVerwachtReturn(Firma firma, String[] filters, boolean result) {
        for (String filter : filters) {
            Assert.assertEquals(result, firma.containsFilter(filter));
        }
    }
}
