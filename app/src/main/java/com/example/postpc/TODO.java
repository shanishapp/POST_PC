package com.example.postpc;

import com.google.firebase.Timestamp;

public class TODO {

    public String content;
    public boolean isDone;
    public Timestamp creation_timestamp;
    public Timestamp edit_timestamp;
    public String id;

    TODO(){}
    TODO(String content, boolean isDone) {
        this.content = content;
        creation_timestamp = Timestamp.now();
        edit_timestamp = Timestamp.now();
        this.id ="";
        this.isDone = isDone;
    }


}
