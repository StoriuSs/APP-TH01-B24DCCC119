package com.example.learnandroid;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.learnandroid.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Student> studentList;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Quản Lý Sinh Viên");
        }

        // Initialize Student List & Adapter
        studentList = new ArrayList<>();
        adapter = new StudentAdapter(studentList);

        // Add sample data
        addSampleData();

        binding.contentMain.rvStudents.setLayoutManager(new LinearLayoutManager(this));
        binding.contentMain.rvStudents.setAdapter(adapter);

        // Handle Add Student Button Click
        binding.contentMain.btnAddStudent.setOnClickListener(v -> addStudent());

        // Handle Clear Inputs Button Click
        binding.contentMain.btnClear.setOnClickListener(v -> clearInputs());

        // Handle Item Click (Select to fill or option to delete)
        adapter.setOnItemClickListener(this::showStudentOptionsDialog);

        // Hide FAB
        binding.fab.hide();
    }

    private void addSampleData() {
        studentList.add(new Student("SV01", "Nguyễn Văn A", 20, 8.8));
        studentList.add(new Student("SV02", "Trần Thị B", 21, 7.5));
        studentList.add(new Student("SV03", "Lê Văn C", 19, 4.8));
        adapter.notifyDataSetChanged();
    }

    private void addStudent() {
        String id = getStringValue(binding.contentMain.edtStudentId);
        String name = getStringValue(binding.contentMain.edtStudentName);
        String ageStr = getStringValue(binding.contentMain.edtStudentAge);
        String gpaStr = getStringValue(binding.contentMain.edtStudentGpa);

        // Validation
        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "Vui lòng nhập mã sinh viên!", Toast.LENGTH_SHORT).show();
            binding.contentMain.edtStudentId.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Vui lòng nhập họ tên!", Toast.LENGTH_SHORT).show();
            binding.contentMain.edtStudentName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(ageStr)) {
            Toast.makeText(this, "Vui lòng nhập tuổi!", Toast.LENGTH_SHORT).show();
            binding.contentMain.edtStudentAge.requestFocus();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age <= 0 || age > 100) {
                Toast.makeText(this, "Tuổi phải từ 1 đến 100!", Toast.LENGTH_SHORT).show();
                binding.contentMain.edtStudentAge.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Tuổi không hợp lệ!", Toast.LENGTH_SHORT).show();
            binding.contentMain.edtStudentAge.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(gpaStr)) {
            Toast.makeText(this, "Vui lòng nhập điểm GPA!", Toast.LENGTH_SHORT).show();
            binding.contentMain.edtStudentGpa.requestFocus();
            return;
        }

        double gpa;
        try {
            gpa = Double.parseDouble(gpaStr);
            if (gpa < 0.0 || gpa > 10.0) {
                Toast.makeText(this, "GPA phải từ 0.0 đến 10.0!", Toast.LENGTH_SHORT).show();
                binding.contentMain.edtStudentGpa.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "GPA không hợp lệ!", Toast.LENGTH_SHORT).show();
            binding.contentMain.edtStudentGpa.requestFocus();
            return;
        }

        // Check duplicate student ID
        for (Student s : studentList) {
            if (s.getId().equalsIgnoreCase(id)) {
                Toast.makeText(this, "Mã sinh viên '" + id + "' đã tồn tại!", Toast.LENGTH_SHORT).show();
                binding.contentMain.edtStudentId.requestFocus();
                return;
            }
        }

        // Create Student Object & Add to ArrayList
        Student newStudent = new Student(id, name, age, gpa);
        studentList.add(newStudent);
        adapter.notifyItemInserted(studentList.size() - 1);
        binding.contentMain.rvStudents.scrollToPosition(studentList.size() - 1);

        Toast.makeText(this, "Thêm sinh viên thành công!\n" + newStudent.displayInfo(), Toast.LENGTH_LONG).show();

        clearInputs();
    }

    private void clearInputs() {
        if (binding.contentMain.edtStudentId.getText() != null) binding.contentMain.edtStudentId.setText("");
        if (binding.contentMain.edtStudentName.getText() != null) binding.contentMain.edtStudentName.setText("");
        if (binding.contentMain.edtStudentAge.getText() != null) binding.contentMain.edtStudentAge.setText("");
        if (binding.contentMain.edtStudentGpa.getText() != null) binding.contentMain.edtStudentGpa.setText("");
        binding.contentMain.edtStudentId.clearFocus();
        binding.contentMain.edtStudentName.clearFocus();
        binding.contentMain.edtStudentAge.clearFocus();
        binding.contentMain.edtStudentGpa.clearFocus();
    }

    private String getStringValue(android.widget.EditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    private void showStudentOptionsDialog(Student student, int position) {
        String[] options = {"Điền vào form nhập", "Xóa sinh viên", "Hủy"};
        new AlertDialog.Builder(this)
                .setTitle("Sinh viên: " + student.getName())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) { // Fill form
                        binding.contentMain.edtStudentId.setText(student.getId());
                        binding.contentMain.edtStudentName.setText(student.getName());
                        binding.contentMain.edtStudentAge.setText(String.valueOf(student.getAge()));
                        binding.contentMain.edtStudentGpa.setText(String.valueOf(student.getGpa()));
                    } else if (which == 1) { // Delete student
                        studentList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, studentList.size());
                        Toast.makeText(MainActivity.this, "Đã xóa sinh viên " + student.getName(), Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}
