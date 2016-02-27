package org.ncfrcteams.frcscoutinghub2016.network.dialogue.checkup;

import org.ncfrcteams.frcscoutinghub2016.database.MatchRecord;
import org.ncfrcteams.frcscoutinghub2016.network.Message;
import org.ncfrcteams.frcscoutinghub2016.network.server.Server;
import org.ncfrcteams.frcscoutinghub2016.network.server.SocketThread;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Admin on 2/26/2016.
 */
public class CheckupMessage extends Message implements Serializable {
    private int id;

    public CheckupMessage(int id) {
        super(Type.CONNECT);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public AcknowledgeMessage update(SocketThread socketThread) {
        return null;
    }

    private Object getObject() {
        return null;
    }

    public static CheckupMessage UpdateMessage(int id, final MatchRecord matchRecord) {

        return new CheckupMessage(id) {
            public AcknowledgeMessage update(SocketThread socketThread) {
                socketThread.setReady(true);
                socketThread.submitMatchRecord((MatchRecord) getObject());

                return new AcknowledgeMessage(getId());
            }

            public Object getObject() {
                return matchRecord;
            }
        };
    }

    public static CheckupMessage ReadyMessage(int id) {
        return new CheckupMessage(id) {
            public AcknowledgeMessage update(SocketThread socketThread) {
                socketThread.setReady(true);
                MatchRecord matchRecord = socketThread.requestMatch();

                if(matchRecord != null) {
                    return new MatchRequestMessage(getId(),matchRecord);
                }

                return new AcknowledgeMessage(getId());
            }
        };
    }

    public static CheckupMessage UnreadyMessage(int id) {
        return new CheckupMessage(id) {
            public AcknowledgeMessage update(SocketThread socketThread) {
                socketThread.setReady(false);

                return new AcknowledgeMessage(getId());
            }
        };
    }

    public static CheckupMessage DisconnectMessage(int id) {
        return new CheckupMessage(id) {
            public AcknowledgeMessage update(SocketThread socketThread) {
                socketThread.kill();
                return null;
            }
        };
    }
}
