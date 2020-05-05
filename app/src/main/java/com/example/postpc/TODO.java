package com.example.postpc;

import com.google.firebase.Timestamp;

public class TODO {

    public String content;
    public boolean isDone;
    public Timestamp creation_timestamp;
    public Timestamp edit_timestamp;
    public int id;

    private static int idUniqueNumber = 0;

    TODO(){}

    TODO(String content, boolean isDone) {
        this.content = content;
        this.isDone = isDone;
        creation_timestamp = Timestamp.now();
        edit_timestamp = Timestamp.now();
        this.id = idUniqueNumber;
        idUniqueNumber +=1;
    }


}
