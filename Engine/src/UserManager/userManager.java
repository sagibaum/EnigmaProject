package UserManager;
import Constants.EngineConstants;
import DataBase.Agent;
import DataBase.Allies;
import DataBase.UBoat;
import EngineFunctionallity.EngineObject;
import ServerDTOs.Uboat.UBoatDTO;
import user.data.transfer.unit.UserDataTransferUnit;

import java.util.*;


public class userManager {

    private HashMap<String, UBoat> UBoatAvailable;//String = UBoat name , key = UBoat Object

    private HashMap<String, Allies> alliesAvailable;//String = Allie name , key = Allie Object
    private HashMap<String, Agent> agentsAvailable;//String = Agent name , key = Agent Object

    private List<String> battleFieldAbandoned;//String = Agent name , key = Agent Object
    private UserDataTransferUnit UICommunicationUnit;
    EngineObject engine;


    public userManager() {
        UBoatAvailable = new HashMap<String, UBoat>();
        alliesAvailable = new HashMap<String, Allies>();
        agentsAvailable = new HashMap<String, Agent>();
        battleFieldAbandoned = new ArrayList<>();
        engine = new EngineObject();
        UICommunicationUnit= new UserDataTransferUnit();

    }

    public synchronized void addUser(String username,String type) {
        switch(type){
            case EngineConstants.UBOAT:UBoatAvailable.put(username,new UBoat(username));
            break;
            case EngineConstants.Agent:agentsAvailable.put(username,new Agent(username));
            break;
            case EngineConstants.Allies:alliesAvailable.put(username,new Allies(username));
            break;
        }
    }

    public synchronized void removeUser(String username,String type) {

        switch(type){
            case EngineConstants.UBOAT:
                UBoat uBoatAbandoned = UBoatAvailable.get(username);
                //make uboat stop receiving candidates  -> in u boat make home return to the contest tab
                if(uBoatAbandoned.getRunning()){
                    battleFieldAbandoned.add(uBoatAbandoned.getBattlefieldName());}
                //for each ally x , stop his threads from producing candidates  -> return him back to contest screen !
                //now Ally will check if not in progress &&winner =="" -> uboat abandon!
                //for each agent of each ally -> clear his data !
                uBoatAbandoned.setRunning(false);
                stopUBoatAllyWorkingAndReleaseThem(uBoatAbandoned);
                uBoatAbandoned.setAlliesJoined(0);
                UBoatAvailable.remove(username);
                break;
            case EngineConstants.Agent:
                Agent agentAbandoned = agentsAvailable.get(username);
                removeAgentFromAllyTeam(agentAbandoned);
                agentsAvailable.remove(username);//removee from the usermanger
                //System.out.println(agentsAvailable);
                break;
            case EngineConstants.Allies:
                Allies allyAbandoned = alliesAvailable.get(username);
                allyAbandoned.setInProgress(false);//make ally stop creating candidates  -> in ally make him return to the contests tab
                removeAllyFromBattleField(allyAbandoned);
                //clear ally from u boat  + release his agents!
                stopAllyAndHisAgentsWorkAndReleaseThem(allyAbandoned);
                alliesAvailable.remove(username); // removing
                break;
        }


    }




    public synchronized EngineObject getEngine() {
        return engine;
    }

    public synchronized UserDataTransferUnit getUICommunicationUnit() {
        return UICommunicationUnit;
    }

    public synchronized HashMap<String, UBoat> getUBoatAvailable() {
        return UBoatAvailable;
    }

    public synchronized List<String> getBattleFieldAbandoned() {
        return battleFieldAbandoned;
    }

    public boolean checkIfFileExist(String UBoatName, String Filename){

        for(UBoat uBoat:UBoatAvailable.values())
        {
            if(uBoat.getFilename().equals(Filename))
                return true;
        }
        return false;
    }

    public synchronized HashMap<String, Allies> getAlliesAvailable() {
        return alliesAvailable;
    }

    public synchronized HashMap<String, Agent> getAgentsAvailable() {
        return agentsAvailable;
    }

    //sets the ready status of user
    public void setReadyStatus(String type, String name, boolean status, String missionSize){ //synchronized maybe?
        switch (type){
            case EngineConstants.UBOAT:
                UBoatAvailable.get(name).setReadyToCompete(status);
                break;
            case EngineConstants.Agent:
                agentsAvailable.get(name).setReadyToCompete(status);
                break;
            case EngineConstants.Allies:
                alliesAvailable.get(name).setReadyToCompete(status);
                alliesAvailable.get(name).setMissionSize(Integer.valueOf(missionSize));
                break;
        }
    }

    public boolean isUserExists(String username) { // will return if user exist in on of the database structures
        return UBoatAvailable.containsKey(username) || agentsAvailable.containsKey(username) || alliesAvailable.containsKey(username);
    }

