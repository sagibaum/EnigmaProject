package parts.keyboard;

import java.io.Serializable;

public class KeyBoard implements Serializable {//class in ?

    private String ABCLanguage;
    private Integer ABCLength;

    public KeyBoard(String ABCLanguage, Integer ABCLength) {
        this.ABCLanguage = ABCLanguage;
        this.ABCLength = ABCLength;
    }

    public String getABCLanguage() {
        return ABCLanguage;
    }

    public Integer getCharacterPositionByChar(String C){ //need to know the index of the 1st character entering the rotor chamber
        Integer position=0;
        for (int i = 0 ; i < ABCLength ; i++) {
            if(Character.toString(ABCLanguage.charAt(i)).equals(C))
                return position;
            else position++;
        }
        return -1;//if the character isn't in the queue
    }

    public String getCharacterPositionByIndex(Integer position){
       return String.valueOf(ABCLanguage.charAt(position));
    }

    @Override
    public String toString() {
        return "KeyBoard{" +
                "ABCLanguage='" + ABCLanguage + '\'' +
                ", ABCLength=" + ABCLength +
                '}';
    }
}
