package missions.results;

public class ProgressBarListener {
    private Integer TotalMissions;
    private Integer MissionsCompleted;

    public Integer getTotalMissions() {
        return TotalMissions;
    }

    public void setTotalMissions(Integer totalMissions) {
        TotalMissions = totalMissions;
    }

    public Integer getMissionsCompleted() {
        return MissionsCompleted;
    }

    public void setMissionsCompleted(Integer missionsCompleted) {
        MissionsCompleted = missionsCompleted;
    }
}
