package com.example.enrollmentsystemproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.TreeMap;

public class ProfessionMain extends AppCompatActivity {
    private EditText studentNo, subject, prelim, midterm, prefi, finalExam;
    private TextView outputFinalGrade;
    private Button finalGrade, submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_student_grade_semester);

        AddElement();
        Interaction();
    }

    private void AddElement() {
        studentNo = (EditText) findViewById(R.id.StudentNumberProf);
        subject = (EditText) findViewById(R.id.Subject);
        prelim = (EditText) findViewById(R.id.Prelim);
        midterm = (EditText) findViewById(R.id.Midterm);
        prefi = (EditText) findViewById(R.id.PreFinals);

        finalExam = (EditText) findViewById(R.id.Fx);
        finalGrade = (Button) findViewById(R.id.FinalGrade);

        outputFinalGrade = (TextView) findViewById(R.id.FinalGradeOutput);

        submit = (Button) findViewById(R.id.submit);
    }

    private void Interaction() {
        finalGrade.setOnClickListener(e -> {
            double prelimText = Double.parseDouble(prelim.getText().toString());
            double midtermText = Double.parseDouble(midterm.getText().toString());
            double prefiText = Double.parseDouble(prefi.getText().toString());
            double finalXText = Double.parseDouble(finalExam.getText().toString());

            double amountGrade = (prelimText + midtermText + prefiText + finalXText) / 4;

            outputFinalGrade.setText(String.valueOf(amountGrade));
        });

        submit.setOnClickListener(e -> {
            AddStudentGrade();
        });
    }

    FirebaseDatabase database;
    DatabaseReference professorGradeReference;

    private void AddStudentGrade() {
        TreeMap<String, Object> gradeData = new TreeMap<>();
        gradeData.put("StudentNumber", studentNo.getText().toString());
        gradeData.put("Subject", subject.getText().toString());
        gradeData.put("Prelim", prelim.getText().toString());
        gradeData.put("Midterm", midterm.getText().toString());
        gradeData.put("PreFinals", prefi.getText().toString());
        gradeData.put("FinalExam", finalExam.getText().toString());
        gradeData.put("FinalGrade", outputFinalGrade.getText().toString());

        database = FirebaseDatabase.getInstance();
        professorGradeReference = database.getReference("grades");

        String key = professorGradeReference.push().getKey();
        gradeData.put("Key", key);

        professorGradeReference.child(key).setValue(gradeData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ProfessionMain.this, "Added yung Grades", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
