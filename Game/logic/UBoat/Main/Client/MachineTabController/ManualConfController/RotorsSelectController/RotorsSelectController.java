package Main.Client.MachineTabController.ManualConfController.RotorsSelectController;

import Main.Client.MachineTabController.ManualConfController.ManualConfigurationController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RotorsSelectController {

    @FXML
    private Label rotorsNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="RotorsAvailableRadioViewList"
    private ListView<CheckBox> RotorsAvailableCheckViewList,RotorsOrdered; // Value injected by FXMLLoader

    @FXML // fx:id="selectRotorsButton"
    private Button selectRotorsButton; // Value injected by FXMLLoader


    @FXML
    private ListView<HBox> RotorsPositionCheckViewList;

    @FXML
    private Button selectRotorsPosButton;


    private Alert alert;

    private String Output;

    private List<Integer> Rotors;

    private ArrayList<Pair<Integer,Integer>> rotorsChosen;


    private ManualConfigurationController manualConfigurationController;

    private Integer rotorsCount;

    public String getOutput() {
        return Output;
    }

    public void setOutput(String output) {
        Output = output;
    }

    public ListView<CheckBox> getRotorsAvailableCheckViewList() {
        return RotorsAvailableCheckViewList;
    }

    /*    public ComboBox<Integer> getRotorsNumComboBox() {
          return rotorsNumComboBox;
    }*/

    public Button getSelectRotorsButton() {
        return selectRotorsButton;
    }

    public ListView<CheckBox> getRotorsOrdered() {
        return RotorsOrdered;
    }

    public ListView<HBox> getRotorsPositionCheckViewList() {
        return RotorsPositionCheckViewList;
    }

    public void setManualConfController(ManualConfigurationController manualConfController) {
        this.manualConfigurationController = manualConfController;
    }

    public void setInterface(String output, List<Integer> rotors, Integer rotorsCount){
        Output = output;
        Rotors = rotors;
        RotorsOrdered= new ListView<CheckBox>();;
        String Helper="";
        //setting the num of rotors option
        this.rotorsCount=rotorsCount;
        rotorsNumberLabel.setText("Number of rotors this machine demands:"+rotorsCount);

        //setting the rotors checkboxes
        for (int i = 0; i < Rotors.size(); i++) { // gets all rotors
            Helper = String.valueOf(Rotors.get(i));
            CheckBox X = new CheckBox();//creat a checkboxes
            X.setText(Helper);
            RotorsAvailableCheckViewList.getItems().add(X);// push them to list of rotors checkboxes
        }
    }

    @FXML // rotors controller
    void submitSelectedRotors(MouseEvent event) {
        alert = new Alert(Alert.AlertType.ERROR);
        manualConfigurationController.getUnit().setUIMessageInput("");
        alert.setHeaderText("Rotors Choosing Process");
        rotorsChosen= new ArrayList<Pair<Integer,Integer>>();
        Integer counter = 0;
        String rotors = "";
        for (int i = 0; i < RotorsAvailableCheckViewList.getItems().size(); i++) {
            if (RotorsAvailableCheckViewList.getItems().get(i).isSelected()) {
                rotorsChosen.add(new Pair<>(Integer.valueOf(RotorsAvailableCheckViewList.getItems().get(i).getText()),0));//adds to the rotors chosen
                counter++;
            }
        }

        if(!counter.equals(rotorsCount)){
            alert.setContentText("you haven't choose demanded number of rotors! please choose "+rotorsCount);
            alert.showAndWait();}

        else if(counter==0) {
            alert.setContentText("you haven't chose any rotors ! please chose ! ");
            alert.showAndWait();
        }
        else{
            alert = new Alert(Alert.AlertType.INFORMATION);
            selectRotorsButton.setDisable(true);
            RotorsAvailableCheckViewList.setDisable(true);
            selectRotorsPosButton.setDisable(false);//
            RotorsPositionCheckViewList.setDisable(false);
            creatPositionViewListValues();//part 2 in this controller -> setting the pos in the enigma machine
            alert.setHeaderText("Rotors Choosing Process");
            alert.setContentText("Operation succeed!");
            alert.showAndWait();
        }

    }

    //creating label + choice box to each rotor to order them
    void creatPositionViewListValues(){
        String helper;
        ListView<HBox> temp = new ListView<HBox>();
        for (int i =0; i < rotorsChosen.size(); i++) {
            helper = String.valueOf(rotorsChosen.get(i).getKey());
            HBox newOne = new HBox();
            newOne.setSpacing(10);
            //check boxes to the order
            ComboBox<String> X = new ComboBox<String>();//creat a checkboxes
            for (int j = 1; j <=rotorsChosen.size() ; j++) {
                X.getItems().add(String.valueOf(j));
            }
            X.setVisibleRowCount(5);
            X.setValue(String.valueOf(i+1));
            newOne.getChildren().add(new javafx.scene.control.Label((rotorsChosen.get(i).getKey() +"->Order pos:")));
            newOne.getChildren().add(X);
            temp.getItems().add(newOne);//adding to the listview temp
        }
        RotorsPositionCheckViewList.getItems().addAll(temp.getItems());
    }

    //submit selected positioning of rotors
    @FXML
    void submitSelectedRotorsPos(MouseEvent event) {
        alert.setHeaderText("Rotors pos choosing Process");
        String rotors = "";
        alert = new Alert(Alert.AlertType.INFORMATION);
        for (int i = 0; i < RotorsPositionCheckViewList.getItems().size(); i++) {//sets the rotor chosen array
            HBox temp = RotorsPositionCheckViewList.getItems().get(i);//getting the HBOX
            ComboBox<String> tempCombo = (ComboBox<String>) temp.getChildren().get(1);//get the value of the combo box
            rotorsChosen.set(i, new Pair<>(rotorsChosen.get(i).getKey(), Integer.valueOf(tempCombo.getValue())));
        }
        rotorsChosen.sort(new Comparator<Pair<Integer, Integer>>() {
            @Override
            public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
                if (o1.getValue() > o2.getValue()) {
                    return 1;
                } else if (o1.getValue().equals(o2.getValue())) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        //checks if there is 2 rotors that shares same position
        if (checkMultipleSameChoosing()) {

            rotors = setRotorAvailableListview(RotorsOrdered);
            char temper[] = rotors.toCharArray();
            temper[rotors.length() - 1] = '>';
            rotors = new String(temper);
            Output += "<" + rotors;
            manualConfigurationController.getUnit().setUIMessageInput(manualConfigurationController.getUnit().getUIMessageInput() +
                    Output);
            selectRotorsPosButton.setDisable(true);
            RotorsPositionCheckViewList.setDisable(true);
            manualConfigurationController.setDisableStatusOfInitSubmitButton(false);
            manualConfigurationController.setInitProcess();
            alert.setHeaderText("Rotors Placement Choosing Process");
            alert.setContentText("Operation succeed!");
            alert.showAndWait();

        }
    }

    private  boolean checkMultipleSameChoosing() {
        for (int i = 0; i < rotorsChosen.size()-1; i++) {
            if(rotorsChosen.get(i).getValue()==rotorsChosen.get(i+1).getValue()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("One or more rotors shares same position! , please choose one position for each! ");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }

    private String setRotorAvailableListview(ListView<CheckBox> RotorsOrdered){
        //setting the rotors checkboxes
        String Helper="";
        for (int i = 0; i <= rotorsChosen.size()-1; i++) { // gets all rotors
            Helper += rotorsChosen.get(i).getKey()+ ",";;
            CheckBox X = new CheckBox();//creat a checkboxes
            X.setText(String.valueOf(rotorsChosen.get(i).getKey()));
            X.setSelected(true);
            RotorsOrdered.getItems().add(X);// push them to list of rotors checkboxes
        }
        return Helper;
    }
}
