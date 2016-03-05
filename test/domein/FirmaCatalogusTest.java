package domein;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.MateriaalView;

public class FirmaCatalogusTest {

    private final Firma FIRMA_1 = new Firma("BFirma1", "contact@firma1.com");
    private final Firma FIRMA_2 = new Firma("AFirma2");
    
    private final List<MateriaalView> materialenMetFrima_1 = new ArrayList<>(Arrays.asList(
            new MateriaalView("test_materiaal", 1).setFirma(FIRMA_1.getNaam())
    ));

    private final List<Firma> firmas = new ArrayList<>(Arrays.asList(
            FIRMA_1,
            FIRMA_2
    ));

    private FirmaCatalogus firmaCat;

    @Before
    public void before() {
        firmaCat = new FirmaCatalogus(firmas);
    }

    @Test
    public void testGetAllFirmasSortedGeeftFirmasAlfabetisch() {
        List<Firma> gesorteerdeFrimas = firmaCat.getAllFirmasSorted();
        Assert.assertEquals(FIRMA_2, gesorteerdeFrimas.get(0));
        Assert.assertEquals(FIRMA_1, gesorteerdeFrimas.get(1));
    }

    @Test
    public void testGeefFirmaGeeftJuisteFirma() {
        Assert.assertEquals(FIRMA_1, firmaCat.geefFirma(FIRMA_1.getNaam()).get());
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
        firmaCat.voegFrimaToe(FIRMA_1.getNaam(), FIRMA_1.getEmail());
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
    
    @Test(expected = IllegalArgumentException.class )
    public void testVerwijderFirmaKanGeenOnbestaandeFirmasVerwijderen(){
        firmaCat.verwijderFirma("niet bestaand", materialenMetFrima_1);
    }
    
    @Test(expected = IllegalArgumentException.class )
    public void testVerwijderFirmaKanGeenFirmasVerwijderenIndienMateriaalFirmaNogBevat(){
        firmaCat.verwijderFirma(FIRMA_1.getNaam(), materialenMetFrima_1);
    }
    
    @Test
    public void testVerwijderFirmaGeeftCorrecteVerwijderdeFirmaTerug(){
        Firma f = firmaCat.verwijderFirma(FIRMA_2.getNaam(), materialenMetFrima_1);
        Assert.assertEquals(FIRMA_2.getNaam(), f.getNaam());
        Assert.assertEquals(FIRMA_2.getEmail(), f.getEmail());
    }
}
