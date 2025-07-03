package com.example.talentube.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talentube.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText edEmail, edPassword;
    Button loginbtn, registerbtn;
    TextView forgetpasswordtv;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.edtEmail);
        edPassword = findViewById(R.id.edtPassword);
        registerbtn = findViewById(R.id.btnRegister1);
        loginbtn = findViewById(R.id.btnLogin);
        forgetpasswordtv = findViewById(R.id.forget);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        loginbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = edEmail.getText().toString();
                        String password = edPassword.getText().toString();
                        if (TextUtils.isEmpty(email)) {
                            edEmail.setError("Email is Required");
                            edEmail.requestFocus();
                            return;
                        }
                        if (TextUtils.isEmpty(password)) {
                            edPassword.setError("Password is Required");
                            return;
                        }
                        if (password.length() < 6) {
                            edPassword.setError("Passwo rd Must be >= 6 Characters");
                            return;
                        }
                        progressBar.setVisibility(View.VISIBLE);
                        mAuth
                                .signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(LoginActivity.this, VideoActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            FirebaseUser fuser = mAuth.getCurrentUser();
                                            assert fuser != null;
                                            if (fuser.isEmailVerified()) {
                                                progressBar.setVisibility(View.GONE);
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this,"please verify your email",Toast.LENGTH_SHORT)
                                                        .show();
                                                progressBar.setVisibility(View.GONE);
                                            }

                                        } else {
                                            Toast.makeText(LoginActivity.this,"Error !" + task.getException().getMessage(),
                                                            Toast.LENGTH_SHORT)
                                                    .show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                });

        forgetpasswordtv.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final EditText resetMail = new EditText(view.getContext());
                        final AlertDialog.Builder passwordResetDialog =
                                new AlertDialog.Builder(view.getContext());
                        passwordResetDialog.setTitle("Reset Password ?");
                        passwordResetDialog.setMessage("Enter Your Email To Receive Reset link");
                        passwordResetDialog.setView(resetMail);

                        passwordResetDialog.setPositiveButton(
                                "Send",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {
                                        String mail = resetMail.getText().toString();
                                        mAuth
                                                .sendPasswordResetEmail(mail)
                                                .addOnSuccessListener(
                                                        new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(
                                                                                LoginActivity.this,
                                                                                "Reset Link Sent To Your Email",
                                                                                Toast.LENGTH_SHORT)
                                                                        .show();
                                                            }
                                                        })
                                                .addOnFailureListener(
                                                        new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(
                                                                                LoginActivity.this,
                                                                                "Error ! Reset link is not Sent",
                                                                                Toast.LENGTH_SHORT)
                                                                        .show();
                                                            }
                                                        });
                                    }
                                });
                        passwordResetDialog.setNegativeButton(
                                "Close",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {}
                                });
                        passwordResetDialog.create().show();
                    }
                });
        registerbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                    }
                });
    }
}