/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein.materiaal;

import domein.firma.Firma;
import domein.firma.FirmaRepository;
import domein.groep.Groep;
import domein.groep.GroepRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import shared.MateriaalView;

public class MateriaalCatalogusTest {

    private MateriaalCatalogus materiaalCatalogus;
    private FirmaRepository firmaRepository;
    private GroepRepository groepsRepository;

    private final String CORRECT_NAAM = "NaamTest";
    private final int CORRECT_AANTAL = 15;
    
    private Materiaal m1 = new Materiaal("Wereldbol", 5);
    private Materiaal m2 = new Materiaal("Atlas", 15);
    
    private Firma f1 = new Firma("Firma", "email@firma.be");
    
    private Groep g1 = new Groep("Doelgroep1", false);
    private Groep g2 = new Groep("doelgroep2", false);
    private Groep g3 = new Groep("Leergebied1", true);
    private Groep g4 = new Groep("Leergebied2", true);
    
    private List<Materiaal> materialen;
    
    private MateriaalView mv;
    
    @Before
    public void before() {
        m1.setId(5);
        
        materialen = new ArrayList<>(Arrays.asList(m1, m2));
        
        firmaRepository = new FirmaRepository(Arrays.asList(f1));
        groepsRepository = new GroepRepository(Arrays.asList(g1, g2, g3, g4));

        materiaalCatalogus = new MateriaalCatalogus(materialen, firmaRepository, groepsRepository);
        
        mv = new MateriaalView(CORRECT_NAAM, CORRECT_AANTAL);

    }

    @Test
    public void voegNieuwMateriaalToeEnkelNaamEnAantal() {
        MateriaalView mv = new MateriaalView(CORRECT_NAAM, CORRECT_AANTAL);
        materiaalCatalogus.voegMateriaalToe(mv);
        
        assertTrue(compareMateriaalViews(mv, materiaalCatalogus.geefAlleMaterialenViews().get(2)));
    }

