package org.ncfrcteams.frcscoutinghub2016.network.connect;

import android.bluetooth.BluetoothDevice;

/**
 * Created by Admin on 3/1/2016.
 */
public class Login {
    BluetoothDevice bluetoothDevice;
    String passcode;

    public Login(BluetoothDevice bluetoothDevice, String passcode) {
        this.bluetoothDevice = bluetoothDevice;
        this.passcode = passcode;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public String getPasscode() {
        return passcode;
    }
}
