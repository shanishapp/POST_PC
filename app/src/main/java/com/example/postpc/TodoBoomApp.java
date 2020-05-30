package com.example.postpc;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class TodoBoomApp extends Application {

    public DBManager dbManager;

    @Override
    public void onCreate() {
        super.onCreate();
        dbManager = DBManager.getInstance();
        dbManager.setContext(getApplicationContext());
    }

    public void showTodo(String todoId,String content,String firstEdit, String lastEdit)
    {
        Intent intent = new Intent(this,todo_details.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("todoId",todoId);
        intent.putExtra("content",content);
        intent.putExtra("firstEdit",firstEdit);
        intent.putExtra("lastEdit",lastEdit);
        startActivity(intent);
    }

    public void showDoneTodo(String todoId,String content,String firstEdit, String lastEdit)
    {
        Intent intent = new Intent(this,done_todo_details.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("todoId",todoId);
        intent.putExtra("content",content);
        intent.putExtra("firstEdit",firstEdit);
        intent.putExtra("lastEdit",lastEdit);
        startActivity(intent);
    }
}
