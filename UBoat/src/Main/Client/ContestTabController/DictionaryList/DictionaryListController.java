package Main.Client.ContestTabController.DictionaryList;

import Main.Client.ContestTabController.UBoatContestController;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import java.util.Set;

public class DictionaryListController {
    @FXML
    private ListView<String> T3DictionaryListView;

    private Set<String> DictionaryWordsSet;

    private UBoatContestController uBoatContestController ;

    public void setController(UBoatContestController UBoatContestController, Set<String> DictionaryWords){
        this.uBoatContestController=UBoatContestController;
        this.DictionaryWordsSet = DictionaryWords;
        this.T3DictionaryListView.getItems().addAll(DictionaryWordsSet);
    }

    @FXML
    void addWordToInput(MouseEvent event) {
        String selected = T3DictionaryListView.getSelectionModel().getSelectedItem().trim();
        if(selected != null) {
            if( uBoatContestController.CompareInputTextAreaField("") || uBoatContestController.CompareInputTextAreaField(null))
                uBoatContestController.setInputTextArea(selected);
            else  uBoatContestController.setInputTextArea(uBoatContestController.getInputTextArea() + " " + selected);
        }
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

    public void setDictionaryListViewDisable(boolean status){
        T3DictionaryListView.setDisable(status);
    }
}
