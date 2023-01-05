package parts;

public interface MachineInterface {

    String reset();
    //machine also needs to be able to "reset" ,basically set starting positions of its rotors
    //this will be used when the machine will be given an input,
    //and you will want to reset it given the last starting positions
    //this means that for every encryption, in the ENGINE you'll need to save a string of the last starting positions!

    String encrypt(String input);
    //enigma machine needs ability to encrypt given string, and return the encoded string
}
