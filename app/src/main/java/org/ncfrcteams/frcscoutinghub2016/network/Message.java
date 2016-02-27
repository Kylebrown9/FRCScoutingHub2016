package org.ncfrcteams.frcscoutinghub2016.network;

import java.io.Serializable;

/**
 * Created by Admin on 2/26/2016.
 */
public class Message implements Serializable {
    public enum Type {QUERY,HOSTNAME,CONNECT,CONFIRM,CHECKUP,ACKNOWLEDGE};

    Type type;

    public Message(Type type) {
        this.type = type;
    }

    /**
     * @return the Message sublcass that this instance belongs to
     */
    public Type getType() {
        return type;
    }
}
