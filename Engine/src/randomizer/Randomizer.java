package randomizer;

import parts.reflector.ReflectorsAvailable;
import parts.rotor.RotorsAvailable;
import schem.out.CTEEnigma;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Randomizer implements RandomizerInterface{

    private int rotorsToUseNum;

    public Randomizer() {
    }

    //this function will create a randomized user input, example of randomized: <21,233,42><AVC><III><A|F,C|D,G|B>
    //this function relies on the validity of the file! meaning use this ONLY if the validations have been successful
    public String randomize(CTEEnigma enigmaMachineFromFile){
        ReflectorsAvailable reflectors = new ReflectorsAvailable(enigmaMachineFromFile.getCTEMachine().getCTEReflectors());
        RotorsAvailable rotors = new RotorsAvailable(enigmaMachineFromFile.getCTEMachine().getCTERotors().getCTERotor());
        String lang = enigmaMachineFromFile.getCTEMachine().getABC().trim();
        this.rotorsToUseNum = enigmaMachineFromFile.getCTEMachine().getRotorsCount();
        return randomizeRotors(rotors) + randomizeStartingPos(lang) + randomizeReflectors(reflectors) + randomizePlugboard(lang);
    }

    private String randomizeRotors(RotorsAvailable rotors){
        //randomizing the number of rotors we will use, then randomize the IDs of rotors that we will use
        Random rand = new Random();
        int upperbound = rotors.getNumberOfRotorsAvailable(); //this will randomize numbers from 0 to n - 1, need to do +1 later for id
                                                              //this relies on the fact that rotor ids are sequential
        /*int rotorsNeeded = rand.nextInt(Math.min(upperbound, 99)) + 1; // 0 ... 98, rotors used can be a maximum of 99
        int rotorsToUse = rotorsNeeded <= 1 ? 2 : rotorsNeeded; //if it was randomized to 1, the minimum is two else use the random
        this.rotorsToUseNum = rotorsToUse;*/
        String res = "<";
        Set<Integer> set = new HashSet<>(); //rotors have to be unique

        for (int i = 0; i < rotorsToUseNum; i++) {
            Integer randomized = (rand.nextInt(upperbound) + 1);
            while(set.contains(randomized))
                randomized = (rand.nextInt(upperbound) + 1);
            res += randomized.toString();
            set.add(randomized);
            if(i + 1 < rotorsToUseNum)
                res += ',';
        }
        res += '>';
        return res;
    }

    private String randomizeStartingPos(String lang){
        Random rand = new Random();
        String res = "<";
        for (int i = 0; i <rotorsToUseNum; i++) {
            int randomIndex = rand.nextInt(lang.length()); //in the string lang, they reside from 0 .... n - 1
            res += Character.toString(lang.charAt(randomIndex));
        }
        res += '>';
        return res;
    }

    private String randomizeReflectors(ReflectorsAvailable reflectors){ //need exactly one reflector
        Random rand = new Random();
        int reflectorChosen = rand.nextInt(reflectors.getReflectorsNum()) + 1; //genererates 0 -> 4, need to +1
        return '<' + intToRoman(reflectorChosen) + '>';
    }

    private String randomizePlugboard(String lang){ //<A|F,C|D,G|B>
        //note lang.length() has to be even!
        String res = "<";
        Random rand = new Random(); //giving 20% to NOT connect plugs! 0 .. 4

      /*  if(rand.nextInt(5) == 0)
            return res + '>'; //if it was 0 no plugs are connected
        int howManyPairs = rand.nextInt(lang.length() / 2) + 1; //a minimum of 1 pair
        if (howManyPairs > (lang.length() / 2)) howManyPairs = lang.length() / 2; //cant be more than half of pairs
        String remainingLetters = lang;
        for (int i = 0; i < howManyPairs; i++) {
            Character plugone = remainingLetters.charAt(rand.nextInt(remainingLetters.length())); //randomize first plug
            remainingLetters = remainingLetters.replace(Character.toString(plugone),""); //remove the character randomized
            Character plugtwo = remainingLetters.charAt(rand.nextInt(remainingLetters.length())); //randomize second plug
            remainingLetters = remainingLetters.replace(Character.toString(plugtwo),""); //remove the character randomized
            res += plugone.toString() + '|' + plugtwo.toString();
            if( i + 1 < howManyPairs)
                res += ',';
        }*/
        res += '>';
        return res;
    }


    private String intToRoman(int id){
        switch(id){
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
        }
        return "";
    }

}
