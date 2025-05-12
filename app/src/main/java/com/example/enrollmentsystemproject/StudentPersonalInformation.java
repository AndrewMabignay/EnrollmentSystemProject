package com.example.enrollmentsystemproject;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentPersonalInformation extends AppCompatActivity {
    private EditText studentNo, studentName, studentCourse, studentMajor, studentYearLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_personal_information);

        AddElement();
        Interaction();
    }

    private void AddElement() {
        studentNo = (EditText) findViewById(R.id.StudentNumberPersonalInformation);
        studentName = (EditText) findViewById(R.id.StudentNamePersonalInformation);
        studentCourse = (EditText) findViewById(R.id.StudentCoursePersonalInformation);
        studentMajor = (EditText) findViewById(R.id.StudentMajorPersonalInformation);
        studentYearLevel = (EditText) findViewById(R.id.StudentYearLevelPersonalInformation);
    }

    private FirebaseDatabase database;
    private DatabaseReference studentRef;

    private void Interaction() {
        String userID = getIntent().getStringExtra("userID");
        studentNo.setText(userID);

        DisplayPersonalInformationInteraction();

        studentNo.setEnabled(false);
        studentName.setEnabled(false);
        studentCourse.setEnabled(false);
        studentMajor.setEnabled(false);
        studentYearLevel.setEnabled(false);
    }

    private void DisplayPersonalInformationInteraction() {
        database = FirebaseDatabase.getInstance();
        studentRef = database.getReference("student");

        Query query = studentRef.orderByChild("StudentNumber").equalTo(studentNo.getText().toString());
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
                        break;
                    }
                } else {
                    Toast.makeText(StudentPersonalInformation.this, "Not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StudentPersonalInformation.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
