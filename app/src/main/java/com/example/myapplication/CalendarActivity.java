package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CalendarActivity extends AppCompatActivity {

    // tag for debugging
    private static final String TAG = "CalendarActivity";

    private Calendar calendar;

    // drawer layout for navigation
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle abdt;
    private Toolbar myToolbar;

    // list of tasks
    private ArrayList<Task> myDataset;

    // recyclerview to show tasks
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    // Firebase variables
    // Firebase variables
    private DatabaseReference mTasksDatabaseRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser user;

    // get database match with date picked
    String match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // sets toolbar
        setToolbar();

        // sets recycler view
        recyclerView = findViewById(R.id.cal_to_do_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // set calendar format
        final TextView date = findViewById(R.id.text_date);
        calendar = Calendar.getInstance();
        final SimpleDateFormat form = new SimpleDateFormat("EEE MMMM dd, yyyy", Locale.US);
        match = form.format((calendar.getTime()));
        date.setText(match);

        // initialize database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTasksDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("tasks");
        // get today's tasks
        mTasksDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // prevent multiple instances of same data
                myDataset = new ArrayList<>();
                // get all the data in database
                if(dataSnapshot.hasChildren()) {
                    for(DataSnapshot data: dataSnapshot.getChildren()) {
                        Task tempTask = data.getValue(Task.class);
                        if(tempTask.getDueDate().contains(match)) {
                            myDataset.add(tempTask);
                        }
                    }
                    // sort data by due date
                    Collections.sort(myDataset);
                }
                // specify an adapter
                mAdapter = new MyAdapter(myDataset, CalendarActivity.this);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        // update calendar date
        CalendarView cal = findViewById(R.id.calendar_view);
        cal.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calendar = new GregorianCalendar(year, month, dayOfMonth );
                match = form.format((calendar.getTime()));
                date.setText(match);

                // get tasks of days when date is changed
                mTasksDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // prevent multiple instances of same data
                        myDataset = new ArrayList<>();
                        // get all the data in database
                        for(DataSnapshot data: dataSnapshot.getChildren()) {
                            Task tempTask = data.getValue(Task.class);
                            if(tempTask.getDueDate().contains(match)) {
                                myDataset.add(tempTask);
                            }
                        }
                        // sort data by due date
                        Collections.sort(myDataset);
                        // specify an adapter (see also next example)
                        mAdapter = new MyAdapter(myDataset, CalendarActivity.this);
                        recyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     *  Set toolbar and navigation
     */
    public void setToolbar(){
        // sets toolbar
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        abdt = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(abdt);
        abdt.syncState();
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                if (menuItem.getItemId() == R.id.action_pomodoro){
                    startActivity(new Intent(CalendarActivity.this, PomodoroActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_change_pomodoro) {
                    startActivity(new Intent(CalendarActivity.this, ChooseTimerActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_add_task) {
                    startActivity(new Intent(CalendarActivity.this, AddActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_to_do) {
                    startActivity(new Intent(CalendarActivity.this, ToDoActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_settings) {
                    startActivity(new Intent(CalendarActivity.this, SettingsActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_calendar) {
                    startActivity(new Intent(CalendarActivity.this, CalendarActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.sign_out) {
                    startActivity(new Intent(CalendarActivity.this, LoginActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_classes) {
                    startActivity(new Intent(CalendarActivity.this, ClassesActivity.class));
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_add_task:
                // User chose the "Settings" item, show the app settings UI...
                startActivity(new Intent(CalendarActivity.this, AddActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
