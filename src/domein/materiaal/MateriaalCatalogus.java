package domein.materiaal;

import domein.groep.GroepRepository;
import domein.groep.Groep;
import domein.firma.FirmaRepository;
import domein.firma.Firma;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import shared.MateriaalView;
import util.ImageUtil;

public class MateriaalCatalogus {

    private List<Materiaal> materialen;
    private FirmaRepository firmaRepo;
    private GroepRepository groepRepo;

    public MateriaalCatalogus(List<Materiaal> materialen, FirmaRepository firmaRepo) {
        this.materialen = materialen;
        this.firmaRepo = firmaRepo;
        groepRepo = new GroepRepository();
    }
    
    /**
     * Enkel gebruikt voor testklasse.
     * 
     * @param materialen
     * @param firmaRepo
     * @param groepRepo 
     */
    public MateriaalCatalogus(List<Materiaal> materialen, FirmaRepository firmaRepo, GroepRepository groepRepo) {
        this.materialen = materialen;
        this.firmaRepo = firmaRepo;
        this.groepRepo = groepRepo;
    }

    /**
     * Retourneer een materiaal object gebaseerd op de meegegeven id.
     *
     * @param id Id van de het materiaal
     * @return Materiaal object
     */
    public Optional<Materiaal> geefMateriaalMetId(long id) {
        for (Materiaal m : materialen) {
            if (m.getId() == id) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

    /**
     * Retourneer een materiaal object gebaseerd op de meegegeven naam.
     *
     * @param materiaalNaam naam van het materiaal
     * @return het optioneel materiaal, leeg indien geen gevonden was met de
     * gegeven naam
     */
    public Optional<Materiaal> geefMateriaal(String materiaalNaam) {
        for (Materiaal m : materialen) {
            if (m.getNaam().equals(materiaalNaam)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

    /**
     * Maak een nieuw materiaal met de gegevens uit het gegeven materiaalview.
     *
     * @param mv het materiaalview met de gegevens voor het nieuwe materiaal
     * @return het nieuwe materiaal
     * @throws IllegalArgumentException indien de gegevens in materiaalview niet
     * geldig zijn
     */
    public Materiaal voegMateriaalToe(MateriaalView mv) {
        String newFotoUrl = mv.getNewFotoUrl();
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
        validatieMateriaalView(newFotoUrl, naam, aantal, prijs, aantalOnbeschikbaar);

        if (materialen.stream().anyMatch(m -> m.getNaam().equalsIgnoreCase(naam))){
            throw new IllegalArgumentException("naam");
        }
        
        Materiaal materiaal = new Materiaal(naam, aantal);

        Optional<Firma> firmaOpt = firmaRepo.geefFirma(firmaNaam);
        Firma firma = firmaOpt.isPresent() ? firmaOpt.get() : null;

        List<Groep> doelGroepen = groepRepo.geefDoelgroep(doelgroepenStr);
        List<Groep> leerGroepen = groepRepo.convertStringListToLeerGebiedenList(leergebiedenStr);

        //maak materiaal aan met gegevens uit de MateriaalView
        if (newFotoUrl != null && !newFotoUrl.trim().isEmpty()){
            materiaal.setFotoBytes(ImageUtil.imageFileToByteArray(newFotoUrl));
        }
        materiaal.setBeschrijving(beschrijving)
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

    /**
     * Verwijdert het materiaal met de gegeven materiaal naam.
     *
     * @param materiaalNaam de naam van het te verwijderen materiaal
     * @return het verwijderde materiaal
     * @throws IllegalArgumentException indien de naam niet geldig is of geen
     * materiaal bestaat met de naam
     */
    public Materiaal verwijderMateriaal(String materiaalNaam) {
        if (materiaalNaam == null || materiaalNaam.isEmpty()) {
            throw new IllegalArgumentException("De materiaal naam is verplicht.");
        }

        Optional<Materiaal> materiaalOpt = geefMateriaal(materiaalNaam);
        if (!materiaalOpt.isPresent()) {
            throw new IllegalArgumentException("Er is geen materiaal met deze naam.");
        }
        Materiaal mat = materiaalOpt.get();
        materialen.remove(mat);
        return mat;
    }

    /**
     * Retourneert alle materialen in de vorm van een materiaalview
     *
     * @return lijst van materiaalviews
     */
    public List<MateriaalView> geefAlleMaterialenViews() {
        List<MateriaalView> materiaalViews = new ArrayList();
        for (Materiaal m : materialen) {
            materiaalViews.add(toMateriaalView(m));
        }
        Comparator<MateriaalView> comparator = (f1, f2) -> f1.getNaam().compareToIgnoreCase(f2.getNaam());
        Collections.sort(materiaalViews, comparator);
        return materiaalViews;
    }

    /**
     * Geeft alle materialen terug die de filter bevatten als materiaalviews.
     *
     * @param filter de filter
     * @return list van m
     */
    public List<MateriaalView> geefMaterialenMetFilter(String filter) {
        if (filter == null || filter.isEmpty()) {
            return geefAlleMaterialenViews();
        }
        List<MateriaalView> matViews = new ArrayList();
        for (Materiaal mat : materialen) {
            if (mat.containsFilter(filter)) {
                matViews.add(toMateriaalView(mat));
            }
        }
        Comparator<MateriaalView> comparator = (f1, f2) -> f1.getNaam().compareToIgnoreCase(f2.getNaam());
        Collections.sort(matViews, comparator);
        return matViews;
    }

    /**
     * Wijst alle attributen van een materiaal view toe aan een materiaal.
     *
     * @param mv Materiaal view object
     */
    public void wijsAttributenMateriaalViewToeAanMateriaal(MateriaalView mv) {
        Optional<Materiaal> matOpt = geefMateriaalMetId(mv.getId());
        if (!matOpt.isPresent()) {
            throw new IllegalArgumentException("Er bestaat geen materiaal voor de materiaal view.");
        }
        Materiaal materiaal = matOpt.get();
        
        String newFotoUrl = mv.getNewFotoUrl();
        String naam = mv.getNaam();
        int aantal = mv.getAantal();
        String firmanaam = mv.getFirma();
        double prijs = mv.getPrijs();
        int aantalOnbeschikbaar = mv.getAantalOnbeschikbaar();
        List<String> doelgroepenStr = mv.getDoelgroepen();
        List<String> leergebiedenStr = mv.getLeergebieden();

        // Valideer de gegevens
        validatieMateriaalView(newFotoUrl, naam, aantal, prijs, aantalOnbeschikbaar);

        if (materialen.stream().anyMatch(m -> m.getId() != mv.getId() && m.getNaam().equalsIgnoreCase(naam))){
            throw new IllegalArgumentException("naam");
        }
        
        Optional<Firma> firmaOpt = firmaRepo.geefFirma(firmanaam);
        Firma firma = firmaOpt.isPresent() ? firmaOpt.get() : null;

        List<Groep> doelGroepen = groepRepo.geefDoelgroep(doelgroepenStr);
        List<Groep> leerGroepen = groepRepo.convertStringListToLeerGebiedenList(leergebiedenStr);

        if (newFotoUrl != null && !newFotoUrl.trim().isEmpty()){
            materiaal.setFotoBytes(ImageUtil.imageFileToByteArray(newFotoUrl));
        }
        materiaal.setAantal(mv.getAantal())
                .setAantalOnbeschikbaar(mv.getAantalOnbeschikbaar())
                .setArtikelnummer(mv.getArtikelNummer())
                .setBeschrijving(mv.getOmschrijving())
                .setDoelgroepen(doelGroepen)
                .setLeergebieden(leerGroepen)
                .setNaam(mv.getNaam())
                .setPlaats(mv.getPlaats())
                .setPrijs(mv.getPrijs())
                .setUitleenbaarheid(mv.isUitleenbaarheid())
                .setFirma(firma);
    }

    /**
     * Zet het materiaal om in een materiaalview.
     *
     * @param mat het om te zetten materiaal
     * @return de materiaalview met dezelde gegevens als het opgegeven materiaal
     */
    public MateriaalView toMateriaalView(Materiaal mat) {        
        MateriaalView mv = new MateriaalView(mat.getNaam(), mat.getAantal());
        mv.setFotoBytes(mat.getFotoBytes())
                .setOmschrijving(mat.getBeschrijving())
                .setArtikelNummer(mat.getArtikelnummer())
                .setAantalOnbeschikbaar(mat.getAantalOnbeschikbaar())
                .setUitleenbaarheid(mat.isUitleenbaarheid())
                .setPlaats(mat.getPlaats())
                .setFirmaId(mat.getFirma() == null ? -1 : mat.getFirma().getId())
                .setFirma(mat.getFirma() == null ? null : mat.getFirma().getNaam())
                .setEmailFirma(mat.getFirma() == null ? null : mat.getFirma().getEmail())
                .setDoelgroepen(groepListToString(mat.getDoelgroepen()))
                .setLeergebieden(groepListToString(mat.getLeergebieden()))
                .setPrijs(mat.getPrijs())
                .setId(Long.max(mat.getId(), 0));
        return mv;
    }

    // al de protected methodes zijn methodes die het gewoon doorgeven, dit behoort dus dus niet tot MateriaalCatalogusTest
    protected void verwijderGroep(String groepStr, boolean isLeerGroep) {
        groepRepo.verwijderGroep(groepStr, isLeerGroep, materialen);
    }

    protected MateriaalView geefMateriaalView(String materiaalNaam) {
        Optional<Materiaal> materiaalOpt = geefMateriaal(materiaalNaam);
        if (materiaalOpt.isPresent()) {
            return toMateriaalView(materiaalOpt.get());
        } else {
            return null;
        }
    }

    protected void voegGroepToe(String groepNaam, boolean isLeergroep) {
        groepRepo.voegGroepToe(groepNaam, isLeergroep);
    }
    
    protected void voegFirmaToe(String firma, String emailFirma) {
        firmaRepo.voegFirmaToe(firma, emailFirma);
    }
    protected void wijzigFirmas(Firma firma, String nieuweNaam, String nieuwEmailadres) {
        firmaRepo.wijzigFirmas(firma, nieuweNaam, nieuwEmailadres, materialen);
    }
    
    protected List<Groep> geefAlleLeergebieden() {
        return groepRepo.getLeerGebieden();
    }

    protected List<Groep> geefAlleDoelgroepen() {
        return groepRepo.getDoelGroepen();
    }

    private void validatieMateriaalView(String urlFoto, String naam, int aantal,
            double prijs, int aantalOnbeschikbaar) {
        //Exceptions werpen
        if (urlFoto != null && !urlFoto.isEmpty() && !urlFoto.endsWith(".jpg") && !urlFoto.endsWith(".png") && !urlFoto.endsWith(".gif")) {
            throw new IllegalArgumentException("foto");
        }

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

        if (aantalOnbeschikbaar > aantal) {
            throw new IllegalArgumentException("onbeschikbaarAantal");
        }
    }

    private List<String> groepListToString(List<Groep> groepen) {
        if (groepen == null)
            return null;
        
        return groepen.stream().map(g -> g.getGroep()).collect(Collectors.toList());
    }
}
