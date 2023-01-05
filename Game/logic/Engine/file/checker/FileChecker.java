package file.checker;

import Exceptions.*;
import parts.reflector.Reflector;
import parts.reflector.ReflectorsAvailable;
import parts.rotor.Rotor;
import parts.rotor.RotorsAvailable;
import schem.out.CTEBattlefield;
import schem.out.CTEDecipher;

import java.io.Serializable;
import java.util.*;

public class FileChecker implements Serializable {
    //this class is responsible to check the contents of the XML file received from user (UI)
    //files will be the way to receive input from the user in part 2 and part 3 as well
    //therefore this class needs to be a part of the machine's engine
    // if the file is NOT okay, a variable will be sent to the UI (part 1) to indicate the specific problem with the file
    //if the file is OKAY the enigma machine will be created, and  preform desired actions
    //the data retrieved back to the UI will be in the form of UserDataTransferUnit
    public FileChecker() {
    } // default constructor

    public void checkFileName(String filepath) {
        StringTokenizer st = new StringTokenizer(filepath, ".");
        String fileSuffix = st.nextToken(".");
        if (!st.hasMoreTokens())
            throw new InvalidXmlFileException("This path is invalid! There isn't a file name at the end of this path!");//file name is not valid.
        fileSuffix = st.nextToken(".");
        if (!fileSuffix.equals("xml"))
            throw new InvalidXmlFileException("File name isn't a valid path /File name isn't end with .xml suffix!");//The file isn't end with the suffix ".xml"
    }

    public void checkABC(String ABC) {
        if (ABC.length() % 2 != 0)//checking that the length of the ABC is even
            throw new InvalidXmlFileException("The ABC size isn't even!");
        String dupLetters = "";
        for (int i = 0; i < ABC.length(); i++) {
            for (int j = i + 1; j < ABC.length(); j++) {

                if ((ABC.charAt(i) == ABC.charAt(j)) && dupLetters.indexOf(ABC.charAt(i))==-1) { //checking that there is not a char that appears twice or more!
                    dupLetters+= dupLetters.valueOf(ABC.charAt(i))+" ";
                    break;
                }
            }
        }
        if(!dupLetters.equals("")) {
            throw new InvalidXmlFileException("The letters " + dupLetters +"appears more than once!\n");}
    }

    //--------------Rotors Validation--------------//

    //Main function to validate Rotors characteristics
    public void checkRotors(RotorsAvailable rotors,Integer rotorsCount,String ABC){

        if(rotorsCount<2) throw new InvalidXmlFileException("Invalid rotor-count size!\n"+"rotor-count size is  " +
                rotorsCount+" that is less than 2!"); // rotors count size checker
        checkRotorCount(rotors.getNumberOfRotorsAvailable(),rotorsCount);//Checking given rotors data compared to Rotor Count
        checkRotorsSize(rotors,ABC,rotorsCount);//checks that rotors size is as the abc size and that there isn't any char that didn't map
        checkRotorId(rotors);//checking the unique ID of each rotor/if there are rotors with the same ID-> will throw exception if so
        checkSerialAppearance(rotors,rotorsCount);//checks if the rotors' id arranged in aggregate
        checkRotorsNotchPosition(rotors); //checks for each rotor if its notch position is exceeding its size
        checkDoubleMappingInRotors(rotors);//checking for each rotor if he consists a double mapping positions
        checkCharsBelongToABCInRotors(rotors,ABC);//checks if there is a letter that is in the position but isn't belongs to the ABC
        checkRotorsShareSameSize(rotors);// all rotors share the same size !
    }

    //Checks given rotors data compared to Rotor Count
    private void checkRotorCount(Integer rotorsNumberInList,Integer rotorsCount){
        String message = "";
        if(rotorsNumberInList<rotorsCount) {
             message+="There is fewer rotor's data then expected in rotors-count!\n" + "There is a data of "
                     +rotorsNumberInList + " rotors,while the size of rotor-count is "+rotorsCount;
            throw new InvalidXmlFileException(message);
        }

    }

