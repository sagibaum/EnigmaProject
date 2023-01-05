package Main.Client;

import EngineFunctionallity.EngineObject;
import Exceptions.InvalidXmlFileException;
import Main.Client.ContestTabController.UBoatContestController;
import Main.Client.MachineTabController.MachineTabController;
import Main.login.LoginController;
import ServerDTOs.Uboat.TableDataObject;
import ServerDTOs.Uboat.UBoatDTO;
import api.UBoatFunctionality;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import missions.results.MissionResults;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import schem.out.CTEEnigma;
import user.data.transfer.unit.UserDataTransferUnit;
import util.Constants;
import util.http.HttpClientUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static util.Constants.*;

public class UBoatClientController implements Initializable, UBoatFunctionality {

    //----------JavaFX App----------//
    @FXML
    private ImageView UBoatPic;
    @FXML private Tab MachineTab,ContestTab;
    @FXML private TabPane AppTabPane ;

    @FXML GridPane machineTabComponent;
    @FXML GridPane contestTabComponent;

    @FXML
    private TextField FilePathTextField; // Value injected by FXMLLoader
    @FXML private Button LoadButton;

    @FXML private Button LoadFileButton;
    @FXML
    private ScrollPane mainStage;//sag added
    @FXML private Label UBoatNameLabel;

    @FXML private MachineTabController machineTabComponentController;

    @FXML private UBoatContestController contestTabComponentController;

    @FXML
    private Button LogOutButtonMachineTab;

    private LoginController fatherController;
    private EngineObject Engine;
    private String filePath,name;
    UserDataTransferUnit UICommunicationUnit;
    private Timer timer;
    private TimerTask UBoatRefresher;
    private Alert alert;

    private boolean contestStartedNow;
    private boolean allAbandon;

    public enum Tabs{
        MACHINE_TAB(0),
        CONTEST_TAB(1);
        private final int value;
        Tabs( final Integer newValue) {value = newValue;}
        public int getValue() { return value; }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.Engine = new EngineObject();
        this.UICommunicationUnit = new UserDataTransferUnit();
        setMachineTab(Engine,UICommunicationUnit);
        initContestTab();
        bindMachineConfTextAreas();
        contestStartedNow= true;


    }

    //change listeners INIT for upload files process.

    private void bindMachineConfTextAreas(){ // binding machine conf text areas !
        contestTabComponentController.getMachineConfTextField().textProperty()
                .bind(machineTabComponentController.getT1CurrMachineConfTextArea().textProperty());//bind properties
    }


    //sets machine tab controller
    private  void setMachineTab(EngineObject Engine,UserDataTransferUnit UICommunicationUnit){
        machineTabComponentController.setRootController(this);
        machineTabComponentController.setMachineTabController(Engine,UICommunicationUnit,"");
    }
    public void setMachineTabImageScales(Stage fatherStage){ // for scaling the images according to father stage size
        machineTabComponentController.setFatherStage(fatherStage);
    }


    private void  initContestTab(){ //sets contest tab controller
        contestTabComponentController.setContestTab(this,Engine,UICommunicationUnit);

    }

    public void setUBoatClientController(LoginController loginController){
        this.fatherController = loginController;
    }



    @FXML
    void SelectFileHandler(ActionEvent event)  {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load File Dialog");
        Stage stage = (Stage) mainStage.getScene().getWindow();//saving the main window of the app, must typecast
        File file = fileChooser.showOpenDialog(stage);
        if(file!=null) {
            filePath+=file.getPath();//saving path
            FilePathTextField.setText(file.getPath());//set the path into the text field
            LoadButton.setDisable(false);
        }

    }

    //main load button functionality
    @FXML
    void LoadHandler(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("XML file error");
        if(!FilePathTextField.getText().equals("")) {
            uploadXMLFileToServer(FilePathTextField.getText());
            contestTabComponentController.clearAllFields();
        }

        else { //lock load button if its empty file path!
            alert.setContentText("Empty file path , please insert valid file path!");
            LoadButton.setDisable(true);
            FilePathTextField.setText(filePath);//returns the text in the text box to the prev state
            alert.showAndWait();
        }
    }

