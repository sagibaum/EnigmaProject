/**
 * Sample Skeleton for 'sagiEnigma.fxml' Controller Class
 */

package RootController;

import BruteForceTabController.BruteForceController;
import EncryptDecryptTabController.EncryptDecryptTabController;
import EngineFunctionallity.EngineObject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import user.data.transfer.unit.UserDataTransferUnit;

import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    @FXML
    private Pane mainGrid;//sagi added

    @FXML
    private ScrollPane mainStage;//sag added

    @FXML
    private Button LoadButton;

    @FXML // fx:id="FilePathTextField"
    private TextField FilePathTextField; // Value injected by FXMLLoader

    @FXML // fx:id="SkinChoiceBox"
    private ComboBox<String> SkinComboBox; // Value injected by FXMLLoader

//sagi work//
    @FXML GridPane encryptComponent,bruteForceComponent,machineTabComponent;
    @FXML private EncryptDecryptTabController encryptComponentController;
    @FXML private BruteForceController bruteForceComponentController;
   // @FXML private MachineTabController machineTabComponentController;

    EngineObject Engine;
    UserDataTransferUnit UICommunicationUnit;

    Alert alert;

        private String filePath;

        private String currentCSS;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.Engine = new EngineObject();
        this.UICommunicationUnit =new UserDataTransferUnit();
        currentCSS="";
      /*  setMachineTab(Engine,UICommunicationUnit);
        setEncrypt(Engine,UICommunicationUnit);
        setBruteForce(Engine,UICommunicationUnit);*/
        this.UICommunicationUnit.setOperationStatus(false);
        SkinComboBox.getItems().add("Light");
        SkinComboBox.getItems().add("Dark");
        SkinComboBox.getItems().add("Blue");
        SkinComboBox.getItems().add("Red");
        SkinComboBox.setValue("Light");
        /*SkinComboBox.setOnAction(this::skinChooser);*/
        //file = new File("/Resources/Video/EnigmaExplanation.mp4");
        /*URL url = getClass().getClassLoader().getResource("Resources/Video/EnigmaExplanation.mp4");
        videoMedia = new Media(url.toExternalForm().toString());//insert to
        //HERE WHEN THIS IS MOVED TO JARS, NEED TO CHANGE TO RELATIVE PATH USING CLASS LOADER IN THE FINAL FOLDER
        //videoMedia = new Media(file.toURI().toString());//insert to media object the file
        videoPlayer = new MediaPlayer(videoMedia);
        videoViewT1.setMediaPlayer(videoPlayer);
        //not working
        VolumeVideoSlider.valueProperty().addListener((observable, oldValue, newValue) -> videoPlayer.setVolume(VolumeVideoSlider.getValue()*0.01));
       mainGrid.widthProperty().addListener((observable, oldValue, newValue) ->{
           videoViewT1.fitWidthProperty().setValue(newValue);
                });*/
        filePath="";
    }

  /*  public void setMachineTabImageScales(Stage fatherStage){
        machineTabComponentController.setFatherStage(fatherStage);
    }


    //sets tab1 controller
    void setMachineTab(EngineObject Engine,UserDataTransferUnit UICommunicationUnit){
        machineTabComponentController.setRootController(this);
        machineTabComponentController.setMachineTabController(Engine,UICommunicationUnit,currentCSS);
    }


    //sets tab2 controller
    void setEncrypt(EngineObject Engine,UserDataTransferUnit UICommunicationUnit){
        encryptComponentController.setRootController(this);
        encryptComponentController.getMachineConfTextFieldT2().textProperty().bind(machineTabComponentController.getT1CurrMachineConfTextArea().textProperty());//bind properties
        encryptComponentController.setEncryptTabController(Engine,UICommunicationUnit);
    }

    //sets tab3 controller
    void setBruteForce(EngineObject Engine,UserDataTransferUnit UICommunicationUnit){
        bruteForceComponentController.setRootController(this);
        bruteForceComponentController.getT3CurrMachineTextFiled().textProperty().bind(machineTabComponentController.getT1CurrMachineConfTextArea().textProperty());//bind properties
        bruteForceComponentController.setBruteForceTabController(Engine,UICommunicationUnit);
    }




    //This functions will build the CTEEnigma Machine
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
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("XML file error");
        if(!FilePathTextField.getText().equals("")) {
            try {
                Engine.UploadXmlFile(FilePathTextField.getText(), UICommunicationUnit);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("XML file loaded successfully!");
                setT1PartConfTextField(); //This function will set the text in the part conf text field (option 2)
                machineTabComponentController.UnlockLockTab1Components(true);
                alert.show();
                machineTabComponentController.setRandomConfLabelOpacity(0);
                //T1RandomConfLabel.setOpacity(0);
                encryptComponentController.setEncryptTabButtonsUnavailable(true);
                encryptComponentController.setKeyBoardAndBulbBoard();//sets the tab2 keyboard and bulb board
                bruteForceComponentController.setDMConfig();
                bruteForceComponentController.setDictionaryHBoxComponentController();
                bruteForceComponentController.setDictionaryListViewComponentController();
            } catch (Exception e) {//will throw an alarm alert window!
                alert.setContentText(e.getCause().getMessage());
                FilePathTextField.clear();
                FilePathTextField.setText(filePath);//returns the text in the text box to the prev state
                alert.showAndWait();
            }
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

//----------------------tab1----------------------//

    //Tab 2:


    public TextArea getT1CurrMachineConfTextArea() {
        return machineTabComponentController.getT1CurrMachineConfTextArea();
    }


    public String getCurrentCSS() {
        return currentCSS;
    }

    @FXML
    void skinChooser(ActionEvent event){
        if(this.SkinComboBox.getValue().equals("Dark")){
            SkinComboBox.getScene().getRoot().getStylesheets().clear();
            SkinComboBox.getScene().getRoot().getStylesheets().add(String.valueOf((getClass().getResource("dark_mode.css".toString()))));
            this.currentCSS = "dark_mode.css";
        }
        else if(this.SkinComboBox.getValue().equals("Blue")){
            SkinComboBox.getScene().getRoot().getStylesheets().clear();
            SkinComboBox.getScene().getRoot().getStylesheets().add(String.valueOf((getClass().getResource("light_blue.css".toString()))));
            this.currentCSS = "light_blue.css";
        }
        else if(this.SkinComboBox.getValue().equals("Red")){
            SkinComboBox.getScene().getRoot().getStylesheets().clear();
            SkinComboBox.getScene().getRoot().getStylesheets().add(String.valueOf((getClass().getResource("red_mode.css".toString()))));
            this.currentCSS = "red_mode.css";
        }
        else{
            SkinComboBox.getScene().getRoot().getStylesheets().clear();
            this.currentCSS = "";
        }
    }

     public RootController getRootController(){
        return this;
 }


    public BruteForceController getBruteForceComponentController() {
        return bruteForceComponentController;
    }

    public void setEncryptTabButtonsUnavailable(boolean state){
        encryptComponentController.setEncryptTabButtonsUnavailable(state);
    }*/

}