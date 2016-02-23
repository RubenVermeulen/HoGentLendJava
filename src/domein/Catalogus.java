package domein;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import shared.MateriaalView;
import shared.ReadCVS;
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
        Materiaal materiaal = this.geefMateriaal(materiaalNaam);

        if (materiaal != null) {
            em.getTransaction().begin();

            try {
                em.remove(materiaal);
                em.getTransaction().commit();

                materialen.remove(materiaal);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * Retourneert materiaal uit de database die hoort bij de meegegeven id.
     *
     * @param id
     * @return
     */
    public Materiaal geefMateriaal(String materiaalNaam) {
        Materiaal materiaal = null;

        for (Materiaal m : materialen) {
            if (m.getNaam().equals(materiaalNaam)) {
                materiaal = m;
                break;
            }
        }

        return materiaal;
    }

    public List<MateriaalView> geefMaterialenMetFilter(String filter) {
        if (filter == null || filter.isEmpty()) {
            return geefAlleMaterialen();
        }
        List<MateriaalView> matViews = new ArrayList();
        for (Materiaal mat : materialen) {
            if (mat.containsFilter(filter)) {
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
