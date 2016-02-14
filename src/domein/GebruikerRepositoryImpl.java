package domein;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.jasypt.util.password.StrongPasswordEncryptor;
import util.JPAUtil;

public class GebruikerRepositoryImpl implements GebruikerRepository {
    
    private List<Gebruiker> gebruikers;
    private StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
    
    public GebruikerRepositoryImpl(){
        loadGebruikers();
    }
    
    @Override
    public Optional<Gebruiker> getGebruiker(String email, String userPass){
        if (email == null || userPass == null){
            return null; // zal geen gebruikers hebben zonder email of wachtwoord
        }
        Optional<Gebruiker> gebruiker = gebruikers.stream()
                .filter(g -> g.getEmail().equalsIgnoreCase(email) && passwordEncryptor.checkPassword(userPass, g.getPaswoord()))
                .findAny();
        return gebruiker;
    }
    
    private void loadGebruikers(){
        // dit zou ook van de domeincontroller kunnen meegegeven worden als parameter, mss beter?
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        Query q = em.createQuery("SELECT g FROM Gebruiker g");
        gebruikers = (List<Gebruiker>)q.getResultList();
        em.close();
        System.out.println("GEBRUIKERS: "+gebruikers.toString());
    }
    
}
