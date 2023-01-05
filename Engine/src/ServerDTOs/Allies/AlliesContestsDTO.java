package ServerDTOs.Allies;

import DataBase.Allies;
import MissionCreator.MissionCreator;
import missions.results.MissionsProgress;

public class AlliesContestsDTO {

    private String allyName , readyStatus, winner, battlefield, uboatUsername, contestDifficulty,message,
    unDecryptedMessage;
    private Integer agentsNum , missionSize , alliesNeeded , alliesJoined;
    private MissionsProgress progress;
    private Boolean readyToCompete;

    private boolean isInProgress;

    public AlliesContestsDTO(Allies ally) {
        this.allyName = ally.getAllyName();
        this.missionSize=ally.getMissionSize();
        this.agentsNum =ally.getAgentsNum();
        this.alliesJoined = ally.getAlliesJoined();
        this.readyToCompete = ally.getReadyToCompete();
        setReadyToCompete(readyToCompete);
        this.winner = ally.getWinner();
        isInProgress = ally.isInProgress();
        this.progress = ally.getMissionsProgress();
        message=ally.getMessageToDecrpyt();
        unDecryptedMessage=ally.getUnDecryptedSentence();

    }

    public String getUnDecryptedMessage() {
        return unDecryptedMessage;
    }

    public void setUnDecryptedMessage(String unDecryptedMessage) {
        this.unDecryptedMessage = unDecryptedMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReadyToCompete(boolean status) {
        if(status){
            readyStatus="Ready!"; //SAGI WHY NEED BOTH readyStatus and readyToCompete?????????????????????
            readyToCompete=true;  //IT IS THE SAME VARIABLE!!!
        }

        else{
            readyStatus="Not Ready!";
            readyToCompete=false;
        }
    }

    public String getAllyName() {
        return allyName;
    }

    public void setAllyName(String allyName) {
        this.allyName = allyName;
    }

    public String getReadyStatus() {
        return readyStatus;
    }

    public void setReadyStatus(String readyStatus) {
        this.readyStatus = readyStatus;
    }

    public Boolean getReadyToCompete() {
        return readyToCompete;
    }

    public Integer getAgentsNum() {
        return agentsNum;
    }

    public synchronized void setAgentsNum(Integer agentsNum) {
        this.agentsNum = agentsNum;
    }

    public Integer getMissionSize() {
        return missionSize;
    }


    public void setMissionSize(Integer missionSize) {
        this.missionSize = missionSize;}

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }


    private MissionCreator.missionDifficulty stringToMissionDiff(String missionDiff) {
        switch (missionDiff){
            case "Easy":
                return MissionCreator.missionDifficulty.EASY;
            case "Medium":
                return MissionCreator.missionDifficulty.MEDIUM;
            case "Hard":
                return MissionCreator.missionDifficulty.HARD;
            case "Impossible":
                return MissionCreator.missionDifficulty.IMPOSSIBLE;
            default:
                return MissionCreator.missionDifficulty.INVALID;
        }
    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean inProgress) {
        isInProgress = inProgress;
    }


    public String getBattlefield() {
        return battlefield;
    }

    public void setBattlefield(String battlefield) {
        this.battlefield = battlefield;
    }

    public String getUboatUsername() {
        return uboatUsername;
    }

    public void setUboatUsername(String uboatUsername) {
        this.uboatUsername = uboatUsername;
    }

    public String getContestDifficulty() {
        return contestDifficulty;
    }

    public void setContestDifficulty(String contestDifficulty) {
        this.contestDifficulty = contestDifficulty;
    }

    public Integer getAlliesNeeded() {
        return alliesNeeded;
    }

    public void setAlliesNeeded(Integer alliesNeeded) {
        this.alliesNeeded = alliesNeeded;
    }

    public Integer getAlliesJoined() {
        return alliesJoined;
    }

    public void setAlliesJoined(Integer alliesJoined) {
        this.alliesJoined = alliesJoined;
    }

    public MissionsProgress getProgress() {
        return progress;
    }

    public void setProgress(MissionsProgress progress) {
        this.progress = progress;
    }

}
