package domein.firma;

import domein.materiaal.Materiaal;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import shared.MateriaalView;
import util.JPAUtil;

public class FirmaRepository {

    private FirmaCatalogus firmaCat;
    private EntityManager em;

    public FirmaRepository() {
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadFirmaCatalogus();
    }
    
    /**
     * Gebruikt voor testen.
     * 
     * @param firmas 
     */
    public FirmaRepository(List<Firma> firmas) {
        firmaCat = new FirmaCatalogus(firmas);
    }

    private void loadFirmaCatalogus() {
        Query q = em.createQuery("SELECT f FROM Firma f ORDER BY f.naam");
        List<Firma> firmas = (List<Firma>) q.getResultList();
        firmaCat = new FirmaCatalogus(firmas);
    }

    public List<Firma> getAllFirmasSorted() {
        return firmaCat.getAllFirmasSorted();
    }

    public Optional<Firma> geefFirma(String naam) {
        return firmaCat.geefFirma(naam);
    }

    public Firma voegFirmaToe(String naam, String email) {
        Firma firma = firmaCat.voegFrimaToe(naam, email);

        em.getTransaction().begin();
        em.persist(firma);
        em.getTransaction().commit();

        return firma;
    }

    public void verwijderFirma(String naam, List<MateriaalView> materialen) {
        Firma firmaToDelete = firmaCat.verwijderFirma(naam, materialen);

        em.getTransaction().begin();
        em.remove(firmaToDelete);
        em.getTransaction().commit();
    }

    public void persisteerFirmas() {
        em.getTransaction().begin();
        em.getTransaction().commit();
    }

    public void wijzigFirmas(Firma firma, String nieuweNaam, String nieuwEmailadres, List<Materiaal> materialen) {
        firmaCat.wijzigFirmas(firma, nieuweNaam, nieuwEmailadres, materialen);
        persisteerFirmas();
    }
}
