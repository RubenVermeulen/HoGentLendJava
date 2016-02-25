/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.ArrayList;
import java.util.List;
import shared.MateriaalView;

/**
 *
 * @author Xander
 */
public class MateriaalCatalogus {

    private List<Materiaal> materialen;
    private FirmaRepository firmaRepo;

    public MateriaalCatalogus() {
        firmaRepo = new FirmaRepository();
    }

    public void loadMaterialen(List<Materiaal> materialen) {
        this.materialen = materialen;
    }

    public Materiaal voegMateriaalToe(MateriaalView mv) {
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

        //voeg materiaal toe aan repo
        materialen.add(materiaal);
        
        return(materiaal);
    }
    
    public List<MateriaalView> geefAlleMaterialen() {
        List<MateriaalView> materiaalViews = new ArrayList();

        for (Materiaal m : materialen) {
            materiaalViews.add(convertMateriaalToMateriaalView(m));
        }

        return materiaalViews;
    }
    
    public void verwijderMateriaal(Materiaal materiaal) {
        
        materialen.remove(materiaal);
        
    }
    
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
