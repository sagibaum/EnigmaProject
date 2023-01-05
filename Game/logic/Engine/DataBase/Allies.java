package DataBase;

import Decipher.Decipher;
import EngineFunctionallity.EngineObject;
import Mission.Mission;
import MissionCreator.MissionCreator;
import com.sun.org.apache.xpath.internal.objects.XNull;
import missions.results.MissionsProgress;
import schem.out.CTEDecipher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class Allies {

    private String allyName , readyStatus, winner, battlefield, uboatUsername, contestDifficulty, messageToDecrpyt;
    private Integer agentsNum , missionSize , alliesNeeded , alliesJoined;
    private Boolean readyToCompete,isInProgress;
    private List<Agent> thisTeamsAgents;
    private List<Agent> AgentsInHold;

    //mission creation variables:
    private ArrayBlockingQueue<Runnable> missionsQueue;
    private MissionsProgress missionsProgress; //contains total missions AND missions created needed for the desktop app!
    private Consumer<MissionsProgress> missionsProgressConsumer;
    private MissionCreator missionsCreator;
    private Thread missionCreatorThread;


    public Allies(String allyName) {
        this.allyName = allyName;
        thisTeamsAgents = new ArrayList<>();
        AgentsInHold = new ArrayList<>();
        this.missionSize=this.agentsNum =0;
        this.alliesNeeded = 0;
        this.alliesJoined = 0;
        this.readyToCompete = false;
        setReadyToCompete(false);
        this.winner = "";
        messageToDecrpyt = "";
        isInProgress =false;
        this.missionsProgress = new MissionsProgress(new AtomicLong(0),0L);
    }

    public void setReadyToCompete(boolean status) {
        if(status){
            readyStatus="Ready!";
            readyToCompete=true;
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
        return this.thisTeamsAgents.size();
    }

    public synchronized void setAgentsNum(Integer agentsNum) {
        this.agentsNum = agentsNum;
    }

    public Integer getMissionSize() {
        return missionSize;
    }

    public void addAgentToTeam(Agent agent){
        if(!this.isInProgress){
        this.thisTeamsAgents.add(agent);
        agent.setBelongToContest(true);
        this.agentsNum=1+agentsNum;
        }
        else{
            AgentsInHold.add(agent);
            agent.setBelongToContest(false);
        }

    }

    public List<Agent> getThisTeamsAgents() {
        return thisTeamsAgents;
    }

    public void setMissionSize(Integer missionSize) {
        this.missionSize = missionSize;}

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void startCreatingMissions(CTEDecipher cteDecipher, String difficulty, EngineObject engine) {
        //creating all variables needed for mission creator:
        this.missionsQueue = new ArrayBlockingQueue<>(1000);
        Decipher decipher = new Decipher(cteDecipher);
        this.missionsProgressConsumer = missionsProgress -> this.missionsProgress = missionsProgress; //use mission progress into local variable here
        MissionCreator.missionDifficulty missionDifficulty = stringToMissionDiff(difficulty);
        //creating the mission creator and the thread:
        this.missionsCreator = new MissionCreator(missionDifficulty,this.missionSize ,decipher
                ,missionsQueue, this.messageToDecrpyt, engine, allyName, missionsProgressConsumer);
        this.missionCreatorThread = new Thread(missionsCreator);
        // System.out.println("Before thread start");
        //start creating missions and sending them to the server:
        this.missionCreatorThread.start();
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

    public void stopCreatingMissions() {
        if(missionCreatorThread!=null)
            this.missionCreatorThread.interrupt();
        if(missionsQueue!=null)
            this.missionsQueue.clear();
        this.missionsProgress = new MissionsProgress(new AtomicLong(0),0L);
    }

    public boolean isInProgress() {
        return isInProgress;
    }

    public void setInProgress(boolean inProgress) {
        isInProgress = inProgress;
    }

    public Mission getOneMission() throws InterruptedException {
        if(!missionsQueue.isEmpty())
            return (Mission) missionsQueue.remove();//removes head of queue and returns it
        return null;
        //if needed it will wait here until mission is created
        //NOTE: the waiting part keeps the server OCCUPIED please notice this!
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

    public MissionsProgress getMissionsProgress() {
        return missionsProgress;
    }

    public Integer getMissionsInQueueNumber() {
        return missionsQueue.size();
    }

    public String getMessageToDecrpyt() {
        return messageToDecrpyt;
    }

    public void setMessageToDecrpyt(String messageToDecrpyt) {
        this.messageToDecrpyt = messageToDecrpyt;
    }

    public void setEngine(EngineObject engine) {

    }

    public boolean isQueueEmpty() {
        return missionsQueue.isEmpty();
    }

    public List<Agent> getAgentsInHold() {
        return AgentsInHold;
    }

    public void moveAgentsFromHoldListToAgentsList() {
        for(Agent agent:AgentsInHold){
         thisTeamsAgents.add(agent);
         agent.setBelongToContest(true);
         agentsNum+=1;
        }
        AgentsInHold.clear();
    }

    public void clearAgentsData() {
        for(Agent agent:thisTeamsAgents){
        agent.getAllCandidates().clear();
        }
    }

}