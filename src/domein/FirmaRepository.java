package domein;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import util.JPAUtil;

public class FirmaRepository {

    private List<Firma> firmas;
    private EntityManager em;

    public FirmaRepository() {
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadFirmas();
    }
    
    private void loadFirmas() {
        Query q = em.createQuery("SELECT f FROM Firma f");
        firmas = (List<Firma>) q.getResultList();
    }

    public List<Firma> getFirmas() {
        return firmas;
    }

    public Firma geefFirma(String naam) {

        for (Firma f : firmas) {
            if (f.getNaam().equals(naam)) {
                return f;
            }
        }

        return null;
    }

    //maakt nieuwe firma aan en returnt hem
    public Firma voegFirmaToe(String naam, String email) {

        Firma firma = new Firma(naam).setEmail(email);
        firmas.add(firma);
        
        em.getTransaction().begin();
        em.persist(firma);
        em.getTransaction().commit();
        
        return firma;

    }

}