    @Test
    public void voegNieuwMateriaalToeVolledigCorrect() {
        mv.setAantalOnbeschikbaar(CORRECT_AANTAL)
            .setArtikelNummer("56465465sqf")
                .setDoelgroepen(Arrays.asList("Doelgroep1","doelgroep2"))
                .setFotoUrl("C://pad/naar/url.jpg")
                .setFirma("Firma")
                .setEmailFirma("email@firma.be")
                .setLeergebieden(Arrays.asList("Leergebied1","Leergebied2"))
                .setOmschrijving("Omschrijving")
                .setPlaats("Plaats")
                .setPrijs(2.22)
                .setUitleenbaarheid(true);
        
        materiaalCatalogus.voegMateriaalToe(mv);
        
        MateriaalView toegevoegdMv = materiaalCatalogus.geefAlleMaterialenViews().get(2);
        
        assertTrue(compareMateriaalViews(mv, toegevoegdMv));
        assertEquals(mv.getPrijs(), toegevoegdMv.getPrijs(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwMateriaalToeNaamIsEmpty() {
        mv.setNaam("");
        materiaalCatalogus.voegMateriaalToe(mv);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwMateriaalToeNaamIsNull() {
        mv.setNaam(null);
        materiaalCatalogus.voegMateriaalToe(mv);
    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwMateriaalToeAantalIsKleinerDanNul() {
        mv.setAantal(-1);
        materiaalCatalogus.voegMateriaalToe(mv);
    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwMateriaalToeAantalOnbeschikbaarIsKleinerDanNul() {
        mv.setAantalOnbeschikbaar(-1);
        materiaalCatalogus.voegMateriaalToe(mv);
    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwMateriaalToePrijsKleinerDanNul() {
        mv.setPrijs(-0.25);
        materiaalCatalogus.voegMateriaalToe(mv);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwMateriaalToeFouteFotoExtensie() {
        mv.setFotoUrl("c:/images/foto.icon");
        
        materiaalCatalogus.voegMateriaalToe(mv);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwMateriaalToeAantalOnbeschikbaarGroterDanAantal() {
        mv.setAantal(5);
        mv.setAantalOnbeschikbaar(10);
        
        materiaalCatalogus.voegMateriaalToe(mv);
    }
    
    @Test
    public void voegNieuwMateriaalToeAantalOnbeschikbaarGelijkAanAantal() {
        mv.setAantal(5);
        mv.setAantalOnbeschikbaar(5);
        
        materiaalCatalogus.voegMateriaalToe(mv);
        
        assertEquals(mv.getAantal() , materiaalCatalogus.geefAlleMaterialenViews().get(2).getAantal());
        assertEquals(mv.getAantalOnbeschikbaar(), materiaalCatalogus.geefAlleMaterialenViews().get(2).getAantalOnbeschikbaar());
    }
    
    @Test
    public void voegNieuwMateriaalToeAantalOnbeschikbaarKleinerDanAantal() {
        mv.setAantal(5);
        mv.setAantalOnbeschikbaar(2);
        
        materiaalCatalogus.voegMateriaalToe(mv);
        
        assertEquals(mv.getAantal() , materiaalCatalogus.geefAlleMaterialenViews().get(2).getAantal());
        assertEquals(mv.getAantalOnbeschikbaar(), materiaalCatalogus.geefAlleMaterialenViews().get(2).getAantalOnbeschikbaar());
    }
    
    @Test 
    public void geefMateriaalBestaandMateriaal() {
        assertEquals(m1 , materiaalCatalogus.geefMateriaal(m1.getNaam()).get());
    }
    
    @Test 
    public void geefMateriaalNietBestaandMateriaal() {
        assertEquals(Optional.empty() , materiaalCatalogus.geefMateriaal("blabla"));
    }
    
    @Test
    public void verwijderMateriaalBestaandMateriaal() {
        assertEquals(m1 , materiaalCatalogus.verwijderMateriaal(m1.getNaam()));
        assertEquals(1, materiaalCatalogus.geefAlleMaterialenViews().size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void verwijderMateriaalNietBestaandMateriaal() {
        materiaalCatalogus.verwijderMateriaal("blabla");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void verwijderMateriaalNaamIsNull() {
        materiaalCatalogus.verwijderMateriaal(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void verwijderMateriaalNaamIsEmpty() {
        materiaalCatalogus.verwijderMateriaal("");
    }
    
    @Test
    public void geefAlleMaterialenViews() {
        assertEquals(2, materiaalCatalogus.geefAlleMaterialenViews().size());
        assertTrue(materiaalCatalogus.geefAlleMaterialenViews().get(0) instanceof MateriaalView);
    }
    
    @Test
    public void geefMaterialenMetFilterIsNull() {
        assertEquals(2, materiaalCatalogus.geefMaterialenMetFilter(null).size());
        assertTrue(materiaalCatalogus.geefAlleMaterialenViews().get(0) instanceof MateriaalView);
    }
    
    @Test
    public void geefMaterialenMetFilterIsEmpty() {
        assertEquals(2, materiaalCatalogus.geefMaterialenMetFilter("").size());
        assertTrue(materiaalCatalogus.geefAlleMaterialenViews().get(0) instanceof MateriaalView);
    }
    
    @Test
    public void geefMaterialenMetFilterGeeftEenResultaat() {
        assertEquals(1, materiaalCatalogus.geefMaterialenMetFilter("Wereld").size());
        assertTrue(materiaalCatalogus.geefAlleMaterialenViews().get(0) instanceof MateriaalView);
    }
    
    @Test
    public void geefMaterialenMetFilterGeeftGeenResultaat() {
        assertEquals(0, materiaalCatalogus.geefMaterialenMetFilter("Eskimo").size());
        assertTrue(materiaalCatalogus.geefAlleMaterialenViews().get(0) instanceof MateriaalView);
    }
    
//    @Test 
//    public void toMateriaalViewEnkelNaamEnAantal() {           
//        mv = materiaalCatalogus.toMateriaalView(m1);
//        
//        assertTrue(compareMateriaalViewWithMateriaal(mv, m1));
//    }
    
    @Test
    public void toMateriaalViewCompleet() {
        m1.setFirma(f1)
                .setDoelgroepen(Arrays.asList(g1, g2))
                .setLeergebieden(Arrays.asList(g3, g4))
                .setFoto("c:/foto.jpg")
                .setBeschrijving("Een beschrijving")
                .setArtikelnummer("ABC123")
                .setAantalOnbeschikbaar(5)
                .setAantal(10)
                .setUitleenbaarheid(true)
                .setPlaats("Lokaal B4.013");
        
        
        mv = materiaalCatalogus.toMateriaalView(m1);
        
        assertTrue(compareMateriaalViewWithMateriaal(mv, m1));
    }
    
    @Test 
    public void wijsAttributenMateriaalViewToeAanMateriaalCorrect() {
        mv.setId(5)
                .setAantalOnbeschikbaar(CORRECT_AANTAL)
                .setArtikelNummer("56465465sqf")
                .setDoelgroepen(Arrays.asList("Doelgroep1","doelgroep2"))
                .setFotoUrl("C://pad/naar/url.jpg")
                .setFirma("Firma")
                .setEmailFirma("email@firma.be")
                .setLeergebieden(Arrays.asList("Leergebied1","Leergebied2"))
                .setOmschrijving("Omschrijving")
                .setPlaats("Plaats")
                .setPrijs(2.22)
                .setUitleenbaarheid(true);
        
        materiaalCatalogus.wijsAttributenMateriaalViewToeAanMateriaal(mv);
        
        assertTrue(compareMateriaalViewWithMateriaal(mv, m1));
                
    }

    private boolean compareMateriaalViews(MateriaalView mv1, MateriaalView mv2) {
        
        return (mv1.getNaam().equals(mv2.getNaam()))
                && (mv1.getAantal() == mv2.getAantal())
                && (mv1.getFirma() == null ? mv2.getFirma() == null : mv1.getFirma().equals(mv2.getFirma()))
                && (mv1.getEmailFirma() == null ? mv2.getEmailFirma() == null : mv1.getEmailFirma().equals(mv2.getEmailFirma()))
                && (mv1.getFotoUrl() == null ? mv2.getFotoUrl() == null : mv1.getFotoUrl().equals(mv2.getFotoUrl()))
                && (mv1.getOmschrijving() == null ? mv2.getOmschrijving() == null : mv1.getOmschrijving().equals(mv2.getOmschrijving()))
                && (mv1.getArtikelNummer() == null ? mv2.getArtikelNummer() == null : mv1.getArtikelNummer().equals(mv2.getArtikelNummer()))
                && (mv1.getAantalOnbeschikbaar() == mv2.getAantalOnbeschikbaar())
                && (mv1.isUitleenbaarheid() == mv2.isUitleenbaarheid())
                && (mv1.getPlaats() == null ? mv2.getPlaats() == null : mv1.getPlaats().equals(mv2.getPlaats()))
                && (mv1.getDoelgroepen() == null ? mv2.getDoelgroepen() == null : mv1.getDoelgroepen().equals(mv2.getDoelgroepen()))
                && (mv1.getLeergebieden() == null ? mv2.getLeergebieden() == null : mv1.getLeergebieden().equals(mv2.getLeergebieden()));
    }

    private boolean compareMateriaalViewWithMateriaal(MateriaalView mv, Materiaal mat) {
        
        return (mv.getNaam().equals(mat.getNaam()))
                && (mv.getAantal() == mat.getAantal())
                && (mv.getFirma() == null ? mat.getFirma().getNaam() == null : mv.getFirma().equals(mat.getFirma().getNaam()))
                && (mv.getEmailFirma() == null ? mat.getFirma().getEmail() == null : mv.getEmailFirma().equals(mat.getFirma().getEmail()))
                && (mv.getFotoUrl() == null ? mat.getFoto()== null : mv.getFotoUrl().equals(mat.getFoto()))
                && (mv.getOmschrijving() == null ? mat.getBeschrijving()== null : mv.getOmschrijving().equals(mat.getBeschrijving()))
                && (mv.getArtikelNummer() == null ? mat.getArtikelnummer()== null : mv.getArtikelNummer().equals(mat.getArtikelnummer()))
                && (mv.getAantalOnbeschikbaar() == mat.getAantalOnbeschikbaar())
                && (mv.isUitleenbaarheid() == mat.isUitleenbaarheid())
                && (mv.getPlaats() == null ? mat.getPlaats() == null : mv.getPlaats().equals(mat.getPlaats()))
                && (mv.getDoelgroepen() == null ? mv.getDoelgroepen() == null : mv.getDoelgroepen().equals(geefGroepen(mat.getDoelgroepen())))
                && (mv.getLeergebieden() == null ? mv.getLeergebieden() == null : mv.getLeergebieden().equals(geefGroepen(mat.getLeergebieden())))
                ;
    }
    
    private List<String> geefGroepen(List<Groep> groepen) {
        return groepen.stream().map(Groep::getGroep).collect(Collectors.toList());
    }
}
