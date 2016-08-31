/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein.reservatie;

import domein.gebruiker.GebruikerRepository;
import domein.materiaal.MateriaalRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import shared.ReservatieLijnView;
import shared.ReservatieView;
import util.JPAUtil;

public class ReservatieRepository {

    private EntityManager em;
    private MateriaalRepository materiaalRepository;
    private GebruikerRepository gebruikerRepository;
    private ReservatieCatalogus reservatieCat;

    public ReservatieRepository(MateriaalRepository materiaalRepo, GebruikerRepository gebruikerRepo) {
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        this.materiaalRepository = materiaalRepo;
        this.gebruikerRepository = gebruikerRepo;
    }

    /**
     * Haalt alle reservaties op uit de database
     * en maakt een catalogus aan.
     */
    private void loadReservatieCatalogus() {
        Query q = em.createQuery("SELECT r FROM Reservatie r");
        List<Reservatie> reservatieLijst = (List<Reservatie>) q.getResultList();
        reservatieCat = new ReservatieCatalogus(reservatieLijst, materiaalRepository, gebruikerRepository);
    }

    public List<Long> geefAlleReservatieIds() {
        loadReservatieCatalogus();
        
        return reservatieCat.geefAlleReservatieIds();
    }

    public List<ReservatieView> geefAlleReservaties() {
        loadReservatieCatalogus();
        
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

    public List<ReservatieView> geefAlleReservatiesMetFilter(String filter, LocalDateTime dtOphaal, LocalDateTime dtIndien) {
        loadReservatieCatalogus();
        
        return reservatieCat.geefAlleReservatiesMetFilter(filter, dtOphaal, dtIndien);
    }

    public void setReservatieViewConflict(ReservatieView rv) {
        reservatieCat.setReservatieViewConflict(rv);
    }

}