    public synchronized void setAgentInfo(String userName, String alliesTeam, Integer threadNumber, Integer missionSize) {
        this.agentsAvailable.get(userName).setAgent(userName, alliesTeam, threadNumber, missionSize);
    }


    public Set<String> getAllies() {
        Set<String> allies = new HashSet<>();
        for (Allies ally: alliesAvailable.values()) {
            allies.add(ally.getAllyName());
        }
        return allies;
    }

    public UBoatDTO getUBoatDTO(String UBoatName){
        return UBoatAvailable.get(UBoatName).getuBoatDTO();
    }

    private UBoat getUBoatOfThisAlliesTeam(Allies ally){
        for (UBoat uboat: UBoatAvailable.values()) {
            if(uboat.isAlliesTeamInThisUBoat(ally))
                return uboat;
        }
        return null; //This is on purpose, if there is a problem it will send an exception
    }

    public void startCompetitionIfAllReady(String userType, String userName) {
        boolean started;
        switch (userType){
            case EngineConstants.UBOAT:
                started = UBoatAvailable.get(userName).checkIfAllAlliesAreReady();
                if(started) { //if everyone ready start creating missions
                    setAllAllyRunningStatus(UBoatAvailable.get(userName)); // announce the next info pull of UBoat that competition has just started
                    AllTeamsStartCreatingMissions(UBoatAvailable.get(userName));

                }
                break;

            case EngineConstants.Allies:
                UBoat uboat = getUBoatOfThisAlliesTeam(alliesAvailable.get(userName));
                started =  uboat.checkIfAllAlliesAreReady();
                if(started) { //if everyone ready start creating missions
                    setAllAllyRunningStatus(uboat); // must sets all allies running status
                    AllTeamsStartCreatingMissions(uboat);
                }
                break;
        }
    }

    private void setAllAllyRunningStatus(UBoat uboat) { // sets all running status to true
        uboat.setRunning(true);
        for(Allies ally:uboat.getAlliesAssociated())
            ally.setInProgress(true);
    }

    private void AllTeamsStartCreatingMissions(UBoat uboat) {
        for (Allies ally: uboat.getAlliesAssociated()) { //if contest started all allies start creating their own missions
            ally.startCreatingMissions(uboat.getCteEnigma().getCTEDecipher(), uboat.getDifficulty(),
                    uboat.getEngine());
           // ally.setInProgress(true);
        }
    }

    public Allies getRelevantAllyFromAgent(String agentUsername){
        for (Allies ally: alliesAvailable.values()) { //going through all available agents of all allies.
            for (Agent agent: ally.getThisTeamsAgents()) {
                if(agent.getUserName().equals(agentUsername))
                    return ally;
            }
        }
        return null; //This is the case that the relevant ally isnt found
    }

    public Agent getRelevantAgentFromUserName(String username){
        for (Agent agent: agentsAvailable.values()) {
            if(agent.getUserName().equals(username))
                return agent;
        }
        return null; //this is the case that the agent isnt listed in the server side! if so exception
    }

    public Allies getRelevantAllyFromAllyName(String alliesTeam) {
        return alliesAvailable.get(alliesTeam);
    }

    private void stopUBoatAllyWorkingAndReleaseThem(UBoat uBoat) {
        for(Allies ally : uBoat.getAlliesAssociated()){
            ally.setInProgress(false);
            ally.stopCreatingMissions();;
            ally.setReadyToCompete(false);

        }
    }

    private void stopAllyAndHisAgentsWorkAndReleaseThem(Allies allyAbandoned) {
        allyAbandoned.stopCreatingMissions();
        for(Agent agent : allyAbandoned.getThisTeamsAgents()){
            agent.setAllyDisconnected(true);// a notify to agent that ALLY has just abandon
            agent.getAllCandidates().clear();//clearing agent candidates
           agentsAvailable.remove(agent.getUserName()); // removing agents of this ally from agents availabe
        }


    }

  private void removeAllyFromBattleField(Allies ally){
        UBoat currUBoat;
      for (UBoat uboat : this.UBoatAvailable.values()) {
          if (uboat.getBattlefieldName().equals(ally.getBattlefield())) {
              uboat.removeAlly(ally);;
              break;
          }
      }

  }


    private void removeAgentFromAllyTeam(Agent agentAbandoned) {
        for(Allies ally : alliesAvailable.values()){
                ally.getThisTeamsAgents().remove(agentAbandoned);
                ally.setAgentsNum(ally.getAgentsNum()-1);
            //System.out.println(ally.getThisTeamsAgents());
        }

    }

    public void checkIfFileNameIsInAbandonList(String battleFieldName) {
        synchronized(this){
        for(String name:battleFieldAbandoned){
            if(name.equals(battleFieldName))
             this.battleFieldAbandoned.remove(name);
         }
        }
    }
}
