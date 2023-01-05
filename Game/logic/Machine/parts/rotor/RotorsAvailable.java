package parts.rotor;

import schem.out.CTERotor;
import java.util.LinkedList;
import java.util.List;

public class RotorsAvailable {

    private Integer numberOfRotorsAvailable; // number of rotors AVAILABLE
    protected LinkedList<Rotor> rotorsAvailableList; //data structure to control the order od the rotors in the list .

    public RotorsAvailable(List<CTERotor> cteRotor) {
        //this function adds all rotors available to a linked list, later need to create actual rotor chamber from this data structure
        this.numberOfRotorsAvailable = cteRotor.size();
        rotorsAvailableList =new LinkedList<>();
        for (int i = 0; i < cteRotor.size(); i++) {
             Rotor R =  new Rotor(cteRotor.get(i));
            this.rotorsAvailableList.addLast(R);
        }
    }

    public LinkedList<Rotor> getRotorsAvailableList() {
        return rotorsAvailableList;
    }

    public Integer getNumberOfRotorsAvailable() {
        return numberOfRotorsAvailable;
    }

    public Rotor returnDesiredRotorById(Integer desiredRotorID)
    { //this function returns a specific rotor
        for (int i = 0; i < numberOfRotorsAvailable; i++) {
            if(rotorsAvailableList.get(i).id.equals(desiredRotorID))
                return rotorsAvailableList.get(i);
        }
        return null;
    }

    @Override
    public String toString() {
        return "RotorsAvailable{" +
                "numberOfRotorsAvailable=" + numberOfRotorsAvailable +
                ", rotorsAvailableList=" + rotorsAvailableList +
                '}';
    }
}
