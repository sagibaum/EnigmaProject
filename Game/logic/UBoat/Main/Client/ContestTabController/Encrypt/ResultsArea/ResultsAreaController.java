package Main.Client.ContestTabController.Encrypt.ResultsArea;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ResultsAreaController {
    @FXML
    private TextArea EncryptResultTextArea;

    public void clearTextArea() {
        EncryptResultTextArea.clear();
    }

    public void setEncryptResultTextArea(String s){
        EncryptResultTextArea.setText(s);
    }
    public String getEncryptResultTextArea(){
        return EncryptResultTextArea.getText();
    }
}
