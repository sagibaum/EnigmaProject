package DataBase;

import EngineFunctionallity.EngineObject;
import ServerDTOs.Uboat.UBoatDTO;
import schem.out.CTEEnigma;
import user.data.transfer.unit.UserDataTransferUnit;

import java.util.ArrayList;

public class UBoat {
    private String filename ,currentMachineConf,encryptMessage, BattlefieldName ,difficultyLevel;
    private CTEEnigma cteEnigma;
    private ArrayList<Allies> alliesAssociated;

    private String username;
    private boolean validFile , readyToCompete;
    private UBoatDTO uBoatDTO;

    private Integer alliesNeeded, alliesJoined ;
    private Boolean running;
    private EngineObject engine;


    public UBoat(String name) {
        this.readyToCompete = false;
        this.filename = this.currentMachineConf= this.encryptMessage= "";
        alliesAssociated = new ArrayList<Allies>();
        this.username = name;
        this.running = false;
        this.alliesJoined = 0;
        this.uBoatDTO = new UBoatDTO(running);
        this.engine = new EngineObject();
    }


    public String getBattlefieldName() {
        return BattlefieldName;
    }


    public void setRunning(boolean running) {
        this.running = running;
        uBoatDTO.setRunning(running);
    }

    public String isRunning() {
        return running ? "Running" : "Idle";
    }

    public void setBattlefieldName(String battlefieldName) {BattlefieldName = battlefieldName;}

    public String getDifficulty(){
        return cteEnigma.getCTEBattlefield().getLevel();
    }

    public String getAlliesNeededAmountString(){
        return String.valueOf(cteEnigma.getCTEBattlefield().getAllies());
    }

    public String getAlliesJoinedString() {
        return String.valueOf(alliesJoined);
    }

    public Integer getAlliesNeededAmountInteger(){
        return (cteEnigma.getCTEBattlefield().getAllies());
    }

    public Integer getAlliesJoinedInteger() {
        return (alliesJoined);
    }

    public void setAlliesJoined(Integer alliesJoined) {
        this.alliesJoined = alliesJoined;
    }

    public boolean isValidFile() {
        return validFile;
    }

    public void setValidFile(boolean validFile) {
        this.validFile = validFile;
    }

    public String getFilename() {return filename;}

    //synchronised because a risk of data collision
    public synchronized void setFilename(String filename) {
        this.filename = filename;
    }

    public  CTEEnigma getCteEnigma() {
        return cteEnigma;
    }

    //synchronised because risk of data collision
    public  void setCteEnigma(CTEEnigma cteEnigma) {
        this.cteEnigma = cteEnigma;
    }

    public void setCurrentMachineConf(String currentMachineConf) {
        this.currentMachineConf = currentMachineConf;
    }

    public void setEncryptMessage(String encryptMessage) {
        this.encryptMessage = encryptMessage;
        this.encryptMessage = encryptMessage;
        for (Allies ally : this.alliesAssociated) { //give message to all allies
            ally.setMessageToDecrpyt(encryptMessage);
        }
    }


    public boolean isReadyToCompete() {
        return readyToCompete;
    }

    public void setReadyToCompete(boolean status) {
        this.readyToCompete = status;
    }

    public String getEncryptMessage() {
        return encryptMessage;
    }

    public void addAlly(Allies ally){
        alliesAssociated.add(ally);
        alliesJoined+=1;
        uBoatDTO.addAllyToDTO(ally);
    }

    public void removeAlly(Allies ally){
        alliesAssociated.remove(ally);
        alliesJoined-=1;
        uBoatDTO.removeAllyFromDTO(ally);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String uBoatName) {
        this.username = uBoatName;
    }

    @Override
    public String toString() {
        return "UBoat{" +
                "filename='" + filename + '\'' +
                ", currentMachineConf='" + currentMachineConf + '\'' +
                ", encryptMessage='" + encryptMessage + '\'' +
                ", BattlefieldName='" + BattlefieldName + '\'' +
                ", alliesAssociated=" + alliesAssociated +
                ", username='" + username + '\'' +
                ", validFile=" + validFile +
                ", readyToCompete=" + readyToCompete +
                ", uBoatDTO=" + uBoatDTO +
                ", alliesNeeded=" + alliesNeeded +
                ", alliesJoined=" + alliesJoined +
                ", running=" + running +
                '}';
    }

    public synchronized void setUBoatDataEnigma(CTEEnigma cteEnigma, String filename, boolean fileValidity) {
        setFilename(filename);
        setCteEnigma(cteEnigma);
        setEngine();//setting the engine data to the current file uploaded !
        difficultyLevel = cteEnigma.getCTEBattlefield().getLevel();
        BattlefieldName = cteEnigma.getCTEBattlefield().getBattleName();
        alliesNeeded =cteEnigma.getCTEBattlefield().getAllies();
        validFile = fileValidity;
       if(!alliesAssociated.isEmpty())
            alliesAssociated.clear();
       this.alliesJoined=0;

    }

    public UBoatDTO getuBoatDTO() {
        return uBoatDTO;
    }


    public void refreshList() {
        this.uBoatDTO.resetAlliesAssociated();//clears prev data of all allies
        for (Allies a:alliesAssociated){//updates all data again
            uBoatDTO.addAllyToDTO(a);
        }
    }

    public synchronized boolean checkIfAllAlliesAreReady() {
       /* if(alliesAssociated.isEmpty() || !this.readyToCompete) {
            //need to add here a constraint :&& if num of allies in the contest === num of allies needed
            return false;
        }
        for (Allies ally : this.alliesAssociated) {
            if (!ally.getReadyToCompete()) {
                return false;
            }
        }

        return true;*/
        if(alliesJoined < alliesNeeded){
            return false;
        }
        if(alliesAssociated.isEmpty() || !this.readyToCompete) {
            return false;
        }
        for (Allies ally : this.alliesAssociated) {
            if (!ally.getReadyToCompete()) {
                return false;
            }
        }
        //if after all of this running is still TRUE ->  everyone is ready.
        return true;
    }

    public boolean isAlliesTeamInThisUBoat(Allies ally){
        return alliesAssociated.contains(ally);
    }

    public void updateCandidates() { // adding all candidates of this u boat again to the UBoat DTO
        this.uBoatDTO.resetCandidatesAssociated();
        for (Allies ally : alliesAssociated) {//updates all data again
                for (Agent agent : ally.getThisTeamsAgents())
                    uBoatDTO.addToCandidates(agent.getAllCandidates());
        }
    }

    public ArrayList<Allies> getAlliesAssociated() {
        return alliesAssociated;
    }


    public EngineObject getEngine() {
        return engine;
    }

    public String getCurrentMachineConf() {
        return currentMachineConf;
    }

    public Boolean getRunning() {
        return running;
    }

    public void setEngine() {
        this.engine.setXmlEnigma(this.cteEnigma);//setting engine current CTEEnigma machine
        this.engine.setEngineObjects();//setting engine objects in server
    }

    public void setMachineInEngine() {
        this.engine.setMachine(currentMachineConf,new UserDataTransferUnit());
    }
}
