package Main.Client.MachineTabController.ManualConfController.PlugBoardController;

import Main.Client.MachineTabController.ManualConfController.ManualConfigurationController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import javafx.scene.shape.Circle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class PlugBoardController {
    @FXML // fx:id="plugBoardScrollPane"
    private javafx.scene.control.ScrollPane plugBoardScrollPane; // Value injected by FXMLLoader

    @FXML // fx:id="plugPairTextField"
    private javafx.scene.control.TextField plugPairTextField; // Value injected by FXMLLoader

    @FXML // fx:id="resetPlugButton"
    private javafx.scene.control.Button resetPlugButton; // Value injected by FXMLLoader

    @FXML // fx:id="submitPlugPairButton"
    private Button submitPlugPairButton; // Value injected by FXMLLoader



    private String Output;

    private GridPane plugBoardGridPane;// for plugboard

    private FlowPane plugBoardFlowPane;
    private Alert alert;

    private Integer randomer;

    private Color randomColorValue;
    private ManualConfigurationController manualConfigurationController;
    private String ABC,result;

    private Hashtable<String, List<ToggleButton>> coloredButtons;

    public String getOutput() {
        return Output;
    }

    public void setOutput(String output) {
        Output = output;
    }

    public TextField getPlugPairTextField() {
        return plugPairTextField;
    }

    public ScrollPane getPlugBoardScrollPane() {
        return plugBoardScrollPane;
    }

    public Button getSubmitPlugPairButton() {
        return submitPlugPairButton;
    }

    public Button getResetPlugButton() {
        return resetPlugButton;
    }

    public void setManualConfController(ManualConfigurationController manualConfController) {
        this.manualConfigurationController = manualConfController;
    }
    public void SetPlugboardControllerABC(String ABC){
        this.ABC =ABC;

    }

    public void setPlugBoardScrollPane(String UIMessage,boolean status){// sets plug board as grid pane --> add function that if 2 buttons has been pushed all the other becomes disabled!and push inside to each one!
       Output = UIMessage;
       randomer=0;
       coloredButtons = new Hashtable<String,List<ToggleButton>>();
        int numOfABC = ABC.length();
        int radius=30;
        plugBoardFlowPane = new FlowPane();
        setPropertiesOfPlugBoard();
        for (int j = 0; j <numOfABC; j++){
                ToggleButton Letter = new ToggleButton();
                Letter.setText(String.valueOf(ABC.charAt(j)));
                Letter.getStylesheets().add(String.valueOf((getClass().getResource("colored-toggle.css".toString()))));
                Letter.setShape(new Circle(radius));// make the button a circle!
                Letter.setMinSize(1.5*radius,1.5*radius);
                Letter.setMaxSize(1.5*radius,1.5*radius);
                Letter.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        if(Letter.isSelected())
                        buttonFunctionalityFirstPress(Letter);

                        else buttonFunctionalityUnpressed(Letter);

                    }
                });
            plugBoardFlowPane.getChildren().add(Letter);
        }
        plugPairTextField.setEditable(false);
        plugBoardScrollPane.setContent(plugBoardFlowPane);
        plugBoardScrollPane.setDisable(status);
    }

    void buttonFunctionalityFirstPress(ToggleButton B){
        if(randomer%2 ==0) {
            Random random = new Random();
            int r = random.nextInt(256),g = random.nextInt(256),b = random.nextInt(256);
            randomColorValue = new Color(r, g, b);
             result =Integer.toHexString(randomColorValue.darker().getBlue())+Integer.toHexString(randomColorValue.darker().getRed())
                    +Integer.toHexString(randomColorValue.brighter().getGreen());
           while(result.length()<6) result+="f";
           result="-fx-background-color: #"+result;
            B.setStyle(result);
            List<ToggleButton> buttonsColor=new ArrayList<ToggleButton>();
            buttonsColor.add(B);//add to the colored list to this button
            coloredButtons.put(result,buttonsColor);
            randomer++;

        }
        else {
            B.setStyle(result);
            coloredButtons.get(result).add(B);//adds the second button to this color list buttons
            randomer--;
        }
        plugPairTextField.setText(plugPairTextField.getText()+ B.getText());

    }

    void buttonFunctionalityUnpressed(ToggleButton B){
        for (ToggleButton temp:coloredButtons.get(B.getStyle())) {
            StringBuilder string = new StringBuilder(plugPairTextField.getText());
            string.deleteCharAt(string.indexOf(temp.getText()));
            plugPairTextField.setText(string.toString());
            temp.setStyle("-fx-background-color:black");
            temp.setSelected(false);
            randomer--;
        }
    }

    void setPropertiesOfPlugBoard(){
        plugBoardFlowPane.setStyle("-fx-background-color: peru;");
        plugBoardFlowPane.setMaxSize(Region.USE_COMPUTED_SIZE,Region.USE_COMPUTED_SIZE);
        plugBoardFlowPane.setVgap(20);
        plugBoardFlowPane.setHgap(20);
        plugBoardFlowPane.setColumnHalignment(HPos.CENTER);
        plugBoardFlowPane.setRowValignment(VPos.CENTER);
        plugBoardFlowPane.setAlignment(Pos.CENTER);
    }

    @FXML
    void resetPlugPairTextField(MouseEvent event) {
        setPlugBoardScrollPane(Output,false);
        plugPairTextField.clear();
    }

    @FXML
    void submitInitPlugs(MouseEvent event) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        if(plugPairTextField.getText().length()%2==0){
        Output +="<"+plugPairTextField.getText() + ">";
        //test validation?
        manualConfigurationController.getUnit().setUIMessageInput( manualConfigurationController.getUnit().getUIMessageInput()+
                Output);
        alert.setHeaderText("Manually Code Configuration Process");
        alert.setContentText("Manually code configuration process went successfully!\n" +
                " please press ok or exit from this window to return to the main application.");
        alert.showAndWait();
        manualConfigurationController.closeWindow();
    }
        else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Manually Code Configuration Process-Plugboard");
            alert.setContentText("Please choose even plugs to connect with or clean board(for none) and submit!" );
            alert.showAndWait();
        }
    }
}
