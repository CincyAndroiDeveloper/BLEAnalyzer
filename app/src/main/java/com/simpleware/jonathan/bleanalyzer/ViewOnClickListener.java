package com.simpleware.jonathan.bleanalyzer;

import android.view.View;

/**
 * Created by JDavis on 8/14/2017.
 */

public interface ViewOnClickListener {
    /**
     * Callback method that will get fired when a View inside of a RecyclerView is tapped.
     *
     * @param v         The View that was clicked.
     * @param position  The position of the data in the RecyclerView's dataset that this view represents.
     */
    void onViewClicked(View v, int position);
}
