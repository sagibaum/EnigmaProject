package user.data.transfer.unit;

import parts.Machine;
public class UserDataTransferUnit {
    //this unit exists to deliver all relevant information from the engine module (here)
    //that received the command and input from the user, preformed the relevant tasks in the machine
    //and the information will be encapsulated into a specific data type and sent back to the UI module
    //this way the UI module won't have any access to the machine or the engine itself
    //this guarantees a level of security for the machine
    private String currentMachineConfiguration;
    private String rotorsConfig;
    private String notchesLocations;
    private String howManyReflectorsAvailable;
    private String howManyMessagedEncrypted;
    //--------------
    private String EngineOutputMessage;

    private String UIMessageInput;

    private String UICommandMessage;

    private boolean OperationStatus;

    private Long encryptionTime;

    private String machineConfBeforeEncryption;


    //following function receives a machine instance, and creates a string of its configuration, example: <45,27,94><AO!><III><A|Z,D|E>
    public void setUserDataTransferUnitStrings(Machine machine) { //using the ctor of the userdata transfer unit will initialize
                                                   //the current state of the machine and will save all components needed for option 2
        createMachineConfigString(machine);
        createReflectorsAvailableString(machine);
        createHowManyMessagesEncryptedString(machine);
        createRotorsLocationsString(machine);
        createRotorsConfigString(machine);
    }

    public UserDataTransferUnit(){
        this.encryptionTime = 0L;
    }

    public String getCurrentMachineConfiguration() {
        return currentMachineConfiguration;
    }

    public String getEntireMachineConfiguration(){ //this is basically option 2 from the menu!
        String res = "";
        res += "Rotors in use/Rotors available: " + this.rotorsConfig + '\n'
                + this.notchesLocations + '\n' +
                "Reflectors available: " +this.howManyReflectorsAvailable + '\n'
                + "Amount of messages encrypted in this machine: " + this.howManyMessagedEncrypted + '\n'
                + "Current machine configuration: " + this.currentMachineConfiguration;
        return res;
    }

    private String createMachineConfigString(Machine machine){
        String res = "<";
        res += machine.getUsedRotorsId() + '>';
        res += '<' + machine.getCurrentStartingPositions() + '>';
        res += '<' + machine.getReflectorID() + '>';
        res += '<' + machine.getPlugBoardConfig() + '>';
        this.currentMachineConfiguration = res; //also save this currentMachine configuration for future use.
        return res;
    }

    private void createRotorsConfigString(Machine machine){
        this.rotorsConfig = machine.getRotorsConfig();
    }

    private void createRotorsLocationsString(Machine machine){
        this.notchesLocations = machine.getNotchLocations();
    }

    private void createReflectorsAvailableString(Machine machine){
        this.howManyReflectorsAvailable = machine.getReflectorsAvailableNum();
    }

    private void createHowManyMessagesEncryptedString(Machine machine){
        this.howManyMessagedEncrypted = machine.getHowManyMessagedEncrypted();
    }

    public String getEngineOutputMessage() {
        return EngineOutputMessage;
    }

    public boolean getOperationStatus() {
        return OperationStatus;
    }

    public void setOperationStatus(boolean operationStatus) {
        OperationStatus = operationStatus;
    }

    public void setEngineOutputMessage(String engineOutputMessage) {
        EngineOutputMessage = engineOutputMessage;
    }

    public String getUIMessageInput() {
        return UIMessageInput;
    }

    public void setUIMessageInput(String UIMessageInput) {
        this.UIMessageInput = UIMessageInput;
    }

    public String getUICommandMessage() {
        return UICommandMessage;
    }

    public void setUICommandMessage(String UICommandMessage) {
        this.UICommandMessage = UICommandMessage;
    }

    public Long getEncryptionTime() {
        return encryptionTime;
    }

    public void addEncryptionTime(Long encryptionTime) {
        this.encryptionTime += encryptionTime;
    }

    public void setEncryptionTime(Long encryptionTime) {
        this.encryptionTime = encryptionTime;
    }

    public String getMachineConfBeforeEncryption() {
        return machineConfBeforeEncryption;
    }

    public void setMachineConfBeforeEncryption(String machineConfBeforeEncryption) {
        this.machineConfBeforeEncryption = machineConfBeforeEncryption;
    }

}
