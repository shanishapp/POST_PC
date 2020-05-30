package com.example.postpc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class done_todo_details extends AppCompatActivity {

    private DBManager dbManager;
    private TodoBoomApp app;
    private String todoId;

    private TextView content;
    private TextView firstEditView;
    private TextView lastEditView;
    private CheckBox checkBox;
    private  AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_todo_details);
        app = (TodoBoomApp) getApplicationContext();
        dbManager = app.dbManager;
        content = findViewById(R.id.done_content);
        firstEditView = findViewById(R.id.done_first_edit);
        lastEditView = findViewById(R.id.done_last_edit);

        Intent intent = getIntent();
        todoId = intent.getStringExtra("todoId");
        content.setText(intent.getStringExtra("content"));
        firstEditView.setText("first edited: "+intent.getStringExtra("firstEdit"));
        lastEditView.setText("last edited: "+intent.getStringExtra("lastEdit"));
    }

    public void markTodoAsUnDone(View view) {
        dbManager.markTodoAsUnDone(todoId);
        finish();
    }

    public void deleteTodoForever(View view) {
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           View mView = getLayoutInflater().inflate(R.layout.dialog_delete_item,null);
           builder.setView(mView);
           dialog = builder.create();
           dialog.show();
    }

    public void deleteTodo(View view) {
        dbManager.deleteTodoForever(todoId);
        dialog.cancel();
        finish();
    }

    public void closeAlert(View view) {
        dialog.cancel();
    }
}
