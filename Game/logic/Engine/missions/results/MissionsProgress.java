package missions.results;

import java.util.concurrent.atomic.AtomicLong;

public class MissionsProgress {
    private AtomicLong missionsCreated; //missions that were created
    private long missionsTotal; //missions total

    private long missionsCompleted; //missions finished


    public MissionsProgress(AtomicLong missionsCompleted, long missionsTotal) {
        this.missionsCreated = missionsCompleted;
        this.missionsTotal = missionsTotal;
        this.missionsCompleted = 0;
    }


    public synchronized AtomicLong getMissionsCreated() {
        return missionsCreated;
    }

    public synchronized long getMissionsTotal() {
        return missionsTotal;
    }

    public synchronized void setMissionsTotal(long missionsTotal) {
        this.missionsTotal = missionsTotal;
    }

    public synchronized void setMissionsCreated(AtomicLong missionsCreated) {
        this.missionsCreated = missionsCreated;
    }

    public synchronized void increment(){
        this.missionsCreated.incrementAndGet();
    }

    public synchronized Double getCompletedAsDouble(){
        return this.missionsCreated.doubleValue();
    }

    public long getMissionsCompleted() {
        return missionsCompleted;
    }

    public void setMissionsCompleted(long missionsCompleted) {
        this.missionsCompleted = missionsCompleted;
    }
}
