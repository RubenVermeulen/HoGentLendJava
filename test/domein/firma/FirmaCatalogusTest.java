package domein.firma;

import domein.firma.FirmaCatalogus;
import domein.firma.Firma;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.MateriaalView;

public class FirmaCatalogusTest {

    private Firma firma1;
    private Firma firma2;

    private List<MateriaalView> materialenMetFrima_1;
    private List<Firma> firmas;
    
    private FirmaCatalogus firmaCat;

    @Before
    public void before() {
        firma1 = new Firma("BFirma1", "contact@firma1.com");
        firma2 = new Firma("AFirma2");

        materialenMetFrima_1 = new ArrayList<>(Arrays.asList(
                new MateriaalView("test_materiaal", 1).setFirma(firma1.getNaam())
        ));
        firmas = new ArrayList<>(Arrays.asList(
                firma1,
                firma2
        ));

        firmaCat = new FirmaCatalogus(firmas);

    }

    @Test
    public void testGetAllFirmasSortedGeeftFirmasAlfabetisch() {
        List<Firma> gesorteerdeFrimas = firmaCat.getAllFirmasSorted();
        Assert.assertEquals(firma2, gesorteerdeFrimas.get(0));
        Assert.assertEquals(firma1, gesorteerdeFrimas.get(1));
    }

    @Test
    public void testGeefFirmaGeeftJuisteFirma() {
        Assert.assertEquals(firma1, firmaCat.geefFirma(firma1.getNaam()).get());
    }

    @Test
    public void testGeefFirmaGeeftLegeOptionalBijLeegeOfOngeldigeNaam() {
        final String[] ONBESTAANDE_FIRMA_NAMEN = new String[]{
            "bfirma1",
            "onbestaand",
            "",
            "  ",
            null
        };
        for (String naam : ONBESTAANDE_FIRMA_NAMEN) {
            Assert.assertFalse(firmaCat.geefFirma(naam).isPresent());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVoegFirmaToeKanNietMetAlBestaandeNaam() {
        firmaCat.voegFrimaToe(firma1.getNaam(), firma1.getEmail());
    }

    @Test
    public void testVoegFirmaToeMetGeldigeParametersSlaatFirmaOp() {
        final String FIRMA_NAAM = "Geldige firma";
        final String FIRMA_EMAIL = "contact@geldig.com";

        Firma f = firmaCat.voegFrimaToe(FIRMA_NAAM, FIRMA_EMAIL);

        Assert.assertEquals(FIRMA_NAAM, f.getNaam());
        Assert.assertEquals(FIRMA_EMAIL, f.getEmail());
    }

    @Test
    public void testVoegFirmaToeMetGeldigeParametersGeeftJuisteFirmaTerug() {
        final String FIRMA_NAAM = "Geldige firma";
        final String FIRMA_EMAIL = "contact@geldig.com";

        firmaCat.voegFrimaToe(FIRMA_NAAM, FIRMA_EMAIL);
        Optional<Firma> f2 = firmaCat.geefFirma(FIRMA_NAAM);

        Assert.assertEquals(FIRMA_NAAM, f2.get().getNaam());
        Assert.assertEquals(FIRMA_EMAIL, f2.get().getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerwijderFirmaKanGeenOnbestaandeFirmasVerwijderen() {
        firmaCat.verwijderFirma("niet bestaand", materialenMetFrima_1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerwijderFirmaKanGeenFirmasVerwijderenIndienMateriaalFirmaNogBevat() {
        firmaCat.verwijderFirma(firma1.getNaam(), materialenMetFrima_1);
    }

    @Test
    public void testVerwijderFirmaGeeftCorrecteVerwijderdeFirmaTerug() {
        Firma f = firmaCat.verwijderFirma(firma2.getNaam(), materialenMetFrima_1);
        Assert.assertEquals(firma2.getNaam(), f.getNaam());
        Assert.assertEquals(firma2.getEmail(), f.getEmail());
    }
}
