package com.example.talentube.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.talentube.R;

import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class ProfileDisplay extends AppCompatActivity {

    public static final int REQUEST_PROFILE_UPDATE = 1;
    public static final String USER_NAME = "com.example.talentube.NAME";
    public static final String USER_EMAIL = "com.example.talentube.EMAIL";
    public static final String USER_PHONE = "com.example.talentube.PHONE";
    public static final String USER_DOB = "com.example.talentube.DOB";
    public static final String USER_ADDRESS = "com.example.talentube.ADDRESS";
    public static final String USER_GENDER = "com.example.talentube.GENDER";
    public static final String USER_PROFILE = "com.example.talentube.PROFILE";

    private TextView textViewNAME,textViewEMAIL,textViewPHONE,textViewDOB,textViewADDESS,textViewGENDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        textViewNAME = (TextView) findViewById(R.id.textname);
        textViewEMAIL = (TextView) findViewById(R.id.textemail);
        textViewPHONE = (TextView) findViewById(R.id.textphone);
        textViewDOB = (TextView) findViewById(R.id.textdob);
        textViewADDESS = (TextView) findViewById(R.id.textaddress);
        textViewGENDER = (TextView) findViewById(R.id.textgender);
    }
    public void editProfile(View view) {

        Intent intent = new Intent(this,Profile.class);
        startActivityForResult(intent,REQUEST_PROFILE_UPDATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_PROFILE_UPDATE){
            String name, email, phone, dob, address, sex, profile;
            name = data.getStringExtra(USER_NAME);
            email = data.getStringExtra(USER_EMAIL);
            phone = data.getStringExtra(USER_PHONE);
            dob = data.getStringExtra(USER_DOB);
            address = data.getStringExtra(USER_ADDRESS);
            sex = data.getStringExtra(USER_GENDER);



            textViewNAME.setText(name);
            textViewEMAIL.setText(email);
            textViewPHONE.setText(phone);
            textViewDOB.setText(dob);
            textViewADDESS.setText(address);
            textViewGENDER.setText(sex);

        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ProfileDisplay", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ProfileDisplay", "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ProfileDisplay", "onStart");
    }
}