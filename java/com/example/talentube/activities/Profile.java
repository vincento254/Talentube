package com.example.talentube.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.talentube.R;

import android.app.ProgressDialog;
import android.app.usage.NetworkStats;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import com.google.firebase.auth.FirebaseAuth;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.karumi.dexter.Dexter;


import static com.example.talentube.activities.ProfileDisplay.REQUEST_PROFILE_UPDATE;


public class Profile extends AppCompatActivity {
    ImageView profile_image;
    ImageButton add_profile_image;
    AutoCompleteTextView gender;
    private TextView tv_name;
    private TextView tv_email;
    private TextView tv_phone;
    private TextView tv_dob;
    private TextView tv_address;
    public static final int CAMERA_REQUEST = 100;
    public static final int STORAGE_REQUEST = 101;
    String cameraPermission[];
    String storagePermission[];
    String[] items = {"Male", "Female"};
    ArrayAdapter<String> adapterItems;
    private static final int CHOOSE_IMAGE = 101;
    private FirebaseDatabase database;
    DatabaseReference dbreference;
    StorageReference storageReference;
    String profileImageUrl;
    Bitmap bitmap;
    String UserID;
    Uri uriProfileImage;
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        gender = findViewById(R.id.gender);
        tv_name = (TextView) findViewById(R.id.edtName);
        tv_email = (TextView) findViewById(R.id.edtEmail1);
        tv_phone = (TextView) findViewById(R.id.edtPhone);
        tv_dob = (TextView) findViewById(R.id.edtDob);
        tv_address = (TextView) findViewById(R.id.edtAddress);
        profile_image = findViewById(R.id.edit);
        add_profile_image = findViewById(R.id.addphoto);
        gender = findViewById(R.id.gender);
        dbreference = FirebaseDatabase.getInstance().getReference().child("userprofile");

        storageReference = FirebaseStorage.getInstance().getReference();


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Please Select File"),CHOOSE_IMAGE);
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();


            }
        });

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        gender.setAdapter(adapterItems);
        gender.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = parent.getItemAtPosition(position).toString();
                    }
                });

        add_profile_image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int picd = 0;
                if (picd==0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else{
                        pickFromGallery();
                    }
                }
                else if (picd == 1){
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else{
                        pickFromGallery();
                    }
                }
            }
        });

    }


    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    public void saveProfile(View view) {


        final  StorageReference uploader = storageReference.child("profileimage/"+"img"+System.currentTimeMillis());


        String name, email, phone, dob, address, sex;

        Intent intent = new Intent();
        name = tv_name.getText().toString();
        email = tv_email.getText().toString();
        phone = tv_phone.getText().toString();
        dob = tv_dob.getText().toString();
        sex = gender.getText().toString();
        address = tv_address.getText().toString();
        intent.putExtra(ProfileDisplay.USER_NAME, name);
        intent.putExtra(ProfileDisplay.USER_EMAIL, email);
        intent.putExtra(ProfileDisplay.USER_PHONE, phone);
        intent.putExtra(ProfileDisplay.USER_DOB, dob);
        intent.putExtra(ProfileDisplay.USER_ADDRESS, address);
        intent.putExtra(ProfileDisplay.USER_GENDER, sex);


        setResult(ProfileDisplay.REQUEST_PROFILE_UPDATE, intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }



    private void pickFromGallery() {


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CHOOSE_IMAGE && data != null && data.getData() != null)
        {
            uriProfileImage = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uriProfileImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
                profile_image.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    private void uploadImageToFirebaseStorage() {
        final StorageReference profileImageRef = storageReference.child("profileimages/" + System.currentTimeMillis() + ".jpg");
        if (uriProfileImage != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profileImageUrl = uri.toString();

                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    if (currentUser != null) {
                                        UserID = currentUser.getUid();
                                        Map<String, Object> profileData = new HashMap<>();
                                        profileData.put("profileImageUrl", profileImageUrl);
                                        dbreference.child(UserID).updateChildren(profileData);

                                    }
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Profile.this, "Failed to upload profile image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case CAMERA_REQUEST:{
                if (grantResults.length > 0){
                    boolean camera_accepted = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    boolean storage_accepted = grantResults[1] == (PackageManager.PERMISSION_GRANTED);
                    if(camera_accepted && storage_accepted){
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(this, "Please enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST:{
                if (grantResults.length > 0){
                    boolean storage_accepted = grantResults[1] == (PackageManager.PERMISSION_GRANTED);
                    if(storage_accepted){
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }
}