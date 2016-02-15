/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.List;

/**
 *
 * @author Xander
 */
public class FirmaRepository {
    List<Firma> firmas;

    public FirmaRepository() {
    }
    
    public List<Firma> getFirmas(){
        return firmas;
    }
    
    public Firma geefFirma(String naam){
        
        for(Firma f : firmas){
            if (f.getNaam().equals(naam)){
                return f;
            }
        }
        
        return null;
    }
    
    //maakt nieuwe firma aan en returnt hem
    public Firma voegFirmaToe(String naam, String email){
        
        Firma firma = new Firma(naam, email);
        firmas.add(firma);
        return firma;
        
    }
    
}
