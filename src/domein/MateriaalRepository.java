package domein;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.lang3.math.NumberUtils;
import shared.MateriaalView;
import util.ReadCSV;
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
        ReadCSV obj = new ReadCSV();

        materialen = obj.run(csvFile);

        System.out.println(materialen);

        for (String[] materiaal : materialen) {
            String naam = materiaal[1];

            String fotoUrl = materiaal[0];

            System.out.println(materiaal[0]);
            String omschrijving = materiaal[2];
            String artikelNummer = materiaal[3];
            double prijs = NumberUtils.toDouble(materiaal[4], 0);

            /*
            if (!materiaal[4].isEmpty()) {
                prijs = Double.parseDouble(materiaal[4]);
            }
             */
            int aantal;

            aantal = NumberUtils.toInt(materiaal[5], 0);
            boolean uitleenbaarheid;
            if (!materiaal[6].isEmpty()) {
                uitleenbaarheid = Boolean.parseBoolean(materiaal[6]);
            } else {
                uitleenbaarheid = true;
            }

            String plaats = materiaal[7];
            String firma = materiaal[8];
            String emailFirma = materiaal[9];
            String doelgroepen = materiaal[10];
            String leergebieden = materiaal[11];

            MateriaalView matView = new MateriaalView(naam, aantal);

            matView.setAantalOnbeschikbaar(0);   //zelf ingevuld
            matView.setArtikelNummer(artikelNummer);

            List<String> doelGroepkes = new ArrayList<String>(Arrays.asList(doelgroepen.split(",")));
            matView.setDoelgroepen(doelGroepkes);

            matView.setEmailFirma(emailFirma);
            matView.setFirma(firma);
            matView.setFotoUrl(fotoUrl);

            // TODO: fix this
            List<String> leerGroepkes = new ArrayList<String>(Arrays.asList(leergebieden.split(",")));
            matView.setLeergebieden(leerGroepkes);

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
     * @param materiaalNaam
     * @return
     * @throws java.lang.IllegalAccessException
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

    public MateriaalView geefMateriaalView(String materiaalNaam) {
        Materiaal materiaal = geefMateriaal(materiaalNaam);
        MateriaalView materiaalView = null;

        if (materiaal == null) {
            return materiaalView;
        }

        materiaalView = materiaalCatalogus.convertMateriaalToMateriaalView(materiaal);

        return materiaalView;
    }

    public List<MateriaalView> geefMaterialenMetFilter(String filter) {

        return materiaalCatalogus.geefMaterialenMetFilter(filter);

    }

    public boolean wijzigMateriaal(MateriaalView materiaalView) {

        Materiaal materiaal = materiaalCatalogus.geefMateriaalMetId(materiaalView.getId());
        System.out.println(materiaalView);

        if (materiaal == null) {
            return false;
        }

        em.getTransaction().begin();

        materiaalCatalogus.wijsAttributenMateriaalViewToeAanMateriaal(materiaalView, materiaal);

        System.out.println(materiaal);

        em.getTransaction().commit();

        return true;
    }

    public List<Groep> geefAlleDoelgroepen() {
        return materiaalCatalogus.geefAlleDoelgroepen();
    }

    public List<Groep> geefAlleLeergebieden() {
        return materiaalCatalogus.geefAlleLeergebieden();
    }

    public void voegGroepToe(String text, boolean isLeerGroep) {
        materiaalCatalogus.voegGroepToe(text, isLeerGroep);
    }

    public void verwijderGroep(String groep, boolean isLeerGroep){
        materiaalCatalogus.verwijderGroep(groep, isLeerGroep);
    }
}
