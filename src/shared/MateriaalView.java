/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import domein.groep.Groep;
import java.util.List;

/**
 *
 * @author Xander
 */
public class MateriaalView {

    private String naam;
    private String fotoUrl;
    private String omschrijving;
    private String artikelNummer;
    private double prijs;
    private int aantal;
    private int aantalOnbeschikbaar;
    private boolean uitleenbaarheid;
    private String plaats;
    private String firma;
    private String emailFirma;
    private List<String> doelgroepen;
    private List<String> leergebieden;
    private long id;
    private long firmaId;

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
        this.firma = Firma;
        return this;
    }

    public MateriaalView setEmailFirma(String emailFirma) {
        this.emailFirma = emailFirma;
        return this;
    }

    public MateriaalView setDoelgroepen(List<String> doelgroepen) {
        this.doelgroepen = doelgroepen;
        return this;
    }

    public MateriaalView setLeergebieden(List<String> leergebieden) {
        this.leergebieden = leergebieden;
        return this;
    }

    public MateriaalView setId(long id) {
        this.id = id;
        return this;
    }

    public MateriaalView setFirmaId(long firmaId) {
        this.firmaId = firmaId;
        return this;
    }

    public MateriaalView setNaam(String naam) {
        this.naam = naam;
        return this;
    }

    public MateriaalView setAantal(int aantal) {
        this.aantal = aantal;
        return this;
    }

    //getters
    public String getNaam() {
        return naam;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public String getArtikelNummer() {
        return artikelNummer;
    }

    public double getPrijs() {
        return prijs;
    }

    public int getAantal() {
        return aantal;
    }

    public int getAantalOnbeschikbaar() {
        return aantalOnbeschikbaar;
    }

    public boolean isUitleenbaarheid() {
        return uitleenbaarheid;
    }

    public String getPlaats() {
        return plaats;
    }

    public String getFirma() {
        return firma;
    }

    public String getEmailFirma() {
        return emailFirma;
    }

    public List<String> getDoelgroepen() {
        return doelgroepen;
    }

    public List<String> getLeergebieden() {
        return leergebieden;
    }

    public long getId() {
        return id;
    }

    public long getFirmaId() {
        return firmaId;
    }

    @Override
    public String toString() {
        return "MateriaalView{" + "naam=" + naam + ", fotoUrl=" + fotoUrl + ", omschrijving=" + omschrijving + ", artikelNummer=" + artikelNummer + ", prijs=" + prijs + ", aantal=" + aantal + ", aantalOnbeschikbaar=" + aantalOnbeschikbaar + ", uitleenbaarheid=" + uitleenbaarheid + ", plaats=" + plaats + ", firma=" + firma + ", emailFirma=" + emailFirma + ", doelgroepen=" + doelgroepen + ", leergebieden=" + leergebieden + ", id=" + id + '}';
    }

}
