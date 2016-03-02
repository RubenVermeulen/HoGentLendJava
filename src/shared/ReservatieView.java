/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Xander
 */
public class ReservatieView {
    
    private long id;
    private String lener;
    private LocalDateTime ophaalmoment;
    private LocalDateTime indienmoment;
    private String ophaalmomentAlsString;
    private String indienmomentAlsString;
    private String reservatieLijnenAlsString;
    private List<ReservatieLijnView> reservatieLijnen;

    public ReservatieView(long id, String lener, LocalDateTime ophaalmoment, LocalDateTime indienmoment, List<ReservatieLijnView> gereserveerdeMaterialen) {
        this.id = id;
        this.lener = lener;
        this.ophaalmoment = ophaalmoment;
        this.indienmoment = indienmoment;
        this.reservatieLijnen = gereserveerdeMaterialen;
        this.reservatieLijnenAlsString = reservatieLijnenToString(reservatieLijnen);
        
        this.ophaalmomentAlsString = formatLocalDateTime(ophaalmoment);
        this.indienmomentAlsString = formatLocalDateTime(indienmoment);
    }

    public String getOphaalmomentAlsString() {
        return ophaalmomentAlsString;
    }

    public String getIndienmomentAlsString() {
        return indienmomentAlsString;
    }

    public String getReservatieLijnenAlsString() {
        return reservatieLijnenAlsString;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLener() {
        return lener;
    }

    public void setLener(String lener) {
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

    public List<ReservatieLijnView> getReservatieLijnen() {
        return reservatieLijnen;
    }

    public void setReservatieLijnen(List<ReservatieLijnView> gereserveerdeMaterialen) {
        this.reservatieLijnen = gereserveerdeMaterialen;
    }
    
    public String reservatieLijnenToString(){
        String s = "";
        return reservatieLijnen.stream().map(m -> m.getMateriaal().getNaam()).collect(Collectors.joining(", "));
    }
    
    private String reservatieLijnenToString(List<ReservatieLijnView> rl){
        String s = "";
        return rl.stream().map(m -> m.getMateriaal().getNaam()).collect(Collectors.joining(", "));
    }
    
    private String formatLocalDateTime(LocalDateTime ldt){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");
        System.out.println(ldt.format(formatter));
        return ldt.format(formatter);
    }
    
}
