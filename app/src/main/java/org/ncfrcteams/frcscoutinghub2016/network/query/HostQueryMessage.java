package org.ncfrcteams.frcscoutinghub2016.network.query;

import org.ncfrcteams.frcscoutinghub2016.network.Message;

import java.io.Serializable;

/**
 * Created by Admin on 2/26/2016.
 */
public class HostQueryMessage extends Message implements Serializable {
    /**
     * Constructs Message with type Type.QUERY
     */
    public HostQueryMessage() {
        super(Type.QUERY);
    }
}
