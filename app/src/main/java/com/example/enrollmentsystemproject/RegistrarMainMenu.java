package com.example.enrollmentsystemproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrarMainMenu extends AppCompatActivity {
    private String userID = "";
    private LinearLayout userManagement, logout;
    private TextView userProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_main_menu);

        userID = getIntent().getStringExtra("userID");
        AddElement();
        Interaction();
    }

    private void AddElement() {
        userProfileName = (TextView) findViewById(R.id.UserProfileName);

        userManagement = (LinearLayout) findViewById(R.id.UserManagement);
        logout = (LinearLayout) findViewById(R.id.RegistrarLogout);
    }

    private void Interaction() {
        userProfileName.setText(String.valueOf(userID));

        userManagement.setOnClickListener(e -> {
            Intent intent = new Intent(RegistrarMainMenu.this, RegistrarUserManagement.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        });

        logout.setOnClickListener(e -> {
            Intent intent = new Intent(RegistrarMainMenu.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
