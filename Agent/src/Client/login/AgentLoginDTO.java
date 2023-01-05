package Client.login;

public class AgentLoginDTO {

    private String userName;

    private String AlliesTeam;

    private Integer ThreadNumber;

    private Integer MissionSize;

    public AgentLoginDTO(String userName, String alliesTeam, Integer threadNumber, Integer missionSize) {
        this.userName = userName;
        AlliesTeam = alliesTeam;
        ThreadNumber = threadNumber;
        MissionSize = missionSize;
    }

    public String getAlliesTeam() {
        return AlliesTeam;
    }

    public Integer getThreadNumber() {
        return ThreadNumber;
    }

    public Integer getMissionSize() {
        return MissionSize;
    }

    public String getUserName() {
        return userName;
    }
}
