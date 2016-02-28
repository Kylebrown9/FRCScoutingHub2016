package org.ncfrcteams.frcscoutinghub2016.network.query;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Admin on 2/26/2016.
 */
public class Querier extends Thread {
    public static final long TIMEOUT = 15000;
    private static int NUM_RUNNING = 0;

    private Set<BluetoothDevice> bluetoothDeviceSet;
    private Set<HostDetails> hostDetailsSet = new HashSet<>();
    private boolean done = false;

    /**
     * Creates a running Querier if there is no other querier running
     * that will check all of its paired devices for Hubs
     *
     * @return the started Querier
     */
    public static Querier spawn() {
        if(NUM_RUNNING != 0) {
            return null;
        }
        NUM_RUNNING++;
        Querier querier = new Querier();
        querier.start();
        return querier;
    }

    /**
     * @return The number of actively running Querier instances should be 1 or 0
     */
    public static int getNumRunning() {
        return NUM_RUNNING;
    }

    /**
     * The core functionality of the Querier.
     * Opens up a communication with all of the BluetoothDevices that are bonded, waits TIMEOUT
     * amount of time then asks whether the HostQuery found a Hub and then kills the HostQuery
     * This should not be explicitly called.
     */
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
        done = true;
        NUM_RUNNING--;
    }

    /**
     * @return whether the query operation has been finished
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Retrieves all of the HostDetails collected from the HostQueries if the run() operation has
     * been completed, othewise it returns null.
     *
     * @return the Set of HostDetails for every Host that responded, or null
     */
    public Set<HostDetails> getHostDetailsSet() {
        return done ? hostDetailsSet : null;
    }
}
