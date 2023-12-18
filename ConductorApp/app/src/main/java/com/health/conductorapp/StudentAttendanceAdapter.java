package com.health.conductorapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String, String>> attendanceList;

    public StudentAttendanceAdapter(Context context, ArrayList<HashMap<String, String>> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;

    }

    @NonNull
    @Override
    public StudentAttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_status_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAttendanceAdapter.ViewHolder holder, int position) {
        HashMap<String, String> hm = attendanceList.get(position);

        if (hm.get("status").equals("IN")) {
            holder.status.setTextColor(Color.parseColor("#3DDC84"));
        } else {
            holder.status.setTextColor(Color.parseColor("#FF0000"));
        }

        holder.studentName.setText("" + hm.get("student_name"));
        holder.status.setText("" + hm.get("status"));


    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView studentName, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.txt_name);
            status = itemView.findViewById(R.id.txt_status);

        }
    }
}
