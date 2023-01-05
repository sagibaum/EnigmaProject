package BruteForceTabController.DictionarySearchHbox;

import BruteForceTabController.BruteForceController;
import EngineFunctionallity.EngineObject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DictionaryHboxController
{

    @FXML
    private TextField T3WordSearchTextField;

    private EngineObject Engine;
    @FXML
    private Button SelectWordButtonT3;

    private BruteForceController bruteForceController;
/*
    public void setController(BruteForceController bruteForceController, EngineObject engine){
        this.bruteForceController=bruteForceController;
        this.Engine = engine;
        setSelectWordButtonT3Disabled(false);
    }

    @FXML
    void addWordToInput(MouseEvent event) {
        String selected = bruteForceController.getWordSelectedFromListView();
        if(selected != null) {
            if( bruteForceController.CompareInputTextAreaField("") || bruteForceController.CompareInputTextAreaField(null))
                bruteForceController.setInputTextArea(selected);
            else  bruteForceController.setInputTextArea(bruteForceController.getInputTextArea() + " " + selected);
        }
    }

    @FXML
    void filterDictionaryWords(KeyEvent event) {
        if(T3WordSearchTextField.getText().equals("")){
           bruteForceController.addSetToListView(Engine.getDecipher().getDictionary().getDictionaryWords());
        }else {
            bruteForceController.clearListView();
            //Set<String> dictLowerCase = Engine.getDecipher().getDictionary().getDictionaryWords();
            Set<String> resDict = new HashSet<>(),dictLowerCase = Engine.getDecipher().getDictionary().getDictionaryWords();;
            for (String curr : dictLowerCase) {
                if(curr.contains(T3WordSearchTextField.getText().toUpperCase())){
                    resDict.add(curr.toUpperCase());
                }
            }
            bruteForceController.addSetToListView(resDict);
        }
    }

    public void setT3WordSearchTextFieldDisabled(boolean status){
        T3WordSearchTextField.setDisable(status);
    }
    public void setSelectWordButtonT3Disabled(boolean status){
        SelectWordButtonT3.setDisable(status);
    }*/
}
