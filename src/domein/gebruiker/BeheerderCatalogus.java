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

    public Optional<Gebruiker> getBeheerder(String email, String userPass) {
        if (email == null || userPass == null) {
            return Optional.empty(); // zal geen gebruikers hebben zonder email of wachtwoord
        }
        Optional<Gebruiker> gebruiker = beheerders.stream()
                .filter(g -> g.getEmail().equalsIgnoreCase(email) && passwordEncryptor.checkPassword(userPass, g.getPaswoord()))
                .findAny();
        return gebruiker;
    }

    public void verwijderBeheerder(Gebruiker gebruiker) {
        gebruiker.setBeheerder(false);
        beheerders.remove(gebruiker);
    }

    public ObservableList<Gebruiker> geefObservableListBeheerdersZonderHoofdBeheerders() {
        return FXCollections.unmodifiableObservableList(
                FXCollections.observableArrayList(beheerders.stream().filter(g -> g.isBeheerder()).collect(Collectors.toList()))
        );
    }

}
