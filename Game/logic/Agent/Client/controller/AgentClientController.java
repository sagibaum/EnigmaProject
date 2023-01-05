package Client.controller;

import Client.login.AgentLoginController;
import Constants.AgentConstants;
import DataBase.Agent;
import Main.Client.ContestTabController.Candidates.TableCandidate;
import Mission.Mission;
import ServerDTOs.Agent.AgentDTO;
import ServerDTOs.Agent.MissionDTO;
import api.AgentFunctionality;
import customThread.PausableThreadPoolExecutor;
import customThread.ThreadFactoryBuilder;
import http.HttpClientUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import missions.results.MissionResults;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

import static Constants.AgentConstants.*;

public class AgentClientController implements Initializable, AgentFunctionality, Closeable {

    @FXML
    private FlowPane T3candidatesFlowPane;

    @FXML
    private TextField AlliesTeamTextField;

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
    private Label TotalMissionsNumberLabel;

    @FXML
    private Label MissionsPulledNumberLabel;

    @FXML
    private Label MissionsCompletedNumberLabel;

    @FXML
    private Label MissionsInQueueNumberLabel;

    @FXML
    private Label CandidatesNumberLabel;
    @FXML
    private TableView<TableCandidate> CandidatesTable;

    @FXML
    private TableColumn<TableCandidate, String> EncryptionTimeCol;

    @FXML
    private TableColumn<TableCandidate, String> CodeConfigurationCol;

    @FXML
    private TableColumn<TableCandidate, String> DecryptedMessageCol;

    @FXML
    private ScrollPane CandidatesScrollPane;


    //refresher variables:
    private Timer timer;
    private TimerTask listRefresher;
    //mission related stuff:
    private TimerTask missionRefresher;
    private boolean isReadyToPullMoreMissions;

    //login variables:
    private AgentDTO data;
    //
    private AgentLoginController fatherController;
    private MissionDTO missionsData;
    private ArrayBlockingQueue<Runnable> missionsQueue;
    private PausableThreadPoolExecutor customThreadPool;
    private LinkedBlockingQueue<MissionResults> resultsQueue;
    private Consumer<List<MissionResults>> resultsConsumer;
    private Consumer<Long> missionCompletedConsumer;
    private Long missionsCompletedNum, missionPulledNum;
    private Alert alert;

    @FXML
    private Button LogOutButton;

    private AgentLoginController rootController;
    @FXML
    void LogOut(MouseEvent event) throws Exception {
        preformLogout(false);
    }

    private void preformLogout(Boolean allyAbandon){
        //logout logic -> can press this button only if there isnt any competition runing !!
        //add here simple change listener(in initialized method ) to if value is changing =>disable button false
        String finalUrl = HttpUrl.parse(LOGOUT)
                .newBuilder()
                .addQueryParameter("type", AGENT)//type of user, username not needed cause of the session and cookies
                .addQueryParameter("userName", data.getAgentUserName())
                .build().toString();

        util.http.HttpClientUtil.runAsync(finalUrl, new Callback() {
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
                    if(allyAbandon)
                        showConfirmationAlert("Ally abandon competition! \nReturning to login page...");

                    showConfirmationAlert("Thank Tou For Playing And See You Soon!!");
                    try {
                        //next put here some logic of logout same as in ally and u boat(if its ok i should stop the refresher)
                        close();
                        deleteContestVariables();
                        rootController.switchToAgentLoginPage();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });}
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startListRefresher();
        missionRefresher = null;
        isReadyToPullMoreMissions = true;
        this.missionsCompletedNum = 0L;
        this.missionPulledNum =0L;
        setCandidatesTable();
    }

    private void setCandidatesTable() {
        CandidatesTable =new TableView<>();
        CandidatesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        EncryptionTimeCol.setCellValueFactory(new PropertyValueFactory<>("encryptionTime"));//Agents num set up
        DecryptedMessageCol.setCellValueFactory(new PropertyValueFactory<>("decryptedMessage"));//Mission size num set up
        CodeConfigurationCol.setCellValueFactory(new PropertyValueFactory<>("codeConfiguration"));//Ready status num set up
        DecryptedMessageCol.setStyle("-fx-alignment: CENTER;");
        CodeConfigurationCol.setStyle("-fx-alignment: CENTER;");
        EncryptionTimeCol.setStyle("-fx-alignment: CENTER;");
        CandidatesTable.getColumns().addAll(EncryptionTimeCol,CodeConfigurationCol,DecryptedMessageCol);
        CandidatesScrollPane.setContent(CandidatesTable);


    }

