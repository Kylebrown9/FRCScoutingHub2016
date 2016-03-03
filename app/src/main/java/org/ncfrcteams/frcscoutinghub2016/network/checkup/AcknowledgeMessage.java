package org.ncfrcteams.frcscoutinghub2016.network.checkup;

import org.ncfrcteams.frcscoutinghub2016.database.MatchRecord;
import org.ncfrcteams.frcscoutinghub2016.network.Message;

import java.io.Serializable;

/**
 * Created by Admin on 2/26/2016.
 */
public class AcknowledgeMessage extends Message implements Serializable {
    private int id;
    private MatchRecord matchRequest;
    private boolean hasMatchRequest;

    public AcknowledgeMessage(int id) {
        super(Type.ACKNOWLEDGE);
        this.id = id;
        matchRequest = null;
        hasMatchRequest = false;
    }

    public AcknowledgeMessage(int id, MatchRecord matchRequest) {
        super(Type.ACKNOWLEDGE);
        this.id = id;
        this.matchRequest = matchRequest;
        hasMatchRequest = true;
    }

    public int getId() {
        return id;
    }

    public boolean hasMatchRequest() {
        return hasMatchRequest;
    }
}
