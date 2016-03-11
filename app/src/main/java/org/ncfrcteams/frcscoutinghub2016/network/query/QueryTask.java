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
public class QueryTask extends AsyncTask<QueryTask.HubDetailSetListener,Void,Set<HubDetails>> {
    public static final long TIMEOUT = 10000;
    private HubDetailSetListener hubDetailSetListener;

    /**
     * The core functionality of the QueryTask.
     * Opens up a communication with all of the BluetoothDevices that are bonded, waits TIMEOUT
     * amount of time then asks whether the HubQuery found a Hub and then kills the HubQuery
     * This should not be explicitly called.
     */
    @Override
    protected Set<HubDetails> doInBackground(HubDetailSetListener... params) {
        Set<BluetoothDevice> bluetoothDeviceSet = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        Set<HubQuery> hubQuerySet = new HashSet<HubQuery>();

        Log.d("QueryTask","Num Bonded" + Integer.toString(bluetoothDeviceSet.size())); //currently equals 1 if a paired device is within range even unconnected

        for(BluetoothDevice bluetoothDevice : bluetoothDeviceSet) {
            hubQuerySet.add(HubQuery.spawn(bluetoothDevice));
        }

        Set<HubDetails> newHubDetailsSet = new HashSet<>();
        if(bluetoothDeviceSet.size() == 0)
            return newHubDetailsSet;
        //Wait
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        HubDetails hubDetails;
        for(HubQuery hubQuery : hubQuerySet) {
            hubDetails = hubQuery.getHubDetails();
            if (hubDetails != null)
                newHubDetailsSet.add(hubDetails);

            hubQuery.kill();
        }

        this.hubDetailSetListener = params[0];
        return newHubDetailsSet;
    }

    @Override
    protected void onPostExecute(Set<HubDetails> hubDetailsSet) {
        super.onPostExecute(hubDetailsSet);
        if(hubDetailSetListener.isActive())
            hubDetailSetListener.onHubDetailsReady(hubDetailsSet);
    }

    /**
     * The interface which any Activity creating a QueryTask must implement in order to receive
     * the results
     */
    public interface HubDetailSetListener {
        /**
         * @return whether the HubDetailSetListener still can still accept the result
         */
        boolean isActive();

        /**
         * Delivers the hubDetailsSet to the HubDetailSetListener
         * @param hubDetailsSet
         */
        void onHubDetailsReady(Set<HubDetails> hubDetailsSet);
    }
}
