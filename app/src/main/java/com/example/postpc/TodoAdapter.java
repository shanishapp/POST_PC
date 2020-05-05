package com.example.postpc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{

    private List<TODO> todoList;
    private OnTodoListener mOnTodoListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View todoView = inflater.inflate(R.layout.layout_one_todo, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(todoView,mOnTodoListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        TODO todoItem = todoList.get(position);
        TextView textView = holder.todo;
        ImageView imageView = holder.check;
        textView.setText(todoItem.content);
        if (todoItem.isDone)
        {
            imageView.setBackgroundResource(R.drawable.done);
        }
        else
        {
            imageView.setBackgroundResource(R.drawable.undone);
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public TodoAdapter(List<TODO> todolist, OnTodoListener onTodoListener)
    {
        todoList = todolist;
        this.mOnTodoListener = onTodoListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public TextView todo;
        public ImageView check;
        OnTodoListener onTodoListener;

        public ViewHolder(View view,OnTodoListener onTodoListener){
            super(view);
            todo = view.findViewById(R.id.todo_item);
            check = view.findViewById(R.id.checkbox);
            this.onTodoListener = onTodoListener;
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onTodoListener.onTodoClick(getAdapterPosition(),check);

        }
    }

    public interface OnTodoListener{
        void onTodoClick(int pos,ImageView imageView);
    }









}
