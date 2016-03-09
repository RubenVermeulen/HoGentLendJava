package domein.config;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
    
    private LocalTime standaardOphaaltijd;
    private LocalTime standaardIndientijd;
    
    public long getId() {
        
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalTime getStandaardOphaaltijd() {
        return standaardOphaaltijd;
    }

    public void setStandaardOphaaltijd(LocalTime standaardOphaaltijd) {
        this.standaardOphaaltijd = standaardOphaaltijd;
    }

    public LocalTime getStandaardIndoentijd() {
        return standaardIndientijd;
    }

    public void setStandaardIndientijd(LocalTime standaardIndientijd) {
        this.standaardIndientijd = standaardIndientijd;
    }

    public void applyvView(ConfigView view){
        standaardOphaaltijd = view.getStandaardOphaaltijd();
        standaardIndientijd = view.getStandaardIndientijd();
    }
}
