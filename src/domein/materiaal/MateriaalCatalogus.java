package domein.materiaal;

import domein.groep.GroepRepository;
import domein.groep.Groep;
import domein.firma.FirmaRepository;
import domein.firma.Firma;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import shared.MateriaalView;

public class MateriaalCatalogus {

    private List<Materiaal> materialen;
    private FirmaRepository firmaRepo;
    private GroepRepository groepRepo;

    public MateriaalCatalogus() {
        this(new FirmaRepository());
    }

    MateriaalCatalogus(FirmaRepository firmaRepo) {
        this.firmaRepo = firmaRepo;
        materialen = new ArrayList<>();
        groepRepo = new GroepRepository();
    }

    /**
     * Initialiseert het attribuut materialen.
     *
     * @param materialen Lijst van materialen
     */
    public void loadMaterialen(List<Materiaal> materialen) {
        this.materialen = materialen;
    }

    /**
     * Voegt materiaal toe aan de database en aan de materialen lijst.
     *
     * @param mv MateriaalView object
     * @return
     */
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

        Optional<Firma> firmaOpt = firmaRepo.geefFirma(firmaNaam);
        Firma firma = firmaOpt.isPresent() ? firmaOpt.get() : null;

        List<Groep> doelGroepen = groepRepo.geefDoelgroep(doelgroepenStr);
        List<Groep> leerGroepen = groepRepo.convertStringListToLeerGebiedenList(leergebiedenStr);

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

    /**
     * Retourneert alle materialen.
     *
     * @return Lijst van materialen
     */
    public List<MateriaalView> geefAlleMaterialen() {
        List<MateriaalView> materiaalViews = new ArrayList();

        for (Materiaal m : materialen) {
            materiaalViews.add(toMateriaalView(m));
        }

        return materiaalViews;
    }

    /**
     * Verwijdert meegegeven materiaal uit de materialen lijst.
     *
     * @param materiaal Materiaal object
     */
    public void verwijderMateriaal(Materiaal materiaal) {

        materialen.remove(materiaal);

    }

    /**
     * Retourneer een materiaal object gebaseerd op de meegegeven naam.
     *
     * @param materiaalNaam naam van het materiaal
     * @return Materiaal object
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
     * Retourneert een firma repository object.
     *
     * @return FirmaRepository object
     */
    protected FirmaRepository geefFirmaRepo() {

        return firmaRepo;

    }

