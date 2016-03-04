package org.ncfrcteams.frcscoutinghub2016.network;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.ncfrcteams.frcscoutinghub2016.network.server.Hub;
import org.ncfrcteams.frcscoutinghub2016.network.server.Server;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Admin on 2/26/2016.
 */
public class Network {
    public static final UUID SCOUTING_HUB_UUID = UUID.fromString("4e73e0f0-dcd5-11e5-a837-0800200c9a66");   //a random UUID generated from a website
    public static final String NAME = "org.ncfrcteams.frcscoutinghub2016";

    public static Hub getHub(String name, String passcode) {
        return Server.spawn(name, passcode);
    }

    //http://stackoverflow.com/questions/18657427/ioexception-read-failed-socket-might-closed-bluetooth-on-android-4-3
    public static BluetoothSocket getConnectedSocketTo(BluetoothDevice bluetoothDevice) {
        BluetoothSocket socket = null;

        try {
            socket = bluetoothDevice.createRfcommSocketToServiceRecord(Network.SCOUTING_HUB_UUID);
        } catch (Exception e) {
            Log.d("HubQuery", "Error creating socket");}

        try {
            if (socket != null) socket.connect();
            Log.e("HubQuery","Connected via standard method");
        } catch (IOException e) {
            Log.e("",e.getMessage());
            try {
                Log.d("HubQuery", "Failed fallback");

                socket =(BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(bluetoothDevice,1);
                socket.connect();

                Log.d("HubQuery", "Connected via fallback");
            }
            catch (Exception e2) {
                Log.d("HubQuery", "Failed fallback");
            }
        }
        Log.d("HubQuery",(socket != null && socket.isConnected())? "socket is connected" : "socket is not connected");
        return socket;
    }
}
