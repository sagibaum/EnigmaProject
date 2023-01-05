package Main.Client.MachineTabController.RandomConfController;

import EngineFunctionallity.EngineObject;
import Main.Client.MachineTabController.MachineTabController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import user.data.transfer.unit.UserDataTransferUnit;

public class RandomConfButtonController {

    private MachineTabController machineTabController ;
    private EngineObject Engine;
    private UserDataTransferUnit UICommunicationUnit;
    @FXML
    private Button T1RandomConfButton;
    public void setRandomConfButtonController(MachineTabController machineTabController, EngineObject engine, UserDataTransferUnit UICommunicationUnit) {
        this.machineTabController = machineTabController;
        this.Engine = engine;
        this.UICommunicationUnit = UICommunicationUnit;
    }

    public void setButtonDisable(boolean status){
        this.T1RandomConfButton.setDisable(status);
    }
    @FXML
    void setRandomMachineConfig(MouseEvent event) {
        Engine.AutomaticallyInitialCodeConf(UICommunicationUnit);
        machineTabController.setComponentsAfterCodeConf();
        machineTabController.setDictionaryWords();
    }
}
