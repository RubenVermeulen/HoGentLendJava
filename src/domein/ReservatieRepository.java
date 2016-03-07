/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import domein.materiaal.MateriaalRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import shared.ReservatieLijnView;
import shared.MateriaalView;
import shared.ReservatieView;
import util.JPAUtil;

/**
 *
 * @author Xander
 */
public class ReservatieRepository {

    private List<Reservatie> reservaties;
    private EntityManager em;
    private MateriaalRepository matRepo;
    private GebruikerRepository gebrRepo;

    ReservatieRepository(MateriaalRepository materiaalRepo, GebruikerRepository gebruikerRepo) {
        reservaties = new ArrayList<>();
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        this.matRepo = matRepo;
        this.gebrRepo = gebruikerRepo;
        loadReservaties();
    }

    private void loadReservaties() {
        Query q = em.createQuery("SELECT r FROM Reservatie r");
        reservaties = (List<Reservatie>) q.getResultList();
    }

    public List<Long> geefAlleReservatieIds() {
        return reservaties.stream().map(Reservatie::getId).collect(Collectors.toList());
    }

    public List<ReservatieView> geefAlleReservaties() {

        List<ReservatieView> reservatieViews = new ArrayList();

        for (Reservatie r : reservaties) {
            reservatieViews.add(convertReservatieToReservatieView(r));
        }

        return reservatieViews;

    }

    private ReservatieView convertReservatieToReservatieView(Reservatie r) {

        return r.toReservatieView();

    }

    public void verwijderReservatie(ReservatieView rv) {
        Optional<Reservatie> optR = geefReservatie(rv.getId());
        if (!optR.isPresent()) {
            throw new IllegalArgumentException("De gegeven reservatie bestaat niet.");
        }
        Reservatie reservatie = optR.get();

        em.getTransaction().begin();
        em.remove(reservatie);
        em.getTransaction().commit();

        reservaties.remove(reservatie);
    }

    public Optional<Reservatie> geefReservatie(long id) {
        for (Reservatie r : reservaties) {
            if (r.getId() == id) {
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    public void wijzigReservatie(ReservatieView rv) {
        Optional<Reservatie> optR = geefReservatie(rv.getId());
        if (!optR.isPresent()) {
            throw new IllegalArgumentException("De gegeven reservatie bestaat niet.");
        }
        Reservatie r = optR.get();

        validateOphaalEnIndienMomentsForLijn(rv.getOphaalmoment(), rv.getIndienmoment());
        r.setOphaalmoment(rv.getOphaalmoment());
        r.setIndienmoment(rv.getIndienmoment());

        List<ReservatieLijnView> lvn = rv.getReservatieLijnen();
        List<Long> lnIds = r.getReservatielijnen().stream().map(l -> l.getId()).collect(Collectors.toList());

        for (ReservatieLijnView lv : lvn) {
            if (lv.getId() == null) {
                voegReservatieLijnToe(r, lv);
            } else if (!lnIds.contains(lv.getId())) {
                verwijderReservatieLijn(r, lv.getId());
            } else {
                pasReservatieLijnAan(r, lv);
            }
        }
    }

    private void verwijderReservatieLijn(Reservatie r, long rl) {
        em.getTransaction().begin();
        Iterator<ReservatieLijn> it = r.getReservatielijnen().iterator();
        while (it.hasNext()) {
            if (it.next().getId() == rl) {
                it.remove();
                break;
            }
        }
        em.remove(rl);
        em.getTransaction().commit();
    }

    private void voegReservatieLijnToe(Reservatie r, ReservatieLijnView rlv) {
        em.getTransaction().begin();
        ReservatieLijn rl = new ReservatieLijn(matRepo.geefMateriaalMetId(rlv.getMateriaal().getId()), rlv.getAantal(), rlv.getOphaalmoment(), rlv.getIndienmoment());
        em.persist(rl);
        r.getReservatielijnen().add(rl);
        em.getTransaction().commit();
    }

    private void pasReservatieLijnAan(Reservatie r, ReservatieLijnView rlv) {
        ReservatieLijn rl = r.getReservatielijnen().stream().filter(mrl -> mrl.getId() == rlv.getId()).findFirst().get();
        validateOphaalEnIndienMomentsForLijn(rlv.getOphaalmoment(), rlv.getIndienmoment());
        em.getTransaction().begin();
        rl.setAantal(rlv.getAantal());
        rl.setOphaalmoment(rlv.getOphaalmoment());
        rl.setIndienmoment(rlv.getIndienmoment());
        em.getTransaction().commit();
    }

    private void validateOphaalEnIndienMomentsForLijn(LocalDateTime ophaal, LocalDateTime indien) {
        if (ophaal.isAfter(indien)) {
            throw new IllegalArgumentException("De ophaal datum kan niet na de indien datum liggen.");
        }
    }

    public void voegReservatieToe(ReservatieView rv) {
        long id = rv.getId();
        String lener = rv.getLener();
        String emailLener = rv.getEmailLener();
        LocalDateTime ophaalmoment = rv.getOphaalmoment();
        LocalDateTime indienmoment = rv.getIndienmoment();
        String ophaalmomentAlsString = rv.getOphaalmomentAlsString();
        String indienmomentAlsString = rv.getIndienmomentAlsString();
        String reservatieLijnenAlsString = rv.getReservatieLijnenAlsString();
        List<ReservatieLijnView> reservatieLijnViews = rv.getReservatieLijnen();

        Gebruiker deLener = gebrRepo.geefGebruikerViaEmail(emailLener);
        List<ReservatieLijn> reservatieLijnen = new ArrayList<>();

        for (ReservatieLijnView rlView : reservatieLijnViews) {

            MateriaalView mv = rlView.getMateriaal();
            Materiaal m = matRepo.geefMateriaal(mv.getNaam());

            ReservatieLijn rl = new ReservatieLijn(m, rlView.getAantal(), rlView.getOphaalmoment(), rlView.getIndienmoment());

        }

        validateOphaalEnIndienMomentsForLijn(ophaalmoment, indienmoment);
        Reservatie reservatie = new Reservatie(deLener, ophaalmoment, indienmoment);

        reservatie.setReservatielijnen(reservatieLijnen);

        reservaties.add(reservatie);

        
        //reservatielijnen toevoegen aan db
        rv.getReservatieLijnen().stream().forEach((rlv) -> {
            voegReservatieLijnToe(reservatie, rlv);
        });
        
        
        //reservatie toevoegen aan db
       em.getTransaction().begin();
        em.persist(reservatie);
        em.getTransaction().commit();
        

        
    }

}
