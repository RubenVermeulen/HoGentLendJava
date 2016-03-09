package shared;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConfigView {
    private LocalTime standaardOphaaltijd;
    private LocalTime standaardIndientijd;

    public ConfigView(LocalTime standaardOphaaltijd, LocalTime standaardIndientijd) {
        this.standaardOphaaltijd = standaardOphaaltijd;
        this.standaardIndientijd = standaardIndientijd;
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
}
