/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.time.LocalDateTime;

/**
 *
 * @author Xander
 */
public class GereserveerdMateriaal {
    
    private LocalDateTime ophaalmoment;
    private LocalDateTime indienmoment;
    private Materiaal materiaal;
    private int aantal;

    public GereserveerdMateriaal(Materiaal materiaal, int aantal, LocalDateTime ophaalmoment, LocalDateTime indienmoment) {
        this.ophaalmoment = ophaalmoment;
        this.indienmoment = indienmoment;
        this.materiaal = materiaal;
        this.aantal = aantal;
    }
    
    
    
    
}