    @Override
    public void pullAllDataNeededFromServerForThisApp() {

    }

    @Override
    public void login() {

    }

    @Override
    public void pullAndShowAvailableAlliesTeams() {

    }

    @Override
    public void registerAgent() {

    }

    @Override
    public void pullMissionsFromServer() {

    }

    @Override
    public void sendCandidatesListToServer() {

    }

    @Override
    public void pullAndShowData() {

    }

    @Override
    public void showInnerQueueProgress() {

    }

    @Override
    public void showAgentCandidates() {

    }

    @Override
    public void stopAgent() {

    }


    private void updateRelevantAllyData(AgentDTO agentDataPackage) {
        // System.out.println(agentDataPackage);
        if(agentDataPackage != null) {
            Platform.runLater(() -> {
                this.data = agentDataPackage;
                AlliesTeamTextField.setText(agentDataPackage.getAllyTeam());
                BattlefieldTextField.setText(agentDataPackage.getBattleField());
                UboatUsernameTextField.setText(agentDataPackage.getUsername());
                if (data.getRunningOrIdle()) {
                    GameStatusLabel.setText("Running");
                    GameStatusLabel.setStyle("-fx-text-fill: green");
                } else {
                    GameStatusLabel.setText("Idle");
                    GameStatusLabel.setStyle("-fx-text-fill: red");
                }
                MissionDifficultyLabel.setText(agentDataPackage.getDifficulty());
                AlliesJoinedLabel.setText(agentDataPackage.getAlliesJoined());
                AlliesNeededLabel.setText(agentDataPackage.getAlliesNeeded());
                if (data.isBelongToContest())
                {//only if agent belongs to contest he will start contests process
                    if (agentDataPackage.getRunningOrIdle() && missionRefresher == null) {
                       // System.out.println("\ncontest started");
                        //if mission refresher is null competition has just begun
                        LogOutButton.setDisable(true);
                        createMissionRefresher();
                        startContestDecryptionProcess();

                    } else if (agentDataPackage.getRunningOrIdle() && missionRefresher != null) {
                        if (this.missionsQueue.isEmpty()) {//might be a problem until new missions come!!!!!!!
                            //get more missions, missions received done

                           // System.out.println("\npulling more missions");
                            runMissionDataRefresher(); //get more missions
                            pushMissionListIntoMissionQueue(); //pull mission data from server

                            addAllCandidates(new ArrayList<>(resultsQueue)); //update candidate part in app
                            updateAppContestPart(); //update labels

                            sendMissionResultsListToServer(); //send all results so far to server
                        }

                    } else if (!agentDataPackage.getRunningOrIdle() && missionRefresher != null) { //contest over
                        stopPullingMissions();
                        isReadyToPullMoreMissions = false;
                       /* CandidatesTable.getItems().clear();//when contest is over clear the candidates' table data !
                        setCandidatesTable();//reset the candidates table*/
                        LogOutButton.setDisable(false);//only when contest finished you can logout!

                    }
                }
            });
        }
        else{//ally abandon
            preformLogout(true);}
    }

