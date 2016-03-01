package org.ncfrcteams.frcscoutinghub2016.network.query;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Admin on 2/26/2016.
 */
public class QueryTask extends AsyncTask<QueryTask.HostDetailSetListener,Void,Set<HostDetails>> {
    public static final long TIMEOUT = 3000;
    private HostDetailSetListener hostDetailSetListener;

    /**
     * The core functionality of the QueryTask.
     * Opens up a communication with all of the BluetoothDevices that are bonded, waits TIMEOUT
     * amount of time then asks whether the HostQuery found a Hub and then kills the HostQuery
     * This should not be explicitly called.
     */
    @Override
    protected Set<HostDetails> doInBackground(HostDetailSetListener... params) {
        Set<BluetoothDevice> bluetoothDeviceSet = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
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
            if (hostDetails != null)
                newHostDetailsSet.add(hostDetails);

            hostQuery.kill();
        }

        this.hostDetailSetListener = params[0];
        return newHostDetailsSet;
    }

    @Override
    protected void onPostExecute(Set<HostDetails> hostDetailsSet) {
        super.onPostExecute(hostDetailsSet);
        hostDetailSetListener.onHostDetailsReady(hostDetailsSet);
    }

    public interface HostDetailSetListener {
        void onHostDetailsReady(Set<HostDetails> hostDetailsSet);
    }
}
