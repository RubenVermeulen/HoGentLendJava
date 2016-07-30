package domein.gebruiker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.ObservableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BeheerderCatalogusTest {

    private final String PASSWORD = "rubsup";
    private final String PASSWORD_HASH = "phQwxXirQXoxORs1hBuWctW6AuGTX5MiaYuMgs1N/BJOAWkFU6I0GGqZv7jYL1xY";

    private Gebruiker hoofdBeheerder;
    private Gebruiker beheerder;

    private List<Gebruiker> beheerders;
    private BeheerderCatalogus beheerderCat;

    @Before
    public void before() {
        hoofdBeheerder = new Gebruiker("HoofdBeheer Voornaam",
                "HoofdBeheer Naam",
                "voornaam.naam@hoofdbeheerder.com",
                true, false, false);

        beheerder = new Gebruiker("Beheer Voornaam",
                "Beheerder Naam",
                "voornaam.naam@beheer.com",
                false, true, false);
        beheerders = new ArrayList<>(Arrays.asList(hoofdBeheerder, beheerder));
        beheerderCat = new BeheerderCatalogus(beheerders);
    }

    @Test
    public void testGetBeheerderGeeftLeegIncorrectEmail() {
        Assert.assertFalse(beheerderCat.getBeheerder("incorrect email").isPresent());
        Assert.assertFalse(beheerderCat.getBeheerder("jef@hoofdbeheerder.com").isPresent());
    }

    @Test
    public void testGetBeheerderGeeftBeheerderCorrectEmailEnPass() {
        Gebruiker result = beheerderCat.getBeheerder(beheerder.getEmail()).get();
        Assert.assertEquals(beheerder, result);
    }

    @Test
    public void testNaVerwijderBeheerderGeeftBeheerderNietMeerTerug() {
        beheerderCat.verwijderBeheerder(beheerder);
        Assert.assertFalse(beheerderCat.getBeheerder(beheerder.getEmail()).isPresent());
    }

    @Test
    public void testGeefObservableListBeheerdersZonderHoofdBeheerdersGeefCorrectResult() {
        ObservableList<Gebruiker> result = beheerderCat.geefObservableListBeheerdersZonderHoofdBeheerders();
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(beheerder, result.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVoegToeAlsBeheerderMoetEenLectorZijn() {
        Gebruiker student = new Gebruiker("student Voornaam",
                "student Naam",
                "voornaam.naam@student.com",
                false, false, false);
        beheerderCat.voegToeAlsBeheerder(student);
    }

    @Test
    public void testVoegToeAlsBeheerderZalDeBeheerderToevoegen() {
        Gebruiker lector = new Gebruiker("lector Voornaam",
                "lector Naam",
                "voornaam.naam@lector.com",
                false, false, true);
        beheerderCat.voegToeAlsBeheerder(lector);
        Assert.assertEquals(lector, beheerderCat.getBeheerder(lector.getEmail()).get());
    }
}
