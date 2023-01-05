package user.input.checker;

import Exceptions.InValidUserInputException;

import java.io.Serializable;
import java.util.*;

public class UserInputChecker implements Serializable {
    public UserInputChecker() {
    }

    //checks if there is unwanted char appearance in the rotors stream input
    //checks if rotors exist in the rotors available list
    public void RotorsValidation(String input, List<Integer> rotorsList,Integer rotorscount) {
        String ErrMessage = "";
        Integer x;
        List<Integer> temp = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(input, ",");
        while (st.hasMoreTokens()) {
            x = Integer.parseInt(st.nextToken());//throw exception if one of them isn't  int
            temp.add(x);
        }
        if (temp.size() < 2) throw new InValidUserInputException("You have not entered enough rotors\nThe number of rotors set for use of this machine is "
                + rotorscount+". " );
        else if(temp.size()>rotorscount) throw new InValidUserInputException("You have entered too many rotors\nhe number of rotors set for use of this machine is "
                + rotorscount+". " );
        for (int i = 0; i < temp.size(); i++) {
            if (!rotorsList.contains(temp.get(i)))
                ErrMessage += "{" + temp.get(i).toString() + "} ";//adding the invalid rotors to err message
        }
        if (!ErrMessage.equals(""))
            throw new InValidUserInputException("Invalid Rotors stream input -> Invalid rotors Id given in the stream!\n" +
                    "These Rotors " + ErrMessage + " isn't exist in stock \n" + "Please choose from these:\n" +
                    rotorsList + "\n");
    }

    //checks if reflector exist in the reflectors available list + input validate
    public void ReflectorValidation(String input, List<String> ReflectorsList) {
        //checks that the reflector is in stock
        String ErrMessage = "";
        Integer x;
        x = Integer.parseInt(input);//throw exception if one of them isn't  int
        if(x<1||x>ReflectorsList.size())//checks if user chose form option given
            throw new InValidUserInputException("Invalid reflector input -> Invalid/Non existent option chosen !\n");
    }

    //checks if chars in plug input exist in the ABC + input validate
    public void PlugInputValidation(String input,String ABC){
        String ErrMessage = "";
        ArrayList<Character> appearance = new ArrayList<>(ABC.length());
        int numOfPairs=0;
        checkABCBelonging(input,ABC);//checks if there are letters that don't belong to the ABC
        checkSamePlugMapping(input);//checks if there are letters that plugged into themselves or plugged twice or more
        if(input.length()>ABC.length()/2)
            throw new InValidUserInputException("You have entered too many pairs! Maximum size is: " + ABC.length()/2+
                    "\n");
        if(input.length()%2!=0)
            throw new InValidUserInputException("You have entered Odd size of pairs\n please enter even size!");

    }

    private void checkSamePlugMapping(String input){
        String ErrMessage = "";
        Set<Character> Appearance = new HashSet<>();
        for (int i = 0; i < input.length(); i++) {

           if(Appearance.add(input.charAt(i))==false)
               ErrMessage+="{" + input.charAt(i) + "} ";
        }
        if (!ErrMessage.equals(""))
            throw new InValidUserInputException("Invalid Pairs stream input -> these chars appear mapped to themselves or appears in more than on pair: \n"
                    +ErrMessage +",Please be aware of mapping letter plug to itself!.");
    }

    //checks if reflector exist in the reflectors available list + input validate
    public void InitialPosValidation(String input,String rotorsChosen,String ABC){
        String ErrMessage = "";
        int numOfRotors=0;
        StringTokenizer st = new StringTokenizer(rotorsChosen, ",<>");
        while (st.hasMoreTokens()) {
             st.nextToken();//throw exception if one of them isn't  int
            numOfRotors++;
        } // gets number of rotors
       checkABCBelonging(input,ABC);//checks if there are letters that don't belong to the ABC
        if(numOfRotors!=input.length())//checks if user entered less/too many initial poses
            throw new InValidUserInputException("Invalid Initial position stream input -> You have entered "+input.length()+
                    " initial positions while there are "+numOfRotors+" rotors\n" +"\nPlease choose from these:" + ABC);
    }

    //checks if there are letters that don't belong to the ABC
    public void checkABCBelonging(String input,String ABC){
        String ErrMessage = "";
        for (int i = 0; i <input.length(); i++) {//checks if there are letters that don't belong to the ABC
            if(!ABC.contains(String.valueOf(input.charAt(i))))
                if("\n".equals(input.charAt(i)))
                    ErrMessage += "{"+ '\n' +"} ";
                else ErrMessage += "{"+ input.charAt(i) +"} ";
        }
        if (!ErrMessage.equals(""))
            throw new InValidUserInputException("Invalid Initial position stream input, these letters dont exist in the language: \n"+
                    ErrMessage +"\nPlease choose from these: " + ABC);
    }

    public void checkNotEmptyPath(String path){
        if(path.equals(".ser"))
            throw new InValidUserInputException("You have entered empty path! please enter valid path!");
    }


}

