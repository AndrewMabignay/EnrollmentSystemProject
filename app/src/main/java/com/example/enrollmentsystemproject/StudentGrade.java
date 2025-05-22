package com.example.enrollmentsystemproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentGrade extends AppCompatActivity {
    private String userID = "";
    private EditText studentNo, subject, prelim, midterm, prefi, finalExam;
    private TextView finalGradeOutput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_grade);

        userID = getIntent().getStringExtra("userID");
        AddElement();
        Interaction();
    }

    private void AddElement() {
        studentNo = (EditText) findViewById(R.id.StudentNumber);
        subject = (EditText) findViewById(R.id.Subject);
        prelim = (EditText) findViewById(R.id.Prelim);
        midterm = (EditText) findViewById(R.id.Midterm);
        prefi = (EditText) findViewById(R.id.PreFinals);

        finalExam = (EditText) findViewById(R.id.Fx);
        finalGradeOutput = (TextView) findViewById(R.id.FinalGradeOutput);
    }

    private void Interaction() {
        DisplayDeficiencyInformation();
    }

    private FirebaseDatabase database;
    private DatabaseReference studentDeficiencyRef;

    private void DisplayDeficiencyInformation() {
        database = FirebaseDatabase.getInstance();
        studentDeficiencyRef = database.getReference("grades");

        Query query = studentDeficiencyRef.orderByChild("StudentNumber").equalTo(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String subjectText = snapshot.child("Subject").getValue(String.class);
                        String prelimText = snapshot.child("Prelim").getValue(String.class);
                        String midtermText = snapshot.child("Midterm").getValue(String.class);
                        String preFinalsText = snapshot.child("PreFinals").getValue(String.class);
                        String finalExamText = snapshot.child("FinalExam").getValue(String.class);
                        String finalGradeText = snapshot.child("FinalGrade").getValue(String.class);

                        studentNo.setText(userID != null ? userID : "");
                        subject.setText(subjectText != null ? subjectText : "");
                        prelim.setText(prelimText != null ? prelimText : "");
                        midterm.setText(midtermText != null ? midtermText : "");
                        prefi.setText(preFinalsText != null ? preFinalsText : "");
                        finalExam.setText(finalExamText != null ? finalExamText : "");
                        finalGradeOutput.setText(finalGradeText != null ? finalGradeText : "");
                        break;
                    }
                } else {
                    Toast.makeText(StudentGrade.this, "Not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StudentGrade.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
