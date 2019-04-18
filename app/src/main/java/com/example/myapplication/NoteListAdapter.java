package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {

    private List<Note> itemList;
    private Context context;

    public NoteListAdapter(Context context, List<Note> itemList)
    {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_noteitem, parent, false);
        return new NoteViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, final int position)
    {
        holder.noteName.setText(itemList.get(position).getTitle());
        holder.note.setText(itemList.get(position).getNotes());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final String noteTitle = itemList.get(position).getTitle();
                final String noteKey = itemList.get(position).getNoteKey();
                Snackbar delete = Snackbar
                        .make(v, "Delete " + noteTitle + "?", Snackbar.LENGTH_LONG)
                        .setAction("DELETE", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                itemList.get(position).deleteNote(noteKey);
                            }
                        });
                delete.setActionTextColor(Color.RED);
                delete.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }

    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        // all views to change for each class
        public TextView noteName;
        public TextView note;

        public NoteViewHolder(View v) {
            super(v);
            noteName = itemView.findViewById(R.id.text_note_title);
            note = itemView.findViewById(R.id.text_note_body);
        }
    }
}
