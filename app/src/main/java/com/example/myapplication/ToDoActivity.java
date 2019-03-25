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

    private ArrayList<Task> myDataset = new ArrayList<Task>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Log.d(TAG,"onCreate: started.");
        initTasks();
        recyclerView = (RecyclerView) findViewById(R.id.ToDoList);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset, this);
        recyclerView.setAdapter(mAdapter);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton button = (FloatingActionButton)findViewById(R.id.addButton);
        button.setFocusable(true);
        button.setFocusableInTouchMode(true);
        button.requestFocus();
    }

    private void initTasks(){
        Calendar c1 = new GregorianCalendar(2019, Calendar.MARCH, 30, 16, 00);
        myDataset.add(new Task("1 Do laundry", c1));
        Calendar c2 = new GregorianCalendar(2019, Calendar.MARCH, 31, 16, 00);
        myDataset.add(new Task("2 Math WS", c2));
        Calendar c3 = new GregorianCalendar(2019, Calendar.APRIL, 2, 12, 30);
        myDataset.add(new Task("3 MIDTERM", c3));
        Calendar c4 = new GregorianCalendar(2019, Calendar.APRIL, 3, 8, 15);
        myDataset.add(new Task("4 Drink water", c4));
        Calendar c5 = new GregorianCalendar(2019, Calendar.APRIL, 7, 16, 00);
        myDataset.add(new Task("5 GOV EXAM", c5));
        Calendar c6 = new GregorianCalendar(2019, Calendar.APRIL, 13, 16, 00);
        myDataset.add(new Task("6 Sleep", c6));
        Calendar c7 = new GregorianCalendar(2019, Calendar.APRIL, 16, 12, 30);
        myDataset.add(new Task("7 Buy oranges", c7));
        Calendar c8 = new GregorianCalendar(2019, Calendar.APRIL, 19, 8, 15);
        myDataset.add(new Task("8 Draw Temoc", c8));
        myDataset.add(new Task("9 Praise Enarc", c1));
        myDataset.add(new Task("10 Blood sacrifice", c1));
        myDataset.add(new Task("11 Dance club", c1));
        myDataset.add(new Task("12 Find outfit", c1));
        myDataset.add(new Task("13 ACM Dance Party", c1));
        myDataset.add(new Task("14 ???", c1));
        myDataset.add(new Task("14 Profit", c8));

    }
}
