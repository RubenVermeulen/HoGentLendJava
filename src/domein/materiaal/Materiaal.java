package domein.materiaal;

import domein.groep.Groep;
import domein.firma.Firma;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import shared.MateriaalView;

@Entity
@Table(name = "materialen")
public class Materiaal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Firma firma;

    @Lob
    private byte[] fotoBytes;
    
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "materiaal_doelgroepen", joinColumns = @JoinColumn(name = "materiaal_id"),
            inverseJoinColumns = @JoinColumn(name = "doelgroep_id"))
    private List<Groep> doelgroepen = new ArrayList();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "materiaal_leergebieden", joinColumns = @JoinColumn(name = "materiaal_id"),
            inverseJoinColumns = @JoinColumn(name = "leergebied_id"))
    private List<Groep> leergebieden = new ArrayList();
    
    protected Materiaal() {
        // default constructor for jpa
    }

    //naam en aantal zijn verplicht
    public Materiaal(String naam, int aantal) {
        setNaam(naam);
        setAantal(aantal);
    }

    public boolean containsFilter(String filter) {
        if (filter == null || filter.isEmpty()){
            return true;
        }
        boolean hasGroepFilter = false;
        if (doelgroepen != null) {
            for (Groep g : doelgroepen) {
                hasGroepFilter = g.containsFilter(filter);
                if (hasGroepFilter) {
                    break;
                }
            }
        }
        if (!hasGroepFilter) {
            if (leergebieden != null) {
                for (Groep g : leergebieden) {
                    hasGroepFilter = g.containsFilter(filter);
                    if (hasGroepFilter) {
                        break;
                    }
                }
            }
        }
        return hasGroepFilter || (firma != null && firma.containsFilter(filter))
                || hasFilter(naam, filter)
                || hasFilter(beschrijving, filter)
                || hasFilter(artikelnummer, filter)
                || hasFilter(plaats, filter);
    }

    private boolean hasFilter(String string, String filter) {
        return string != null && string.toLowerCase().contains(filter.toLowerCase());
    }

    @Override
    public String toString() {
        return "Materiaal{" + "id=" + id + ", firma=" + firma + ", naam=" + naam + ", beschrijving=" + beschrijving + ", artikelnummer=" + artikelnummer + ", prijs=" + prijs + ", aantal=" + aantal + ", aantalOnbeschikbaar=" + aantalOnbeschikbaar + ", uitleenbaarheid=" + uitleenbaarheid + ", plaats=" + plaats + ", doelgroepen=" + doelgroepen + ", leergebieden=" + leergebieden + '}';
    }

    /* BEGIN SETTERS */
    public final Materiaal setNaam(String naam) {
        if (naam == null || naam.trim().isEmpty()) {
            throw new IllegalArgumentException("De naam van een materiaal is verplicht.");
        }
        this.naam = naam;
        return this;
    }

    public Materiaal setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
        return this;
    }

    public Materiaal setFirma(Firma firma) {
        this.firma = firma;
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
    
    public Materiaal setFotoBytes(byte[] fotoBytes){
        this.fotoBytes = fotoBytes;
        return this;
    }

    public Materiaal setDoelgroepen(List<Groep> doelgroepen) {
        this.doelgroepen = doelgroepen;
        return this;
    }

    public Materiaal setLeergebieden(List<Groep> leergebieden) {
        this.leergebieden = leergebieden;
        return this;
    }

    public void setId(long id) {
        this.id = id;
    }

    /* EINDE  SETTERS */

 /* BEGIN GETTERS */
    public List<Groep> getDoelgroepen() {
        return doelgroepen;
    }

    public List<Groep> getLeergebieden() {
        return leergebieden;
    }

    public long getId() {
        return id;
    }

    public Firma getFirma() {
        return firma;
    }

    public byte[] getFotoBytes(){
        return fotoBytes;
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
    /* EINDE GETTERS */

}
