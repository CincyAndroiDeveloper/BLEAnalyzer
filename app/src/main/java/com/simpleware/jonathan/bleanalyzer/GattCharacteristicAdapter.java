package com.simpleware.jonathan.bleanalyzer;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jdavis on 8/15/2017.
 */

public class GattCharacteristicAdapter extends RecyclerView.Adapter<TwoLineViewHolder> {

    ArrayList<BluetoothGattCharacteristic> gattCharacteristics = new ArrayList(5);

    ViewOnClickListener mlistener;

    public GattCharacteristicAdapter(ViewOnClickListener mlistener) {
        this.mlistener = mlistener;
    }

    public BluetoothGattCharacteristic getCharacteristicForPosition(int position) {
        return gattCharacteristics.get(position);
    }

    public void addCharacteristics(List<BluetoothGattCharacteristic> gattCharacteristics) {
        this.gattCharacteristics = new ArrayList(gattCharacteristics);
        notifyDataSetChanged();
    }

    public void clearData() {
        int size = gattCharacteristics.size();
        gattCharacteristics.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public TwoLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new TwoLineViewHolder(inflater.inflate(android.R.layout.simple_list_item_2, parent, false), mlistener);
    }

    @Override
    public void onBindViewHolder(TwoLineViewHolder holder, int position) {
        BluetoothGattCharacteristic charact = gattCharacteristics.get(position);
        String name = UUIDConstants.getNameForServiceUUID(charact.getUuid());
        String uuid = charact.getUuid().toString();
        SpannableStringBuilder spannableString = new SpannableStringBuilder(name);
        spannableString.append(" uuid: ");
        spannableString.append(uuid);
        spannableString.setSpan(new RelativeSizeSpan(.6f), name.length(), spannableString.length() , 0);

        holder.titleTxtVw.setText(spannableString);
        StringBuilder builder = new StringBuilder("UUID: ");
        builder.append(charact.getUuid());

        String val = getCharacteristicString(charact);
        holder.subTitleTxtVw.setText(val);
    }

    private String getCharacteristicString(BluetoothGattCharacteristic characteristic) {
        StringBuilder builder = new StringBuilder("Characteristic is: ");
        if(isCharacteristicWriteable(characteristic)) {
            builder.append("Writeable, ");
        }
        if(isCharacteristicReadable(characteristic)) {
            builder.append("Readable, ");
        }
        if(isCharacteristicNotifiable(characteristic)) {
            builder.append("Notifiable  ");
        }
        builder.replace(builder.length() - 2, builder.length(), "");
        return builder.toString();
    }

    /**
     * @return Returns <b>true</b> if property is writable
     */
    public static boolean isCharacteristicWriteable(BluetoothGattCharacteristic pChar) {
        return (pChar.getProperties() & (BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
    }

    /**
     * @return Returns <b>true</b> if property is Readable
     */
    public static boolean isCharacteristicReadable(BluetoothGattCharacteristic pChar) {
        return ((pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
    }

    /**
     * @return Returns <b>true</b> if property is supports notification
     */
    public boolean isCharacteristicNotifiable(BluetoothGattCharacteristic pChar) {
        return (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
    }

    @Override
    public int getItemCount() {
        return gattCharacteristics.size();
    }
}

