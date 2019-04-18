package com.example.myapplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Note {
    private String title;
    private String notes;

    // Firebase variables
    private DatabaseReference mClassesDatabaseRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser user;

    public Note (String t, String n) {
        title = t;
        notes = n;
    }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return  notes;
    }

    public void setTitle(String t) {
        title = t;
    }

    public void setNotes(String n) {
        notes = n;
    }

    // delete a class
    public void deleteClass(String className) {
//        // initialize database
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mClassesDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("classes");
//        mClassesDatabaseRef.child(className).removeValue();
    }
}
