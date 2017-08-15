package com.simpleware.jonathan.bleanalyzer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by JDavis on 8/14/2017.
 */

public class DeviceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView deviceNameTxtVw;
    public TextView deviceRSSITxtVw;
    public ViewOnClickListener mListener;

    public DeviceViewHolder(View itemView, ViewOnClickListener listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);
        deviceNameTxtVw = (TextView) itemView.findViewById(R.id.device_name_txtVw);
        deviceRSSITxtVw = (TextView) itemView.findViewById(R.id.device_rssi_txtVw);
    }

    @Override
    public void onClick(View v) {
        // Pass the selected view, and the adapter position that corresponds to this View.
        if(mListener != null) {
            mListener.onViewClicked(v, getAdapterPosition());
        }
    }
}
