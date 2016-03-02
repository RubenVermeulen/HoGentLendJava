/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.time.LocalDateTime;
import javax.persistence.CascadeType;
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
@Table(name = "reservatie_lijn")
public class ReservatieLijn {
    
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
    
    @ManyToOne
    @JoinColumn(name="reservatie_id")
    private Reservatie reservatie;

    public ReservatieLijn() {
    }

    public ReservatieLijn(Materiaal materiaal, int aantal, LocalDateTime ophaalmoment, LocalDateTime indienmoment) {
        this.ophaalmoment = ophaalmoment;
        this.indienmoment = indienmoment;
        this.materiaal = materiaal;
        this.aantal = aantal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOphaalmoment() {
        return ophaalmoment;
    }

    public void setOphaalmoment(LocalDateTime ophaalmoment) {
        this.ophaalmoment = ophaalmoment;
    }

    public LocalDateTime getIndienmoment() {
        return indienmoment;
    }

    public void setIndienmoment(LocalDateTime indienmoment) {
        this.indienmoment = indienmoment;
    }

    public Materiaal getMateriaal() {
        return materiaal;
    }

    public void setMateriaal(Materiaal materiaal) {
        this.materiaal = materiaal;
    }

    public int getAantal() {
        return aantal;
    }

    public void setAantal(int aantal) {
        this.aantal = aantal;
    }

    public Reservatie getReservatie() {
        return reservatie;
    }

    public void setReservatie(Reservatie reservatie) {
        this.reservatie = reservatie;
    }

    @Override
    public String toString() {
        return "GereserveerdMateriaal{" + "id=" + id + ", ophaalmoment=" + ophaalmoment + ", indienmoment=" + indienmoment + ", materiaal=" + materiaal + ", aantal=" + aantal + ", reservatie=" + reservatie + '}';
    }
    
    
    
    
}