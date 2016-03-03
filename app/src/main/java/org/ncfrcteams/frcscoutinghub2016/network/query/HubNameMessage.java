package org.ncfrcteams.frcscoutinghub2016.network.query;

import org.ncfrcteams.frcscoutinghub2016.network.Message;

import java.io.Serializable;

/**
 * Created by Admin on 2/26/2016.
 */
public class HubNameMessage extends Message implements Serializable {
    String hubName;

    public HubNameMessage(String hubName) {
        super(Type.HUBNAME);
        this.hubName = hubName;
    }

    public String getName() {
        return hubName;
    }
}
