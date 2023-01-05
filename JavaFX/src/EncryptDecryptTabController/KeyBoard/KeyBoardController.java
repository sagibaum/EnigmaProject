package EncryptDecryptTabController.KeyBoard;

import EncryptDecryptTabController.EncryptDecryptTabController;
import EngineFunctionallity.EngineObject;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import user.data.transfer.unit.UserDataTransferUnit;

import java.util.ArrayList;
import java.util.List;

public class KeyBoardController {
    @FXML
    private ScrollPane T2keyboardScrollPane;
    private EngineObject Engine;

    private UserDataTransferUnit UICommunicationUnit;

    private List<Button> activatedAnimation;

    private EncryptDecryptTabController fatherController;

    private FlowPane T2keyboardFlowPane;


    public void  setKeyBoardController(EngineObject engine , EncryptDecryptTabController encryptDecryptTabController
            ,UserDataTransferUnit UICommunicationUnit){
        this.Engine=engine;
        this.UICommunicationUnit = UICommunicationUnit;
        this.activatedAnimation = new ArrayList<Button>();
        this.fatherController = encryptDecryptTabController;
        T2keyboardFlowPane = new FlowPane();
    }

    public void setKeyBoardFunctionality(){
        T2keyboardFlowPane.setDisable(true);
        setBoardsCharacteristics(T2keyboardFlowPane);
        setKeyBoardKeys(Engine.getAbc());
    }

    public boolean isActivatedAnimationListEmpty(){
        return activatedAnimation.isEmpty();
    }

    public Button removeAndReturnFromActivatedAnimationList(Integer index){
        return activatedAnimation.remove(0);
    }

    public void setT2keyboardScrollPaneDisable(boolean status) {
        T2keyboardScrollPane.setDisable(status);
    }
    public void setT2keyboardFlowPaneDisable(boolean status){
        T2keyboardFlowPane.setDisable(status);
    }

    private void setBoardsCharacteristics(FlowPane flowPane) {
        flowPane.setVgap(20);
        flowPane.setHgap(20);
        flowPane.setColumnHalignment(HPos.CENTER);
        flowPane.setRowValignment(VPos.CENTER);
        flowPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        flowPane.setAlignment(Pos.CENTER);

    }


    private void setKeyBoardKeys(String ABC) {
        int radius = 30;
        for (int j = 0; j < ABC.length(); j++) {
            Button Letter = new Button();
            Letter.setText(String.valueOf(ABC.charAt(j)));
            //Letter.getStylesheets().add(String.valueOf((getClass().getResource("colored-toggle.css".toString()))));
            Letter.setShape(new Circle(radius));// make the button a circle!
            Letter.setMinSize(1.2 * radius, 1.2 * radius);
            Letter.setMaxSize(1.2 * radius, 1.2 * radius);
            Letter.setOnMouseClicked((MouseEvent event) -> {
                String encrypted = Engine.InputEncryptionOrDecoding(Letter.getText(), UICommunicationUnit, true);
               // EncryptResultTextAreaT2.setText(EncryptResultTextAreaT2.getText() + encrypted);
                fatherController.setEncryptResultTextArea(fatherController.getEncryptResultTextArea() + encrypted);
                //EncryptTextAreaT2.setText(EncryptTextAreaT2.getText() + Letter.getText());
                fatherController.setUserEncryptTextAreaInput(fatherController.getUserEncryptTextAreaInput() + Letter.getText());

                if(!activatedAnimation.isEmpty()){//stop [rev animation
                    fatherController.stopAnimation(activatedAnimation.remove(0));
                    //stopGlowingAnimation(activatedAnimation.remove(0));
                }
                activatedAnimation.add(fatherController.getSpecifiedBulb(encrypted));//save this animation to stop it !
                //activatedAnimation.add(bulbs.get(encrypted));//save this animation to stop it !
                fatherController.startAnimation(fatherController.getSpecifiedBulb(encrypted));//will start glowing animation
                //makeGlowingAnimation(bulbs.get(encrypted));//will start glowing animation

               // MachineConfTextFieldT2.clear();
                fatherController.clearMachineConfTextFiled();
               // rootController.getT1CurrMachineConfTextArea().setText(UICommunicationUnit.getCurrentMachineConfiguration());
                fatherController.setFatherCurrMachineConfTextArea(UICommunicationUnit.getCurrentMachineConfiguration());
                //animations for bulbs:
                fatherController.getSpecifiedBulb(encrypted).fireEvent(event);
               // bulbs.get(encrypted).fireEvent(event);
            });
            //----button functionality match-----//
            T2keyboardFlowPane.getChildren().add(Letter);
        }
        T2keyboardScrollPane.setContent(T2keyboardFlowPane);
    }
}
