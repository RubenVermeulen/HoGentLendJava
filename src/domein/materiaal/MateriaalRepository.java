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
import java.util.Optional;

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

    public void verwijderMateriaal(String materiaalNaam) {
        Materiaal mat = materiaalCat.verwijderMateriaal(materiaalNaam);
        em.getTransaction().begin();
        em.remove(mat);
        em.getTransaction().commit();
    }

    public Optional<Materiaal> geefMateriaal(String materiaalNaam) {
        return materiaalCat.geefMateriaal(materiaalNaam);
    }

    public Optional<Materiaal> geefMateriaalMetId(long id) {
        return materiaalCat.geefMateriaalMetId(id);
    }

    public MateriaalView geefMateriaalView(String materiaalNaam) {
       return materiaalCat.geefMateriaalView(materiaalNaam);
    }

    public List<MateriaalView> geefMaterialenMetFilter(String filter) {
        return materiaalCat.geefMaterialenMetFilter(filter);
    }

    public void wijzigMateriaal(MateriaalView materiaalView) {
        materiaalCat.wijsAttributenMateriaalViewToeAanMateriaal(materiaalView);
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
