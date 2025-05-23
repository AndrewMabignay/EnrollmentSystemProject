package com.example.enrollmentsystemproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentMainMenu extends AppCompatActivity {
    private String userID = "";
    private LinearLayout personalInformation, changePassword, deficiency, grades, announcement, logout;
    private TextView userProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main_menu);

        userID = getIntent().getStringExtra("userID");
        AddElement();
        Interaction();
    }

    private void AddElement() {
        userProfileName = (TextView) findViewById(R.id.UserProfileName);
        personalInformation = (LinearLayout) findViewById(R.id.PersonalInformation);
        changePassword = (LinearLayout) findViewById(R.id.ChangePassword);
        deficiency = (LinearLayout) findViewById(R.id.Deficiencies);
        grades = (LinearLayout) findViewById(R.id.Grades);
        announcement = (LinearLayout) findViewById(R.id.Announcements);
        logout = (LinearLayout) findViewById(R.id.Logout);
    }

    private void Interaction() {
        userProfileName.setText("Hello, " + String.valueOf(userID));

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

        deficiency.setOnClickListener(e -> {
            Intent intent = new Intent(StudentMainMenu.this, StudentDeficiency.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });

        grades.setOnClickListener(e -> {
            Intent intent = new Intent(StudentMainMenu.this, StudentGrade.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });

        announcement.setOnClickListener(e -> {
//            Intent intent = new Intent(StudentMainMenu.this, StudentAnnouncement.class);
//            intent.putExtra("userID", userID);
//            startActivity(intent);
        });

        logout.setOnClickListener(e -> {
            Intent intent = new Intent(StudentMainMenu.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
