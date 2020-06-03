package com.example.postpc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {

    private EditText editText = null;
    private ArrayList<TODO> todoList;
    private Snackbar snackbar = null;
    private TodoAdapter adapter = null;
    private SharedPreferences paramsFile;
    private TodoBoomApp app;


    final private String error_message = "you can't create an empty TODO item, oh silly!";
    final private int snackbarDuration = Snackbar.LENGTH_SHORT;
    final private String activityParamsFileName = "params";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.todo_recycler_view);
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initVariables()
    {
        app = (TodoBoomApp) getApplicationContext();
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        View view = findViewById(R.id.main_layout);
        snackbar = Snackbar.make(view, error_message, snackbarDuration);
        editText = findViewById(R.id.editText);
        String editTextHint = sp.getString("editTextHint", "");
        editText.setText(editTextHint);
        adapter = app.dbManager.adapter;
        paramsFile = this.getSharedPreferences(activityParamsFileName,MODE_PRIVATE);

    }

    public void createTodo(View v)
    {
        String text = editText.getText().toString();
        if (text.equals("")) {
            snackbar.show();
        } else {
            TODO todo = new TODO(text, false);
            app.dbManager.addTodo(todo);
            editText.setText("");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        saveData();
    }

    private void saveData()
    {
        paramsFile.edit().
                putString("editTextHint", editText.getText().toString()).apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        todoList = app.dbManager.getAllTodos();
    }
}

