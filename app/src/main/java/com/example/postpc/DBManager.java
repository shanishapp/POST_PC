package com.example.postpc;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private ArrayList<TODO> todoList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("todos");
    private static DBManager instance = new DBManager();

    public static DBManager getInstance()
    {
        return instance;
    }

    private DBManager()
    {
        todoList = new ArrayList<>();
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if ( e != null)
                {
                    //TODO some failure happened
                }

                for ( QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    TODO todo = documentSnapshot.toObject(TODO.class);
                    if ( ! todoList.contains(todo))
                    {
                        todoList.add(todo);
                    }
                    //TODO notify data changed !
                }
            }
        });
    }

    private void initTodoList()
    {
        //TODO does it needed ???
    }

    public void addTodo(TODO todo,TodoAdapter adapter)
    {
        collectionReference.document(String.valueOf(todo.id))
                .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //TODO successfully added todo
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //TODO failed to add todo
            }
        })
        ;
        todoList.add(todo);
        adapter.notifyItemInserted(todoList.lastIndexOf(todo));
    }

    public void markTodoAsDone(TODO todo)
    {
        //TODO notify ???
        collectionReference.document(String.valueOf(todo.id)).update("isDone",true);
    }

    public void deleteTodoForever(TODO todo)
    {
        collectionReference.document(String.valueOf(todo.id)).delete();
        //TODO notify ???
    }

    public ArrayList<TODO> getAllTodos()
    {
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if ( e != null)
                {
                    //TODO some failure happened
                }

                for ( QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    TODO todo = documentSnapshot.toObject(TODO.class);
                    if ( ! todoList.contains(todo))
                    {
                        todoList.add(todo);
                    }
                    //TODO notify data changed !
                }
            }
        });
        return todoList;
    }
}

