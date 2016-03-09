package domein.config;

import java.time.LocalDateTime;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import shared.ConfigView;
import util.LocalDateTimeAttributeConverter;

@Entity
@Table(name = "config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    private LocalDateTime standaardOphaalmoment;
    private LocalDateTime standaardIndienmoment;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStandaardOphaalmoment() {
        return standaardOphaalmoment;
    }

    public void setStandaardOphaalmoment(LocalDateTime standaardOphaalmoment) {
        this.standaardOphaalmoment = standaardOphaalmoment;
    }

    public LocalDateTime getStandaardIndienmoment() {
        return standaardIndienmoment;
    }

    public void setStandaardIndienmoment(LocalDateTime standaardIndienmoment) {
        this.standaardIndienmoment = standaardIndienmoment;
    }
    
    public void applyvView(ConfigView view){
        standaardIndienmoment = view.getStandaardIndienmoment();
        standaardOphaalmoment = view.getStandaardOphaalmoment();
    }
}
