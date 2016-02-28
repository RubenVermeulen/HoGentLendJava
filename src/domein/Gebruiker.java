package domein;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "gebruikers")
public class Gebruiker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String voornaam;
    private String achternaam;
    private String email;
    private String paswoord;
    private boolean hoofdbeheerder;
    private boolean beheerder;

    protected Gebruiker() {
        // default constructor for jpa
    }

    public Gebruiker(String voornaam, String achternaam, String email, String paswoord, boolean hoofdbeheerder, boolean beheerder) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.email = email;
        this.paswoord = paswoord;
        this.hoofdbeheerder = hoofdbeheerder;
        this.beheerder = beheerder;
    }

    public long getId() {
        return id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaswoord() {
        return paswoord;
    }

    public boolean isHoofdbeheerder() {
        return hoofdbeheerder;
    }

    public void setHoofdbeheerder(boolean hoofdbeheerder) {
        this.hoofdbeheerder = hoofdbeheerder;
    }

    public boolean isBeheerder() {
        return beheerder;
    }

    public void setBeheerder(boolean beheerder) {
        this.beheerder = beheerder;
    }
    
    
}
