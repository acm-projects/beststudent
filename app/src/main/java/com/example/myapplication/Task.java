package com.example.myapplication;

import java.time.Duration;
import java.util.Calendar;

public class Task {
    private String taskName;
    private String dueDate;
    private String className;
    private String notes;
    private Duration duration;
    private int priority;
    private boolean isComplete;


    public static final int DEFAULT_PRIORITY = 3;
    public static final String NO_CLASS = "No Class";

    public Task(String name, String date, String cl, String note, Duration d, int priorLvl) {
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

    public void setDuration(Duration d){
        duration = d;
    }

    public void setPriority(int p){
        priority = p;
    }

    public void setStatus(boolean b){
        isComplete = b;
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

    public Duration getDuration(){
        return duration;
    }

    public int getPriority(){
        return priority;
    }

    public boolean isComplete() {
        return isComplete;
    }
}
