package domein;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "groepen")
public class Groep {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private String groep;
    private boolean isLeerGroep;
    
    protected Groep() {
        // default constructor for jpa
    }

    public Groep(String groep, boolean isLeerGroep) {
        setIsLeerGroep(isLeerGroep);
        setGroep(groep);
    }    

    public String getGroep() {
        return groep;
    }

    public void setGroep(String groep) {
        if (groep == null || groep.isEmpty()){
            throw new IllegalArgumentException("De naam is niet ingevuld.");
        }
        this.groep = groep;
    }

    public boolean isLeerGroep() {
        return isLeerGroep;
    }

    public void setIsLeerGroep(boolean isLeerGroep) {
        this.isLeerGroep = isLeerGroep;
    }

    public long getId() {
        return id;
    }    
    
    boolean containsFilter(String filter) {
        return hasFilter(groep, filter);
    }
    private boolean hasFilter(String string, String filter){
        return string != null && string.toLowerCase().contains(filter.toLowerCase());
    }     

    @Override
    public String toString() {
        return "Groep{" + "id=" + id + ", groep=" + groep + ", isLeerGroep=" + isLeerGroep + '}';
    }    
}
