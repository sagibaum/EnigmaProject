package parts.reflector;

import schem.out.CTEReflector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Reflector implements  ReflectorInterface, Serializable {

    private String id; // should we insert here an enum ? for the symbols ? public enum{I,II,III,IV ,V} ?

    private List<Reflect> arrReflect;

    public Reflector(CTEReflector R) {
        this.setId(R.getId());
        arrReflect=new ArrayList<>(R.getCTEReflect().size());
        for (int i = 0; i < R.getCTEReflect().size(); i++) {
        Reflect r =  new Reflect(R.getCTEReflect().get(i).getInput() - 1,R.getCTEReflect().get(i).getOutput() - 1);
            arrReflect.add(r);
        }//Java supports autoboxing and automatically wraps primitive values
        // into objects and unwraps objects to primitive values for certain types, like char - Character, int - Integer

    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Integer returnLocationIndex(Integer indexOfInputChar) {
        //this function searches for the input , reflects and returns the desired one
        for (Reflect reflect : arrReflect) {
            if (reflect.GetOutput().equals(indexOfInputChar))
                return reflect.GetInput();
            else if (reflect.GetInput().equals(indexOfInputChar))
                return reflect.GetOutput();
        }
       return -1;//problem occur-> if its value return that's means that the reflector is not valid
    }

    public List<Reflect> getArrReflect() {
        return arrReflect;
    }

    @Override
    public String toString() {
        return "Reflector{" +
                "id='" + id + '\'' +
                ", arrReflect=" + arrReflect +
                '}';
    }
}
