package com.example.postpc;

import android.os.Parcel;
import android.os.Parcelable;

public class TODO implements Parcelable {

    public String description;
    public int isDone;

    TODO(String d, int i) {
        description = d;
        isDone = i;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeInt(isDone);

    }

    public static final Parcelable.Creator<TODO> CREATOR
            = new Parcelable.Creator<TODO>() {
        public TODO createFromParcel(Parcel in) {
            return new TODO(in);
        }

        public TODO[] newArray(int size) {
            return new TODO[size];
        }
    };

    private TODO(Parcel in)
    {
        description = in.readString();
        isDone = in.readInt();
    }
}
