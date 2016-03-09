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
        
        
        boolean materiaalFilter = materiaal.containsFilter(sFilter);
        boolean materiaalFilterMatters = (sFilter != null && !sFilter.trim().isEmpty()) || (dtOphaal == null && dtIndien == null);
        if (materiaalFilterMatters){
            return filterDatum && materiaalFilter;
        }else{
            return filterDatum;
        }
    }
    /**
     
     * 
     * 
     * 
    return filterDatum && materiaalFilterMatters ? filterDatum && materiaalFilter
                : !filterDatum && materiaalFilterMatters ? materiaalFilter
                : filterDatum && !materiaalFilterMatters ? filterDatum
                : !filterDatum && !materiaalFilterMatters ? false : false;
    datum en matters -> datum && filter
    datum niet en matters wel -> filter
    datum wel en matters niet -> datum
    datums niet en mattters niet -> false
     * 
     * 
     */

}
