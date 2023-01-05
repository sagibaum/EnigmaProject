package api;

import ServerDTOs.Uboat.TableDataObject;
import missions.results.MissionResults;

import java.util.ArrayList;

public interface UBoatFunctionality {

    //this functionality is the one responsible for the login screen
    //when a new UBoat entity (the application is opened) is created
    //the login screen shows up, it allows the user to choose a username
    //once logged in the information is send to the server, and he saves the username and session ID
    //what ever is needed to communicate back and forth.
    // void login(); -> no need caus there is a login page that start this process

    //this function calls to all pull functions here, and updates all of the application
    void AnnounceWinner(String allyName);

    //user chooses XML file from his computer to upload to the server to indicate a specific competition
    //NOTE: cant upload the same xml file (which indicates a competition) more than once
    //File is being sent to the server, and then the file's validity is checked on the server side!
    //if something is wrong with the xml input file, the server returns the relevant error message (in http back)
    //with relevant error code.
    void uploadXMLFileToServer(String filePath);

    //this function sends a RANDOMIZED "user input" for example: <2,3,1><ABC><III><> (no plugboard allowed)
    //to the server with the compatible username that was given in login screen
    //once competition has started this is NOT available
    //void randomizeMachineConfiguration();

    //this function sends a CUSTOM "user input" for example: <2,3,1><ABC><III><> (no plugboard allowed)
    //to the server with the compatible username that was given in login screen
    // void manualMachineConfiguration();

    //this function is the encryption message that will be competed on in the contest
    //like in part 2, the message MUST contain ONLY words from the dictionary
    //this function will send all competition data that necessary to start a contest in server
    // + will call the contest controller to set disable all the unnecessary components
    void setMessageOfTheCompetition(ArrayList<String> machineDetails);

    //this function informs the server that the competition is ready to start
    //competition can start only when all allies and uboat is ready
    void sendServerReadyNotice(boolean ready);

    //this functionality needs to PULL relevant information about the teams (and the teammates inside of the teams)
    //from the server and update the active team details in the 'competition' tab
    //the information needed : team name, agent amount, mission size.
    void showActiveTeamDetailsInCompetition(ArrayList<TableDataObject> alliesInTable);

    //this function (via a button) needs to allow the user to logout of the competition (or from the app)
    //this button can ONLY be clicked when a competition is NOT in progress!
    //the relevant Button appears ONLY after a competition has finished.
    //when this is clicked HTTP message is sent to server and from there to all relevant applications
    //which are connected to this competition to RESET, and they need to choose a new competition.
    void logout();

    //this function PULLS information from the server about the candidates of the running competition
    //and updates the candidates on-screen in the desktop application UBoat for the user to see
    //NOTE: need to add to mission results the allies (team) that the candidate originates from
    void updateCandidatesPart(ArrayList<MissionResults> candidates);

}
