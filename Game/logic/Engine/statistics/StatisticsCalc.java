package statistics;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StatisticsCalc implements Serializable {

    private Map<String, Pair<Integer,String>> stats;
    private boolean empty;

    //this class if for statistics, the key = machine configuration, the value = the encryptions and time they took

    public StatisticsCalc() {
        stats = new HashMap<>();
        stats.clear();
        this.empty = true;
    }

    public void deleteStats(){
        stats.clear();
        empty = true;
    }

    public void addEncryption(String key, String val){ //NEED TO ADD NUMBER 1. 2. ... IN THE BEGINNING
        if(stats.containsKey(key)){
            Pair<Integer,String> current = stats.get(key);
            Integer newInt = current.getKey() + 1;
            String newStr = current.getValue() + newInt.toString() + ". " + val;
            //current += val;
            stats.replace(key, new Pair<>(newInt, newStr));
        }
        else {
            Integer newInt = 1;
            String newStr = newInt.toString() + ". " + val;
            stats.put(key,new Pair<>(newInt,newStr));
        }
        this.empty = false;
    }

    public boolean isStatsEmpty(){
        return empty;
    }

    @Override
    public String toString() {
        String res = "";
        for (Map.Entry<String, Pair<Integer,String>> entry : stats.entrySet()) {
            res += entry.getKey() + "\n" + entry.getValue().getValue() + "\n";
        }
        return res;
    }
}
