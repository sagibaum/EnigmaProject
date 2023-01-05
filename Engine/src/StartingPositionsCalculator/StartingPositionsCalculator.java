package StartingPositionsCalculator;

import EngineFunctionallity.EngineObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartingPositionsCalculator {

    private Map<String,Integer> calculator;
    private String language;
    private String lastStartingPos;
    private EngineObject engine;

    public StartingPositionsCalculator(String language, EngineObject engine, String lastStartingPos) { //startingPos example: AAA
        this.language = language;
        this.engine = engine;
        this.lastStartingPos = lastStartingPos;
        this.calculator = new HashMap<>(); // amount of rotors
        for (int i = 0; i < engine.getMachineRotorsNumConf(); i++) {
            for (int j = 0; j < language.length(); j++) {
                calculator.put(String.valueOf(language.charAt(j)),j); //each integer in a map represents a char, A = 0, B = 1, and so on...
            }
        }
    }

    public synchronized List<String> calculateNextMissions(Integer MissionSize, String CurrentStartingPos) {
        List<String> res = new ArrayList<>();
        res.add(CurrentStartingPos); //adding AAA
        String temp = CurrentStartingPos;
        for (int i = 0; i < MissionSize - 1; i++) {
            temp = addOneToCurrentPositions(temp);
            res.add(temp);
            if(temp.equals(lastStartingPos))
                break;
        }
        return res;
    }

    public synchronized String addOneToCurrentPositions(String currentPos){
        StringBuilder res = new StringBuilder(currentPos);
        Integer spinnerIndex = currentPos.length() - 1;
        String ch = String.valueOf(currentPos.charAt(spinnerIndex)); //char at the rightmost
        if(ch.equals(engine.getLastLetter()) ) {
            while (spinnerIndex >= 0 && ch.equals(engine.getLastLetter())) {
                res.deleteCharAt(spinnerIndex);
                spinnerIndex--;
                if(spinnerIndex >= 0)
                    ch = String.valueOf(currentPos.charAt(spinnerIndex)); //going left when met with 'Z's
            } //delete all 'Z's
            if(spinnerIndex >= 0) {
                res.deleteCharAt(spinnerIndex);
                res.append(language.charAt(calculator.get(ch) + 1));
                while (res.toString().length() != currentPos.length())
                    res.append(engine.getFirstLetter());
            }
            else{
                while (res.toString().length() != currentPos.length()) //deleted all the string
                    res.append(engine.getLastLetter());
            }
        }
        else { //if rightmost isnt 'Z'
            res = new StringBuilder(currentPos);
            res.deleteCharAt(spinnerIndex);
            res.append(language.charAt(calculator.get(ch) + 1));
        }
        //System.out.println(res.toString());
        return res.toString();
            //int rotorNotchCycle = rotorChamber.getRotorsInUseNumber() - 1; //starting from the right
            //while(rotorNotchCycle >=0 && rotorChamber.getRotorsInUseList().get(rotorNotchCycle).spin())
    }
}
