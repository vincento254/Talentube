package com.example.talentube.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.talentube.R;

import androidx.annotation.NonNull;

import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;

import android.view.View;
import android.view.WindowManager;

import com.example.talentube.MediaObject;
import com.example.talentube.adapters.VideoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    FloatingActionButton btn_add_video;
    ViewPager2 viewPager2;
    ArrayList<MediaObject> mediaObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // hide status bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        // Action bar Title
        setTitle("FireBase Videos");

        viewPager2 = (ViewPager2)findViewById(R.id.viewPager);
        mediaObjects = new ArrayList<>();

        // For Data from firebase realtime database
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Videos");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mediaObjects.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    MediaObject object = data.getValue(MediaObject.class);
                    mediaObjects.add(object);
                    System.out.println(object);
                }
                viewPager2.setAdapter(new VideoAdapter(mediaObjects));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        viewPager2.setAdapter(new VideoAdapter(mediaObjects));


        // Upload video button
        btn_add_video = findViewById(R.id.btn_add_video);

        btn_add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start new video add activity
                startActivity(new Intent(VideoActivity.this,add_video_Activity.class));
            }
        });
    }

    public void person(View view) {
        Intent intent = new Intent(this, ProfileDisplay.class);
        startActivity(intent);
    }

    public void menu(View view) {
        Intent intent = new Intent(this, DashActivity.class);
        startActivity(intent);
    }
}