package ServerDTOs.Uboat;

public class TableDataObject {
    String allyName , agentsNum , missionSize,readyStatus;


    public TableDataObject(String allyName, String agentsNum, String missionSize, String readyStatus) {
        this.allyName = allyName;
        this.agentsNum = agentsNum;
        this.missionSize = missionSize;
        this.readyStatus = readyStatus;
    }

    public String getAllyName() {
        return allyName;
    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }

    public String getAgentsNum() {
        return agentsNum;
    }

    public void setAgentsNum(String agentsNum) {
        this.agentsNum = agentsNum;
    }

    public String getMissionSize() {
        return missionSize;
    }

    public void setMissionSize(String missionSize) {
        this.missionSize = missionSize;
    }

    public String getReadyStatus() {
        return readyStatus;
    }

    public void setReadyStatus(String readyStatus) {
        this.readyStatus = readyStatus;
    }

    @Override
    public String toString() {
        return "TableDataObject{" +
                "allyName='" + allyName + '\'' +
                ", agentsNum='" + agentsNum + '\'' +
                ", missionSize='" + missionSize + '\'' +
                ", readyStatus='" + readyStatus + '\'' +
                '}';
    }
}

