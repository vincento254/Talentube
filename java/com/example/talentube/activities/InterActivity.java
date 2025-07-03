package com.example.talentube.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.example.talentube.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InterActivity extends AppCompatActivity {

    View mContentView;
    Handler handler;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter);

        ImageView imageView = findViewById(R.id.splash);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        mContentView = findViewById(R.id.splash);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    if (user.isEmailVerified()) {
                        startActivity(new Intent(InterActivity.this, DashActivity.class));

                    } else {
                        startActivity(new Intent(InterActivity.this, RegisterActivity.class));

                    }
                } else {
                    startActivity(new Intent(InterActivity.this, RegisterActivity.class));

                }
                finish();
            }
        },2000);
    }
}