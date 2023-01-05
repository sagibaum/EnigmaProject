package BruteForceTabController;

import BruteForceTabController.CandidatesFlowPane.CandidatesFlowPaneController;
import BruteForceTabController.DictionaryListView.DictionaryListViewController;
import BruteForceTabController.DictionarySearchHbox.DictionaryHboxController;
import BruteForceTabController.InputMessageTextArea.InputMessageTextAreaController;
import DecriptionManagerClass.DM;
import EngineFunctionallity.EngineObject;
import Mission.MissionPauseListener;
import RootController.RootController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import missions.results.MissionResults;
import user.data.transfer.unit.UserDataTransferUnit;

import java.util.function.Consumer;

public class BruteForceController {

    @FXML
    private Label T3CurrMachineConfLable;

    @FXML
    private TextField T3CurrMachinTextFiled;

    @FXML
    private Label T3InputMessageLabel;

    @FXML
    private Label T3DictionaryWordsLabel;


    @FXML
    private Button T3ProcessButton;

    @FXML
    private Button T3ResetButton;

    @FXML
    private Label T3ErrorsLabel;

    @FXML
    private Slider T3AgentAmountSlider;

    @FXML
    private Label T3AgentAmountLabel;

    @FXML
    private ComboBox<String> T3DifficultLevelComboBox;

    @FXML
    private TextField T3MissionSizeTextField;

    @FXML
    private Button T3SetAgentConfButton;

    @FXML
    private Button T3StartButton;

    @FXML
    private Button T3StopButton;

    @FXML
    private Label T3ErrWithMissionSizeLabel;

    @FXML
    private Button T3PauseButton;

    @FXML
    private Button T3ResumeButton;

    @FXML
    private Button T3ClearButton;

    @FXML
    private ProgressBar T3MissionProgressBar;

    RootController rootController;

    @FXML TextArea InputTextAreaComponent;
    @FXML HBox DictionaryHBoxComponent;
    @FXML ListView<String>  DictionaryListViewComponent;

    @FXML FlowPane  CandidatesFlowPaneComponent;

    @FXML private DictionaryHboxController DictionaryHBoxComponentController;
    @FXML private DictionaryListViewController DictionaryListViewComponentController;
    @FXML private InputMessageTextAreaController InputTextAreaComponentController;

    @FXML private CandidatesFlowPaneController CandidatesFlowPaneComponentController;

    private EngineObject Engine;

    private String message;

    private MissionPauseListener isPaused;

