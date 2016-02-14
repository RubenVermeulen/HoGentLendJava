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
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ruben
 */

@Entity
@Table(name = "gebruikers")
@NamedQuery(
    name="findAllGebruikers",
    query="SELECT g FROM Gebruiker g"
)
public class Gebruiker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String voornaam;
    private String achternaam;
    private String email;
    private String paswoord;

    
    protected Gebruiker() {
        // default constructor for jpa
    }

    public Gebruiker(String voornaam, String achternaam, String email, String paswoord) {
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.email = email;
        this.paswoord = paswoord;
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
}