    //checks that the rotors size is as the abc size and that there isn't any char that didn't map
    private void checkRotorsSize(RotorsAvailable rotors,String ABC,Integer rotorsCount){
        String SizeInvalidRotors = "";
        for (int i = 0; i <rotorsCount; i++) {
            if(rotors.getRotorsAvailableList().get(i).getSize()<ABC.length())
            {
                SizeInvalidRotors+="Rotor ID:{"+rotors.getRotorsAvailableList().get(i).getId()+ "} , size: {" +
                        rotors.getRotorsAvailableList().get(i).getSize() + "}\n";
            }
        }
        if(!SizeInvalidRotors.equals("")) {//throws exception with the problematic IDs
            throw new InvalidXmlFileException("These Rotors have non-valid size(not as the length of ABC) :\n"+SizeInvalidRotors
                    +"Please fix these rotors size by adding unmapped characters in the xml file, or use a different xml file.\n");}
    }

    //checks the unique ID of each rotor/if there are rotors with the same ID-> will throw exception if so
    private void checkRotorId(RotorsAvailable rotors){//this function will check if there is a rotor with non-valid ID or if there are rotors with the same ID
        String dupIds = "";
        String wrongIDs = "";
        List<Integer> Seen= new ArrayList<Integer>();
        for (int i = 0; i < rotors.getNumberOfRotorsAvailable(); i++) {//check if there is a rotor with non-valid ID
            if(rotors.getRotorsAvailableList().get(i).getId()<1)
                wrongIDs+= "Rotor ID:{"+rotors.getRotorsAvailableList().get(i).getId()+ "}\n";
        }
        if(!wrongIDs.equals("")) {//throws exception with the problematic IDs
            throw new InvalidXmlFileException("These Rotors have non-valid IDs(their IDs is smaller than 1):\n"+wrongIDs
            +"Please fix these rotors IDs in the xml file, or use a different xml file.\n");}

        //checks if there are rotors with the same ID
        for (int i = 0; i < rotors.getNumberOfRotorsAvailable(); i++) {
            for (int j = i + 1; j < rotors.getNumberOfRotorsAvailable(); j++) {
                if(rotors.getRotorsAvailableList().get(i).getId().equals(rotors.getRotorsAvailableList().get(j).getId())&&
                        !Seen.contains(rotors.getRotorsAvailableList().get(i).getId())){
                    Seen.add(rotors.getRotorsAvailableList().get(i).getId());
                    dupIds+= "Rotors ID:{" +rotors.getRotorsAvailableList().get(i).getId() + "} ";
                    break;
                }
            }
        }
        if(!dupIds.equals("")) {//throws exception with the problematic IDs
            throw new InvalidXmlFileException("There is components IDs that appears more than once.\n"+dupIds
                    +"\nPlease fix these components IDs in the xml file, or use a different xml file.\n");}
    }

    //checks for each rotor if its notch position is exceeding its size
    private void  checkRotorsNotchPosition(RotorsAvailable rotors){
        String RotorsWithNotchProblemIds = "";
        for (int i = 0; i < rotors.getNumberOfRotorsAvailable(); i++) {
            if(rotors.getRotorsAvailableList().get(i).getNotch()+1>rotors.returnDesiredRotorById(i+1).getSize()
                ||rotors.getRotorsAvailableList().get(i).getNotch()<0)
            {
                RotorsWithNotchProblemIds+= rotors.getRotorsAvailableList().get(i).getId()+ " ";
            }
        }
        if(!RotorsWithNotchProblemIds.equals("")) {//throws exception with the problematic notch positions of these Rotors IDs
            throw new InvalidXmlFileException("In This rotors(by IDs): " +RotorsWithNotchProblemIds
                    +",the notch position isn't valid->outside of the size range of the rotors");}
    }

