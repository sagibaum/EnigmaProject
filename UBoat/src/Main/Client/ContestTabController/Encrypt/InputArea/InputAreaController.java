package Main.Client.ContestTabController.Encrypt.InputArea;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class InputAreaController {
    @FXML
    private TextArea InputTextArea;


    public void clearInputTextArea(){
        InputTextArea.clear();
    }

    public void setInputTextArea(String input){
        InputTextArea.setText(input);
    }

    public boolean CompareFieldText(String compare){
        return InputTextArea.getText().equals(compare);
    }

    public String getInputTextAreaText() {
        return InputTextArea.getText();
    }


    public void setEncryptTextAreaInput(String s){
        InputTextArea.setText(s);
    }

    public void setEncryptTextAreaInputEditable(boolean status){
        InputTextArea.setEditable(status);
    }

    public void setInputTextAreaDisable(boolean status) {
        InputTextArea.setDisable(status);
    }
}
