package ServerDTOs.Uboat;

import DataBase.Allies;
import missions.results.MissionResults;

import java.util.ArrayList;

public class UBoatDTO {
    ArrayList<TableDataObject> AlliesInTable;
    //add also the candidates array list + add an if condition to "updateAlliesAssociatedTable"

    Integer AlliesJoined;
    Boolean running;
    ArrayList<MissionResults> Candidates;
    public UBoatDTO(Boolean contestRunning) {
        AlliesInTable = new ArrayList<TableDataObject>();
        Candidates = new ArrayList<MissionResults>();
        running = contestRunning;

    }

    public void addAllyToDTO(Allies ally) {
        TableDataObject row = new TableDataObject(ally.getAllyName(),String.valueOf(ally.getAgentsNum()),
                String.valueOf(ally.getMissionSize()),ally.getReadyStatus());
      //  System.out.println("name: "+ally.getAllyName() +" Agent: "+ String.valueOf(ally.getAgentsNum())+
          //      "mission: "+ String.valueOf(ally.getMissionSize())+"ready: "+ally.getReadyStatus());
        AlliesInTable.add(row);

    }

    public ArrayList<TableDataObject> getAlliesInTable() {
        return AlliesInTable;
    }

    public void removeAllyFromDTO(Allies ally) {
        Integer index=0 ;
        for (TableDataObject object: AlliesInTable){
            index++;
            if(object.allyName.equals(ally.getAllyName()))
                break;}
        AlliesInTable.remove(index);
    }

    public Integer getAlliesJoined() {
        return AlliesJoined;
    }

    public void setAlliesJoined(Integer alliesJoined) {
        AlliesJoined = alliesJoined;
    }

    public boolean isAlliesAssociateTabledEmpty(){
        return this.AlliesInTable.isEmpty();
    }


    public void resetAlliesAssociated() {
        AlliesInTable.clear();
    }

    public void resetCandidatesAssociated() {
        Candidates.clear();
    }

    public void addToCandidates(ArrayList<MissionResults> allAllyCandidates) {
        Candidates.addAll(allAllyCandidates);
    }

    public boolean inContest() {
        return running;
    }

    public ArrayList<MissionResults> getCandidates() {
        return Candidates;
    }

    public void setRunning(Boolean running) {
        this.running = running;
    }

    @Override
    public String toString() {
        return "UBoatDTO{" +
                "AlliesInTable=" + AlliesInTable +
                ", AlliesJoined=" + AlliesJoined +
                ", running=" + running +
                ", Candidates=" + Candidates +
                '}';
    }
}
