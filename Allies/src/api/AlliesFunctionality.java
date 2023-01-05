package api;

import DataBase.Agent;
import ServerDTOs.Allies.ContestsDTO;

import java.util.List;

public interface AlliesFunctionality {

    //IMPORTANT NOTE: when the application pulls information needed from the server
    //it pulls ALL OF THE RELEVANT INFO FROM THE SERVER to update all parts of the desktop allies app
    //meaning - for example it pulls the agent data and the competition data together, NOT SEPERATE.


    //this function pulls the information from the relevant agents which have registered
    //to this specific Allies team! the agents number is dynamic, and is unlimited
    //the information showed about each agent is: Agent name, Agent Thread Amount, Mission size.
    void pullAndShowAvailableAgentsReadyToWork(List<Agent> agents);

    //this function pulls the relevant information from the server about ALL existing contests available in the background
    //NOTE: save information about contests on the server side like boolean isRunning?
    //then through a DTO which is updated when a pull is requsted send all the relevant info that needs to appear
    // on screen in the desktop application
    //in the Contest Data part the info needed is: Battlefield Name, UBoat User name (which uploaded the competition), game status(running/standby)
    //Difficulty level of the competition, number of allies NEEDED to run the competition, numbers of allies which actually exist (ex: 2/3)
    void pullAndShowAllContestsData(List<ContestsDTO> contests);

    //in this function using the pulled information about existing competitions
    //the user picks a competition and chooses the 'Ready' button, and then informs the server about it being ready
    //NOTE:if the competition is full show relevant ERROR message to user, and announce that this competition is FULL.
    boolean checkContestErrors();

    //this function PULLS information from the server about the candidates of the running competition
    //and updates the candidates on-screen in the desktop application Allies for the user to see
    //NOTE: need to add to mission results the allies (team) that the candidate originates from
    //NOTE: this part is the EXACT same part as the part used in the UBoat application (meaning they both use the same controller)
    //      but the data this time about the competition's candidates is pulled from the server (use scroller)
    void updateCandidatesPart(List<Agent> teamsAgents);

    void LogOutFromServer();
}

