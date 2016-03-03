package org.ncfrcteams.frcscoutinghub2016.network.connect;

import org.ncfrcteams.frcscoutinghub2016.network.Message;

import java.io.Serializable;

/**
 * Created by Admin on 2/26/2016.
 */
public class ConfirmMessage extends Message implements Serializable {
    private boolean connected;

    public ConfirmMessage(boolean connected) {
        super(Type.CONFIRM);
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }
}
