package parts.rotor;

public interface RotorInterface {
    //a rotor should have the ability to spin() rotors by notches
    //to translate, and to set starting pos
    public void setStartingPos(String start);
    //rotor needs to receive location, and to be able to start from there
    public boolean spin();
    //rotor needs the ability to spin, and return boolean value if the next rotor should spin as well
    public Integer rightToLeft(Integer index);
    //translate ability from input in the right
    public Integer leftToRight(Integer index);
    //translate ability from input in the left
}
