/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import shared.MateriaalView;

/**
 *
 * @author Xander
 */
public class MateriaalCatalogus {

    private List<Materiaal> materialen;
    private FirmaRepository firmaRepo;
    private GroepRepository groepRepo;

    public MateriaalCatalogus() {
        firmaRepo = new FirmaRepository();
        materialen = new ArrayList<>();
        groepRepo = new GroepRepository();
    }

    public void loadMaterialen(List<Materiaal> materialen) {
        this.materialen = materialen;

        System.out.println(materialen);

    }

    public Materiaal voegMateriaalToe(MateriaalView mv) {
               
        String urlFoto = mv.getFotoUrl();
        String naam = mv.getNaam();
        int aantal = mv.getAantal();
        String firmaNaam = mv.getFirma();
        String firmaEmail = mv.getEmailFirma();
        String beschrijving = mv.getOmschrijving();
        String artikelnummer = mv.getArtikelNummer();
        double prijs = mv.getPrijs();
        int aantalOnbeschikbaar = mv.getAantalOnbeschikbaar();
        boolean uitleenbaarheid = mv.isUitleenbaarheid();
        String plaats = mv.getPlaats();
        List<String> doelgroepenStr = mv.getDoelgroepen();
        List<String> leergebiedenStr = mv.getLeergebieden();

        //Exceptions werpen
        validatieMateriaalView(urlFoto, naam, aantal, firmaEmail, prijs, aantalOnbeschikbaar);

        Materiaal materiaal = new Materiaal(naam, aantal);
            
        // Geeft ofwel een firma object terug of wel de waarde NULL
        Firma firma = firmaRepo.geefFirma(firmaNaam);

        List<Groep> doelGroepen = groepRepo.geefDoelgroep(doelgroepenStr);
        List<Groep> leerGroepen = groepRepo.geefLeergroepen(leergebiedenStr);

        //maak materiaal aan met gegevens uit de MateriaalView
        materiaal.setFoto(urlFoto)
                .setBeschrijving(beschrijving)
                .setArtikelnummer(artikelnummer)
                .setPrijs(prijs)
                .setAantalOnbeschikbaar(aantalOnbeschikbaar)
                .setUitleenbaarheid(uitleenbaarheid)
                .setPlaats(plaats)
                .setDoelgroepen(doelGroepen)
                .setLeergebieden(leerGroepen)
                .setFirma(firma);

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
                .setFirma(m.getFirma() == null ? null : m.getFirma().getNaam())
                .setEmailFirma(m.getFirma() == null ? null : m.getFirma().getEmail())
                .setDoelgroepen(groepListToString(m.getDoelgroepen()))
                .setLeergebieden(groepListToString(m.getLeergebieden()))
                .setPrijs(m.getPrijs())
                .setId(Long.max(m.getId(), 0));

        return mv;
    }

    public void wijsAttributenMateriaalViewToeAanMateriaal(MateriaalView mv, Materiaal materiaal) {

        String urlFoto = mv.getFotoUrl();
        String naam = mv.getNaam();
        int aantal = mv.getAantal();
        String firmanaam = mv.getFirma();
        double prijs = mv.getPrijs();
        int aantalOnbeschikbaar = mv.getAantalOnbeschikbaar();
        List<String> doelgroepenStr = mv.getDoelgroepen();
        List<String> leergebiedenStr = mv.getLeergebieden();

        System.out.println(leergebiedenStr.toString());

        // Valideer de gegevens
        validatieMateriaalView(urlFoto, naam, aantal, null, prijs, aantalOnbeschikbaar);
        
        Firma firma = firmaRepo.geefFirma(firmanaam);
        
        List<Groep> doelGroepen = groepRepo.geefDoelgroep(doelgroepenStr);
        List<Groep> leerGroepen = groepRepo.geefLeergroepen(leergebiedenStr);
        
        materiaal.setAantal(mv.getAantal())
                .setAantalOnbeschikbaar(mv.getAantalOnbeschikbaar())
                .setArtikelnummer(mv.getArtikelNummer())
                .setBeschrijving(mv.getOmschrijving())
                .setDoelgroepen(doelGroepen)
                .setFoto(urlFoto)
                .setLeergebieden(leerGroepen)
                .setNaam(mv.getNaam())
                .setPlaats(mv.getPlaats())
                .setPrijs(mv.getPrijs())
                .setUitleenbaarheid(mv.isUitleenbaarheid())
                .setFirma(firma);
    }

    public void validatieMateriaalView(String urlFoto, String naam, int aantal, String firmaEmail, double prijs, int aantalOnbeschikbaar) {
        //Exceptions werpen
        if (urlFoto != null && ! urlFoto.isEmpty() && ! urlFoto.endsWith(".jpg") && ! urlFoto.endsWith(".png") &&  ! urlFoto.endsWith(".gif"))
            throw new IllegalArgumentException("foto");
        
        if (naam == null || naam.isEmpty()) {
            throw new IllegalArgumentException("naam");
        }

        if (aantal < 0) {
            throw new IllegalArgumentException("aantal");
        }

        if (prijs < 0) {
            throw new IllegalArgumentException("prijs");
        }

        if (aantalOnbeschikbaar < 0) {
            throw new IllegalArgumentException("onbeschikbaar");
        }
    }

    private List<String> groepListToString(List<Groep> groepen) {
        return groepen.stream().map(g -> g.getGroep()).collect(Collectors.toList());
    }

    public List<Groep> geefAlleLeergebieden() {
        return groepRepo.getLeergebieden();
    }

    public List<Groep> geefAlleDoelgroepen() {
        return groepRepo.getDoelgroepen();
    }

    public void voegGroepToe(String text, boolean isLeergroep) {
      groepRepo.voegGroepToe(text, isLeergroep);
    }

    public void verwijderGroep(String groepStr, boolean isLeerGroep){
        if (groepStr == null || groepStr.isEmpty()) {
            throw new IllegalArgumentException("Je hebt geen " + (isLeerGroep ? "leergebied" : "doelgroep") + " geselecteerd.");
        }
        
        Optional<Groep> groepOpt = groepRepo.geefGroep(groepStr, isLeerGroep);
        
        if (!groepOpt.isPresent()) {
            if (isLeerGroep) {
                throw new IllegalArgumentException("Die leergebied bestaat niet.");
            }else{
                throw new IllegalArgumentException("Die doelgroep bestaat niet.");
            }
        }
        
        Groep groep = groepOpt.get();
        
        for(Materiaal m : materialen){
            if (isLeerGroep){
                if (m.getLeergebieden().stream().anyMatch(g -> g.getId() == groep.getId())){
                    throw new IllegalArgumentException("Er is nog een materiaal met dit leergebied.");
                }
            }else{
                if (m.getDoelgroepen().stream().anyMatch(g -> g.getId() == groep.getId())){
                    throw new IllegalArgumentException("Er is nog een materiaal met deze doelgroep.");
                }
            }
        }
        groepRepo.verwijderGroep(groep);
    }
}
