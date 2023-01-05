package missions.results;

public class MissionResults {
    private String candidate ;
    private String codeConfiguration;

    private String allieReResponsible ;
    private long encryptionTime, tasksCompleted;

    public MissionResults(String candidate, String codeConfiguration, long encryptionTime,String allyTeam) {
        this.candidate = candidate;
        this.codeConfiguration = codeConfiguration;
        this.encryptionTime = encryptionTime;
        this.allieReResponsible=allyTeam; // adapt the mission runnable to send it the Agent name + Allie Name
        this.tasksCompleted = 0L;
    }

    public String getCandidate() {
        return candidate;
    }

    public String getCodeConfiguration() {
        return codeConfiguration;
    }

    public long getEncryptionTime() {
        return encryptionTime;
    }

    public String getAllieResponsible() {
        return allieReResponsible;
    }

    public void setTasksCompleted(long tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public long getTasksCompleted() {
        return tasksCompleted;
    }

    public String getThreadResponsible() {
        return "nothing";
    }

    @Override
    public String toString() {
        return "MissionResults{" +
                "candidate='" + candidate + '\'' +
                ", codeConfiguration='" + codeConfiguration + '\'' +
                ", allieReResponsible='" + allieReResponsible + '\'' +
                ", encryptionTime=" + encryptionTime +
                '}';
    }
}