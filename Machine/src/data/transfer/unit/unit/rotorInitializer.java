package data.transfer.unit.unit;

public class rotorInitializer {
    private Integer rotorID;
    private String rotorStartingPos;

    public rotorInitializer(Integer rotorID, String rotorStartingPos) {
        this.rotorID = rotorID;
        this.rotorStartingPos = rotorStartingPos;
    }

    public Integer getRotorID() {
        return rotorID;
    }

    public String getRotorStartingPos() {
        return rotorStartingPos;
    }
}