    /**
     * Retourneer een materiaal object gebaseerd op de meegegeven id.
     *
     * @param id Id van de het materiaal
     * @return Materiaal object
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
                matViews.add(toMateriaalView(mat));
            }
        }
        return matViews;
    }

    /**
     * Wijst alle attributen van een materiaal view toe aan een materiaal.
     *
     * @param mv Materiaal view object
     * @param materiaal Materiaal object
     */
    public void wijsAttributenMateriaalViewToeAanMateriaal(MateriaalView mv, Materiaal materiaal) {

        String urlFoto = mv.getFotoUrl();
        String naam = mv.getNaam();
        int aantal = mv.getAantal();
        String firmanaam = mv.getFirma();
        double prijs = mv.getPrijs();
        int aantalOnbeschikbaar = mv.getAantalOnbeschikbaar();
        List<String> doelgroepenStr = mv.getDoelgroepen();
        List<String> leergebiedenStr = mv.getLeergebieden();

        // Valideer de gegevens
        validatieMateriaalView(urlFoto, naam, aantal, null, prijs, aantalOnbeschikbaar);

        Optional<Firma> firmaOpt = firmaRepo.geefFirma(firmanaam);
        Firma firma = firmaOpt.isPresent() ? firmaOpt.get() : null;

        List<Groep> doelGroepen = groepRepo.geefDoelgroep(doelgroepenStr);
        List<Groep> leerGroepen = groepRepo.convertStringListToLeerGebiedenList(leergebiedenStr);

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

    /**
     * Valideert de meegegeven parameters.
     *
     * @param urlFoto De foto url van de materiaal view
     * @param naam De naam van de materiaal view
     * @param aantal Het aantal van de materiaal view
     * @param firmaEmail Het e-mailadres van de firma in de materiaal view
     * @param prijs De prijs van de materiaal view
     * @param aantalOnbeschikbaar Het aantal onbeschikbaar van de materiaal view
     * @throws IllegalArgumentException *
     */
    public void validatieMateriaalView(String urlFoto, String naam, int aantal, String firmaEmail, double prijs, int aantalOnbeschikbaar) {
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

    /**
     * Vormt een lijst van groep objecten om naar een lijst van strings.
     *
     * @param groepen Lijst van groep objecten
     * @return Lijst van strings
     */
    private List<String> groepListToString(List<Groep> groepen) {
        return groepen.stream().map(g -> g.getGroep()).collect(Collectors.toList());
    }

    /**
     * Retourneert alle groepen die een leergebied zijn.
     *
     * @return Alle leergebieden
     */
    public List<Groep> geefAlleLeergebieden() {
        return groepRepo.getLeerGebieden();
    }

    /**
     * Retourneert alle groepen die een doelgroep zijn.
     *
     * @return Alle doelgroepen
     */
    public List<Groep> geefAlleDoelgroepen() {
        return groepRepo.getDoelGroepen();
    }

    /**
     * Voegt een groep toe op basis van de 2 parameters.
     *
     * @param text De naam van de groep
     * @param isLeergroep Is de groep een leergroep of niet
     */
    public void voegGroepToe(String text, boolean isLeergroep) {
        groepRepo.voegGroepToe(text, isLeergroep);
    }

    /**
     * Verwijdert de groep.
     *
     * @param groepStr De naam van de groep
     * @param isLeerGroep Is de groep een leergroep of een niet
     * @throws IllegalArgumentException *
     */
    public void verwijderGroep(String groepStr, boolean isLeerGroep) {
        if (groepStr == null || groepStr.isEmpty()) {
            throw new IllegalArgumentException("Je hebt geen " + (isLeerGroep ? "leergebied" : "doelgroep") + " geselecteerd.");
        }

        Optional<Groep> groepOpt = groepRepo.geefGroep(groepStr, isLeerGroep);

        if (!groepOpt.isPresent()) {
            if (isLeerGroep) {
                throw new IllegalArgumentException("Het leergebied bestaat niet.");
            } else {
                throw new IllegalArgumentException("Het doelgroep bestaat niet.");
            }
        }

        Groep groep = groepOpt.get();

        for (Materiaal m : materialen) {
            if (isLeerGroep) {
                if (m.getLeergebieden().stream().anyMatch(g -> g.getId() == groep.getId())) {
                    throw new IllegalArgumentException("Er is nog een materiaal met dit leergebied.");
                }
            } else if (m.getDoelgroepen().stream().anyMatch(g -> g.getId() == groep.getId())) {
                throw new IllegalArgumentException("Er is nog een materiaal met deze doelgroep.");
            }
        }
        groepRepo.verwijderGroep(groep);
    }

    /**
     * Wijzigt het firma object van de firma repository en wijzigt alle
     * materialen hun firma object zodat deze overeen komen met de nieuwe
     * waarden.
     *
     * @param firma Het firma object dat zal worden gewijzigd
     * @param nieuweNaam De nieuwe naam voor de firma
     * @param nieuwEmailadres Het nieuwe e-mailadres voor de firma
     */
    public void wijzigFirmas(Firma firma, String nieuweNaam, String nieuwEmailadres) {
        firmaRepo.wijzigFirmas(firma, nieuweNaam, nieuwEmailadres, materialen);
    }
    
    public MateriaalView toMateriaalView(Materiaal mat) {
        MateriaalView mv = new MateriaalView(mat.getNaam(), mat.getAantal());
        mv.setFotoUrl(mat.getFoto())
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
}
