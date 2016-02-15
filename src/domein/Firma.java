/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author ruben
 */

@Entity
@Table(name = "firmas")
public class Firma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String naam;
    private String email;

    public Firma() {
    }

    public Firma(String naam, String email) {
        this.naam = naam;
        this.email = email;
    }

    public String getNaam() {
        return naam;
    }

    public String getEmail() {
        return email;
    }
    
    
}
