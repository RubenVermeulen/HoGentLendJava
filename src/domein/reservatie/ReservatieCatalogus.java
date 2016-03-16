/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein.reservatie;

import domein.gebruiker.Gebruiker;
import domein.gebruiker.GebruikerRepository;
import domein.materiaal.MateriaalRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import shared.MateriaalView;
import shared.ReservatieLijnView;
import shared.ReservatieView;

public class ReservatieCatalogus {

    private MateriaalRepository materiaalRepo;
    private GebruikerRepository gebruikersRepo;

    private List<Reservatie> reservaties;

    /**
     * Constructor ReservatieCatalogus
     *
     * @param reservaties
     * @param materiaalRepo
     * @param gebruikerRepo
     */
    public ReservatieCatalogus(List<Reservatie> reservaties, MateriaalRepository materiaalRepo,
            GebruikerRepository gebruikerRepo) {

        this.reservaties = reservaties;
        this.materiaalRepo = materiaalRepo;
        this.gebruikersRepo = gebruikerRepo;

    }

    /**
     * Retourneer de id's van alle bestaande reservaties
     *
     * @return lijst met id's
     */
    public List<Long> geefAlleReservatieIds() {
        return reservaties.stream().map(Reservatie::getId).collect(Collectors.toList());
    }

    /**
     * Retourneer alle reservaties in de vorm van een reservatieview
     *
     * @return lijst van reservatieviews
     */
    public List<ReservatieView> geefAlleReservaties() {

        List<ReservatieView> reservatieViews = new ArrayList();

        for (Reservatie r : reservaties) {
            System.out.println(r.toString());
            reservatieViews.add(toReservatieView(r));
        }
        
        Comparator<ReservatieView> comparator = (f1, f2) -> f1.getOphaalmoment().compareTo(f2.getOphaalmoment());
        Collections.sort(reservatieViews, comparator);

        return reservatieViews;

    }

    /**
     * Retourneert alle reservaties (geconverteerd naar een reservatieview), die voldoen aan de meegegeven voorwaarden
     *
     * @param filter de filter met het trefwoord
     * @param dtOphaal de filter van het ophaalmoment
     * @param dtIndien de filter van het indienmoment
     * @return lijst van reservatieviews
     */
    public List<ReservatieView> geefAlleReservatiesMetFiler(String filter, LocalDateTime dtOphaal, LocalDateTime dtIndien) {
        return reservaties.stream()
                .filter(r -> r.containsFilter(filter, dtOphaal, dtIndien))
                .map(r -> toReservatieView(r))
                .collect(Collectors.toList());
    }

    /**
     * Verwijdert de reservatie die hoort bij de meegegeven reservatieview
     *
     * @param rv reservatieview waarvan het materiaal verwijderd moet worden
     * @return de verwijderde reservatie
     * @throws IllegalArgumentException indien de reservatieview niet overeenkomt met een bestaande reservatie
     */
    public Reservatie verwijderReservatie(ReservatieView rv) {
        if (rv == null) {
            throw new IllegalArgumentException();

        }
        Optional<Reservatie> optR = geefReservatie(rv.getId());

        if (!optR.isPresent()) {
            throw new IllegalArgumentException("De gegeven reservatie bestaat niet.");
        }
        Reservatie reservatie = optR.get();

        reservaties.remove(reservatie);

        return reservatie;
    }

