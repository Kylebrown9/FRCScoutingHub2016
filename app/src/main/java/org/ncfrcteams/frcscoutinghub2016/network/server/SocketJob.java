package org.ncfrcteams.frcscoutinghub2016.network.server;

import android.bluetooth.BluetoothSocket;

import org.ncfrcteams.frcscoutinghub2016.database.MatchRecord;
import org.ncfrcteams.frcscoutinghub2016.network.Message;
import org.ncfrcteams.frcscoutinghub2016.network.checkup.AcknowledgeMessage;
import org.ncfrcteams.frcscoutinghub2016.network.checkup.CheckupMessage;
import org.ncfrcteams.frcscoutinghub2016.network.connect.ConfirmMessage;
import org.ncfrcteams.frcscoutinghub2016.network.connect.ConnectMessage;
import org.ncfrcteams.frcscoutinghub2016.network.query.HubNameMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Admin on 2/26/2016.
 */
public class SocketJob extends Thread {
    private Server server;
    private BluetoothSocket bluetoothSocket;
    private boolean alive = true;
    private boolean connected = false;
    private boolean clientReadyFlag = false;

    public static SocketJob spawn(Server server, BluetoothSocket bluetoothSocket) {
        SocketJob newOne = new SocketJob(server, bluetoothSocket);
        newOne.start();
        return newOne;
    }

    private SocketJob(Server server, BluetoothSocket bluetoothSocket) {
        this.server = server;
        this.bluetoothSocket = bluetoothSocket;
    }

    public void run() {
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;

        try {
            objectInputStream = new ObjectInputStream(bluetoothSocket.getInputStream());
            objectOutputStream = new ObjectOutputStream(bluetoothSocket.getOutputStream());

            Message firstMessage = (Message) objectInputStream.readObject();
            if(firstMessage.getType() == Message.Type.QUERY) {
                objectOutputStream.writeObject(new HubNameMessage(server.getName()));
                closeAll();
                return;
            }

            if(firstMessage.getType() != Message.Type.CONNECT) {
                closeAll();
                return;
            }

            ConnectMessage connectMessage = (ConnectMessage) firstMessage;

            //Checks if the ConnectMessage had the correct passcode
            if(!server.matchesPasscode(connectMessage.getPasscode())) {
                objectOutputStream.writeObject(new ConfirmMessage(false));
                return;
            } else {
                objectOutputStream.writeObject(new ConfirmMessage(true));
                server.add(this);
                connected = true;
            }

            Message newMessage;
            CheckupMessage checkupMessage;
            AcknowledgeMessage acknowledgeMessage;

            while(alive) {
                newMessage = (Message) objectInputStream.readObject();
                if(newMessage.getType() != Message.Type.CHECKUP) {
                    continue;
                }
                checkupMessage = (CheckupMessage) newMessage;
                acknowledgeMessage = checkupMessage.update(this);
                objectOutputStream.writeObject(acknowledgeMessage);
            }
        } catch (IOException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    public void setReady(boolean ready) {
        this.clientReadyFlag = ready;
    }

    public MatchRecord requestMatch() {
        return server.getMatchRequest();
    }

    public void submitMatchRecord(MatchRecord matchRecord) {
        server.submitMatchRecord(matchRecord);
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isReady() {
        return clientReadyFlag;
    }

    public void kill() {
        connected = false;
        server.remove(this);
        alive = false;
        closeAll();
    }

    public void closeAll() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}