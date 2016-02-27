/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import com.sun.javafx.scene.control.skin.VirtualFlow;
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
        materialen = new ArrayList<>();
    }

    public void loadMaterialen(List<Materiaal> materialen) {
        this.materialen = materialen;
    }

    public Materiaal voegMateriaalToe(MateriaalView mv) {

        String naam = mv.getNaam();
        int aantal = mv.getAantal();
        String firmaNaam = mv.getFirma();
        String firmaEmail = mv.getEmailFirma();
        String fotoUrl = mv.getFotoUrl();
        String beschrijving = mv.getOmschrijving();
        String artikelnummer = mv.getArtikelNummer();
        double prijs = mv.getPrijs();
        int aantalOnbeschikbaar = mv.getAantalOnbeschikbaar();
        boolean uitleenbaarheid = mv.isUitleenbaarheid();
        String plaats = mv.getPlaats();
        String doelgroepen = mv.getDoelgroepen();
        String leergebieden = mv.getLeergebieden();

        System.out.println(prijs);
        
        //Exceptions werpen
        validatieMateriaalView(naam, aantal, firmaEmail, prijs, aantalOnbeschikbaar);

        Materiaal materiaal = new Materiaal(naam, aantal);

        //check of opgegeven firma al bestaat in db
        //indien niet: maak meteen aan met naam en email en steek in database
        Firma firma = firmaRepo.geefFirma(firmaNaam);
        if (firma == null) {
            firma = firmaRepo.voegFirmaToe(firmaNaam, firmaEmail);
        }

        //maak materiaal aan met gegevens uit de MateriaalView
        materiaal.setFoto(fotoUrl).setBeschrijving(beschrijving).setArtikelnummer(artikelnummer)
                .setPrijs(prijs).setAantalOnbeschikbaar(aantalOnbeschikbaar).setUitleenbaarheid(uitleenbaarheid)
                .setPlaats(plaats).setDoelgroepen(doelgroepen).setFirma(firma)
                .setLeergebieden(leergebieden);

        //voeg materiaal toe aan repo
        materialen.add(materiaal);

        return (materiaal);
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

    /**
     * Retourneer een materiaal object gebaseerd op de meegegeven naam.
     * 
     * @param materiaalNaam
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
    
    /**
     * Retourneer een materiaal object gebaseerd op de meegegeven id.
     * 
     * @param id
     * @return 
     */
    public Materiaal geefMateriaalMetId(long id) {
        Materiaal materiaal = null;

        for (Materiaal m : materialen) {
            if (m.getId() == id) {
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

    public MateriaalView convertMateriaalToMateriaalView(Materiaal m) {
        MateriaalView mv = new MateriaalView(m.getNaam(), m.getAantal());
        mv.setFotoUrl(m.getFoto())
                .setOmschrijving(m.getBeschrijving())
                .setArtikelNummer(m.getArtikelnummer())
                .setAantalOnbeschikbaar(m.getAantalOnbeschikbaar())
                .setUitleenbaarheid(m.isUitleenbaarheid())
                .setPlaats(m.getPlaats())
                .setFirma(m.getFirma().getNaam())
                .setEmailFirma(m.getFirma().getEmail())
                .setDoelgroepen(m.getDoelgroepen())
                .setLeergebieden(m.getLeergebieden())
                .setPrijs(m.getPrijs())
                .setId(Long.max(m.getId(), 0));
        
        return mv;
    }
    
    public void wijsAttributenMateriaalViewToeAanMateriaal(MateriaalView mv, Materiaal materiaal) {
        
        String naam = mv.getNaam();
        int aantal = mv.getAantal();
        String firmaEmail = mv.getEmailFirma();
        String firmanaam = mv.getFirma();
        double prijs = mv.getPrijs();
        int aantalOnbeschikbaar = mv.getAantalOnbeschikbaar();
        
        // Valideer de gegevens
        validatieMateriaalView(naam, aantal, firmaEmail, prijs, aantalOnbeschikbaar);
        
        Firma firma = firmaRepo.geefFirma(firmanaam);
        
        if (firma == null) {
            firma = firmaRepo.voegFirmaToe(firmanaam, firmaEmail);
        }
        
        materiaal.setAantal(mv.getAantal())
                .setAantalOnbeschikbaar(mv.getAantalOnbeschikbaar())
                .setArtikelnummer(mv.getArtikelNummer())
                .setBeschrijving(mv.getOmschrijving())
                .setDoelgroepen(mv.getDoelgroepen())
                .setFirma(firmaRepo.geefFirma(mv.getFirma()))
                .setFoto(mv.getFotoUrl())
                .setLeergebieden(mv.getLeergebieden())
                .setNaam(mv.getNaam())
                .setPlaats(mv.getPlaats())
                .setPrijs(mv.getPrijs())
                .setUitleenbaarheid(mv.isUitleenbaarheid());
    }
    
    public void validatieMateriaalView(String naam, int aantal, String firmaEmail, double prijs, int aantalOnbeschikbaar) {
        //Exceptions werpen
        if (naam == null || naam.isEmpty()) {
            throw new IllegalArgumentException("naam");
        }

        if (aantal < 0) {
            throw new IllegalArgumentException("aantal");
        }

        if (firmaEmail != null && !firmaEmail.isEmpty() && !firmaEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new IllegalArgumentException("emailFirma");
        }

        if (prijs < 0) {
            throw new IllegalArgumentException("prijs");
        }

        if (aantalOnbeschikbaar < 0) {
            throw new IllegalArgumentException("onbeschikbaar");
        }
    }
}
