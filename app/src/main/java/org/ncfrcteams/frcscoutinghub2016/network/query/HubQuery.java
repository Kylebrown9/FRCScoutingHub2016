package org.ncfrcteams.frcscoutinghub2016.network.query;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.ncfrcteams.frcscoutinghub2016.network.Network;
import org.ncfrcteams.frcscoutinghub2016.network.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Admin on 2/26/2016.
 */
public class HubQuery extends Thread {
    BluetoothDevice device;
    BluetoothSocket bluetoothSocket;
    ObjectInputStream objectInputStream = null;
    ObjectOutputStream objectOutputStream = null;

    String hubName = null;

    /**
     * Creates and starts a thread that will attempt to find a host on a BluetoothDevice
     * @param device the BluetoothDevice you want to determine whether or not is hosting
     * @return the thread that is performing the check
     */
    public static HubQuery spawn(BluetoothDevice device) {
        HubQuery hubQuery = new HubQuery(device);
        hubQuery.start();
        return hubQuery;
    }

    private HubQuery(BluetoothDevice device) {
        this.device = device;
    }

    /**
     * Attempts to acquire a HostDetail from its BluetoothDevice
     */
    public void run() {
        bluetoothSocket = Network.startSocketTo(device);

        Message message;
        try {
            objectInputStream = new ObjectInputStream(bluetoothSocket.getInputStream());
            objectOutputStream = new ObjectOutputStream(bluetoothSocket.getOutputStream());

            objectOutputStream.writeObject(new HubQueryMessage());
            message = (Message) objectInputStream.readObject();

            if(message.getType() == Message.Type.HUBNAME) {
                hubName = ((HubNameMessage) message).getName();
            } else {
                Log.d("HubQuery","invalid reply from server");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeAll();
        }
    }

    /**
     * @return an object that describes the hub that corresponds to its BluetoothDevice and name if there is one,
     * if not hub has been found returns null
     */
    public HubDetails getHubDetails() {
        if(hubName != null)
            return new HubDetails(hubName,device);
        return null;
    }

    /**
     * Stops the HubQuery operation by closing the ObjectInputStream
     */
    public void kill() {
        Log.d("HubQuery","Thread close");
        closeAll();
    }

    private void closeAll() {
        try {
            if(objectOutputStream != null) {
                objectOutputStream.flush();
                objectOutputStream.close();
            }

            if(objectInputStream != null)
                objectInputStream.close();

            if(bluetoothSocket != null)
                bluetoothSocket.close();
        } catch (IOException e) {
            Log.d("HubQuery","IOException in IO stream closings");
            e.printStackTrace();
        }
    }
}
