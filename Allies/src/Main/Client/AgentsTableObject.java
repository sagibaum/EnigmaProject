package Main.Client;

public class AgentsTableObject {

    private String agentName;
    private String agentTasks;
    private String candidatesNum;

    public AgentsTableObject(String agentName, String agentTasks, String candidatesNum) {
        this.agentName = agentName;
        this.agentTasks = agentTasks;
        this.candidatesNum = candidatesNum;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentTasks() {
        return agentTasks;
    }

    public void setAgentTasks(String agentTasks) {
        this.agentTasks = agentTasks;
    }

    public String getCandidatesNum() {
        return candidatesNum;
    }

    public void setCandidatesNum(String candidatesNum) {
        this.candidatesNum = candidatesNum;
    }
}
