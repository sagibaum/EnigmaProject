package parts.reflector;

import schem.out.CTEReflectors;

import java.util.ArrayList;
import java.util.List;

public class ReflectorsAvailable {
    //structure for the reflectors
    protected Integer ReflectorsNum;

    protected List<Reflector> Reflectors;

    protected Reflector CurrentInChamber;

    public ReflectorsAvailable(CTEReflectors reflectorsarr,String reflectorChosen) { // build the array of reflectors .
        ReflectorsNum = reflectorsarr.getCTEReflector().size();
        Reflectors = new ArrayList<>(ReflectorsNum);//allocation
        for (int i = 0; i < getReflectorsNum(); i++) {
            Reflector r =new Reflector(reflectorsarr.getCTEReflector().get(i)); //extract a CTEreflector from the array and build it with reflector class constructor
            if(r.getId().equals(reflectorChosen)) {
                this.setCurrentInChamber(r);
            }
            Reflectors.add(r);//adding it
        }
    }

    public ReflectorsAvailable(CTEReflectors reflectorsarr) { // build the array of reflectors .
        ReflectorsNum = reflectorsarr.getCTEReflector().size();
        Reflectors = new ArrayList<>(ReflectorsNum);//allocation
        for (int i = 0; i < getReflectorsNum(); i++) {
            Reflector r =new Reflector(reflectorsarr.getCTEReflector().get(i)); //extract a CTEreflector from the array and build it with reflector class constructor
            Reflectors.add(r);//adding it
        }
    }

    public Integer getReflectorsNum() { // getting reflectors num
        return ReflectorsNum;
    }

    public List<Reflector> getReflectors() {
        return Reflectors;
    }

    public Reflector getCurrentInChamber() {//gets the current reflector !ID! that's in the reflectors chamber
        return this.CurrentInChamber;
    }

    public void setCurrentInChamber(Reflector currentInChamber) {//sets new reflector instead of the current reflector
        CurrentInChamber = currentInChamber;
    }
}
