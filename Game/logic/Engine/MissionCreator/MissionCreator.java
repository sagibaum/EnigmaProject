package MissionCreator;

import Decipher.Decipher;
import EngineFunctionallity.EngineObject;
import Mission.Mission;
import StartingPositionsCalculator.StartingPositionsCalculator;
import data.transfer.unit.unit.MachineDataTransferUnit;
import missions.results.MissionsProgress;
import parts.Machine;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.paukov.combinatorics3.Generator;

public class MissionCreator  implements Runnable{
    private final Integer missionSize;
    private final Decipher decipher;
    private final Consumer<MissionsProgress> missionsProgressConsumer;
    private List<String> startPosToInvestigate;
    private final BlockingQueue<Runnable> missionsQueue; //use this blocking queue to prevent CPU overwork, if the queue is full wait then push
    private final String message;
    private static missionDifficulty missionDifficulty;
    private MissionsProgress progress;
    private final EngineObject engine;
    private List<Integer> currentMachineRotorIdList;   //THESE ARE NEEDED FOR MACHINE IN DIFF DIFFICULTIES
    // private final Consumer<MissionsProgress> missionsProgressConsumer;
    private String usedReflector,AllyName;
    private long easyTotal;
    private long missionsAddedToQueue;

    public enum missionDifficulty {
        EASY,
        MEDIUM,
        HARD,
        IMPOSSIBLE,
        INVALID;
    }

    public MissionCreator(missionDifficulty missionDifficulty, Integer missionSize, Decipher decipher,
                          BlockingQueue<Runnable> missionsQueue, String message, EngineObject engine, String allyName,
                          Consumer<MissionsProgress> missionsProgressConsumer) {
        MissionCreator.missionDifficulty = missionDifficulty;
        this.engine = engine;
        this.missionSize = missionSize;
        this.missionsAddedToQueue = 0;
        this.AllyName=allyName;//added ally name
        this.decipher = decipher;
        this.easyTotal = calcTotalMissions(MissionCreator.missionDifficulty.EASY);
        this.missionsQueue = missionsQueue;
        this.message = message;
        this.missionsProgressConsumer = missionsProgressConsumer;
    }


    @Override
    public void run() {
        long total;
        getRotorIdList(engine.getUsedRotorsId()); //create list of used rotors
        switch (missionDifficulty){
            case EASY:

                total = calcTotalMissions(missionDifficulty.EASY);
                this.progress = new MissionsProgress(new AtomicLong(0), total);
                this.missionsProgressConsumer.accept(progress);
                this.missionsAddedToQueue = 0;
                difficultyOneLogic(engine.getUsedRotorsId(), engine.getReflectorID());
                break;
            case MEDIUM:

                total = calcTotalMissions(missionDifficulty.MEDIUM);
                this.progress = new MissionsProgress(new AtomicLong(0), total);
                this.missionsProgressConsumer.accept(progress);
                this.missionsAddedToQueue = 0;
                difficultyTwoLogic(engine.getUsedRotorsId());
                break;
            case HARD:

                total = calcTotalMissions(missionDifficulty.HARD);
                this.progress = new MissionsProgress(new AtomicLong(0), total);
                this.missionsProgressConsumer.accept(progress);
                this.missionsAddedToQueue = 0;
                difficultyThreeLogic(this.currentMachineRotorIdList);
                break;
            case IMPOSSIBLE:
                total = calcTotalMissions(missionDifficulty.IMPOSSIBLE);
                this.progress = new MissionsProgress(new AtomicLong(0), total);
                this.missionsProgressConsumer.accept(progress);
                this.missionsAddedToQueue = 0;
                difficultyImpossibleLogic();
                break;
        }
    }

    private synchronized long getMissionsTotalForInputDifficulty(missionDifficulty missionDifficulty){
        long total = 0;
        int langLen = engine.getAbc().length();
        int rotorsNumber = engine.getMachineRotorsNumConf();
        int rotorsTotal = engine.getRotorsAvailable().size();
        int reflectorsNum = engine.getReflectorsAvailable().size();
        switch (missionDifficulty){
            case EASY:
                total = (long)(Math.pow(langLen,rotorsNumber));
                break;
            case MEDIUM:
                total = (long)Math.pow(langLen,rotorsNumber)  *
                        reflectorsNum;
                break;
            case HARD:
                total = (long)(factorial(rotorsNumber) *
                        Math.pow(langLen,rotorsNumber)  *
                        reflectorsNum);
                break;
            case IMPOSSIBLE:
                /*     N!
                    --------
                    (N-K)!K!       */
                long nFactorial = factorial(rotorsTotal);
                long kFactorial = factorial(rotorsNumber);
                long nMinusKFactorial = factorial(rotorsTotal - rotorsNumber);
                long nCk = (nFactorial / (nMinusKFactorial * kFactorial));
                total = (long)(factorial(rotorsNumber) * nCk *
                        Math.pow(langLen,rotorsNumber)  * reflectorsNum);
                break;
        }
        return total;
    }

    private synchronized Long calcTotalMissions(missionDifficulty difficulty){
        long total = 0;
        long numOfTotal = getMissionsTotalForInputDifficulty(difficulty);
        long numOfEasyTotal = getMissionsTotalForInputDifficulty(missionDifficulty.EASY);
        long extraEdgeMissions = numOfTotal / numOfEasyTotal;
        if(numOfEasyTotal % missionSize != 0){
            total += extraEdgeMissions;
        }
        total += (numOfTotal - (extraEdgeMissions * (numOfEasyTotal % missionSize))) / missionSize;
        return total;
    }

