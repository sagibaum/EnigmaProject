package EncryptDecryptTabController.LightBoard;

import EncryptDecryptTabController.EncryptDecryptTabController;
import EngineFunctionallity.EngineObject;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LightBoardController {
    @FXML
    private ScrollPane T2bulbBoardScrollPane;
    private FlowPane T2bulbFlowPane;
    private Map<String,Button> bulbs;
    private HashMap<Button, FadeTransition> BulbAnimations;
    private EngineObject Engine;

    public void setBulbBoardFunctionality(){
        setBoardsCharacteristics(T2bulbFlowPane);
        setKBulbBoardKeys(Engine.getAbc());
    }

    public void setBulbBoardController(EngineObject engine  , EncryptDecryptTabController encryptDecryptTabController){
        T2bulbFlowPane = new FlowPane();
        this.Engine=engine;


    }

    //bulb board bulbs setter
    private void setKBulbBoardKeys(String ABC) {
        int radius = 30;
        bulbs = new HashMap<>();
        BulbAnimations = new HashMap<Button, FadeTransition>();
        for (int j = 0; j < ABC.length(); j++) {
            Button Letter = new Button();
            Letter.setText(String.valueOf(ABC.charAt(j)));
            Letter.setShape(new Circle(radius));// make the button a circle!
            Letter.setMinSize(1.2 * radius, 1.2 * radius);
            Letter.setMaxSize(1.2 * radius, 1.2 * radius);
            Letter.setStyle("-fx-background-color: grey; -fx-text-fill: black");
            //animation
            FadeTransition fade = new FadeTransition();
            fade.setNode(Letter);
            BulbAnimations.put(Letter,fade);
            //----button functionality match-----//
            //Letter.setDisable(true);
            T2bulbFlowPane.getChildren().add(Letter);
            //adding to map the buttons by letter for animations
            bulbs.put(String.valueOf(ABC.charAt(j)),Letter);
        }

        T2bulbBoardScrollPane.setContent(T2bulbFlowPane);
        setImageAnimationProperties();
    }

    public Button getBulb(String Encrypt){
        return bulbs.get(Encrypt);
    }

    public void makeGlowingAnimation(Button L){
        L.setStyle("-fx-background-color: yellow; -fx-text-fill: black;");
        L.setDisable(true);
        BulbAnimations.get(L).play();
    }

    public void stopGlowingAnimation(Button L){
        L.setStyle("-fx-background-color: grey; -fx-text-fill: black");
        BulbAnimations.get(L).stop();
        L.setOpacity(1);
        L.setDisable(true);
    }


    private void setImageAnimationProperties(){
        Iterator it = this.BulbAnimations.entrySet().iterator();
        while (it.hasNext()) {//iterates over the collection of hash map
            Map.Entry pair = (Map.Entry)it.next();
            ((FadeTransition)pair.getValue()).setDuration(Duration.millis(650));
            ((FadeTransition)pair.getValue()).setCycleCount(TranslateTransition.INDEFINITE);
            ((FadeTransition)pair.getValue()).setInterpolator(Interpolator.LINEAR);
            ((FadeTransition)pair.getValue()).setFromValue(0);
            ((FadeTransition)pair.getValue()).setToValue(1);
            ((FadeTransition)pair.getValue()).setAutoReverse(true);

        }
    }

    private void setBoardsCharacteristics(FlowPane flowPane) {
        flowPane.setVgap(20);
        flowPane.setHgap(20);
        flowPane.setColumnHalignment(HPos.CENTER);
        flowPane.setRowValignment(VPos.CENTER);
        flowPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        flowPane.setAlignment(Pos.CENTER);

    }

}
