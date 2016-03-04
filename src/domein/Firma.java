package domein;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "firmas")
public class Firma{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String naam;
    private String email;

    public Firma() {
    }

    public Firma(String naam) {
        this.naam = naam;
    }
    
    public long getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email){
        this.email = email;
    }

    boolean containsFilter(String filter) {
        return hasFilter(email, filter) || hasFilter(naam, filter);
    }
    private boolean hasFilter(String string, String filter){
        return string != null && string.toLowerCase().contains(filter.toLowerCase());
    }
}
