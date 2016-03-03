package org.ncfrcteams.frcscoutinghub2016.network.query;

import org.ncfrcteams.frcscoutinghub2016.network.Message;

import java.io.Serializable;

/**
 * Created by Admin on 2/26/2016.
 */
public class HubQueryMessage extends Message implements Serializable {
    /**
     * Constructs Message with type Type.QUERY
     */
    public HubQueryMessage() {
        super(Type.QUERY);
    }
}
