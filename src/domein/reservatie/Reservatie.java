package domein.reservatie;

import domein.gebruiker.Gebruiker;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Convert;
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
import util.MyDateUtil;


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
    private LocalDateTime reservatiemoment;
    
    private boolean opgehaald;

    @OneToMany(mappedBy = "reservatie", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<ReservatieLijn> reservatielijen;

    public Reservatie() {
    }

//    public Reservatie(Gebruiker lener, LocalDateTime ophaalmoment, LocalDateTime indienmoment, List<ReservatieLijn> materialen) {
//        this.lener = lener;
//        this.ophaalmoment = ophaalmoment;
//        this.indienmoment = indienmoment;
//        this.materialen = materialen;
//    }
    public Reservatie(Gebruiker lener, LocalDateTime ophaalmoment, LocalDateTime indienmoment) {
        this(lener, ophaalmoment, indienmoment, LocalDateTime.now());
    }
    
     public Reservatie(Gebruiker lener, LocalDateTime ophaalmoment, LocalDateTime indienmoment,
             LocalDateTime reservatiemoment) {
        this(lener, ophaalmoment, indienmoment, reservatiemoment, false);
    }
    
   public Reservatie(Gebruiker lener, LocalDateTime ophaalmoment, LocalDateTime indienmoment, 
           LocalDateTime reservatiemoment, boolean opgehaald) {
        this.lener = lener;
        this.ophaalmoment = ophaalmoment;
        this.indienmoment = indienmoment;
        this.reservatiemoment = reservatiemoment;
        this.opgehaald = opgehaald;
    }
   
    public boolean containsFilter(String sFilter, LocalDateTime dtOphaal, LocalDateTime dtIndien){
        boolean filterInLijnen = false;
        for(ReservatieLijn l : reservatielijen){
            filterInLijnen = l.containsFilter(sFilter, dtOphaal, dtIndien);
            if (filterInLijnen) break;
        }

        
        boolean filterInLener = lener.containsFilter(sFilter);
        boolean lenerFiltersMatter = (sFilter != null && !sFilter.trim().isEmpty()) || (dtOphaal == null && dtIndien == null);
        boolean filterDatum = MyDateUtil.doesFirstPairOverlapWithSecond(dtOphaal, dtIndien, ophaalmoment, indienmoment);
    
        if (lenerFiltersMatter){
            return (filterDatum || filterInLijnen) && filterInLener;
        }else{
            return filterDatum || filterInLijnen;
        }
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

    public List<ReservatieLijn> getReservatielijnen() {
        return reservatielijen;
    }

    public void setReservatielijnen(List<ReservatieLijn> reservatielijen) {
        this.reservatielijen = reservatielijen;
    }

    public LocalDateTime getReservatiemoment() {
        return reservatiemoment;
    }

    public void setReservatiemoment(LocalDateTime reservatiemoment) {
        this.reservatiemoment = reservatiemoment;
    }

    public boolean isOpgehaald() {
        return opgehaald;
    }

    public void setOpgehaald(boolean opgehaald) {
        this.opgehaald = opgehaald;
    }

    @Override
    public String toString() {
        return "Reservatie{" + "id=" + id + ", lener=" + lener + ", ophaalmoment=" + ophaalmoment + ", indienmoment=" + indienmoment + ", reservatielijen=" + reservatielijen + '}';
    }

}
