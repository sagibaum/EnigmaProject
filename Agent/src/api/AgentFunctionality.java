package api;

public interface AgentFunctionality {

    //The entire point of this class is to be the Worker that works on missions, and display the relevant data on-screen
    //and transfer data to the server

    //this function calls to all pull functions here, and updates all of the application
    void pullAllDataNeededFromServerForThisApp();

    //this functionality is the one responsible for the login screen
    //when a new Allies entity (the application is opened) is created
    //the login screen shows up, it allows the user to choose a username
    //once logged in the information is send to the server, and he saves the username and session ID
    //all the information that is needed to preform the acions (DTO)..
    void login();

    //this function pulls data from the server and shows on-screen in the 'Contest and team data'
    //part of the app the available allies teams this agent can choose to join to
    void pullAndShowAvailableAlliesTeams();

    //THE FOLLOWING IS DONE IN THE LOGIN SCREEN (the agent needs a different login screen from UBoat / Allies)
    //this function is responsible for the 'Choose' button functionality, once an allies team has been chosen
    //this data is sent to the server which updates information on his side,
    //and when allies app pulls this inforamation it updates the team data on his side that an agent has joined
    //the data needed (taken from the application user interface) to set up an Agent is as follows:
    // - A unique name (if this name exists server returns an error)
    // - Allies Team name this agent wants to join (chooses from teams that are pulled from the server side)
    // - Number of threads this agents uses to work on missions (positive number from 1 to 4, scroller?)
    // - Number (Natural) of mission size that this Agent pulls from the mission queue on the server side every time this agent is available
    //NOTE: agent can join an Allies Team before the Allies team has chosen a Competition/Battlefield to participate in
    //NOTE: once an Agent has chosen an Allies team it is FOREVER linked to that specific team, meaning after a competition is over
    //      the agent DOES NOT regain the ability to choose a team, its stuck with the same team it was initialized with
    //AFTER this function is finished and the user chose 'Ready' this part of the application is LOCKED
    //the Agent app is passive, after the register part it has no abilities AT ALL (except for bonus), just shows data on screen
    void registerAgent();

    //this function checks if the Allies team it is a part of is in the process of a running competition\Battlefield
    //if so, it starts pulling missions from the missions queue on the server side,
    //and gives the missions into the ThreadPool (with the size of threads given in register process)
    //the amount of missions an Agent pulls from the server is the amount given in the register process
    //this function is called whenever an agent has finished preforming all the missions he was given
    //then the agent is free to pull more missions from the server (until there are no more missions / competition is over)
    //need to understand how the queue is empty on the server side without BLOCKING (watch ex3 in this word part to understand more)
    void pullMissionsFromServer();

    //for every mission completed the Agent saves all of the mission results of that specific mission
    //and once the mission is completed this function is called to then send these candidates(mission results)
    //to the server, which then when applications pull data from the server, they update on their applications
    //the candidates in the competition so far (by teams)
    //NOTE: basically, there is a variable in the serverside for each Allies that saves the candidates so far in the running competition
    //      that variable is updated when this function calls to the relevant server which receives the http request sent from this function
    void sendCandidatesListToServer();

    //in the 'Contest and team data' part of this app the following data shows:
    // - the name of the allies team this agent is a part of
    // - the data of the competition this Agent is a part of ( if exists else 'Idle' )
    //NOTE: this information is pulled from the server
    void pullAndShowData();

    //Data about: (this data shows up in 'Agent progress & status' part of the app)
    // 1.Number of the internal mission queue this Agent has left in the queue
    // 2.Number of the Missions pulled from the server
    // 3.Sum of missions completed by this agent
    // 4.Sum of candidates found by this agent
    void showInnerQueueProgress();

    //Data about: (this data shows up in 'Agent’s Candidates' part of the app)
    //show ALL of the candidates that were found by this agent ALONE
    //for each candidate the data shown in the 'candidate box' is:
    // 1. The mission that this candidate originates from (Machine configuration)
    // 2. The mission number of that this candidate originates from
    //NOTE: THIS means that on the server side there is a mission sent counter
    //      and for every mission sent the mission is also numbered!
    void showAgentCandidates();

    //once the DTO that is pulled from the server shows that the competition this agent is a part of has FINISHED
    //this agent has to STOP all of its work, if this agent still has threads running on missions they can finish the missions
    //but DO NOT GIVE THEM NEW MISSIONS, stop pulling from server as well.
    //when a competition is over a relevant message is shown in the ALLIES application
    //when the user finished the competition (clicks 'OK' on the popup/alert --> send to server that its okay to reset agents)
    //then the data shown in the Agent application will be DELETED, until they receive new information from the server
    //about the new competition they are going to take part of (until such competition is selected in allies the DTO that the agent pulls
    //from the server tells the agent the contest is 'Idle')
    //NOTE: after the competition is done the Agent is still LINKED TO THE SAME ALLIES TEAM
    //      an agent can choose a team ONLY in the login screen
    void stopAgent();

}
