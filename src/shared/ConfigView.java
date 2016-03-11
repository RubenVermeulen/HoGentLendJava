package shared;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConfigView {
    private LocalTime standaardOphaaltijd;
    private LocalTime standaardIndientijd;
    private String standaardOphaalDag;
    private String standaardIndienDag;
    private int leentermijn;

    public ConfigView(LocalTime standaardOphaaltijd, LocalTime standaardIndientijd, String standaardOphaalDag, String standaardIndienDag, int leentermijn) {
        this.standaardOphaaltijd = standaardOphaaltijd;
        this.standaardIndientijd = standaardIndientijd;
        this.standaardOphaalDag = standaardOphaalDag;
        this.standaardIndienDag = standaardIndienDag;
        this.leentermijn = leentermijn;
    }

    public LocalTime getStandaardOphaaltijd() {
        return standaardOphaaltijd;
    }

    public void setStandaardOphaaltijd(LocalTime standaardOphaaltijd) {
        this.standaardOphaaltijd = standaardOphaaltijd;
    }

    public LocalTime getStandaardIndientijd() {
        return standaardIndientijd;
    }

    public void setStandaardIndientijd(LocalTime standaardIndientijd) {
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
