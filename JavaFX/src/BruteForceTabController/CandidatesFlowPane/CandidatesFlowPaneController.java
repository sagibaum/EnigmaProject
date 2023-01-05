package BruteForceTabController.CandidatesFlowPane;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

public class CandidatesFlowPaneController {

    @FXML
    private FlowPane T3candidatesFlowPane;

    /*public void creatNewCandidateBlock(MissionResults mission){
        VBox candidateVBox = new VBox();
        Label candidate = new Label(mission.getCandidate());
        Label codeConf = new Label(mission.getCodeConfiguration());
        String time = "Time to encrypt: "+mission.getEncryptionTime()+" nano seconds";
        Label encryptTime = new Label(time);
        Label worker = new Label(mission.getThreadResponsible());
        candidateVBox.getChildren().addAll(candidate,codeConf,encryptTime,worker);
        candidateVBox.setAlignment(Pos.CENTER);
        candidateVBox.getStylesheets().add(String.valueOf((getClass().getResource("MissionResults.css"))));
        T3candidatesFlowPane.getChildren().add(candidateVBox);// push new vbox to flow pane
    }*/

    public void clearCandidatesFlowPane(){
        T3candidatesFlowPane.getChildren().clear();
    }
}
