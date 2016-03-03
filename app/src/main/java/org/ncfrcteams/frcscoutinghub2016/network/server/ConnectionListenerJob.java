package org.ncfrcteams.frcscoutinghub2016.network.server;

import android.bluetooth.BluetoothServerSocket;

import org.ncfrcteams.frcscoutinghub2016.network.Job;

import java.io.IOException;

/**
 * Created by Admin on 2/26/2016.
 */
public class ConnectionListenerJob extends Job {
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
            SocketJob.spawn(server, bluetoothServerSocket.accept());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
