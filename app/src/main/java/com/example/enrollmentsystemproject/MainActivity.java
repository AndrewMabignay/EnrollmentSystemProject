package com.example.enrollmentsystemproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText username, password;
    private LinearLayout reset, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(MainActivity.this, RegistrarUserManagement.class);
//        startActivity(intent);
        AddElement();
        Interaction();
    }

    private void AddElement() {
        username = (EditText) findViewById(R.id.Username);
        password = (EditText) findViewById(R.id.Password);

        reset = (LinearLayout) findViewById(R.id.Reset);
        login = (LinearLayout) findViewById(R.id.Login);
    }

    FirebaseDatabase database;
    DatabaseReference studentReference, userReference;

    private void Interaction() {
        login.setOnClickListener(e -> {
            AuthenticationInteraction(username.getText().toString(), password.getText().toString());
        });
    }

    private void AuthenticationInteraction(String usernameField, String passwordField) {
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("user");

        Query query = userReference.orderByChild("Username").equalTo(usernameField);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    username.setError("Username not found.");
                    return;
                }

                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    String userPasswordText = userSnap.child("Password").getValue(String.class);

                    if (userPasswordText == null || !userPasswordText.equals(passwordField)) {
                        password.setError("Incorrect Password");
                        return;
                    }

                    String userRoleText = userSnap.child("Role").getValue(String.class);

                    if (userRoleText == null) {
                        username.setError("Role is missing");
                        return;
                    }

                    switch (userRoleText) {
                        case "Student":
                            Intent studentIntent = new Intent(MainActivity.this, StudentMainMenu.class);
                            studentIntent.putExtra("userID", usernameField);
                            startActivity(studentIntent);
                            return;
                        case "Admin":
                            Intent adminIntent = new Intent(MainActivity.this, RegistrarMainMenu.class);
                            adminIntent.putExtra("userID", usernameField);
                            startActivity(adminIntent);
                            return;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}