/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author ruben
 */

@Entity
@Table(name = "materialen")
public class Materiaal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne
    private Firma firma;
    
    private String foto;
    private String naam;
    private String beschrijving;
    private String artikelnummer; // kan letters bevatten
    private String prijs;
    private int aantal;
    private int aantalOnbeschikbaar;
    private boolean uitleenbaarheid; 
    private String plaats;
    private String doelgroepen; 
    private String leergebieden; 
    
    public Materiaal(long id, Firma firma, String foto, String naam, String beschrijving, String artikelnummer, String prijs,
            int aantal, int aantalOnbeschikbaar, boolean uitleenbaarheid, String plaats, String doelgroepen, String leergebieden
    ) {
        this.id = id;
        this.firma = firma;
        this.foto = foto;
        this.naam = naam;
        this.beschrijving=beschrijving;
        this.artikelnummer=artikelnummer;
        this.prijs=prijs;
        this.aantal=aantal;
        this.aantalOnbeschikbaar=aantalOnbeschikbaar;
        this.uitleenbaarheid=uitleenbaarheid;
        this.plaats=plaats;
        this.doelgroepen=doelgroepen;
        this.leergebieden=leergebieden;
    }

    
}
