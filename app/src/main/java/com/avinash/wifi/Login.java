package com.avinash.wifi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private TextInputLayout editTextName,editTextEmail,editTextPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        editTextEmail =  findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        auth = FirebaseAuth.getInstance();

    }
    public void SignUp(View view) {
        startActivity(new Intent(this,SignUp.class));
    }

    public void Login(View view) {
        userLogin();
    }

    private void userLogin() {
        String emal = editTextEmail.getEditText().getText().toString().trim();
        String password = editTextPassword.getEditText().getText().toString().trim();
        if (!validateEmail() | !validatePassword()){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(emal,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        startActivity(new Intent(Login.this,Admin.class));
                        progressBar.setVisibility(View.GONE);
                    }else {
                        progressBar.setVisibility(View.GONE);
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Check your email to verify your account", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(Login.this, "Failed to login! check email & password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private Boolean validateEmail(){
        String val3 = editTextEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val3.isEmpty()){
            editTextEmail.setError("Field can't be empty!");
            return false;
        }else if (!val3.matches(emailPattern)){
            editTextEmail.setError("Invalid mail !");
            return false;

        }else {
            editTextEmail.setError(null);
            editTextEmail.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatePassword(){
        String val5 = editTextPassword.getEditText().getText().toString();

        if (val5.isEmpty()){
            editTextPassword.setError("Field can't be empty!");
            return false;
        }else {
            return true;
        }
    }
}