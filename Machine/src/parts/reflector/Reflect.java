package parts.reflector;

import java.io.Serializable;

public class Reflect implements Serializable {
    //how the reflector is arranged
    private Integer input ;
    private Integer output;
    public Reflect(Integer input,Integer output){
        this.input=input;
        this.output=output;
    }
    public Integer GetInput(){return this.input;}
    public Integer GetOutput(){return this.output;}

    public void setInput(Integer input) {
        this.input = input;
    }

    public void setOutput(Integer output) {
        this.output = output;
    }
}
