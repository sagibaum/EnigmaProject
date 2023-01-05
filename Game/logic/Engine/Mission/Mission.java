package Mission;

import Decipher.Decipher;
import missions.results.MissionResults;
import missions.results.MissionsProgress;
import parts.Machine;
import user.data.transfer.unit.UserDataTransferUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;


public class Mission implements Runnable {
    // private  MissionsProgress progress;
    private Machine machine; //copy of the current machine to work on

    private  Integer missionSize;
    private Long missionCompleted;

    private Decipher decipher;

    private List<MissionResults> missionResultsList;//to report the result per mission

    private Consumer<List<MissionResults>> resultsConsumer;

    private MissionResults missionResults;
    // private BlockingQueue<Mission.Mission> taskQueue; //to get the blocking queue

    private List<String> startPosToInvestigate;
    private String message,AllyName;
    private Consumer<Long> missionCompletedConsumer;


    public Mission(Machine machine, List<String> startPosToInvestigate, Decipher decipher,
                   String message,String allyName) {
        this.machine = machine;//getting here new machine that created by DM.DM // will get here new machine!!
        this.startPosToInvestigate = startPosToInvestigate;//getting the mission size
        this.missionSize = startPosToInvestigate.size();
        this.message = message;
        this.AllyName=allyName;
        this.decipher = decipher;// saving decipher instance
        this.missionResultsList = new ArrayList<>();
        this.missionCompleted = 0L;
    }

    public void setResultsConsumer(Consumer<List<MissionResults>> resultsConsumer) { //use this to bring back all of the results list
        this.resultsConsumer = resultsConsumer;
    }

    @Override
    public void run() { //NOW WHEN A MISSION FINISHED HE ADDS
        //will run mission size times on combinations  for the initial starting pos of the machine rotors .
        //demands: we need to know the abc to run over and check whenever i got to the last letter
        String decryptMessage, currentMachineConf;
        long begin = System.nanoTime(), encryptionTime; //begin time and end of encrypt
        UserDataTransferUnit userDataTransferUnit = new UserDataTransferUnit();
        //System.out.println("starting mission: \n" + startPosToInvestigate + "\n");
        //thread.setName("Agent "+ thread.getId()); // getting agent id
        //System.out.println( thread.getName() +" started mission!");
        for (int i = 0; i < startPosToInvestigate.size(); i++) {
            initMachineRotorsToListStartPos(startPosToInvestigate.get(i));//sets the machine rotors to specified starting position
            userDataTransferUnit.setUserDataTransferUnitStrings(machine);//sets machine conf before encrypting
            currentMachineConf = userDataTransferUnit.getCurrentMachineConfiguration();//saves machine conf before encrypting
            decryptMessage = machine.encrypt(message);// decrypt
            //System.out.println(" message decrypted is : " + decryptMessage);
            if (checkBelongingToDictionary(decryptMessage)) {//check in-front of the dictionary if all the words of the decrypted message
                encryptionTime = System.nanoTime() - begin;
                creatMissionResultAndPushToQueue(decryptMessage, currentMachineConf, encryptionTime);//creat missions.results.MissionResults object with the input and push it into the mission result queue

                //System.out.println( thread.getName() +" pushing results to the queue");
            }
        }
        synchronized (this) {
            this.missionCompleted += 1;
            sendMissionResultsListToAgent(); //when finishing the mission send all mission results
            //back to the agent
            //then in agent add all the mission results of all missions and send to server!!
        }
    }

    private void sendMissionResultsListToAgent() {
        this.resultsConsumer.accept(this.missionResultsList);
        this.missionCompletedConsumer.accept(this.missionCompleted);
    }


    //sets the machine rotors to specified starting position
    private synchronized void initMachineRotorsToListStartPos(String startPos){
        machine.setMachineStartingPos(startPos);
    }

    //check in-front of the dictionary if all the words of the decrypted message
    private synchronized boolean checkBelongingToDictionary(String decryptMessage){
        // System.out.println( thread.getName() +" checking this decrypt result " + decryptMessage+"\n");
        StringTokenizer st = new StringTokenizer(decryptMessage," ");
        while (st.hasMoreTokens()) {
            String temp = st.nextToken();
            //System.out.println( thread.getName() +" checkig if the word: " + temp +" appears in dictionary");
            if(!decipher.getDictionary().getDictionaryWords().contains(temp)){
                //System.out.println( thread.getName() +" this word: " + temp +" dont appears in dictionary\n");
                return false;//if there is one word that isnt belong to dictionary , we will not generate result!
            }
        }
        return true;
    }

    //creat missions.results.MissionResults object with the input and push it into the mission result queue
    private synchronized void creatMissionResultAndPushToQueue(String decryptMessage ,String currentMachineConf, long encryptionTime){
        this.missionResults = new MissionResults(decryptMessage,currentMachineConf,encryptionTime,AllyName);
        this.missionResultsList.add(missionResults);
    }


    public void setProgressConsumer(Consumer<Long> progressConsumer) {
        this.missionCompletedConsumer = progressConsumer;
    }

    @Override
    public String toString() {
        return "Mission{" +
                "machine=" + machine +
                ", missionSize=" + missionSize +
                ", missionCompleted=" + missionCompleted +
                ", decipher=" + decipher +
                ", missionResultsList=" + missionResultsList +
                ", resultsConsumer=" + resultsConsumer +
                ", missionResults=" + missionResults +
                ", startPosToInvestigate=" + startPosToInvestigate +
                ", message='" + message + '\'' +
                ", AllyName='" + AllyName + '\'' +
                ", missionCompletedConsumer=" + missionCompletedConsumer +
                '}';
    }
}

