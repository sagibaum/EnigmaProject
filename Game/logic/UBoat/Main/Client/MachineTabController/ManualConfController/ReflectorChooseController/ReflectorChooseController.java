package Main.Client.MachineTabController.ManualConfController.ReflectorChooseController;



import Main.Client.MachineTabController.ManualConfController.ManualConfigurationController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class ReflectorChooseController {
    @FXML // fx:id="reflectorsCheckBox"
    private ComboBox<String> reflectorsComboBox;; // Value injected by FXMLLoader

    @FXML // fx:id="submitReflectorButton"
    private Button submitReflectorButton; // Value injected by FXMLLoader

    private Alert alert;
    private String Output;
    private List<String> Reflectors;

    private ManualConfigurationController manualConfigurationController;

    public void setReflectorsComboBoxValue(String value) {
        this.reflectorsComboBox.setValue(value);
    }

    public String getOutput() {
        return Output;
    }

    public void setOutput(String output) {
        Output = output;
    }

    public ComboBox<String> getReflectorsComboBox() {
        return reflectorsComboBox;
    }

    public Button getSubmitReflectorButton() {
        return submitReflectorButton;
    }

    public void setManualConfController(ManualConfigurationController manualConfController) {
        this.manualConfigurationController = manualConfController;
    }
    //initialize reflector component
    public void setReflectorComponent(String UnitUIMessage,List<String> reflectors){
        this.Output = UnitUIMessage;
        this.Reflectors = reflectors;
        reflectorsComboBox.getItems().addAll(Reflectors);
        reflectorsComboBox.setVisibleRowCount(5);
        reflectorsComboBox.setValue("I");
    }

    @FXML
    void submitReflector(MouseEvent event) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        String choice =reflectorsComboBox.getValue();
        try {
            Output+= "<" + choice + ">";
            manualConfigurationController.getUnit().setUIMessageInput( manualConfigurationController.getUnit().getUIMessageInput()+
                    Output);
            submitReflectorButton.setDisable(true);
            reflectorsComboBox.setDisable(true);
            manualConfigurationController.setPlugBoardSubmitButton(false);
            manualConfigurationController.setPlugBoardScrollPane(false);
            alert.setHeaderText("Reflector Choosing Process");
            alert.setContentText("Operation succeed!");
            alert.showAndWait();
        }
        catch (Exception e){
            //System.out.println(e.getCause().getMessage());}

    }

}
}
