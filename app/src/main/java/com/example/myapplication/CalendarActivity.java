package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
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
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    // tag for debugging
    private static final String TAG = "CalendarActivity";

    private Calendar calendar;

    // drawer layout for navigation
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle abdt;
    private Toolbar myToolbar;

    // list of tasks
    private ArrayList<Task> myDataset = new ArrayList<>();

    // recyclerview to show tasks
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // sets toolbar
        setToolbar();
        myToolbar.setSubtitle(R.string.calendar);

        // initializes a list of tasks for testing purposes
        initTasks();
        recyclerView = findViewById(R.id.cal_to_do_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset, this);
        recyclerView.setAdapter(mAdapter);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final TextView date = findViewById(R.id.text_date);
        calendar = Calendar.getInstance();
        final SimpleDateFormat form = new SimpleDateFormat("EEE MMMM dd, yyyy", Locale.US);
        date.setText(form.format((calendar.getTime())));

//        ArrayList<Task> currentTasks = new ArrayList<>();
//        for (int k = 0; k < myDataset.size(); k++) {
//            if (myDataset.get(k).getDueDate().get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
//                && myDataset.get(k).getDueDate().get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
//                && myDataset.get(k).getDueDate().get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
//                currentTasks.add(myDataset.get(k));
//            }
//        }

        CalendarView cal = findViewById(R.id.calendar_view);
        cal.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calendar = new GregorianCalendar(year, month, dayOfMonth );
                date.setText(form.format((calendar.getTime())));
            }
        });
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

    /**
     * initializes some task for testing
     */
    private void initTasks(){
        Calendar cal1 = new GregorianCalendar(2019, Calendar.MARCH, 30, 16, 0);
        Calendar cal2 = new GregorianCalendar(2019, Calendar.MARCH, 31, 16, 0);
        Calendar cal3 = new GregorianCalendar(2019, Calendar.APRIL, 2, 12, 30);
        Calendar cal4 = new GregorianCalendar(2019, Calendar.APRIL, 3, 8, 15);
        Calendar cal5 = new GregorianCalendar(2019, Calendar.APRIL, 7, 16, 0);
        Calendar cal6 = new GregorianCalendar(2019, Calendar.APRIL, 13, 16, 0);
        Calendar cal7 = new GregorianCalendar(2019, Calendar.APRIL, 16, 13, 30);
        Calendar cal8 = new GregorianCalendar(2019, Calendar.APRIL, 19, 20, 15);

        myDataset.add(new Task("Draw Temoc", cal1, "UTD", "Remember to bring paint", Duration.ofSeconds(0), 3));
        myDataset.add(new Task("Praise Enarc", cal2, "Life", "", Duration.ofSeconds(0), 1));
        myDataset.add(new Task("Blood sacrifice", cal3, "", "", Duration.ofMinutes(30), 2));
        myDataset.add(new Task("Dance club", cal4, "DFC", "We're up all night 'til the sun\n" +
                "We're up all night to get some\n" +
                "We're up all night for good fun\n" +
                "We're up all night to get lucky", Duration.ofSeconds(0), 4));
        myDataset.add(new Task("Find outfit", cal5, "", "", Duration.ofSeconds(0), 5));
        myDataset.add(new Task("ACM Dance Party", cal6, "ACM", "", Duration.ofMinutes(120), 2));
        myDataset.add(new Task("???", cal7, "???", "???", Duration.ofHours(75), 1));
        myDataset.add(new Task("Profit", cal8, "", "", Duration.ofSeconds(0), 5));

    }
}
