/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author ruben
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    domein.DomeinControllerTest.class,
    domein.firma.FirmaTest.class,
    domein.firma.FirmaCatalogusTest.class,
    domein.gebruiker.GebruikerTest.class,
    domein.gebruiker.BeheerderCatalogusTest.class,
    domein.groep.GroepTest.class,
    domein.groep.GroepCatalogusTest.class,
    domein.materiaal.MateriaalTest.class,
    domein.materiaal.MateriaalCatalogusTest.class,
domein.reservatie.ReservatieCatalogusTest.class,
})
public class TestSuite {
    
}
