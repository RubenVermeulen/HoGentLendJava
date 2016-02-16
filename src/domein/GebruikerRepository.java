package domein;

import java.util.Optional;

public interface GebruikerRepository {

    Optional<Gebruiker> getGebruiker(String email, String userPass);

}
