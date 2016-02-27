package org.ncfrcteams.frcscoutinghub2016.network.query;

import org.ncfrcteams.frcscoutinghub2016.network.Message;

import java.io.Serializable;

/**
 * Created by Admin on 2/26/2016.
 */
public class HostNameMessage extends Message implements Serializable {
    String hostName;

    public HostNameMessage(String hostName) {
        super(Type.HOSTNAME);
        this.hostName = hostName;
    }

    public String getName() {
        return hostName;
    }
}
