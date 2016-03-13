/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein.reservatie;

import domein.gebruiker.Gebruiker;
import domein.gebruiker.GebruikerRepository;
import domein.materiaal.Materiaal;
import domein.materiaal.MateriaalRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public ReservatieCatalogus(List<Reservatie> reservaties, MateriaalRepository materiaalRepo,
            GebruikerRepository gebruikerRepo) {

        this.reservaties = reservaties;
        this.materiaalRepo = materiaalRepo;
        this.gebruikersRepo = gebruikerRepo;

    }

    public List<Long> geefAlleReservatieIds() {
        return reservaties.stream().map(Reservatie::getId).collect(Collectors.toList());
    }

    public List<ReservatieView> geefAlleReservaties() {

        List<ReservatieView> reservatieViews = new ArrayList();

        for (Reservatie r : reservaties) {
            System.out.println(r.toString());
            reservatieViews.add(toReservatieView(r));
        }

        return reservatieViews;

    }

    public List<ReservatieView> geefAlleReservatiesMetFiler(String filter, LocalDateTime dtOphaal, LocalDateTime dtIndien) {
        return reservaties.stream()
                .filter(r -> r.containsFilter(filter, dtOphaal, dtIndien))
                .map(r -> toReservatieView(r))
                .collect(Collectors.toList());
    }

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

    public Optional<Reservatie> geefReservatie(long id) {
        System.out.println(id);
        for (Reservatie r : reservaties) {
            if (r.getId() == id) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

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
        System.out.println("RESERVATIEMOMENT " + rv.getReservatiemoment());

        List<ReservatieLijnView> lvn = rv.getReservatieLijnen();
        List<Long> lnIds = r.getReservatielijnen().stream().map(l -> l.getId()).collect(Collectors.toList());

        List<ReservatieLijnView> wijzigenLijnen = new ArrayList();
        List<ReservatieLijnView> toevoegenLijnen = new ArrayList();
        List<Long> verwijderenLijnIds = new ArrayList();

        for (ReservatieLijnView lv : lvn) {
            if (lv.getId() == null) {
                System.out.println("VOEG RESERVATIELIJN TOE");
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

    public ReservatieLijn voegReservatieLijnToe(Reservatie r, ReservatieLijnView rlv) {
        ReservatieLijn rl = new ReservatieLijn(materiaalRepo.geefMateriaalMetId(rlv.getMateriaal().getId()).get(),
                rlv.getAantal(), rlv.getOphaalmoment(), rlv.getIndienmoment());
        r.getReservatielijnen().add(rl);
        rl.setReservatie(r);
        return rl;
    }

    public void pasReservatieLijnAan(Reservatie r, ReservatieLijnView rlv) {
        ReservatieLijn rl = r.getReservatielijnen().stream().filter(mrl -> mrl.getId() == rlv.getId()).findFirst().get();
        validateOphaalEnIndienMomentsForLijn(rlv.getOphaalmoment(), rlv.getIndienmoment());
        rl.setAantal(rlv.getAantal());
        rl.setOphaalmoment(rlv.getOphaalmoment());
        rl.setIndienmoment(rlv.getIndienmoment());
    }

    public void validateOphaalEnIndienMomentsForLijn(LocalDateTime ophaal, LocalDateTime indien) {
        if (ophaal.isAfter(indien)) {
            throw new IllegalArgumentException("De ophaaldatum kan niet na de indiendatum liggen.");
        }
    }

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

        System.out.println("aantal beschikbaar vooraf = " + aantalBeschikbaar);

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
        System.out.println("lijst size : " + teller);
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
            System.out.println(teller + ": " + aantalBeschikbaar);
        }

        System.out.println("gereserveerd" + aantalGereserveerd + ", beschikbaar" + aantalBeschikbaar);

        System.out.println(lijst);

        return aantalBeschikbaar - aantalGereserveerd;
    }

    public ReservatieView toReservatieView(Reservatie r) {
        List<ReservatieLijnView> gereserveerdeMaterialen = new ArrayList<>();

        for (ReservatieLijn gm : r.getReservatielijnen()) {

            MateriaalView mv = materiaalRepo.toMateriaalView(gm.getMateriaal());
            ReservatieLijnView gmv
                    = new ReservatieLijnView(gm.getId(), gm.getOphaalmoment(), gm.getIndienmoment(), mv, gm.getAantal());
            gereserveerdeMaterialen.add(gmv);
        }
        System.out.println("id in toreservatieview: " + r.getId());
        ReservatieView rv = new ReservatieView(Long.max(r.getId(), 0), r.getLener().getVoornaam() + " " + r.getLener().getAchternaam(),
                r.getLener().getEmail(), r.getOphaalmoment(), r.getIndienmoment(), r.getReservatiemoment(),
                r.isOpgehaald(), gereserveerdeMaterialen);

        return rv;
    }
}
