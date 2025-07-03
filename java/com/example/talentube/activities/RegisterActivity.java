package com.example.talentube.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talentube.R;
import com.example.talentube.model.User;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private FirebaseAuth mAuth;
    FirebaseDatabase database;

    EditText edName, edEmail, edPassword, edConfirm, edPhone, edDob, edAddress;
    Button registerbtn;
    TextView alredyhaveac;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edName = findViewById(R.id.edtName);
        edEmail = findViewById(R.id.edtEmail1);
        edPassword = findViewById(R.id.edtPassword1);
        edConfirm = findViewById(R.id.edtConfirmPassword);
        edPhone = findViewById(R.id.edtPhone);
        edDob = findViewById(R.id.edtDob);
        edAddress = findViewById(R.id.edtAddress);
        registerbtn = findViewById(R.id.btnRegister1);
        alredyhaveac = findViewById(R.id.btnalready);
        progressBar = findViewById(R.id.progressBar);
        database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();


        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                String confirm = edConfirm.getText().toString();
                String phone = edPhone.getText().toString();
                String dob = edDob.getText().toString();
                String address = edAddress.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    edName.setError("Full Name is Required");
                    edName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    edEmail.setError("Email is Required");
                    edEmail.requestFocus();
                    return;

                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edEmail.setError("Please enter a valid email");
                    edEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    edPassword.setError("Password is Required");
                    edPassword.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    edPhone.setError("Phone no. is Required");
                    edPhone.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(dob)) {
                    edDob.setError("Date of Birth is Required");
                    edDob.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(address)) {
                    edAddress.setError("Address is Required");
                    edAddress.requestFocus();
                    return;

                }
                if (password.length() <6) {
                    edPassword.setError("Password Must be >= 6 Characters");
                    edPassword.requestFocus();
                    return;
                }
                if (confirm.isEmpty() || !password.equals(confirm)) {
                    edConfirm.setError("Invalid Password");
                }
                progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User Registered Successfull", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            progressBar.setVisibility(View.GONE);
                            startActivity(intent);

                            FirebaseUser fuser = mAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterActivity.this, "Verification Email Has Been Sent", Toast.LENGTH_SHORT).show();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure:Email not sent " + e.getMessage());

                                }
                            });


                            User user = new User(edAddress.getText().toString(), edName.getText().toString(), edPhone.getText().toString(), email, password);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);

                        }else{
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "You already registered", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);





                            } else {
                                Toast.makeText(getApplicationContext(), "Erro !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }


                        }

                    }
                });
            }
        });

        alredyhaveac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }
}