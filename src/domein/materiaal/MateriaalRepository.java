package domein.materiaal;

import domein.groep.Groep;
import domein.firma.FirmaRepository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import shared.MateriaalView;
import util.JPAUtil;
import domein.DomeinController;
import domein.firma.Firma;
import exceptions.BulkToevoegenMisluktException;

public class MateriaalRepository {

    private DomeinController dc;

    private MateriaalCatalogus materiaalCat;
    private EntityManager em;

    public MateriaalRepository(FirmaRepository firmaRepo) {
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadMateriaalCatalogus(firmaRepo);
    }

    public final void loadMateriaalCatalogus(FirmaRepository firmaRepo) {
        Query q = em.createQuery("SELECT m FROM Materiaal m");
        List<Materiaal> materialen = (List<Materiaal>) q.getResultList();
        materiaalCat = new MateriaalCatalogus(materialen, firmaRepo);
    }

    public void voegMateriaalToe(MateriaalView mv) {
        Materiaal materiaal = materiaalCat.voegMateriaalToe(mv);

        //voeg materiaal toe aan db
        em.getTransaction().begin();
        em.persist(materiaal);
        em.getTransaction().commit();

    }

    public void voegMaterialenToeInBulk(String csvFile) throws BulkToevoegenMisluktException {
        BulkToevoeger bulkToevoeger = new BulkToevoeger(this, materiaalCat);
        bulkToevoeger.voegMaterialenToeInBulk(csvFile);
    }

    public List<MateriaalView> geefAlleMaterialen() {
        return materiaalCat.geefAlleMaterialen();

    }

    /**
     * Retourneert een boolean die aangeeft of het materiaal verwijderd is of
     * niet.
     *
     * @param materiaalNaam
     * @return
     */
    public boolean verwijderMateriaal(String materiaalNaam) {
        if (materiaalNaam == null || materiaalNaam.isEmpty()) {
            throw new IllegalArgumentException("De parameter materiaalNaam mag niet leeg of null zijn.");
        }

        Materiaal materiaal = this.geefMateriaal(materiaalNaam);

        if (materiaal != null) {
            em.getTransaction().begin();

            try {
                em.remove(materiaal);
                em.getTransaction().commit();

                materiaalCat.verwijderMateriaal(materiaal);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * Retourneert materiaal uit de database die hoort bij de meegegeven naam.
     *
     * @param id
     * @return
     */
    public Materiaal geefMateriaal(String materiaalNaam) {

        return materiaalCat.geefMateriaal(materiaalNaam);

    }

    /**
     * Retourneert materiaal uit de database die hoort bij de meegegeven id.
     *
     * @param id
     * @return
     */
    public Materiaal geefMateriaalMetId(long id) {

        return materiaalCat.geefMateriaalMetId(id);

    }

    public MateriaalView geefMateriaalView(String materiaalNaam) {
        Materiaal materiaal = geefMateriaal(materiaalNaam);
        MateriaalView materiaalView = null;

        if (materiaal == null) {
            return materiaalView;
        }

        materiaalView = materiaalCat.toMateriaalView(materiaal);

        return materiaalView;
    }

    public List<MateriaalView> geefMaterialenMetFilter(String filter) {

        return materiaalCat.geefMaterialenMetFilter(filter);

    }

    public void wijzigMateriaal(MateriaalView materiaalView) {

        Materiaal materiaal = materiaalCat.geefMateriaalMetId(materiaalView.getId());

        if (materiaal == null) {
            return;
        }

        materiaalCat.wijsAttributenMateriaalViewToeAanMateriaal(materiaalView, materiaal);

        em.getTransaction().begin();
        em.getTransaction().commit();

    }

    public List<Groep> geefAlleDoelgroepen() {
        return materiaalCat.geefAlleDoelgroepen();
    }

    public List<Groep> geefAlleLeergebieden() {
        return materiaalCat.geefAlleLeergebieden();
    }

    public void voegGroepToe(String text, boolean isLeerGroep) {
        materiaalCat.voegGroepToe(text, isLeerGroep);
    }

    public void verwijderGroep(String groepStr, boolean isLeerGroep) {
        materiaalCat.verwijderGroep(groepStr, isLeerGroep);
    }

    public void wijzigFirmas(Firma firma, String nieuweNaam, String nieuwEmailadres) {
        materiaalCat.wijzigFirmas(firma, nieuweNaam, nieuwEmailadres);
    }

    public MateriaalView toMateriaalView(Materiaal materiaal) {
        return materiaalCat.toMateriaalView(materiaal);
    }
}
