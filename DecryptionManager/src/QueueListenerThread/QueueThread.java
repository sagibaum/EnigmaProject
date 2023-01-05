package QueueListenerThread;

import missions.results.MissionResults;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

public class QueueThread implements Runnable{

    private BlockingQueue<MissionResults> resultsQueue;// queue of missions results

    private Consumer<MissionResults> resultsConsumer;

    public QueueThread(BlockingQueue<MissionResults> resultsQueue, Consumer<MissionResults> resultsConsumer) {
        this.resultsQueue = resultsQueue;
        this.resultsConsumer = resultsConsumer;
    }

    @Override
    public void run() {
    //try to get missions results from queue until interrupt! , also blocking for saving cpu
        MissionResults m = null;
        do {
            try {
                m = resultsQueue.take();
                resultsConsumer.accept(m);
                //System.out.println("queue thread pulled the mission!");

            } catch (InterruptedException e) {
               // System.out.println("queue thread terminated");
            }
        }while (true); //do here while flag!!!/ maybe replace with SimpleBooleanProperty?
    }

}
