package parts.rotor;

import schem.out.CTERotor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;

public class Rotor implements RotorInterface,Comparable<Rotor>, Serializable {
    protected Integer notch; //notches are saved here as 0 ... n -1 , if you want to display them to user need to + 1
    protected Integer id ,size;
    private Integer originalNotch;
    private LinkedList<Positioning> wheel;
    private String startingPos;

    public String getStartingPos() {
        return startingPos;
    }

    public Integer getId() {
        return id;
    }

    public Integer getNotch() {
        return notch;
    }

    public Rotor(CTERotor rotorData){
        this.id = rotorData.getId();
        this.originalNotch = rotorData.getNotch() - 1;
        this.notch = rotorData.getNotch() - 1; //marking the notches as -1 ( 0 .. n -1 ) REMEMBER
        this.size=rotorData.getCTEPositioning().size();
        wheel = new LinkedList<>();
        for (int i = 0; i < rotorData.getCTEPositioning().size(); i++) {
            Positioning newPos = new Positioning(rotorData.getCTEPositioning().get(i), i == notch);
            wheel.add(newPos);
        }
        this.startingPos = wheel.get(0).getRight();
    }
    public void setStartingPos(String start){
        while(!wheel.get(0).getRight().equals(start)){
            moveForward();
        }
    }

    public LinkedList<Positioning> getWheel() {
        return wheel;
    }

    public void moveForward(){
        Positioning out = wheel.removeFirst();
        wheel.add(out);
        if(out.isNotch())
            this.notch = wheel.size() - 1;
        else this.notch -= 1;
        this.startingPos = wheel.get(0).getRight();
    }
    public boolean spin(){
        Positioning out = wheel.removeFirst();
        wheel.add(out);
        if(out.isNotch())
            this.notch = wheel.size() - 1;
        else this.notch -= 1;
        this.startingPos = wheel.get(0).getRight();
        return wheel.get(0).isNotch();
    }
    public Integer rightToLeft(Integer index){
        String c = wheel.get(index).getRight();
        for (int i = 0; i < wheel.size(); i++) {
            if(wheel.get(i).getLeft().equals(c)) {
                return i;
            }
        }
        return -1;
    }

    public Integer leftToRight(Integer index){
        String c = wheel.get(index).getLeft();
        for (int i = 0; i < wheel.size(); i++) {
            if(wheel.get(i).getRight().equals(c))
                return i;
        }
        return -1;
    }

    public Integer getOriginalNotch() {
        return originalNotch + 1;
    }

    public Integer getSize() {
        return size;
    }

    @Override
    public int compareTo(Rotor R) { //by size!!!!!!!
        return this.size - R.getSize();
    }

    @Override
    public int hashCode() {
        return Objects.hash(notch, id, size, wheel, startingPos);
    }

}