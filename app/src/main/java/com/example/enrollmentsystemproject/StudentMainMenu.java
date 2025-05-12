package com.example.enrollmentsystemproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class StudentMainMenu extends AppCompatActivity {
    private String userID = "";
    private LinearLayout personalInformation, changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main_menu);

        userID = getIntent().getStringExtra("userID");
        AddElement();
        Interaction();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    private void AddElement() {
        personalInformation = (LinearLayout) findViewById(R.id.PersonalInformation);
        changePassword = (LinearLayout) findViewById(R.id.ChangePassword);
    }

    private void Interaction() {
        personalInformation.setOnClickListener(e -> {
            Intent intent = new Intent(StudentMainMenu.this, StudentPersonalInformation.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });

        changePassword.setOnClickListener(e -> {
            Intent intent = new Intent(StudentMainMenu.this, StudentChangePassword.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });
    }
}
