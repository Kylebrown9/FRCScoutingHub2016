package org.ncfrcteams.frcscoutinghub2016.network.dialogue;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.ncfrcteams.frcscoutinghub2016.network.Network;

import java.io.IOException;

/**
 * Created by Admin on 2/26/2016.
 */
public class Client extends Thread {
    public enum ClientState {READY,UNREADY,INMATCH}
    public static final long PULSE_RATE = 1000;
    private static int NUM_CLIENTS = 0;

    private BluetoothDevice targetServer;
    private BluetoothSocket serverConnection;
    private boolean alive = true;
    private int messageCount = 0;
//    private ClientState

    public static Client spawn(BluetoothDevice targetServer) {
        if(NUM_CLIENTS != 0) {
            Log.d("Network","Attempted to create more than one Client");
            return null;
        }
        NUM_CLIENTS++;
        Client client = new Client(targetServer);
        client.start();
        return client;
    }

    private Client(BluetoothDevice targetServer) {
        this.targetServer = targetServer;
    }

    public void run() {
        try {
            serverConnection = targetServer.createRfcommSocketToServiceRecord(Network.SCOUTING_HUB_UUID);
            serverConnection.connect();

            while (alive) {

                Thread.sleep(PULSE_RATE);
            }
        } catch (IOException e) {
            Log.d("Network","Connection Timeout, Client did not find Server at target BluetoothDevice");
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d("Network","Interrupt, Client waiting interval has been interrupted");
            e.printStackTrace();
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
