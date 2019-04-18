package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {
    // Firebase variables
    private DatabaseReference mClassesDatabaseRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser user;

    // list of classes
    private ArrayList<Note> myDataset;

    // recyclerview to show classes
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    // drawer layout for navigation
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle abdt;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // sets tool bar
        setToolbar();

        // get recyclerview
        recyclerView = findViewById(R.id.NoteList);
        myDataset = new ArrayList<Note>();
        initTestData();


//        // initialize database
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mClassesDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("classes");
//        mClassesDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // prevent multiple instances of same data
//                myDataset = new ArrayList<>();
//                // get all the data in database
//                for(DataSnapshot data: dataSnapshot.getChildren()) {
//                    SchoolClass tempClass = data.getValue(SchoolClass.class);
//                    myDataset.add(tempClass);
//                }
                // specify an adapter
                mAdapter = new NoteListAdapter(NotesActivity.this, myDataset);
                recyclerView.setAdapter(mAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(false);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     *  Set toolbar and navigation
     */
    public void setToolbar(){
        // sets toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
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
                    startActivity(new Intent(NotesActivity.this, PomodoroActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_change_pomodoro) {
                    startActivity(new Intent(NotesActivity.this, ChooseTimerActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_add_task) {
                    startActivity(new Intent(NotesActivity.this, AddActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_to_do) {
                    startActivity(new Intent(NotesActivity.this, ToDoActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_settings) {
                    startActivity(new Intent(NotesActivity.this, SettingsActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_calendar) {
                    startActivity(new Intent(NotesActivity.this, CalendarActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.sign_out) {
                    startActivity(new Intent(NotesActivity.this, LogoutActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_classes) {
                    startActivity(new Intent(NotesActivity.this, ClassesActivity.class));
                    return true;
                } else if (menuItem.getItemId() == R.id.action_notes) {
                    startActivity(new Intent(NotesActivity.this, NotesActivity.class));
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startAddNote(View view) {
        startActivity(new Intent(NotesActivity.this, AddNoteActivity.class));
    }

    private void initTestData() {
        myDataset.add(new Note("Yes", "This works great"));
        myDataset.add(new Note("OMG", "I love this app"));
        myDataset.add(new Note("", "10/10 would buy"));
        myDataset.add(new Note("", "but\n how\nlong\ndoes\nthis\ngoo\no\no\no\no\n\n\n\n\n\n\n\n\n\nooooo"));
        myDataset.add(new Note("Try Me", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Convallis posuere morbi leo urna molestie at elementum eu facilisis. Fermentum iaculis eu non diam. Enim ut tellus elementum sagittis. Cursus sit amet dictum sit amet justo donec enim. Sit amet luctus venenatis lectus magna. Maecenas volutpat blandit aliquam etiam erat velit scelerisque. Tellus in hac habitasse platea dictumst. Enim sed faucibus turpis in eu. Laoreet id donec ultrices tincidunt arcu non sodales neque sodales. Eros donec ac odio tempor. Faucibus ornare suspendisse sed nisi. Donec et odio pellentesque diam volutpat. Eget egestas purus viverra accumsan in. Tellus in hac habitasse platea. Ultrices eros in cursus turpis. Cras sed felis eget velit aliquet sagittis. Eget nullam non nisi est sit amet facilisis magna. Eget magna fermentum iaculis eu non diam phasellus vestibulum.\n" +
                "\n" +
                "Adipiscing elit ut aliquam purus sit amet luctus venenatis. Nunc consequat interdum varius sit. Ut enim blandit volutpat maecenas volutpat. Tellus orci ac auctor augue mauris. Sit amet nisl purus in mollis. Malesuada proin libero nunc consequat. Amet est placerat in egestas. Nisl nisi scelerisque eu ultrices vitae auctor eu augue. Euismod elementum nisi quis eleifend quam adipiscing. Fusce ut placerat orci nulla. Netus et malesuada fames ac. Sed id semper risus in hendrerit gravida rutrum. Porta lorem mollis aliquam ut porttitor leo a diam. Et malesuada fames ac turpis.\n" +
                "\n" +
                "Mattis molestie a iaculis at erat. Eu turpis egestas pretium aenean pharetra. Felis eget velit aliquet sagittis id consectetur. Commodo nulla facilisi nullam vehicula ipsum a arcu. Mauris nunc congue nisi vitae suscipit tellus. Senectus et netus et malesuada fames ac. Pellentesque id nibh tortor id aliquet lectus. Id cursus metus aliquam eleifend. Scelerisque varius morbi enim nunc faucibus a pellentesque sit. Nisi vitae suscipit tellus mauris a. Magna sit amet purus gravida quis blandit turpis cursus in. Semper eget duis at tellus at urna condimentum mattis pellentesque. Ullamcorper a lacus vestibulum sed arcu non odio euismod. Quis risus sed vulputate odio ut enim. Urna molestie at elementum eu facilisis. Consectetur purus ut faucibus pulvinar elementum integer enim neque volutpat. Sit amet est placerat in egestas. Congue quisque egestas diam in arcu cursus euismod. Vitae nunc sed velit dignissim sodales ut eu sem.\n" +
                "\n" +
                "Sed euismod nisi porta lorem mollis aliquam ut porttitor. Amet porttitor eget dolor morbi non arcu risus quis. Luctus venenatis lectus magna fringilla urna porttitor rhoncus dolor purus. Lacus vel facilisis volutpat est velit egestas. Facilisis mauris sit amet massa vitae tortor condimentum lacinia. Nulla facilisi cras fermentum odio eu feugiat pretium. Diam maecenas ultricies mi eget mauris pharetra et. Tellus mauris a diam maecenas. Nullam non nisi est sit. Ridiculus mus mauris vitae ultricies leo integer malesuada nunc vel. Turpis egestas maecenas pharetra convallis posuere morbi leo. Diam maecenas sed enim ut sem viverra aliquet eget sit. Mi tempus imperdiet nulla malesuada pellentesque elit. Proin fermentum leo vel orci porta non pulvinar. Tristique senectus et netus et malesuada fames. Accumsan in nisl nisi scelerisque eu. Posuere urna nec tincidunt praesent semper feugiat. Gravida cum sociis natoque penatibus et magnis. Nulla aliquet porttitor lacus luctus accumsan tortor posuere.\n" +
                "\n" +
                "Sollicitudin nibh sit amet commodo. Et ligula ullamcorper malesuada proin libero nunc consequat interdum. Urna nec tincidunt praesent semper feugiat nibh sed. Tristique senectus et netus et malesuada fames. Tempus urna et pharetra pharetra massa massa ultricies mi quis. Turpis egestas maecenas pharetra convallis. Nibh ipsum consequat nisl vel. Felis imperdiet proin fermentum leo vel. Mattis nunc sed blandit libero volutpat sed cras ornare arcu. Quis hendrerit dolor magna eget est. Vitae justo eget magna fermentum iaculis eu non. Id ornare arcu odio ut sem nulla pharetra diam."));
    }
}