    private Alert alert;
    private UserDataTransferUnit UICommunicationUnit;
    private Consumer<MissionResults> resultsConsumer;
    private Consumer<Double> missionsProgressPercentageConsumer;
    private DM dm;
/*
    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }
    public void setBruteForceTabController(EngineObject Engine, UserDataTransferUnit UICommunicationUnit) {
        this.Engine=Engine;
        this.UICommunicationUnit=UICommunicationUnit;
        this.message = "";
        //creat results consumer that will creat new UI methods
        this.resultsConsumer = missionResults -> Platform.runLater(
                () -> CandidatesFlowPaneComponentController.creatNewCandidateBlock(missionResults));//creat method to creat the objects for th UI
        this.missionsProgressPercentageConsumer = missionsProgressPercentage -> Platform.runLater(
                () -> updateProgressBar(missionsProgressPercentage));


    }

    public void setDictionaryListViewComponentController(){
        this.DictionaryListViewComponentController.setController(this,Engine.getDecipher().getDictionary().getDictionaryWords());
    }

    public void setDictionaryHBoxComponentController(){
        this.DictionaryHBoxComponentController.setController(this,Engine);
    }
    @FXML
    private synchronized void updateProgressBar(Double missionsProgressPercentage) {
        DecimalFormat df = new DecimalFormat("0.00");
       // System.out.println("updating progress: " +  missionsProgressPercentage + '\n');
        T3MissionProgressBar.setProgress((missionsProgressPercentage));
        Double percent = (missionsProgressPercentage * 100);
        T3ErrWithMissionSizeLabel.setText((df.format(percent) + "%"));
    }

    public void setDMConfig(){
        T3AgentAmountSlider.setMin(2);
        T3AgentAmountSlider.setMax(Engine.getDecipher().getAgentsNumber());
        T3DifficultLevelComboBox.getItems().add("Easy");
        T3DifficultLevelComboBox.getItems().add("Medium");
        T3DifficultLevelComboBox.getItems().add("Hard");
        T3DifficultLevelComboBox.getItems().add("Impossible");
        T3DifficultLevelComboBox.setValue("Easy");
        DictionaryListViewComponentController.setT3DictionaryListView(Engine.getDecipher().getDictionary().getDictionaryWords());
        InputTextAreaComponentController.clearInputTextArea();
        T3ErrWithMissionSizeLabel.setText("0.0");
        T3SetAgentConfButton.setDisable(true);
    }


    @FXML
    void setDecryptionManager(MouseEvent event) { // sets the settings before starting the mission! when start will activate it will send the input !
        Integer agents = (int) T3AgentAmountSlider.getValue();
        if(Engine.isMachineNull())
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Machine not set yet!");
            alert.setContentText("Please first configure the machine");
            T3MissionSizeTextField.clear();
            alert.showAndWait();
        }
        else if(!Engine.isPlugBoardEmpty())
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Plugboard is NOT empty");
            alert.setContentText("The task assumes that the plugboard must be empty\n Please reconfigure the machine.");
            T3MissionSizeTextField.clear();
            alert.showAndWait();
        }
        else if(!T3MissionSizeTextField.getText().matches("[0-9]+"))
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error with mission size!");
            alert.setContentText("Mission size isnt a number or not positive!");
            T3MissionSizeTextField.clear();
            alert.showAndWait();
        }
        else {
            this.isPaused = new MissionPauseListener();
            this.dm = new DM((int) Math.round(T3AgentAmountSlider.getValue()), Integer.valueOf(T3MissionSizeTextField.getText()), Engine,
                    intToMissionDiff(T3DifficultLevelComboBox.getValue()), this.resultsConsumer,this.missionsProgressPercentageConsumer, message,
                    this.isPaused);
            //before build it -> check that the text area of input isn't null + mission size is integer + when you will creat the difficulty and the
            // agent number initialized it to default number !!!
            T3StartButton.setDisable(false);
            T3SetAgentConfButton.setDisable(true);
            T3MissionProgressBar.setProgress(0);
        }
    }


    @FXML
    void startBruteProcess(MouseEvent event) { // starting process
        if(message.equals("")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Message box is empty!");
            alert.setContentText("Please enter a message you want to decipher");
            T3MissionSizeTextField.clear();
            alert.showAndWait();
        }
        else {
            dm.startProcess();
            T3StopButton.setDisable(false);
            T3PauseButton.setDisable(false);
            T3ResumeButton.setDisable(true);
            T3StartButton.setDisable(true);
            isPaused.setPaused(false);
        }
    }

    @FXML
    void processMessage(MouseEvent event) {
        if(Engine.isMachineNull())
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Machine not set yet!");
            alert.setContentText("Please first configure the machine");
            T3MissionSizeTextField.clear();
            alert.showAndWait();
        }
        else if(!Engine.isPlugBoardEmpty())
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Plugboard is NOT empty");
            alert.setContentText("The task assumes that the plugboard must be empty\n Please reconfigure the machine.");
            T3MissionSizeTextField.clear();
            alert.showAndWait();
        }
        else if( CompareInputTextAreaField("")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Message box is empty!");
            alert.setContentText("Please enter a message you want to decipher");
            T3MissionSizeTextField.clear();
            alert.showAndWait();
        }
        else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Encryption Error");
            if (!Engine.isMachineNull()) {
                try {
                    checkUserInputIsFromDictionary(InputTextAreaComponentController.getInputTextAreaText().toUpperCase());
                    String beforeEnc = InputTextAreaComponentController.getInputTextAreaText();
                    String encrypted = Engine.InputEncryptionOrDecoding(InputTextAreaComponentController.getInputTextAreaText().toUpperCase(), UICommunicationUnit, false);
                    this.message = encrypted;
                    rootController.getT1CurrMachineConfTextArea().setText(UICommunicationUnit.getCurrentMachineConfiguration());
                    String res = "Before Encryption:\n------\n" + beforeEnc + "\n------\nAfter Encryption:\n" + encrypted;
                    InputTextAreaComponentController.setT3InputTextArea(res);
                    T3ProcessButton.setDisable(true);
                    T3ResetButton.setDisable(true);
                    T3ClearButton.setDisable(true);
                    DictionaryHBoxComponentController.setT3WordSearchTextFieldDisabled(true);
                    DictionaryHBoxComponentController.setSelectWordButtonT3Disabled(true);
                    T3SetAgentConfButton.setDisable(false);
                } catch (InValidUserInputException e) {
                    alert.setContentText(e.getCause().getMessage());
                    alert.showAndWait();
                }
            }
        }
    }

    @FXML
    void restartEntireConfiguration(MouseEvent event) {
        T3ErrWithMissionSizeLabel.setText("0.0%");
        T3MissionProgressBar.setProgress(0);
        T3SetAgentConfButton.setDisable(true);
        T3ProcessButton.setDisable(false);
        T3ResetButton.setDisable(false);
        T3ClearButton.setDisable(false);
        DictionaryHBoxComponentController.setT3WordSearchTextFieldDisabled(false);
        DictionaryHBoxComponentController.setSelectWordButtonT3Disabled(false);
        T3StopButton.setDisable(true);
        T3PauseButton.setDisable(true);
        T3ResumeButton.setDisable(true);
        T3StartButton.setDisable(true);
        InputTextAreaComponentController.setT3InputTextArea("");
        CandidatesFlowPaneComponentController.clearCandidatesFlowPane();
        isPaused.setPaused(false);
        ///ENTER HERE THREAD KILL FUNCTIONALITY!:
        dm.killAllThreads(); //stop pressed kill all living threads of missions
    }

    @FXML
    void resetMachineToInitial(MouseEvent event) {
        Engine.ResetCode(UICommunicationUnit);
        alert = new Alert(Alert.AlertType.INFORMATION);
        UICommunicationUnit.setMachineConfBeforeEncryption(UICommunicationUnit.getCurrentMachineConfiguration());
        rootController.getT1CurrMachineConfTextArea().clear();
        rootController.getT1CurrMachineConfTextArea().setText(UICommunicationUnit.getCurrentMachineConfiguration());
        alert.setHeaderText("Code resets successfully!");
        alert.showAndWait();
    }

    public TextField getT3CurrMachineTextFiled() {
        return T3CurrMachinTextFiled;
    }

    @FXML
    void processPauseLogic(MouseEvent event) {
        T3ResumeButton.setDisable(false);
        T3PauseButton.setDisable(true);
        //enter thread pause logic:
        isPaused.setPaused(true);
        dm.pauseAllThreads();
    }

    @FXML
    void processResumeLogic(MouseEvent event) {
        T3ResumeButton.setDisable(true);
        T3PauseButton.setDisable(false);
        //enter thread resume logic
        isPaused.setPaused(false);
        dm.resumeAllThreads();
    }

    @FXML
    void clearText(MouseEvent event) {
        InputTextAreaComponentController.clearInputTextArea();
    }

    private MissionCreator.missionDifficulty intToMissionDiff(String missionDiff) {
        switch (missionDiff){
            case "Easy":
                return MissionCreator.missionDifficulty.EASY;
            case "Medium":
                return MissionCreator.missionDifficulty.MEDIUM;
            case "Hard":
                return MissionCreator.missionDifficulty.HARD;
            case "Impossible":
                return MissionCreator.missionDifficulty.IMPOSSIBLE;
            default:
                return MissionCreator.missionDifficulty.INVALID;
        }
    }

    private void checkUserInputIsFromDictionary(String input) {// checking valid inputs!
        StringTokenizer st = new StringTokenizer(input, " ");
        String caus = "";
        while (st.hasMoreTokens()) {
            String temp = st.nextToken();
            if (!Engine.getDecipher().getDictionary().getDictionaryWords().contains(temp)) {
                caus += temp + " , ";
            }
        }
        if(!caus.equals("")) throw new InValidUserInputException("These word isn't belong to dictionary: "+caus);
    }

    public DM getDm() {
        return dm;
    }

    public boolean CompareInputTextAreaField(String compare){
        return InputTextAreaComponentController.CompareFieldText(compare);
    }
    public void setInputTextArea(String word){
        InputTextAreaComponentController.setT3InputTextArea(word);
    }

    public String getInputTextArea(){
        return InputTextAreaComponentController.getInputTextAreaText();
    }

    public String getWordSelectedFromListView(){
        return DictionaryListViewComponentController.getWordSelectedFromList();
    }

    public void addSetToListView(Set<String> resDict ){
        DictionaryListViewComponentController.setT3DictionaryListView(resDict);
    }
    public void clearListView(){
        DictionaryListViewComponentController.clearT3DictionaryListView();
    }*/
}