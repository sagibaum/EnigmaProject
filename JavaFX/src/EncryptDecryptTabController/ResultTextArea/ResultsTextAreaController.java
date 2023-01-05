package EncryptDecryptTabController.ResultTextArea;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ResultsTextAreaController {
    @FXML
    private TextArea EncryptResultTextAreaT2;

    public void clearTextArea() {
        EncryptResultTextAreaT2.clear();
    }

    public void setEncryptResultTextArea(String s){
        EncryptResultTextAreaT2.setText(s);
    }
    public String getEncryptResultTextArea(){
        return EncryptResultTextAreaT2.getText();
    }
}
