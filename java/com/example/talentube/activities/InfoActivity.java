package com.example.talentube.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.talentube.R;

public class InfoActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info);

    }

public void arrow(View view) {
    Intent intent=new Intent(this,Setup.class);
    startActivity(intent);
}
}
