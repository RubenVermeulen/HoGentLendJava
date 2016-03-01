/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Xander
 */

@Entity
@Table(name = "materialen")

public class Reservatie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Gebruiker lener;
    
    private LocalDateTime ophaalmoment;
    private LocalDateTime indienmoment;
    private List<GereserveerdMateriaal> materialen;

    public Reservatie(Gebruiker lener, LocalDateTime ophaalmoment, LocalDateTime indienmoment, List<GereserveerdMateriaal> materialen) {
        this.lener = lener;
        this.ophaalmoment = ophaalmoment;
        this.indienmoment = indienmoment;
        this.materialen = materialen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    
    
    
}
