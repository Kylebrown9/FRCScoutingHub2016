package org.ncfrcteams.frcscoutinghub2016.network.checkup;

import org.ncfrcteams.frcscoutinghub2016.database.MatchRecord;
import org.ncfrcteams.frcscoutinghub2016.network.Message;
import org.ncfrcteams.frcscoutinghub2016.network.server.SocketJob;

import java.io.Serializable;

/**
 * Created by Admin on 2/26/2016.
 */
public class CheckupMessage extends Message implements Serializable {
    private int id;

    public CheckupMessage(int id) {
        super(Type.CONNECT);
        this.id = id;
    }
    //TODO: finish documentation

    /**
     *
     * @return The messages id number, should correspond to the order of the messages
     */
    public int getId() {
        return id;
    }

    /**
     * Called by the SocketJob on itself to allow the Message to make changes to the SocketJob
     *
     * @param socketJob The SocketJob maintaining the connection this message is a part of
     * @return The message that will be sent in response to this message
     */
    public AcknowledgeMessage update(SocketJob socketJob) {
        return null;
    }

    //*******************************Checkup Message Subclasses*************************************

    /**
     * Generates an update message that tells the server the current status of the match being
     * scouted by the client
     *
     * @param id The id number of the message being sent
     * @param matchRecord The updated MatchRecord being sent to the server
     * @return The message for the Client to send
     */

    public static CheckupMessage UpdateMessage(int id, final MatchRecord matchRecord) {
        return new CheckupMessage(id) {
            public AcknowledgeMessage update(SocketJob socketJob) {
                socketJob.setReady(true);
                socketJob.submitMatchRecord(matchRecord);

                return new AcknowledgeMessage(getId());
            }
        };
    }

    /**
     * Generates a message that indicates to the server that the client is ready to accept a match
     * This should prompt the server to send a MatchRecord that represents a match request
     * if one is available.
     *
     * @param id The id number of the message being sent
     * @return The message for the Client to send
     */
    public static CheckupMessage ReadyMessage(int id) {
        return new CheckupMessage(id) {
            /**
             * @param socketJob The SocketJob maintaining the connection this message is a part of
             * @return an AcknowledgeMessage with the same id number, if there is a matchRequest
             * available it will contain it as well;
             */
            public AcknowledgeMessage update(SocketJob socketJob) {
                socketJob.setReady(true);
                MatchRecord matchRecord = socketJob.requestMatch();

                if(matchRecord != null) {
                    return new AcknowledgeMessage(getId(),matchRecord);
                }
                return new AcknowledgeMessage(getId());
            }
        };
    }

    /**
     * Generates a message that indicates to the server that the client is not ready to
     * accept a match
     *
     * @param id The id number of the message being sent
     * @return The message for the Client to send
     */
    public static CheckupMessage UnreadyMessage(int id) {
        return new CheckupMessage(id) {
            public AcknowledgeMessage update(SocketJob socketJob) {
                socketJob.setReady(false);

                return new AcknowledgeMessage(getId());
            }
        };
    }

    /**
     * Generates a message that indicates to the server that the client is disconnecting
     *
     * @param id The id number of the message being sent
     * @return The message for the Client to send
     */
    public static CheckupMessage DisconnectMessage(int id) {
        return new CheckupMessage(id) {
            public AcknowledgeMessage update(SocketJob socketJob) {
                socketJob.kill();
                return null;
            }
        };
    }
}
