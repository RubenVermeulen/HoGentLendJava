package shared;

import java.time.LocalDateTime;

public class ConfigView {
    private LocalDateTime standaardOphaalmoment;
    private LocalDateTime standaardIndienmoment;

    public ConfigView(LocalDateTime standaardOphaalmoment, LocalDateTime standaardIndienmoment) {
        this.standaardOphaalmoment = standaardOphaalmoment;
        this.standaardIndienmoment = standaardIndienmoment;
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
}