    //this function checks if the rotors' id arranged in aggregate
    private void checkSerialAppearance(RotorsAvailable rotors,Integer rotorsCount){
        String RotorsIdsMissing = "";
        for (int i = 1; i <=rotorsCount; i++) {
                if(rotors.returnDesiredRotorById(i) ==null)
                    RotorsIdsMissing+= "{" +i+ "} ";
            }
        if(!RotorsIdsMissing.equals("")) {//throws exception with the problematic notch positions of these Rotors IDs
            throw new InvalidXmlFileException("In This XML file rotor-counts is:" +rotorsCount +" ,so system expected" +
                    "these rotors Id's:1 to "+rotorsCount+"\nBut these rotors ID's are missing:" +RotorsIdsMissing+
                    "Please fix these ID's in the xml file, or use a different xml file.\n");}

    }


    //this function checks the double mapping in all rotors
    private void checkDoubleMappingInRotors(RotorsAvailable rotors){
        String ProblematicRotors="";
        for (int i = 0; i < rotors.getNumberOfRotorsAvailable(); i++) {
           ProblematicRotors+= checkDoubleMappingInOneRotor(rotors.getRotorsAvailableList().get(i));
        }
        if(!ProblematicRotors.equals(""))
            throw new InvalidXmlFileException("This following rotors have double mapping inside them:\n" +
                    ProblematicRotors+"These characters in these pairs above (one or both) " +
                    "already mapped in other pairs.\n"+
                    "Please fix these pairs in the xml file, or use a different xml file.");
    }

    //this function checks the double mapping in one rotor
    private String checkDoubleMappingInOneRotor(Rotor rotor){
        Set<String> SetofRight = new HashSet<String>();
        Set<String> SetofLeft = new HashSet<String>();
        String ProblematicPairs="";
        ArrayList<String> dupControlLeft = new ArrayList<String>();
        ArrayList<String> dupControlRight = new ArrayList<String>();
        for (int i = 0; i < rotor.getSize(); i++) {
            if(!SetofRight.contains(rotor.getWheel().get(i).getRight())) {//if the positioning pair isnt exist -> add it to the set
                SetofRight.add(rotor.getWheel().get(i).getRight());
            }
            else// right already exist!, there for we will save this pair in my string
            {
                if (!dupControlLeft.contains(rotor.getWheel().get(i).getLeft())&&
                        !dupControlRight.contains(rotor.getWheel().get(i).getRight())) { // this condition purpose is to prevent double insertion into the documentation list
                    dupControlRight.add(rotor.getWheel().get(i).getRight());
                    dupControlLeft.add(rotor.getWheel().get(i).getLeft());
                }
                else if(dupControlRight.contains(rotor.getWheel().get(i).getRight())&&!dupControlLeft.contains(rotor.getWheel().get(i).getLeft())){
                dupControlLeft.add(rotor.getWheel().get(i).getLeft());
                 }
            }
            if(!SetofLeft.contains(rotor.getWheel().get(i).getLeft())){
                SetofLeft.add(rotor.getWheel().get(i).getLeft());
            }
            else{// left already exist!, there for we will save this pair in my string
                if (!dupControlLeft.contains(rotor.getWheel().get(i).getLeft())&&
                        !dupControlRight.contains(rotor.getWheel().get(i).getRight())) { // this condition purpose is to prevent double insertion into the documentation list
                    dupControlRight.add(rotor.getWheel().get(i).getRight());
                    dupControlLeft.add(rotor.getWheel().get(i).getLeft());
                }
                    else if(!dupControlRight.contains(rotor.getWheel().get(i).getRight())&&dupControlLeft.contains(rotor.getWheel().get(i).getLeft())){
                        dupControlRight.add(rotor.getWheel().get(i).getRight());
                    }
                ProblematicPairs += "{" + rotor.getWheel().get(i).getRight() + "," + rotor.getWheel().get(i).getLeft() + "} ";
            }
        }
        if(!ProblematicPairs.equals("")) // adding fault rotor id and attach the dup pairs into it
           return "Rotor id:" + rotor.getId() +"\n" +"Pairs that causing double mapping: "+ ProblematicPairs + "\n";
        else return "";
    }

