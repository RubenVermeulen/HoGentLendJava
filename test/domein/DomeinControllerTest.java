package domein;

import domein.gebruiker.Gebruiker;
import domein.gebruiker.GebruikerRepository;
import java.util.Optional;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

public class DomeinControllerTest {

    private final String CORRECT_VOORNAAM = "Test";
    private final String CORRECT_ACHTERNAAM = "Email";
    private final String CORRECT_EMAIL = "testemail@test.com";
    private final String CORRECT_PASSWORD = "rubsup";
    private final String FOUT_EMAIL = "emailtest@test.com";
    private final String FOUT_PASSWORD = "blahblah";
    private final boolean HOOFDBEEHERDER = true;
    private final boolean BEHEERDER = true;
    private final boolean LECTOR = true;

    private DomeinController domCon;

    @Mock
    private GebruikerRepository dummyGebruikerRepo;

    @Before
    public void before() {
        // GebruikerRepository dummy training
        dummyGebruikerRepo = mock(GebruikerRepository.class, "dummyGebruikerRepo");
        when(dummyGebruikerRepo.getBeheerder(anyString()))
                .thenReturn(Optional.empty());
        when(dummyGebruikerRepo.getBeheerder(CORRECT_EMAIL))
                .thenReturn(Optional.of(new Gebruiker(CORRECT_VOORNAAM, CORRECT_ACHTERNAAM, CORRECT_EMAIL, HOOFDBEEHERDER, BEHEERDER, LECTOR)));

        domCon = new DomeinController(dummyGebruikerRepo);
    }

    @Test
    public void testMeldAanGeeftFalseMetOngeldigEmailOngeldigWachtwoord() {
        assertFalse(domCon.meldAan(FOUT_EMAIL, FOUT_PASSWORD));
    }

    @Test
    public void testMeldAanGeeftFalseMetOngeldigEmail() {
        assertFalse(domCon.meldAan(FOUT_EMAIL, CORRECT_PASSWORD));
    }

    @Test
    public void testMeldAanGeeftFalseMetOngeldigWachtwoord() {
        assertFalse(domCon.meldAan(CORRECT_EMAIL, FOUT_PASSWORD));
    }

    @Test
    public void testMeldAanGeefTrueMetGeldigEmailEnWachtwoord() {
        assertTrue(domCon.meldAan(CORRECT_EMAIL, CORRECT_PASSWORD));
    }

    @Test
    public void testGeefGegevensAangemeldeGebruikerIsCorrectNaGeldigMeldAan() {
        assertTrue(domCon.meldAan(CORRECT_EMAIL, CORRECT_PASSWORD));
        checkStringArrayAsserts(domCon.geefGegevensAangemeldeGebruiker(),
                CORRECT_VOORNAAM, CORRECT_ACHTERNAAM, CORRECT_EMAIL);
    }

    @Test
    public void testGeefGegevensAangemeldeGebruikerIsLeegNaOngeldigMeldAan() {
        assertFalse(domCon.meldAan(FOUT_EMAIL, FOUT_PASSWORD));
        checkStringArrayAsserts(domCon.geefGegevensAangemeldeGebruiker(),
                "", "", "");
    }

    @Test
    public void testGeefGegevensAangemeldeGebruikerIsLeegNaGeldigMeldAanEnMeldAf() {
        assertTrue(domCon.meldAan(CORRECT_EMAIL, CORRECT_PASSWORD));
        domCon.meldAf();
        checkStringArrayAsserts(domCon.geefGegevensAangemeldeGebruiker(),
                "", "", "");
    }

    private void checkStringArrayAsserts(String[] actuals, String... expecteds) {
        for (int i = 0; i < expecteds.length; i++) {
            assertEquals(expecteds[i], actuals[i]);
        }
    }
}
