package org.ncfrcteams.frcscoutinghub2016.network.server;

import android.bluetooth.BluetoothServerSocket;
import android.util.Log;

import org.ncfrcteams.frcscoutinghub2016.network.stuff.Job;

import java.io.IOException;

/**
 * Created by Admin on 2/26/2016.
 */
public class ConnectionListenerJob extends Job {
    public static int NUM_ACCEPTS = 0;
    private Server server;
    private BluetoothServerSocket bluetoothServerSocket;

    public static ConnectionListenerJob spawn(Server server, BluetoothServerSocket bluetoothServerSocket) {
        ConnectionListenerJob connectionListenerJob = new ConnectionListenerJob(server, bluetoothServerSocket);
        connectionListenerJob.start();
        return connectionListenerJob;
    }

    private ConnectionListenerJob(Server server, BluetoothServerSocket bluetoothServerSocket) {
        super(true);
        this.server = server;
        this.bluetoothServerSocket = bluetoothServerSocket;
    }

    public void init(){}

    public void periodic() {
        try {
            Log.d("ConnectionListenerJob","start of accept()");
            SocketJob.spawn(server, bluetoothServerSocket.accept());
            NUM_ACCEPTS++;
//            Log.d("ConnectionListenerJob", "connect heard");
        } catch (IOException e) {
            Log.d("ConnectionListenerJob","IOException on accept()");
            e.printStackTrace();
        }
    }
}
