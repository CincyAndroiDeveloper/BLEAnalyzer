package com.simpleware.jonathan.bleanalyzer;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JDavis on 8/14/2017.
 */

public class BTDevice implements Parcelable {
    public BluetoothDevice device;
    public int rssi = 0;

    public BTDevice(BluetoothDevice device, int rssi) {
        this.device = device;
        this.rssi = rssi;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.device, flags);
        dest.writeInt(this.rssi);
    }

    protected BTDevice(Parcel in) {
        this.device = in.readParcelable(BluetoothDevice.class.getClassLoader());
        this.rssi = in.readInt();
    }

    public static final Parcelable.Creator<BTDevice> CREATOR = new Parcelable.Creator<BTDevice>() {
        @Override
        public BTDevice createFromParcel(Parcel source) {
            return new BTDevice(source);
        }

        @Override
        public BTDevice[] newArray(int size) {
            return new BTDevice[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BTDevice btDevice = (BTDevice) o;

        return device != null ? device.equals(btDevice.device) : btDevice.device == null;

    }

}
