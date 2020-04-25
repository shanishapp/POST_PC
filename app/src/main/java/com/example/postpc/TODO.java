package com.example.postpc;

import android.os.Parcel;
import android.os.Parcelable;

public class TODO{

    public String description;
    public boolean isDone;
    public String id;

    TODO(String d, boolean i, int id) {
        description = d;
        isDone = i;
        this.id = String.valueOf(id);
    }
}
