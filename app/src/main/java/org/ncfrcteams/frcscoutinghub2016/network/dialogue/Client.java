package org.ncfrcteams.frcscoutinghub2016.network.dialogue;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.ncfrcteams.frcscoutinghub2016.network.Message;
import org.ncfrcteams.frcscoutinghub2016.network.Network;
import org.ncfrcteams.frcscoutinghub2016.network.dialogue.checkup.AcknowledgeMessage;
import org.ncfrcteams.frcscoutinghub2016.network.dialogue.checkup.CheckupMessage;
import org.ncfrcteams.frcscoutinghub2016.network.dialogue.connect.ConfirmMessage;
import org.ncfrcteams.frcscoutinghub2016.network.dialogue.connect.ConnectMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Admin on 2/26/2016.
 */
public class Client extends Thread {
    public enum ClientState {READY,UNREADY,INMATCH}
    public static final long PULSE_RATE = 1000;
    public static final int FREQUENCY = 0b111;
    private static int NUM_CLIENTS = 0;

    private BluetoothDevice targetServer;
    private String passcode;

    private BluetoothSocket serverConnection;
    private boolean alive = true;
    private boolean failedConnect = false;
    private int messageCount = 0;


    public static Client spawn(BluetoothDevice targetServer, String passcode) {
        if(NUM_CLIENTS != 0) {
            Log.d("Network","Attempted to create more than one Client");
            return null;
        }
        NUM_CLIENTS++;
        Client client = new Client(targetServer,passcode);
        client.start();
        return client;
    }

    private Client(BluetoothDevice targetServer, String passcode) {
        this.targetServer = targetServer;
        this.passcode = passcode;
    }

    public void run() {
        try {
            serverConnection = targetServer.createRfcommSocketToServiceRecord(Network.SCOUTING_HUB_UUID);
            serverConnection.connect();
        } catch (IOException e) {
            Log.d("Network-Client","Connection Timeout, Client did not find Server at target BluetoothDevice");
            failedConnect = true;
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
                Log.d("Network-Client","transferred Object not of Message type");
                e.printStackTrace();
            }

        } catch (IOException e) {
            Log.d("Network-Client","Initial message/response for connect failed");
            e.printStackTrace();
        }


        try {
            while (alive) {
                Thread.sleep(PULSE_RATE);
            }
        } catch (InterruptedException e) {
            Log.d("Network-Client","Interrupt, Client waiting interval has been interrupted");
            e.printStackTrace();
        }
    }

    //TODO:Fix entire method
    private CheckupMessage getHeartBeatMessage() {
//        if()
        if((messageCount & FREQUENCY) == 0) {
            return CheckupMessage.UpdateMessage(messageCount,null);
        } else {

        }
        return null;
    }

    public static class Sender extends Thread {
        ObjectOutputStream outputStream;

        public static Sender spawn(ObjectOutputStream outputStream) {
            Sender sender = new Sender(outputStream);
            sender.start();
            return sender;
        }

        public Sender(ObjectOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        public void run() {

        }
    }

    public static class Receiver extends Thread {
        ObjectInputStream inputStream;

        public Receiver(ObjectInputStream inputStream) {
            this.inputStream = inputStream;
        }
    }


    public void kill() {
        alive = false;
        try {
            serverConnection.close();
        } catch (IOException e) {
            Log.d("Network","Failed to close serverConnection because ");
            e.printStackTrace();
        }
        NUM_CLIENTS--;
    }
}
