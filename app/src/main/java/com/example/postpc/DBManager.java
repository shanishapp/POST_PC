package com.example.postpc;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class DBManager implements TodoAdapter.OnTodoListener  {

    private ArrayList<TODO> todoList;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    public boolean todoListInitialized;
    private final Query query;
    private static DBManager instance = new DBManager();
    public TodoAdapter adapter = null;
    private Context app = null;
    private static boolean firstTimeLaunch = true;

    public static DBManager getInstance()
    {
        firstTimeLaunch = true;
        return instance;
    }

    private DBManager()
    {
        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("todos");
        this.query = collectionReference;
        todoList = new ArrayList<>();
        todoListInitialized = false;
        adapter = new TodoAdapter(todoList,this);
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("error", "Listen failed.", e);
                    return;
                }
                else
                {
                    todoList.clear();
                    for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots)
                    {
                        TODO todo = snapshot.toObject(TODO.class);
                        todoList.add(todo);
                    }
                    adapter.notifyDataSetChanged();
                    if (firstTimeLaunch)
                    {
                        Log.d("sizeoftodolist",String.valueOf(todoList.size()));
                        firstTimeLaunch = false;
                    }
                }

            }
        });


    }


    public void addTodo(final TODO todo)
    {
        todoList.add(todo);
        adapter.notifyDataSetChanged();

        collectionReference.add(todo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                todo.id = documentReference.getId();
                collectionReference.document(todo.id).update("id",todo.id);
            }
        });
    }

    public void markTodoAsDone(String todoId)
    {
        collectionReference.document(todoId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                TODO todo = documentSnapshot.toObject(TODO.class);
                todoList.remove(todo);
                todo.isDone = true;
                todoList.add(todo);
                collectionReference.document(todo.id).update("isDone",true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                adapter.notifyDataSetChanged();
                            }
                        });
            }
        });


    }

    public void deleteTodoForever(String todo)
    {
        collectionReference.document(todo).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public ArrayList<TODO> getAllTodos()
    {
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                todoList.clear();
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    TODO todo = documentSnapshot.toObject(TODO.class);
                    todoList.add(todo);
                }
                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("fail","oh no");
            }
        });
        return todoList;
    }

    public void setContext(Context applicationContext) {
        app = applicationContext;
    }

    @Override
    public void onTodoClick(int pos, ImageView imageView)
    {
        TODO todoItem = todoList.get(pos);
        TodoBoomApp todoApp = (TodoBoomApp) app.getApplicationContext();


        if (todoItem.isDone)
        {
            todoApp.showDoneTodo(todoItem.id,
                                    todoItem.content,
                                    todoItem.creation_timestamp.toDate().toString(),
                                    todoItem.edit_timestamp.toDate().toString());
        }
        else
        {
            todoApp.showTodo(todoItem.id,
                                todoItem.content,
                                todoItem.creation_timestamp.toDate().toString(),
                                todoItem.edit_timestamp.toDate().toString());
        }
    }

    public void setTodoData(String todoId,String newContent,Timestamp timestamp) {

        collectionReference.document(todoId).update("content",newContent,"edit_timestamp", timestamp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    public void markTodoAsUnDone(String todoId) {
        collectionReference.document(todoId).update("isDone", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}



