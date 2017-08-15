package com.simpleware.jonathan.bleanalyzer;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.bluetooth.le.ScanSettings.CALLBACK_TYPE_ALL_MATCHES;
import static android.bluetooth.le.ScanSettings.MATCH_MODE_AGGRESSIVE;
import static android.bluetooth.le.ScanSettings.SCAN_MODE_BALANCED;
import static android.bluetooth.le.ScanSettings.SCAN_MODE_LOW_LATENCY;

/**
 * Created by JDavis on 8/14/2017.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ListLEDevicesActivity extends AppCompatActivity implements BluetoothAdapter.LeScanCallback, ViewOnClickListener, TextView.OnEditorActionListener {

    public static final int PERMISSION_REQUEST_CODE = 1170;
    public static final String DEVICE_ARRAYLIST = "DEVICE_ARRAYLIST";
    public static final String SELECTED_DEVICE = "SELECTED_DEVICE";
    BluetoothAdapter mAdapter;
    DeviceAdapter mDeviceAdapter;
    RecyclerView deviceList;
    EditText mFilterTxtVw;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_le_activity_xml);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mFilterTxtVw = (EditText) findViewById(R.id.services_uuid);
        mFilterTxtVw.setOnEditorActionListener(this);

        setSupportActionBar(toolbar);
        deviceList = (RecyclerView) findViewById(R.id.device_list);
        deviceList.setAdapter(mDeviceAdapter = new DeviceAdapter(this));
        deviceList.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemAnimator animator = deviceList.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        // Recommended way of getting the BluetoothAdapter on Jelly Bean MR2 and above.
        BluetoothManager manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mAdapter = manager.getAdapter();

        if(savedInstanceState != null) {
            ArrayList<BTDevice> parcelableArrayList = savedInstanceState.getParcelableArrayList(DEVICE_ARRAYLIST);
            if(parcelableArrayList != null) {
                mDeviceAdapter.setDevices(parcelableArrayList);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAdapter == null) {
            // TODO
            return;
        }
        else if(!mAdapter.isEnabled()) {
            // TODO
            return;
        }
        else if(!hasPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            return;
        }

        String uuid = mFilterTxtVw.getText().toString();
        if(uuid.length() == 36) {
            // Stop the old scan.
            stopLEScan();
            mDeviceAdapter.clearData();
            // Start a new scan.
            startLEScan(UUID.fromString(uuid));
            return;
        }
        else {
            startLEScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_CODE) {
            if(permissions.length > 0) {
                if(permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLEScan();
                }
            }
        }
    }

    boolean hasPermission() {
        return (ContextCompat.checkSelfPermission(this,
                permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onViewClicked(View v, int position) {
        Intent intent = new Intent(this, ConnectionActivity.class);
        // Store the selected device into the starting intent so we can immediately connect to the device.
        intent.putExtra(SELECTED_DEVICE, mDeviceAdapter.getDevice(position).device);
        startActivity(intent);
    }
    public void startLEScan() {
        startLEScan(null);
    }

    public void startLEScan(UUID uuid) {
        if(mAdapter != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                BluetoothLeScanner bluetoothLeScanner = mAdapter.getBluetoothLeScanner();
                // Customize the ScanSettings to aggressively scan for the Bluetooth LE.
                ScanSettings.Builder builder = new ScanSettings.Builder();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    builder.setCallbackType(CALLBACK_TYPE_ALL_MATCHES);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    builder.setMatchMode(MATCH_MODE_AGGRESSIVE);
                }
                builder.setReportDelay(0);
                builder.setScanMode(SCAN_MODE_LOW_LATENCY);

                List<ScanFilter> filters = null;
                if(uuid != null) {
                    ScanFilter.Builder filterBuilder = new ScanFilter.Builder();
                    filterBuilder.setServiceUuid(new ParcelUuid(uuid));
                    // Create a new list to store the ScanFilter before passing it to the bluetoothLEScanner.
                    filters = new ArrayList<>();
                    filters.add(filterBuilder.build());
                }

                bluetoothLeScanner.startScan(filters, builder.build(), mScanCallback);
            }
            else {
                mAdapter.startLeScan(new UUID[] {uuid}, this);
            }
        }
    }

    public void stopLEScan() {
        if(mAdapter != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                BluetoothLeScanner bluetoothLeScanner = mAdapter.getBluetoothLeScanner();
                bluetoothLeScanner.stopScan(mScanCallback);
            }
            else {
                mAdapter.stopLeScan(this);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLEScan();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(isChangingConfigurations()) {
            outState.putParcelableArrayList(DEVICE_ARRAYLIST, mDeviceAdapter.getDevices());
        }
    }

    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            // Pass in the newly scanned LE device.. The adapter will handle updating the RecyclerView.
            mDeviceAdapter.addDeviceAndRssi(result.getDevice(), result.getRssi());
        }
    };

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        // Pass in the newly scanned LE device.. The adapter will handle updating the RecyclerView.
        mDeviceAdapter.addDeviceAndRssi(device, rssi);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            String uuid = mFilterTxtVw.getText().toString();
            if(uuid.length() == 36) {
                // Stop the old scan.
                stopLEScan();
                mDeviceAdapter.clearData();
                // Start a new scan.
                startLEScan(UUID.fromString(uuid));
                return true;
            }
            mFilterTxtVw.setError("Enter a valid UUID String");
            return true;
        }
        return false;
    }

    /**
     * OnClick method for starting a LE scan.
     *
     * @param view
     */
    public void scan(View view) {
        String uuid = mFilterTxtVw.getText().toString();
        if(uuid.length() == 36) {
            // Stop the old scan.
            stopLEScan();
            mDeviceAdapter.clearData();
            // Start a new scan.
            startLEScan(UUID.fromString(uuid));
            return;
        }
        else {
            startLEScan();
        }
        mFilterTxtVw.setError("Enter a valid UUID String");
    }


//    private String createUUIDString(String dumbString) {
//        if(dumbString.length() == 32) {
//            StringBuilder builder = new StringBuilder(dumbString);
//            builder.insert(20, "-");
//            builder.insert(16, "-");
//            builder.insert(12, "-");
//            builder.insert(8, "-");
//            return builder.toString();
//        }
//        throw new IllegalArgumentException("Invalid UUID string: " + dumbString);
//    }
}
