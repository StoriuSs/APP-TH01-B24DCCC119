package com.example.learnandroid;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private final ArrayList<Student> studentList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Student student, int position);
    }

    public StudentAdapter(ArrayList<Student> studentList) {
        this.studentList = studentList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvStudentId.setText("Mã SV: " + student.getId());
        holder.tvStudentName.setText(student.getName());
        holder.tvStudentAge.setText("Tuổi: " + student.getAge());
        holder.tvStudentGpa.setText("GPA: " + student.getGpa());

        String rank = student.getRank();
        holder.tvRank.setText(rank);

        // Dynamic background color for Rank badge
        int color;
        switch (rank) {
            case "Giỏi":
                color = Color.parseColor("#4CAF50"); // Green
                break;
            case "Khá":
                color = Color.parseColor("#2196F3"); // Blue
                break;
            case "Trung bình":
                color = Color.parseColor("#FF9800"); // Orange
                break;
            default: // Yếu
                color = Color.parseColor("#F44336"); // Red
                break;
        }

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(24f);
        drawable.setColor(color);
        holder.tvRank.setBackground(drawable);

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (onItemClickListener != null && pos != RecyclerView.NO_POSITION) {
                onItemClickListener.onItemClick(student, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentId;
        TextView tvStudentName;
        TextView tvStudentAge;
        TextView tvStudentGpa;
        TextView tvRank;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentAge = itemView.findViewById(R.id.tvStudentAge);
            tvStudentGpa = itemView.findViewById(R.id.tvStudentGpa);
            tvRank = itemView.findViewById(R.id.tvRank);
        }
    }
}
