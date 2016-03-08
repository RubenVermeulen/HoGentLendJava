package domein.groep;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import util.JPAUtil;

public class GroepRepository {

    private GroepCatalogus groepCat;
    private EntityManager em;

    public GroepRepository() {
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadGroepCatalogus();
    }

    private void loadGroepCatalogus() {
        Query q = em.createQuery("SELECT g FROM Groep g");
        List<Groep> groepen = (List<Groep>) q.getResultList();
        groepCat = new GroepCatalogus(groepen);
    }

    public List<Groep> getLeerGebieden() {
        return groepCat.getLeerGebieden();
    }

    public List<Groep> getDoelGroepen() {
        return groepCat.getDoelGroepen();
    }

    public List<Groep> convertStringListToLeerGebiedenList(List<String> leerGebiedenStr) {
        return groepCat.convertStringListToGroepList(leerGebiedenStr, true);
    }

    public List<Groep> geefDoelgroep(List<String> doelGroepenStr) {
        return groepCat.convertStringListToGroepList(doelGroepenStr, false);
    }

    public Groep voegGroepToe(String groepNaam, boolean isLeerGebied) {
        Groep groep = groepCat.voegGroepToe(groepNaam, isLeerGebied);

        em.getTransaction().begin();
        em.persist(groep);
        em.getTransaction().commit();

        return groep;
    }

    public Optional<Groep> geefGroep(String groepStr, boolean isLeerGebied) {
        return groepCat.geefGroep(groepStr, isLeerGebied);
    }

    public void verwijderGroep(Groep groep) {
        groepCat.verwijderGroep(groep);

        em.getTransaction().begin();
        em.remove(groep);
        em.getTransaction().commit();
    }
}
