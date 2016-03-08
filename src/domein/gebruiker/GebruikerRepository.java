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
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();;
        loadBeheerderCatalogus();
    }

    public Optional<Gebruiker> getBeheerder(String email, String userPass) {
        return beheerderCat.getBeheerder(email, userPass);
    }

    private void loadBeheerderCatalogus() {
        Query q = em.createQuery("SELECT g FROM Gebruiker g WHERE g.beheerder = true OR g.hoofdbeheerder = true");
        List<Gebruiker> beheerders = (List<Gebruiker>) q.getResultList();
        System.out.println(beheerders);
        this.beheerderCat = new BeheerderCatalogus(beheerders);
    }

    public void stelAanAlsBeheerder(Gebruiker gebruiker) {
        em.getTransaction().begin();
        if (!gebruiker.isLector()) {
            throw new IllegalArgumentException("De gebruiker moet een lector zijn.");
        }
        gebruiker.setBeheerder(true);
        em.getTransaction().commit();
    }

    public void verwijderBeheerder(Gebruiker gebruiker) {
        em.getTransaction().begin();
        beheerderCat.verwijderBeheerder(gebruiker);
        em.getTransaction().commit();

    }

    public ObservableList<Gebruiker> geefObservableListBeheerdersZonderHoofdBeheerders() {
        return beheerderCat.geefObservableListBeheerdersZonderHoofdBeheerders();
    }

    public Optional<Gebruiker> geefGebruikerViaEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Een e-mailadres is vereist.");
        }
        email = email.toLowerCase();
        Query q = em.createQuery("SELECT g FROM Gebruiker g WHERE g.email LIKE :arg0");
        q.setParameter(0, email);
        Optional<Gebruiker> result;
        try {
            result = Optional.of((Gebruiker) q.getSingleResult());
        } catch (NoResultException e) {
            result = Optional.empty();
        }
        return result;
    }

}
