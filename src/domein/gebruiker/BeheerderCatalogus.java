package domein.gebruiker;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BeheerderCatalogus {

    private List<Gebruiker> beheerders;

    public BeheerderCatalogus(List<Gebruiker> beheerders) {
        this.beheerders = beheerders;
    }
    
    /**
     * Geeft de optionele beheerder met het e-maiadres en paswoord.
     * 
     * @param email het e-mailadres
     * @return Leeg indien geen beheerder gevonden.
     */
    public Optional<Gebruiker> getBeheerder(String email) {
        if (email == null) {
            return Optional.empty(); // zal geen gebruikers hebben zonder email of wachtwoord
        }

        Optional<Gebruiker> gebruiker = beheerders.stream()
                .filter(g -> g.getEmail().equalsIgnoreCase(email))
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
     * Geeft een lijst met alleen de beheerders. Zonder de hoofdbeheerder.
     * 
     * @return de lijst
     */
    public ObservableList<Gebruiker> geefObservableListBeheerdersZonderHoofdBeheerders() {
        return FXCollections.unmodifiableObservableList(
                FXCollections.observableArrayList(beheerders.stream().filter(g -> g.isBeheerder()).collect(Collectors.toList()))
        );
    }

    /**
     * Zal de gegeven gebruiker toevoegen als beheerder indien het een lector is.
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
