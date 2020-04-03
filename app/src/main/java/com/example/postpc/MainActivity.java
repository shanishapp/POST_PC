package com.example.postpc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public TextView textView;
    public String text = "Edit Text";
    public EditText editText = null;
    public String editT = "";
    public Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState != null) {
            text = savedInstanceState.getString("text");
            editT = savedInstanceState.getString("editT");
        }
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        textView.setText(text);
        button = findViewById(R.id.button);
        editText.setText(editT);
        setOrientation();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = editText.getText().toString();
                textView.setText(text);
                editText.setText("");
            }
        });

        //enable press create when input keyboard on screen
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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

    }


}