    //This function will set the text in the part conf text field (option 2) after the system successfully loaded xml
    private void setT1PartConfTextField(){
        Engine.viewPartialMachineSpecs(UICommunicationUnit);
        machineTabComponentController.setPartTextArea(UICommunicationUnit.getEngineOutputMessage());

    }

    //if we write something in text filed . it will release load button
    @FXML
    void UnlockLoadIfNeeded(KeyEvent event) {
        LoadButton.setDisable(false);
    }

    public void setClientName(String text) {
        name = text;
        UBoatNameLabel.setText("Welcome "+ text);
    }
    public void switchBetweenTabs(Integer index,boolean status) { // switching between tabs in app according to index value
        if(index.equals(Tabs.CONTEST_TAB.value)) {
            ContestTab.setDisable(status);
            AppTabPane.getSelectionModel().select(index);
            MachineTab.setDisable(!status);
        }
        else{
            ContestTab.setDisable(!status);
            AppTabPane.getSelectionModel().select(index);
            MachineTab.setDisable(status);
        }

    }

    public void setDictionaryComponents() {
        contestTabComponentController.setDictionaryListViewComponentController();
        contestTabComponentController.setDictionaryHBoxComponentController();
    }

    public void  setCurrMachineConfTextArea(String message) {
        machineTabComponentController.getT1CurrMachineConfTextArea().setText(message);
    }
    public  void clearCurrMachineConfTextArea(){
        machineTabComponentController.getT1CurrMachineConfTextArea().clear();
    }

    private void setXMLContestComponentDetails(){
        machineTabComponentController.setNameOfBattleFiled(Engine.getBattleFieldName());
        machineTabComponentController.setRequiredAlliesLabel(String.valueOf(Engine.getBattleFieldAlliesNum()));
        machineTabComponentController.setContestDifficulty(Engine.getBattleFieldDifficultLevel());
    }