    /**
     * Retourneert de reservatie met het opgegeven id
     *
     * @param id van de gewenste reservatie
     * @return de optionele reservatie, leeg indien geen reservatie met opgegeven id werd gevonden
     */
    public Optional<Reservatie> geefReservatie(long id) {
        for (Reservatie r : reservaties) {
            if (r.getId() == id) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    /**
     * Retourneert een ReservatieChanges object, dat de toe te voegen, te wijzigen of te verwijderen reservatielijnen
     * bevat van de opgegeven reservatie(view)
     *
     * @param rv reservatieview dat gewijzigde, verwijderde of toegevoegde reservatielijnen bevat
     * @return ReservatieChanges object
     */
    public ReservatieChanges geefReservatieChanges(ReservatieView rv) {
        Optional<Reservatie> optR = geefReservatie(rv.getId());
        if (!optR.isPresent()) {
            throw new IllegalArgumentException("De gegeven reservatie bestaat niet.");
        }
        Reservatie r = optR.get();
        validateOphaalEnIndienMomentsForLijn(rv.getOphaalmoment(), rv.getIndienmoment());
        r.setOphaalmoment(rv.getOphaalmoment());
        r.setIndienmoment(rv.getIndienmoment());
        r.setOpgehaald(rv.isOpgehaald());
//        System.out.println("RESERVATIEMOMENT " + rv.getReservatiemoment());

        List<ReservatieLijnView> lvn = rv.getReservatieLijnen();
        List<Long> lnIds = r.getReservatielijnen().stream().map(l -> l.getId()).collect(Collectors.toList());

        List<ReservatieLijnView> wijzigenLijnen = new ArrayList();
        List<ReservatieLijnView> toevoegenLijnen = new ArrayList();
        List<Long> verwijderenLijnIds = new ArrayList();

        for (ReservatieLijnView lv : lvn) {
            if (lv.getId() == null) {
//                System.out.println("VOEG RESERVATIELIJN TOE");
                toevoegenLijnen.add(lv);
            } else {
                wijzigenLijnen.add(lv);
            }
        }
        List<Long> lijnViewIds = lvn.stream().map(l -> l.getId()).collect(Collectors.toList());
        for (Long lijnId : lnIds) {
            if (!lijnViewIds.contains(lijnId)) {
                verwijderenLijnIds.add(lijnId);
            }
        }

        return new ReservatieChanges(r, wijzigenLijnen, toevoegenLijnen, verwijderenLijnIds);
    }

    /**
     * Verwijdert de meegegeven reservatielijn uit de meegegeven reservatie
     *
     * @param r reservatie waarvan een lijn wordt verwijderd
     * @param rl lijn die wordt verwijderd
     * @return ReservatieLijn die werd verwijderd, null indien de reservatielijn niet gevonden werd
     */
    public ReservatieLijn verwijderReservatieLijn(Reservatie r, long rl) {
        Iterator<ReservatieLijn> it = r.getReservatielijnen().iterator();
        while (it.hasNext()) {
            ReservatieLijn deRl = it.next();
            if (deRl.getId() == rl) {
                it.remove();
                return (deRl);
            }
        }
        return (null);
    }

    /**
     * Maakt een nieuwe ReservatieLijn met de gegevens van de meegegeven ReservatieLijnView
     * Voegt het nieuwe ReservatieLijn object toe aan de meegegeven Reservatie
     *
     * @param r reservatie waaraan de lijn moet worden toegevoegd
     * @param rlv lijn die moet worden toegevoegd
     * @return de aangemaakte ReservatieLijn
     */
    public ReservatieLijn voegReservatieLijnToe(Reservatie r, ReservatieLijnView rlv) {
        ReservatieLijn rl = new ReservatieLijn(materiaalRepo.geefMateriaalMetId(rlv.getMateriaal().getId()).get(),
                rlv.getAantal(), rlv.getOphaalmoment(), rlv.getIndienmoment());
        r.getReservatielijnen().add(rl);
        rl.setReservatie(r);
        return rl;
    }

    /**
     * Wijzigt de ReservatieLijn die overeenkomt met de meegegeven ReservatieLijnView
     *
     * @param r reservatie waartoe de te wijzigen reservatielijn behoort
     * @param rlv reservatielijnview met wijzigingen die moeten worden aangebracht aan de bijhorende reservatielijn
     */
    public void pasReservatieLijnAan(Reservatie r, ReservatieLijnView rlv) {
        ReservatieLijn rl = r.getReservatielijnen().stream().filter(mrl -> mrl.getId() == rlv.getId()).findFirst().get();
        validateOphaalEnIndienMomentsForLijn(rlv.getOphaalmoment(), rlv.getIndienmoment());
        rl.setAantal(rlv.getAantal());
        rl.setOphaalmoment(rlv.getOphaalmoment());
        rl.setIndienmoment(rlv.getIndienmoment());
    }

    /**
     * Valideert of het ophaalmoment van een reservatielijn voor zijn indienmoment ligt
     *
     * @param ophaal ophaalmoment van de reservatielijn
     * @param indien indienmoment van de reservatielijn
     * @throws IllegalArgumentException wanneer het ophaalmoment na het indienmoment valt
     */
    public void validateOphaalEnIndienMomentsForLijn(LocalDateTime ophaal, LocalDateTime indien) {
        if (ophaal.isAfter(indien)) {
            throw new IllegalArgumentException("De ophaaldatum kan niet na de indiendatum liggen.");
        }
    }

    /**
     * Voegt nieuwe reservatie toe met de gegevens van de meegeleverde reservatieview
     *
     * @param rv reservatieview met de gegevens van de te maken reservatie
     * @return het nieuwe Reservatie object
     * @throws IllegalArgumentException indien een van de gegevens in de reservatieview niet voldoen aan de opgelegde
     * restricties
     * @throws IllegalArgumentException indien geen gebruiker werd gevonden met het meegegeven emailadres
     * @throws IllegalArgumentException indien de meegeleverde reservatieview geen reservatielijn met materiaal
     * bevat
     */
    public Reservatie voegReservatieToe(ReservatieView rv) {
        if (rv == null) {
            throw new IllegalArgumentException();
        }

        if (rv.getEmailLener() == null || rv.getEmailLener().isEmpty()) {
            throw new IllegalArgumentException();
        }
        String emailLener = rv.getEmailLener();

        if (rv.getOphaalmoment() == null) {
            throw new IllegalArgumentException();
        }
        LocalDateTime ophaalmoment = rv.getOphaalmoment();

        if (rv.getIndienmoment() == null) {
            throw new IllegalArgumentException();
        }
        LocalDateTime indienmoment = rv.getIndienmoment();

        if (rv.getReservatiemoment() == null) {
            throw new IllegalArgumentException();
        }
        LocalDateTime reservatiemoment = rv.getReservatiemoment();

        if (rv.getReservatieLijnen() == null) {
            throw new IllegalArgumentException();
        }
        List<ReservatieLijnView> reservatieLijnViews = rv.getReservatieLijnen();

        Optional<Gebruiker> deLenerOpt = gebruikersRepo.geefGebruikerViaEmail(emailLener);
        if (!deLenerOpt.isPresent()) {
            throw new IllegalArgumentException("Geen gebruiker gevonden met het geven email adres.");
        }
        Gebruiker deLener = deLenerOpt.get();
        List<ReservatieLijn> reservatieLijnen = new ArrayList<>();

        Reservatie reservatie = new Reservatie(deLener, ophaalmoment, indienmoment, reservatiemoment);

        for (ReservatieLijnView rlView : reservatieLijnViews) {

            MateriaalView mv = rlView.getMateriaal();

            if (mv.getNaam().equals("")) {
                throw new IllegalArgumentException("Er is geen materiaal geselecteerd.");
            }

        }

        validateOphaalEnIndienMomentsForLijn(ophaalmoment, indienmoment);

        reservatie.setReservatielijnen(reservatieLijnen);

        reservaties.add(reservatie);

        return reservatie;

    }

    /**
     * Geeft het aantal beschikbare materialen(*) terug van het totaal aantal materialen die 
     * de gebruiker wenste te reserveren
     * 
     * @param rlv
     * @param rv
     * @return aantal materialen van de reservatielijn die niet beschikbaar zijn
     * @throws IllegalArgumentException wanneer de reservatielijnview of reservatieview null zijn
     */
    public int heeftConflicten(ReservatieLijnView rlv, ReservatieView rv) {
        if (rlv == null || rv ==null) {
            throw new IllegalArgumentException("Reservatie of reservatieview niet meegegeven!");
        }

        Reservatie meegegevenReservatie = geefReservatie(rv.getId()).get();
        
        LocalDateTime reservatiemoment = rv.getReservatiemoment();
        LocalDateTime indienmoment = rlv.getIndienmoment();
        LocalDateTime ophaalmoment = rlv.getOphaalmoment();
        long materiaalId = rlv.getMateriaal().getId();

        List<ReservatieLijn> lijst = new ArrayList<>();
        int aantalGereserveerd = 0;
        int aantalBeschikbaar = rlv.getMateriaal().getAantal() - rlv.getMateriaal().getAantalOnbeschikbaar();


        //gaat over alle reservatielijnen (ook de deze), checkt op overlappingen, voeg toe aan lijst
        for (Reservatie r : reservaties) {
            r.getReservatielijnen().stream().filter(
                    rl -> ((rl.getMateriaal().getId() == materiaalId)
                    && (((rl.getOphaalmoment().isBefore(ophaalmoment) || rl.getOphaalmoment().isEqual(ophaalmoment))
                    && rl.getIndienmoment().isAfter(ophaalmoment))
                    || (rl.getOphaalmoment().isAfter(ophaalmoment) && rl.getOphaalmoment().isBefore(indienmoment))))
            ).forEach(lijst::add);
        }

        aantalGereserveerd = lijst.stream().map((rl) -> rl.getAantal()).reduce(aantalGereserveerd, Integer::sum);
        int teller = lijst.size() - 1;
        ReservatieLijn temprl;
        
        while (aantalGereserveerd > aantalBeschikbaar && teller > -1) {
            temprl = lijst.get(teller);
            
            if(!meegegevenReservatie.getLener().isLector() && temprl.getReservatie().getLener().isLector()){
                
            }
            else if (
                    ((!temprl.getReservatie().getLener().isLector()) && meegegevenReservatie.getLener().isLector())
                    ||
                    temprl.getReservatie().getReservatiemoment().isAfter(reservatiemoment)
                    || (temprl.getReservatie().getReservatiemoment().isEqual(reservatiemoment)
                    && temprl.getId() > rlv.getId()))   {
                aantalBeschikbaar += temprl.getAantal();
            }
            teller--;
            
        }

        return aantalBeschikbaar - aantalGereserveerd;
    }

    /**
     * Converteert een reservatie naar een reservatieview
     *
     * @param r de te converteren reservatie
     * @return reservatieview
     */
    public ReservatieView toReservatieView(Reservatie r) {
        List<ReservatieLijnView> gereserveerdeMaterialen = new ArrayList<>();

        for (ReservatieLijn gm : r.getReservatielijnen()) {

            MateriaalView mv = materiaalRepo.toMateriaalView(gm.getMateriaal());
            ReservatieLijnView gmv
                    = new ReservatieLijnView(gm.getId(), gm.getOphaalmoment(), gm.getIndienmoment(), mv, gm.getAantal());
            gereserveerdeMaterialen.add(gmv);
        }
        
        ReservatieView rv = new ReservatieView(Long.max(r.getId(), 0), r.getLener().getVoornaam() + " " + r.getLener().getAchternaam(),
                r.getLener().getEmail(), r.getOphaalmoment(), r.getIndienmoment(), r.getReservatiemoment(),
                r.isOpgehaald(), gereserveerdeMaterialen);

        return rv;
    }

    /**
     * Controleert voor elke reservatieviewlijn in meegegeven reservatie, of er een conflict is of niet, en past nadien
     * het attribuutje "conflict" aan in de reservatieview
     * Deze methode wordt gebruikt door de MateriaalBoxController 
     *
     * @param rv reservatieview waarvan de conflicten gecontroleerd moeten worden
     * @return boolean indien een conflict is gevonden
     */
    public boolean setReservatieViewConflict(ReservatieView rv) {
        boolean check = false;
        
        for(ReservatieLijnView rlv : rv.getReservatieLijnen()){
            if(heeftConflicten(rlv, rv) < 0){
                check = true;
                break;
            }
        }
        
        rv.setConflict(check);
        return check;
        
    }
}
