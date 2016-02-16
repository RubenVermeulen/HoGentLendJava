/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import javax.persistence.Column;
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
    
    // We hanteren het datatype text in de database zodat we niet zullen worden gehindered door een max aantal karakters.
    @Column(columnDefinition = "text")
    private String beschrijving;
    
    private String artikelnummer; // kan letters bevatten
    private double prijs;
    private int aantal;
    private int aantalOnbeschikbaar;
    private boolean uitleenbaarheid; 
    private String plaats;
    private String doelgroepen; 
    private String leergebieden; 
    
    protected Materiaal() {
        // default constructor for jpa
    }
    
    public Materiaal(long id, Firma firma, String foto, String naam, String beschrijving, String artikelnummer, double prijs,
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

    
    //naam en aantal zijn verplicht
    public Materiaal(String naam, int aantal) {
        this.naam = naam;
        this.aantal = aantal;
    }

    public Materiaal setId(long id) {
        this.id = id;
        return this;
    }

    public Materiaal setFirma(Firma firma) {
        this.firma = firma;
        return this;
    }

    public Materiaal setFoto(String foto) {
        this.foto = foto;
        return this;
    }

    public Materiaal setNaam(String naam) {
        this.naam = naam;
        return this;
    }

    public Materiaal setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
        return this;
    }

    public Materiaal setArtikelnummer(String artikelnummer) {
        this.artikelnummer = artikelnummer;
        return this;
    }

    public Materiaal setPrijs(double prijs) {
        this.prijs = prijs;
        return this;
    }

    public Materiaal setAantal(int aantal) {
        this.aantal = aantal;
        return this;
    }

    public Materiaal setAantalOnbeschikbaar(int aantalOnbeschikbaar) {
        this.aantalOnbeschikbaar = aantalOnbeschikbaar;
        return this;
    }

    public Materiaal setUitleenbaarheid(boolean uitleenbaarheid) {
        this.uitleenbaarheid = uitleenbaarheid;
        return this;
    }

    public Materiaal setPlaats(String plaats) {
        this.plaats = plaats;
        return this;
    }

    public Materiaal setDoelgroepen(String doelgroepen) {
        this.doelgroepen = doelgroepen;
        return this;
    }

    public Materiaal setLeergebieden(String leergebieden) {
        this.leergebieden = leergebieden;
        return this;
    }
    
    

    
    
    

    
}
