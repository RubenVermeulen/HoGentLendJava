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
import util.JPAUtil;

/**
 *
 * @author Xander
 */
public class ReservatieRepository {
    
    private List<Reservatie> reservaties;
    private EntityManager em;

    public ReservatieRepository(EntityManager em) {
        reservaties = new ArrayList<>();
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadReservaties();
    }

    private void loadReservaties() {
        Query q = em.createQuery("SELECT r FROM Reservatie r");
        reservaties = (List<Reservatie>) q.getResultList();
    }
    
    public List<Long> geefAlleReservatieIds(){
        return reservaties.stream().map(Reservatie::getId).collect(Collectors.toList());
    }
    
    public void voegReservatieToe(){
        
    }
    
    
    
    
    
}