    //missions will be generated by creating the different machines needed to decrpyt the message
    //the machine needs in order to be created to receive a CTEEnigma
    //and a user input, like : <45,27,94><AO!><III><A|Z,D|E>
    //in the DM part of the project can be assumed no plugs are used.

    private synchronized void  difficultyOneLogic(String rotorIds, String reflector){
        //in this difficulty everything is known about the machine except for the starting positions of the rotors
        //example of machine given: <3,7,4><CBA><V> machine is given from the engine!
        //basically the "Checker" knows the correct machine configuration except for the starting positions
        //so the logic here is creating Missions that will run across all of the starting positions by Mission Size
        //use rotors same as the machine right now
        this.usedReflector = reflector; //reflector is known

        //starting positions needed to be calc:
        String currentStartingPos = createFirstStartingPos(); //example for classic ABC: AAA (if there are 3 rotors)
        String finalStartingPos = createLastStartingPos();
        while(missionsAddedToQueue != easyTotal ){ //calcing all missions
            StartingPositionsCalculator calculator = new StartingPositionsCalculator(engine.getAbc(), engine, createLastStartingPos());
            startPosToInvestigate = calculator.calculateNextMissions(missionSize, currentStartingPos); //if from AAA to AAB returns AAC
            currentStartingPos = calculator.addOneToCurrentPositions(startPosToInvestigate.get(startPosToInvestigate.size() - 1));
            //this function just created the list of positions
            //all thats left is to create the mission:
            String userInput = userInputMaker(rotorIds, startPosToInvestigate.get(0), reflector);
            MachineDataTransferUnit mdtu = new MachineDataTransferUnit(engine.getXmlEnigma(),userInput);
            Machine machine=new Machine(mdtu);
            try {
               if (checkValidation(machine)){
                   missionsQueue.put(new Mission(machine, startPosToInvestigate, engine.getDecipher(), message,AllyName)); //gets paused when full
                missionsAddedToQueue++;
                this.progress.getMissionsCreated().incrementAndGet();
                this.missionsProgressConsumer.accept(progress);}

            } catch (Exception e) {
                //here waits for space to clear DO NOT DELETE
                //System.out.println(e);
            }
        } //this while NEEDS to calculate all the missions and divide them, AND PUSH INTO QUEUE IN OTHER PLACE
        //out of for finished pushing all missions into queue.
    }


    private synchronized void difficultyTwoLogic(String rotorIds){
        //This is like difficulty one except need to run across the reflectors as well
        List<String> reflectors= engine.getReflectorsAvailable();
        for (int i = 0; i < reflectors.size(); i++) {
            difficultyOneLogic(rotorIds, reflectors.get(i));
            missionsAddedToQueue = 0;
        }
    }
private boolean checkValidation(Machine machine){
    if (missionsQueue==null ||machine==null||startPosToInvestigate==null|| engine==null
            ||message==null||AllyName==null)
        return false;
    else return true;
    }
    private synchronized void difficultyThreeLogic(List<Integer> rotors) {
        //this is like difficulty two except that you also need to switch all of the rotors around
        //basically need to run across all of the rotors possible positions and then use difficulty two for each iteration
        //Here i need a data structure to help me know (Set) what order of rotors i already went through/
        //using a math calc i can know what all of the possibilities for the different orders of the rotors
        // Set<String> left = this.engine.getRotorIdPermutations(); //getting all permutations left     //example: 1,2,3
        Generator.permutation(rotors).simple()
                .stream()
                .forEach(e -> {
                    String perm = e.toString().trim().replace("]","").replace("[","")
                            .replace(" ","");
                    difficultyTwoLogic(perm);
                });
    }

    private synchronized void difficultyImpossibleLogic(){
        List<Integer> allRotorsList = engine.getRotorsAvailable();
        Generator.combination(allRotorsList)
                .simple(engine.getMachineRotorsNumConf())
                .stream()
                .forEach(combination -> Generator.permutation(combination)
                        .simple()
                        .forEach(list -> {
                            //System.out.println(list.toString());
                            String perm = list.toString().trim().replace("]","").replace("[","")
                                    .replace(" ","");
                            difficultyTwoLogic(perm);
                        }));
    }



    private synchronized String createFirstStartingPos() {
        String res = "";
        for (int i = 0; i < this.currentMachineRotorIdList.size(); i++) { //concats the first letter of alphabet in the amount of rotors in machine
            res += engine.getFirstLetter();
        }
        return res;
    }

    private synchronized String createLastStartingPos() {
        String res = "";
        for (int i = 0; i < this.currentMachineRotorIdList.size(); i++) { //concats the first letter of alphabet in the amount of rotors in machine
            res += (engine.getLastLetter());
        }
        return res;
    }

    public synchronized void getRotorIdList(String Rotors) {
        //example of user input: <45,27,94><AO!><III><A|Z,D|E>
        //NOTE - THIS FUNCTION SHOULD BE CALLED UPON ONLY IF THE INPUT IS VALID
        //meaning this function assumes that the rotor part of the input is OK
        StringTokenizer rotorsToken = new StringTokenizer(Rotors, ",");
        this.currentMachineRotorIdList = new ArrayList<>();
        while(rotorsToken.hasMoreTokens()) { //file has been checked so doesnt matter
            this.currentMachineRotorIdList.add(Integer.valueOf(rotorsToken.nextToken()));
        }
    }

    private synchronized int factorial(int n)
    {
        int res = 1, i;
        for (i=2; i<=n; i++)
            res *= i;
        return res;
    }

    private String userInputMaker(String rotors, String startingPos,String Reflector){
        return "<" + rotors + "><" + startingPos + "><" + Reflector + "><>";
    } //<3,7,4><CBA><V><>

}