    private void showConfirmationAlert(String HeaderText){
        this.alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(HeaderText);
        alert.showAndWait();
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



    private void UpdateUBoatEngine(Response response)  {
        //convert the CTEEnigma object from the response body
        String jsonCTEMachine = null;
        try {
            jsonCTEMachine = Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.Engine.setXmlEnigma( GSON_INSTANCE.fromJson(jsonCTEMachine, CTEEnigma.class)); // extract the CTEEnigma from json
        this.Engine.setEngineObjects();// update engine necessary inner objects
    }

    private void UpdateMachineTabComponents(){
        //commands below will happen when we will take a data from the server
        machineTabComponentController.setRandomConfLabelOpacity(0);
        machineTabComponentController.UnlockLockTab1Components(true);
        setT1PartConfTextField(); //This function will set the text in the part conf text field (option 2)
        setDictionaryComponents();//sets dictionary words
        setXMLContestComponentDetails();//set initial xml file details component in machine tab
        ContestTab.setDisable(false);//open the next tab to the user
    }


    private void failureAction(String processName,String failureReason){
        this.alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(processName);
        alert.setContentText(failureReason);
        alert.showAndWait();
    }

    @FXML
    void LogOutMachineTab(MouseEvent event) {
        close();
        logout();
    }


    //---------------server communication---------------//





    @Override
    public void uploadXMLFileToServer(String filePath) throws InvalidXmlFileException  {
        Engine.CheckXMLFileToUploadIntoServer(filePath); // checks if the file path is legal
        File file = new File(filePath);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file","",
                        RequestBody.create(file, MediaType.parse("text/xml"))).build();

        String finalUrl = HttpUrl.parse(Constants.UPLOAD_FILE)
                .newBuilder().addQueryParameter("filename", file.getName()).build().toString();

        Request request = new Request.Builder()
                .url(finalUrl)
                .method("POST", body)
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                        String responseBody = null;
                        try {
                            responseBody = response.body().string();
                            showErrorAlert("XML file error",
                                    "Error in upload xml file to server:\n" + "Error Code: " + response.code() + "\n details: " + responseBody);
                            //disable polling stuffs from server cause the file is invalid + clear all screen data function !
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {Platform.runLater(() -> {
                    //poll from server things  - start this function process !!!
                    UpdateUBoatEngine(response); // creates here enigma machine object in this UBoat Engine
                    UpdateMachineTabComponents();//Updates all components at the Machine tab
                    startTableRefresher();//starts to ask server for allies associated to u-boat competition
                    showConfirmationAlert("XML file loaded to server successfully!");
                    machineTabComponentController.getT1CurrMachineConfTextArea().clear();
                });
                }
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    showErrorAlert("File Upload Process ","File Upload failed!\n" + "Reason: " + e.getMessage());
                });

            }
        });
    }

    @Override
    public void setMessageOfTheCompetition(ArrayList<String> machineDetails) {
        String finalUrl = HttpUrl.parse(SEND_MACHINE_DETAILS)
                .newBuilder().addQueryParameter("machineConf",machineDetails.get(0))
                .addQueryParameter("encryptMessage", machineDetails.get(1))
                .addQueryParameter("wordToDecrypt",machineDetails.get(2) )
                .build().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {Platform.runLater(() -> {
                showErrorAlert("Send Machine Details Process","Send Machine Details Failed!\n" + "Reason: " + e.getMessage());});
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                        showErrorAlert("Updating Server MachineConf/Encrypt Message",responseBody);
                    });
                }
            }});

    }

    @Override
    public void sendServerReadyNotice( boolean Ready) {
        MachineTab.setDisable(Ready);
        String finalUrl = HttpUrl.parse(ANNOUNCE_READY)
                .newBuilder().addQueryParameter("type", UBOAT)//type of user, username not needed cause of the session and cookies
                .addQueryParameter("readyStatus", String.valueOf(Ready)) // ready status
                .build().toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseBody = response.body().string();
                if (response.code() != 200 ) {
                    if(response.code()==100){
                        Platform.runLater(() -> {
                            showInformationAlert("Send Ready Announce TO Server ", responseBody);
                        });}
                    else{
                    Platform.runLater(() -> {
                        showErrorAlert("Send Ready Announce TO Server ", responseBody);
                    });
                    }
                }

            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    showErrorAlert("Send Ready Announce Process", "Send Ready Announce Failed!\n" + "Reason: " + e.getMessage());
                });
            }

        });
    }


    @Override
    public void showActiveTeamDetailsInCompetition(ArrayList<TableDataObject> alliesInTable) {
        //starts here the refresher to update u boat allies associated table
        contestTabComponentController.addAlliesDetails(alliesInTable);

    }

    private  void startTableRefresher(){
        //starts here the refresher to update u boat allies associated table
        UBoatRefresher = new UBoatAlliesRefresher(
                this::updateAlliesAssociatedTable);
        timer = new Timer();
        timer.schedule(UBoatRefresher, REFRESH_RATE, REFRESH_RATE);
    }

    private void updateAlliesAssociatedTable(UBoatDTO UBoatDataPackage) {

        Platform.runLater(() -> {
            if (UBoatDataPackage!=null ) {
                if (!UBoatDataPackage.isAlliesAssociateTabledEmpty()) {
                    showActiveTeamDetailsInCompetition(UBoatDataPackage.getAlliesInTable());
                    if (UBoatDataPackage.inContest()) {
                        if (contestStartedNow) // enters here only one time-> when starting contest
                        {
                            contestStartedNow = false;
                            allAbandon = false;
                            contestTabComponentController.setReadyButtonDisable(true);
                            contestTabComponentController.setGameStatusLabel("RUNNING");
                        }
                        updateCandidatesPart(UBoatDataPackage.getCandidates());//update the table view of candidates
                        checkForWinner(UBoatDataPackage.getCandidates());//checking for the fastest winner.
                    }
                } else if (UBoatDataPackage.isAlliesAssociateTabledEmpty() && UBoatDataPackage.inContest() && !allAbandon) { // all allies abandon competition
                    showErrorAlert("Contest process", "all allies abandon contest while contest is in progress!" +
                            "\nTo reset all data please press on 'reset all data button'");
                    contestTabComponentController.setResetAllDataButtonDisable(false);
                    MachineTab.setDisable(false);
                    allAbandon = true;
                }
            }
        });
    }


    @Override
    public void updateCandidatesPart(ArrayList<MissionResults> candidates) {
        //starts here the refresher to update u boat candidates associated table
        contestTabComponentController.addAllCandidates(candidates);
    }

    private void checkForWinner(ArrayList<MissionResults> candidates) {
        //if someone won call the winnerAnnounceServlet
        ArrayList<Pair<String,Long>> currentDecrypts = new ArrayList<>();
        for(MissionResults candidate:candidates){
            if(candidate.getCandidate().equals(contestTabComponentController.getMessageBeforeDecrypt()))
                currentDecrypts.add(new Pair<>(candidate.getAllieResponsible(),candidate.getEncryptionTime()));
        }
        Collections.sort(currentDecrypts, new Comparator<Pair<String, Long>>() {
            @Override
            public int compare(Pair<String, Long> o1, Pair<String, Long> o2) {
                if(o1.getValue()>o2.getValue())
                    return 0;
                else return 1;
            }
        });

        //calling the winnerAnnounceServlet and shut down in this app the whole candidate's creation progress
        if(!currentDecrypts.isEmpty()){
            AnnounceWinner(currentDecrypts.get(0).getKey());
        }

    }

    @Override
    public void AnnounceWinner(String allyName) {
        //calling the winnerAnnounceServlet + updated the running boolean in server
        String finalUrl = HttpUrl.parse(WINNER_ANNOUNCE)
                .newBuilder().addQueryParameter("allyName", allyName)//type of user, username not needed cause of the session and cookies
                .build().toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                        showErrorAlert("Winner Announce to server ", responseBody);
                    });
                } else {
                    Platform.runLater(() -> {
                        contestTabComponentController.setResetAllDataButtonDisable(false);
                        contestTabComponentController.setWinnerLabel("The Winner Is "+ allyName);
                        contestTabComponentController.setGameStatusLabel("IDLE");
                        contestStartedNow =true;
                        MachineTab.setDisable(false);
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                showErrorAlert("Send winner Announce Process", "Send winner Failed!\n" + "Reason: " + e.getMessage());
            }
        });

    }

    @Override
    public void logout() {
        //return to the login stage again
            String finalUrl = HttpUrl.parse(LOGOUT)
                    .newBuilder()
                    .addQueryParameter("type", UBOAT)//type of user, username not needed cause of the session and cookies
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
                                    fatherController.switchToUBoatLoginPage();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });}
                        }
                    });
    }


    public void resetAllContestsDataInServer(){
        String finalUrl = HttpUrl.parse(RESET_COMPETITION_DATA)
                .newBuilder()
                .build().toString();

        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() -> {
                    showErrorAlert("Reset all data at server Process  ", "Reset data at server Failed!\n" + "Reason: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                if (response.code() != 200) {
                    Platform.runLater(() -> {
                        showErrorAlert("Reset all data at server Process  ", responseBody);
                    });
                }
            }
        });

    }



    public EngineObject getEngine() {
        return Engine;
    }

    public void close() {
        contestTabComponentController.clearAllFields();
        if (UBoatRefresher != null && timer != null) {
            UBoatRefresher.cancel();
            timer.cancel();
            UBoatRefresher=null;
            timer=null;
        }
    }

    public void setMachineTabDisable( boolean status){
        MachineTab.setDisable(status);
    }


}


