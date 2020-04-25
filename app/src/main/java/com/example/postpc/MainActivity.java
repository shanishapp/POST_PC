package com.example.postpc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnTodoListener {

    private EditText editText = null;
    private String editTextHint = "";
    private ArrayList<TODO> todoList = new ArrayList<>();
    private TODO todo = null;
    private View view = null;
    private Snackbar snackbar = null;
    private TodoAdapter adapter = null;
    private int current_pos = -1;
    private AlertDialog alertDialog;
    private SharedPreferences paramsFile;
    private SharedPreferences todosFile;
    private int uniqueID;


    final private String error_message = "you can't create an empty TODO item, oh silly!";
    final private int snackbarDuration = Snackbar.LENGTH_SHORT;
    final private String todoListFileName = "todos";
    final private String activityParamsFileName = "params";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
        Log.d("sizeoftodolist",String.valueOf(todoList.size()));

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.todo_recycler_view);
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initVariables()
    {
        view = findViewById(R.id.main_layout);
        snackbar = Snackbar.make(view, error_message, snackbarDuration);

        paramsFile = this.getSharedPreferences(activityParamsFileName,MODE_PRIVATE);
        todosFile = this.getSharedPreferences(todoListFileName,MODE_PRIVATE);

        editText = findViewById(R.id.editText);
        editTextHint = paramsFile.getString("editTextHint","");
        editText.setText(editTextHint);
        uniqueID = paramsFile.getInt("uniqueID",0);

        Gson gson = new Gson();
        todoList = new ArrayList<>();
        Map<String,?> todosJson = todosFile.getAll();
        List<String> keys = new ArrayList<>(todosJson.keySet());
        Collections.sort(keys,new StrComparator());
        for (String key : keys) {
            String value = todosJson.get(key).toString();
            todoList.add(gson.fromJson(value,TODO.class));
        }
        adapter = new TodoAdapter(todoList,this);

    }

    public void createTodo(View v)
    {
        String text = editText.getText().toString();
        if (text.equals("")) {
            snackbar.show();
        } else {
            todo = new TODO(text, false,uniqueID);
            uniqueID += 1;
            paramsFile.edit().putInt("uniqueID",uniqueID).apply();
            todoList.add(todo);
            saveTodo(todo);
            editText.setText("");
            paramsFile.edit().remove("editTextHint").apply();
            adapter.notifyItemInserted(todoList.lastIndexOf(todo));
        }
    }

    private void saveTodo(TODO todo)
    {
        Gson gson = new Gson();
        String json = gson.toJson(todo);
        todosFile.edit().putString(todo.id,json).apply();
    }

    private void dialogShow()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_delete_item, null);
        alert.setView(dialogView);
        alertDialog = alert.create();
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
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
    public void onTodoClick(int pos, ImageView imageView)
    {
        TODO todoItem = todoList.get(pos);
        if (!todoItem.isDone)
        {
            todoItem.isDone = true;
            imageView.setBackgroundResource(R.drawable.done);
            Context context = getApplicationContext();
            CharSequence text = "TODO "+todoItem.description+" is now DONE. BOOM!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            adapter.notifyItemChanged(pos);
            saveTodo(todoItem);
        }
    }

    @Override
    public void onLongTodoClick(int pos)
    {
        current_pos = pos;
        dialogShow();
    }

    public void deleteTodo(View v) {
        if (todoList.size() > current_pos)
        {
            todosFile.edit().remove(todoList.get(current_pos).id).apply();
            todoList.remove(current_pos);
            adapter.notifyItemRemoved(current_pos);
        }
        closeDialog(v);
    }

    public void closeDialog(View view) {
        alertDialog.cancel();
    }

    static class StrComparator implements Comparator<String>
    {
        public int compare(String s1, String s2)
        {
            if (Integer.valueOf(s1) >= Integer.valueOf(s2))
            {
                return 0;
            }
            return -1;
        }
    }
}

