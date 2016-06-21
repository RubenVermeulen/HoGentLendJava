package domein;

import domein.config.Config;
import domein.config.ConfigLoader;
import domein.gebruiker.GebruikerRepository;
import domein.gebruiker.Gebruiker;
import domein.reservatie.ReservatieRepository;
import domein.materiaal.MateriaalRepository;
import domein.groep.Groep;
import domein.firma.FirmaRepository;
import domein.firma.Firma;
import exceptions.BulkToevoegenMisluktException;
import exceptions.GeenToegangException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import shared.ConfigView;
import shared.MateriaalView;
import shared.ReservatieLijnView;
import shared.ReservatieView;

public class DomeinController {

    private GebruikerRepository gebruikerRepo;
    private MateriaalRepository materiaalRepo;
    private ReservatieRepository reservatieRepo;
    private FirmaRepository firmaRepo;
    private Gebruiker aangemelde;
    private Config config;
    private ConfigLoader configLoader;

    public DomeinController() {
        this(new GebruikerRepository());
    }

    public DomeinController(GebruikerRepository gebruikerRepo) {
        this.gebruikerRepo = gebruikerRepo;
        this.firmaRepo = new FirmaRepository();
        this.materiaalRepo = new MateriaalRepository(firmaRepo);
        this.reservatieRepo = new ReservatieRepository(materiaalRepo, gebruikerRepo);
        this.configLoader = new ConfigLoader();
        this.config = configLoader.load();
    }

    /* -------------------------------- */
    // AANGEMELDE
    /* -------------------------------- */
    /**
     * Meld een beheerder aan met het gegeven email en ongeencrypteerd
     * wachtwoord.
     *
     * @param email beheerder email
     * @param wachtwoord ongeencrypteerd wachtwoord
     * @return true indien de gebruiker nu is aangemeld, false indien het email
     * en/of wachtwoord verkeerd waren.
     */
    public boolean meldAan(String email, String wachtwoord) {
        Optional<Gebruiker> optGeb = gebruikerRepo.getBeheerder(email, wachtwoord);

        // Is optGeb aanwezig en een hoofdbeheerder of beheerder
        if (email.equals("hoofdbeheerder@hogent.be") && wachtwoord.equals("hb")) {
            aangemelde = new Gebruiker("Hoofd", "Beheerder", "hoofdbeheerder@hogent.be", true, true, true);
            return true;
        } else if (optGeb.isPresent() && (optGeb.get().isHoofdbeheerder() || optGeb.get().isBeheerder())) {
            aangemelde = optGeb.get();
            return true;
        } else {
            aangemelde = null;
            return false;
        }
    }

    /**
     * Geeft de gegevens van de aangemelde beheerder. Indien geen aangemelde
     * beheerder, geeft arrays van lege strings.
     *
     * @return String[3] - voornaam, achternaam, email
     */
    public String[] geefGegevensAangemeldeGebruiker() {
        if (aangemelde != null) {
            return new String[]{aangemelde.getVoornaam(), aangemelde.getAchternaam(), aangemelde.getEmail()};
        } else {
            return new String[]{"", "", ""};
        }
    }

    /**
     * @return Of de aangemelde beheerder een hoofdbeheer is of niet.
     */
    public boolean isAangemeldeHoofdbeheerder() {
        return aangemelde.isHoofdbeheerder();
    }

    /**
     * Meld de aangemelde beheerder af.
     */
    public void meldAf() {
        aangemelde = null;
    }

    /* -------------------------------- */
    // MATERIALEN
    /* -------------------------------- */
    /**
     * Voeg een nieuw materiaal toe op basis van de gegevens die in de
     * materiaalview zitten.
     *
     * @param mv de materiaalview
     */
    public void voegMateriaalToe(MateriaalView mv) {
        materiaalRepo.voegMateriaalToe(mv);

    }

    /**
     * Voegt alle materialen toe die in het csv bestand van het gegeven path
     * zitten.
     *
     * @param csvFilePath het path naar het csv bestand
     * @throws exceptions.BulkToevoegenMisluktException indien het bulk
     * toevoegen mislukt is
     */
    public void voegMaterialenToeInBulk(String csvFilePath) throws BulkToevoegenMisluktException {
        materiaalRepo.voegMaterialenToeInBulk(csvFilePath);
    }

    /**
     * Zal het materiaal met het materiaal id van in de materiaalview aanpassen
     * zodat de attributen hetzelfde zijn als de gegevens materiaalview.
     *
     * @param mv het aan te passen materiaal in de vorm van een materiaalview
     */
    public void wijzigMateriaal(MateriaalView mv) {
        materiaalRepo.wijzigMateriaal(mv);
    }

