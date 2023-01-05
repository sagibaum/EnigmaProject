package parts;
import data.transfer.unit.unit.MachineDataTransferUnit;
import data.transfer.unit.unit.rotorInitializer;
import parts.keyboard.KeyBoard;
import parts.plugboard.PlugBoard;
import parts.reflector.Reflector;
import parts.reflector.ReflectorsAvailable;
import parts.rotor.RotorChamber;
import parts.rotor.RotorsAvailable;

import java.io.Serializable;
import java.util.List;

public class Machine implements MachineInterface, Serializable {
    //machine parts:
    private PlugBoard plugBoard;
    private KeyBoard keyBoard;
    private Reflector reflector;
    private RotorChamber rotorChamber;
    //machine configuration appearance variables:
    private String lastUsedPositions;
    private String originalMachineStartingPositions;
    private Integer howManyMessagedEncrypted;
    private Integer reflectorsAvailable;

    public Machine(MachineDataTransferUnit unit) {
        this.howManyMessagedEncrypted = 0;
        String lang = unit.getEnigmaMachineFromFile().getCTEMachine().getABC();
        lang = lang.replaceAll("\n","");
        lang = lang.replaceAll("\t","");
        //keyboard creation:
        this.keyBoard = new KeyBoard(lang, lang.length());
        //plugBoard creation:
        this.setPlugBoard(unit);
        //reflector creation:
        ReflectorsAvailable reflectors = new ReflectorsAvailable(unit.getEnigmaMachineFromFile().getCTEMachine().getCTEReflectors(),
                                                                 unit.getReflectorInput());
        this.reflectorsAvailable = reflectors.getReflectorsNum();
        this.reflector = reflectors.getCurrentInChamber();
        //rotorChamber creation:
        saveOriginalMachineStartingPositions(unit.getRotorsNeededInitList());
        RotorsAvailable newList = new RotorsAvailable(unit.getEnigmaMachineFromFile().getCTEMachine().getCTERotors().getCTERotor());
        this.rotorChamber = new RotorChamber(unit.getRotorsNeededInitList(), newList);
    }

    private void setPlugBoard(MachineDataTransferUnit unit){
        String lang = unit.getEnigmaMachineFromFile().getCTEMachine().getABC();
        lang = lang.replaceAll("\n","");
        lang = lang.replaceAll("\t","");
        this.plugBoard = new PlugBoard(lang,lang.length(), unit.getPlugBoardInput()); //creates an initialized plugboard
        if(unit.getPlugBoardInput().length() != 0) { //if the plugboard string input isnt empty, this assumes that
            //the input has already been checked in Engine, and that lang.length() % 2 == 0
            String plugboardEntry = unit.getPlugBoardInput(); //for example ACDE, need to preform in plugboard A<->C, D<->E
            for (int i = 0; i < plugboardEntry.length() - 1; i+=2) { //this connects the plugs on the plugboard
                plugBoard.connectTwoPLugs(plugboardEntry.charAt(i),plugboardEntry.charAt(i + 1));
            }
        }
    }

    public String reset(){ //resets to initial machine configuration
        for (int i = 0; i < rotorChamber.getRotorsInUseNumber(); i++) {
            rotorChamber.getRotorsInUseList().get(i).setStartingPos(Character.toString(originalMachineStartingPositions.charAt(i)));
        }
        return originalMachineStartingPositions;
    }


    public void setMachineStartingPos(String startingPositions){
        this.rotorChamber.setRotorsStartingPositions(startingPositions);
    }

    public void resetOneConfigBack(){ //resets one encryption move back
        for (int i = 0; i < rotorChamber.getRotorsInUseNumber(); i++) {
            rotorChamber.getRotorsInUseList().get(i).setStartingPos(Character.toString(lastUsedPositions.charAt(i)));
        }
    }

    private void saveLastRotorsLocations() {
        for (int i = 0; i < this.rotorChamber.getRotorsInUseNumber(); i++) {
            this.lastUsedPositions += rotorChamber.getRotorsInUseList().get(i).getStartingPos();
        }

    }

    private void saveOriginalMachineStartingPositions(List<rotorInitializer> rotorsNeeded) {
        this.originalMachineStartingPositions = "";
        for (int i = 0; i < rotorsNeeded.size(); i++) {
            this.originalMachineStartingPositions += rotorsNeeded.get(i).getRotorStartingPos();
        }
    }


