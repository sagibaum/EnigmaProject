# EnigmaProject
Enigma machine implementation, including automatic decipher process (using brute force).

Project characteristics: 
1) The machine implementation created base on (XML) file and is a generic one, not limited to a certain scale and have the ability to encrypt various letters.
2) The project architecture includes console, desktop and client-server apps (based on HTTP using tomcat platform).
3) The decryption/encryption process involved working with multiple threads, files, collections and other OOP concepts and principles
   (e.g. interfaces, encapsulation,inheritance etc ).
4) The project uses external Libraries like ok-HTTP, J-Son To support communication between the various applications and the server, by HTTP requests,
   and in combinatorics library for decryption process .
   
How run the game ? 
1) Download a tomcat server and java.
2) Download the "Game file" as a zip ' and then unzip it in selected location.
3) Implement the "Server.war" file in the tomcat server. 
4) Run one of the following files(from cmd or just double click) : 
  a) to play an ally -> press on the allies.bat file. 
  b) to play an uboat -> press on the uboat.bat file.
  c) to add agents -> press on the agent.bat file.
 
Project Modules explenation: 
1)machine module: 
   Description by packages ->
      a)"data.transfer.unit" package -> Contains the relevant classes for the transfer of information between the engine and the physical machine.
      b)"parts" package -> Contains the different parts of the physical machine. In addition Contain the Available class that represent a warehouse of parts that given          in a xml configuration file.  
      
2)"Engine" module: Contains the various packages needed to control all data validation and transmission. 
   Description by packages->
      •	Decipher – Data structure that holds the forbidden letters and dictionary.
      •	EngineFunctionality – The msin package that links the engine to a user interface.
      •	Exceptions – A package responsible for throwing expceptions on invalid data inputs.
      •	File – A package Divided into packs of a file checker class , which checks the integrity of the supplied input file, and file reader class which responsible            to read the xml input file.
      •	Randomizer – Responsible to supply a random configuration of a machine given.
      •	Statistics- Responsible for managing the statistics of the currently loaded machine.
      •	Mission - Contains the task class that runs by a thread (runnable class).
      •	MissionCreator - Contains the class that creates tasks and pushes them to the queue that is on the server.
      •	MissionResults - Contains a DTO that describes the results of a mission (The Agent clients will creat this DTO and send it to the server after each mission            they completed).
      •	StartingPosCalculator - Contains the class that responsible for calculates the initial configurations/rotors positions needed to pass by in order to                    decrypt(by brute-force) the message ,according to the level that the game is initialized on. afterwards each mission will get a range of starting positions            which the agents will pass by for trying decrypt the message.

3)"out.scheme" module: Contains the classes that the jaxbe proccess generated(according to Scheme.xml file given beforehand).

4)"Uboat" module: Responsible for the UBoat desktop app. The UBoat desktop app  is responsible for pulling information from the server, update the app and   send to the server relevant information such as who the winner is,contest progress etc. In addition, the application is divided into sub-parts and allows the user,     among other things, to configure a random or manual machine configuration.

5)"Agent" module: Responsible for the Agent desktop app. The Agent desktop app is responsible for the registration of an agent, pulling information from the server(allies available,competitions details,missions etc),update the server about missions completed and their results and update its desktop app.

6)"Allies" module: Responsible for the Allies desktop app. The Allies desktop app is responsible for the registration of an ally, pulling information from the server
(competitions available details, agents associated, message to decrypt etc), define how many starting locations for each task will be given to each agent and         updating the server about its progress. 

7)"Server" War module: Responsible for the server side app, relies on the various logics found in the "engine" module. In addition contains an instance of usermanager class that control information relevant to each client and updates it if necessary. The server is devided to servlets that handles http requests from clients(The servlets name gives a hint of their functioning).  


Game rules:
 
 
