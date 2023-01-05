package Main.Client.ContestTabController;

import EngineFunctionallity.EngineObject;
import Exceptions.InValidUserInputException;
import Main.Client.ContestTabController.Candidates.TableCandidate;
import Main.Client.ContestTabController.DictionaryList.DictionaryListController;
import Main.Client.ContestTabController.DictionarySearch.DictionarySearchController;
import Main.Client.ContestTabController.Encrypt.InputArea.InputAreaController;
import Main.Client.ContestTabController.Encrypt.ResultsArea.ResultsAreaController;
import Main.Client.UBoatClientController;
import ServerDTOs.Uboat.TableDataObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import missions.results.MissionResults;
import user.data.transfer.unit.UserDataTransferUnit;

import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;

public class UBoatContestController {

    @FXML
    private Button LogOutButton;

    @FXML
    private Label ArrowLabel;

    @FXML
    private Label winnerLabel,gameStatus;

    @FXML
    private Button ProcessButton;

    @FXML
    private Button ClearButton;

    @FXML
    private Button ResetButton;

    @FXML
    private Button ResetAllDataButton;

    @FXML
    private ToggleButton ReadyButton;

    @FXML
    private TextField MachineConfTextField;

    @FXML
    private TableView<TableDataObject> ActiveTeamsTable;

    @FXML TableColumn<TableDataObject,String>  alliesNameCol;
    @FXML TableColumn<TableDataObject,String>  numOfAgentsCol;
    @FXML TableColumn<TableDataObject,String>  missionSizeCol;
    @FXML TableColumn<TableDataObject,String>  readyStatusCol;


    @FXML
    private TableView<TableCandidate> CandidatesTable;

    @FXML TableColumn<TableCandidate,String>  AllyNameCol;
    @FXML TableColumn<TableCandidate,String>  EncryptionTimeCol;
    @FXML TableColumn<TableCandidate,String>  CodeConfigurationCol;
    @FXML TableColumn<TableCandidate,String>  DecryptedMessageCol;



    @FXML private ScrollPane TableScrollPane,CandidatesScrollPane;

    @FXML private TextArea ResultsComponent,InputComponent;

   // @FXML private FlowPane CandidatesComponent;

    @FXML ListView<String> DictionaryListComponent;

    @FXML HBox DictionarySearchComponent;

    @FXML private DictionarySearchController DictionarySearchComponentController;
    @FXML private DictionaryListController DictionaryListComponentController;
    @FXML private ResultsAreaController ResultsComponentController;
    @FXML private InputAreaController InputComponentController;

    //@FXML private CandidatesController CandidatesComponentController;

    private UBoatClientController fatherController;
    private EngineObject Engine;
    UserDataTransferUnit UICommunicationUnit;

    private String message , messageBeforeDecrypt;


    public void setContestTab(UBoatClientController uBoatClientController,EngineObject engine,UserDataTransferUnit userDataTransferUnit){
        //AlliesTable = new TableView<TableDataObject>(); // for saving Allies info from server .
        this.UICommunicationUnit=userDataTransferUnit;
        this.Engine=engine;
        this.fatherController = uBoatClientController;
        ReadyButton.getStylesheets().add(String.valueOf((getClass().getResource("ReadyButton.css".toString()))));
        message = "";
        setGameStatusLabel("IDLE");
        setAlliesTable();//sets table observable properties
        setCandidatesTable();
    }

    private void setCandidatesTable() {
        CandidatesTable =new TableView<>();
        CandidatesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        AllyNameCol.setCellValueFactory(new PropertyValueFactory<>("allyName")); //Ally name set up
        EncryptionTimeCol.setCellValueFactory(new PropertyValueFactory<>("encryptionTime"));//Agents num set up
        DecryptedMessageCol.setCellValueFactory(new PropertyValueFactory<>("decryptedMessage"));//Mission size num set up
        CodeConfigurationCol.setCellValueFactory(new PropertyValueFactory<>("codeConfiguration"));//Ready status num set up
        AllyNameCol.setStyle("-fx-alignment: CENTER;");
        DecryptedMessageCol.setStyle("-fx-alignment: CENTER;");
        CodeConfigurationCol.setStyle("-fx-alignment: CENTER;");
        EncryptionTimeCol.setStyle("-fx-alignment: CENTER;");
        CandidatesTable.getColumns().addAll(AllyNameCol,EncryptionTimeCol,CodeConfigurationCol,DecryptedMessageCol);
        CandidatesScrollPane.setContent(CandidatesTable);

    }


