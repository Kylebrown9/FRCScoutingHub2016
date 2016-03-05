package org.ncfrcteams.frcscoutinghub2016.network.server;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.ncfrcteams.frcscoutinghub2016.database.MatchRecord;
import org.ncfrcteams.frcscoutinghub2016.network.stuff.Job;
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
public class SocketJob extends Job {
    private Server server;
    private BluetoothSocket bluetoothSocket;

    private boolean connected = false;
    private boolean clientReadyFlag = false;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Message newMessage;
    private CheckupMessage checkupMessage;
    private AcknowledgeMessage acknowledgeMessage;

    public static SocketJob spawn(Server server, BluetoothSocket bluetoothSocket) {
        SocketJob newOne = new SocketJob(server, bluetoothSocket);
        newOne.start();
        return newOne;
    }

    private SocketJob(Server server, BluetoothSocket bluetoothSocket) {
        super(true);
        this.server = server;
        this.bluetoothSocket = bluetoothSocket;
    }

    public void init() {
        try {
            Log.d("SocketJob","reaches init");
            objectInputStream = new ObjectInputStream(bluetoothSocket.getInputStream());
            objectOutputStream = new ObjectOutputStream(bluetoothSocket.getOutputStream());

            Log.d("SocketJob","reaches message request");
            Message firstMessage = (Message) objectInputStream.readObject();
            if(firstMessage.getType() == Message.Type.QUERY) {
                Log.d("SocketJob","First message is a QUERY");
                objectOutputStream.writeObject(new HubNameMessage(server.getName()));
                kill();
                return;
            }

            if(firstMessage.getType() != Message.Type.CONNECT) {
                kill();
                return;
            }

            ConnectMessage connectMessage = (ConnectMessage) firstMessage;

            //Checks if the ConnectMessage had the correct passcode
            if(!server.matchesPasscode(connectMessage.getPasscode())) {
                objectOutputStream.writeObject(new ConfirmMessage(false));
            } else {
                objectOutputStream.writeObject(new ConfirmMessage(true));
                server.add(this);
                connected = true;
            }

        } catch (ClassNotFoundException e) {
            Log.d("SocketJob-Init","received object not of message type");
            kill();
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("SocketJob-Init","object stream failure");
            kill();
            e.printStackTrace();
        }
    }

    public void periodic() {
        try {
            newMessage = (Message) objectInputStream.readObject();
            if(newMessage.getType() != Message.Type.CHECKUP) {
                return;
            }
            checkupMessage = (CheckupMessage) newMessage;
            acknowledgeMessage = checkupMessage.update(this);
            objectOutputStream.writeObject(acknowledgeMessage);

        } catch (ClassNotFoundException e) {
            Log.d("SocketJob-Periodic","received object not of message type");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("SocketJob-Periodic","object stream failure");
            e.printStackTrace();
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
        super.kill();
        if(connected) {
            connected = false;
            server.remove(this);
        }
        closeAll();
    }

    public void closeAll() {
        try {
            if(objectOutputStream != null) {
                objectOutputStream.flush();
                objectOutputStream.close();
            }

            if(objectInputStream != null)
                objectInputStream.close();

            if(bluetoothSocket != null)
                bluetoothSocket.close();
        } catch (IOException e) {
            Log.d("SocketJob-closeAll()","IOException");
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.d("SocketJob-closeAll()","NullPointerException");
            e.printStackTrace();
        }
    }
}