    /**
     * Verwijderd het materiaal met de gegeven naam.
     *
     * @param materiaalNaam de materiaal naam
     */
    public void verwijderMateriaal(String materiaalNaam) {

        List<ReservatieView> reservaties = reservatieRepo.geefAlleReservaties();

        materiaalRepo.verwijderMateriaal(materiaalNaam, reservaties);
    }

    /**
     * Geeft alle materialen terug in de vorm van MateriaalViews.
     *
     * @return een lijst van materiaalviews
     */
    public List<MateriaalView> geefAlleMaterialen() {
        return materiaalRepo.geefAlleMaterialen();
    }

    /**
     * Geeft alle materialen in de vorm van een materiaalview waarin de filter
     * string voorkomt.
     *
     * @param filter de zin/woorden voor op te filteren
     * @return een lijst van materiaalviews waarin de filter voorkomt
     */
    public List<MateriaalView> geefMaterialenMetFilter(String filter) {
        return materiaalRepo.geefMaterialenMetFilter(filter);
    }

    /* -------------------------------- */
    // GROEPEN
    /* -------------------------------- */
    /**
     * Voeg een nieuwe groep toe met gegeven naam en isleergroep.
     *
     * @param text de naam van de nieuwe groep
     * @param isLeerGroep of het een doelgroep of leergebied is.
     */
    public void voegGroepToe(String text, boolean isLeerGroep) {
        materiaalRepo.voegGroepToe(text, isLeerGroep);
    }

    /**
     * Verwijdert de groep met gegeven naar en isleergroep.
     *
     * @param groep de naam van de te verwijderen groep
     * @param isLeerGroep of het een doelgroep of leergebied is
     */
    public void verwijderGroep(String groep, boolean isLeerGroep) {
        materiaalRepo.verwijderGroep(groep, isLeerGroep);
    }

    /**
     * Geeft alle doelgroepen in de vorm van een string list.
     *
     * @return de string list
     */
    public List<String> geefAlleDoelgroepen() {
        return groepListToString(materiaalRepo.geefAlleDoelgroepen());
    }

    /**
     * Geeft alle leergebieden in de vorm van een string list.
     *
     * @return de string list
     */
    public List<String> geefAlleLeergebieden() {
        return groepListToString(materiaalRepo.geefAlleLeergebieden());
    }

    /* -------------------------------- */
    // RESERVATIES
    /* -------------------------------- */
    /**
     * Voeg een nieuwe reservatie toe op basis van de gegevens die in de
     * reservatieview zitten.
     *
     * @param rv de reservatieview
     */
    public void voegReservatieToe(ReservatieView rv) {
        reservatieRepo.voegReservatieToe(rv);
    }

    /**
     * Zal de resevatie met het reservatie id van in de reservatieview aanpassen
     * zodat de attributen hetzelfde zijn als de gegevens ReservatieView.
     *
     * @param rv het aan te passen reservaite in de vorm van een reservatieview
     */
    public void wijzigReservatie(ReservatieView rv) {
        reservatieRepo.wijzigReservatie(rv);
    }

    /**
     * Verwijder de reservatie die overeenkomt met de gegeven reservatieview.
     *
     * @param rv de reservatieview
     */
    public void verwijderReservatie(ReservatieView rv) {
        reservatieRepo.verwijderReservatie(rv);
    }

    /**
     * Geeft alle reservatie in de vorm van reservatieviews.
     *
     * @return de lijst van reservatieviews
     */
    public List<ReservatieView> geefAlleReservaties() {
        return reservatieRepo.geefAlleReservaties();
    }

    /**
     * * Geeft het aantal beschikbare materialen(*) terug van het totaal aantal
     * materialen die de gebruiker wenste te reserveren
     *
     * @param rlv
     * @param rv
     * @return aantal materialen van de reservatielijn die niet beschikbaar zijn
     */
    public int heeftConflicten(ReservatieLijnView rlv, ReservatieView rv) {
        return reservatieRepo.heeftConflicten(rlv, rv);
    }

    /**
     * Controleert voor elke reservatieviewlijn in meegegeven reservatie, of er
     * een conflict is of niet, en past nadien het attribuutje "conflict" aan in
     * de reservatieview Deze methode wordt gebruikt door de
     * MateriaalBoxController
     *
     * @param rv reservatieview waarvan de conflicten gecontroleerd moeten
     * worden
     */
    public void setReservatieViewConflict(ReservatieView rv) {
        reservatieRepo.setReservatieViewConflict(rv);
    }

    /**
     * Geeft alle reservatie die voldoen aan de filter gegevens.
     *
     * @param filter
     * @param dtOphaal
     * @param dtIndien
     * @return lijst met reservatieviews
     */
    public List<ReservatieView> geefAlleReservatiesMetFiler(String filter, LocalDateTime dtOphaal, LocalDateTime dtIndien) {
        return reservatieRepo.geefAlleReservatiesMetFiler(filter, dtOphaal, dtIndien);
    }