    private void updateAppContestPart() {
        if(this.missionsData != null) {
            this.TotalMissionsNumberLabel.setText(String.valueOf(missionsData.getTotalMissions()));
            this.MissionsPulledNumberLabel.setText(String.valueOf(this.missionPulledNum));
            this.MissionsInQueueNumberLabel.setText(String.valueOf(missionsData.getMissionsInQueue()));
            this.MissionsCompletedNumberLabel.setText(String.valueOf(this.missionsCompletedNum));
            this.CandidatesNumberLabel.setText(String.valueOf(this.resultsQueue.size()));
        }
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

    private void startContestDecryptionProcess() {

        createContestVariables();  //create all variables needed for contest (consumers, queues, threadpool ..)

        runMissionDataRefresher(); //create the mission data refresher.

        pushMissionListIntoMissionQueue(); //pull mission data from server

        this.customThreadPool.prestartAllCoreThreads(); //start running missions
    }

    private synchronized void sendMissionResultsListToServer() {
        //Here input logic to send back the list of all mission results created in agent to server:
        //use toJSON to send to the server. in the server in synchronized way add all missions results of this agent
        //to the list of mission results of allies.
        if(this.resultsQueue.isEmpty())
            return; //nothing to send

        List<MissionResults> resultsToServer = new ArrayList<>(resultsQueue);

        String resultsJson = GSON_INSTANCE.toJson(resultsToServer);

        RequestBody body = RequestBody.create( resultsJson , AgentConstants.JSON);

        String finalUrl = HttpUrl
                .parse(AgentConstants.SEND_MISSION_RESULTS)
                .newBuilder()
                .addQueryParameter("userName", this.data.getAgentUserName()) //Username of this agent to enter results to specific
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(body)
                .build();

        HttpClientUtil.runAsyncWithBody(request, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                   // System.out.println("could not send results to server!!!!!");
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() == 200){
                    //do nothing , means sent successfully
                }
                else{
                    Platform.runLater(() -> {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Mission Results");
                        alert.setContentText("Send Results To Server Failed!\n" + "Response Code: " + response.code());
                        alert.showAndWait();
                    });
                }
            }
        });
    }

    private void createContestVariables() {

        this.missionPulledNum = 0L;
        this.missionsCompletedNum = 0L;

        this.missionsQueue = new ArrayBlockingQueue<>(1000);
        ThreadFactory customThreadFactory = new ThreadFactoryBuilder().setNamePrefix("Agent").build();//custom thread factory to set threads names
        this.customThreadPool= new PausableThreadPoolExecutor(data.getAgentThreadNumber(), missionsQueue, customThreadFactory);

        this.resultsQueue = new LinkedBlockingQueue<>();// send it to mission for threads know where to push the candidates
        this.resultsConsumer = missionResults -> {
            // System.out.println(missionResults);
            resultsQueue.addAll(missionResults);

        }; //when a new mission results created, take it
        this.missionCompletedConsumer = missionCompletedNum -> {
            this.missionsCompletedNum += missionCompletedNum;
            for (MissionResults results: resultsQueue) {
                results.setTasksCompleted(this.missionsCompletedNum);
            }
        }; //take amount of missions finished and added them to counter
    }

    private void pushMissionListIntoMissionQueue() {
        if(missionsData != null) {
            for (Mission mission : missionsData.getMissionsToExecute()) {
                // System.out.println(mission);
                if(mission!=null) {
                    if(resultsConsumer!=null)
                        mission.setResultsConsumer(this.resultsConsumer);
                mission.setProgressConsumer(this.missionCompletedConsumer);
                missionsQueue.add(mission);}
            }
        }
    }

    private synchronized void runMissionDataRefresher() {
        missionRefresher.run();
    }

    public synchronized void startListRefresher() {
        listRefresher = new agentAppDataRefresher(this::updateRelevantAllyData);
        timer = new Timer();
        timer.schedule(listRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateMissionsData(MissionDTO missionDataPackage) {
        //System.out.println(missionDataPackage);
        Platform.runLater(() -> {
            this.missionsData = missionDataPackage;
            //System.out.println(missionsData.getMissionsToExecute());
            // System.out.println("setting this.missionsData to: " + missionsData);
            this.missionPulledNum += missionDataPackage.getMissionsPulled();
        });

    }

    public void createMissionRefresher() {
        missionRefresher = new missionsDataRefresher(this::updateMissionsData);
    }

    @Override
    public void close() {
        //AgentList.getItems().clear();
        //AvailableContestList.getItems().clear(); CLEAR STUFF HERE ON CLOSE!!
        if (listRefresher != null && timer != null) {
            listRefresher.cancel();
            timer.cancel();
        }
    }

    private void stopPullingMissions() {
        //AgentList.getItems().clear();
        //AvailableContestList.getItems().clear(); CLEAR STUFF HERE ON CLOSE!!
        deleteContestVariables();
        if (missionRefresher != null) {
            missionRefresher.cancel();
            missionRefresher = null;
        }
    }

    private void deleteContestVariables() {
        if(missionsQueue != null)
             this.missionsQueue.clear();
        this.missionsQueue = null;
        if(customThreadPool != null)
             this.customThreadPool.stop();
        this.customThreadPool = null;
        if(resultsQueue != null)
            this.resultsQueue.clear();
        this.resultsQueue = null;
        this.missionPulledNum = 0L;
        this.missionsCompletedNum = 0L;
    }

    public void setRootController(AgentLoginController agentLoginController) {
        this.rootController = agentLoginController;
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


