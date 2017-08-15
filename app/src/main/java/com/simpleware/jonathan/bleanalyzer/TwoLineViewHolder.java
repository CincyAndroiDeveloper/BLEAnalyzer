package com.simpleware.jonathan.bleanalyzer;

import android.R.id;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * Created by jdavis on 8/15/2017.
 */

public class TwoLineViewHolder extends ViewHolder implements OnClickListener {

    public TextView titleTxtVw;
    public TextView subTitleTxtVw;
    public ViewOnClickListener mListener;


    public TwoLineViewHolder(View itemView, ViewOnClickListener listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);
        titleTxtVw = (TextView) itemView.findViewById(id.text1);
        subTitleTxtVw = (TextView) itemView.findViewById(id.text2);
    }

    @Override
    public void onClick(View v) {
        if(mListener != null) {
            mListener.onViewClicked(v, getAdapterPosition());
        }
    }
}
