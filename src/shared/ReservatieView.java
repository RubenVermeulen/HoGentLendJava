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
    private String emailLener;
    private LocalDateTime ophaalmoment;
    private LocalDateTime indienmoment;
    private LocalDateTime reservatiemoment;
    private String ophaalmomentAlsString;
    private String indienmomentAlsString;
    private String reservatiemomentAlsString;
    private String reservatieLijnenAlsString;
    private boolean opgehaald;
    private String conflict;
    private List<ReservatieLijnView> reservatieLijnen;

    public ReservatieView(long id, String lener, String emailLener, LocalDateTime ophaalmoment, LocalDateTime indienmoment, LocalDateTime reservatiemoment, boolean opgehaald, List<ReservatieLijnView> gereserveerdeMaterialen) {
        this.id = id;
        this.lener = lener;
        this.emailLener = emailLener;
        this.ophaalmoment = ophaalmoment;
        this.indienmoment = indienmoment;
        this.reservatiemoment = reservatiemoment;
        this.opgehaald = opgehaald;
        this.reservatieLijnen = gereserveerdeMaterialen;
        this.reservatieLijnenAlsString = reservatieLijnenToString(reservatieLijnen);

        this.ophaalmomentAlsString = formatLocalDateTime(ophaalmoment);
        this.indienmomentAlsString = formatLocalDateTime(indienmoment);
        this.reservatiemomentAlsString = formatLocalDateTime(reservatiemoment);
    }

    public ReservatieView(String emailLener, LocalDateTime ophaalmoment, LocalDateTime indienmoment,
            LocalDateTime reservatiemoment, List<ReservatieLijnView> reservatieLijnen) {
        this.emailLener = emailLener;
        this.ophaalmoment = ophaalmoment;
        this.indienmoment = indienmoment;
        this.reservatiemoment = reservatiemoment;
        this.reservatieLijnen = reservatieLijnen;

        this.ophaalmomentAlsString = formatLocalDateTime(ophaalmoment);
        this.indienmomentAlsString = formatLocalDateTime(indienmoment);
        this.reservatiemomentAlsString = formatLocalDateTime(reservatiemoment);
        this.opgehaald = false;
    }

    public String getOphaalmomentAlsString() {
        return ophaalmomentAlsString;
    }

    public String getIndienmomentAlsString() {
        return indienmomentAlsString;
    }

    public String getReservatiemomentAlsString() {
        return reservatiemomentAlsString;
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
        this.ophaalmomentAlsString = formatLocalDateTime(ophaalmoment);
    }

    public LocalDateTime getIndienmoment() {
        return indienmoment;
    }

    public void setIndienmoment(LocalDateTime indienmoment) {
        this.indienmoment = indienmoment;
        this.indienmomentAlsString = formatLocalDateTime(indienmoment);
    }

    public LocalDateTime getReservatiemoment() {
        return reservatiemoment;
    }

    public void setReservatiemoment(LocalDateTime reservatiemoment) {
        this.reservatiemoment = reservatiemoment;
        this.reservatiemomentAlsString = formatLocalDateTime(reservatiemoment);
    }

    public boolean isOpgehaald() {
        return opgehaald;
    }

    public void setOpgehaald(boolean opgehaald) {
        this.opgehaald = opgehaald;
    }

    public List<ReservatieLijnView> getReservatieLijnen() {
        return reservatieLijnen;
    }

    public void setReservatieLijnen(List<ReservatieLijnView> gereserveerdeMaterialen) {
        this.reservatieLijnen = gereserveerdeMaterialen;
    }

    public String reservatieLijnenToString() {
        String s = "";
        return reservatieLijnen.stream().map(m -> m.getMateriaal().getNaam()).collect(Collectors.joining(", "));
    }

    private String reservatieLijnenToString(List<ReservatieLijnView> rl) {
        String s = "";
        return rl.stream().map(m -> m.getMateriaal().getNaam()).collect(Collectors.joining(", "));
    }

    private String formatLocalDateTime(LocalDateTime ldt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");
        return ldt.format(formatter);
    }

    public String getEmailLener() {
        return emailLener;
    }

    public void setEmailLener(String emailLener) {
        this.emailLener = emailLener;
    }
    
    //is nodig voor de MainMenuFrameController - reservatielijst
    public String getConflict(){
        return conflict;
    }
    
    public void setConflict(boolean conflict){
        if(conflict)
            this.conflict="!!!";
        else
            this.conflict="";
    }

    @Override
    public String toString() {
        return id + "  " + lener + "  " + emailLener + "  " + ophaalmoment + "  " + indienmoment + "  " + reservatiemoment + "  " + ophaalmomentAlsString + "  "
                + indienmomentAlsString + "  " + reservatiemomentAlsString + "  " + reservatieLijnenAlsString + "  " + opgehaald + "  "
                + reservatieLijnen;
    }
}
