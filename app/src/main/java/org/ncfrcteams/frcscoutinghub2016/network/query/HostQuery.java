package org.ncfrcteams.frcscoutinghub2016.network.query;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import org.ncfrcteams.frcscoutinghub2016.network.Network;
import org.ncfrcteams.frcscoutinghub2016.network.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Admin on 2/26/2016.
 */
public class HostQuery extends Thread {
    BluetoothDevice device;
    BluetoothSocket bluetoothSocket;

    String hostName = null;

    /**
     * Creates and starts a thread that will attempt to find a host on a BluetoothDevice
     * @param device the BluetoothDevice you want to determine whether or not is hosting
     * @return the thread that is performing the check
     */
    public static HostQuery spawn(BluetoothDevice device) {
        HostQuery hostQuery = new HostQuery(device);
        hostQuery.start();
        return hostQuery;
    }

    private HostQuery(BluetoothDevice device) {
        this.device = device;
    }

    /**
     * Attempts to acquire a HostDetail from its BluetoothDevice
     */
    public void run() {
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;

        Message message;
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(Network.SCOUTING_HUB_UUID);
            objectInputStream = new ObjectInputStream(bluetoothSocket.getInputStream());
            objectOutputStream = new ObjectOutputStream(bluetoothSocket.getOutputStream());

            objectOutputStream.writeObject(new HostQueryMessage());
            message = (Message) objectInputStream.readObject();

            if(message.getType() == Message.Type.HOSTNAME) {
                hostName = ((HostNameMessage) message).getName();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return an object that describes the host that corresponds to its BluetoothDevice and name if there is one,
     * if not host has been found returns null
     */
    public HostDetails getHostDetails() {
        if(hostName != null)
            return new HostDetails(hostName,device);
        return null;
    }

    /**
     * Stops the HostQuery operation by closing the ObjectInputStream
     */
    public void kill() {
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
