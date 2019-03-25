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
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        Log.d(TAG, "onBindViewHolder: called.");
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.taskName.setText(taskList.get(position).getTaskName());

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + taskList.get(position));

                Toast.makeText(mContext, taskList.get(position).getTaskName(), Toast.LENGTH_SHORT).show();
            }
        });

        SimpleDateFormat format1 = new SimpleDateFormat("EEE MMMM dd, yyyy h:mm a", Locale.US);
        holder.date.setText(format1.format(taskList.get(position).getDueDate().getTime()));
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
        // each data item is just a string in this case
        public TextView taskName;
        public TextView date;
        public LinearLayout parentLayout;

        public MyViewHolder(View v) {
            super(v);
            taskName = itemView.findViewById(R.id.task_name);
            date = itemView.findViewById(R.id.due_date);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