    //This function checks chars in all rotors
    private void checkCharsBelongToABCInRotors(RotorsAvailable rotors,String ABC){
        String ProblematicRotors="";
        for (int i = 0; i < rotors.getNumberOfRotorsAvailable(); i++) {
            ProblematicRotors+= checkCharsBelongToABCInRotor(rotors.getRotorsAvailableList().get(i),ABC);
        }
        if(!ProblematicRotors.equals(""))
            throw new InvalidXmlFileException("In this following rotors/reflectors there are letters that do not belong to the ABC:\n" +
                    ProblematicRotors+ "Please fix these pairs in the xml file, or use a different xml file.\n");
    }

    //This function checks chars in one rotor
    private String checkCharsBelongToABCInRotor(Rotor rotor,String ABC){
        String ProblematicPairs="";
        for (int i = 0; i < rotor.getSize(); i++) {
            if(!ABC.contains(rotor.getWheel().get(i).getRight()) ||
                    !ABC.contains(rotor.getWheel().get(i).getLeft())) {//one of chars isn't in belong to the ABC
                ProblematicPairs += "{" + rotor.getWheel().get(i).getRight() + "," + rotor.getWheel().get(i).getLeft() + "} ";
            }
            else if(rotor.getWheel().get(i).getRight().equals("")||rotor.getWheel().get(i).getLeft().equals("")){
                ProblematicPairs += "{" + rotor.getWheel().get(i).getRight() + "," + rotor.getWheel().get(i).getLeft() + "} ";
            }
        }
        if(!ProblematicPairs.equals("")) // adding fault rotor id and attach the dup pairs into it, if there are pairs like that
            return "Rotor id:" + rotor.getId() +"\n" +"Pairs that contain chars that don't belong to ABC/Empty pairs: "+ ProblematicPairs + "\n";
        else return "";
    }

    //This function checks that rotors shares the same size
    private void checkRotorsShareSameSize(RotorsAvailable rotors){
        String ProblematicRotors="";
       Collections.sort(rotors.getRotorsAvailableList()); // sorting rotors by size
        for (int i = 0; i < rotors.getNumberOfRotorsAvailable()-1; i++) {
            if(!rotors.getRotorsAvailableList().get(i).getSize().
                    equals(rotors.getRotorsAvailableList().get(i+1).getSize()))//if they aren't share same size
            {
                ProblematicRotors += "Rotor ID:{" + rotors.getRotorsAvailableList().get(i).getId()
                        + "},Size:" + rotors.getRotorsAvailableList().get(i).getSize() +
                        " && Rotor ID:{" + rotors.getRotorsAvailableList().get(i+1).getId()
                        + "},Size:" + rotors.getRotorsAvailableList().get(i+1).getSize() +"\n";
            }
        }
        if(!ProblematicRotors.equals(""))
            throw new InvalidXmlFileException("One or more components(by IDs) from the list belong hasn't share the same size:\n"
                    +ProblematicRotors+"Please fix these components size in the xml file, or use a different xml file.\n");
    }

    //--------------Reflectors Validation--------------//

    //Main function to validate Reflectors characteristics
        public void checkReflectors(ReflectorsAvailable reflectors,String ABC,Integer rotorSize){
            checkReflectorsId(reflectors);//check if there is a rotor with non-valid ID or if there are rotors with the same ID
            checkReflectorsShareSamRotorSize(reflectors,rotorSize);//checks that the reflectors share same sze of rotorSize/2
            checkReflectorsCharSameMapping(reflectors);//checks if there is any reflector that maps a letter to itself
            checkInvalidCharMapping(reflectors,rotorSize);//checks if the reflector have cell that exceeds the size of it(half size of rotors)

    }

