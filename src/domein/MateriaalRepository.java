package domein;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import shared.MateriaalView;
import util.JPAUtil;

public class MateriaalRepository {

    private List<Materiaal> materialen;
    private FirmaRepository firmaRepo;
    private EntityManager em;

    public MateriaalRepository() {
        firmaRepo = new FirmaRepository();
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadMaterialen();
    }

    private void loadMaterialen() {
        Query q = em.createQuery("SELECT m FROM Materiaal m");
        materialen = (List<Materiaal>) q.getResultList();
    }

    public void materialenToevoegenInBulk(ArrayList<Materiaal> materialen) {
        materialen.stream().forEach((materiaal) -> {
            this.materialen.add(materiaal);
        });

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
            MateriaalView mv = new MateriaalView(m.getNaam(), m.getAantal());
            mv.setFotoUrl(m.getFoto()).setOmschrijving(m.getBeschrijving()).setArtikelNummer(m.getArtikelnummer())
                    .setAantalOnbeschikbaar(m.getAantalOnbeschikbaar()).setUitleenbaarheid(m.isUitleenbaarheid())
                    .setPlaats(m.getPlaats()).setFirma(m.getFirma().getNaam()).setEmailFirma(m.getFirma().getEmail())
                    .setDoelgroepen(m.getDoelgroepen()).setLeergebieden(m.getLeergebieden());
            materiaalViews.add(mv);

        }

        return materiaalViews;

    }

}
