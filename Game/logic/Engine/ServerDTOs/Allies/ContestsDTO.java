package ServerDTOs.Allies;

import DataBase.Allies;
import DataBase.UBoat;

import java.util.ArrayList;

public class ContestsDTO {
    private String filename ,currentMachineConf,encryptMessage,BattlefieldName ,difficultyLevel;
    private ArrayList<AlliesContestsDTO> alliesAssociated;

    private String username;
    private boolean validFile , readyToCompete;

    private Integer alliesNeeded, alliesJoined ;
    private Boolean running;


    public ContestsDTO(UBoat uboat) {
        this.readyToCompete = uboat.isReadyToCompete();
        this.filename = uboat.getFilename();
        this.currentMachineConf = uboat.getCurrentMachineConf();
        this.encryptMessage= uboat.getEncryptMessage();
        this.BattlefieldName = uboat.getBattlefieldName();
        this.validFile = uboat.isValidFile();
        this.alliesNeeded = uboat.getAlliesNeededAmountInteger();
        this.difficultyLevel = uboat.getDifficulty();
        alliesAssociated = new ArrayList<>();
        for (Allies ally : uboat.getAlliesAssociated()) {
            this.alliesAssociated.add(new AlliesContestsDTO(ally));
        }
        this.username = uboat.getUsername();
        this.running = uboat.getRunning();
        this.alliesJoined = uboat.getAlliesJoinedInteger();
    }


    public String getBattlefieldName() {
        return BattlefieldName;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public String isRunning() {
        return running ? "Running" : "Idle";
    }

    public void setBattlefieldName(String battlefieldName) {BattlefieldName = battlefieldName;}

    public String getDifficulty(){
        return difficultyLevel;
    }

    public String getAlliesNeededAmountString(){
        return String.valueOf(alliesNeeded);
    }

    public String getAlliesJoinedString() {
        return String.valueOf(alliesJoined);
    }

    public Integer getAlliesNeededAmountInteger(){
        return alliesNeeded;
    }

    public Integer getAlliesJoinedInteger() {
        return (alliesJoined);
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


    public String getEncryptMessage() {
        return encryptMessage;
    }

    public boolean isReadyToCompete() {
        return readyToCompete;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String uBoatName) {
        this.username = uBoatName;
    }


    public ArrayList<AlliesContestsDTO> getAlliesAssociated() {
        return alliesAssociated;
    }

}
