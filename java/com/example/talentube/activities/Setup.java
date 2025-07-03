package com.example.talentube.activities;

import android.os.Bundle;

import android.content.Intent;

import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.talentube.R;
import com.example.talentube.View.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Setup extends AppCompatActivity {

    ImageView logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_activity);

        logout = findViewById(R.id.loginBtn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();

            }
        });
    }public void arrow(View view) {
        Intent intent=new Intent(this,DashActivity.class);
        startActivity(intent);
    }

    public void setting(View view) {
        Intent intent = new Intent(this, Changepasword.class);
        startActivity(intent);
    }

    public void about(View view) {
        Intent intent = new Intent(this, InfoActivity.class);
    }

    public void map(View view) {
        Intent intent = new Intent(this,MapActivity.class);
    }
}