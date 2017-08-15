package com.simpleware.jonathan.bleanalyzer;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by JDavis on 8/14/2017.
 */

public class ConnectionActivity extends AppCompatActivity {

    BluetoothDevice mDevice;
    BluetoothGatt mBluetoothGatt;
    Drawable connActDrawable;
    boolean mConnected;
    boolean mConnecting = false;
    Handler mhandler;

    CoordinatorLayout mParentLayout;
    ImageView connIndicator;
    Button connectionBtn;
    Button discoverBtn;
    RecyclerView mServicesList;
    RecyclerView mCharacteristicList;
    GattServiceAdapter mGattServiceAdapter;
    GattCharacteristicAdapter mGattCharacAdapter;
    TextView mServiceTxtVw;
    TextView mCharacteristicTxtVw;
    private BluetoothGattService mSelectedService;
    private BluetoothGattCharacteristic mSelectedCharac;
    long startConnectTime = 0L;
    long stopConnectTime = 0L;
    long startDiscoveryTime = 0L;
    long stopDiscoveryTime = 0L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mhandler = new Handler();
        setContentView(R.layout.connection_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        connIndicator = (ImageView) findViewById(R.id.connection_indicator);
        connectionBtn = (Button) findViewById(R.id.conn_btn);
        discoverBtn = (Button) findViewById(R.id.disc_btn);
        mServiceTxtVw = (TextView) findViewById(R.id.service_txtVw);
        mCharacteristicTxtVw = (TextView) findViewById(R.id.characteristic_txtVw);

        mServicesList = (RecyclerView) findViewById(R.id.services_list);
        mCharacteristicList = (RecyclerView) findViewById(R.id.characteristic_list);
        mServicesList.setLayoutManager(new LinearLayoutManager(this));

        mServicesList.setAdapter(mGattServiceAdapter = new GattServiceAdapter(new ViewOnClickListener() {
            @Override
            public void onViewClicked(View v, int position) {
                mSelectedService = mGattServiceAdapter.getServiceForPosition(position);
                mGattCharacAdapter.addCharacteristics(mSelectedService.getCharacteristics());
                mServiceTxtVw.setText(UUIDConstants.getNameForServiceUUID(mSelectedService.getUuid()));
            }
        }));

        mCharacteristicList.setLayoutManager(new LinearLayoutManager(this));
        mCharacteristicList.setAdapter(mGattCharacAdapter = new GattCharacteristicAdapter(new ViewOnClickListener() {
            @Override
            public void onViewClicked(View v, int position) {
                mSelectedCharac = mGattCharacAdapter.getCharacteristicForPosition(position);
                mCharacteristicTxtVw.setText(UUIDConstants.getNameForCharacteristicUUID(mSelectedCharac.getUuid()));
            }
        }));


        connActDrawable = getResources().getDrawable(R.drawable.bluetooth_activity_state);
        connActDrawable.setColorFilter(new PorterDuffColorFilter(Color.RED, Mode.SRC));
        connIndicator.setImageDrawable(connActDrawable);

        setSupportActionBar(toolbar);
        mDevice = getIntent().getParcelableExtra(ListLEDevicesActivity.SELECTED_DEVICE);
        String friendlyName = mDevice.getName();
        StringBuilder builder = new StringBuilder("Device: ");
        builder.append((friendlyName != null && !friendlyName.isEmpty()) ? friendlyName : mDevice.getAddress());
        toolbar.setSubtitle(builder.toString());
    }

    public void connect(View view) {
        connectionBtn.setEnabled(false);
        if(!mConnected) {
            mConnecting = true;
            startConnectTime = System.currentTimeMillis();
            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                mBluetoothGatt = mDevice.connectGatt(getApplicationContext(), false, mGattCallback, BluetoothDevice.TRANSPORT_LE);
            }
            else {
                mBluetoothGatt = mDevice.connectGatt(getApplicationContext(), false, mGattCallback);
            }
        }
        else {
            if(mBluetoothGatt != null) {
                mBluetoothGatt.disconnect();
            }
        }
    }

    BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            mhandler.removeCallbacksAndMessages(null);
            if(newState ==  BluetoothProfile.STATE_CONNECTED) {
                stopConnectTime = System.currentTimeMillis();
                mConnected = true;
                mhandler.post(connected.setMessage("CONNECTED"));
                mConnecting = false;
                return;
            }
            else if(newState == BluetoothProfile.STATE_DISCONNECTED) {
                mBluetoothGatt.close();
                mConnected = false;
                mhandler.post(disconnected.setMessage(mConnecting ? "FAILED TO CONNECT":"DISCONNECTED"));
                mConnecting = false;
                return;
            }
            return;
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            stopDiscoveryTime = System.currentTimeMillis();
            mhandler.post(servicesDiscovered.addServices(gatt.getServices()));
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if(mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }
    }

    GattServiceRunnable servicesDiscovered = new GattServiceRunnable() {

        @Override
        public void run() {
            long diff = stopDiscoveryTime - startDiscoveryTime;
            mGattServiceAdapter.addServices(gattServices);
            discoverBtn.setEnabled(true);
            Toast.makeText(ConnectionActivity.this, "Services Discovered! time taken " + diff + " ms", Toast.LENGTH_SHORT).show();
        }
    };

    MessageRunnable connected = new MessageRunnable() {
        @Override
        public void run() {
            long diff = stopConnectTime - startConnectTime;
            discoverBtn.setEnabled(true);
            Toast.makeText(ConnectionActivity.this, message + " time taken: " + diff + " mS", Toast.LENGTH_SHORT).show();
            connActDrawable.setColorFilter(new PorterDuffColorFilter(Color.GREEN, Mode.SRC));
            connectionBtn.setText(R.string.disconnect);
            connectionBtn.setEnabled(true);
        }
    };

    MessageRunnable disconnected = new MessageRunnable() {
        @Override
        public void run() {
            mGattServiceAdapter.clearData();
            mGattCharacAdapter.clearData();
            discoverBtn.setEnabled(false);
            Toast.makeText(ConnectionActivity.this, message, Toast.LENGTH_SHORT).show();
            connActDrawable.setColorFilter(new PorterDuffColorFilter(Color.RED, Mode.SRC));
            connectionBtn.setText(R.string.connect);
            connectionBtn.setEnabled(true);
        }
    };

    public void discover(View view) {
        view.setEnabled(false);
        mGattServiceAdapter.clearData();
        mGattCharacAdapter.clearData();
        Toast.makeText(ConnectionActivity.this, "Discovering Services!", Toast.LENGTH_SHORT).show();
        if(mBluetoothGatt != null) {
            startDiscoveryTime = System.currentTimeMillis();
            mBluetoothGatt.discoverServices();
        }
    }
}
