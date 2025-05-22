package com.example.enrollmentsystemproject;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentDeficiency extends AppCompatActivity {
    private String userID = "";

    TextView psa, form137, goodmoral, exam, interview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_deficiencies);

        userID = getIntent().getStringExtra("userID");
        AddElement();
        Interaction();
    }

    private void AddElement() {
        psa = findViewById(R.id.psa);
        form137 = findViewById(R.id.form137);
        goodmoral = findViewById(R.id.goodmoral);
        exam = findViewById(R.id.exam);
        interview = findViewById(R.id.interview);
    }

    private void Interaction() {
        DisplayDeficiencyInformation();
    }

    private FirebaseDatabase database;
    private DatabaseReference studentDeficiencyRef;

    private void DisplayDeficiencyInformation() {
        database = FirebaseDatabase.getInstance();
        studentDeficiencyRef = database.getReference("deficiency");

        Query query = studentDeficiencyRef.orderByChild("StudentNumber").equalTo(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String psaText = snapshot.child("PSA").getValue(String.class);
                        String form137Text = snapshot.child("Form 137").getValue(String.class);
                        String goodmoralText = snapshot.child("Good Moral").getValue(String.class);
                        String examText = snapshot.child("Exam").getValue(String.class);
                        String interviewText = snapshot.child("Interview").getValue(String.class);

                        psa.setText(psaText != null ? psaText : "");
                        form137.setText(form137Text != null ? form137Text : "");
                        goodmoral.setText(goodmoralText != null ? goodmoralText : "");
                        exam.setText(examText != null ? examText : "");
                        interview.setText(examText != null ? interviewText : "");
                        break;
                    }
                } else {
                    Toast.makeText(StudentDeficiency.this, "Not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StudentDeficiency.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
