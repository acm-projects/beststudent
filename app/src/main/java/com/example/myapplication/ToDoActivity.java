package com.example.myapplication;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ToDoActivity extends AppCompatActivity {
    private static final String TAG = "ToDoActivity";

    private ArrayList<Task> myDataset = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Log.d(TAG,"onCreate: started.");
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

        FloatingActionButton button = findViewById(R.id.addButton);
        button.setFocusable(true);
        button.setFocusableInTouchMode(true);
        button.requestFocus();
    }

    private void initTasks(){
        Calendar cal1 = new GregorianCalendar(2019, Calendar.MARCH, 30, 16, 0);
        myDataset.add(new Task("1 Do laundry", cal1));
        Calendar cal2 = new GregorianCalendar(2019, Calendar.MARCH, 31, 16, 0);
        myDataset.add(new Task("2 Math WS", cal2));
        Calendar cal3 = new GregorianCalendar(2019, Calendar.APRIL, 2, 12, 30);
        myDataset.add(new Task("3 Gregorian Chant", cal3));
        Calendar cal4 = new GregorianCalendar(2019, Calendar.APRIL, 3, 8, 15);
        myDataset.add(new Task("4 Drink water", cal4));
        Calendar cal5 = new GregorianCalendar(2019, Calendar.APRIL, 7, 16, 0);
        myDataset.add(new Task("5 GOV EXAM", cal5));
        Calendar cal6 = new GregorianCalendar(2019, Calendar.APRIL, 13, 16, 0);
        myDataset.add(new Task("6 Sleep", cal6));
        Calendar cal7 = new GregorianCalendar(2019, Calendar.APRIL, 16, 13, 30);
        myDataset.add(new Task("7 Buy oranges", cal7));
        Calendar cal8 = new GregorianCalendar(2019, Calendar.APRIL, 19, 20, 15);
        myDataset.add(new Task("8 Draw Temoc", cal8));
        myDataset.add(new Task("9 Praise Enarc", cal1));
        myDataset.add(new Task("10 Blood sacrifice", cal1));
        myDataset.add(new Task("11 Dance club", cal1));
        myDataset.add(new Task("12 Find outfit", cal1));
        myDataset.add(new Task("13 ACM Dance Party", cal1));
        myDataset.add(new Task("14 ???", cal1));
        myDataset.add(new Task("15 Profit", cal8));

    }
}
