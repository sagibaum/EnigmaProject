package Main.Client.MachineTabController.ManualConfController.InitialPosController;


import Main.Client.MachineTabController.ManualConfController.ManualConfigurationController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class RotorsInitPosController {

    @FXML // fx:id="InitPosChoiceBoxViewList"
    private ListView<HBox> InitPosChoiceBoxViewList; // Value injected by FXMLLoader

    @FXML // fx:id="submitRotorsInitPosButton"
    private Button submitRotorsInitPosButton; // Value injected by FXMLLoader
    private Alert alert;

    private String Output,ABC;

    private ListView<HBox> components;

    private ManualConfigurationController ManualConfigurationController;


    public String getOutput() {
        return Output;
    }

    public void setOutput(String output) {
        Output = output;
    }

    public Button getSubmitRotorsInitPosButton() {
        return submitRotorsInitPosButton;
    }

    public ListView<HBox> getInitPosChoiceBoxViewList() {
        return InitPosChoiceBoxViewList;
    }

    public ListView<HBox> getComponents() {
        return components;
    }

    public void setManualConfController(ManualConfigurationController manualConfController) {
        this.ManualConfigurationController = manualConfController;
    }

    //initialized component fields
    public void setInitPos(String output , String abc){
        Output = output;
        ABC = abc;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        components = new ListView<HBox>();
    }

    @FXML
    void submitInitPos(MouseEvent event) {
        String input="";
        for (int i = 0; i < InitPosChoiceBoxViewList.getItems().size(); i++) {
            HBox temp = InitPosChoiceBoxViewList.getItems().get(i);//getting the HBOX
            ComboBox<String> tempCombo = (ComboBox<String>) temp.getChildren().get(1);//getting the combobox and its value
            input+=tempCombo.getValue();
        }
        try {
            Output+="<"+input+">";
            ManualConfigurationController.getUnit().setUIMessageInput( ManualConfigurationController.getUnit().getUIMessageInput()+
                    Output);
            submitRotorsInitPosButton.setDisable(true);
            InitPosChoiceBoxViewList.setDisable(true);
            ManualConfigurationController.setRotorChooseDisable(false);//enable next step
            alert.setHeaderText("Rotors Initial Position Choosing Process");
            alert.setContentText("Operation succeed!");
            alert.showAndWait();
        }
        catch (Exception e ){
            //System.out.println(e.getCause().toString());
        }
    }

    public  void setComponents(ListView<CheckBox> RotorsAvailableCheckViewList){
        ListView<HBox> temp = new ListView<HBox>();
        alert.setHeaderText("Rotors Choosing Process");
        Integer counter = 0;
        String rotors = "";
        for (int i = 0; i < RotorsAvailableCheckViewList.getItems().size(); i++) {
            if (RotorsAvailableCheckViewList.getItems().get(i).isSelected()) {
                rotors += RotorsAvailableCheckViewList.getItems().get(i).getText() + ",";
                temp.getItems().add(creatNewInitHBoxPair(RotorsAvailableCheckViewList.getItems().get(i).getText(),ABC));
            }
        }
        InitPosChoiceBoxViewList.getItems().addAll(temp.getItems());

    }

    private HBox creatNewInitHBoxPair(String id, String ABC) {
        HBox newOne = new HBox();
        newOne.setSpacing(10);
        ComboBox<String> rotor = new ComboBox<String>();

        for (int i = 0; i < ABC.length(); i++) {
            rotor.getItems().add(String.valueOf(ABC.charAt(i)));
        }
        rotor.setVisibleRowCount(5);
        rotor.setValue("A");
        newOne.getChildren().add(new javafx.scene.control.Label(id +"->initial pos:"));
        newOne.getChildren().add(rotor);


        return newOne;
    }



}
