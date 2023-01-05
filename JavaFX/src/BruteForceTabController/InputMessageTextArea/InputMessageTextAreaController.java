package BruteForceTabController.InputMessageTextArea;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class InputMessageTextAreaController {
    @FXML
    private TextArea T3InputTextArea;

    public void clearInputTextArea(){
        T3InputTextArea.clear();
    }

    public void setT3InputTextArea(String input){
        T3InputTextArea.setText(input);
    }

    public boolean CompareFieldText(String compare){
        return T3InputTextArea.getText().equals(compare);
    }

    public String getInputTextAreaText() {
        return T3InputTextArea.getText();
    }
}
