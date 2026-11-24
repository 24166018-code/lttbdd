package com.example.lttbdd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        String username = prefs.getString(MainActivity.KEY_USERNAME, "");

        TextView welcomeTv = findViewById(R.id.welcome_tv);
        MaterialButton logoutBtn = findViewById(R.id.logout_btn);

        welcomeTv.setText("Xin chào, " + username);

        logoutBtn.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}