    public String encrypt(String input) {
        this.howManyMessagedEncrypted += 1;
        this.lastUsedPositions = "";
        saveLastRotorsLocations();
        String res = "";
        for (int i = 0; i < input.length(); i++) {
            //for every letter : spin rotors as needed, encrypt letter
            moveRotorsAfterKeyPressed();
            Character c = input.charAt(i);
            res += encryptLetter(c);
        }
        return res.toUpperCase();
    }

    private Character encryptLetter(Character c) {
        //every letter: plugboard->rotors from right -> reflector ->rotors from left -> plugboard -> out
        c = plugBoard.getValue(c);
        int indexFromRight = keyBoard.getCharacterPositionByChar(c.toString().toUpperCase()); //sending CHAR THAT DONT EXIST!!!!!
        indexFromRight = rotorChamber.chamberRightToLeft(indexFromRight);
        int indexFromLeft = this.reflector.returnLocationIndex(indexFromRight); //going through the reflector
        indexFromLeft = rotorChamber.chamberLeftToRight(indexFromLeft);
        c = keyBoard.getCharacterPositionByIndex(indexFromLeft).charAt(0);
        c = plugBoard.getValue(c);
        return c;
    }

    private void moveRotorsAfterKeyPressed(){
        int rotorNotchCycle = rotorChamber.getRotorsInUseNumber() - 1; //starting from the right
        while(rotorNotchCycle >=0 && rotorChamber.getRotorsInUseList().get(rotorNotchCycle).spin())
            rotorNotchCycle--; //for every key pressed, the notches spin from the right most to the left, this loop indicates that
    }

    public String getUsedRotorsId(){
        String ids = "";
        for (int i = 0; i < rotorChamber.getRotorsInUseNumber(); i++) {
            ids += rotorChamber.getRotorsInUseList().get(i).getId().toString();
            if(i != rotorChamber.getRotorsInUseNumber() - 1){
                ids += ',';
            }
        }
        return ids;
    }

    public String getCurrentStartingPositions(){
        String pos = "";
        for (int i = 0; i < rotorChamber.getRotorsInUseNumber(); i++) {
            Integer notchLocation = rotorChamber.getRotorsInUseList().get(i).getNotch() + 1;
            String notchPos = "(" + notchLocation.toString() + ")";
            pos += rotorChamber.getRotorsInUseList().get(i).getStartingPos() + notchPos;
        }
        return pos;
    }

    public String getCurrentStartingPositionsNoNotches(){
        String pos = "";
        for (int i = 0; i < rotorChamber.getRotorsInUseNumber(); i++) {
            pos += rotorChamber.getRotorsInUseList().get(i).getStartingPos();
        }
        return pos;
    }

    public String getReflectorID(){
        return this.reflector.getId();
    }

    public String getPlugBoardConfig(){
        String plug = "";
        for (int i = 0; i < plugBoard.getPlugbboardEntry().length(); i+=2) {
            plug += Character.toString(plugBoard.getPlugbboardEntry().charAt(i)) + '|' +
                    Character.toString(plugBoard.getPlugbboardEntry().charAt(i + 1));
            if(i + 2 < plugBoard.getPlugbboardEntry().length()){
                plug += ',';
            }
        }
        return plug;
    }

    public String getRotorsConfig(){
        String res = "";
        res += rotorChamber.getRotorsInUseNumber().toString() + '/' + rotorChamber.getRotorsTotalNumber();
        return res;
    }

    public String getNotchLocations(){
        String res = "";
        for (int i = 0; i < rotorChamber.getRotorsInUseNumber(); i++) {
            res += "Rotor ID: " + rotorChamber.getRotorsInUseList().get(i).getId()
                    + ", Rotor notch position: " + (rotorChamber.getRotorsInUseList().get(i).getNotch() + 1); //HERE ADDING 1 FOR USER
            if(i + 1 < rotorChamber.getRotorsInUseNumber())
                res += '\n';
        }
        return res;
    }

    public boolean isPlugBoardEmpty(){
         if(this.plugBoard.getPlugbboardEntry().equals(""))
             return true;
         return false;
    }

    public String getReflectorsAvailableNum(){
        return reflectorsAvailable.toString();
    }

    public String getLanguageLetters(){
        return keyBoard.getABCLanguage();
    }

    public String getHowManyMessagedEncrypted() {
        return howManyMessagedEncrypted.toString();
    }



    @Override
    public String toString() {
        return "Machine{" +
                "plugBoard=" + plugBoard +
                ", keyBoard=" + keyBoard +
                ", reflector=" + reflector +
                ", rotorChamber=" + rotorChamber +
                '}';
    }
}
