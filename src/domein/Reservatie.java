/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import shared.ReservatieLijnView;
import shared.MateriaalView;
import shared.ReservatieView;

/**
 *
 * @author Xander
 */
@Entity
@Table(name = "reservaties")
public class Reservatie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lener_id")
    private Gebruiker lener;

    //wordt geconverteerd door util.LocalDateTimeAttributeConverter
    private LocalDateTime ophaalmoment;
    private LocalDateTime indienmoment;

    @OneToMany(mappedBy = "reservatie", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ReservatieLijn> materialen;

    public Reservatie() {
    }

    public Reservatie(Gebruiker lener, LocalDateTime ophaalmoment, LocalDateTime indienmoment, List<ReservatieLijn> materialen) {
        this.lener = lener;
        this.ophaalmoment = ophaalmoment;
        this.indienmoment = indienmoment;
        this.materialen = materialen;
    }
    
    public Reservatie(Gebruiker lener, LocalDateTime ophaalmoment, LocalDateTime indienmoment) {
        this.lener = lener;
        this.ophaalmoment = ophaalmoment;
        this.indienmoment = indienmoment;
//        this.materialen = materialen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Gebruiker getLener() {
        return lener;
    }

    public void setLener(Gebruiker lener) {
        this.lener = lener;
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

    public List<ReservatieLijn> getMaterialen() {
        return materialen;
    }

    public void setMaterialen(List<ReservatieLijn> materialen) {
        this.materialen = materialen;
    }

    public ReservatieView toReservatieView() {
        List<ReservatieLijnView> gereserveerdeMaterialen = new ArrayList<>();

        for (ReservatieLijn gm : materialen) {
            MateriaalView mv = gm.getMateriaal().toMateriaalView();
            ReservatieLijnView gmv
                    = new ReservatieLijnView(gm.getId(), gm.getOphaalmoment(), gm.getIndienmoment(), mv, gm.getAantal());
            gereserveerdeMaterialen.add(gmv);
        }

        ReservatieView rv = new ReservatieView(id, lener.getVoornaam() + " " + lener.getAchternaam(),
                ophaalmoment, indienmoment, gereserveerdeMaterialen);

        return rv;
    }

    @Override
    public String toString() {
        return "Reservatie{" + "id=" + id + ", lener=" + lener + ", ophaalmoment=" + ophaalmoment + ", indienmoment=" + indienmoment + ", materialen=" + materialen + '}';
    }
    
    

}
