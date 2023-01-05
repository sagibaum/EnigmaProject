package Main.Client;

import DataBase.Agent;
import Main.Client.ContestTabController.Candidates.TableCandidate;
import Main.login.LoginController;
import ServerDTOs.Allies.AlliesContestsDTO;
import ServerDTOs.Allies.AlliesDTO;
import ServerDTOs.Allies.ContestsDTO;
import api.AlliesFunctionality;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import missions.results.MissionResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import user.data.transfer.unit.UserDataTransferUnit;
import util.http.HttpClientUtil;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static Constants.AlliesConstants.*;
import static Constants.AlliesConstants.alliesCurrentStatus.*;

public class AlliesClientController implements Initializable, AlliesFunctionality, Closeable {

    @FXML
    private TabPane AlliesTabs;

    @FXML
    private Tab DashBoardTab;

    @FXML
    private ListView<String> AvailableContestList;

    @FXML
    private ListView<String> AgentList;

    @FXML
    private Pane ContestDataPane;

    @FXML
    private TextField BattlefieldTextField;

    @FXML
    private TextField UboatUsernameTextField;

    @FXML
    private Label GameStatusLabel;

    @FXML
    private Label MissionDifficultyLabel;

    @FXML
    private Label AlliesNeededLabel;

    @FXML
    private Label AlliesJoinedLabel;

    @FXML
    private Button ChooseBattlefieldButton;

    @FXML
    private Tab ContestTab;

    @FXML
    private Pane ContestDataPaneContestTab;

    @FXML
    private TextField BattlefieldTextField2;

    @FXML
    private TextField UboatUsernameTextField2;

    @FXML
    private Label GameStatusLabel2;

    @FXML
    private Label MissionDifficultyLabel2;

    @FXML
    private Label AlliesNeededLabel2;

    @FXML
    private Label AlliesJoinedLabel2;

    @FXML
    private TextField MissionSizeTextField;

    @FXML
    private Button SetMissionSizeButton;

    @FXML
    private Label ContestSelectionErrorLabel;

    @FXML
    private ToggleButton ReadyForCompetitionButton;// S added

    @FXML
    private Label TotalMissionsNumberLabel;

    @FXML
    private Label MissionsCreatedNumberLabel;

    @FXML
    private Label MissionsCompletedNumberLabel;

    @FXML
    private TableView<AgentsTableObject> ActiveAgentsTableView;

    @FXML
    private TableColumn<AgentsTableObject, String> ActiveAgentsTableNameColumn;

    @FXML
    private TableColumn<AgentsTableObject, String> ActiveAgentsTableTasksNumberColumn;

    @FXML
    private TableColumn<AgentsTableObject, String> ActiveAgentsTableCandidatesNumberColumn;

    @FXML
    private TextArea EncryptedMessageTextArea;

    @FXML
    private TableView<AlliesInContestTableObject> ContestTeamsTableView;

    @FXML
    private TableColumn<AlliesInContestTableObject, String> ContestTeamsNameColumn;

    @FXML
    private TableColumn<AlliesInContestTableObject, String> ContestTeamsAgentsNumberColumn;

    @FXML
    private TableColumn<AlliesInContestTableObject, String> ContestTeamsMissionSizeColumn;

    @FXML
    private Label waitingNotificationLabel;

    @FXML
    private Label winnerLabel;

    @FXML
    private Button LogOutButtonContestTab;


    @FXML
    private Button DashBoardButton;

    @FXML
    private Button LogOutButtonDashTab;

    private Alert alert;

    @FXML
    private TableView<TableCandidate> CandidatesTable;
    @FXML TableColumn<TableCandidate,String>  EncryptionTimeCol;
    @FXML TableColumn<TableCandidate,String>  CodeConfigurationCol;
    @FXML TableColumn<TableCandidate,String>  DecryptedMessageCol;

    @FXML ScrollPane CandidatesScrollPane,AgentsScrollPane,contestTeamsScrollPane;

