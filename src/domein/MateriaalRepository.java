package domein;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import shared.MateriaalView;
import util.JPAUtil;

public class MateriaalRepository {

    private List<Materiaal> materialen;
    private FirmaRepository firmas;
    private EntityManager em;

    public MateriaalRepository() {
        firmas = new FirmaRepository();
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadMaterialen();
    }

    private void loadMaterialen() {
        Query q = em.createQuery("SELECT m FROM Materiaal m");
        materialen = (List<Materiaal>) q.getResultList();
        System.out.println("test" + materialen.toString());

    }

    public void materialenToevoegenInBulk(ArrayList<Materiaal> materialen) {
        materialen.stream().forEach((materiaal) -> {
            this.materialen.add(materiaal);
        });

    }

    public void voegMateriaalToe(MateriaalView mv) {

        Materiaal materiaal = new Materiaal(mv.getNaam(), mv.getAantal());

        //check of opgegeven firma al bestaat in db
        Firma firma = firmas.geefFirma(mv.getFirma());
        if (firma == null) {
            firma = firmas.voegFirmaToe(mv.getFirma(), mv.getEmailFirma());
        }

        materiaal.setFoto(mv.getFotoUrl()).setBeschrijving(mv.getOmschrijving()).setArtikelnummer(mv.getArtikelNummer())
                .setPrijs(mv.getPrijs()).setAantalOnbeschikbaar(mv.getAantalOnbeschikbaar()).setUitleenbaarheid(mv.isUitleenbaarheid())
                .setPlaats(mv.getPlaats()).setFirma(firma);

        //TODO DOELGROEPEN, LEERGEBIEDEN
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
