package data.transfer.unit.unit;

import schem.out.CTEEnigma;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MachineDataTransferUnit {
    //this is a class of relevant CTE to send to Machine, only if the File is good!
    //this variable needs to be extracted from the XML only if the file has been checked and deemed okay,
    //the file is to be checked in FileChecker!
    //this is a data structure of ready information to create an Enigma Machine (machine class in machine module) instance!
    private final CTEEnigma enigmaMachineFromFile; //alot of constructors in "Machine" module use the CTEs.
    private String plugBoardInput; //a String of ready for use plugboard input, example :ABDE
    private String reflectorInput; //the desired reflector to use, for example: III
    private List<rotorInitializer> rotorsNeededInitList; //a ready list of rotor init units
                                                         //an example of the list: [[12,'A'],[27,'B'],[53,'C']]

    public MachineDataTransferUnit(CTEEnigma enigmaMachineFromFile, String userInput) {
        this.enigmaMachineFromFile = enigmaMachineFromFile;
        setReflectorInput(userInput);
        setPlugBoardInput(userInput);
        setRotorsNeededInitList(userInput);
    }

    public String getReflectorInput() {
        return reflectorInput;
    }

    public void setReflectorInput(String userInput) { // "<III>" -> "III"
        StringTokenizer reflectorToken = new StringTokenizer(userInput, "<>");
        reflectorToken.nextToken(); //rotors id now
        reflectorToken.nextToken(); //rotor starting pos now
        this.reflectorInput = reflectorToken.nextToken();
    }

    public List<rotorInitializer> getRotorsNeededInitList() {
        return rotorsNeededInitList;
    }

    public void setRotorsNeededInitList(String userInput) {
        //example of user input: <45,27,94><AO!><III><A|Z,D|E>
        //NOTE - THIS FUNCTION SHOULD BE CALLED UPON ONLY IF THE INPUT IS VALID
        //meaning this function assumes that the rotor part of the input is OK
        StringTokenizer rotorsToken = new StringTokenizer(userInput, "<>");
        String rotorsDesired, beginningPositions;
        rotorsDesired = rotorsToken.nextToken();
        beginningPositions = rotorsToken.nextToken();

        rotorsToken = new StringTokenizer(rotorsDesired, "<>,"); // now we add to the list the desired rotors by order of input
        int positionsIndex = 0;
        List<rotorInitializer> newList = new ArrayList<>();
        while(rotorsToken.hasMoreTokens()) { //file has been checked so doesnt matter
            rotorInitializer rotorInitUnit = new rotorInitializer(Integer.parseInt(rotorsToken.nextToken()),
                                                                 (Character.toString(beginningPositions.charAt(positionsIndex))));
            positionsIndex++;
            //this tokenizes the rotor id string, and the rotor starting position string (like strtok()
            //and then adds them to the list of rotor init units
            newList.add(rotorInitUnit);
        }
        this.rotorsNeededInitList = newList;
    }

    public String getPlugBoardInput() {
        return plugBoardInput;
    }

    public void setPlugBoardInput(String userInput) { // "<A|F> -> "AF"
        StringTokenizer plugBoardToken = new StringTokenizer(userInput, "<>");
        String temp;
        temp = plugBoardToken.nextToken(); //rotors id now
        temp = plugBoardToken.nextToken(); //rotor starting pos now
        temp = plugBoardToken.nextToken();//now it should be the Reflector string
        if(plugBoardToken.hasMoreTokens())
           temp = plugBoardToken.nextToken();
        else {
            this.plugBoardInput = "";
            return;
        }

        StringTokenizer plugBoardToken2 = new StringTokenizer(temp, "<,|>");
        String plugboard = "";
        //this.plugBoardInput = plugBoardToken.nextToken(); //now it should be on the plugboard input, example: ABCEDF
        while(plugBoardToken2.hasMoreTokens()) {
            plugboard = plugboard.concat(plugBoardToken2.nextToken());
        }
        this.plugBoardInput = plugboard;
    }

    public CTEEnigma getEnigmaMachineFromFile() {
        return enigmaMachineFromFile;
    }

    @Override
    public String toString() { //use this for the main to check if the unit to create the machine is okay!
        return "MachineDataTransferUnit{" +
               // "enigmaMachineFromFile=" + enigmaMachineFromFile +
                ", plugboardInput='" + plugBoardInput + '\'' +
                ", reflectorInput='" + reflectorInput + '\'' +
                ", rotorsNeededInitList=" + rotorsNeededInitList +
                '}';
    }
}
