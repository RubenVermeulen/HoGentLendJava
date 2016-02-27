package domein;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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

    //naam en aantal zijn verplicht
    public Materiaal(String naam, int aantal) {
        this.naam = naam;
        this.aantal = aantal;
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

    public long getId() {
        return id;
    }

    public Firma getFirma() {
        return firma;
    }

    public String getFoto() {
        return foto;
    }

    public String getNaam() {
        return naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public String getArtikelnummer() {
        return artikelnummer;
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

    public String getDoelgroepen() {
        return doelgroepen;
    }

    public String getLeergebieden() {
        return leergebieden;
    }

    public boolean containsFilter(String filter) {
        return firma.containsFilter(filter)
                || hasFilter(naam, filter)
                || hasFilter(beschrijving, filter)
                || hasFilter(artikelnummer, filter)
                || hasFilter(plaats, filter)
                || hasFilter(doelgroepen, filter)
                || hasFilter(leergebieden, filter);
    }
    
    private boolean hasFilter(String string, String filter){
        return string != null && string.toLowerCase().contains(filter.toLowerCase());
    }

    @Override
    public String toString() {
        return "Materiaal{" + "id=" + id + ", firma=" + firma + ", foto=" + foto + ", naam=" + naam + ", beschrijving=" + beschrijving + ", artikelnummer=" + artikelnummer + ", prijs=" + prijs + ", aantal=" + aantal + ", aantalOnbeschikbaar=" + aantalOnbeschikbaar + ", uitleenbaarheid=" + uitleenbaarheid + ", plaats=" + plaats + ", doelgroepen=" + doelgroepen + ", leergebieden=" + leergebieden + '}';
    }

    
}
