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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import shared.ReservatieLijnView;
import shared.MateriaalView;
import shared.ReservatieView;
import util.JPAUtil;

public class ReservatieRepository {

//    private List<Reservatie> reservaties;
    private EntityManager em;
    private ReservatieCatalogus reservatieCat;
//    private MateriaalRepository materiaalRepo;
//    private GebruikerRepository gebruikersRepo;

    public ReservatieRepository(MateriaalRepository materiaalRepo, GebruikerRepository gebruikerRepo) {
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadReservatieCatalogus(materiaalRepo, gebruikerRepo);
    }

    private void loadReservatieCatalogus(MateriaalRepository materiaalRepo, GebruikerRepository gebruikersRepo) {
        Query q = em.createQuery("SELECT r FROM Reservatie r");
        List<Reservatie> reservatieLijst = (List<Reservatie>) q.getResultList();
        reservatieCat = new ReservatieCatalogus(reservatieLijst, materiaalRepo, gebruikersRepo);
    }

    public List<Long> geefAlleReservatieIds() {
        return reservatieCat.geefAlleReservatieIds();
    }

    public List<ReservatieView> geefAlleReservaties() {
        return reservatieCat.geefAlleReservaties();
    }

    public void verwijderReservatie(ReservatieView rv) {
        Reservatie reservatie = reservatieCat.verwijderReservatie(rv);
        em.getTransaction().begin();
        em.remove(reservatie);
        em.getTransaction().commit();
    }

    public Optional<Reservatie> geefReservatie(long id) {
        return reservatieCat.geefReservatie(id);
    }

    //kan niet getest worden (test child methodes voegReservatieLijnToe, pasReservatieLijnAan, verwijderReservatieLijn)
    public void wijzigReservatie(ReservatieView rv) {
        ReservatieChanges rChanges = reservatieCat.geefReservatieChanges(rv);
        Reservatie r = rChanges.getReservatie();
        for (ReservatieLijnView lv : rChanges.getWijzigenLijnen()) {
            pasReservatieLijnAan(r, lv);
        }
        for (ReservatieLijnView lv : rChanges.getToevoegenLijnen()) {
            voegReservatieLijnToe(r, lv);
        }
        for (Long id : rChanges.getVerwijderenLijnIds()) {
            verwijderReservatieLijn(r, id);
        }
    }

    private void verwijderReservatieLijn(Reservatie r, long rl) {
        ReservatieLijn deRl = reservatieCat.verwijderReservatieLijn(r, rl);
        if (deRl != null) {
            em.getTransaction().begin();
            em.remove(deRl);
            em.getTransaction().commit();
        }
    }

    private void voegReservatieLijnToe(Reservatie r, ReservatieLijnView rlv) {
        ReservatieLijn rl = reservatieCat.voegReservatieLijnToe(r, rlv);

        em.getTransaction().begin();
        em.persist(rl);
        em.getTransaction().commit();
    }

    private void pasReservatieLijnAan(Reservatie r, ReservatieLijnView rlv) {
        reservatieCat.pasReservatieLijnAan(r, rlv);
        em.getTransaction().begin();
        em.getTransaction().commit();
    }

    public void voegReservatieToe(ReservatieView rv) {
        Reservatie reservatie = reservatieCat.voegReservatieToe(rv);

        //reservatielijnen toevoegen aan db
        rv.getReservatieLijnen().stream().forEach((rlv) -> {
            voegReservatieLijnToe(reservatie, rlv);
        });

        //reservatie toevoegen aan db
        em.getTransaction().begin();
        em.persist(reservatie);
        em.getTransaction().commit();

    }

    public ReservatieView toReservatieView(Reservatie r) {
        return reservatieCat.toReservatieView(r);
    }

    public int heeftConflicten(ReservatieLijnView rlv, ReservatieView rv) {
        return reservatieCat.heeftConflicten(rlv, rv);
    }

    public List<ReservatieView> geefAlleReservatiesMetFiler(String filter, LocalDateTime dtOphaal, LocalDateTime dtIndien) {
        return reservatieCat.geefAlleReservatiesMetFiler(filter, dtOphaal, dtIndien);
    }

}
