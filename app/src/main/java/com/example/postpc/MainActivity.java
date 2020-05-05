package com.example.postpc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnTodoListener {

    private String text ;
    private EditText editText = null;
    private String editTextHint = "";
    private ArrayList<TODO> todoList = new ArrayList<>();
    private Button button = null;
    private TODO todo = null;
    private View view = null;
    private Snackbar snackbar = null;
    private TodoAdapter adapter = null;
    private int current_pos = -1;
    private DBManager dbManager;

    final private String error_message = "you can't create an empty TODO item, oh silly!";
    final private int snackbarDuration = Snackbar.LENGTH_SHORT;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = DBManager.getInstance();

        initVariables();
        Log.d("sizeoftodolist",String.valueOf(todoList.size()));

        adapter = new TodoAdapter(todoList,this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTodo(adapter);

            }
        });

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.todo_recycler_view);
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initVariables()
    {
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        view = findViewById(R.id.main_layout);
        snackbar = Snackbar.make(view, error_message, snackbarDuration);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        text = sp.getString("text","");
        editTextHint = sp.getString("editTextHint","");
        editText.setText(editTextHint);
        todoList = dbManager.getAllTodos();
    }

    private void createTodo(TodoAdapter adapter)
    {
        text = editText.getText().toString();
        if (text.equals("")) {
            snackbar.show();
        } else {
            todo = new TODO(text, false);
            dbManager.addTodo(todo,adapter);
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
        SharedPreferences sp  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("text", text);
        editor.putString("editTextHint", editText.getText().toString());
        editor.apply();
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
            dbManager.markTodoAsDone(todoItem);
            imageView.setBackgroundResource(R.drawable.done);
            Context context = getApplicationContext();
            CharSequence text = "TODO "+todoItem.content+" is now DONE. BOOM!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            adapter.notifyItemChanged(pos);
        }
    }

}