    //This function will check if there is a rotor with non-valid ID or if there are rotors with the same ID
    private void checkReflectorsId(ReflectorsAvailable reflectors){
        String dupIds = "";
        String wrongIDs = "";
        List<String> Seen= new ArrayList<String>();
        //check if there is a reflector with non-valid ID
        for (int i = 0; i < reflectors.getReflectorsNum(); i++) {
            if(!checkMatchToRomanAlphabet(reflectors.getReflectors().get(i)))
                wrongIDs+= "Reflector ID:{"+reflectors.getReflectors().get(i).getId() + "}\n";
        }
        if(!wrongIDs.equals("")) {//throws exception with the problematic IDs
            throw new InvalidXmlFileException("These Reflector IDs isn't belong to roman alphabet," +
                    "from 1-5 {I,II,III,IV,V):\n"+wrongIDs+"Please fix these reflectors IDs in the xml file, or use a different xml file.");}

        //checks if there are rotors with the same ID
        for (int i = 0; i < reflectors.getReflectorsNum(); i++) {
            for (int j = i + 1; j < reflectors.getReflectorsNum(); j++) {
                if(reflectors.getReflectors().get(i).getId().equals(reflectors.getReflectors().get(j).getId())&&
                        !Seen.contains(reflectors.getReflectors().get(i).getId())){
                    Seen.add(reflectors.getReflectors().get(i).getId());
                    dupIds+= "Reflector ID:{"+reflectors.getReflectors().get(i).getId() + "} ";
                    break;
                }
            }
        }
        if(!dupIds.equals("")) {//throws exception with the problematic IDs
            throw new InvalidXmlFileException("There is components IDs that appears more than once.\n"+ dupIds+
                    "\nPlease fix these components IDs in the xml file, or use a different xml file.\n");}
    }

    //This function will check if there is a reflector ID that doesn't match the roman alphabet
    private boolean checkMatchToRomanAlphabet(Reflector reflector){
        return reflector.getId().equals("I") || reflector.getId().equals("II") || reflector.getId().equals("III") ||
                reflector.getId().equals("IV") || reflector.getId().equals("V");
    }

    //This function will check that the reflectors share same size of rotorSize/2
    private void checkReflectorsShareSamRotorSize(ReflectorsAvailable reflectors,Integer rotorSize){
        String ProblematicReflectors="";
        for (int i = 0; i < reflectors.getReflectorsNum(); i++) {
            if(reflectors.getReflectors().get(i).getArrReflect().size() != rotorSize / 2)//if reflector size isn't rotorSize/2
            {
                ProblematicReflectors += "Reflector ID:{" +reflectors.getReflectors().get(i).getId()
                        + "},Size:[" +reflectors.getReflectors().get(i).getArrReflect().size()
                        +"]->Expected size:["+rotorSize/2 +"]"
                        +"\n";
            }
        }
        if(!ProblematicReflectors.equals(""))
            throw new InvalidXmlFileException("One or more reflectors(by IDs) from the list size isn't {rotors size/2}:\n"+ProblematicReflectors
            +"Please fix these reflectors size by adding lines in the xml file, or use a different xml file.\n");
    }

    //This function will check if there is any reflector that maps a letter to itself
    private void checkReflectorsCharSameMapping(ReflectorsAvailable reflectors){
        String ProblematicReflectors="";
        String ProblematicPairs="";
        for (int i = 0; i < reflectors.getReflectorsNum(); i++) {
            for (int j = 0; j < reflectors.getReflectors().get(i).getArrReflect().size(); j++) {
                if (reflectors.getReflectors().get(i).getArrReflect().get(j).GetInput()// checks the condition for mapping char to itself
                        .equals(reflectors.getReflectors().get(i).getArrReflect().get(j).GetOutput())) {
                    ProblematicPairs += "[" + (reflectors.getReflectors().get(i).getArrReflect().get(j).GetInput()+1)
                            + "," + (reflectors.getReflectors().get(i).getArrReflect().get(j).GetOutput()+1) + "] ";
                }
            }
            if(!ProblematicPairs.equals("")){//checks if there are any problematic pairs
                ProblematicReflectors += "Reflector ID:{" + reflectors.getReflectors().get(i).getId() +
                        "}\nInvalid Pairs:" +ProblematicPairs + "\n";
                ProblematicPairs = "";//clearing string
            }
        }
        if(!ProblematicReflectors.equals(""))
            throw  new InvalidXmlFileException("One or more reflectors(by IDs) from the list mapped a char to itself:\n" + ProblematicReflectors
            +"Please fix these reflectors pairs by change the mapping values in the xml file, or use a different xml file.\n");

    }

