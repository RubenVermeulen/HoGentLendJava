package domein;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import org.jasypt.util.password.StrongPasswordEncryptor;
import util.JPAUtil;

public class GebruikerRepository {
    
    private List<Gebruiker> gebruikers;
    private StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
    
    public GebruikerRepository(){
        loadGebruikers();
    }
    
    public Optional<Gebruiker> getGebruiker(String email, String userPass){
        if (email == null || email.trim().isEmpty()){
            throw new IllegalArgumentException("Een email adres is verplicht op te geven.");
        }
        if (userPass == null || userPass.trim().isEmpty()){
            throw new IllegalArgumentException("Een paswoord is verplicht op te geven.");
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
