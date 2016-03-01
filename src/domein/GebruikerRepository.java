package domein;

import java.util.List;
import java.util.Optional;
import javafx.collections.ObservableList;

public interface GebruikerRepository {

    Optional<Gebruiker> getGebruiker(String email, String userPass);
    
    public void stelAanAlsBeheerder(String email);
    
    public void verwijderBeheerder(String email);
    
    public ObservableList<Gebruiker> geefAlleBeheerders();
    
    public Gebruiker geefGebruiker(String email);
}
