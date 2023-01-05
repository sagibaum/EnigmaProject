package EncryptDecryptTabController;

import EncryptDecryptTabController.KeyBoard.KeyBoardController;
import EncryptDecryptTabController.LightBoard.LightBoardController;
import EncryptDecryptTabController.ResultTextArea.ResultsTextAreaController;
import EncryptDecryptTabController.Stats.StatsController;
import EncryptDecryptTabController.UserInputTextArea.UserInputTextAreaController;
import EngineFunctionallity.EngineObject;
import Exceptions.InValidUserInputException;
import RootController.RootController;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import user.data.transfer.unit.UserDataTransferUnit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class EncryptDecryptTabController {

    @FXML
    private Label CurrMachineConfLabelT2;

    @FXML
    private Label EncryptDecryptLabelT2;

    @FXML
    private Label StatisticsLabelT2;

    @FXML
    private Label ArrowLabelT2;


    @FXML
    private TextField MachineConfTextFieldT2;

    @FXML
    private Button EncryptButtonT2;

    @FXML
    private Button T2ClearButton;

    @FXML
    private Button ResetButtonT2;

    @FXML
    private Button ShowStatsButtonT2;


    @FXML
    private Button T2DoneButton;

    @FXML
    private RadioButton byLettersRadioButton;
    @FXML
    private HBox processHBox;

    private EngineObject Engine;
    private Alert alert;
    private UserDataTransferUnit UICommunicationUnit;


    @FXML ScrollPane KeyBoardComponent,BulbBoardComponent;

    @FXML TextArea StatsComponent,UserInputComponent,ResultComponent;

    @FXML private UserInputTextAreaController UserInputComponentController;
    @FXML private ResultsTextAreaController ResultComponentController;
    @FXML private StatsController StatsComponentController;
    @FXML private KeyBoardController KeyBoardComponentController;
    @FXML private LightBoardController BulbBoardComponentController;

    private RootController rootController;
    private FlowPane T2keyboardFlowPane;
    private FlowPane T2bulbFlowPane;
    private Map<String,Button> bulbs;

    private List<Button> activatedAnimation;
    private HashMap<Button, FadeTransition> BulbAnimations;
    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }


    public void setEncryptTabButtonsUnavailable(boolean status) {
        EncryptButtonT2.setDisable(status);
        //EncryptTextAreaT2.setEditable(!status);
        UserInputComponentController.setEncryptTextAreaDisable(!status);
        ResetButtonT2.setDisable(status);
        ShowStatsButtonT2.setDisable(status);
        processHBox.setDisable(status);
        //T2keyboardScrollPane.setDisable(status);
        KeyBoardComponentController.setT2keyboardScrollPaneDisable(status);
        T2ClearButton.setDisable(status);
    }

    public TextField getMachineConfTextFieldT2() {
        return MachineConfTextFieldT2;
    }

    //setter of this controller
    public void setEncryptTabController(EngineObject engine, UserDataTransferUnit UICommunicationUnit) {
        this.Engine = engine;
        //EncryptTextAreaT2.setEditable(false);
        this.UICommunicationUnit = UICommunicationUnit;
       // this.activatedAnimation = new ArrayList<Button>();
        if(UserInputComponentController !=null&& ResultComponentController !=null&& StatsComponentController!=null&& KeyBoardComponentController !=null
        && BulbBoardComponentController !=null)
        {
            setKeyBoardController();
            setLightBoardController();
            UserInputComponentController.setEncryptTextAreaInputEditable(false);
        }

    }


    //it will generate after user selects the xml file
    public void setKeyBoardController( ) {
        KeyBoardComponentController.setKeyBoardController(Engine,this,UICommunicationUnit);

    }

    //it will generate after user selects the xml file
    public void setLightBoardController( ) {
        BulbBoardComponentController.setBulbBoardController(Engine,this);
    }

    @FXML
    private void updateCurrentMachineConfigTextFields() { //this updates all current machine config text fields
       /* rootController.getT1CurrMachineConfTextArea().clear();
        rootController.getT1CurrMachineConfTextArea().setText(UICommunicationUnit.getCurrentMachineConfiguration());*/
    }

    @FXML
    void toggleEncryptionButtonAction(ActionEvent event) {
        if (byLettersRadioButton.isSelected()) {
            UICommunicationUnit.setMachineConfBeforeEncryption(UICommunicationUnit.getCurrentMachineConfiguration());
            EncryptButtonT2.setDisable(true);
            T2DoneButton.setDisable(false);
            KeyBoardComponentController.setT2keyboardFlowPaneDisable(false);
            UserInputComponentController.setEncryptTextAreaInputEditable(false);
            UserInputComponentController.setEncryptTextAreaInput("");
            ResultComponentController.setEncryptResultTextArea("");

        } else {
            EncryptButtonT2.setDisable(false);
            T2DoneButton.setDisable(true);
            KeyBoardComponentController.setT2keyboardFlowPaneDisable(true);
            UserInputComponentController.setEncryptTextAreaInputEditable(true);
            UserInputComponentController.setEncryptTextAreaInput("");
            ResultComponentController.setEncryptResultTextArea("");

        }
        if(!KeyBoardComponentController.isActivatedAnimationListEmpty())
            stopAnimation(KeyBoardComponentController.removeAndReturnFromActivatedAnimationList(0));//stops animation if on activated
    }

    @FXML
    void encryptMessageAndDisplay(MouseEvent event) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Encryption Error");
        if (!Engine.isMachineNull()) {
            try {
                String encrypted = Engine.InputEncryptionOrDecoding(UserInputComponentController.getEncryptTextAreaString(), UICommunicationUnit, false);
                if (!byLettersRadioButton.isSelected())
                    ResultComponentController.setEncryptResultTextArea(encrypted);
                updateCurrentMachineConfigTextFields();
            } catch (InValidUserInputException e) {
                alert.setContentText(e.getCause().getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    void addToStatsAndClearLong(MouseEvent event) { //when pressing "done"
        String val = UserInputComponentController.getEncryptTextAreaString() + " --> " + ResultComponentController.getEncryptResultTextArea() + " ( " + UICommunicationUnit.getEncryptionTime() + " nano seconds )\n";
        Engine.addStat(UICommunicationUnit.getMachineConfBeforeEncryption(), val);
        UICommunicationUnit.setEncryptionTime(0L);
        UserInputComponentController.setEncryptTextAreaInput("");
        ResultComponentController.setEncryptResultTextArea("");
        //after pressing done, need to save the current machine conf as before encrypting
        UICommunicationUnit.setMachineConfBeforeEncryption(UICommunicationUnit.getCurrentMachineConfiguration());
        if(!KeyBoardComponentController.isActivatedAnimationListEmpty())
            stopAnimation(KeyBoardComponentController.removeAndReturnFromActivatedAnimationList(0));//stops animation if on activated
    }

    @FXML
    void resetMachineToInitial(MouseEvent event) {
        this.Engine.ResetCode(UICommunicationUnit);
        updateCurrentMachineConfigTextFields();
        alert = new Alert(Alert.AlertType.INFORMATION);
        UICommunicationUnit.setMachineConfBeforeEncryption(UICommunicationUnit.getCurrentMachineConfiguration());
        alert.setHeaderText("Code resets successfully!");
        UserInputComponentController.clearTextArea();
        ResultComponentController.clearTextArea();
       // rootController.getT1CurrMachineConfTextArea().setText(UICommunicationUnit.getCurrentMachineConfiguration());
        alert.showAndWait();
    }

    @FXML
    void displayStats(MouseEvent event) {
        StatsComponentController.setTextArea(Engine.MachineHistoryAndStatistics());
    }

    public void setKeyBoardAndBulbBoard() {
        String ABC = Engine.getAbc();
       KeyBoardComponentController.setKeyBoardFunctionality();
       BulbBoardComponentController.setBulbBoardFunctionality();

    }

    @FXML
    void clearFields(MouseEvent event) {
        UserInputComponentController.clearTextArea();
       // EncryptTextAreaT2.clear();
        ResultComponentController.clearTextArea();
        //EncryptResultTextAreaT2.clear();
    }

    public void startAnimation(Button L){// animations start method connects keyboard and light board
        BulbBoardComponentController.makeGlowingAnimation(L);
    }

    public void stopAnimation(Button L){ // animations stop method connects keyboard and light board
        BulbBoardComponentController.stopGlowingAnimation(L);
    }

    public Button getSpecifiedBulb(String Encrypt){//function to connect between keyboard and bulb board
        return BulbBoardComponentController.getBulb(Encrypt);
    }

    public void setEncryptResultTextArea(String s) {
        ResultComponentController.setEncryptResultTextArea(s);
    }

    public String getEncryptResultTextArea() {
        return ResultComponentController.getEncryptResultTextArea();
    }

    public void setUserEncryptTextAreaInput(String s) {
        UserInputComponentController.setEncryptTextAreaInput(s);
    }

    public String getUserEncryptTextAreaInput() {
        return UserInputComponentController.getEncryptTextAreaString();
    }

    public void clearMachineConfTextFiled(){
        MachineConfTextFieldT2.clear();
    }

    public void setFatherCurrMachineConfTextArea(String currentMachineConfiguration) {
      //  rootController.getT1CurrMachineConfTextArea().setText(currentMachineConfiguration);
    }
}
