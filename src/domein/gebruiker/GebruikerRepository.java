package domein.gebruiker;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.jasypt.util.password.StrongPasswordEncryptor;
import util.JPAUtil;

public class GebruikerRepository {

    private BeheerderCatalogus beheerderCat;
    private EntityManager em;

    public GebruikerRepository() {
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadBeheerderCatalogus();
    }

    
    //voor te testen
    public GebruikerRepository(List<Gebruiker> gebruikers){
    beheerderCat= new BeheerderCatalogus(gebruikers);
    }
    
    private void loadBeheerderCatalogus() {
        Query q = em.createQuery("SELECT g FROM Gebruiker g WHERE g.beheerder = true OR g.hoofdbeheerder = true");
        List<Gebruiker> beheerders = (List<Gebruiker>) q.getResultList();
        System.out.println(beheerders);
        this.beheerderCat = new BeheerderCatalogus(beheerders);
    }
    
    public Optional<Gebruiker> getBeheerder(String email) {
        return beheerderCat.getBeheerder(email);
    }

    public void verwijderBeheerder(Gebruiker gebruiker) {
        em.getTransaction().begin();
        beheerderCat.verwijderBeheerder(gebruiker);
        em.getTransaction().commit();
    }

    public ObservableList<Gebruiker> geefObservableListBeheerdersZonderHoofdBeheerders() {
        return beheerderCat.geefObservableListBeheerdersZonderHoofdBeheerders();
    }

    public void stelAanAlsBeheerder(Gebruiker gebruiker) {
        em.getTransaction().begin();
        beheerderCat.voegToeAlsBeheerder(gebruiker);
        em.getTransaction().commit();
    }

    public Optional<Gebruiker> geefGebruikerViaEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Een e-mailadres is vereist.");
        }
        email = email.toLowerCase();
        Query q = em.createQuery("SELECT g FROM Gebruiker g WHERE g.email = ?1");
        q.setParameter(1, email);
        Optional<Gebruiker> result;
        try {
            result = Optional.of((Gebruiker) q.getSingleResult());
        } catch (NoResultException e) {
            result = Optional.empty();
        }
        return result;
    }

}
