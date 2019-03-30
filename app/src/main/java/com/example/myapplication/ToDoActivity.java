package com.example.myapplication;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Duration;
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
    private ArrayList<Task> myDataset = new ArrayList<>();

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

        // initialize database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mTasksDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("tasks");

        // sets toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setSubtitle(R.string.to_do);

        // initializes a list of tasks for testing purposes
        initTasks();
        recyclerView = findViewById(R.id.ToDoList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset, this);
        recyclerView.setAdapter(mAdapter);

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

    /**
     * initializes some task for testing
     */
    private void initTasks(){
        /*Calendar cal1 = new GregorianCalendar(2019, Calendar.MARCH, 30, 16, 0);
        Calendar cal2 = new GregorianCalendar(2019, Calendar.MARCH, 31, 16, 0);
        Calendar cal3 = new GregorianCalendar(2019, Calendar.APRIL, 2, 12, 30);
        Calendar cal4 = new GregorianCalendar(2019, Calendar.APRIL, 3, 8, 15);
        Calendar cal5 = new GregorianCalendar(2019, Calendar.APRIL, 7, 16, 0);
        Calendar cal6 = new GregorianCalendar(2019, Calendar.APRIL, 13, 16, 0);
        Calendar cal7 = new GregorianCalendar(2019, Calendar.APRIL, 16, 13, 30);
        Calendar cal8 = new GregorianCalendar(2019, Calendar.APRIL, 19, 20, 15);

        myDataset.add(new Task("1 Do laundry", cal1, "", "", Duration.ofSeconds(0), 5));
        myDataset.add(new Task("2 Math WS", cal2, "Discrete Math", "", Duration.ofSeconds(0), 2));
        myDataset.add(new Task("3 Gregorian Chant", cal3, "Choir", "", Duration.ofMinutes(60), 1));
        myDataset.add(new Task("4 Drink water", cal4, "Life", "", Duration.ofSeconds(0), 1));
        myDataset.add(new Task("5 GOV EXAM", cal5, "2306 Govt", "", Duration.ofSeconds(0), 1));
        myDataset.add(new Task("6 Sleep", cal6, "", "At least 3 hours", Duration.ofMinutes(240), 1));
        myDataset.add(new Task("7 Buy oranges", cal7, "Chores", "", Duration.ofSeconds(0), 5));
        myDataset.add(new Task("8 Draw Temoc", cal8, "UTD", "Remember to bring paint", Duration.ofSeconds(0), 3));
        myDataset.add(new Task("9 Praise Enarc", cal1, "Life", "", Duration.ofSeconds(0), 1));
        myDataset.add(new Task("10 Blood sacrifice", cal1, "", "", Duration.ofMinutes(30), 2));
        myDataset.add(new Task("11 Dance club", cal1, "DFC", "We're up all night 'til the sun\n" +
                "We're up all night to get some\n" +
                "We're up all night for good fun\n" +
                "We're up all night to get lucky", Duration.ofSeconds(0), 4));
        myDataset.add(new Task("12 Find outfit", cal1, "", "", Duration.ofSeconds(0), 5));
        myDataset.add(new Task("13 ACM Dance Party", cal1, "ACM", "", Duration.ofMinutes(120), 2));
        myDataset.add(new Task("14 ???", cal1, "???", "???", Duration.ofHours(75), 1));
        myDataset.add(new Task("15 Profit", cal8, "", "", Duration.ofSeconds(0), 5));*/

    }
}
