package com.simpleware.jonathan.bleanalyzer;

import android.bluetooth.BluetoothGattService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jdavis on 8/15/2017.
 */

public abstract class GattServiceRunnable implements Runnable {

    public ArrayList<BluetoothGattService> gattServices;

    public Runnable addServices(List<BluetoothGattService> services) {
        gattServices = new ArrayList<>(services);
        return this;
    }
}
