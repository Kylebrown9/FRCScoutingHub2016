package org.ncfrcteams.frcscoutinghub2016.network.server;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;

import org.ncfrcteams.frcscoutinghub2016.database.Database;
import org.ncfrcteams.frcscoutinghub2016.database.SuperMap;
import org.ncfrcteams.frcscoutinghub2016.network.stuff.Job;
import org.ncfrcteams.frcscoutinghub2016.network.Network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Admin on 2/26/2016.
 */
public class Server extends Job implements Hub {
    private String name;
    private String passcode;
    private ConnectionListenerJob connectionListenerJob;
    private List<SocketJob> socketJobList = new ArrayList<>();

    private List<SuperMap> matchRequestList;
    private Stack<SuperMap> matchRequestStack = new Stack<SuperMap>();

    private Database database = new Database();

    public static Server spawn(String name, String passcode) {
        Server server = new Server(name,passcode);
        server.run();
        return server;
    }

    private Server(String name, String passcode) {
        super(false);
        this.name = name;
        this.passcode = passcode;
    }

    public void init() {
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothServerSocket serverSocket = adapter.listenUsingRfcommWithServiceRecord(Network.NAME, Network.SCOUTING_HUB_UUID);
            connectionListenerJob = ConnectionListenerJob.spawn(this, serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void periodic() {}

    //******************************SocketJob Methods*********************************
    public String getHubName() {
        return name;
    }

    public boolean matchesPasscode(String attempt) {
        return attempt.equals(passcode);
    }

    public void add(SocketJob socketJob) {
        socketJobList.add(socketJob);
    }

    public void remove(SocketJob socketJob) {
        socketJobList.remove(socketJob);
    }

    public void submitMatchRecord(SuperMap superMap) {
        database.addMatchRecord(superMap);
    }

    public SuperMap getMatchRequest() {
        if(matchRequestStack.size() != 0) {
            return matchRequestStack.pop();
        }
        return null;
    }

    //*****************************Interface Methods***************************************
    public void publishMatchRequests(List<SuperMap> matchRequestList) {
        this.matchRequestList = matchRequestList;

        matchRequestStack = new Stack<SuperMap>();
        matchRequestStack.addAll(matchRequestList);
    }

    public Database getDatabase() {
        return database;
    }

    /**
     * @return the number of connected SocketThreads as defined by SocketJob.isConnected()
     */
    public int getNumConnected() {
        int num = 0;
        for(SocketJob socketJob : socketJobList) {
            if(socketJob.isConnected()) {
                num++;
            }
        }
        return num;
    }

    /**
     * @return the number of connected and ready SocketThreads as defined by SocketJob.isConnected()
     * and SocketJob.isReady()
     */
    public int getNumReady() {
        int num = 0;
        for(SocketJob socketJob : socketJobList) {
            if(socketJob.isConnected() && socketJob.isReady()) {
                num++;
            }
        }
        return num;
    }

    /**
     * Kills the contained ConnectionListenerJob and all associated SocketThreads
     */
    public void kill() {
        super.kill();
        connectionListenerJob.kill();
        for(SocketJob socketJob : socketJobList) {
            socketJob.kill();
        }
    }
}
