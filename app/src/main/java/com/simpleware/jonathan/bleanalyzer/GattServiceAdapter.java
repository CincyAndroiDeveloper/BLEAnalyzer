package com.simpleware.jonathan.bleanalyzer;

import android.R.layout;
import android.bluetooth.BluetoothGattService;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by jdavis on 8/15/2017.
 */

public class GattServiceAdapter extends Adapter<TwoLineViewHolder> {

    ArrayList<BluetoothGattService> gattServices = new ArrayList(5);

    ViewOnClickListener mlistener;

    public GattServiceAdapter(ViewOnClickListener mlistener) {
        this.mlistener = mlistener;
    }

    public void clearData() {
        int size = gattServices.size();
        gattServices.clear();
        notifyItemRangeRemoved(0, size);
    }

    public BluetoothGattService getServiceForPosition(int position) {
        return gattServices.get(position);
    }

    public void addServices(ArrayList<BluetoothGattService> gattServices) {
        this.gattServices = new ArrayList(gattServices);
        notifyDataSetChanged();
    }

    @Override
    public TwoLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new TwoLineViewHolder(inflater.inflate(layout.simple_list_item_2, parent, false), mlistener);
    }

    @Override
    public void onBindViewHolder(TwoLineViewHolder holder, int position) {
        BluetoothGattService service = gattServices.get(position);
        holder.titleTxtVw.setText(UUIDConstants.getNameForServiceUUID(service.getUuid()));
        StringBuilder builder = new StringBuilder("UUID: ");
        builder.append(service.getUuid().toString());
        holder.subTitleTxtVw.setText(builder.toString());
    }

    @Override
    public int getItemCount() {
        return gattServices.size();
    }
}
