package org.ncfrcteams.frcscoutinghub2016.network.dialogue.checkup;

import org.ncfrcteams.frcscoutinghub2016.database.MatchRecord;

import java.io.Serializable;

/**
 * Created by Admin on 2/26/2016.
 */
public class MatchRequestMessage extends AcknowledgeMessage implements Serializable {


    public MatchRequestMessage(int id, MatchRecord matchTemplate) {
        super(id);
    }

    public boolean isMatchRequest() {
        return true;
    }
}
