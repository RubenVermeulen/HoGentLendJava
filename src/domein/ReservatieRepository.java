/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Xander
 */
public class ReservatieRepository {
    
    private List<Reservatie> reservaties;
    private EntityManager em;

    public ReservatieRepository(EntityManager em) {
        this.em = em;
    }
    
    
    
    
    
}
