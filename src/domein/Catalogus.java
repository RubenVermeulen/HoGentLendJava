package domein;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import shared.MateriaalView;
import util.JPAUtil;

public class Catalogus {

    private List<Materiaal> materialen;
    private FirmaRepository firmaRepo;
    private EntityManager em;

    public Catalogus() {
        firmaRepo = new FirmaRepository();
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadMaterialen();
    }

    private void loadMaterialen() {
        Query q = em.createQuery("SELECT m FROM Materiaal m");
        materialen = (List<Materiaal>) q.getResultList();
    }

    public void voegMateriaalToe(MateriaalView mv) {

        Materiaal materiaal = new Materiaal(mv.getNaam(), mv.getAantal());

        //check of opgegeven firma al bestaat in db
        //indien niet: maak meteen aan met naam en email en steek in database
        Firma firma = firmaRepo.geefFirma(mv.getFirma());
        if (firma == null) {
            firma = firmaRepo.voegFirmaToe(mv.getFirma(), mv.getEmailFirma());
        }

        //maak materiaal aan met gegevens uit de MateriaalView
        materiaal.setFoto(mv.getFotoUrl()).setBeschrijving(mv.getOmschrijving()).setArtikelnummer(mv.getArtikelNummer())
                .setPrijs(mv.getPrijs()).setAantalOnbeschikbaar(mv.getAantalOnbeschikbaar()).setUitleenbaarheid(mv.isUitleenbaarheid())
                .setPlaats(mv.getPlaats()).setDoelgroepen(mv.getDoelgroepen()).setFirma(firma)
                .setLeergebieden(mv.getLeergebieden());

        System.out.println(materialen.toString());
        //voeg materiaal toe aan repo
        materialen.add(materiaal);
        System.out.println(materialen.toString());

        //voeg materiaal toe aan db
        em.getTransaction().begin();
        em.persist(materiaal);
        em.getTransaction().commit();

    }

    public List<MateriaalView> geefAlleMaterialen() {

        List<MateriaalView> materiaalViews = new ArrayList();

        for (Materiaal m : materialen) {
            materiaalViews.add(convertMateriaalToMateriaalView(m));
        }

        return materiaalViews;

    }

    /**
     * Retourneert een boolean die aangeeft of het materiaal verwijderd is of
     * niet.
     *
     * @param materiaal
     * @return
     */
    public boolean verwijderMateriaal(String materiaalNaam) {
        // todo ruben fix this
        /*  em.getTransaction().begin();
        
        try {
            em.remove(materiaal);
            em.getTransaction().commit();
            
            materialen.remove(materiaal);
            
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }*/
        return false;
    }

    /**
     * Retourneert materiaal uit de database die hoort bij de meegegeven id.
     *
     * @param id
     * @return
     */
    public Materiaal geefMateriaal(String materiaalNaam) {
        // todo ruben fix this dat het de string gebruikt idpv de long
        Materiaal materiaal = null;

        em.getTransaction().begin();

        try {
            //      materiaal = (Materiaal) em.find(Materiaal.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return materiaal;
    }

    public List<MateriaalView> geefMaterialenMetFilter(String filter) {
        if (filter == null || filter.isEmpty()){
            return geefAlleMaterialen();
        }
        List<MateriaalView> matViews = new ArrayList();
        for (Materiaal mat : materialen) {
            if (mat.containsFilter(filter)){
                matViews.add(convertMateriaalToMateriaalView(mat));
            }
        }
        return matViews;
    }

    private MateriaalView convertMateriaalToMateriaalView(Materiaal m) {
        MateriaalView mv = new MateriaalView(m.getNaam(), m.getAantal());
        mv.setFotoUrl(m.getFoto()).setOmschrijving(m.getBeschrijving()).setArtikelNummer(m.getArtikelnummer())
                .setAantalOnbeschikbaar(m.getAantalOnbeschikbaar()).setUitleenbaarheid(m.isUitleenbaarheid())
                .setPlaats(m.getPlaats()).setFirma(m.getFirma().getNaam()).setEmailFirma(m.getFirma().getEmail())
                .setDoelgroepen(m.getDoelgroepen()).setLeergebieden(m.getLeergebieden());
        return mv;
    }
}
