package ServerDTOs.Agent;

public class AgentDTO {

    private final Integer agentThreadNumber;
    private final String agentUserName;
    private Integer missionsToTakeEveryTimeReady;
    private String allyTeam;
    private String BattleField;
    private String Username;
    private boolean runningOrIdle;
    private String difficulty;
    private String alliesNeeded;
    private String alliesJoined;

    private boolean belongToContest,allyDisconnected;

    public AgentDTO(Integer missionsToTakeEveryTimeReady,
                    String allyTeam, String battleField, String username, boolean runningOrIdle,
                    String difficulty, String alliesNeeded, String alliesJoined, Integer threadNumber,
                    String agentName,boolean belongToContest,Boolean allyDisconnected) {
        this.missionsToTakeEveryTimeReady = missionsToTakeEveryTimeReady;
        this.allyTeam = allyTeam;
        BattleField = battleField;
        Username = username;
        this.runningOrIdle = runningOrIdle;
        this.difficulty = difficulty;
        this.alliesNeeded = alliesNeeded;
        this.alliesJoined = alliesJoined;
        this.agentThreadNumber = threadNumber;
        this.agentUserName = agentName;
        this.belongToContest=belongToContest;
        this.allyDisconnected=allyDisconnected;
    }


    public boolean isBelongToContest() {
        return belongToContest;
    }

    public Integer getMissionsToTakeEveryTimeReady() {
        return missionsToTakeEveryTimeReady;
    }

    public String getAllyTeam() {
        return allyTeam;
    }

    public String getBattleField() {
        return BattleField;
    }

    public String getUsername() {
        return Username;
    }

    public boolean getRunningOrIdle() {
        return runningOrIdle;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getAlliesNeeded() {
        return alliesNeeded;
    }

    public String getAlliesJoined() {
        return alliesJoined;
    }

    public Integer getAgentThreadNumber() {
        return agentThreadNumber;
    }

    public AgentDTO returnDataPack(){
        return this;
    }

    public String getAgentUserName() {
        return agentUserName;
    }

    @Override
    public String toString() {
        return "AgentDTO{" +
                "agentThreadNumber=" + agentThreadNumber +
                ", agentUserName='" + agentUserName + '\'' +
                ", missionsToTakeEveryTimeReady=" + missionsToTakeEveryTimeReady +
                ", allyTeam='" + allyTeam + '\'' +
                ", BattleField='" + BattleField + '\'' +
                ", Username='" + Username + '\'' +
                ", runningOrIdle=" + runningOrIdle +
                ", difficulty='" + difficulty + '\'' +
                ", alliesNeeded='" + alliesNeeded + '\'' +
                ", alliesJoined='" + alliesJoined + '\'' +
                '}';
    }
}
