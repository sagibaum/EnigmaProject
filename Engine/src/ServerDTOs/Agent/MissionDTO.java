package ServerDTOs.Agent;

import Mission.Mission;

import java.util.List;

public class MissionDTO {
    //here is everything required for an agent to do missions every pull
    private Long totalMissions;
    private Integer missionsPulled;
    private Integer missionsInQueue;
    private List<Mission> missionsToExecute;

    private String messageToDecrypt;

    public MissionDTO(Long totalMissions, Integer missionsPulled, Integer missionsInQueue, List<Mission> missionsToExecute, String messageToDecrypt) {
        this.totalMissions = totalMissions;
        this.missionsPulled = missionsPulled;
        this.missionsInQueue = missionsInQueue;
        this.missionsToExecute = missionsToExecute;
        this.messageToDecrypt = messageToDecrypt;
    }

    public MissionDTO getMissionPack(){
        return this;
    }

    public Long getTotalMissions() {
        return totalMissions;
    }

    public void setTotalMissions(Long totalMissions) {
        this.totalMissions = totalMissions;
    }

    public Integer getMissionsPulled() {
        return missionsPulled;
    }

    public void setMissionsPulled(Integer missionsPulled) {
        this.missionsPulled = missionsPulled;
    }

    public Integer getMissionsInQueue() {
        return missionsInQueue;
    }

    public void setMissionsInQueue(Integer missionsInQueue) {
        this.missionsInQueue = missionsInQueue;
    }

    public List<Mission> getMissionsToExecute() {
        return missionsToExecute;
    }

    public String getMessageToDecrypt() {
        return messageToDecrypt;
    }

    public void setMessageToDecrypt(String messageToDecrypt) {
        this.messageToDecrypt = messageToDecrypt;
    }

    @Override
    public String toString() {
        return "MissionDTO{" +
                "totalMissions=" + totalMissions +
                ", missionsPulled=" + missionsPulled +
                ", missionsInQueue=" + missionsInQueue +
                ", missionsToExecute=" + missionsToExecute +
                ", messageToDecrypt='" + messageToDecrypt + '\'' +
                '}';
    }
}
