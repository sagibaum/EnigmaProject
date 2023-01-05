package EngineFunctionallity;

import BattleField.BattleField;
import Decipher.Decipher;
import Exceptions.InValidUserInputException;
import file.checker.FileChecker;
import file.reader.FileReader;
import data.transfer.unit.unit.MachineDataTransferUnit;
import parts.Machine;
import parts.reflector.ReflectorsAvailable;
import parts.rotor.RotorsAvailable;
import randomizer.Randomizer;
import schem.out.CTEBattlefield;
import schem.out.CTEDecipher;
import schem.out.CTEEnigma;
import statistics.StatisticsCalc;
import user.data.transfer.unit.UserDataTransferUnit;
import user.input.checker.UserInputChecker;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EngineObject implements UICommands,Serializable {
    private final FileChecker XMLChecker;
    private CTEEnigma XmlEnigma ;

    private List<Integer> rotorsAvailable; //by ID

    private List<String>  reflectorsAvailable; //by ID

    private StatisticsCalc stats;
    private final UserInputChecker UserInputChecker;
    private Machine machine;
    private String initialMachineConfiguration;

    private Decipher decipher;

    private BattleField battleField;



    public EngineObject() {
        this.XMLChecker =  new FileChecker();
        this.UserInputChecker= new UserInputChecker();
        this.reflectorsAvailable = new ArrayList<String>();
        this.rotorsAvailable =new ArrayList<Integer>();
        this.stats = new StatisticsCalc();
        this.decipher = new Decipher();
        this.battleField = new BattleField();
    }

    public CTEEnigma getXmlEnigma() {
        return XmlEnigma;
    }

    @Override
    public void UploadXmlFile(String filePath,UserDataTransferUnit UICommunicationUnit) {
        CheckXmlFile(filePath);//checking xml file. If something went wrong the Ui will
        // catch the exception and if valid , the XMLEnigma will set
        //Also sets here the CTEnigma if its a valid file + the Decipher .
        stats.deleteStats(); //every time using command 1 stats get wiped.
        UICommunicationUnit.setOperationStatus(true);
    }

    public CTEEnigma UploadXmlFileServer(InputStream file){
        return CheckXmlFileServer(file);//checking xml file. If something went wrong the Ui will
        // catch the exception and if valid , the XMLEnigma will set
        //Also sets here the CTEnigma if its a valid file + the Decipher .

    }

    public void CheckXMLFileToUploadIntoServer(String filePath){
        XMLChecker.checkFileName(filePath);
    }

    private CTEEnigma CheckXmlFileServer(InputStream file) {
        Integer rotorsSize;
        try {//from server
            JAXBContext jaxbContext =JAXBContext.newInstance(CTEEnigma.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            this.XmlEnigma =(CTEEnigma) jaxbUnmarshaller.unmarshal(file);//sets here the CTEnigma
        } catch (JAXBException e) {
            e.printStackTrace();
            throw new RuntimeException("Jaxbe problem");
        }

        XmlEnigma.getCTEMachine().setABC(XmlEnigma.getCTEMachine().getABC().toUpperCase());
        XMLChecker.checkABC(XmlEnigma.getCTEMachine().getABC().trim());//checking ABC
        RotorsAvailable rotorsAvailable = new RotorsAvailable(XmlEnigma.getCTEMachine().getCTERotors().getCTERotor());//arrange Rotors to pass-by and check validation
        ReflectorsAvailable reflectorsAvailable = new ReflectorsAvailable(XmlEnigma.getCTEMachine().getCTEReflectors());//arrange Reflectors to pass-by and check validation
        XMLChecker.checkRotors(rotorsAvailable, XmlEnigma.getCTEMachine().getRotorsCount(), XmlEnigma.getCTEMachine().getABC().trim());//checking rotors
        rotorsSize = rotorsAvailable.getRotorsAvailableList().get(0).getSize();
        XMLChecker.checkReflectors(reflectorsAvailable, XmlEnigma.getCTEMachine().getABC().trim(), rotorsSize);//reflectors checker
        //check decipher and sets to decipher object
        XMLChecker.checkDecipherValidation(XmlEnigma.getCTEDecipher()); // miss here some checks !!
        XMLChecker.checkBattleFieldValidation(XmlEnigma.getCTEBattlefield());
        return XmlEnigma;

    }


    public void setEngineObjects(){
        XmlEnigma.getCTEMachine().setABC(XmlEnigma.getCTEMachine().getABC().toUpperCase());//sets ABC
        RotorsAvailable rotorsAvailable = new RotorsAvailable(XmlEnigma.getCTEMachine().getCTERotors().getCTERotor());//arrange Rotors to pass-by and check validation
        ReflectorsAvailable reflectorsAvailable = new ReflectorsAvailable(XmlEnigma.getCTEMachine().getCTEReflectors());//arrange Reflectors to pass-by and check validation
        setReflectorsAvailable(reflectorsAvailable);//setting reflectors available
        setRotorsAvailable(rotorsAvailable);//setting rotors available
        //check decipher and sets to decipher object
        setDecipherValues(XmlEnigma.getCTEDecipher());// sets Decipher
        setBattleFieldValues(XmlEnigma.getCTEBattlefield());// sets BattleField
    }

    //Shell function for checking XML file
    private void CheckXmlFile(String filePath) {
        Integer rotorsSize;
        FileReader fr = new FileReader();
        this.XmlEnigma = (fr.readFile(filePath));//sets here the CTEnigma
        XMLChecker.checkFileName(filePath);
        XmlEnigma.getCTEMachine().setABC(XmlEnigma.getCTEMachine().getABC().toUpperCase());
        XMLChecker.checkABC(XmlEnigma.getCTEMachine().getABC().trim());//checking ABC
        RotorsAvailable rotorsAvailable = new RotorsAvailable(XmlEnigma.getCTEMachine().getCTERotors().getCTERotor());//arrange Rotors to pass-by and check validation
        ReflectorsAvailable reflectorsAvailable = new ReflectorsAvailable(XmlEnigma.getCTEMachine().getCTEReflectors());//arrange Reflectors to pass-by and check validation
        XMLChecker.checkRotors(rotorsAvailable, XmlEnigma.getCTEMachine().getRotorsCount(), XmlEnigma.getCTEMachine().getABC().trim());//checking rotors
        rotorsSize = rotorsAvailable.getRotorsAvailableList().get(0).getSize();
        XMLChecker.checkReflectors(reflectorsAvailable, XmlEnigma.getCTEMachine().getABC().trim(), rotorsSize);//reflectors checker
        setReflectorsAvailable(reflectorsAvailable);//setting reflectors available
        setRotorsAvailable(rotorsAvailable);//setting rotors available
        //check decipher and sets to decipher object
        XMLChecker.checkDecipherValidation(XmlEnigma.getCTEDecipher()); // miss here some checks !!
        setDecipherValues(XmlEnigma.getCTEDecipher());// sets Decipher*/
        XMLChecker.checkBattleFieldValidation(XmlEnigma.getCTEBattlefield());
        setBattleFieldValues(XmlEnigma.getCTEBattlefield());
    }


    @Override
    public void ViewMachineSpecifications(UserDataTransferUnit UICommunicationUnit) {
        UICommunicationUnit.setUserDataTransferUnitStrings(machine);
        UICommunicationUnit.setEngineOutputMessage(UICommunicationUnit.getEntireMachineConfiguration()
                + '\n' +"Initial machine configuration: "+ this.initialMachineConfiguration + '\n');
        UICommunicationUnit.setOperationStatus(true);
    }

    //This function will set up the machine according user input
    @Override
    public void ManuallyInitialCodeConf(String Code) {

    }

    public void viewPartialMachineSpecs(UserDataTransferUnit UICommunicationUnit){
        String res = "";
        res += "Rotors in use/Rotors available: 0/" + this.rotorsAvailable.size() + '\n' +
                "Reflectors available: " +this.reflectorsAvailable.size() + '\n'
                + "Amount of messages encrypted in this machine: " + "no messages have been encrypted yet" + '\n'
                + "Current machine configuration: " + "no machine configuration set yet.\n" ;
        UICommunicationUnit.setEngineOutputMessage(res);
    }

    @Override
    public void AutomaticallyInitialCodeConf(UserDataTransferUnit UICommunicationUnit) {
        String newConfig = new Randomizer().randomize(XmlEnigma);
        MachineDataTransferUnit mdtu = new MachineDataTransferUnit(XmlEnigma,newConfig);
        this.initialMachineConfiguration = newConfig;
        this.machine=new Machine(mdtu);
        UICommunicationUnit.setUserDataTransferUnitStrings(machine);
        //this.initialMachineConfiguration = UICommunicationUnit.getCurrentMachineConfiguration();
        UICommunicationUnit.setUserDataTransferUnitStrings(this.machine);
    }

    @Override
    public String InputEncryptionOrDecoding(String message, UserDataTransferUnit UICommunicationUnit,boolean byLetter) {
        //NEED TO CHECKER WHETHER THE MESSAGE CHARACTERS ARE OK
        UserInputChecker.checkABCBelonging(message, machine.getLanguageLetters());
        if(message.equals("") || message.equals(null))
            return "";
        //after the check is completed
        UICommunicationUnit.setUserDataTransferUnitStrings(this.machine);
        String currentMachineConfig = UICommunicationUnit.getCurrentMachineConfiguration(); //current machine config before encryption is key
        long begin = System.nanoTime(); //begin time
        String encryptedMessage = machine.encrypt(message); //encrypting message
        long encryptionTime = System.nanoTime() - begin;
        UICommunicationUnit.addEncryptionTime(encryptionTime);
        String val = message + " --> " + encryptedMessage + " ( " + Long.toString(encryptionTime) + " nano seconds )\n";
        if(!byLetter)
            stats.addEncryption(currentMachineConfig, val);
        UICommunicationUnit.setUserDataTransferUnitStrings(this.machine);
        return encryptedMessage;
    }

    public void addStat(String key, String val){
        stats.addEncryption(key,val);
    }

    @Override
    public void ResetCode(UserDataTransferUnit UICommunicationUnit) {
        UICommunicationUnit.setUIMessageInput("Machine's rotors have successfully reset back to initial machine starting positions: " +
                machine.reset());
        UICommunicationUnit.setUserDataTransferUnitStrings(this.machine);
    }
    @Override
    public String MachineHistoryAndStatistics() {
        if(stats.isStatsEmpty())
            return "No encryptions have been made yet.\n";
        return "Machine message encryptions and time each encryption took, arranged by the machine configuration: \n"
                +  stats.toString();
    }

    public UserInputChecker getUserInputChecker() {
        return UserInputChecker;
    }

    //get the reflectors available as list of ints(only way to get access to this list!)
    public List<Integer> getRotorsAvailable() {
        return rotorsAvailable;
    }

    //set the reflectors available as list of ints
    private void setRotorsAvailable(RotorsAvailable rotorsAvailable) {
        this.rotorsAvailable.clear();
        for (int i = 0; i < rotorsAvailable.getNumberOfRotorsAvailable(); i++) {
            this.rotorsAvailable.add( rotorsAvailable.getRotorsAvailableList().get(i).getId());
        }
    }

    //get the reflectors available as list of strings
    public List<String> getReflectorsAvailable() {
        return reflectorsAvailable;
    }

    //set the reflectors available as list of strings(only way to get access to this list! )
    private void setReflectorsAvailable(ReflectorsAvailable reflectorsAvailable) {
        this.reflectorsAvailable.clear();;
        for (int i = 0; i < reflectorsAvailable.getReflectorsNum(); i++) {
            this.reflectorsAvailable.add(reflectorsAvailable.getReflectors().get(i).getId());
        }
    }

    //getting the ABC of the current machine settings form th e xml file
    public String getAbc(){
        return XmlEnigma.getCTEMachine().getABC().trim();
        //trim is ok cause if a space belongs to the abc-> he will appear inside the string ABC
    }

    //returns how many rotors that machine configure with(rotors count determines the num of machine rotors possible in that machine
    public Integer getMachineRotorsNumConf(){
        return  this.XmlEnigma.getCTEMachine().getRotorsCount();
    }

    public String getAbcFromMachine(){
        return machine.getLanguageLetters();
    }

    //Setting the machine state
    public void setMachine(String userInput, UserDataTransferUnit UICommunicationUnit) {
        MachineDataTransferUnit mdtu = new MachineDataTransferUnit(XmlEnigma,userInput);
        this.initialMachineConfiguration = userInput;
        this.machine=new Machine(mdtu);
        UICommunicationUnit.setUserDataTransferUnitStrings(machine);
        //this.initialMachineConfiguration = UICommunicationUnit.getCurrentMachineConfiguration();
    }

    public String getInitialMachineConfiguration() {
        return initialMachineConfiguration;
    }

    //get plug boards as string
    public String getPlugboardSets(){
        if(machine!=null)
            return machine.getPlugBoardConfig();
        else return "";
    }

    public String getRotorsAndNotch(){
        return machine.getNotchLocations();
    }

    public boolean isMachineNull(){
        if (machine == null)
            return true;
        return false;
    }

    //Saving machine  object (data) into ser file
    @Override
    public void SaveMachineState(String Path) {
        String message="";
        try {
            UserInputChecker.checkNotEmptyPath(Path);
            FileOutputStream fileOut = new FileOutputStream(Path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        }
        catch(FileNotFoundException e){
            throw new InValidUserInputException("Invalid Path:\nFile doesn't exist in current path! Please check " +
                    "file name/path inserted.\n");
        }
        catch(IOException|InValidUserInputException e){
            message+=e.getMessage();
            throw new InValidUserInputException(message);
        }
    }

    //Loading machine  object (data) into ser file
    @Override
    public EngineObject LoadMachineState(String Path, UserDataTransferUnit UICommunicationUnit) {
        String message="";
        try {
            UserInputChecker.checkNotEmptyPath(Path);
            FileInputStream fileIn = new FileInputStream(Path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            EngineObject newEngine =(EngineObject) in.readObject();
            in.close();
            fileIn.close();
            return newEngine;
        }
        catch(FileNotFoundException e){
            throw new InValidUserInputException("Invalid Path:\nFile doesn't exist in current path! Please check " +
                    "file name/path inserted.\n");
        }
        catch(IOException | ClassNotFoundException|InValidUserInputException e){
            message+=e.getMessage();
            throw new InValidUserInputException(message);
        }

    }

    //sets the decipher values
    private  void setDecipherValues(CTEDecipher decipher){
        this.decipher= new Decipher(decipher);
    }

    //set battlefield value
    private  void setBattleFieldValues(CTEBattlefield battlefield ){
        this.battleField = new BattleField(battlefield);
    }

    public Decipher getDecipher() {
        return decipher;
    }

    public String getBattleFieldName() {
        return battleField.getBattleName();
    }
    public String getBattleFieldDifficultLevel(){
        return battleField.getLevel();
    }
    public Integer getBattleFieldAlliesNum(){
        return battleField.getAllies();
    }
    public String getReflectorID(){
        return machine.getReflectorID();
    }

    public String getUsedRotorsId(){
        return machine.getUsedRotorsId();
    }

    public String getFirstLetter(){
        return String.valueOf(XmlEnigma.getCTEMachine().getABC().trim().charAt(0));
        //returns first letter of alphabet
    }

    public String getLastLetter(){
        return String.valueOf(XmlEnigma.getCTEMachine().getABC().trim().charAt(XmlEnigma.getCTEMachine().getABC().trim().length() - 1));
        //last letter of alphabet
    }
    public String getLetterAtIndex(Integer index){
        return String.valueOf(XmlEnigma.getCTEMachine().getABC().trim().charAt(index));
        //index letter of alphabet NOTE: from 0 ---> n - 1
    }

    public Integer getIndexOfLetter(String letter){
        return (XmlEnigma.getCTEMachine().getABC().trim().indexOf(letter));
    }

    public String getNextLetter(String letter) {
        return String.valueOf(XmlEnigma.getCTEMachine().getABC().trim().charAt(getIndexOfLetter(letter) + 1));
    }//assumes letter isnt Z

    public boolean isPlugBoardEmpty(){
        return machine.isPlugBoardEmpty();
    }

    public void setXmlEnigma(CTEEnigma xmlEnigma) {
        XmlEnigma = xmlEnigma;
    }
}
