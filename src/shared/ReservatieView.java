/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collector;
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
    private List<GereserveerdMateriaalView> gereserveerdeMaterialen;

    public ReservatieView(long id, String lener, LocalDateTime ophaalmoment, LocalDateTime indienmoment, List<GereserveerdMateriaalView> gereserveerdeMaterialen) {
        this.id = id;
        this.lener = lener;
        this.ophaalmoment = ophaalmoment;
        this.indienmoment = indienmoment;
        this.gereserveerdeMaterialen = gereserveerdeMaterialen;
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

    public List<GereserveerdMateriaalView> getGereserveerdeMaterialen() {
        return gereserveerdeMaterialen;
    }

    public void setGereserveerdeMaterialen(List<GereserveerdMateriaalView> gereserveerdeMaterialen) {
        this.gereserveerdeMaterialen = gereserveerdeMaterialen;
    }
    
    public String gereserveerdeMaterialenToString(){
        String s = "";
        return gereserveerdeMaterialen.stream().map(m -> m.getMateriaal().getNaam()).collect(Collectors.joining(", "));
    }
    
}
