package parts.rotor;

import data.transfer.unit.unit.rotorInitializer;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class RotorChamber implements Serializable {
    private Integer rotorsInUseNumber; // number of rotors IN USE
    private Integer rotorsTotalNumber;
    protected LinkedList<Rotor> rotorsInUseList; //data structure to control the order od the rotors in the list .
    // example for input of choosing rotors: <45,27,94><AO!>

    public LinkedList<Rotor> getRotorsInUseList() {
        return rotorsInUseList;
    }
    public void setRotorsInUseNumber(Integer rotorsInUseNumber) {
        this.rotorsInUseNumber = rotorsInUseNumber;
    }

    public Integer getRotorsInUseNumber() {
        return rotorsInUseNumber;
    }

    public RotorChamber(List<rotorInitializer> rotorsNeeded , RotorsAvailable allRotors) {
        //reminder: if you get to here the file has already been checked and is okay.
        this.rotorsTotalNumber = allRotors.getNumberOfRotorsAvailable();
        LinkedList<Rotor> newList = new LinkedList<>();
        this.setRotorsInUseNumber(rotorsNeeded.size());
        for (int i = 0; i < rotorsNeeded.size(); i++) {
            Rotor r = allRotors.returnDesiredRotorById(rotorsNeeded.get(i).getRotorID());
            r.setStartingPos(rotorsNeeded.get(i).getRotorStartingPos());
            //set rotor r to starting position
            newList.addLast(r);
        }
        this.rotorsInUseList = newList;
    }

    public Integer chamberRightToLeft(Integer index){
        Integer idFromRight = index;
        for (int i = this.rotorsInUseNumber - 1; i >= 0 ; i--) {
            idFromRight = this.getRotorsInUseList().get(i).rightToLeft(idFromRight);
        }
        return idFromRight;
    }

    public void setRotorsStartingPositions(String startingPositions){
        for (int i = 0; i < startingPositions.length(); i++) {
            this.rotorsInUseList.get(i).setStartingPos(String.valueOf(startingPositions.charAt(i)));
        }
    }

    public Integer chamberLeftToRight(Integer index){
        Integer idFromLeft = index;
        for (int i = 0; i < this.rotorsInUseNumber; i++) {
            idFromLeft = this.rotorsInUseList.get(i).leftToRight(idFromLeft);
        }
        return idFromLeft;
    }

    public Integer getRotorsTotalNumber() {
        return rotorsTotalNumber;
    }

    @Override
    public String toString() {
        return "RotorChamber{" +
                "rotorsInUseNumber=" + rotorsInUseNumber +
                ", rotorsInUseList=" + rotorsInUseList +
                '}';
    }
}
