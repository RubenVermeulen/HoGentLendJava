package domein.gebruiker;

import java.util.List;
import java.util.Optional;
import javafx.collections.ObservableList;

public interface GebruikerRepository {

    Optional<Gebruiker> getGebruiker(String email, String userPass);
    
    public void stelAanAlsBeheerder(Gebruiker gebruiker);
    
    public void verwijderBeheerder(Gebruiker gebruiker);
    
    public ObservableList<Gebruiker> geefAlleBeheerders();
    
    public Gebruiker geefGebruikerViaEmail(String email);
    
    public Gebruiker geefGebruikerViaNaam(String naam);
}