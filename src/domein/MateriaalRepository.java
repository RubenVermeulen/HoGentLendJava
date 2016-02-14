/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.ArrayList;

/**
 *
 * @author alexa_000
 */
public class MateriaalRepository {
    ArrayList<Materiaal> materialen;
    
    
   public void materialenToevoegenInBulk(ArrayList<Materiaal> materialen){
        materialen.stream().forEach((materiaal) -> {
            this.materialen.add(materiaal);
        });
        
        
    }

    
    
    
    
    
    
    
    
}
