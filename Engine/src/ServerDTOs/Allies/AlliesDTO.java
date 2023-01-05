package ServerDTOs.Allies;

import DataBase.Agent;

import java.util.Collections;
import java.util.List;

public class AlliesDTO {
    //this class represents the data that will be sent to an allies desktop app
    //when it pulls for data
    List<Agent> thisTeamsAgents;
    List<ContestsDTO> uboatsAvailable;

    boolean UBoatAbandon;

    public AlliesDTO(List<Agent> thisTeamsAgents, List<ContestsDTO> uboatsAvailable,boolean abandon) {
        this.thisTeamsAgents = thisTeamsAgents;
        this.uboatsAvailable = uboatsAvailable;
        this.UBoatAbandon=abandon;

    }

    public List<Agent> getTeamsAgents() {
        return Collections.unmodifiableList(thisTeamsAgents);
    }

    public List<ContestsDTO> getUboatsAvailable() {
        return Collections.unmodifiableList(uboatsAvailable);
    }

    public boolean isAgentsEmpty(){
        return thisTeamsAgents.isEmpty();
    }

    public boolean isContestsEmpty(){
        return this.uboatsAvailable.isEmpty();
    }

    @Override
    public String toString() {
        return "AlliesDTO{" +
                "thisTeamsAgents=" + thisTeamsAgents +
                ", uboatsAvailable=" + uboatsAvailable +
                '}';
    }

    public boolean isUBoatAbandon() {
        return UBoatAbandon;
    }

    public void setUBoatAbandon(boolean UBoatAbandon) {
        this.UBoatAbandon = UBoatAbandon;
    }
}