    private boolean firstTime;
    private List<Agent> thisTeamsAgents;
    private List<ContestsDTO> availableUBoats;
    private ContestsDTO RelevantContestData;
    private AlliesContestsDTO ThisAlly;
    private Timer timer;
    private TimerTask listRefresher;
    UserDataTransferUnit UICommunicationUnit;
    private String alliesTeamName;
    private LoginController fatherController;
    private String selectedBattleField;
    private Integer StartingPosAmountPerMission;
    private alliesCurrentStatus contestInProgress;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ContestTab.setDisable(true);
        contestInProgress = alliesCurrentStatus.IDLE;
        RelevantContestData = null;
        this.selectedBattleField="";
        setCandidatesTable();
        setAgentsTable();
        setOtherAlliesInCompetitionTable();
    }

    private void setOtherAlliesInCompetitionTable() {
        ContestTeamsTableView =new TableView<>();
        ContestTeamsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ContestTeamsNameColumn.setCellValueFactory(new PropertyValueFactory<AlliesInContestTableObject,String>("allyName"));//Agents num set up
        ContestTeamsAgentsNumberColumn.setCellValueFactory(new PropertyValueFactory<AlliesInContestTableObject,String>("agentNumber"));//Mission size num set up
        ContestTeamsMissionSizeColumn.setCellValueFactory(new PropertyValueFactory<AlliesInContestTableObject,String>("missionSize"));//Ready status num set up
        ContestTeamsNameColumn.setStyle("-fx-alignment: CENTER;");
        ContestTeamsAgentsNumberColumn.setStyle("-fx-alignment: CENTER;");
        ContestTeamsMissionSizeColumn.setStyle("-fx-alignment: CENTER;");
        ContestTeamsTableView.getColumns().addAll(ContestTeamsNameColumn,ContestTeamsAgentsNumberColumn,ContestTeamsMissionSizeColumn);
        contestTeamsScrollPane.setContent(ContestTeamsTableView);
    }

    private void setAgentsTable() {
        ActiveAgentsTableView =new TableView<>();
        ActiveAgentsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ActiveAgentsTableNameColumn.setCellValueFactory(new PropertyValueFactory<AgentsTableObject,String>("agentName"));//Agents num set up
        ActiveAgentsTableTasksNumberColumn.setCellValueFactory(new PropertyValueFactory<AgentsTableObject,String>("agentTasks"));//Mission size num set up
        ActiveAgentsTableCandidatesNumberColumn.setCellValueFactory(new PropertyValueFactory<AgentsTableObject,String>("candidatesNum"));//Ready status num set up
        ActiveAgentsTableNameColumn.setStyle("-fx-alignment: CENTER;");
        ActiveAgentsTableTasksNumberColumn.setStyle("-fx-alignment: CENTER;");
        ActiveAgentsTableCandidatesNumberColumn.setStyle("-fx-alignment: CENTER;");
        ActiveAgentsTableView.getColumns().addAll(ActiveAgentsTableNameColumn,ActiveAgentsTableTasksNumberColumn,ActiveAgentsTableCandidatesNumberColumn);
        AgentsScrollPane.setContent(ActiveAgentsTableView);

    }

    private void setCandidatesTable() {
        CandidatesTable =new TableView<>();
        CandidatesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        EncryptionTimeCol.setCellValueFactory(new PropertyValueFactory<TableCandidate,String>("encryptionTime"));//Agents num set up
        DecryptedMessageCol.setCellValueFactory(new PropertyValueFactory<TableCandidate,String>("decryptedMessage"));//Mission size num set up
        CodeConfigurationCol.setCellValueFactory(new PropertyValueFactory<TableCandidate,String>("codeConfiguration"));//Ready status num set up
        DecryptedMessageCol.setStyle("-fx-alignment: CENTER;");
        CodeConfigurationCol.setStyle("-fx-alignment: CENTER;");
        EncryptionTimeCol.setStyle("-fx-alignment: CENTER;");
        CandidatesTable.getColumns().addAll(EncryptionTimeCol,CodeConfigurationCol,DecryptedMessageCol);
        CandidatesScrollPane.setContent(CandidatesTable);
    }


    @Override
    public void pullAndShowAvailableAgentsReadyToWork(List<Agent> agents) {
        if(!agents.isEmpty()) {
            this.thisTeamsAgents = agents;
            ObservableList<String> items = AgentList.getItems();
            List<String> display = new ArrayList<>();
            for (Agent agent : agents) {
                String add = "Name: " + agent.getUserName() + " | Threads: " + agent.getThreadNumber() +
                        " | # Missions takes: " + agent.getMissionSize();
                display.add(add);
            }
            items.clear();
            items.addAll(display);
        }
        else{
            AgentList.getItems().clear();
        }

    }

    public void updatePreContestTables(AlliesDTO alliesDataPackage){
        updateAgentsTable(alliesDataPackage.getTeamsAgents());
        updateContestAlliesTable(alliesDataPackage.getUboatsAvailable());
    }

    private void updateAgentsTable(List<Agent> teamsAgents) {
        List<AgentsTableObject> temp = new ArrayList<>();
        ObservableList<AgentsTableObject> data = FXCollections.observableArrayList();
        data.clear();
        if(teamsAgents!=null)
            for(Agent agent:teamsAgents){
            AgentsTableObject object = new AgentsTableObject(agent.getUserName(),String.valueOf(agent.getTasksCompleted())
                    ,String.valueOf(agent.getAllCandidates().size()));
            temp.add(object);
        }
        data.addAll(temp);
        ActiveAgentsTableView.setItems(data);
    }

    private void updateContestAlliesTable(List<ContestsDTO> uboatsAvailable) {
        List<AlliesContestsDTO> temp = new ArrayList<>();
        for(ContestsDTO C:uboatsAvailable){
            if(C.getBattlefieldName().equals(selectedBattleField)){
                temp=C.getAlliesAssociated();
            }
        }

        ObservableList<AlliesInContestTableObject> data = FXCollections.observableArrayList();
        ArrayList<AlliesInContestTableObject> Allies = new ArrayList<>();
        for(AlliesContestsDTO a:temp){
            AlliesInContestTableObject ally = new AlliesInContestTableObject(a.getAllyName(),
                    String.valueOf(a.getAgentsNum()),String.valueOf(a.getMissionSize()));
            Allies.add(ally);
        }
        data.addAll(Allies);
        ContestTeamsTableView.setItems(data);
        if(ThisAlly != null)
            EncryptedMessageTextArea.setText(ThisAlly.getMessage());
    }

    @Override
    public void updateCandidatesPart(List<Agent> teamsAgents) {
        ArrayList<TableCandidate> tCandidates = new ArrayList<>();
        ObservableList<TableCandidate> data = FXCollections.observableArrayList();
        for(Agent agent:teamsAgents){
            for(MissionResults m:agent.getAllCandidates()){
                TableCandidate tableCandidate = new TableCandidate(m.getCandidate(),m.getCodeConfiguration(),m.getAllieResponsible(),m.getEncryptionTime());
                tCandidates.add(tableCandidate);
            }
        }
        data.addAll(tCandidates);// add all to the
       /* if(!tCandidates.isEmpty())*/
            CandidatesTable.setItems(data);
    }


    @Override
    public void pullAndShowAllContestsData(List<ContestsDTO> contests) {

        if(!contests.isEmpty()) {
            this.availableUBoats = contests;
            ObservableList<String> items = AvailableContestList.getItems();
            List<String> temp = new ArrayList<>(items);
            for (ContestsDTO uboat : contests) { //IF battlefield ISNT IN contests list view ADD IT
                if (!items.contains(uboat.getBattlefieldName()) && uboat.getBattlefieldName() != null)
                    items.add(uboat.getBattlefieldName());
            }
            boolean isThere = false;
            for (String item:temp){
            for (ContestsDTO uboat : contests) {
                    if(item.equals(uboat.getBattlefieldName()))
                    {
                        isThere=true;
                        break;
                    }
                }
                     if(!isThere)
                         items.remove(item);
                else isThere=false;
            }
    }

    }

    @FXML
    void showSelectedContestData(MouseEvent event) {
        if (availableUBoats != null) {
            for (ContestsDTO uboat : availableUBoats) {
                if (uboat.getBattlefieldName().equals(AvailableContestList.getSelectionModel().getSelectedItem())) {
                    BattlefieldTextField.setText(uboat.getBattlefieldName());
                    UboatUsernameTextField.setText(uboat.getUsername());
                    GameStatusLabel.setText(uboat.isRunning());
                    if (uboat.isRunning().equals("Running")) {
                        GameStatusLabel.setStyle("-fx-text-fill: green");
                    } else GameStatusLabel.setStyle("-fx-text-fill: red");
                    MissionDifficultyLabel.setText(uboat.getDifficulty());
                    AlliesNeededLabel.setText(uboat.getAlliesNeededAmountString());
                    AlliesJoinedLabel.setText(uboat.getAlliesJoinedString());
                    //NOTE: NEED TO UPDATE ALLIES JOINED IN UBOAT IN SERVER WHEN
                    //      AN ALLY USES 'Choose Battlefield' button
                }
            }
        }
    }

    private void updateAlliesDashboardDataList(AlliesDTO alliesDataPackage) {
        Platform.runLater(() -> {
           if (alliesDataPackage!=null && !alliesDataPackage.isContestsEmpty()) {
                pullAndShowAllContestsData(alliesDataPackage.getUboatsAvailable());
                updateThisAllyAndUBoat(alliesDataPackage); //updates local uboat and this ally variables.
                updateAlliesAppStatus(); //updates this app functionality status according to 'running' and 'winner'
                if(!selectedBattleField.equals(""))
                    checkIfBattlefieldChanged(alliesDataPackage.getUboatsAvailable());
                updatePreContestTables(alliesDataPackage);
                updatePreContestLabels();
            }
            if(alliesDataPackage!=null&&alliesDataPackage.getTeamsAgents()!=null)
                pullAndShowAvailableAgentsReadyToWork(alliesDataPackage.getTeamsAgents());
        switch(contestInProgress) {
            case CONTEST_IN_PROGRESS: //start creating missions and sending to server
                if(alliesDataPackage==null || alliesDataPackage.isUBoatAbandon()){
                    UBBoatAbandonProcedure();
                    contestInProgress=IDLE;
                }
                else{
                updateInProgressContestObjects(alliesDataPackage.getTeamsAgents());
                ReadyForCompetitionButton.setDisable(true);
                }
                break;
            case FINISHED: //stop creating missions, tell user, and move back to contest choose tab
                updateInProgressContestObjects(alliesDataPackage.getTeamsAgents());
                contestHasFinished();
                break;
        }

        });
    }
    private void UBBoatAbandonProcedure( ) {
          AvailableContestList.getItems().clear();
            if(contestInProgress.equals(CONTEST_IN_PROGRESS)){
                showInformationAlert("Contest Canceled ","UBoat abandoned the contest ,returning back");
                switchTabsBack();
            }
    }

    private void runningRunningProcess(){
        GameStatusLabel2.setText(RelevantContestData.isRunning());
        if (RelevantContestData.isRunning().equals("Running")) {
            GameStatusLabel2.setStyle("-fx-text-fill: green");
        } else GameStatusLabel2.setStyle("-fx-text-fill: red");
    }
    private void updateAlliesAppStatus() {
        if(RelevantContestData!=null && ThisAlly != null) {
            if (!ThisAlly.getWinner().equals("")) //if winner has been found
                this.contestInProgress = FINISHED; //winner has been found
            else if (RelevantContestData.isRunning().equals("Running") && ThisAlly.getWinner().equals("")) //if 1st time running and no winner yet
                this.contestInProgress = alliesCurrentStatus.CONTEST_IN_PROGRESS; //means contest has begun
            else if (RelevantContestData.isRunning().equals("Idle") && ThisAlly.getWinner().equals("")) //if contest hasnt begun
                this.contestInProgress = alliesCurrentStatus.IDLE;
            }
        }//means contest has begun


    private void updateInProgressContestObjects(List<Agent> agents) {
        TotalMissionsNumberLabel.setText(String.valueOf(ThisAlly.getProgress().getMissionsTotal()));
        MissionsCreatedNumberLabel.setText(String.valueOf(ThisAlly.getProgress().getMissionsCreated()));
        MissionsCompletedNumberLabel.setText(sumAllMissionsCompleted());
        runningRunningProcess();
        updateCandidatesPart(agents);
    }

    private String sumAllMissionsCompleted() {
        Long sum=0L;
        for(Agent agent:thisTeamsAgents){
            sum+=agent.getTasksCompleted();
        }
        return String.valueOf(sum);
    }

    private void updatePreContestLabels() {
        if(!selectedBattleField.equals(""))
        {AlliesNeededLabel2.setText(RelevantContestData.getAlliesNeededAmountString());
        AlliesJoinedLabel2.setText(RelevantContestData.getAlliesJoinedString());
        runningRunningProcess();
        winnerLabel.setText("");
        DashBoardButton.setDisable(true);
        }
    }


    public  void checkIfBattlefieldChanged(List<ContestsDTO> uboatsAvailable){
        boolean exist = false;
        for(ContestsDTO contestsDTO:uboatsAvailable){
            if (contestsDTO.getBattlefieldName().equals(selectedBattleField)) {
                exist = true;
                break;
            }
        }

        if(!exist){
            AvailableContestList.getItems().clear();
            showInformationAlert("Contest file changed","UBoat uploaded new contest ,returning back");
            switchTabsBack();
        }
    }

    private void contestHasFinished() {
        //in here once a winner has been announced three things happen:
        //1. show a message to user about win \ lose:
        winnerLabel.setOpacity(1);
        String  string=EncryptedMessageTextArea.getText();
        if(ThisAlly.getWinner().equals(alliesTeamName)){
            winnerLabel.setText("You are the WINNER!");
            EncryptedMessageTextArea.clear();
            EncryptedMessageTextArea.setText(string
                    +"\nThe Sentence was: "+ThisAlly.getUnDecryptedMessage());

        }
        else{
            winnerLabel.setText("Contest Finished, "+"You have lost... , the Winner is: " + ThisAlly.getWinner());
            EncryptedMessageTextArea.clear();
            EncryptedMessageTextArea.setText(string
                    +"\nThe Sentence was: "+ThisAlly.getUnDecryptedMessage());
        }
        //2. stop creating missions and sending them to server:
        //3. move this app back to contest picking tab and refresh all relevant variables!:
        DashBoardButton.setDisable(false);
        //listRefresher.cancel();
    }


    @FXML
    void BackToDashBoard(MouseEvent event) {
        switchTabsBack();
    }
    private void updateThisAllyAndUBoat(AlliesDTO alliesDataPackage){
        for (ContestsDTO uboat: alliesDataPackage.getUboatsAvailable()) {
            if (uboat.getBattlefieldName().equals(selectedBattleField)) {
                this.RelevantContestData = uboat; //save updated uboat
                for (AlliesContestsDTO ally : uboat.getAlliesAssociated()) { //save from allies of the uboat, this ally
                    if (ally.getAllyName().equals(this.alliesTeamName)) {
                        this.ThisAlly = ally;
                        break;
                    }
                }
            }
        }
    }

    public void startListRefresher() {

        listRefresher = new alliesAppDataRefresher(
                this::updateAlliesDashboardDataList,
                this.alliesTeamName);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    public void setAlliesTeamName(String alliesTeamName) {
        this.alliesTeamName = alliesTeamName;
        startListRefresher();
    }

    @Override
    public boolean checkContestErrors() {
        //change tab:
        this.selectedBattleField = AvailableContestList.getSelectionModel().getSelectedItem();
        if(selectedBattleField == null) {
            ContestSelectionErrorLabel.setText("You must choose a battlefield to go to the contest page!");
            ContestSelectionErrorLabel.setOpacity(1);
            return false;
        }
        if(AgentList.getItems().isEmpty()) {
            ContestSelectionErrorLabel.setText("Must have at least 1 agent!");
            ContestSelectionErrorLabel.setOpacity(1);
            return false;
        }
        ContestsDTO curr = null;
        for (ContestsDTO uboat: availableUBoats) {
            if (uboat.getBattlefieldName().equals(AvailableContestList.getSelectionModel().getSelectedItem())) {
                curr = uboat;
            }
        }
        if(curr == null) return false;
        if(curr.getAlliesJoinedInteger() >= curr.getAlliesNeededAmountInteger()) {
            ContestSelectionErrorLabel.setText("This Battlefield is full, choose a different one!");
            ContestSelectionErrorLabel.setOpacity(1);
            return false;
        }

        if(curr.isRunning().equals("Running")) {
            ContestSelectionErrorLabel.setText("This Battlefield is in progress, choose a different one!");
            ContestSelectionErrorLabel.setOpacity(1);
            return false;
        }

        return true;
    }

    @FXML
    void selectCompetition(MouseEvent event) {
        if(checkContestErrors()) {
            letServerKnowAboutChoice();
        }
    }

    private void switchTabs() {
        ContestSelectionErrorLabel.setOpacity(0);
        ContestSelectionErrorLabel.setText("");
        ContestTab.setDisable(false);
        SingleSelectionModel<Tab> selectionModel = AlliesTabs.getSelectionModel();
        selectionModel.select(1); //select by index starting with 0
        DashBoardTab.setDisable(true);
        updateContestTabContestData();
    }

    private void switchTabsBack() {
        this.contestInProgress = alliesCurrentStatus.IDLE;
        this.selectedBattleField = null;
        StartingPosAmountPerMission = null;
        AvailableContestList.getItems().clear();
        MissionSizeTextField.clear();
        MissionSizeTextField.setEditable(false);
        SetMissionSizeButton.setDisable(false);
        ContestSelectionErrorLabel.setOpacity(0);
        ContestSelectionErrorLabel.setText("");
        ContestTab.setDisable(true);
        DashBoardTab.setDisable(false);
        buttonFunctionalityUnpressed();
        ReadyForCompetitionButton.setSelected(false);
        winnerLabel.setOpacity(0);
        SetMissionSizeButton.setDisable(false);
        MissionSizeTextField.setEditable(true);
        ReadyForCompetitionButton.setDisable(true);
        selectedBattleField="";
        clearAllLabels();
        clearAllTables();
        SingleSelectionModel<Tab> selectionModel = AlliesTabs.getSelectionModel();
        selectionModel.select(0); //select by index starting with 0
    }

    private void clearAllLabels() {
        TotalMissionsNumberLabel.setText("");
        MissionsCreatedNumberLabel.setText("");
        MissionsCompletedNumberLabel.setText("");
        AlliesNeededLabel2.setText("");
        AlliesJoinedLabel2.setText("");
        EncryptedMessageTextArea.setText("");
    }

    private void clearAllTables() {
        ContestTeamsTableView.getItems().clear();
        ActiveAgentsTableView.getItems().clear();
        EncryptionTimeCol.getColumns().clear();
        CodeConfigurationCol.getColumns().clear();
        DecryptedMessageCol.getColumns().clear();
    }

    private void letServerKnowAboutChoice() {

        String finalUrl = HttpUrl
                .parse(Constants.AlliesConstants.CHOOSE_BATTLEFIELD)
                .newBuilder()
                .addQueryParameter("BattleField", this.selectedBattleField)
                .addQueryParameter("userName", this.alliesTeamName)
                .build()
                .toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Battlefield Selection");
                    alert.setContentText("Battlefield Selection process failed!\n" + "Reason: " + e.getMessage());
                    alert.showAndWait();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseBody = response.body().string();
                    Platform.runLater(() -> {
                        ContestSelectionErrorLabel.setOpacity(1);
                        ContestSelectionErrorLabel.setText("Something went wrong with choosing battlefield:\n" +responseBody);
                    });

                } else {
                    Platform.runLater(() -> {
                        switchTabs();
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Battlefield Selection");
                        alert.setContentText("Battlefield Chosen Successfully!");
                        alert.show();
                    });
                }
            }
        });
    }

    //Contest tab logic starts here:

    private void updateContestTabContestData(){
        boolean exist = false;
        for (ContestsDTO uboat: availableUBoats) {
            if (uboat.getBattlefieldName().equals(selectedBattleField)){
                BattlefieldTextField2.setText(uboat.getBattlefieldName());
                UboatUsernameTextField2.setText(uboat.getUsername());
                GameStatusLabel2.setText(uboat.isRunning());
                if (uboat.isRunning().equals("Running")){
                    GameStatusLabel2.setStyle("-fx-text-fill: green");
                } else GameStatusLabel2.setStyle("-fx-text-fill: red");
                MissionDifficultyLabel2.setText(uboat.getDifficulty());
                AlliesNeededLabel2.setText(uboat.getAlliesNeededAmountString());
                AlliesJoinedLabel2.setText(uboat.getAlliesJoinedString());
                //NOTE: NEED TO UPDATE ALLIES JOINED IN UBOAT IN SERVER WHEN
                //      AN ALLY USES 'Choose Battlefield' button
            }
        }
    }

    @FXML
    void setStartingPosAmountPerMission(MouseEvent event) {
        waitingNotificationLabel.setStyle("-fx-text-fill: black");
        if(!MissionSizeTextField.getText().matches("[0-9]+"))
        {
            waitingNotificationLabel.setOpacity(1);
            waitingNotificationLabel.setStyle("-fx-text-fill: red");
            waitingNotificationLabel.setText("Mission size isnt a number or not positive!");
            MissionSizeTextField.clear();
            return;
        }
        waitingNotificationLabel.setOpacity(0);
        waitingNotificationLabel.setText("");
        this.StartingPosAmountPerMission = Integer.valueOf(MissionSizeTextField.getText());
        SetMissionSizeButton.setDisable(true);
        MissionSizeTextField.setEditable(false);
        MissionSizeTextField.setText("Starting pos per agent: " + StartingPosAmountPerMission);
        ReadyForCompetitionButton.setDisable(false); // S added
    }

    @FXML
    public void setReady(ActionEvent actionEvent) {
        ReadyUnReadyToCompete();
    }

    void buttonFunctionalityUnpressed(){// S added
        ReadyForCompetitionButton.setStyle("-fx-background-color:green");
        ReadyForCompetitionButton.setText("Ready!");

    }

    void buttonFunctionalityPressed(){// S added
        ReadyForCompetitionButton.setStyle("-fx-background-color:red");
        ReadyForCompetitionButton.setText("Unready :(");

    }

    private void ReadyUnReadyToCompete() {// S added
        if(!ReadyForCompetitionButton.isSelected()) // means that user wants to do unready process
        {
            //send to the server that he isn't ready! -> same servlet as the ready but without body of machine ,dictionary and so ..
            buttonFunctionalityUnpressed();
            sendServerReadyNotice(false);
            SetMissionSizeButton.setDisable(false);
        }
        else // user announce the server that he is ready
        {
            buttonFunctionalityPressed();
            sendServerReadyNotice(true);
        }
    }


    private void sendServerReadyNotice(boolean Ready) {

        String finalUrl = HttpUrl.parse(ANNOUNCE_READY)
                .newBuilder().addQueryParameter("type", Allies)//type of user, username not needed cause of the session and cookies
                .addQueryParameter("readyStatus", String.valueOf(Ready)) // ready status
                .addQueryParameter("userName", this.alliesTeamName) // allies serer side username is needed
                .addQueryParameter("MissionSize", this.StartingPosAmountPerMission.toString())
                .build().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200 ) {
                        Platform.runLater(() -> {
                            waitingNotificationLabel.setStyle("-fx-text-fill: red");
                            waitingNotificationLabel.setText("Error: " + "Server has not received ready..");
                            waitingNotificationLabel.setOpacity(1);//S added
                        });
                    }
                    }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Send Ready Announce Process");
                    alert.setContentText("Send Ready Announce Failed!\n" + "Reason: " + e.getMessage());
                    alert.showAndWait();
                });
            }
        });
    }


    @Override
    public void close() {
        AvailableContestList.getItems().clear();
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

    public void setRootController(LoginController loginController) {
        this.fatherController = loginController;
    }
    @FXML
    void LogOut(MouseEvent event) {
        LogOutFromServer();
    }
    @Override
    public void LogOutFromServer() {
        String finalUrl = HttpUrl.parse(LOGOUT)
                .newBuilder()
                .addQueryParameter("type", Allies)//type of user, username not needed cause of the session and cookies
                .build().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    showErrorAlert("Logout Process", "Logout Failed!\n" + "Reason: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                        showErrorAlert("Logout Process from server ", responseBody);
                    });
                }
                else{ Platform.runLater(() -> {
                    showConfirmationAlert("Thank Tou For Playing And See You Soon!!");
                    try {
                        close();
                        fatherController.switchToAllyLoginPage();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });}
            }
        });
    }

    private void showConfirmationAlert(String HeaderText){
        this.alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(HeaderText);
        alert.show();
    }

    private void showErrorAlert(String HeaderText , String Content) {
        this.alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(HeaderText);
        alert.setContentText(Content);
        alert.show();
    }

    private void showInformationAlert(String HeaderText , String Content){
        this.alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(HeaderText);
        alert.setContentText(Content);
        alert.show();
    }


}
