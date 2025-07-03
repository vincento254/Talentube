package com.example.talentube.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.talentube.R;

import android.content.Intent;
import android.view.View;



public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void getstarted(View view) {
        Intent intent=new Intent(this,VideoActivity.class);
        startActivity(intent);
    }

    public void reg(View view) {
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void logg(View view) {
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}