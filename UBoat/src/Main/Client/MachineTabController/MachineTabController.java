package Main.Client.MachineTabController;

import EngineFunctionallity.EngineObject;
import Main.Client.MachineTabController.ImagesController.ImagesGridPaneController;
import Main.Client.MachineTabController.MachineConfController.CurrentMachineConfController;
import Main.Client.MachineTabController.ManualConfController.ManualConfigurationController;
import Main.Client.MachineTabController.PartConfController.PartsConfController;
import Main.Client.MachineTabController.RandomConfController.RandomConfButtonController;
import Main.Client.UBoatClientController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import user.data.transfer.unit.UserDataTransferUnit;

import java.io.IOException;
import java.net.URL;

import static util.Constants.LOGO_IMAGE;
import static util.Constants.U_BOAT_POPUP_MANUAL_CONF_PAGE_FXML_RESOURCE_LOCATION;

public class MachineTabController {

    @FXML
    private Label T1CodeCalibrationLabel;

    @FXML
    private Label T1PartConfLabel;

    @FXML
    private Label T1CurrMachineConfLabel;

    @FXML
    private Button T1ManuallConfButton;

    @FXML
    private Label NameOfBattleFiled,RequiredAllies,MissionDifficulty;

    @FXML
    private Label T1ManualConfLabel;

    @FXML
    private Label T1RandomConfLabel;

    @FXML
    private TextArea T1PartTextArea;


    @FXML GridPane imagesGridPaneComponent;

    @FXML TextArea currentMachineComponent,partConfTextAreaComponent;

    @FXML Button randomConfButtonComponent;


    @FXML private ImagesGridPaneController imagesGridPaneComponentController;
    @FXML private CurrentMachineConfController currentMachineComponentController;
    @FXML  private RandomConfButtonController randomConfButtonComponentController;

    @FXML  private PartsConfController partConfTextAreaComponentController;

    private ManualConfigurationController manualConfigurationController;
    private EngineObject Engine;

    private UserDataTransferUnit UICommunicationUnit;

    private UBoatClientController rootController;

    private String currentCSS;


    public void setRootController(UBoatClientController rootController) {
        this.rootController = rootController;
    }


    public void setMachineTabController(EngineObject engine, UserDataTransferUnit UICommunicationUnit,String currentCss) {
        this.Engine = engine;
        this.UICommunicationUnit = UICommunicationUnit;
        this.currentCSS=currentCss;
       if(imagesGridPaneComponentController!=null&&currentMachineComponentController!=null&& randomConfButtonComponentController!=null)
        {
            setImagesGridPaneControllers();
            setCurrMachineConfController();
            setRandomConfButtonController();
        }

        this.manualConfigurationController= new ManualConfigurationController();

    }

    private void setRandomConfButtonController() {
        randomConfButtonComponentController.setRandomConfButtonController(this,Engine,UICommunicationUnit);
    }


    private void setCurrMachineConfController() {
        currentMachineComponentController.setCurrentMachineTextAreaController(this);
    }

    private void setImagesGridPaneControllers() {
        imagesGridPaneComponentController.setImagesGridPaneController(this,Engine,UICommunicationUnit);
    }

    //------------------------machine part data display------------------------//


    public void setComponentsAfterCodeConf(){
        currentMachineComponentController.updateCurrentMachineConfigTextFields(UICommunicationUnit);
        T1RandomConfLabel.setOpacity(1);
    }
    @FXML
    void ChooseMachineConfHandler(MouseEvent event) throws IOException {
        try {
            URL POPUP = getClass().getResource(U_BOAT_POPUP_MANUAL_CONF_PAGE_FXML_RESOURCE_LOCATION);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(POPUP);
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            manualConfigurationController = fxmlLoader.getController();
            manualConfigurationController.setManualConfController(Engine,UICommunicationUnit,stage);// insert here the function that get the engine object//

            stage.initModality(Modality.APPLICATION_MODAL);//block the user from doing other actions
            stage.initStyle(StageStyle.DECORATED);// now te pop up window will have a toolbar
            stage.setTitle("Manually Configuration Window");
            stage.setScene(new Scene(root1));
            stage.getIcons().add(new Image(LOGO_IMAGE));
            if(!currentCSS.equals(""))
                stage.getScene().getRoot().getStylesheets().add(String.valueOf(getClass().getResource(currentCSS)));
            stage.showAndWait();
        }
        catch(Exception e) {
           // System.out.println(e.getCause().getMessage());
        }
        if(UICommunicationUnit.getOperationStatus()){
            Engine.setMachine(UICommunicationUnit.getUIMessageInput().toUpperCase(), UICommunicationUnit);
            currentMachineComponentController.updateCurrentMachineConfigTextFields(UICommunicationUnit);
            T1RandomConfLabel.setOpacity(1);
            UICommunicationUnit.setOperationStatus(false);

        }
    }

    public TextArea getT1CurrMachineConfTextArea() {
        return  currentMachineComponentController.getCurrMachineConfTextArea();
    }



    //This function will set the text in the part conf text field (option 2) after the system successfully loaded xml
    private void setPartConfTextField(){
        Engine.viewPartialMachineSpecs(UICommunicationUnit);
        T1PartTextArea.setText(UICommunicationUnit.getEngineOutputMessage());
    }

    //This function will unlock functionality of specific components after the system successfully loaded xml
    // file , and if it's not valid it will lock them back
    public void UnlockLockTab1Components(boolean unlock){
        if(unlock){

            imagesGridPaneComponentController.setImagesDisable(false);
            T1ManuallConfButton.setDisable(false);
            randomConfButtonComponentController.setButtonDisable(false);
        }
        else{

            imagesGridPaneComponentController.setImagesDisable(true);
            T1ManuallConfButton.setDisable(true);
            randomConfButtonComponentController.setButtonDisable(true);
        }
    }


    public void setFatherStage(Stage fatherStage) {
        imagesGridPaneComponentController.setFatherStage(fatherStage);
    }


    public void setRandomConfLabelOpacity(Integer value){
        T1RandomConfLabel.setOpacity(value);
    }


    public void setPartTextArea(String value){
        partConfTextAreaComponentController.setPartTextArea(value);
    }

    public void switchTabs() {//switching tabs here if the user finished auto/manual encrypt
        rootController.switchBetweenTabs(UBoatClientController.Tabs.CONTEST_TAB.getValue(),false);
    }

    //after loading valid XML file
    public void setDictionaryWords(){
        rootController.setDictionaryComponents();
    }

    public void setRequiredAlliesLabel(String str){RequiredAllies.setText(str);}

    public void setContestDifficulty(String str){MissionDifficulty.setText(str);}

    public void setNameOfBattleFiled(String str){ NameOfBattleFiled.setText(str);}

}

