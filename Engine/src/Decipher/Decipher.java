package Decipher;

import schem.out.CTEDecipher;

public class Decipher {

    Integer agentsNumber;
    Dictionary dictionary;

    public Decipher(CTEDecipher decipher) {

        this.dictionary = new Dictionary(decipher.getCTEDictionary());
    }

    public Decipher() {
    }

    public Integer getAgentsNumber() {
        return agentsNumber;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }
}
