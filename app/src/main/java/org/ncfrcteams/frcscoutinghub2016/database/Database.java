package org.ncfrcteams.frcscoutinghub2016.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2/26/2016.
 */
public class Database {
    /**
     * Contains the most recent copy of every MatchNumber,TeamNumber,Scouter record
     */
    private List<MatchRecord> matchRecordList = new ArrayList<>();

    /**
     * Adds the the MatchRecord to the matchRecordList, if the matchRecord has an equivalent
     * (defined by the MatchRecord.equals() method) MatchRecord already on the list it will
     * remove the out of date version and add itself in its place.
     *
     * @param matchRecord
     */
    public void addMatchRecord(MatchRecord matchRecord) {
        matchRecordList.remove(matchRecord);
        matchRecordList.add(matchRecord);
    }

    /**
     * Compiles a list of all of the MatchRecords that have a matching TeamNumber
     *
     * @param team the team you are checking
     * @return a list of every match the team participated in
     */
    public List<MatchRecord> getMatchRecordForTeam(int team) {
        List<MatchRecord> teamMatchRecords = new ArrayList<>();

        for (MatchRecord record : matchRecordList) {
            if(record.TEAM_NUM == team) {
                teamMatchRecords.add(record);
            }
        }

        return teamMatchRecords;
    }
}
