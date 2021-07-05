package com.example.hemengo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button launchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launchButton = findViewById(R.id.launchButton);
        launchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent scanActivity = new Intent(MainActivity.this, ScanActivity.class);
        startActivity(scanActivity);
    }
}