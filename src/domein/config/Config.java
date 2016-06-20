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
    
    private LocalDateTime standaardOphaaltijd;
    private LocalDateTime standaardIndientijd;
    private String standaardOphaalDag;
    private String standaardIndienDag;
    private int leentermijn;
    
    public long getId() {
        
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStandaardOphaaltijd() {
        return standaardOphaaltijd;
    }

    public void setStandaardOphaaltijd(LocalDateTime standaardOphaaltijd) {
        this.standaardOphaaltijd = standaardOphaaltijd;
    }

    public LocalDateTime getStandaardIndientijd() {
        return standaardIndientijd;
    }

    public void setStandaardIndientijd(LocalDateTime standaardIndientijd) {
        this.standaardIndientijd = standaardIndientijd;
    }

    public String getStandaardOphaalDag() {
        return standaardOphaalDag;
    }

    public void setStandaardOphaalDag(String standaardOphaalDag) {
        this.standaardOphaalDag = standaardOphaalDag;
    }

    public String getStandaardIndienDag() {
        return standaardIndienDag;
    }

    public void setStandaardIndienDag(String standaardIndienDag) {
        this.standaardIndienDag = standaardIndienDag;
    }

    public int getLeentermijn() {
        return leentermijn;
    }

    public void setLeentermijn(int leentermijn) {
        this.leentermijn = leentermijn;
    }
    
    public void applyvView(ConfigView view){
        standaardOphaaltijd = view.getStandaardOphaaltijd();
        standaardIndientijd = view.getStandaardIndientijd();
        standaardOphaalDag = view.getStandaardOphaalDag();
        standaardIndienDag = view.getStandaardIndienDag();
        leentermijn = view.getLeentermijn();
    }
}
