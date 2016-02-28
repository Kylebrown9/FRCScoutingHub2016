package org.ncfrcteams.frcscoutinghub2016.network.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.util.Log;

import org.ncfrcteams.frcscoutinghub2016.database.Database;
import org.ncfrcteams.frcscoutinghub2016.database.MatchRecord;
import org.ncfrcteams.frcscoutinghub2016.network.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2/26/2016.
 */
public class Server extends Hub implements Runnable {
    private static int NUM_SERVERS = 0;

    private String name;
    private String passcode;
    private ConnectionListener connectionListener;
    private List<SocketThread> socketThreadList = new ArrayList<>();

    private List<MatchRecord> matchRequests;

    private Database database = new Database();

    public static Server spawn(String name, String passcode) {
        if(NUM_SERVERS != 0) {
            Log.d("Network", "Attempted to create more than one Server");
            return null;
        }
        NUM_SERVERS++;
        Server server = new Server(name,passcode);
        server.run();
        return server;
    }

    private Server(String name, String passcode) {
        this.name = name;
        this.passcode = passcode;
    }

    public void run() {
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

    public MatchRecord getMatchRequest() {
        return null;
    }

    //*****************************Interface Methods***************************************
    public void publishMatchRequests(List<MatchRecord> matchRequests) {
        this.matchRequests = matchRequests;
    }

    public Database getDatabase() {
        return database;
    }

    /**
     * @return the number of connected SocketThreads as defined by SocketThread.isConnected()
     */
    public int getNumConnected() {
        int num = 0;
        for(SocketThread socketThread : socketThreadList) {
            if(socketThread.isConnected()) {
                num++;
            }
        }
        return num;
    }

    /**
     * @return the number of connected and ready SocketThreads as defined by SocketThread.isConnected()
     * and SocketThread.isReady()
     */
    public int getNumReady() {
        int num = 0;
        for(SocketThread socketThread : socketThreadList) {
            if(socketThread.isConnected() && socketThread.isReady()) {
                num++;
            }
        }
        return num;
    }

    /**
     * Kills the contained ConnectionListener and all associated SocketThreads
     */
    public void kill() {
        connectionListener.kill();
        for(SocketThread socketThread : socketThreadList) {
            socketThread.kill();
        }
    }
}
