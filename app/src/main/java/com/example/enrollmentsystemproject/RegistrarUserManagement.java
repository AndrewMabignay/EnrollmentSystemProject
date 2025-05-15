package com.example.enrollmentsystemproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.TreeMap;

public class RegistrarUserManagement extends AppCompatActivity {
    private EditText studentNo, studentName, studentCourse, studentMajor, studentYearLevel;
    private Button addUser, updateUser, saveUser, searchUser;
    private String crudPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_user_management);

        AddElement();
        Interaction();
    }

    private void AddElement() {
        studentNo = (EditText) findViewById(R.id.StudentNumber);
        studentName = (EditText) findViewById(R.id.StudentName);
        studentCourse = (EditText) findViewById(R.id.StudentCourse);
        studentMajor = (EditText) findViewById(R.id.StudentMajor);
        studentYearLevel = (EditText) findViewById(R.id.StudentYearLevel);

        addUser = (Button) findViewById(R.id.AddUser);
        updateUser = (Button) findViewById(R.id.UpdateUser);
        saveUser = (Button) findViewById(R.id.SaveUser);
        searchUser = (Button) findViewById(R.id.SearchUser);
    }

    FirebaseDatabase database;
    DatabaseReference studentReference, userReference;

    private void Interaction() {
        // SECURITY FIELDS
        SecurityInteraction(true, false, false, false, false, true, false, false, true);

        addUser.setOnClickListener(e -> {
            crudPosition = "Add User";

            // SECURITY FEATURES
            studentNo.setEnabled(true);
            studentName.setEnabled(true);
            studentCourse.setEnabled(true);
            studentMajor.setEnabled(true);
            studentYearLevel.setEnabled(true);

            addUser.setEnabled(false);
            updateUser.setEnabled(false);
            searchUser.setEnabled(false);
            saveUser.setEnabled(true);
        });

        searchUser.setOnClickListener(e -> {
            SearchUserInteraction(studentNo.getText().toString());
        });

        saveUser.setOnClickListener(e -> {
            String studentNumberText = studentNo.getText().toString();
            String studentNameText = studentName.getText().toString();
            String studentCourseText = studentCourse.getText().toString();
            String studentMajorText = studentMajor.getText().toString();
            String studentYearLevelText = studentYearLevel.getText().toString();

            switch (crudPosition) {
                case "Add User":
                    AddUserInteraction(studentNumberText, studentNameText, studentCourseText, studentMajorText, studentYearLevelText);
                    break;
                case "Update User":
                    EditUserInteraction(studentNumberText, studentNameText, studentCourseText, studentMajorText, studentYearLevelText);
                    break;
            }
        });

        updateUser.setOnClickListener(e -> {
            crudPosition = "Update User";
            SecurityInteraction(true, true, true, true, true, false, false, true, false);
        });
    }

    private void AddUserInteraction(String studentNoField, String studentNameField, String studentCourseField, String studentMajorField, String studentYearLevelField) {
        crudPosition = "";

        if (studentNoField.isEmpty()) {
            studentNo.setError("Must not be empty.");
            return;
        }

        if (studentNameField.isEmpty()) {
            studentName.setError("Must not be empty.");
            return;
        }

        if (studentCourseField.isEmpty()) {
            studentCourse.setError("Must not be empty.");
            return;
        }

        if (studentMajorField.isEmpty()) {
            studentMajor.setError("Must not be empty.");
            return;
        }

        if (studentYearLevelField.isEmpty()) {
            studentYearLevel.setError("Must not be empty.");
            return;
        }

        // STUDENT RECORDS
        TreeMap<String, Object> studentData = new TreeMap<>();
        studentData.put("StudentNumber", studentNoField);
        studentData.put("StudentName", studentNameField);
        studentData.put("StudentCourse", studentCourseField);
        studentData.put("StudentMajor", studentMajorField);
        studentData.put("StudentYearLevel", studentYearLevelField);

        database = FirebaseDatabase.getInstance();
        studentReference = database.getReference("student");

        // CHECK FOR DUPLICATE STUDENTNUMBER (MAINTENANCE)




        //

        String key = studentReference.push().getKey();
        studentData.put("Key", key);

        studentReference.child(key).setValue(studentData).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(RegistrarUserManagement.this, "Failed to add user", Toast.LENGTH_SHORT).show();
            }
        });

        // STUDENT ACCOUNT
        TreeMap<String, Object> userData = new TreeMap<>();

        userData.put("Username", studentNoField);
        userData.put("Password", studentNoField);
        userData.put("Role", "Student");

        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("user");

        key = userReference.push().getKey();
        userData.put("Key", key);

        userReference.child(key).setValue(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RegistrarUserManagement.this, "New Student and User Added", Toast.LENGTH_SHORT).show();
                studentNo.getText().clear();
                studentName.getText().clear();
                studentCourse.getText().clear();
                studentMajor.getText().clear();
                studentYearLevel.getText().clear();

                SecurityInteraction(true, false, false, false, false, true, false, false, true);
            } else {
                Toast.makeText(RegistrarUserManagement.this, "Failed to add user", Toast.LENGTH_SHORT).show();
            }
        });

        addUser.setEnabled(true);
        searchUser.setEnabled(true);
        updateUser.setEnabled(false);
        saveUser.setEnabled(false);
    }

    private void SearchUserInteraction(String studentNoField) {
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("student");

        Query query = userReference.orderByChild("StudentNumber").equalTo(studentNoField);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String studentNameText = snapshot.child("StudentName").getValue(String.class);
                        String studentCourseText = snapshot.child("StudentCourse").getValue(String.class);
                        String studentMajorText = snapshot.child("StudentMajor").getValue(String.class);
                        String studentYearLevelText = snapshot.child("StudentYearLevel").getValue(String.class);

                        studentName.setText(studentNameText != null ? studentNameText : "");
                        studentCourse.setText(studentCourseText != null ? studentCourseText : "");
                        studentMajor.setText(studentMajorText != null ? studentMajorText : "");
                        studentYearLevel.setText(studentYearLevelText != null ? studentYearLevelText : "");
                        Toast.makeText(RegistrarUserManagement.this, "Student found", Toast.LENGTH_SHORT).show();
                        SecurityInteraction(true, false, false, false, false, true, true, false, true);
                        break;
                    }
                } else {
                    studentName.setText("");
                    studentCourse.setText("");
                    studentMajor.setText("");
                    studentYearLevel.setText("");
                    SecurityInteraction(true, false, false, false, false, true, false, false, true);
                    Toast.makeText(RegistrarUserManagement.this, "Student not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RegistrarUserManagement.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void EditUserInteraction(String studentNoField, String studentNameField, String studentCourseField, String studentMajorField, String studentYearField) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("student");

        Query query = myRef.orderByChild("StudentNumber").equalTo(studentNoField);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().child("StudentName").setValue(studentNameField);
                        snapshot.getRef().child("StudentCourse").setValue(studentCourseField);
                        snapshot.getRef().child("StudentMajor").setValue(studentMajorField);
                        snapshot.getRef().child("StudentYearLevel").setValue(studentYearField);
                    }

                    studentNo.getText().clear();
                    studentName.getText().clear();
                    studentCourse.getText().clear();
                    studentMajor.getText().clear();
                    studentYearLevel.getText().clear();

                    SecurityInteraction(true, false, false, false, false, true, false, false, true);

                    Toast.makeText(RegistrarUserManagement.this, "Student " + studentNoField + " is Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistrarUserManagement.this, "No record found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RegistrarUserManagement.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SecurityInteraction(boolean no, boolean name, boolean course, boolean major, boolean yearlevel, boolean add, boolean update, boolean save, boolean search) {
        // FIELD SECURITY
        studentNo.setEnabled(no);
        studentName.setEnabled(name);
        studentCourse.setEnabled(course);
        studentMajor.setEnabled(major);
        studentYearLevel.setEnabled(yearlevel);

        // BUTTON SECURITY
        addUser.setEnabled(add);
        updateUser.setEnabled(update);
        saveUser.setEnabled(save);
        searchUser.setEnabled(search);
    }
}
