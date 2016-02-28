package org.ncfrcteams.frcscoutinghub2016.network.server;

import org.ncfrcteams.frcscoutinghub2016.database.Database;
import org.ncfrcteams.frcscoutinghub2016.database.MatchRecord;

import java.util.List;

/**
 * Created by Admin on 2/26/2016.
 */
public abstract class Hub {
    /**
     * creates and starts a Hub containing a server which will manage new and persistent connections
     * and handle all direct dialogue with the remote clients. The hub has heavily restricted access
     * privileges this is highly intentional and the User Interface should not circumvent these
     * restrictions. Will returns null in cases where Server.spawn() returns null;
     *
     * @param name the host users selected name for their hub e.g. "Pyrotech-Hub"
     * @param passcode the host users selected passcode for their hub e.g. "12345"
     * @return a Hub interface for an actively running Server
     */
    public static Hub getHub(String name, String passcode) {
        return Server.spawn(name,passcode);
    }

    /**
     * Publishes match requests to the server so that ready Scouters will receive a match request
     * and then enter data
     *
     * @param matchRequests the match requests that you are sending, they should be newly constructed
     *                      MatchRecord. The requests should together describe all of the robots that
     *                      need to be scouted for a specific match
     */
    public abstract void publishMatchRequests(List<MatchRecord> matchRequests);

    /**
     * Retrieves the Hubs database which contains a dynamic collection of all submitted MatchRecords
     *
     * @return the Database that the Server is placing the data it collects into
     */
    public abstract Database getDatabase();

    /**
     * @return the number of Scouters currently connected to the Hub
     */
    public abstract int getNumConnected();

    /**
     * @return the number of connected Scouters who have their ready flag set
     */
    public abstract int getNumReady();

    /**
     * Kills the entire Server thread system and should only be used when the Hub is being completely closed
     */
    public abstract void kill();
}
