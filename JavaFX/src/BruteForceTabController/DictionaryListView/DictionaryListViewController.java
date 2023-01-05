package BruteForceTabController.DictionaryListView;

import BruteForceTabController.BruteForceController;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.Set;

public class DictionaryListViewController {
    @FXML
    private ListView<String> T3DictionaryListView;

    private Set<String> DictionaryWordsSet;

    private BruteForceController bruteForceController;

    public void setController(BruteForceController bruteForceController, Set<String> DictionaryWords){
        this.bruteForceController=bruteForceController;
        this.DictionaryWordsSet = DictionaryWords;
        this.T3DictionaryListView.getItems().addAll(DictionaryWordsSet);
    }

    @FXML
    void addWordToInput(MouseEvent event) {
      /*  String selected = T3DictionaryListView.getSelectionModel().getSelectedItem().trim();
        if(selected != null) {
            if( bruteForceController.CompareInputTextAreaField("") || bruteForceController.CompareInputTextAreaField(null))
                 bruteForceController.setInputTextArea(selected);
            else  bruteForceController.setInputTextArea(bruteForceController.getInputTextArea() + " " + selected);
        }*/
    }

    public void setT3DictionaryListView(Set<String> dictionaryWordsSet) {
        T3DictionaryListView.getItems().addAll(dictionaryWordsSet);
    }

    public void clearT3DictionaryListView(){
        T3DictionaryListView.getItems().clear();
    }

    public String getWordSelectedFromList(){
        return T3DictionaryListView.getSelectionModel().getSelectedItem().trim();
    }
}
