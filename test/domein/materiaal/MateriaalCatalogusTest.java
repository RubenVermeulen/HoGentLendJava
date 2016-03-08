/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein.materiaal;

import domein.materiaal.MateriaalCatalogus;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import shared.MateriaalView;

/**
 *
 * @author Xander
 */
public class MateriaalCatalogusTest {

    private MateriaalCatalogus materiaalCatalogus;

    private final String CORRECT_NAAM = "NaamTest";
    private final int CORRECT_AANTAL = 15;

    @Before
    public void before() {
        //materiaalcatalogus is leeg, zal altijd slechts 1 materiaal bevatten als men 1 materiaal toevoegd
        materiaalCatalogus = new MateriaalCatalogus();

    }

    @Test
    public void voegNieuwMateriaalToeEnkelNaamEnAantal() {
        MateriaalView mv = new MateriaalView(CORRECT_NAAM, CORRECT_AANTAL);
        materiaalCatalogus.voegMateriaalToe(mv);
        assertTrue(compareMateriaalViews(mv, materiaalCatalogus.geefAlleMaterialen().get(0)));
    }

    @Test
    public void voegNieuwMateriaalToeVolledigCorrect() {
        // TODO: fix this
        /*      MateriaalView mv = new MateriaalView(CORRECT_NAAM, CORRECT_AANTAL).setAantalOnbeschikbaar(CORRECT_AANTAL)
                .setArtikelNummer("56465465sqf").setDoelgroepen("Doelgroep1, doelgroep2")
                .setFotoUrl("C://pad/naar/url").setFirma("Firma").setEmailFirma("email@firma.be")
                .setLeergebieden("Leergebied1,Leergebied2").setOmschrijving("Omschrijving")
                .setPlaats("Plaats").setPrijs(2.22).setUitleenbaarheid(true);
        materiaalCatalogus.voegMateriaalToe(mv);
        assertTrue(compareMateriaalViews(mv, materiaalCatalogus.geefAlleMaterialen().get(0)));
        assertEquals(mv.getPrijs(), materiaalCatalogus.geefAlleMaterialen().get(0).getPrijs(),0.001);*/
    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwMateriaalToeNaamIsEmpty() {
        MateriaalView mv = new MateriaalView("", CORRECT_AANTAL);
        materiaalCatalogus.voegMateriaalToe(mv);
    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwMateriaalToeAantalIsKleinerDanNul() {
        MateriaalView mv = new MateriaalView(CORRECT_NAAM, -1);
        materiaalCatalogus.voegMateriaalToe(mv);
    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwMateriaalToeAantalOnbeschikbaarIsKleinerDanNul() {
        MateriaalView mv = new MateriaalView(CORRECT_NAAM, CORRECT_AANTAL).setAantalOnbeschikbaar(-1);
        materiaalCatalogus.voegMateriaalToe(mv);
    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwMateriaalToeEmailGeenAtEnPunt() {
        MateriaalView mv = new MateriaalView(CORRECT_NAAM, CORRECT_AANTAL).setEmailFirma("blablabla");
        materiaalCatalogus.voegMateriaalToe(mv);
    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwMateriaalToeEmailVreemdeKarakters() {
        MateriaalView mv = new MateriaalView(CORRECT_NAAM, CORRECT_AANTAL).setEmailFirma("xander.berkein@gmiail=.com");
        materiaalCatalogus.voegMateriaalToe(mv);
    }

    @Test(expected = IllegalArgumentException.class)
    public void voegNieuwMateriaalToePrijsKleinerDanNul() {
        MateriaalView mv = new MateriaalView(CORRECT_NAAM, CORRECT_AANTAL).setPrijs(-0.25);
        materiaalCatalogus.voegMateriaalToe(mv);
    }

    public boolean compareMateriaalViews(MateriaalView mv1, MateriaalView mv2) {
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

}
