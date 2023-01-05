package parts.plugboard;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PlugBoard implements Serializable {

    private Map<Character,Character>plugboard;

    private String plugbboardEntry;

    //constructor for the plug-board , initializing the map
    public PlugBoard(String abcPlugBoard, Integer abcLength, String plugbboardEntryInput) {
        this.plugbboardEntry = plugbboardEntryInput;
        plugboard=new HashMap<>(abcLength);
        Character c;
        for (int i = 0; i < abcLength; i++) {//set map as key char s , and the value is char s
            c=abcPlugBoard.charAt(i);
            if(!c.equals('\n') && !c.equals('\t')) {
                plugboard.put(c,c);
            }
        }
    }

    public String getPlugbboardEntry() {
        return plugbboardEntry;
    }

    //connect two plugs
    public void connectTwoPLugs(Character X , Character Y){//note - remember to add before sending the chars ,
        // validation to string input(then you will parse it and sed it to this function
        plugboard.replace(X, Y);
        plugboard.replace(Y, X);
    }

    public Character getValue(Character X){// returning value of a key inserted
        return plugboard.get(X);
    }

    @Override
    public String toString() {
        return "PlugBoard{" +
                "plugboard=" + plugboard +
                '}';
    }
}
