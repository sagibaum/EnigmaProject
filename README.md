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
1) machine module: 
   a)"data.transfer.unit" package -> Contains the relevant classes for the transfer of information between the engine and the physical machine.
   b)"parts" package -> Contains the different parts of the physical machine. In addition Contain the Available class that represent a warehouse of parts that given in       a xml configuration file.  
   
2)out.scheme module: Contains the classes that the jaxbe proccess generated(according to Scheme.xml file given beforehand).

3)Uboat module: Responsible for the UBoat desktop app. The UBoat desktop app  is responsible for pulling information from the server, update the app and   send to the server relevant information such as who the winner is,contest progress etc. In addition, the application is divided into sub-parts and allows the user,     among other things, to configure a random or manual machine configuration.

4)Agent module: Responsible for the Agent desktop app. The Agent desktop app is responsible for the registration of an agent, pulling information from the server(allies available,competitions details,missions etc),update the server about missions completed and their results and update its desktop app.

5)Allies module: Responsible for the Allies desktop app. The Allies desktop app is responsible for the registration of an ally, pulling information from the server
(competitions available details, agents associated, message to decrypt etc) , define how many starting locations for each task will be given to each agent and         updating the server about its progress. 

6)Server War module:


 
 
 Game rules:
 
