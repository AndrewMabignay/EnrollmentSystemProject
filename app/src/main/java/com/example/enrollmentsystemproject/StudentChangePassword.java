package com.example.enrollmentsystemproject;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentChangePassword extends AppCompatActivity {
    private EditText oldPassword, newPassword, verifyPassword;
    private LinearLayout updatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_change_password);

        AddElement();
        Interaction();
    }

    private void AddElement() {
        oldPassword = (EditText) findViewById(R.id.OldPassword);
        newPassword = (EditText) findViewById(R.id.NewPassword);
        verifyPassword = (EditText) findViewById(R.id.VerifyPassword);

        updatePassword = (LinearLayout) findViewById(R.id.UpdatePassword);
    }

    FirebaseDatabase database;
    DatabaseReference userReference;

    private void Interaction() {
        String userID = getIntent().getStringExtra("userID");

        updatePassword.setOnClickListener(e -> {
            ChangingPasswordInteraction(userID);
        });
    }

    private void ChangingPasswordInteraction(String username) {
        if (oldPassword.getText().toString().isEmpty()) {
            oldPassword.setError("Input Old Password");
            return;
        }

        if (newPassword.getText().toString().isEmpty()) {
            newPassword.setError("Input New Password");
            return;
        }

        if (verifyPassword.getText().toString().isEmpty()) {
            verifyPassword.setError("Input Verify Password");
            return;
        }

        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("user");

        Query query = userReference.orderByChild("Username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                        String passwordText = userSnapShot.child("Password").getValue(String.class);

                        if (!passwordText.equals(oldPassword.getText().toString())) {
                            oldPassword.setError("Current Password Not Match");
                            return;
                        }

                        if (!newPassword.getText().toString().equals(verifyPassword.getText().toString())) {
                            verifyPassword.setError("Verify and New Password not match");
                            return;
                        }

                        userSnapShot.getRef().child("Password").setValue(newPassword.getText().toString());
                        Toast.makeText(StudentChangePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();

                        break;
                    }
                } else {
                    Toast.makeText(StudentChangePassword.this, "Not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(StudentChangePassword.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
