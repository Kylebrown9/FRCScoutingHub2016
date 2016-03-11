package org.ncfrcteams.frcscoutinghub2016.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle Brown on 2/26/2016.
 */
public class Database implements Serializable {
    private int ID;

    /**
     * Contains the most recent copy of every MatchNumber,TeamNumber,Scouter record
     */
    private List<SuperMap> superMapList = new ArrayList<>();


    public Database(int ID) {
        this.ID = ID;
    }

    /**
     * Adds the the SuperMap to the superMapList, if the superMap has an equivalent
     * (defined by the SuperMap.equals() method) SuperMap already on the list it will
     * remove the out of date version and add itself in its place.
     *
     * @param superMap the match record being added
     */
    public void addMatchRecord(SuperMap superMap) {
        superMapList.remove(superMap);
        superMapList.add(superMap);
    }

    public List<SuperMap> getSuperMapList() {
        return superMapList;
    }

    /**
     * Compiles a list of all of the MatchRecords that have a matching TeamNumber
     *
     * @param team the team you are checking
     * @return a list of every match the team participated in
     */
    public List<SuperMap> getMatchRecordForTeam(int team) {
        List<SuperMap> teamSuperMaps = new ArrayList<>();

        for (SuperMap record : superMapList) {
            if(record.getTeamNum() == team) {
                teamSuperMaps.add(record);
            }
        }

        return teamSuperMaps;
    }

    public int getID() {
        return ID;
    }
}
