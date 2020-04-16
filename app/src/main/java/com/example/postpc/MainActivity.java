package com.example.postpc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnTodoListener {

    public TextView textView;
    public String text = "Edit Text";
    public EditText editText = null;
    public String editT = "";
    public Button button = null;
    public ArrayList<TODO> todoList = new ArrayList<>();
    TODO todo = null;
    private View view = null;
    private String error_message = "you can't create an empty TODO item, oh silly!";
    private int duration = Snackbar.LENGTH_SHORT;
    private Snackbar snackbar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        if (savedInstanceState != null) {
            text = savedInstanceState.getString("text");
            editT = savedInstanceState.getString("editT");
            editText.setText(editT);
            todoList = savedInstanceState.getParcelableArrayList("todolist");
        }
        snackbar = Snackbar.make(view, error_message, duration);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        editText.setText(editT);
        setOrientation();



        final TodoAdapter adapter = new TodoAdapter(todoList,this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });


        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.todo_recycler_view);

        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
    }



    private void setOrientation(){
        ConstraintLayout view = findViewById(R.id.main_layout);
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            view.setBackgroundResource (R.drawable.flowers_landscape);
        }
        else {
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
        savedInstanceState.putString("editT",editT);
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
