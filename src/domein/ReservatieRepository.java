/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.ArrayList;
import java.util.List;
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

    public ReservatieRepository() {
        reservaties = new ArrayList<>();
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadReservaties();
    }

    private void loadReservaties() {
        Query q = em.createQuery("SELECT r FROM Reservatie r");
        reservaties = (List<Reservatie>) q.getResultList();
        System.out.println(reservaties);
    }
    
    public List<Long> geefAlleReservatieIds(){
        return reservaties.stream().map(Reservatie::getId).collect(Collectors.toList());
    }
    
    public List<ReservatieView> geefAlleReservaties(){
        
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
        Reservatie reservatie = geefReservatie(rv.getId());
           
        em.getTransaction().begin();
        em.remove(reservatie);
        em.getTransaction().commit();
        
        reservaties.remove(reservatie);
    }
    
    public Reservatie geefReservatie(long id) {
        for (Reservatie r : reservaties) {
            if (r.getId() == id)
                return r;
        }
        
        throw new IllegalArgumentException("Kon geen reservatie vinden");
    }
    
}
