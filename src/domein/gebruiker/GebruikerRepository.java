package domein.gebruiker;

import java.util.List;
import java.util.Optional;
import javafx.collections.ObservableList;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import util.JPAUtil;

public class GebruikerRepository {

    private BeheerderCatalogus beheerderCatalogus;
    private EntityManager em;

    public GebruikerRepository() {
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
    }

    /**
     * Mag enkel gebruikt worden om te testen.
     * 
     * @param gebruikers 
     */
    public GebruikerRepository(List<Gebruiker> gebruikers){
        beheerderCatalogus= new BeheerderCatalogus(gebruikers);
    }
    
    /**
     * Initiliseert alle beheerder in een catalogus.
     */
    private void loadBeheerderCatalogus() {
        Query q = em.createQuery("SELECT g FROM Gebruiker g WHERE g.beheerder = true OR g.hoofdbeheerder = true");
        List<Gebruiker> beheerders = (List<Gebruiker>) q.getResultList();

        this.beheerderCatalogus = new BeheerderCatalogus(beheerders);
    }
    
    /**
     * Retourneert een beheerder als gebruiker object.
     * 
     * @param email
     * @return 
     */
    public Optional<Gebruiker> getBeheerder(String email) {
        return beheerderCatalogus.getBeheerder(email);
    }

    /**
     * Verwijdert beheerdersrechten van de gebruiker.
     * 
     * @param gebruiker 
     */
    public void verwijderBeheerder(Gebruiker gebruiker) {
        em.getTransaction().begin();
        beheerderCatalogus.verwijderBeheerder(gebruiker);
        em.getTransaction().commit();
    }

    public ObservableList<Gebruiker> geefObservableListBeheerdersZonderHoofdBeheerders() {
        return beheerderCatalogus.geefObservableListBeheerdersZonderHoofdBeheerders();
    }

    /**
     * Geeft beheerdersrechten aan de gebruiker.
     * 
     * @param gebruiker 
     */
    public void stelAanAlsBeheerder(Gebruiker gebruiker) {
        em.getTransaction().begin();
        beheerderCatalogus.voegToeAlsBeheerder(gebruiker);
        em.getTransaction().commit();
    }

    /**
     * Retourneert een gebruiker.
     * Deze methode contacteert de database zonder tussenkomst
     * van de catalogus.
     * 
     * @param email
     * @return 
     */
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
