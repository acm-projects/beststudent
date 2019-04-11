package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Activity for to do list
 */
public class ToDoActivity extends AppCompatActivity {
    // tag for debug
    private static final String TAG = "ToDoActivity";

    // list of tasks
    private ArrayList<Task> myDataset;

    // recyclerview to show tasks
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    // Firebase variables
    private DatabaseReference mTasksDatabaseRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser user;

    // drawer layout for navigation
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle abdt;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        // sets toolbar
        setToolbar();

        // set recycler view
        recyclerView = findViewById(R.id.ToDoList);

        // initialize database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTasksDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("tasks");
        mTasksDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // prevent multiple instances of same data
                myDataset = new ArrayList<>();
                // get all the data in database
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    Task tempTask = data.getValue(Task.class);
                    myDataset.add(tempTask);
                }
                // sort the data in order of due date
                Collections.sort(myDataset);
                // specify an adapter
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
    }

    /**
     *  Set toolbar and navigation
     *
     *
     *
     *
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
                    startActivity(new Intent(ToDoActivity.this, PomodoroActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_change_pomodoro) {
                    startActivity(new Intent(ToDoActivity.this, ChooseTimerActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_add_task) {
                    startActivity(new Intent(ToDoActivity.this, AddActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_to_do) {
                    startActivity(new Intent(ToDoActivity.this, ToDoActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_settings) {
                    startActivity(new Intent(ToDoActivity.this, SettingsActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_calendar) {
                    startActivity(new Intent(ToDoActivity.this, CalendarActivity.class));
                    return true;
                } 
                return true;
            }
        });
    }

    /**
     * sets toolbar buttons
     * @param menu menu of buttons for toolbar
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * Sets what happens when toolbar buttons are clicked
     * @param item button clicked
     * @return true if valid button successfully clicked
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_add_task:
                // User chose the "Settings" item, show the app settings UI...
                startActivity(new Intent(ToDoActivity.this, AddActivity.class));
                return true;
            case R.id.action_sort_priority:
                sortByPriority();
                return true;
            case R.id.action_sort_date:
                sortByDate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sorts list by priority
     */
    public void sortByPriority(){


        setContentView(R.layout.activity_todo);

        // sets toolbar
        setToolbar();

        // set recycler view
        recyclerView = findViewById(R.id.ToDoList);

        // initialize database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTasksDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("tasks");
        mTasksDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // prevent multiple instances of same data
                myDataset = new ArrayList<>();
                // get all the data in database
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    Task tempTask = data.getValue(Task.class);
                    myDataset.add(tempTask);
                }
                // sort the data in order of due date

                for (int i = 0; i < myDataset.size(); i++)
                      {
                          for (int j = myDataset.size() - 1; j > i; j--)
                          {
                              if (myDataset.get(i).getPriority() < myDataset.get(j).getPriority())
                              {

                                  int tmp = myDataset.get(i).getPriority();
                                  myDataset.get(i).setPriority(myDataset.get(j).getPriority());
                                  myDataset.get(j).setPriority(tmp);

                              }

                          }

                      }


                // specify an adapter
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


        Toast.makeText(this, "Sorted by priority!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Sorts list by date
     */
    public void sortByDate(){


        setContentView(R.layout.activity_todo);

        // sets toolbar
        setToolbar();

        // set recycler view
        recyclerView = findViewById(R.id.ToDoList);

        // initialize database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTasksDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("tasks");
        mTasksDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // prevent multiple instances of same data
                myDataset = new ArrayList<>();
                // get all the data in database
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    Task tempTask = data.getValue(Task.class);
                    myDataset.add(tempTask);
                }
                // sort the data in order of due date
                Collections.sort(myDataset);
                // specify an adapter
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




        Toast.makeText(this, "Sorted by date!", Toast.LENGTH_SHORT).show();
    }
}
