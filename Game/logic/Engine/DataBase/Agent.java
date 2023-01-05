package DataBase;

import missions.results.MissionResults;

import java.util.ArrayList;

public class Agent {

    private String userName;

    private String AlliesTeam;

    private Integer ThreadNumber;

    private Integer MissionSize;
    private Boolean readyToCompete, allyDisconnected;

    private Long tasksCompleted;

    private ArrayList<MissionResults> candidates;

    private boolean belongToContest;

    public Agent(String userName) {
        this.userName = userName;
    }

    public void setAgent(String userName, String alliesTeam, Integer threadNumber, Integer missionSize) {
        this.userName = userName;
        AlliesTeam = alliesTeam;
        ThreadNumber = threadNumber;
        MissionSize = missionSize;
        candidates = new ArrayList<>();
        allyDisconnected=false;
        this.tasksCompleted = 0L;
    }

    public Boolean getAllyDisconnected() {
        return allyDisconnected;
    }

    public void setAllyDisconnected(Boolean allyDisconnected) {
        this.allyDisconnected = allyDisconnected;
    }

    public boolean isBelongToContest() {
        return belongToContest;
    }

    public void setBelongToContest(boolean belongToContest) {
        this.belongToContest = belongToContest;
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

    public void setReadyToCompete(boolean status) {
        this.readyToCompete = status;
    }

    public Long getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(Long tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "userName='" + userName + '\'' +
                ", AlliesTeam='" + AlliesTeam + '\'' +
                ", ThreadNumber=" + ThreadNumber +
                ", MissionSize=" + MissionSize +
                ", readyToCompete=" + readyToCompete +
                '}';
    }

    public ArrayList<MissionResults> getAllCandidates() { // S added
        return candidates;
    }

    public void setAlliesTeam(String alliesTeam) {
        AlliesTeam = alliesTeam;
    }
}