/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import domein.reservatie.Reservatie;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Xander
 */
public class ReservatieLijnView {

    private Long id;

    private LocalDateTime ophaalmoment;
    private LocalDateTime indienmoment;

    private MateriaalView materiaal;
    private int aantal;

    public ReservatieLijnView(Long id, LocalDateTime ophaalmoment, LocalDateTime indienmoment, MateriaalView materiaal, int aantal) {
        this.id = id;
        this.ophaalmoment = ophaalmoment;
        this.indienmoment = indienmoment;
        this.materiaal = materiaal;
        this.aantal = aantal;
    }

    public ReservatieLijnView(LocalDateTime ophaalmoment, LocalDateTime indienmoment, MateriaalView materiaal, int aantal) {
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

    public MateriaalView getMateriaal() {
        return materiaal;
    }

    public void setMateriaal(MateriaalView materiaal) {
        this.materiaal = materiaal;
    }

    public int getAantal() {
        return aantal;
    }

    public void setAantal(int aantal) {
        this.aantal = aantal;
    }

    public String getOphaalmomentAlsString() {
        return formatLocalDateTime(ophaalmoment);
    }

    public String getIndienmomentAlsString() {
        return formatLocalDateTime(indienmoment);
    }

    private String formatLocalDateTime(LocalDateTime ldt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm");
        return ldt.format(formatter);
    }

}
