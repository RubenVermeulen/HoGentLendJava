package domein;

import exceptions.GeenToegangException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import shared.MateriaalView;

public class DomeinController {

    private GebruikerRepository gebruikerRepo;
    private MateriaalRepository materiaalRepo;
    private Gebruiker aangemelde;

    public DomeinController() {
        this(new GebruikerRepositoryImpl());
    }

    public DomeinController(GebruikerRepository gebruikerRepo) {
        this.gebruikerRepo = gebruikerRepo;
    }

    /**
     *
     * @param email gebruiker email
     * @param wachtwoord ongeencrypteerd wachtwoord
     * @return true indien de gebruiker nu is aangemeld, false indien het email
     * en/of wachtwoord verkeerd waren.
     */
    public boolean meldAan(String email, String wachtwoord) {
        Optional<Gebruiker> optGeb = gebruikerRepo.getGebruiker(email, wachtwoord);

        // Is optGeb aanwezig en een hoofdbeheerder of beheerder
        if (optGeb.isPresent() && (optGeb.get().isHoofdbeheerder() || optGeb.get().isBeheerder())) {
            aangemelde = optGeb.get();
            return true;
        } else {
            aangemelde = null;
            return false;
        }
    }

    /**
     * Meld de gebruiker af.
     */
    public void meldAf() {
        aangemelde = null;
    }

    /**
     * Geeft de gegevens van de aangemelde gebruiker. Indien geen aangemelde
     * gebruiker, geeft arrays van lege strings.
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
     * TODO xander schrijf dit
     *
     * @param mv
     */
    public void voegMateriaalToe(MateriaalView mv) {
        materiaalRepo.voegMateriaalToe(mv);

    }

    public void voegMaterialenToeInBulk(String csvFile) {
//        materiaalRepo.voegMaterialenToeInBulk(csvFile);
    }

    /**
     * TODO Xander schrijf dit
     *
     * @return
     */
    public List<MateriaalView> geefAlleMaterialen() {
        if (materiaalRepo == null) {
            this.materiaalRepo = new MateriaalRepository();
        }
        return materiaalRepo.geefAlleMaterialen();
    }

    public void verwijderMateriaal(String materiaalNaam) {
        materiaalRepo.verwijderMateriaal(materiaalNaam);
    }

    public List<MateriaalView> geefMaterialenMetFilter(String filter) {
        return materiaalRepo.geefMaterialenMetFilter(filter);
    }

    public boolean wijzigMateriaal(MateriaalView mv) {
        return materiaalRepo.wijzigMateriaal(mv);
    }

    public List<String> geefAlleDoelgroepen() {
        return groepListToString(materiaalRepo.geefAlleDoelgroepen());
    }

    public List<String> geefAlleLeergebieden() {
        return groepListToString(materiaalRepo.geefAlleLeergebieden());
    }

    private List<String> groepListToString(List<Groep> groepen) {
        return groepen.stream().map(g -> g.getGroep()).collect(Collectors.toList());
    }

    public void voegGroepToe(String text, boolean isLeerGroep) {
        materiaalRepo.voegGroepToe(text, isLeerGroep);
    }
    
    public void stelAanAlsBeheerder(String email){
        Gebruiker gebruiker = gebruikerRepo.geefGebruiker(email);
        
        checkKanAangemeldeBeheerderStatusWijzigenVan(gebruiker);
        gebruikerRepo.stelAanAlsBeheerder(gebruiker);
    }
    
    public void verwijderBeheerder(Gebruiker gebruiker){
        checkKanAangemeldeBeheerderStatusWijzigenVan(gebruiker);
        gebruikerRepo.verwijderBeheerder(gebruiker);
    }
    
    private void checkKanAangemeldeBeheerderStatusWijzigenVan(Gebruiker gebruiker){
        if ( ! aangemelde.isHoofdbeheerder()){
            throw new GeenToegangException("Je moet hoofdbeheerder zijn om beheerders te verwijderen.");
        }
        if (gebruiker == null){
            throw new IllegalArgumentException("De gebruiker bestaat niet.");
        }
//        if (!gebruiker.isLector()){
//            throw new IllegalArgumentException("De gebruiker is geen lector.");
//        }
    }
    
    public ObservableList<Gebruiker> geefAlleBeheerders() {
        return gebruikerRepo.geefAlleBeheerders();
    }

    public Gebruiker getAangemelde() {
        return aangemelde;
    }
    
    
}
