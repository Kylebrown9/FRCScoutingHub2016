package org.ncfrcteams.frcscoutinghub2016.network.query;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Admin on 2/26/2016.
 */
public class Querier extends Thread {
    public static final long TIMEOUT = 15000;
    private Set<BluetoothDevice> bluetoothDeviceSet;
    private Set<HostDetails> hostDetailsSet = new HashSet<>();
    boolean hasList = false;

    public static Querier spawn() {
        Querier querier = new Querier();
        querier.start();
        return querier;
    }

    public void run() {
        bluetoothDeviceSet = BluetoothAdapter.getDefaultAdapter().getBondedDevices();

        Set<HostQuery> hostQuerySet = new HashSet<HostQuery>();
        for(BluetoothDevice bluetoothDevice : bluetoothDeviceSet) {
            hostQuerySet.add(HostQuery.spawn(bluetoothDevice));
        }

        //Wait
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Set<HostDetails> newHostDetailsSet = new HashSet<>();
        HostDetails hostDetails;
        for(HostQuery hostQuery : hostQuerySet) {
            hostDetails = hostQuery.getHostDetails();
            if(hostDetails != null)
                newHostDetailsSet.add(hostDetails);

            hostQuery.kill();
        }

        //Update the HostDetail List
        hostDetailsSet.clear();
        hostDetailsSet.addAll(newHostDetailsSet);
    }

    public boolean hasList() {
        return hasList;
    }

    public Set<HostDetails> getHostDetailsSet() {
        return hostDetailsSet;
    }
}
