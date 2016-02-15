/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.ArrayList;
import shared.MateriaalView;

/**
 *
 * @author alexa_000
 */
public class MateriaalRepository {

    ArrayList<Materiaal> materialen;
    FirmaRepository firmas;

    public MateriaalRepository() {
        firmas = new FirmaRepository();
    }
    
    
    public void materialenToevoegenInBulk(ArrayList<Materiaal> materialen) {
        materialen.stream().forEach((materiaal) -> {
            this.materialen.add(materiaal);
        });

    }

    public void voegMateriaalToe(MateriaalView mv) {

        Materiaal materiaal = new Materiaal(mv.getNaam(), mv.getAantal());

        //check of opgegeven firma al bestaat in db
        Firma firma = firmas.geefFirma(mv.getFirma());
        if (firma == null) {
            firma = firmas.voegFirmaToe(mv.getFirma(), mv.getEmailFirma());
        }
        
        materiaal.setFoto(mv.getFotoUrl()).setBeschrijving(mv.getOmschrijving()).setArtikelnummer(mv.getArtikelNummer())
                .setPrijs(mv.getPrijs()).setAantalOnbeschikbaar(mv.getAantalOnbeschikbaar()).setUitleenbaarheid(mv.isUitleenbaarheid())
                .setPlaats(mv.getPlaats()).setFirma(firma);
        
        //TODO DOELGROEPEN, LEERGEBIEDEN
    }

}


