package com.example.postpc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class todo_details extends AppCompatActivity {

    private DBManager dbManager;
    private TODO todo;
    private EditText edit;
    private TextView content;
    private TextView firstEditView;
    private TextView lastEditView;
    private TodoBoomApp app;
    private SharedPreferences paramsFile;
    String firstEdit;
    String lastEdit;
    private int pos;
    String todoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (TodoBoomApp) getApplicationContext();
        setContentView(R.layout.todo_details_layout);
        edit = findViewById(R.id.newContent);
        content = findViewById(R.id.todoDetails);
        firstEditView = findViewById(R.id.firstEdit);
        lastEditView = findViewById(R.id.lastEdit);
        paramsFile = this.getSharedPreferences("params",MODE_PRIVATE);
        dbManager = app.dbManager;

        Intent intent = getIntent();
        todoId = intent.getStringExtra("todoId");
        content.setText(intent.getStringExtra("content"));
        firstEdit = intent.getStringExtra("firstEdit");
        firstEditView.setText("first edited: "+firstEdit);
        edit.setText(paramsFile.getString("editTextHint",""));

        String lastContent = paramsFile.getString("lastContent","");
        if ( !lastContent.equals(""))
        {
            content.setText(lastContent);
        }
        else
        {
            content.setText(intent.getStringExtra("content"));
        }

        String lastEdited = paramsFile.getString("lastEdit","");
        if ( !lastEdited.equals(""))
        {
            lastEditView.setText(lastEdited);
        }
        else
        {
            lastEditView.setText(intent.getStringExtra("lastEdit"));
        }
    }


    public void updateTodoContent(View view) {
        String newContent = edit.getText().toString();
        Timestamp timestamp = Timestamp.now();
        dbManager.setTodoData(todoId,newContent,timestamp);
        content.setText(newContent);
        Toast.makeText(app,"todo "+ content.getText().toString() + "updated",Toast.LENGTH_SHORT).show();
        edit.setText("");
        String lastLastEdit = "last edited: "+timestamp.toDate().toString();
        lastEditView.setText(lastLastEdit);
    }

    public void markTodoAsDone(View view) {
        dbManager.markTodoAsDone(todoId);
        Gif_loading_View vw = new Gif_loading_View(this);
        setContentView(vw);
        Toast.makeText(app,"todo "+ content.getText().toString() + " is Done, good job!",Toast.LENGTH_SHORT).show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                finish();
            }
        }, 4000);
    }


    public class Gif_loading_View extends WebView {

        public Gif_loading_View(Context context) {

            super(context);
            loadUrl("file:///android_asset/todoDone.html");

        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        paramsFile.edit().
                putString("lastContent", content.getText().toString()).
                putString("lastEdit",lastEditView.getText().toString()).
                putString("editTextHint", edit.getText().toString()).apply();
    }
}
