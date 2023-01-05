package Main.Client.MachineTabController.MachineConfController;

import Main.Client.MachineTabController.MachineTabController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import user.data.transfer.unit.UserDataTransferUnit;

public class CurrentMachineConfController {
    private MachineTabController machineTabController ;
    @FXML
    private TextArea T1CurrMachineConfTextArea;

    public void setCurrentMachineTextAreaController(MachineTabController machineTabController) {
        this.machineTabController = machineTabController;
    }

    @FXML
    public void updateCurrentMachineConfigTextFields(UserDataTransferUnit UICommunicationUnit){ //this updates all current machine config text fields
        T1CurrMachineConfTextArea.clear();
        T1CurrMachineConfTextArea.setText(UICommunicationUnit.getCurrentMachineConfiguration());
    }

    public TextArea getCurrMachineConfTextArea() {
        return T1CurrMachineConfTextArea;
    }
}
