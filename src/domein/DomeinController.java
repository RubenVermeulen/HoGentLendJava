package domein;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import shared.MateriaalView;

public class DomeinController {

    private GebruikerRepository gebruikerRepo;
    private MateriaalRepository materiaalRepo;
    private Gebruiker aangemelde;

    public DomeinController() {
        this(new GebruikerRepositoryImpl());
    }

    public DomeinController(GebruikerRepository gebruikerRepo) {
        this.gebruikerRepo = gebruikerRepo;
    }

    /**
     *
     * @param email gebruiker email
     * @param wachtwoord ongeencrypteerd wachtwoord
     * @return true indien de gebruiker nu is aangemeld, false indien het email
     * en/of wachtwoord verkeerd waren.
     */
    public boolean meldAan(String email, String wachtwoord) {
        Optional<Gebruiker> optGeb = gebruikerRepo.getGebruiker(email, wachtwoord);
        if (optGeb.isPresent()) {
            aangemelde = optGeb.get();
            return true;
        } else {
            aangemelde = null;
            return false;
        }
    }

    /**
     * Meld de gebruiker af.
     */
    public void meldAf() {
        aangemelde = null;
    }

    /**
     * Geeft de gegevens van de aangemelde gebruiker. Indien geen aangemelde
     * gebruiker, geeft arrays van lege strings.
     *
     * @return String[3] - voornaam, achternaam, email
     */
    public String[] geefGegevensAangemeldeGebruiker() {
        if (aangemelde != null) {
            return new String[]{aangemelde.getVoornaam(), aangemelde.getAchternaam(), aangemelde.getEmail()};
        } else {
            return new String[]{"", "", ""};
        }
    }

    /**
     * TODO xander schrijf dit
     * @param mv 
     */
    public void voegMateriaalToe(MateriaalView mv) {
        materiaalRepo.voegMateriaalToe(mv);

    }

    /**
     * TODO Xander schrijf dit
     * @return 
     */
    public List<MateriaalView> geefAlleMaterialen() {
        if (materiaalRepo == null) {
            this.materiaalRepo = new MateriaalRepository();
        }
        return materiaalRepo.geefAlleMaterialen();
    }

}
