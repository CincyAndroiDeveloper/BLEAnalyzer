package com.simpleware.jonathan.bleanalyzer;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by JDavis on 8/14/2017.
 */

public class ConnectionActivity extends AppCompatActivity {

    BluetoothDevice mDevice;
    boolean mConnected = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDevice = getIntent().getParcelableExtra(ListLEDevicesActivity.SELECTED_DEVICE);
        String friendlyName = mDevice.getName();
        StringBuilder builder = new StringBuilder((friendlyName != null && !friendlyName.isEmpty()) ? friendlyName : mDevice.getAddress());
        toolbar.setSubtitle(builder.toString());
    }

    public void connect(View view) {
        if(!mConnected) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mDevice.connectGatt(this, false, mGattCallback, BluetoothDevice.TRANSPORT_LE);
            }
            else {
                mDevice.connectGatt(this, false, mGattCallback);
            }
        }
    }

    BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if(newState ==  BluetoothProfile.STATE_CONNECTED) {

            }
            else if(newState == BluetoothProfile.STATE_DISCONNECTED) {

            }
        }
    };
}
