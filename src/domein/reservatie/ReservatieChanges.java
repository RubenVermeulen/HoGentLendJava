package domein.reservatie;

import java.util.List;
import shared.ReservatieLijnView;

public class ReservatieChanges {
    
    private List<ReservatieLijnView> wijzigenLijnen;
    private List<ReservatieLijnView> toevoegenLijnen;
    private List<Long> verwijderenLijnIds;
    private Reservatie r;

    public ReservatieChanges(Reservatie r, List<ReservatieLijnView> wijzigenLijnen, List<ReservatieLijnView> toevoegenLijnen, List<Long> verwijderenLijnIds) {
        this.wijzigenLijnen = wijzigenLijnen;
        this.toevoegenLijnen = toevoegenLijnen;
        this.verwijderenLijnIds = verwijderenLijnIds;
        this.r = r;
    }

    public List<ReservatieLijnView> getWijzigenLijnen() {
        return wijzigenLijnen;
    }

    public List<ReservatieLijnView> getToevoegenLijnen() {
        return toevoegenLijnen;
    }

    public List<Long> getVerwijderenLijnIds() {
        return verwijderenLijnIds;
    }
    
    public Reservatie getReservatie(){
        return r;
    }
}
