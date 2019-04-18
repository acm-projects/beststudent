package com.example.myapplication;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Note {
    private String title;
    private String notes;
    private String noteKey;

    // Firebase variables
    private DatabaseReference mNotesDatabaseRef;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser user;

    public Note() {}

    public Note (String t, String n, String keyIn) {
        title = t;
        notes = n;
        noteKey = keyIn;
    }

    // getters
    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return  notes;
    }

    public String getNoteKey() { return noteKey; }

    // setters
    public void setTitle(String t) {
        title = t;
    }

    public void setNotes(String n) {
        notes = n;
    }

    public void setNoteKey(String keyIn) { noteKey = keyIn; }

    // delete a class
    public void deleteNote(String noteKey) {
        // initialize database
        user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mNotesDatabaseRef = mFirebaseDatabase.getReference().child("users").child(user.getUid()).child("notes");
        mNotesDatabaseRef.child(noteKey).removeValue();
    }
}
