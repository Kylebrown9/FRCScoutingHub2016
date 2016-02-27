package org.ncfrcteams.frcscoutinghub2016.network.dialogue.checkup;

import org.ncfrcteams.frcscoutinghub2016.network.Message;

import java.io.Serializable;

/**
 * Created by Admin on 2/26/2016.
 */
public class AcknowledgeMessage extends Message implements Serializable {
    private int id;

    public AcknowledgeMessage(int id) {
        super(Type.ACKNOWLEDGE);
    }

    public int getId() {
        return id;
    }

    public boolean isMatchRequest() {
        return false;
    }
}
