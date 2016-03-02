/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Xander
 */
@Entity
@Table(name = "gereserveerde_materialen")
public class GereserveerdMateriaal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    //wordt geconverteerd door util.LocalDateTimeAttributeConverter
    private LocalDateTime ophaalmoment;
    private LocalDateTime indienmoment;
    
    @ManyToOne
    @JoinColumn(name="materiaal_id")
    private Materiaal materiaal;
    private int aantal;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="reservatie_id")
    private Reservatie reservatie;

    public GereserveerdMateriaal() {
    }

    public GereserveerdMateriaal(Materiaal materiaal, int aantal, LocalDateTime ophaalmoment, LocalDateTime indienmoment) {
        this.ophaalmoment = ophaalmoment;
        this.indienmoment = indienmoment;
        this.materiaal = materiaal;
        this.aantal = aantal;
    }
    
    
    
    
}
