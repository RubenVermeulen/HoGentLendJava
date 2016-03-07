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
    
    /**
     * Wijzigt het firma object van de firma repository en wijzigt 
     * alle materialen hun firma object zodat deze overeen komen met 
     * de nieuwe waarden.
     * 
     * @param firma Het firma object dat zal worden gewijzigd
     * @param nieuweNaam De nieuwe naam voor de firma
     * @param nieuwEmailadres Het nieuwe e-mailadres voor de firma
     * @param materialen Alle materialen
     */
    public void wijzigFirmas(Firma firma, String nieuweNaam, String nieuwEmailadres, List<Materiaal> materialen) {
        // Wijzigt voor elk materiaal object de firma naam en eventueel het firme e-mailadres.
        for (Materiaal m : materialen) {
            if (m.getFirma().getNaam().equals(firma.getNaam())) {
                m.getFirma().setNaam(nieuweNaam);
                m.getFirma().setEmail(nieuwEmailadres);
            }    
        }
        
        // Firma object komt van firma repository,
        // deze wijzigigen moeten gebeuren om dat dan correct
        // weg te schrijven naar de database.
        firma.setNaam(nieuweNaam);
        firma.setEmail(nieuwEmailadres);
        
        persisteerFirmas();
    }
}
