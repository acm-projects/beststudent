package com.example.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ClassViewHolder> {

    private List<SchoolClass> itemList;
    private Context context;

    public ClassListAdapter(Context context, List<SchoolClass> itemList)
    {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ClassViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_classlist, parent, false);
        return new ClassViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(ClassViewHolder holder, int position)
    {
        holder.className.setText(itemList.get(position).getClassName());
        holder.classTime.setText(itemList.get(position).getClassTime());
        if (itemList.get(position).getRoom().isEmpty())
            holder.room.setVisibility(View.GONE);
        else
            holder.room.setText(itemList.get(position).getRoom());

        if (itemList.get(position).getProfessor().isEmpty())
            holder.professor.setVisibility(View.GONE);
        else
            holder.professor.setText(itemList.get(position).getProfessor());

        if (itemList.get(position).getOffice().isEmpty())
            holder.office.setVisibility(View.GONE);
        else
            holder.office.setText(itemList.get(position).getOffice());

        if (itemList.get(position).getOfficeHours().isEmpty())
            holder.officeHours.setVisibility(View.GONE);
        else
            holder.officeHours.setText(itemList.get(position).getOfficeHours());
    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }

    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        // all views to change for each class
        public TextView className;
        public TextView room;
        public TextView classTime;
        public TextView professor;
        public TextView office;
        public TextView officeHours;

        public ClassViewHolder(View v) {
            super(v);
            className = itemView.findViewById(R.id.ClassName);
            room = itemView.findViewById(R.id.RoomNumber);
            classTime = itemView.findViewById(R.id.ClassTime);
            professor = itemView.findViewById(R.id.Professor);
            office = itemView.findViewById(R.id.OfficeRoom);
            officeHours = itemView.findViewById(R.id.OfficeHours);
        }
    }
}
