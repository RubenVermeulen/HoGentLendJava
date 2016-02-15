/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.util.List;

/**
 *
 * @author Xander
 */
public class MateriaalView {
    
    String naam;
    String fotoUrl;
    String omschrijving;
    String artikelNummer;
    double prijs;
    int aantal;
    int aantalOnbeschikbaar;
    boolean uitleenbaarheid;
    String plaats;
    String Firma;
    String emailFirma;
    List<String> Doelgroepen;
    List<String> Leergebieden;

    public MateriaalView(String naam, int aantal) {
        this.naam = naam;
        this.aantal = aantal;
    }
    
    
    //eerst maak je MateriaalView aan met naam en aantal (deze zijn verplicht)
    //via setters kan je mogelijk verdere gegevens aanvullen
    //setters geven het object MateriaalView zelf terug, zodat we aan setter-chaning kunnen doen
    
    public MateriaalView setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
        return this;
    }

    public MateriaalView setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
        return this;
    }

    public MateriaalView setArtikelNummer(String artikelNummer) {
        this.artikelNummer = artikelNummer;
        return this;
    }

    public MateriaalView setPrijs(double prijs) {
        this.prijs = prijs;
        return this;
    }

    public MateriaalView setAantalOnbeschikbaar(int aantalOnbeschikbaar) {
        this.aantalOnbeschikbaar = aantalOnbeschikbaar;
        return this;
    }

    public MateriaalView setUitleenbaarheid(boolean uitleenbaarheid) {
        this.uitleenbaarheid = uitleenbaarheid;
        return this;
    }

    public MateriaalView setPlaats(String plaats) {
        this.plaats = plaats;
        return this;
    }

    public MateriaalView setFirma(String Firma) {
        this.Firma = Firma;
        return this;
    }

    public MateriaalView setEmailFirma(String emailFirma) {
        this.emailFirma = emailFirma;
        return this;
    }

    public MateriaalView setDoelgroepen(List<String> Doelgroepen) {
        this.Doelgroepen = Doelgroepen;
        return this;
    }

    public MateriaalView setLeergebieden(List<String> Leergebieden) {
        this.Leergebieden = Leergebieden;
        return this;
    }
    
}
