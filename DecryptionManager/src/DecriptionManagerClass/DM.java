package DecriptionManagerClass;

import EngineFunctionallity.EngineObject;
import Mission.MissionPauseListener;
import MissionCreator.MissionCreator;
import QueueListenerThread.QueueThread;
import customThread.PausableThreadPoolExecutor;
import customThread.ThreadFactoryBuilder;
import missions.results.MissionResults;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

public class DM  {
    private  MissionCreator missionsCreator;
    private MissionPauseListener isPaused;
    private Integer numOfAgents;//num of agents

    private BlockingQueue<MissionResults> resultsQueue;// queue of missions results

    private EngineObject engine; //this is used to create MDTU, that with the machine instances are created

    private String message;
    private MissionCreator.missionDifficulty missionDifficulty;
    private Integer missionSize;
    private PausableThreadPoolExecutor customThreadPool;
    private BlockingQueue<Runnable> missionsQueue; //use this blocking queue to prevent CPU overwork, if the queue is full wait then push

     private Consumer<MissionResults> resultsConsumer;
     private Consumer<Double> missionsProgressPercentageConsumer;

     private Thread queueThread,missionCreatorThread;

    public DM(Integer numOfAgents, Integer missionSize, EngineObject engine, MissionCreator.missionDifficulty missionDifficulty, Consumer<MissionResults> ResultsConsumer,
              Consumer<Double> missionsProgressPercentageConsumer, String message, MissionPauseListener isPaused) {
        this.numOfAgents = numOfAgents;
        this.engine = engine;
        this.message=message;
        this.missionSize=missionSize;
        this.isPaused = isPaused;
        this.missionDifficulty = missionDifficulty;
        this.missionsQueue = new ArrayBlockingQueue<Runnable>(1000);
        ThreadFactory customThreadFactory = new ThreadFactoryBuilder().setNamePrefix("Agent").build();//custom thread factory to set threads names
        customThreadPool= new PausableThreadPoolExecutor(numOfAgents,missionsQueue,customThreadFactory);
        this.resultsConsumer = ResultsConsumer;
        this.missionsProgressPercentageConsumer = missionsProgressPercentageConsumer;
        this.resultsQueue = new LinkedBlockingQueue<>();// send it to mission for threads know where to push the candidates
        this.queueThread = new Thread(new QueueThread(resultsQueue,resultsConsumer),"Queue Thread");//when we hit startProcess it will start listen to queue
      //  this.missionsCreator = new MissionCreator(missionDifficulty,missionSize,engine.getDecipher(),resultsQueue,missionsQueue,message,this.engine
              //  , missionsProgressPercentageConsumer, isPaused);
        this.missionCreatorThread = new Thread(missionsCreator);
    }


    public void startProcess(){
        customThreadPool.prestartAllCoreThreads();// start the thread pool instead of execute(), now to insert mission to the thread pool you need to do put in the mission queue!
        queueThread.start();//starts the mission results queue listener!
        missionCreatorThread.start();
    }

    public void killAllThreads(){ // maybe replace with SimpleBooleanProperty?
        queueThread.interrupt();//call it when the mission queue is empty + missionResults queue empty + all pool threads are in idle and the producer thread already
        customThreadPool.shutdownNow();
    }

    public void resumeAllThreads(){
        try {
            synchronized (this) {
                //missionsCreator.notifyAll();
                customThreadPool.resume();
                for (Runnable m : missionsQueue) {
                    m.notifyAll();
                }
            }
        } catch (IllegalMonitorStateException e) {
            //throw new RuntimeException(e);
        }
    }

    public void pauseAllThreads(){
        try {
            synchronized (this) {
                //missionsCreator.wait();
                customThreadPool.pause();
                for (Runnable m : missionsQueue) {
                    m.wait();
                }
            }
        } catch (InterruptedException | IllegalMonitorStateException e) {
            //throw new RuntimeException(e);
        }
    }

    public void killAllThreadsWhileExit() {
        queueThread.interrupt();
        missionCreatorThread.interrupt();
        customThreadPool.shutdownNow();;
    }

}
