package Main.Client;

public class AlliesInContestTableObject {

    private  String allyName;
    private String agentNumber;
    private  String missionSize;

    public AlliesInContestTableObject(String allyName, String agentNumber, String missionSize) {
        this.allyName = allyName;
        this.agentNumber = agentNumber;
        this.missionSize = missionSize;
    }

    public String getAllyName() {
        return allyName;
    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }

    public String getAgentNumber() {
        return agentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        this.agentNumber = agentNumber;
    }

    public String getMissionSize() {
        return missionSize;
    }

    public void setMissionSize(String missionSize) {
        this.missionSize = missionSize;
    }
}
