package org.ncfrcteams.frcscoutinghub2016.network.checkup;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.ncfrcteams.frcscoutinghub2016.network.Job;
import org.ncfrcteams.frcscoutinghub2016.network.Message;
import org.ncfrcteams.frcscoutinghub2016.network.Network;
import org.ncfrcteams.frcscoutinghub2016.network.connect.ConfirmMessage;
import org.ncfrcteams.frcscoutinghub2016.network.connect.ConnectMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Admin on 2/26/2016.
 */
public class Client extends Job {
    public enum ClientState {READY,UNREADY,INMATCH}
    public static final int FREQUENCY = 0b111;

    private BluetoothDevice targetServer;
    private String passcode;

    private BluetoothSocket serverConnection;
    private boolean alive = true;
    private int messageCount = 0;
    private SenderJob senderJob;
    private Receiver receiver;


    public static Client spawn(BluetoothDevice targetServer, String passcode) {
        Client client = new Client(targetServer,passcode);
        client.start();
        return client;
    }

    private Client(BluetoothDevice targetServer, String passcode) {
        super(false);
        this.targetServer = targetServer;
        this.passcode = passcode;
    }

    public void init() {
        try {
            serverConnection = targetServer.createRfcommSocketToServiceRecord(Network.SCOUTING_HUB_UUID);
            serverConnection.connect();
        } catch (IOException e) {
            Log.d("Client-Main","Connection Timeout, Client did not find Server at target BluetoothDevice");
            e.printStackTrace();
        }


        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(serverConnection.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(serverConnection.getInputStream());

            outputStream.writeObject(new ConnectMessage(passcode));

            try {
                Message response = (Message) inputStream.readObject();
                if(response.getType() != Message.Type.CONNECT || !((ConfirmMessage) response).isConnected()) {
                    return;
                }
            } catch (ClassNotFoundException e) {
                Log.d("Client-Main","transferred Object not of Message type");
                e.printStackTrace();
            }

        } catch (IOException e) {
            Log.d("Client-Main","Initial message/response for connect failed");
            e.printStackTrace();
        }
    }

    public void periodic() {}

    //TODO: all
    public CheckupMessage getHeartBeatMessage() {
        if((messageCount & FREQUENCY) == 0) {
            return CheckupMessage.UpdateMessage(messageCount,null);
        } else {

        }
        return null;
    }

    public static class Receiver extends Thread {

    }


    public void kill() {
        senderJob.kill();
        try {
            serverConnection.close();
        } catch (IOException e) {
            Log.d("Network","Failed to close serverConnection because ");
            e.printStackTrace();
        }
    }
}
