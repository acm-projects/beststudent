package com.example.myapplication;

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
    private int priority;
    private boolean isComplete;

    // constants
    public static final int DEFAULT_PRIORITY = 3;
    public static final String NO_CLASS = "No Class";

    // constructors
    public Task() {}
    public Task(String name, String date, String cl, String note, String d, int priorLvl) {
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
    }

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
}
