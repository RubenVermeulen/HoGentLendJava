package domein.gebruiker;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jasypt.util.password.StrongPasswordEncryptor;

public class BeheerderCatalogus {

    private List<Gebruiker> beheerders;
    private StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

    public BeheerderCatalogus(List<Gebruiker> beheerders) {
        this.beheerders = beheerders;
    }

    /**
     * Geeft de optionele beheerder met het email en paswoord.
     * 
     * @param email de email
     * @param userPass het paswoord
     * @return Leeg indien geen beheerder gevonden.
     */
    public Optional<Gebruiker> getBeheerder(String email, String userPass) {
        if (email == null || userPass == null) {
            return Optional.empty(); // zal geen gebruikers hebben zonder email of wachtwoord
        }
        Optional<Gebruiker> gebruiker = beheerders.stream()
                .filter(g -> g.getEmail().equalsIgnoreCase(email) && passwordEncryptor.checkPassword(userPass, g.getPaswoord()))
                .findAny();
        return gebruiker;
    }

    /**
     * Maakt de gegeven gebruiker geen beheerder meer.
     * 
     * @param gebruiker de gebruiker
     */
    public void verwijderBeheerder(Gebruiker gebruiker) {
        gebruiker.setBeheerder(false);
        beheerders.remove(gebruiker);
    }

    /**
     * Geeft een lijst met alleen de beheeders. Zonder de hoofdbeheerder.
     * 
     * @return de lijst
     */
    public ObservableList<Gebruiker> geefObservableListBeheerdersZonderHoofdBeheerders() {
        return FXCollections.unmodifiableObservableList(
                FXCollections.observableArrayList(beheerders.stream().filter(g -> g.isBeheerder()).collect(Collectors.toList()))
        );
    }

    /**
     * Zal de geven gebruiker toevoegen als beheer indien het een lector is.
     * @param gebruiker de te beheerder te maken lector
     */
    public void voegToeAlsBeheerder(Gebruiker gebruiker) {
        if (!gebruiker.isLector()) {
            throw new IllegalArgumentException("De gebruiker moet een lector zijn.");
        }
        gebruiker.setBeheerder(true);
        beheerders.add(gebruiker);
    }

}
