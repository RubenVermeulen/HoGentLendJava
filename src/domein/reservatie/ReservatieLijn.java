/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein.reservatie;

import domein.materiaal.Materiaal;
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
import util.MyDateUtil;

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
    private Materiaal materiaal;
    private int aantal;

    @ManyToOne
    private Reservatie reservatie;

    public ReservatieLijn() {
    }

    public ReservatieLijn(Materiaal materiaal, int aantal, LocalDateTime ophaalmoment, LocalDateTime indienmoment) {
        this.ophaalmoment = ophaalmoment;
        this.indienmoment = indienmoment;
        this.materiaal = materiaal;
        this.aantal = aantal;
        materiaal.setAantalOnbeschikbaar(materiaal.getAantalOnbeschikbaar()+aantal);
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
        if(reservatie==null)
            System.out.println("Sven had gelijk xp");
        return reservatie;
    }

    public void setReservatie(Reservatie reservatie) {
        this.reservatie = reservatie;
    }

    @Override
    public String toString() {
        return "ReservatieLijn{" + "id=" + id + ", ophaalmoment=" + ophaalmoment + ", indienmoment=" + indienmoment + ", aantal=" + aantal + '}';
    }

    public boolean containsFilter(String sFilter, LocalDateTime dtOphaal, LocalDateTime dtIndien) {
        boolean filterDatum = MyDateUtil.doesFirstPairOverlapWithSecond(dtOphaal, dtIndien, ophaalmoment, indienmoment);
        
        System.out.println(String.valueOf(dtOphaal) + " ============== " + String.valueOf(dtIndien) + " -------- "+String.valueOf(ophaalmoment) + String.valueOf(indienmoment));
        System.out.println(filterDatum);
        boolean materiaalFilter = materiaal.containsFilter(sFilter);
        boolean materiaalFilterMatters = (sFilter != null && !sFilter.trim().isEmpty()) || (dtOphaal == null && dtIndien == null);
        return filterDatum || (materiaalFilterMatters && materiaalFilter);
    }

}