    private void  setAlliesTable(){
        ActiveTeamsTable = new TableView<>();
        ActiveTeamsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        alliesNameCol.setCellValueFactory(new PropertyValueFactory<>("allyName")); //Ally name set up
        numOfAgentsCol.setCellValueFactory(new PropertyValueFactory<>("agentsNum"));//Agents num set up
        missionSizeCol.setCellValueFactory(new PropertyValueFactory<>("missionSize"));//Mission size num set up
        readyStatusCol.setCellValueFactory(new PropertyValueFactory<>("readyStatus"));//Ready status num set up
        alliesNameCol.setStyle("-fx-alignment: CENTER;");
        numOfAgentsCol.setStyle("-fx-alignment: CENTER;");
        missionSizeCol.setStyle("-fx-alignment: CENTER;");
        readyStatusCol.setStyle("-fx-alignment: CENTER;");
        ActiveTeamsTable.getColumns().addAll(alliesNameCol,numOfAgentsCol,missionSizeCol,readyStatusCol);
        TableScrollPane.setContent(ActiveTeamsTable);
    }
    public void setDictionaryListViewComponentController(){
        this.DictionaryListComponentController.setController(this,Engine.getDecipher().getDictionary().getDictionaryWords());
    }

    public void setDictionaryHBoxComponentController(){
        this.DictionarySearchComponentController.setController(this,Engine);
    }
    public TextField getMachineConfTextField() {
        return MachineConfTextField;
    }

    public boolean CompareInputTextAreaField(String compare){
        return InputComponentController.CompareFieldText(compare);
    }
    public void setInputTextArea(String word){
        InputComponentController.setInputTextArea(word);
    }
    public String getInputTextArea(){
        return InputComponentController.getInputTextAreaText();
    }

    public String getMessageBeforeDecrypt() {
        return messageBeforeDecrypt;
    }

    public String getWordSelectedFromListView(){
        return DictionaryListComponentController.getWordSelectedFromList();
    }

    public void addSetToListView(Set<String> resDict ){
        DictionaryListComponentController.setT3DictionaryListView(resDict);
    }
    public void clearListView(){
        DictionaryListComponentController.clearT3DictionaryListView();
    }


    @FXML
    void processMessage(MouseEvent event) {
        Alert alert;
        if(Engine.isMachineNull())
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Machine not set yet!");
            alert.setContentText("Please first configure the machine");
            alert.showAndWait();
        }
        else if(!Engine.isPlugBoardEmpty())
        {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Plugboard is NOT empty");
            alert.setContentText("The task assumes that the plugboard must be empty\n Please reconfigure the machine.");
            alert.showAndWait();
        }
        else if( CompareInputTextAreaField("")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Message box is empty!");
            alert.setContentText("Please enter a message you want to decipher");
            alert.showAndWait();
        }
        else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Encryption Error");
            if (!Engine.isMachineNull()) {
                try {
                    checkUserInputIsFromDictionary(InputComponentController.getInputTextAreaText().toUpperCase());
                    String beforeEnc = InputComponentController.getInputTextAreaText();
                    String encrypted = Engine.InputEncryptionOrDecoding(InputComponentController.getInputTextAreaText().toUpperCase(), UICommunicationUnit, false);
                    this.message = encrypted;
                    this.messageBeforeDecrypt =beforeEnc;
                    fatherController.setCurrMachineConfTextArea(UICommunicationUnit.getCurrentMachineConfiguration());
                    String res = "Before Encryption:\n------\n" + beforeEnc + "\n------\nAfter Encryption:\n" + encrypted;
                    ResultsComponentController.setEncryptResultTextArea(res);
                    InputComponentController.clearInputTextArea();
                    ProcessButton.setDisable(true);
                    ResetButton.setDisable(false);
                    ClearButton.setDisable(true);
                    DictionaryListComponentController.setDictionaryListViewDisable(true);
                    DictionarySearchComponentController.setT3WordSearchTextFieldDisabled(true);
                    ReadyButton.setDisable(false);
                    InputComponentController.setInputTextAreaDisable(true);
                    updateCompetitionDetails();
                } catch (InValidUserInputException e) {
                    alert.setContentText(e.getCause().getMessage());
                    alert.showAndWait();
                }
            }
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

    @FXML
    void clearFields(MouseEvent event) {
        InputComponentController.clearInputTextArea();
    }

    @FXML
    void resetMachineToInitial(MouseEvent event) {
        Engine.ResetCode(UICommunicationUnit);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        UICommunicationUnit.setMachineConfBeforeEncryption(UICommunicationUnit.getCurrentMachineConfiguration());
        fatherController.clearCurrMachineConfTextArea();
        fatherController.setCurrMachineConfTextArea(UICommunicationUnit.getCurrentMachineConfiguration());
        ProcessButton.setDisable(false);
        ReadyButton.setDisable(true);
        ResultsComponentController.clearTextArea();
        InputComponentController.clearInputTextArea();
        DictionarySearchComponentController.clearSearchField();
        DictionaryListComponentController.setDictionaryListViewDisable(false);
        DictionarySearchComponentController.setT3WordSearchTextFieldDisabled(false);
        InputComponentController.setInputTextAreaDisable(false);
        alert.setHeaderText("Code resets successfully!");
        alert.showAndWait();
    }


