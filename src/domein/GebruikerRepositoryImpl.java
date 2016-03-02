package domein;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.jasypt.util.password.StrongPasswordEncryptor;
import util.JPAUtil;

public class GebruikerRepositoryImpl implements GebruikerRepository {

    private List<Gebruiker> gebruikers;
    private StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
    private EntityManager em;

    public GebruikerRepositoryImpl() {
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();;
        loadGebruikers();
    }

    @Override
    public Optional<Gebruiker> getGebruiker(String email, String userPass) {
        if (email == null || userPass == null) {
            return Optional.empty(); // zal geen gebruikers hebben zonder email of wachtwoord
        }
        Optional<Gebruiker> gebruiker = gebruikers.stream()
                .filter(g -> g.getEmail().equalsIgnoreCase(email) && passwordEncryptor.checkPassword(userPass, g.getPaswoord()))
                .findAny();
        return gebruiker;
    }

    private void loadGebruikers() {
        Query q = em.createQuery("SELECT g FROM Gebruiker g");
        gebruikers = (List<Gebruiker>) q.getResultList();
    }
    
    public void stelAanAlsBeheerder(Gebruiker gebruiker){        
        if ( ! gebruiker.isLector()) {
            throw new IllegalArgumentException("De gebruiker moet een lector zijn.");
        }
        
        em.getTransaction().begin();
        gebruiker.setBeheerder(true);
        em.getTransaction().commit();
    }
    
    public void verwijderBeheerder(Gebruiker gebruiker){       
        em.getTransaction().begin();
        gebruiker.setBeheerder(false);
        em.getTransaction().commit();
        
        gebruikers.remove(gebruiker);
    }

    @Override
    public ObservableList<Gebruiker> geefAlleBeheerders() {
        return FXCollections.unmodifiableObservableList(
                FXCollections.observableArrayList(gebruikers.stream().filter(Gebruiker::isBeheerder).collect(Collectors.toList())
        ));
    }

    @Override
    public Gebruiker geefGebruiker(String email) {
        if (email == null || email.isEmpty())
            throw new IllegalArgumentException("Een e-mailadres is vereist.");
        
        Gebruiker gebruiker = null;
        
        for (Gebruiker g : gebruikers) {
            if (g.getEmail().equalsIgnoreCase(email)) {
                gebruiker = g;
                break;
            }  
        }
        
        if (gebruiker == null)
            throw new IllegalArgumentException("De gebruiker kon niet worden gevonden.");
        
        return gebruiker;
    }
}
