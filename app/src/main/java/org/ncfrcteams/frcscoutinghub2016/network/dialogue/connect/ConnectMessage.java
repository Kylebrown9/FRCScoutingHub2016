package org.ncfrcteams.frcscoutinghub2016.network.dialogue.connect;

import org.ncfrcteams.frcscoutinghub2016.network.Message;

import java.io.Serializable;

/**
 * Created by Admin on 2/26/2016.
 */
public class ConnectMessage extends Message implements Serializable {
    String passcode;
    /**
     * @param passcode the passcode the client is attempting to connect with
     */
    public ConnectMessage(String passcode) {
        super(Message.Type.CONNECT);
        this.passcode = passcode;
    }

    /**
     * @return the passcode that the client attempted to connect with
     */
    public String getPasscode() {
        return passcode;
    }
}
