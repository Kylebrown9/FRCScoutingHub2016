package org.ncfrcteams.frcscoutinghub2016.network.query;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Admin on 2/26/2016.
 */
public class HubDetails {
    private String name;
    private BluetoothDevice device;

    public HubDetails(String name, BluetoothDevice device) {
        this.name = name;
        this.device = device;
    }

    public String getName() {
        return name;
    }

    public BluetoothDevice getDevice() {
        return device;
    }
}
