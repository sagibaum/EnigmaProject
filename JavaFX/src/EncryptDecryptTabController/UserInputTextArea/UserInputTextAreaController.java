package EncryptDecryptTabController.UserInputTextArea;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class UserInputTextAreaController {
    @FXML
    private TextArea EncryptTextAreaT2;


    public void clearTextArea() {
        EncryptTextAreaT2.clear();
    }

    public void setEncryptTextAreaDisable(boolean status){

    }

    public void setEncryptTextAreaInput(String s){
        EncryptTextAreaT2.setText(s);
    }

    public void setEncryptTextAreaInputEditable(boolean status){
        EncryptTextAreaT2.setEditable(status);
    }
    public String getEncryptTextAreaString(){
        return EncryptTextAreaT2.getText().toUpperCase();
    }
}
