package org.ncfrcteams.frcscoutinghub2016.network.query;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Admin on 2/26/2016.
 */
public class QueryTask extends AsyncTask<QueryTask.HostDetailSetListener,Void,Set<HubDetails>> {
    public static final long TIMEOUT = 3000;
    private HostDetailSetListener hostDetailSetListener;

    /**
     * The core functionality of the QueryTask.
     * Opens up a communication with all of the BluetoothDevices that are bonded, waits TIMEOUT
     * amount of time then asks whether the HubQuery found a Hub and then kills the HubQuery
     * This should not be explicitly called.
     */
    @Override
    protected Set<HubDetails> doInBackground(HostDetailSetListener... params) {
        Set<BluetoothDevice> bluetoothDeviceSet = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        Set<HubQuery> hubQuerySet = new HashSet<HubQuery>();

        Log.d("NumBluetooth",Integer.toString(bluetoothDeviceSet.size())); //currently equals 1 if a paired device is within range even unconnected

        for(BluetoothDevice bluetoothDevice : bluetoothDeviceSet) {
            hubQuerySet.add(HubQuery.spawn(bluetoothDevice));
        }
        //Wait
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Set<HubDetails> newHubDetailsSet = new HashSet<>();
        HubDetails hubDetails;
        for(HubQuery hubQuery : hubQuerySet) {
            hubDetails = hubQuery.getHostDetails();
            if (hubDetails != null)
                newHubDetailsSet.add(hubDetails);

            hubQuery.kill();
        }

        this.hostDetailSetListener = params[0];
        return newHubDetailsSet;
    }

    @Override
    protected void onPostExecute(Set<HubDetails> hubDetailsSet) {
        super.onPostExecute(hubDetailsSet);
        if(hostDetailSetListener.isActive())
            hostDetailSetListener.onHostDetailsReady(hubDetailsSet);
    }

    public interface HostDetailSetListener {
        void onHostDetailsReady(Set<HubDetails> hubDetailsSet);
        boolean isActive();
    }
}
