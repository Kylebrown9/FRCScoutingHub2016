package org.ncfrcteams.frcscoutinghub2016.network.server;

import org.ncfrcteams.frcscoutinghub2016.database.Database;
import org.ncfrcteams.frcscoutinghub2016.database.MatchRecord;

import java.util.List;

/**
 * Created by Admin on 2/26/2016.
 */
public interface Hub {
    /**
     * Publishes match requests to the server so that ready Scouters will receive a match request
     * and then enter data
     *
     * @param matchRequests the match requests that you are sending, they should be newly constructed
     *                      MatchRecord. The requests should together describe all of the robots that
     *                      need to be scouted for a specific match
     */
    public void publishMatchRequests(List<MatchRecord> matchRequests);

    /**
     * Retrieves the Hubs database which contains a dynamic collection of all submitted MatchRecords
     *
     * @return the Database that the Server is placing the data it collects into
     */
    public Database getDatabase();

    /**
     * @return the number of Scouters currently connected to the Hub
     */
    public int getNumConnected();

    /**
     * @return the number of connected Scouters who have their ready flag set
     */
    public int getNumReady();

    /**
     * Kills the entire Server thread system and should only be used when the Hub is being completely closed
     */
    public void kill();
}
