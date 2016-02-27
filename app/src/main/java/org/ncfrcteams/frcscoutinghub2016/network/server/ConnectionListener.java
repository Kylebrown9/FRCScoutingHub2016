package org.ncfrcteams.frcscoutinghub2016.network.server;

import android.bluetooth.BluetoothServerSocket;

import java.io.IOException;

/**
 * Created by Admin on 2/26/2016.
 */
public class ConnectionListener extends  Thread {
    private Server server;
    private BluetoothServerSocket bluetoothServerSocket;
    boolean alive = true;

    public static ConnectionListener spawn(Server server, BluetoothServerSocket bluetoothServerSocket) {
        ConnectionListener connectionListener = new ConnectionListener(server, bluetoothServerSocket);
        connectionListener.start();
        return connectionListener;
    }

    private ConnectionListener(Server server, BluetoothServerSocket bluetoothServerSocket) {
        this.server = server;
        this.bluetoothServerSocket = bluetoothServerSocket;
    }

    public void run() {
        while(alive) {
            try {
                SocketThread.spawn(server, bluetoothServerSocket.accept());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void kill() {
        alive = false;
    }
}
