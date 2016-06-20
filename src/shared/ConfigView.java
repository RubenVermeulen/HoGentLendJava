package shared;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConfigView {
    private LocalDateTime standaardOphaaltijd;
    private LocalDateTime standaardIndientijd;
    private String standaardOphaalDag;
    private String standaardIndienDag;
    private int leentermijn;

    public ConfigView(LocalDateTime standaardOphaaltijd, LocalDateTime standaardIndientijd, String standaardOphaalDag, String standaardIndienDag, int leentermijn) {
        this.standaardOphaaltijd = standaardOphaaltijd;
        this.standaardIndientijd = standaardIndientijd;
        this.standaardOphaalDag = standaardOphaalDag;
        this.standaardIndienDag = standaardIndienDag;
        this.leentermijn = leentermijn;
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
    
    
}
