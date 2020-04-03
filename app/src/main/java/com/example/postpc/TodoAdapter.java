package com.example.postpc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView todo;
        public ImageView check;

        public ViewHolder(View view){
            super(view);
            todo = view.findViewById(R.id.todo_item);
            check = view.findViewById(R.id.checkbox);
        }
    }

    private List<TODO> todoList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View todoView = inflater.inflate(R.layout.layout_one_todo, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(todoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        TODO todoItem = todoList.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.todo;
        textView.setText(todoItem.description);
        ImageView imageView = holder.check;
        if (todoItem.isDone)
        {
            imageView.setImageResource(R.drawable.done);
        }
        else{
            imageView.setImageResource(R.drawable.undone);
        }

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public TodoAdapter(List<TODO> todolist)
    {
        todoList = todolist;
    }







}