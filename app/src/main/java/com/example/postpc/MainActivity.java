package com.example.postpc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

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

    final private String error_message = "you can't create an empty TODO item, oh silly!";
    final private int snackbarDuration = Snackbar.LENGTH_SHORT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVariables(savedInstanceState);

        final TodoAdapter adapter = new TodoAdapter(todoList,this);

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

    private void createTodo(TodoAdapter adapter)
    {
        text = editText.getText().toString();
        if (text.equals("")) {
            snackbar.show();
        } else {
            todo = new TODO(text, 0);
            todoList.add(todo);
            editText.setText("");
            adapter.notifyDataSetChanged();
        }
    }

    private void initVariables(Bundle savedInstanceState)
    {
        view = findViewById(R.id.main_layout);
        snackbar = Snackbar.make(view, error_message, snackbarDuration);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        setOrientation();

        if (savedInstanceState != null) {
            text = savedInstanceState.getString("text");
            editTextHint = savedInstanceState.getString("editTextHint");
            editText.setText(editTextHint);
            todoList = savedInstanceState.getParcelableArrayList("todolist");
        }
    }

    private void setOrientation(){
        ConstraintLayout view = (ConstraintLayout)findViewById(R.id.main_layout);
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            view.setBackgroundResource (R.drawable.flowers_landscape);
        } else {
            view.setBackgroundResource (R.drawable.flowers);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("text",text);
        savedInstanceState.putString("editTextHint", editTextHint);
        savedInstanceState.putParcelableArrayList("todolist",todoList);
        // etc.
    }

    @Override
    public void onTodoClick(int pos, ImageView imageView) {
        TODO todoItem = todoList.get(pos);
        if (todoItem.isDone == 0)
        {
            todoItem.isDone = 1;
            imageView.setBackgroundResource(R.drawable.done);
            Context context = getApplicationContext();
            CharSequence text = "TODO "+todoItem.description+" is now DONE. BOOM!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

}
