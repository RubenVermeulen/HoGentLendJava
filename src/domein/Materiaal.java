package domein;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.CascadeOnDelete;
import shared.MateriaalView;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "materiaal_doelgroepen", joinColumns = @JoinColumn(name = "materiaal_id"),
            inverseJoinColumns = @JoinColumn(name = "doelgroep_id"))
    private List<Groep> doelgroepen = new ArrayList();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "materiaal_leergebieden", joinColumns = @JoinColumn(name = "materiaal_id"),
            inverseJoinColumns = @JoinColumn(name = "leergebied_id"))
    private List<Groep> leergebieden = new ArrayList();

    ;
    
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

    public Materiaal setDoelgroepen(List<Groep> doelgroepen) {
        this.doelgroepen = doelgroepen;

        /* if (groepen != null){
            List<Groep> tgroepen = getLeergebieden();
            tgroepen.addAll(doelgroepen);
            setGroepen(tgroepen);
        }else{
            setGroepen(doelgroepen);
        }*/
        return this;
    }

    public Materiaal setLeergebieden(List<Groep> leergebieden) {
        this.leergebieden = leergebieden;
        return this;
        /*   if (groepen != null){
            List<Groep> tgroepen = getDoelgroepen();
            tgroepen.addAll(leergebieden);
            setGroepen(tgroepen);
        }else{
            setGroepen(leergebieden);
        }
        return this;*/
    }

    public List<Groep> getDoelgroepen() {
        return doelgroepen;
        //if (groepen == null) return null;
        // return groepen.stream().filter(g -> !g.isIsLeerGroep()).collect(Collectors.toList());
    }

    public List<Groep> getLeergebieden() {
        return leergebieden;
        // if (groepen == null) return null;
        //  return groepen.stream().filter(g -> g.isIsLeerGroep()).collect(Collectors.toList());
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

    public boolean containsFilter(String filter) {
        boolean hasGroepFilter = false;
        if (doelgroepen != null){
            for(Groep g : doelgroepen){
                hasGroepFilter = g.containsFilter(filter);
                if (hasGroepFilter) break;
            }
        }
        if (!hasGroepFilter){
            if (doelgroepen != null){
                for(Groep g : doelgroepen){
                    hasGroepFilter = g.containsFilter(filter);
                    if (hasGroepFilter) break;
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
        return "Materiaal{" + "id=" + id + ", firma=" + firma + ", foto=" + foto + ", naam=" + naam + ", beschrijving=" + beschrijving + ", artikelnummer=" + artikelnummer + ", prijs=" + prijs + ", aantal=" + aantal + ", aantalOnbeschikbaar=" + aantalOnbeschikbaar + ", uitleenbaarheid=" + uitleenbaarheid + ", plaats=" + plaats + ", doelgroepen=" + doelgroepen + ", leergebieden=" + leergebieden + '}';
    }

    public MateriaalView toMateriaalView() {
        MateriaalView mv = new MateriaalView(naam, aantal);
        mv.setFotoUrl(foto)
                .setOmschrijving(beschrijving)
                .setArtikelNummer(artikelnummer)
                .setAantalOnbeschikbaar(aantalOnbeschikbaar)
                .setUitleenbaarheid(uitleenbaarheid)
                .setPlaats(plaats)
                .setFirma(firma == null ? null : firma.getNaam())
                .setEmailFirma(firma == null ? null : firma.getEmail())
                .setDoelgroepen(groepListToString(doelgroepen))
                .setLeergebieden(groepListToString(leergebieden))
                .setPrijs(prijs)
                .setId(Long.max(id, 0));

        return mv;
    }

    private List<String> groepListToString(List<Groep> groepen) {
        
        List<String> lijst = new ArrayList<>();
        
        for(Groep g : groepen){
            lijst.add(g.getGroep());
        }
        return lijst;
        
        //is niet compatibel met JPA 2.1
        //return groepen.stream().map(g -> g.getGroep()).collect(Collectors.toList());
       
    }

}
