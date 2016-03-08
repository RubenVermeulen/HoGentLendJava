package domein.gebruiker;

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
    private boolean lector;

    protected Gebruiker() {
        // default constructor for jpa
    }

    public Gebruiker(String voornaam, String achternaam, String email, String paswoord, boolean hoofdbeheerder, boolean beheerder, boolean lector) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        setEmail(email);
        this.paswoord = paswoord;
        this.hoofdbeheerder = hoofdbeheerder;
        this.beheerder = beheerder;
        this.lector = lector;
    }

    protected long getId() {
        return id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    protected void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    protected void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public String getEmail() {
        return email;
    }

    protected void setEmail(String email) {
        if (email != null) {
            email = email.toLowerCase();
        }
        this.email = email;
    }

    protected String getPaswoord() {
        return paswoord;
    }

    public boolean isHoofdbeheerder() {
        return hoofdbeheerder;
    }

    protected void setHoofdbeheerder(boolean hoofdbeheerder) {
        this.hoofdbeheerder = hoofdbeheerder;
    }

    public boolean isBeheerder() {
        return beheerder;
    }

    protected void setBeheerder(boolean beheerder) {
        this.beheerder = beheerder;
    }

    public boolean isLector() {
        return lector;
    }

    public void setLector(boolean lector) {
        this.lector = lector;
    }

    @Override
    public String toString() {
        return "Gebruiker{" + "id=" + id + ", voornaam=" + voornaam + ", achternaam=" + achternaam + ", email=" + email + ", paswoord=" + paswoord + ", hoofdbeheerder=" + hoofdbeheerder + ", beheerder=" + beheerder + ", lector=" + lector + '}';
    }

}