    @FXML void LogOut(MouseEvent event) {//when logout button is pressed
        fatherController.close();
        fatherController.logout();// main controller will accomplish the logout process

    }

    void buttonFunctionalityUnpressed(){
        ReadyButton.setStyle("-fx-background-color:green");
        ReadyButton.setText("Ready!");

    }

    void buttonFunctionalityPressed(){
        ReadyButton.setStyle("-fx-background-color:red");
        ReadyButton.setText("Unready :(");

    }

    @FXML public void ReadyUnReadyCompetition(ActionEvent actionEvent) {

        if(!ReadyButton.isSelected()) // means that user wants to do unready process
        {
            //send to the server that he isn't ready! -> same servlet as the ready but without body of machine ,dictionary and so ..
            buttonFunctionalityUnpressed();
            fatherController.sendServerReadyNotice(false);
            ResetButton.setDisable(false);
        }
        else // user announce the server that he is ready
        {
            buttonFunctionalityPressed();
            fatherController.sendServerReadyNotice(true);
            ResetButton.setDisable(true);
        }
    }

    private void  updateCompetitionDetails(){ // updates server contest details
        ArrayList<String> MachineInfo = new ArrayList<String>();
        MachineInfo.add(fatherController.getEngine().getInitialMachineConfiguration());//adding machine conf
        MachineInfo.add(message);//adding the message after encrypted
        MachineInfo.add(messageBeforeDecrypt);//adding the message
        fatherController.setMessageOfTheCompetition(MachineInfo);//father will send the data to server

    }



    public void addAlliesDetails(ArrayList<TableDataObject> allyRowsArr){
        ObservableList<TableDataObject> data = FXCollections.observableArrayList();
       // System.out.println(allyRowsArr.get(0).getMissionSize());
        data.addAll(allyRowsArr);// add all to the
        ActiveTeamsTable.setItems(data);
    }


    public void addAllCandidates(ArrayList<MissionResults> candidates){ // adding all candidates
        ArrayList<TableCandidate> tCandidates = new ArrayList<>();
        for(MissionResults m : candidates){
            TableCandidate tableCandidate = new TableCandidate(m.getCandidate(),m.getCodeConfiguration(),m.getAllieResponsible(),m.getEncryptionTime());
            tCandidates.add(tableCandidate);
        }
        ObservableList<TableCandidate> data = FXCollections.observableArrayList();
        data.addAll(tCandidates);// add all to the
        CandidatesTable.setItems(data);
    }





    public void UpdateContestTabDisabilityComponents(boolean ready) {
            DictionaryListComponentController.setDictionaryListViewDisable(ready);
            DictionarySearchComponentController.setT3WordSearchTextFieldDisabled(ready);
            ProcessButton.setDisable(ready);
            ClearButton.setDisable(ready);
            InputComponent.setDisable(ready);

    }


    public void clearAllFields() {
        InputComponent.clear();
        DictionarySearchComponentController.clearSearchField();
        ResultsComponentController.clearTextArea();
        clearAllTables();
        winnerLabel.setOpacity(0);
        winnerLabel.setText("");
        gameStatus.setOpacity(0);
        gameStatus.setText("");
    }

    private void clearAllTables() {
        ActiveTeamsTable.getItems().clear();
        CandidatesTable.getItems().clear();
    }


    public void setResetAllDataButtonDisable(boolean status) {
        ResetAllDataButton.setDisable(status);
    }

    @FXML
    public void resetAllData(MouseEvent event){
        clearAllFields();
        UpdateContestTabDisabilityComponents(false);
        buttonFunctionalityUnpressed();
        ReadyButton.setSelected(false);
        winnerLabel.setText("");
        setGameStatusLabel("IDLE");
        InputComponentController.setInputTextAreaDisable(false);
        fatherController.clearCurrMachineConfTextArea();
        fatherController.setMachineTabDisable(false);
        fatherController.resetAllContestsDataInServer();
        setResetAllDataButtonDisable(true);

    }

    public void setWinnerLabel(String s) {
        winnerLabel.setOpacity(1);
        winnerLabel.setText(s);
    }

    public void setGameStatusLabel(String s){
        gameStatus.setOpacity(1);
        gameStatus.setText(s);
        if(s.equals("IDLE"))
            gameStatus.setStyle("-fx-text-fill: red");
        else gameStatus.setStyle("-fx-text-fill: green");

    }
    public void setReadyButtonDisable(boolean status) {
        ReadyButton.setDisable(status);
    }
}
