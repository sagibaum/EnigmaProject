package Main.Client.ContestTabController.DictionarySearch;

import BruteForceTabController.BruteForceController;
import EngineFunctionallity.EngineObject;
import Main.Client.ContestTabController.UBoatContestController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.HashSet;
import java.util.Set;

public class DictionarySearchController {
    @FXML
    private TextField T3WordSearchTextField;

    private EngineObject Engine;

    private UBoatContestController uBoatContestController;

    public void setController(UBoatContestController UBoatContestController, EngineObject engine){
        this.uBoatContestController=UBoatContestController;
        this.Engine = engine;
    }


    @FXML
    void filterDictionaryWords(KeyEvent event) {
        if(T3WordSearchTextField.getText().equals("")){
            uBoatContestController.addSetToListView(Engine.getDecipher().getDictionary().getDictionaryWords());
        }else {
            uBoatContestController.clearListView();
            Set<String> resDict = new HashSet<>(),dictLowerCase = Engine.getDecipher().getDictionary().getDictionaryWords();;
            for (String curr : dictLowerCase) {
                if(curr.contains(T3WordSearchTextField.getText().toUpperCase())){
                    resDict.add(curr.toUpperCase());
                }
            }
            uBoatContestController.addSetToListView(resDict);
        }
    }

    public void setT3WordSearchTextFieldDisabled(boolean status){
        T3WordSearchTextField.setDisable(status);
    }


    public void clearSearchField(){
        T3WordSearchTextField.clear();
    }
}
