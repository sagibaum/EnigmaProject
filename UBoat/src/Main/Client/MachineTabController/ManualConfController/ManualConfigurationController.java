package Main.Client.MachineTabController.ManualConfController;

import EngineFunctionallity.EngineObject;
import Main.Client.MachineTabController.ManualConfController.InitialPosController.RotorsInitPosController;
import Main.Client.MachineTabController.ManualConfController.PlugBoardController.PlugBoardController;
import Main.Client.MachineTabController.ManualConfController.ReflectorChooseController.ReflectorChooseController;
import Main.Client.MachineTabController.ManualConfController.RotorsSelectController.RotorsSelectController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import user.data.transfer.unit.UserDataTransferUnit;

import java.net.URL;
import java.util.ResourceBundle;

public class ManualConfigurationController implements Initializable  {


    @FXML GridPane plugBoardComponent,rotorsSelectComponent,rotorsInitComponent;
    @FXML
    HBox reflectorsComponent;
    @FXML private RotorsInitPosController rotorsInitComponentController;
    @FXML private ReflectorChooseController reflectorsComponentController;
    @FXML private PlugBoardController plugBoardComponentController;
    @FXML  private RotorsSelectController rotorsSelectComponentController;
    @FXML
    private Line separator1;

    @FXML
    private Line separator2;
    @FXML
    private javafx.scene.control.Button resetAllProccessButton;
    private EngineObject Engine;

    private Alert alert;

    private UserDataTransferUnit Unit;

    public ManualConfigurationController() {

    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void  setManualConfController(EngineObject engine, UserDataTransferUnit unit,Stage stage) {
        this.Engine=engine;
        this.Unit = unit;
        Unit.setUIMessageInput("");
        Unit.setOperationStatus(false);
        if(rotorsInitComponentController!=null&&reflectorsComponentController!=null&&plugBoardComponentController!=null&&
                rotorsSelectComponentController!=null)
        {
            setRotorsControllers();
            setRotorInitController();
            setReflectorController();
            setPlugBoardController();
        }
        stage.widthProperty().addListener((observable, oldValue, newValue) ->{
            separator1.setStartX(stage.getWidth()+150);
            separator2.setStartX(stage.getWidth()+150);
        } );

    }

    private void setRotorsControllers(){
        rotorsSelectComponentController.setManualConfController(this);
        rotorsSelectComponentController.setInterface(Unit.getUIMessageInput(),Engine.getRotorsAvailable(),Engine.getMachineRotorsNumConf());

    }

    private void setRotorInitController(){
        rotorsInitComponentController.setManualConfController(this);
        rotorsInitComponentController.setInitPos(Unit.getUIMessageInput(), Engine.getAbc());
    }
    private void setReflectorController(){
        reflectorsComponentController.setManualConfController(this);
        reflectorsComponentController.setReflectorComponent(Unit.getUIMessageInput(),Engine.getReflectorsAvailable());
    }

    private void setPlugBoardController(){
        plugBoardComponentController.setManualConfController(this);
        plugBoardComponentController.SetPlugboardControllerABC(Engine.getAbc());
        plugBoardComponentController.setPlugBoardScrollPane(Unit.getUIMessageInput(),true);

    }

    //starts the init processes of creating the choice boxes !
    public void setInitProcess(){
        rotorsInitComponentController.setComponents(rotorsSelectComponentController.getRotorsOrdered());
    }

    // for freeing the init step submit button
    public void setDisableStatusOfInitSubmitButton(boolean status){
        rotorsInitComponentController.getSubmitRotorsInitPosButton().setDisable(status);
        rotorsInitComponentController.getInitPosChoiceBoxViewList().setDisable(status);

    }
    public void setRotorChooseDisable(boolean status){
        reflectorsComponentController.getSubmitReflectorButton().setDisable(status);
        reflectorsComponentController.getReflectorsComboBox().setDisable(status);
    }

    public  void setPlugBoardSubmitButton(boolean status){
        plugBoardComponentController.getSubmitPlugPairButton().setDisable(status);
        plugBoardComponentController.getResetPlugButton().setDisable(status);
    }

    public UserDataTransferUnit getUnit() {
        return Unit;
    }

    public void closeWindow(){
        Unit.setOperationStatus(true);
        Stage stage = (Stage) resetAllProccessButton.getScene().getWindow();//this will close window!move to father function
        stage.close();
    }

    public void setPlugBoardScrollPane(boolean status){
        plugBoardComponentController.getPlugBoardScrollPane().setDisable(status);
    }
    @FXML
    void resetAllProccess(ActionEvent event) {
        reflectorsComponentController.getSubmitReflectorButton().setDisable(true);
        reflectorsComponentController.setReflectorsComboBoxValue("I");
        reflectorsComponentController.setOutput("");
        //-------------------------------------------------------------------------------//
        rotorsInitComponentController.getSubmitRotorsInitPosButton().setDisable(true);
        rotorsInitComponentController.getInitPosChoiceBoxViewList().getItems().clear();
        rotorsInitComponentController.getInitPosChoiceBoxViewList().setDisable(true);
        rotorsInitComponentController.setOutput("");
        //-------------------------------------------------------------------------------//
        rotorsSelectComponentController.getRotorsAvailableCheckViewList().getItems().stream().unordered().forEach(checkBox -> checkBox.setSelected(false));
        rotorsSelectComponentController.getRotorsAvailableCheckViewList().setDisable(false);
        rotorsSelectComponentController.getSelectRotorsButton().setDisable(false);
 /*       rotorsSelectComponentController.getRotorsNumComboBox().setDisable(false);
        rotorsSelectComponentController.getRotorsNumComboBox().setValue(2);*/
        rotorsSelectComponentController.getRotorsPositionCheckViewList().getItems().clear();
        rotorsSelectComponentController.getRotorsOrdered().getItems().clear();
        rotorsSelectComponentController.setOutput("");
        //-------------------------------------------------------------------------------//
        plugBoardComponentController.getPlugPairTextField().clear();
        plugBoardComponentController.getPlugBoardScrollPane().setDisable(true);
        plugBoardComponentController.setPlugBoardScrollPane(Unit.getUIMessageInput(),true);
        plugBoardComponentController.setOutput("");
        Unit.setUIMessageInput("");

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Reset All values operation");
        alert.setContentText("Reset Operation went successful!");
    }

    //add here func that getb if a button has pressed and went successfully


}
