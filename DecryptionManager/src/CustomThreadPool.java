import Mission.Mission;

import java.util.concurrent.BlockingQueue;

 public class CustomThreadPool {//לא בשימוש בנתיים
    private BlockingQueue<Mission> taskQueue = null;
    //private List<Agent> runnables = new ArrayList<>();
    private boolean isStopped = false;

    private final Integer maxMissions =1000;

/*
    public void ThreadPool(int noOfThreads, int maxMissions, Decipher decipher){
        taskQueue = new ArrayBlockingQueue<Mission>(maxMissions);

        for(int i=0; i<noOfThreads; i++){// creates asked number of threads
            Agent poolThread = new Agent(i,taskQueue,decipher);
            runnables.add(poolThread);//adding threads to the pool
        }
        for(Agent runnable : runnables){// start all threads
            new Thread(runnable).start();
        }
    }

    public synchronized void  execute(Mission mission) throws Exception{
        if(this.isStopped) throw
                new IllegalStateException("ThreadPool is stopped");

        this.taskQueue.offer(mission);
    }

    public synchronized void stop(){
        this.isStopped = true;
        for(Agent runnable : runnables){
            //stop the thread
        }
    }*/

    public synchronized void waitUntilAllTasksFinished() {
        while(this.taskQueue.size() > 0) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

