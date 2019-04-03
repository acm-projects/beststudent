package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Activity for to do list
 */
public class ToDoActivity extends LoginActivity {
    // tag for debug
    private static final String TAG = "ToDoActivity";

    // list of tasks
    private ArrayList<Task> myDataset;

    // recyclerview to show tasks
    private RecyclerView recyclerView;
    protected RecyclerView.Adapter mAdapter;

    // Firebase variables
    protected DatabaseReference mTasksDatabaseRef;
    protected ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo);

        // sets toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setSubtitle(R.string.to_do);

        // set recycler view
        recyclerView = findViewById(R.id.ToDoList);

        // initialize database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mTasksDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("tasks");
        mTasksDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // prevent multiple instances of same data
                myDataset = new ArrayList<>();
                // get all the data in database
                for(DataSnapshot datass: dataSnapshot.getChildren()) {
                    Task tempTask = datass.getValue(Task.class);
                    myDataset.add(tempTask);
                }
                // specify an adapter (see also next example)
                mAdapter = new MyAdapter(myDataset, ToDoActivity.this);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // sets floating button to go to add task page when clicked
        FloatingActionButton button = findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(ToDoActivity.this, AddActivity.class));
            }
        });

    }
}
