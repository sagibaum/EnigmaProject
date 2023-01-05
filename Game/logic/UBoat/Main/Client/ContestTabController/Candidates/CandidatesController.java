package Main.Client.ContestTabController.Candidates;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import missions.results.MissionResults;

public class CandidatesController {
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
/*
    public void clearCandidatesFlowPane(){
        T3candidatesFlowPane.getChildren().clear();
    }*/
}
