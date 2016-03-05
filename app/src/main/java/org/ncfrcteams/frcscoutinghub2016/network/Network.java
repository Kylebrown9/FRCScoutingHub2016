package org.ncfrcteams.frcscoutinghub2016.network;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.ncfrcteams.frcscoutinghub2016.network.server.Hub;
import org.ncfrcteams.frcscoutinghub2016.network.server.Server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Admin on 2/26/2016.
 */
public class Network {
    public static final UUID SCOUTING_HUB_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");   //a random UUID generated from a website
    public static final String NAME = "org.ncfrcteams.frcscoutinghub2016";
    private static final int FIRST_CHANNEL = 1;
    private static final int LAST_CHANNEL = 6;

    public static Hub getHub(String name, String passcode) {
        return Server.spawn(name, passcode);
    }

    //http://stackoverflow.com/questions/18657427/ioexception-read-failed-socket-might-closed-bluetooth-on-android-4-3
    public static BluetoothSocket startSocketTo(BluetoothDevice bluetoothDevice) {
        BluetoothSocket socket = null;

        try {
            socket = bluetoothDevice.createRfcommSocketToServiceRecord(Network.SCOUTING_HUB_UUID);
        } catch (Exception e) {
            Log.d("HubQuery", "Error creating socket");
        }

        try {
            if (socket != null) socket.connect();
            Log.d("HubQuery","Connected via standard method");
        } catch (IOException e) {
//            socket = tryAltConnectTo(bluetoothDevice, FIRST_CHANNEL, LAST_CHANNEL);
        }
        Log.d("HubQuery",(socket != null && socket.isConnected())? "socket is connected" : "socket is not connected");
        return socket;
    }

    public static BluetoothSocket tryAltConnectTo(BluetoothDevice bluetoothDevice, int firstChannel, int lastChannel) {
        BluetoothSocket socket = null;

        try {
            Method createSocket = bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
            for(int i=firstChannel;i<lastChannel;i++) {
                socket = tryConnectTo(createSocket, bluetoothDevice, i);
            }
        } catch (NoSuchMethodException e1) {
            Log.d("HubQuery","could not find reflection method");
            e1.printStackTrace();
        }

        Log.d("HubQuery",(socket != null && socket.isConnected())? "socket is connected" : "socket is not connected");
        return socket;
    }

    private static BluetoothSocket tryConnectTo(Method m, BluetoothDevice bluetoothDevice, int port) {
        BluetoothSocket socket;
        try {
            socket = (BluetoothSocket) m.invoke(bluetoothDevice, port);
            socket.connect();

            Log.d("HubQuery", "Connected via fallback num:" + port);
            return socket;
        }
        catch (Exception e2) {
            Log.d("HubQuery", "Failed fallback num:" + port);
            return null;
        }
    }
}
