package domein.firma;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "firmas")
public class Firma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String naam;
    private String email;

    protected Firma() {
        // default for JPA
    }

    public Firma(String naam) {
        setNaam(naam);
    }

    public Firma(String naam, String email) {
        setNaam(naam);
        setEmail(email);
    }

    /**
     * Controleer of the filter voorkomt in de naam of het email adres.
     *
     * @param filter De te zoeken filter.
     * @return filter
     */
    public boolean containsFilter(String filter) {
        if (filter == null || filter.isEmpty()) {
            return true;
        }
        return hasFilter(email, filter) || hasFilter(naam, filter);
    }

    private boolean hasFilter(String string, String filter) {
        return string != null && string.toLowerCase().contains(filter.toLowerCase());
    }

    /**
     * @param email Een optioneel email adres.
     * @throws IllegalArgumentException indien het email adres bestaat maar niet
     * een geldig email is.
     */
    public final void setEmail(String email) {
        if (email != null && !email.trim().isEmpty() && !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new IllegalArgumentException("Geef een geldig e-mailadres op");
        }
        this.email = email;
    }

    /**
     *
     * @param naam De naam van de firma.
     * @throws IllegalArgumentException indien de naam leeg is.
     */
    public final void setNaam(String naam) {
        if (naam == null || naam.trim().isEmpty()) {
            throw new IllegalArgumentException("Firma naam is verplicht");
        }
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

    @Override
    public String toString() {
        return "Firma{" + "id=" + id + ", naam=" + naam + ", email=" + email + '}';
    }

}
