package com.example.talentube.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.talentube.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class add_video_Activity extends AppCompatActivity {


    // Action bar
    private ActionBar actionBar;

    // UI Elements
    private EditText editText_title,editText_desc;
    private VideoView videoView;
    private Button btn_upload_video;
    private FloatingActionButton fab_pick_video;

    // Request Codes
    private static final int GALLERY_VIDEO_PICK_ID = 100;
    private static final int CAMERA_VIDEO_PICK_ID = 100;
    private static final int CAMERA_REQUEST_ID = 100;

    private String[] cameraPermissions;

    private Uri videoUri;
    private String videoTitle,videoDesc;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Upload Video");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // initializing UI elements
        editText_title = findViewById(R.id.new_video_title);
        editText_desc = findViewById(R.id.new_video_desc);
        videoView = findViewById(R.id.new_video_view);
        btn_upload_video = findViewById(R.id.btn_upload_video);
        fab_pick_video = findViewById(R.id.btn_pick_video);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Uploading...");
        progressDialog.setCanceledOnTouchOutside(false);


        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};


        btn_upload_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoTitle = editText_title.getText().toString().trim();
                videoDesc = editText_desc.getText().toString().trim();

                if(videoTitle.isEmpty()){
                    Toast.makeText(add_video_Activity.this, "Title is required", Toast.LENGTH_SHORT).show();
                }else if(videoDesc.isEmpty()){
                    Toast.makeText(add_video_Activity.this, "Description is required", Toast.LENGTH_SHORT).show();
                }else if(videoUri == null){
                    Toast.makeText(add_video_Activity.this,  "Pick a video !!", Toast.LENGTH_SHORT).show();
                }else{
                    uploadVideoToFirebase();
                }
            }
        });

        fab_pick_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoPickDialog();
            }
        });

    }

    private void uploadVideoToFirebase() {
        // show progress while uploading video
        progressDialog.show();

        // timeStamp
        String timeStamp = "" + System.currentTimeMillis();

        // file Path & name in firebase
        String filePathAndName = "Videos/" + "video_" + timeStamp;

        // storage reference
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);

        // upload video, any file format can be uploaded
        storageReference.putFile(videoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // video upload successful, get video URL
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadURL = uriTask.getResult();
                        if(uriTask.isSuccessful()){
                            // URL of uploaded video is received

                            // Now add details of video to firebase database
                            HashMap<String,Object> hashMap = new HashMap<>();
                            hashMap.put("id",""+timeStamp);
                            hashMap.put("title",""+videoTitle);
                            hashMap.put("description",""+videoDesc);
                            hashMap.put("timeStamp",""+timeStamp);
                            hashMap.put("url",""+downloadURL);

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Videos");
                            reference.child(timeStamp)
                                    .setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // video details uploaded successfully to firebase database
                                            progressDialog.dismiss();
                                            Toast.makeText(add_video_Activity.this, "Video Uploaded 👍", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // video detail addition failed
                                            progressDialog.dismiss();
                                            Toast.makeText(add_video_Activity.this, "Uploaded failed !" + "\n Try again !", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // video upload to storage failed
                        progressDialog.dismiss();
                        Toast.makeText(add_video_Activity.this, "Video Upload Failed !"+"\n   Try again !", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void videoPickDialog() {
        // Dialog Options
        String[] options = {"Camera","Gallery"};

        // Dialog View
        AlertDialog.Builder dialogBuilder =  new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Upload Video from")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                //  from camera
                                if(!checkCameraPermission()){
                                    requestCameraPermission();
                                }else{
                                    videoPickCamera();
                                }
                                break;
                            case 1:
                                // from gallery
                                pickVideoGallery();
                                break;
                        }
                    }
                }).show();
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermissions, CAMERA_REQUEST_ID);
    }

    private boolean checkCameraPermission(){
        boolean permission_1 = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean permission_2 = ContextCompat.checkSelfPermission(this,Manifest.permission.WAKE_LOCK) == PackageManager.PERMISSION_GRANTED;
        return permission_1 && permission_2 ;
    }

    private void pickVideoGallery(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Video"),GALLERY_VIDEO_PICK_ID);
    }

    private void videoPickCamera(){
        Intent intent = new Intent((MediaStore.ACTION_VIDEO_CAPTURE));
        startActivityForResult(intent,CAMERA_VIDEO_PICK_ID);
    }

    private void setVideoToVideoView(){
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView.pause();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_ID:
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted){
                        videoPickCamera();
                    }else{
                        Toast.makeText(this,"Camera & Storage Permission required",Toast.LENGTH_SHORT).show();
                    }
                }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(resultCode == GALLERY_VIDEO_PICK_ID){
                videoUri = data.getData();
                setVideoToVideoView();
            }else if(requestCode == CAMERA_VIDEO_PICK_ID){
                videoUri = data.getData();
                setVideoToVideoView();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // to go to previous activity on clicking back button on the action bar
        return super.onSupportNavigateUp();
    }
}