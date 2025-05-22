package com.example.enrollmentsystemproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.TreeMap;

public class RegistrarDeficiency extends AppCompatActivity {
    private EditText studentNo;
    RadioGroup psaGroup, form137Group, goodMoral, exam, interview;
    RadioButton PSASelected, Form137Selected, GoodMoralSelected, ExamSelected, InterviewSelected;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_deficiencies);

        AddElement();
        Interaction();
    }

    private void AddElement() {
        studentNo = findViewById(R.id.StudentNumberProf);

        psaGroup = findViewById(R.id.PSAGroup);
        form137Group = findViewById(R.id.Form137Group);
        goodMoral = findViewById(R.id.GoodMoralGroup);
        exam = findViewById(R.id.ExamGroup);
        interview = findViewById(R.id.InteviewGroup);

        update = findViewById(R.id.Update);
    }

    private void Interaction() {
        psaGroup.setOnCheckedChangeListener((group, checkedID) -> {
            Function();
        });

        form137Group.setOnCheckedChangeListener((group, checkedID) -> {
            Function();
        });

        goodMoral.setOnCheckedChangeListener((group, checkedID) -> {
            Function();
        });

        exam.setOnCheckedChangeListener((group, checkedID) -> {
            Function();
        });

        interview.setOnCheckedChangeListener((group, checkedID) -> {
            Function();
        });

        update.setOnClickListener(e -> {
            Function();
            Deficiency();
//            EditDeficiency();
        });
    }

    private void Function() {
        int psaSelectedID = psaGroup.getCheckedRadioButtonId();
        if (psaSelectedID == -1) {
            return;
        }

        int form137SelectedID = form137Group.getCheckedRadioButtonId();
        if (form137SelectedID == -1) {
            return;
        }

        int goodMoralSelectedID = goodMoral.getCheckedRadioButtonId();
        if (goodMoralSelectedID == -1) {
            return;
        }

        int examSelectedID = exam.getCheckedRadioButtonId();
        if (examSelectedID == -1) {
            return;
        }

        int interviewSelectedID = interview.getCheckedRadioButtonId();
        if (interviewSelectedID == -1) {
            return;
        }

        PSASelected = findViewById(psaSelectedID);
        Form137Selected = findViewById(form137SelectedID);
        GoodMoralSelected = findViewById(goodMoralSelectedID);
        ExamSelected = findViewById(examSelectedID);
        InterviewSelected = findViewById(interviewSelectedID);
    }

    FirebaseDatabase database;
    DatabaseReference deficiencyReference;

    private void Deficiency() {
        TreeMap<String, Object> gradeData = new TreeMap<>();
        gradeData.put("StudentNumber", studentNo.getText().toString());
        gradeData.put("PSA", PSASelected.getText().toString());
        gradeData.put("Form 137", Form137Selected.getText().toString());
        gradeData.put("Good Moral", GoodMoralSelected.getText().toString());
        gradeData.put("Exam", ExamSelected.getText().toString());
        gradeData.put("Interview", InterviewSelected.getText().toString());

        database = FirebaseDatabase.getInstance();
        deficiencyReference = database.getReference("deficiency");

        String key = deficiencyReference.push().getKey();
        gradeData.put("Key", key);

        deficiencyReference.child(key).setValue(gradeData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(RegistrarDeficiency.this, "Update Deficiency", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void EditDeficiency() {
        database = FirebaseDatabase.getInstance();
        deficiencyReference = database.getReference("deficiency");

        Query query = deficiencyReference.orderByChild("StudentNumber").equalTo(studentNo.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().child("PSA").setValue(PSASelected.getText().toString());
                        snapshot.getRef().child("Form 137").setValue(Form137Selected.getText().toString());
                        snapshot.getRef().child("Good Moral").setValue(GoodMoralSelected.getText().toString());
                        snapshot.getRef().child("Exam").setValue(ExamSelected.getText().toString());
                        snapshot.getRef().child("Interview").setValue(InterviewSelected.getText().toString());
                    }
                    Toast.makeText(RegistrarDeficiency.this, "Gumana Update Deficiency", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegistrarDeficiency.this, "No record found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RegistrarDeficiency.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
