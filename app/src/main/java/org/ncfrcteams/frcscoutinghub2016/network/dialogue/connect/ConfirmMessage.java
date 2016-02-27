package org.ncfrcteams.frcscoutinghub2016.network.dialogue.connect;

import org.ncfrcteams.frcscoutinghub2016.network.Message;

import java.io.Serializable;

/**
 * Created by Admin on 2/26/2016.
 */
public class ConfirmMessage extends Message implements Serializable {
    public ConfirmMessage() {
        super(Type.CONFIRM);
    }
}
