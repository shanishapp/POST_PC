package com.example.postpc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnTodoListener {

    private String text ;
    private EditText editText = null;
    private String editTextHint = "";
    private ArrayList<TODO> todoList = new ArrayList<>();
    private Button button = null;
    private TODO todo = null;
    private View view = null;
    private Snackbar snackbar = null;
    private SharedPreferences sp = null;
    private TodoAdapter adapter = null;


    final private String error_message = "you can't create an empty TODO item, oh silly!";
    final private int snackbarDuration = Snackbar.LENGTH_SHORT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        initVariables(savedInstanceState);

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

        text = sp.getString("text","");
        editTextHint = sp.getString("editTextHint","");
        editText.setText(editTextHint);
        String todoString = sp.getString("todoList","");
        String[] todoStringList = todoString.split("-");
        String checkString = sp.getString("checkList","");
        String[] checkStringList = checkString.split("-");
        todoList = new ArrayList<>();

        if (!todoString.equals(""))
        {
            for (int i = 0; i < todoStringList.length; i++)
            {
                if (checkStringList[i].equals("yes"))
                {
                    todoList.add(new TODO(todoStringList[i],1));
                }
                else
                {
                    todoList.add(new TODO(todoStringList[i],0));
                }
            }
        }

//        if (savedInstanceState != null) {
//            text = savedInstanceState.getString("text");
//            editTextHint = savedInstanceState.getString("editTextHint");
//            editText.setText(editTextHint);
//            todoList = savedInstanceState.getParcelableArrayList("todolist");
//        }
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
//        savedInstanceState.putString("text",text);
//        savedInstanceState.putString("editTextHint", editTextHint);
//        savedInstanceState.putParcelableArrayList("todolist",todoList);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("text", text); 	// insert an int
        editor.putString("editTextHint", editTextHint); 	// insert a boolean
        String todoString="";
        String checkString="";
        for ( TODO todo: todoList)
        {
            todoString = todoString.concat(todo.description+"-");
            if (todo.isDone == 0)
            {
                checkString = checkString.concat("no-");
            }
            else
            {
                checkString = checkString.concat("yes-");
            }
        }
        editor.putString("todoList", todoString); 	// insert a string
        editor.putString("checkList",checkString);

        editor.apply();		// we ALWAYS need to call “apply” after editing!
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

    @Override
    public void onLongTodoClick(int pos)
    {
        if (todoList.size() > pos)
        {
            todoList.remove(pos);
            adapter.notifyDataSetChanged();
        }
    }

}
