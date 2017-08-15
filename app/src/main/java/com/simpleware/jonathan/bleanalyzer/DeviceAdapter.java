package com.simpleware.jonathan.bleanalyzer;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_CLASSIC;
import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_DUAL;
import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_LE;
import static android.bluetooth.BluetoothDevice.DEVICE_TYPE_UNKNOWN;

/**
 * Created by JDavis on 8/14/2017.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceViewHolder> {

    private ArrayList<BTDevice> mDevices = new ArrayList<>(8);
    private ViewOnClickListener mListener;

    public DeviceAdapter(ViewOnClickListener listener) {
        mListener = listener;
    }

    public ArrayList<BTDevice> getDevices() {
        return new ArrayList<>(mDevices);
    }

    public void setDevices(ArrayList<BTDevice> devices) {
        mDevices = new ArrayList<>(devices);
        notifyDataSetChanged();
    }

    public void clearData() {
        int size = mDevices.size();
        mDevices.clear();
        notifyItemRangeRemoved(0, size);

    }

    public void addDeviceAndRssi(BluetoothDevice device, int rssi) {
        int size = mDevices.size();
        for (int i = 0; i < size; i++) {
            BTDevice btDevice = mDevices.get(i);
            // Check to see if the device already exist inside of the mDevices list and notify the adapter.
            if(btDevice.device.equals(device)) {
                btDevice.rssi = rssi;
                notifyItemChanged(i);
                return;
            }
        }
        // We don't already have an entry inside of the mDevices list, so add a new one and notify the adatper an item was added.
        mDevices.add(new BTDevice(device, rssi));
        notifyItemInserted(size);
    }

    /**
     *
     * @param pos
     * @return
     */
    public BTDevice getDevice(int pos) {
        return mDevices.get(pos);
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new DeviceViewHolder(inflater.inflate(R.layout.device_item, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position) {
        BTDevice btDevice = mDevices.get(position);
        String friendlyName = btDevice.device.getName();
        StringBuilder builder = new StringBuilder((friendlyName != null && !friendlyName.isEmpty()) ? friendlyName : btDevice.device.getAddress());
        builder.append("\n");
        builder.append(getDeviceTypeString(btDevice.device));
        holder.deviceNameTxtVw.setText(builder.toString());
        holder.deviceRSSITxtVw.setText(String.valueOf(btDevice.rssi));
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    private String getDeviceTypeString(BluetoothDevice device) {
        switch (device.getType()) {
            case DEVICE_TYPE_CLASSIC: return "type: DEVICE_TYPE_CLASSIC";
            case DEVICE_TYPE_LE: return "type: DEVICE_TYPE_DUAL";
            case DEVICE_TYPE_DUAL: return "type: DEVICE_TYPE_DUAL";
            default: return "type: DEVICE_TYPE_UNKNOWN";
        }
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }
}
