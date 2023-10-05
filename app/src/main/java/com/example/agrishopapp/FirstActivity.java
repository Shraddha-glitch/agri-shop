package com.example.agrishopapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

public class FirstActivity extends AppCompatActivity {

    private ConstraintLayout parentLayout;
    private Handler handler;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parentLayout = findViewById(R.id.parent_layout);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                redirectToMainActivity();
            }
        }, 2000);
    }

    private void redirectToMainActivity() {
        Intent intent = new Intent(FirstActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the delayed redirection when the activity is destroyed
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}

