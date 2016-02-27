package domein;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import shared.MateriaalView;
import shared.ReadCVS;
import util.JPAUtil;

public class MateriaalRepository {

    private MateriaalCatalogus materiaalCatalogus;
    private EntityManager em;

    public MateriaalRepository() {
        materiaalCatalogus = new MateriaalCatalogus();
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadMaterialen();
    }

    private void loadMaterialen() {
        Query q = em.createQuery("SELECT m FROM Materiaal m");
        materiaalCatalogus.loadMaterialen((List<Materiaal>) q.getResultList());
    }

    public void voegMateriaalToe(MateriaalView mv) {

        Materiaal materiaal = materiaalCatalogus.voegMateriaalToe(mv);

        //voeg materiaal toe aan db
        em.getTransaction().begin();
        em.persist(materiaal);
        em.getTransaction().commit();

    }

    void voegMaterialenToeInBulk(String csvFile) {
        ArrayList<String[]> materialen;
        ReadCVS obj = new ReadCVS();

        materialen = obj.run(csvFile);

        for (String[] materiaal : materialen) {
            String naam = materiaal[0];

            String fotoUrl=materiaal[1];
            String omschrijving=materiaal[2];
            String artikelNummer=materiaal[3];
            double prijs=Double.parseDouble(materiaal[12]);
            int aantal=Integer.parseInt(materiaal[11]);
            int aantalOnbeschikbaar=Integer.parseInt(materiaal[4]);
            boolean uitleenbaarheid=Boolean.parseBoolean(materiaal[5]);
            String plaats=materiaal[6];
            String firma=materiaal[7];
            String emailFirma=materiaal[8];
            String doelgroepen=materiaal[9];
            String leergebieden=materiaal[10];
            
            MateriaalView matView=new MateriaalView(naam, aantal);
            
            matView.setAantalOnbeschikbaar(aantalOnbeschikbaar);
            matView.setArtikelNummer(artikelNummer);
            matView.setDoelgroepen(doelgroepen);
            matView.setEmailFirma(emailFirma);
            matView.setFirma(firma);
            matView.setFotoUrl(fotoUrl);
            matView.setLeergebieden(leergebieden);
            matView.setOmschrijving(omschrijving);
            matView.setPlaats(plaats);
            matView.setPrijs(prijs);
            matView.setUitleenbaarheid(uitleenbaarheid);
            
            voegMateriaalToe(matView);

        }

    }

    public List<MateriaalView> geefAlleMaterialen() {

       return materiaalCatalogus.geefAlleMaterialen();

    }

    /**
     * Retourneert een boolean die aangeeft of het materiaal verwijderd is of
     * niet.
     *
     * @param materiaal
     * @return
     */
    public boolean verwijderMateriaal(String materiaalNaam) {
        Materiaal materiaal = this.geefMateriaal(materiaalNaam);

        if (materiaal != null) {
            em.getTransaction().begin();

            try {
                em.remove(materiaal);
                em.getTransaction().commit();

                materiaalCatalogus.verwijderMateriaal(materiaal);

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
        
        return materiaalCatalogus.geefMateriaal(materiaalNaam);
        
    }
    
    /**
     * Retourneert materiaal uit de database die hoort bij de meegegeven id.
     *
     * @param id
     * @return
     */
    public Materiaal geefMateriaalMetId(long id) {
        
        return materiaalCatalogus.geefMateriaalMetId(id);
        
    }
    
    public MateriaalView geefMateriaalView (String materiaalNaam) {
        Materiaal materiaal = geefMateriaal(materiaalNaam);
        MateriaalView materiaalView = null;
        
        if (materiaal == null)
            return materiaalView;
        
        materiaalView = materiaalCatalogus.convertMateriaalToMateriaalView(materiaal);
        
        return materiaalView;
    }

    public List<MateriaalView> geefMaterialenMetFilter(String filter) {
        
        return materiaalCatalogus.geefMaterialenMetFilter(filter);
        
    }

    public boolean wijzigMateriaal(MateriaalView materiaalView) {
        
        Materiaal materiaal = materiaalCatalogus.geefMateriaalMetId(materiaalView.getId());
        
        if (materiaal == null)
            return false;
        
        em.getTransaction().begin();
        
        materiaalCatalogus.wijsAttributenMateriaalViewToeAanMateriaal(materiaalView, materiaal);
        
        em.getTransaction().commit();
        
        return true;
    }
}
