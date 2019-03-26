package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    private static final String TAG = "AddActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setSubtitle(R.string.add_task_event);
    }

    public void addTask(View button) {
        final EditText nameField = (EditText) findViewById(R.id.text_edit_name);
        String name = nameField.getText().toString();

        final EditText dateField = (EditText) findViewById(R.id.text_edit_due_date);
        String date = dateField.getText().toString();

        final EditText notesField = (EditText) findViewById(R.id.text_edit_notes);
        String notes = notesField.getText().toString();
    }

}
