package domein;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import util.JPAUtil;

public class DomeinController {

    private GebruikerRepository gebruikerRepo;
    private Gebruiker aangemelde;
    
    public DomeinController(){
        gebruikerRepo = new GebruikerRepository();
    }
    
    /**
     * 
     * @param email gebruiker email
     * @param wachtwoord ongeencrypteerd wachtwoord
     * @return true indien de gebruiker nu is aangemeld, false indien het email en/of wachtwoord verkeerd waren.
     */
    public boolean meldAan(String email, String wachtwoord) {
        Optional<Gebruiker> optGeb = gebruikerRepo.getGebruiker(email, wachtwoord);
        if (optGeb.isPresent()){
            aangemelde = optGeb.get();
            return true;
        }else{
            return false;
        }
    }
    
    
    
    
    
    
    
}
