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

/**
 *
 * @author Xander
 */
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
        Optional<Reservatie> optR = geefReservatie(rv.getId());
        if (!optR.isPresent()) {
            throw new IllegalArgumentException("De gegeven reservatie bestaat niet.");
        }
        Reservatie reservatie = optR.get();

        for (ReservatieLijn rl : reservatie.getReservatielijnen()) {
            rl.getMateriaal().setAantalOnbeschikbaar(rl.getMateriaal().getAantalOnbeschikbaar() - rl.getAantal());
        }

        reservaties.remove(reservatie);

        return reservatie;
    }

    public Optional<Reservatie> geefReservatie(long id) {
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

        List<ReservatieLijnView> lvn = rv.getReservatieLijnen();
        List<Long> lnIds = r.getReservatielijnen().stream().map(l -> l.getId()).collect(Collectors.toList());

        List<ReservatieLijnView> wijzigenLijnen = new ArrayList();
        List<ReservatieLijnView> toevoegenLijnen = new ArrayList();
        List<Long> verwijderenLijnIds = new ArrayList();

        for (ReservatieLijnView lv : lvn) {
            if (lv.getId() == null) {
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
                deRl.getMateriaal().setAantalOnbeschikbaar(deRl.getMateriaal().getAantalOnbeschikbaar() - deRl.getAantal());
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
        return rl;
    }

    public void pasReservatieLijnAan(Reservatie r, ReservatieLijnView rlv) {
        ReservatieLijn rl = r.getReservatielijnen().stream().filter(mrl -> mrl.getId() == rlv.getId()).findFirst().get();
        validateOphaalEnIndienMomentsForLijn(rlv.getOphaalmoment(), rlv.getIndienmoment());
        rl.setAantal(rlv.getAantal());
        rl.setOphaalmoment(rlv.getOphaalmoment());
        rl.setIndienmoment(rlv.getIndienmoment());
        rl.getMateriaal().setAantalOnbeschikbaar(rlv.getMateriaal().getAantalOnbeschikbaar());
    }

    public void validateOphaalEnIndienMomentsForLijn(LocalDateTime ophaal, LocalDateTime indien) {
        if (ophaal.isAfter(indien)) {
            throw new IllegalArgumentException("De ophaal datum kan niet na de indien datum liggen.");
        }
    }

    public Reservatie voegReservatieToe(ReservatieView rv) {
        long id = rv.getId();
        String lener = rv.getLener();
        String emailLener = rv.getEmailLener();
        LocalDateTime ophaalmoment = rv.getOphaalmoment();
        LocalDateTime indienmoment = rv.getIndienmoment();
        LocalDateTime reservatiemoment = rv.getReservatiemoment();
        List<ReservatieLijnView> reservatieLijnViews = rv.getReservatieLijnen();

        Optional<Gebruiker> deLenerOpt = gebruikersRepo.geefGebruikerViaEmail(emailLener);
        if (!deLenerOpt.isPresent()) {
            throw new IllegalArgumentException("Geen gebruiker gevonden met het geven email adres.");
        }
        Gebruiker deLener = deLenerOpt.get();
        List<ReservatieLijn> reservatieLijnen = new ArrayList<>();

        for (ReservatieLijnView rlView : reservatieLijnViews) {

            MateriaalView mv = rlView.getMateriaal();

            if (mv.getNaam().equals("")) {
                throw new IllegalArgumentException("Er is geen materiaal ingevuld.");
            }
            Materiaal m = materiaalRepo.geefMateriaal(mv.getNaam()).get();

            ReservatieLijn rl = new ReservatieLijn(m, rlView.getAantal(), rlView.getOphaalmoment(), rlView.getIndienmoment());

        }

        validateOphaalEnIndienMomentsForLijn(ophaalmoment, indienmoment);
        Reservatie reservatie = new Reservatie(deLener, ophaalmoment, indienmoment, reservatiemoment);

        reservatie.setReservatielijnen(reservatieLijnen);

        reservaties.add(reservatie);

        return reservatie;

    }

    public int heeftConflicten(ReservatieLijnView rlv, LocalDateTime reservatiemoment) {


        int aantalOver = rlv.getMateriaal().getAantal() - rlv.getMateriaal().getAantalOnbeschikbaar();

  

        if (aantalOver > 0) {
            return 0;
        }

        LocalDateTime indienmoment = rlv.getIndienmoment();
        LocalDateTime ophaalmoment = rlv.getOphaalmoment();
        long materiaalId = rlv.getMateriaal().getId();

        for (Reservatie r : reservaties) {
            Optional<ReservatieLijn> lijstItem
                    = r.getReservatielijnen().stream().filter(
                            rl -> ((rl.getMateriaal().getId() == materiaalId)
                            && (((rl.getOphaalmoment().isBefore(ophaalmoment) || rl.getOphaalmoment().isEqual(ophaalmoment))
                            && rl.getIndienmoment().isAfter(ophaalmoment))
                            || (rl.getOphaalmoment().isAfter(ophaalmoment) && rl.getOphaalmoment().isBefore(indienmoment))))
                            && rl.getReservatie().getReservatiemoment().isAfter(reservatiemoment)
                    ).findFirst();
            if (lijstItem.isPresent()) {
                aantalOver += lijstItem.get().getAantal();
                if (aantalOver >= 0) {
                    return 0;
                }
            }
        }

        return aantalOver;
    }

    public ReservatieView toReservatieView(Reservatie r) {
        List<ReservatieLijnView> gereserveerdeMaterialen = new ArrayList<>();

        for (ReservatieLijn gm : r.getReservatielijnen()) {

            MateriaalView mv = materiaalRepo.toMateriaalView(gm.getMateriaal());
            ReservatieLijnView gmv
                    = new ReservatieLijnView(gm.getId(), gm.getOphaalmoment(), gm.getIndienmoment(), mv, gm.getAantal());
            gereserveerdeMaterialen.add(gmv);
        }

        ReservatieView rv = new ReservatieView(r.getId(), r.getLener().getVoornaam() + " " + r.getLener().getAchternaam(),
                r.getLener().getEmail(), r.getOphaalmoment(), r.getIndienmoment(), r.getReservatiemoment(),
                r.isOpgehaald(), gereserveerdeMaterialen);

        return rv;
    }
}
