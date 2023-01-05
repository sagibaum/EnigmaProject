package Mission;

public class MissionPauseListener {

    private boolean isPaused;

    public MissionPauseListener() {
        this.isPaused = false;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}
