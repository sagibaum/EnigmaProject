package Main.Client.MachineTabController.ImagesController;

import EngineFunctionallity.EngineObject;
import Main.Client.MachineTabController.MachineTabController;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import user.data.transfer.unit.UserDataTransferUnit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ImagesGridPaneController {


    @FXML
    private ImageView T1ReflectorImage;

    @FXML
    private ImageView T1RotorImage;

    @FXML
    private ImageView T1KeyBoardImage;

    @FXML
    private ImageView T1PlugImage;

    @FXML
    private ImageView T1MachineImage;


    private MachineTabController machineTabController ;
    private EngineObject Engine;
    private UserDataTransferUnit UICommunicationUnit;

    private HashMap<String, ScaleTransition> imageAnimations;

    private Stage fatherStage;

    public void setImagesGridPaneController(MachineTabController machineTabController, EngineObject engine, UserDataTransferUnit UICommunicationUnit) {
        this.machineTabController=machineTabController;
        this.Engine = engine;
        this.UICommunicationUnit = UICommunicationUnit;
        setAnimations();
    }

    public void  setImagesDisable(boolean status){
        T1PlugImage.setDisable(status);
        T1ReflectorImage.setDisable(status);
        T1RotorImage.setDisable(status);
        T1MachineImage.setDisable(status);
        T1KeyBoardImage.setDisable(status);
    }


//------------------------machine part data display------------------------//

    @FXML
    void displayABC(MouseEvent event) {
        String ABC = "The ABC in this machine: "+Engine.getAbc();
        machineTabController.setRandomConfLabelOpacity(0);
        machineTabController.setPartTextArea(ABC);

    }

    @FXML
    void displayAvailableReflectors(MouseEvent event) {
        String Reflectors = "Reflectors available:\n";
        machineTabController.setRandomConfLabelOpacity(0);
        for (int i = 0; i <Engine.getReflectorsAvailable().size(); i++) {
            Reflectors+="Reflector ID:{"+Engine.getRotorsAvailable().get(i)+ "} ";
        }
        machineTabController.setPartTextArea(Reflectors);
    }

    @FXML
    void displayAvailableRotors(MouseEvent event) {
        String Rotors = "Rotors available:\n";
        machineTabController.setRandomConfLabelOpacity(0);
        for (int i = 0; i <Engine.getRotorsAvailable().size(); i++) {
            Rotors+="Rotor ID:{"+Engine.getRotorsAvailable().get(i)+ "} ";
        }
        machineTabController.setPartTextArea(Rotors);
    }

    //This function will modify the text in the part conf text field as option 2 output
    @FXML
    void displayMachineConfiguration(MouseEvent event) {
        machineTabController.setPartTextArea(UICommunicationUnit.getEngineOutputMessage());
        machineTabController.setRandomConfLabelOpacity(0);
    }

    @FXML
    void displayPlugBoardConfiguration(MouseEvent event) {
        String plugPairs="";
        plugPairs+=Engine.getPlugboardSets();
        machineTabController.setRandomConfLabelOpacity(0);
        if(!plugPairs.equals(""))
            machineTabController.setPartTextArea(plugPairs);
        else machineTabController.setPartTextArea("No Plug Pairs Have Set!");
    }

    public void setFatherStage(Stage fatherStage) {
        this.fatherStage = fatherStage;
        fatherStage.widthProperty().addListener((observable, oldValue, newValue) ->{ //pair to functions
            if(fatherStage.getWidth()/4.465>=200) T1KeyBoardImage.setFitWidth(fatherStage.getWidth()/4.465);
            else T1KeyBoardImage.setFitWidth(200);
            if(fatherStage.getWidth()/8.6699>=103) T1MachineImage.setFitWidth(fatherStage.getWidth()/8.6699);
            else T1MachineImage.setFitWidth(103);
            if(fatherStage.getWidth()/8.11818>=110) T1RotorImage.setFitWidth(fatherStage.getWidth()/8.11818);
            else T1RotorImage.setFitWidth(110);
            if(fatherStage.getWidth()/7.63247>=117) T1ReflectorImage.setFitWidth(fatherStage.getWidth()/7.63247);
            else T1ReflectorImage.setFitWidth(117);
            if(fatherStage.getWidth()/5.28402>=169) T1PlugImage.setFitWidth(fatherStage.getWidth()/5.28402);
            else T1PlugImage.setFitWidth(169);

        } );
        fatherStage.heightProperty().addListener((observable, oldValue, newValue) ->{ //pair to function
            if(fatherStage.getHeight()/15>=50) T1KeyBoardImage.setFitHeight(fatherStage.getHeight()/15);
            else T1KeyBoardImage.setFitHeight(50);
            if(fatherStage.getHeight()/4.6875>=160)T1MachineImage.setFitHeight(fatherStage.getHeight()/4.6875);
            else T1MachineImage.setFitHeight(160);
            if(fatherStage.getHeight()/12.09677>=62) T1RotorImage.setFitHeight(fatherStage.getHeight()/12.09677);
            else T1RotorImage.setFitHeight(62);
            if(fatherStage.getHeight()/10.56338>=71)T1ReflectorImage.setFitHeight(fatherStage.getHeight()/10.56338);
            else T1ReflectorImage.setFitHeight(71);
            if(fatherStage.getHeight()/12.09677>=62) T1PlugImage.setFitHeight(fatherStage.getHeight()/12.09677);
            else T1PlugImage.setFitHeight(62);
        } );


    }



    //------------------images animations----------------------//

    //set images listeners


    //sets the animation for the images!
    private void  setAnimations(){
        this.imageAnimations = new HashMap<>();
        ScaleTransition scale1 = new ScaleTransition();
        ScaleTransition scale2 = new ScaleTransition();
        ScaleTransition scale3 = new ScaleTransition();
        ScaleTransition scale4 = new ScaleTransition();
        ScaleTransition scale5 = new ScaleTransition();
        scale1.setNode(T1KeyBoardImage);
        scale2.setNode(T1PlugImage);
        scale3.setNode(T1MachineImage);
        scale4.setNode(T1RotorImage);
        scale5.setNode(T1ReflectorImage);
        imageAnimations.put("Keyboard",scale1);
        imageAnimations.put("Plug board",scale2);
        imageAnimations.put("Machine",scale3);
        imageAnimations.put("Rotor",scale4);
        imageAnimations.put("Reflector",scale5);
        setImageAnimationProperties();
    }

    @FXML
    void startAnimation(MouseEvent event) {
        startAnimations(getActivatedNodeName(((ImageView)event.getSource()).getId()));
    }
    @FXML
    void stopAnimation(MouseEvent event){
        stopAnimation(getActivatedNodeName(((ImageView)event.getSource()).getId()));
    }
    private String getActivatedNodeName(String source){
        if(source.equals("T1MachineImage")) return "Machine";
        else if(source.equals("T1KeyBoardImage")) return "Keyboard";
        else if(source.equals("T1RotorImage")) return "Rotor";
        else if(source.equals("T1ReflectorImage")) return "Reflector";
        else if(source.equals("T1PlugImage")) return "Plug board";
        else return "";// will not get here
    }

    //sets animation effects to all og the images in the first tab
    private void setImageAnimationProperties(){
        Iterator it = this.imageAnimations.entrySet().iterator();
        while (it.hasNext()) {//iterates over the collection of hash map
            Map.Entry pair = (Map.Entry)it.next();
            ((ScaleTransition)pair.getValue()).setDuration(Duration.millis(650));
            ((ScaleTransition)pair.getValue()).setCycleCount(TranslateTransition.INDEFINITE);
            ((ScaleTransition)pair.getValue()).setInterpolator(Interpolator.LINEAR);
            ((ScaleTransition)pair.getValue()).setByX(0.25);
            ((ScaleTransition)pair.getValue()).setByY(0.25);
            ((ScaleTransition)pair.getValue()).setAutoReverse(true);
            //it.remove(); // avoids a ConcurrentModificationException
        }
    }

    //start animation
    private void startAnimations(String animationName){
        imageAnimations.get(animationName).play();
    }

    //stops animation
    private void stopAnimation(String animationName){
        imageAnimations.get(animationName).stop();
        toDefaultSize(animationName);

    }

    //help function to get images back to normal size!
    private void toDefaultSize(String fieldName){
        if(fieldName.equals("Machine")){
            T1MachineImage.setFitHeight(fatherStage.getHeight()/4.6875);T1MachineImage.setFitWidth(fatherStage.getWidth()/8.6699);
            T1MachineImage.setScaleX(1);T1MachineImage.setScaleY(1);
        }
        else if(fieldName.equals("Keyboard")){
            T1KeyBoardImage.setFitHeight(fatherStage.getHeight()/15);T1KeyBoardImage.setFitWidth(fatherStage.getWidth()/4.465);
            T1KeyBoardImage.setScaleX(1);T1KeyBoardImage.setScaleY(1);
        }
        else if(fieldName.equals("Rotor")){
            T1RotorImage.setFitHeight( fatherStage.getHeight()/12.09677);T1RotorImage.setFitWidth(fatherStage.getWidth()/8.11818);
            T1RotorImage.setScaleX(1);T1RotorImage.setScaleY(1);
        }
        else if(fieldName.equals("Reflector")){
            T1ReflectorImage.setFitHeight( fatherStage.getHeight()/10.56338);T1ReflectorImage.setFitWidth(fatherStage.getWidth()/7.63247);
            T1ReflectorImage.setScaleX(1);T1ReflectorImage.setScaleY(1);
        }
        else if(fieldName.equals("Plug board")){
            T1PlugImage.setFitHeight( fatherStage.getHeight()/12.09677);T1PlugImage.setFitWidth(fatherStage.getWidth()/5.28402);
            T1PlugImage.setScaleX(1);T1PlugImage.setScaleY(1);
        }
    }
}
