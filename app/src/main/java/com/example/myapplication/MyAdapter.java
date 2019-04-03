package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Recyclerview adapter
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "MyAdapter";
    private ArrayList<Task> taskList;
    private Context mContext;


    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Task> myDataset, Context context) {
        taskList = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_listitem, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // tag for debug
        Log.d(TAG, "onBindViewHolder: called.");
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // sets task name
        holder.taskName.setText(taskList.get(position).getTaskName());

        // displays task name when task is clicked on
        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + taskList.get(position));
                Toast.makeText(mContext, taskList.get(position).getTaskName(), Toast.LENGTH_SHORT).show();
            }
        });

        // sets due date and formats
        holder.date.setText(taskList.get(position).getDueDate());

        // sets notes if there are any
        String notes = taskList.get(position).getNotes();
        if (notes.isEmpty()){
            holder.note.setVisibility(View.GONE);
        } else {
            holder.note.setText(notes);
        }

        // sets class name
        holder.className.setText(taskList.get(position).getClassName());

        // sets priority level
        holder.priority.setText(String.valueOf(taskList.get(position).getPriority()));

        // set duration
        holder.duration.setText(taskList.get(position).getDuration());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return taskList.size();
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // all needs views and layouts to change for each task
        public TextView taskName;
        public TextView date;
        public TextView className;
        public TextView note;
        public TextView duration;
        public TextView priority;
        public LinearLayout parentLayout;

        public MyViewHolder(View v) {
            super(v);
            taskName = itemView.findViewById(R.id.task_name);
            date = itemView.findViewById(R.id.due_date);
            className = itemView.findViewById(R.id.class_name);
            note = itemView.findViewById(R.id.notes);
            duration = itemView.findViewById(R.id.duration);
            priority = itemView.findViewById(R.id.priority_level);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
