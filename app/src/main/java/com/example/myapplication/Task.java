package com.example.myapplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Task implements Comparable<Task>{
    private String taskName;
    private String dueDate;
    private String className;
    private String notes;
    private String duration;
    private String key;
    private int priority;
    private boolean isComplete;

    // Firebase variables
    private DatabaseReference mCompletedDatabaseRef;
    private DatabaseReference mTasksDatabaseRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser user;

    // constants
    public static final int DEFAULT_PRIORITY = 3;
    public static final String NO_CLASS = "No Class";

    // constructors
    public Task() {}
    public Task(String name, String date, String cl, String note, String d, int priorLvl, String keyIn) {
        taskName = name;
        dueDate = date;
        if (cl.isEmpty())
            className = NO_CLASS;
        else
            className = cl;
        notes = note;
        duration = d;
        priority = priorLvl;
        isComplete = false;
        key = keyIn;
    }

    // setters
    public void setTaskName(String name){
        taskName = name;
    }

    public void setDueDate(String date){
        dueDate = date;
    }

    public void setClassName(String name){
        className = name;
    }

    public void setNotes(String note){
        notes = note;
    }

    public void setDuration(String d){
        duration = d;
    }

    public void setPriority(int p){
        priority = p;
    }

    public void setStatus(){
        isComplete = !isComplete;
    }

    public void setKey(String newKey) { key = newKey; }

    // getters
    public String getTaskName(){
        return taskName;
    }

    public String getDueDate(){
        return dueDate;
    }

    public String getClassName(){
        return className;
    }

    public String getNotes(){
        return notes;
    }

    public String getDuration(){
        return duration;
    }

    public int getPriority(){
        return priority;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public String getKey() { return key; }

    @Override
    public int compareTo(Task obj) {
        // get the due date in Date format
        SimpleDateFormat sdformat = new SimpleDateFormat("EEE MMMM dd, yyyy h:mm a", Locale.US);
        try {
            Date date = sdformat.parse(getDueDate());
            Date objDate = sdformat.parse(obj.getDueDate());
            return date.compareTo(objDate);
        }
        catch (ParseException e) {
            e.getMessage();
        }
        return 0;
    }

    public void deleteTask(String keyIn) {
        // initialize database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mTasksDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("tasks");
        mTasksDatabaseRef.child(keyIn).removeValue();
    }

    public void deleteCompletedTask(String keyIn) {
        // initialize database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mCompletedDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("completed tasks");
        mCompletedDatabaseRef.child(keyIn).removeValue();
    }
}
