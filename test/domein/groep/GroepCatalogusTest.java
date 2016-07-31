package domein.groep;

import domein.materiaal.Materiaal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GroepCatalogusTest {

    private Groep doelGroep;
    private Groep leerGebied;
    private Groep doelGroep2;
    private Groep leerGebied2;
    private List<Groep> groepen;
    private GroepCatalogus groepCat;

    @Before
    public void before() {
        this.doelGroep = new Groep("Doelgroep", false);
        this.leerGebied = new Groep("Leergebied", true);
        this.doelGroep2 = new Groep("Doelgroep2", false);
        this.leerGebied2 = new Groep("Leergebied2", true);
        groepen = new ArrayList<>(Arrays.asList(doelGroep, leerGebied, doelGroep2, leerGebied2));
        groepCat = new GroepCatalogus(groepen);
    }

    @Test
    public void testGetLeerGebiedenGeeftDeLeergebieden() {
        List<Groep> leergebieden = groepCat.getLeerGebieden();
        Assert.assertEquals(2, leergebieden.size());
        Assert.assertEquals(leerGebied, leergebieden.get(0));
        Assert.assertEquals(leerGebied2, leergebieden.get(1));
    }

    @Test
    public void testGetDoelGroepenGeeftDeDoelgroepen() {
        List<Groep> doelgroepen = groepCat.getDoelGroepen();
        Assert.assertEquals(2, doelgroepen.size());
        Assert.assertEquals(doelGroep, doelgroepen.get(0));
        Assert.assertEquals(doelGroep2, doelgroepen.get(1));
    }

    @Test
    public void testConvertStringListToGroepListReturnsJuisteLeergebieden() {
        List<Groep> leergebieden = groepCat.convertStringListToGroepList(
                Arrays.asList(leerGebied.getGroep(), leerGebied2.getGroep()), true
        );
        Assert.assertEquals(2, leergebieden.size());
        Assert.assertEquals(leerGebied, leergebieden.get(0));
        Assert.assertEquals(leerGebied2, leergebieden.get(1));
    }

    @Test
    public void testConvertStringListToGroepListReturnsJuisteDoelgroepen() {
        List<Groep> doelGroepen = groepCat.convertStringListToGroepList(
                Arrays.asList(doelGroep.getGroep(), doelGroep2.getGroep()), false
        );
        Assert.assertEquals(2, doelGroepen.size());
        Assert.assertEquals(doelGroep, doelGroepen.get(0));
        Assert.assertEquals(doelGroep2, doelGroepen.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVoegGroepToeMisluktAlsLeerGebiedAlBestaat() {
        groepCat.voegGroepToe(leerGebied.getGroep(), true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVoegGroepToeMisluktAlsDoelGroepAlBestaat() {
        groepCat.voegGroepToe(doelGroep.getGroep(), false);
    }

    @Test
    public void testVoegGroepToeReturndToegevoegdeGroep() {
        final String groepNaam = "Een geldige groepnaam.";
        Groep groep = groepCat.voegGroepToe(groepNaam, true);
        Assert.assertEquals(groepNaam, groep.getGroep());
        Assert.assertEquals(true, groep.isLeerGroep());
        groep = groepCat.voegGroepToe(groepNaam, false);
        Assert.assertEquals(groepNaam, groep.getGroep());
        Assert.assertEquals(false, groep.isLeerGroep());
    }

    @Test
    public void testGeefGroepGeeftLegeOptionalAlsHetNietBestaat() {
        Assert.assertEquals(false, groepCat.geefGroep("onbestaande groep...", false).isPresent());
        Assert.assertEquals(false, groepCat.geefGroep("onbestaande groep...", true).isPresent());
    }

    @Test
    public void testGeefGroepGeeftJuisteGroepTerug() {
        Groep db = groepCat.geefGroep(doelGroep.getGroep(), doelGroep.isLeerGroep()).get();
        Assert.assertEquals(doelGroep.getGroep(), db.getGroep());
        Assert.assertEquals(doelGroep.isLeerGroep(), db.isLeerGroep());
        Groep lg = groepCat.geefGroep(leerGebied.getGroep(), leerGebied.isLeerGroep()).get();
        Assert.assertEquals(leerGebied.getGroep(), lg.getGroep());
        Assert.assertEquals(leerGebied.isLeerGroep(), lg.isLeerGroep());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerwijderGroepWerktNietAlsDoelGroepNogInMateriaalZit() {
        List<Materiaal> materialen = new ArrayList<>(Arrays.asList(
                new Materiaal("een materiaal", 1).setDoelgroepen(new ArrayList<>(Arrays.asList(doelGroep)))
        ));
        groepCat.verwijderGroep(doelGroep.getGroep(), doelGroep.isLeerGroep(), materialen);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerwijderGroepWerktNietAlsLeergebiedNogInMateriaalZit() {
        List<Materiaal> materialen = new ArrayList<>(Arrays.asList(
                new Materiaal("een materiaal", 1).setLeergebieden(new ArrayList<>(Arrays.asList(leerGebied)))
        ));
        groepCat.verwijderGroep(leerGebied.getGroep(), leerGebied.isLeerGroep(), materialen);
    }

    @Test
    public void testVerwijderGroepWerktNietMetOngeldigeOfOnbestaandeGroepNaam() {
        List<Materiaal> materialen = new ArrayList<>();
        final String[] ONGELDIGE_NAMEN = new String[]{
            null,
            " ",
            "\t  \t",
            "onbestaande groep"
        };
        for (String ongeldigeNaam : ONGELDIGE_NAMEN) {
            try {
                groepCat.verwijderGroep(ongeldigeNaam, true, materialen);
                Assert.fail("verwijderGroep gooide geen exceptie bij het verwijderen van een ongeldige groepsnaam.");
            } catch (IllegalArgumentException e) {
            }
        }
    }
    
    @Test
    public void testVerwijderGroepWerkt(){
        List<Materiaal> materialen = new ArrayList<>();
        groepCat.verwijderGroep(doelGroep.getGroep(), doelGroep.isLeerGroep(), materialen);
        groepCat.verwijderGroep(leerGebied.getGroep(), leerGebied.isLeerGroep(), materialen);
        
        Assert.assertFalse(groepCat.geefGroep(doelGroep.getGroep(), doelGroep.isLeerGroep()).isPresent());
        Assert.assertFalse(groepCat.geefGroep(leerGebied.getGroep(), leerGebied.isLeerGroep()).isPresent());
    }

    private List<Materiaal> maakMateriaalListMetTweeMaterialenEenMetDoelGroepEnEenMetLeergebied() {
        return new ArrayList<>(Arrays.asList(
                new Materiaal("een materiaal", 1).setDoelgroepen(new ArrayList<>(Arrays.asList(doelGroep))),
                new Materiaal("een materiaal", 1).setLeergebieden(new ArrayList<>(Arrays.asList(leerGebied)))
        ));
    }

}
