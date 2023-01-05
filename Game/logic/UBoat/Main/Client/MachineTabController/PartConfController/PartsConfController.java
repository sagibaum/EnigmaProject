package Main.Client.MachineTabController.PartConfController;

import Main.Client.MachineTabController.MachineTabController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class PartsConfController{

    private MachineTabController machineTabController ;
    @FXML
    private TextArea T1PartTextArea;

    public void setPartsConfController(MachineTabController machineTabController) {
        this.machineTabController=machineTabController;
    }

    public void setPartTextArea(String value){
        this.T1PartTextArea.setText(value);
    }
}