    /* -------------------------------- */
    // BEHEERDERS
    /* -------------------------------- */
    /**
     * Stel de lector met als email de gegeven email aan als een beheerder.
     *
     * @param email het e-mailadres.
     * @throws IllegalArgumentException indien geen gebruiker is gevonden met
     * het gegeven e-mailadres
     */
    public void stelAanAlsBeheerder(String email) {
        Optional<Gebruiker> gebruikerOpt = gebruikerRepo.geefGebruikerViaEmail(email);
        if (!gebruikerOpt.isPresent()) {
            throw new IllegalArgumentException("Geen gebruiker met het geven e-mailadres gevonden.");
        }
        Gebruiker gebruiker = gebruikerOpt.get();
        checkKanAangemeldeBeheerderStatusWijzigenVan(gebruiker);
        gebruikerRepo.stelAanAlsBeheerder(gebruiker);
    }

    /**
     * Neemt de beheerder status van de gegeven gebruiker weg.
     *
     * @param gebruiker de te degraderen gebruiker
     */
    public void verwijderBeheerder(Gebruiker gebruiker) {
        checkKanAangemeldeBeheerderStatusWijzigenVan(gebruiker);
        gebruikerRepo.verwijderBeheerder(gebruiker);
    }

    /**
     * Geeft alle gebruikers die beheerder zijn. Maar niet de hoofdbeheerder.
     *
     * @return de observablelist van beheerders
     */
    public ObservableList<Gebruiker> geefObservableListBeheerdersZonderHoofdBeheerders() {
        return gebruikerRepo.geefObservableListBeheerdersZonderHoofdBeheerders();
    }

    /* -------------------------------- */
    // FIRMAS
    /* -------------------------------- */
    /**
     * Voegt een nieuwe firma toe met de gegven naam en contact e-mailadres.
     *
     * @param naam de naam
     * @param email het email adres
     */
    public void voegFirmaToe(String naam, String email) {
        firmaRepo.voegFirmaToe(naam, email);
    }

    /**
     * Geeft de gegeven firma een nieuwe naam en e-mailadres.
     *
     * @param firma de te wijzigen firma
     * @param nieuweNaam de nieuwe naam
     * @param nieuwEmailadres het nieuwe e-mailadres
     */
    public void wijzigFirmas(Firma firma, String nieuweNaam, String nieuwEmailadres) {
        materiaalRepo.wijzigFirmas(firma, nieuweNaam, nieuwEmailadres);
    }

    /**
     * Verwijdert de firma met de gegeven naam.
     *
     * @param naam de naam van de te verwijderen firma
     */
    public void verwijderFirma(String naam) {
        firmaRepo.verwijderFirma(naam, materiaalRepo.geefAlleMaterialen());
    }

    /**
     * Geeft een optionele firma met de gegeven naam.
     *
     * @param naam de naam van de te zoeken firma
     * @return leeg indien geen firma gevonden werd
     */
    public Optional<Firma> geefFirma(String naam) {
        return firmaRepo.geefFirma(naam);
    }

    /**
     * Geeft alle namen van alle firmas.
     *
     * @return de string lijst met al de namen van alle firma's
     */
    public List<String> geefAlleFirmas() {
        List<Firma> firmas = firmaRepo.getAllFirmasSorted();

        return firmaListToString(firmas);
    }


    /* -------------------------------- */
    // CONFIG
    /* -------------------------------- */
    public void saveConfig(ConfigView view) {
        if (!aangemelde.isHoofdbeheerder()) {
            throw new GeenToegangException("Je moet hoofdbeheerder zijn om de configuraties te wijzigen.");
        }
        config.applyvView(view);
        configLoader.save();
    }

    public ConfigView geefConfigView() {
        return new ConfigView(config.getStandaardOphaaltijd(), config.getStandaardIndientijd(), config.getStandaardOphaalDag(), config.getStandaardIndienDag(), config.getLeentermijn());
    }

    /* -------------------------------- */
    // HELPERS
    /* -------------------------------- */
    private List<String> groepListToString(List<Groep> groepen) {
        return groepen.stream().map(g -> g.getGroep()).collect(Collectors.toList());
    }

    private void checkKanAangemeldeBeheerderStatusWijzigenVan(Gebruiker gebruiker) {
        if (!aangemelde.isHoofdbeheerder()) {
            throw new GeenToegangException("Je moet hoofdbeheerder zijn om beheerders te verwijderen.");
        }
        if (gebruiker == null) {
            throw new IllegalArgumentException("De gebruiker bestaat niet.");
        }
    }

    private List<String> firmaListToString(List<Firma> firmas) {
        return firmas.stream().map(f -> f.getNaam()).collect(Collectors.toList());
    }

}
