package org.ncfrcteams.frcscoutinghub2016.network.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;

import org.ncfrcteams.frcscoutinghub2016.database.Database;
import org.ncfrcteams.frcscoutinghub2016.database.MatchRecord;
import org.ncfrcteams.frcscoutinghub2016.network.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2/26/2016.
 */
public class Server extends Hub {
    private static int NUMSERVERS = 0;

    private String name;
    private String passcode;
    private ConnectionListener connectionListener;
    private List<SocketThread> socketThreadList = new ArrayList<>();

    private List<MatchRecord> matchRequests;

    private Database database = new Database();

    private Server(String name, String passcode) {
        this.name = name;
        this.passcode = passcode;

        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothServerSocket serverSocket = adapter.listenUsingRfcommWithServiceRecord(Network.NAME, Network.SCOUTING_HUB_UUID);
            connectionListener = ConnectionListener.spawn(this, serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //******************************Socket Methods*********************************
    public String getName() {
        return name;
    }

    public boolean matchesPasscode(String attempt) {
        return attempt.equals(passcode);
    }

    public void add(SocketThread socketThread) {
        socketThreadList.add(socketThread);
    }

    public void remove(SocketThread socketThread) {
        socketThreadList.remove(socketThread);
    }

    public void submitMatchRecord(MatchRecord matchRecord) {
        database.addMatchRecord(matchRecord);
    }

    //*****************************Interface Methods***************************************
    public static Server getServer(String name, String passcode) {
        if(NUMSERVERS != 0) {
            return null;
        }
        NUMSERVERS++;
        return new Server(name,passcode);
    }

    public void publishMatchRequests(List<MatchRecord> matchRequests) {
        this.matchRequests = matchRequests;
    }

    public Database getDatabase() {
        return database;
    }

    public int getNumConnected() {
        int num = 0;
        for(SocketThread socketThread : socketThreadList) {
            if(socketThread.isConnected()) {
                num++;
            }
        }
        return num;
    }

    public int getNumReady() {
        int num = 0;
        for(SocketThread socketThread : socketThreadList) {
            if(socketThread.isReady()) {
                num++;
            }
        }
        return num;
    }

    public void kill() {
        connectionListener.kill();
    }
}