    //This function checks if the reflectors have paired of reflects that one of input/output exceeds the size of it(half size of rotors)
    private void checkInvalidCharMapping(ReflectorsAvailable reflectors,Integer ABCSize){
        String ProblematicReflectors="";
        String ProblematicPairs="";
        for (int i = 0; i < reflectors.getReflectorsNum(); i++) {
            for (int j = 0; j < reflectors.getReflectors().get(i).getArrReflect().size(); j++) {
               {
                    if(checkInvalidCharMappingRange(reflectors.getReflectors().get(i).getArrReflect().get(j).GetInput()+1
                            ,reflectors.getReflectors().get(i).getArrReflect().get(j).GetOutput()+1,ABCSize)){
                        ProblematicPairs += "[" + (reflectors.getReflectors().get(i).getArrReflect().get(j).GetInput()+1)
                                + "," + (reflectors.getReflectors().get(i).getArrReflect().get(j).GetOutput()+1) + "] ";
                    }
                }
            }
            if(!ProblematicPairs.equals("")){//checks if there are any problematic pairs
                ProblematicReflectors += "Reflector ID:{" + reflectors.getReflectors().get(i).getId() +
                        "}\nInvalid Pairs:" +ProblematicPairs + "\n";
                ProblematicPairs = "";//clearing string
            }
        }
        if(!ProblematicReflectors.equals(""))
            throw  new InvalidXmlFileException("One or more reflectors(by IDs) from the list has reflect pair that one value exceed the ABC Size/less than 0:\n"+
                    ProblematicReflectors+"Please fix these reflectors pairs by change the mapping values in the xml file, or use a different xml file.\n");
    }

    // checks the condition for mapping char out of range
    private boolean checkInvalidCharMappingRange(Integer Input,Integer Output,Integer ABCSize){
        return Input > ABCSize || Input <= 0 || Output > ABCSize || Output <= 0;
    }


    //--------------------------------Decipher------------------//

    public void checkDecipherValidation(CTEDecipher agentNumber){
        //agents between 2<-->50
        /*if(agentNumber<2)
        throw new InvalidXmlFileException("There are " + agentNumber + " that is less than 2!\n" +
                "Agents should be a number between 2<-->50 included.Please fix the xml file");
        if(agentNumber>50)
            throw new InvalidXmlFileException("There are " + agentNumber + " that is grater than 50!\n" +
                    "Agents should be a number between 2<-->50 included.Please fix the xml file");*/

    }


    //--------------------BattleField------------------//
    public void checkBattleFieldValidation(CTEBattlefield cteBattlefield) {
        //check level validation
        if(isNumeric(cteBattlefield.getLevel())||!isValidLevelString(cteBattlefield.getLevel()))
            throw new InvalidXmlFileException("Error in current fxml:\nThe level value of CTEBattlefield is a number or invalid one!` ");
        //check Number of allies validation
        if(!isValidAlliesNumber(cteBattlefield.getAllies()))
            throw new InvalidXmlFileException("Error in current fxml:\nThe Allies number value of CTEBattlefield isn't a Natural number! ");
    }

    private  boolean isNumeric(String level) {
       return level.chars().allMatch(Character::isDigit);
    }
    private  boolean isValidLevelString(String level){
        return level.equals("Easy") || level.equals("Medium") || level.equals("Hard") || level.equals("Impossible");
    }
    private boolean isValidAlliesNumber(int AlliesNum){
        return AlliesNum > 0;
    }

}



