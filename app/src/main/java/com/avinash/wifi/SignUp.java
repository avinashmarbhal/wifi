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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.android.*;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextInputLayout editTextName,editTextEmail,editTextPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        editTextName = findViewById(R.id.reg_username);
        editTextEmail =  findViewById(R.id.reg_Email);
        editTextPassword = findViewById(R.id.reg_Password);
        progressBar = (ProgressBar) findViewById(R.id.progress);

    }

    public void Login(View view) {
        startActivity(new Intent(this,Login.class));
    }

    public void REG(View view) {
        registerUser();
    }

    private void registerUser() {
        String email = editTextEmail.getEditText().getText().toString();
        String password = editTextPassword.getEditText().getText().toString();
        String fullName = editTextName.getEditText().getText().toString();
        if (!validateName() | !validateEmail() | !validatePassword()){
            return;

        }
        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Teacher teacher = new Teacher(fullName,email);
                            FirebaseDatabase.getInstance().getReference("Teacher").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(teacher).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUp.this, "Register Successfully !", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(SignUp.this,Login.class));
                                    }else {
                                        Toast.makeText(SignUp.this, "Failed to register! try again ", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }
                            });

                        }else {
                            Toast.makeText(SignUp.this, "Failed to register! try again ", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private Boolean validateName(){
        String val1 = editTextName.getEditText().getText().toString();

        if (val1.isEmpty()){
            editTextName.setError("Field can't be empty!");
            return false;
        }else {
            editTextName.setError(null);
            editTextName.setErrorEnabled(false);
            return true;
        }

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
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val5.isEmpty()){
            editTextPassword.setError("Field can't be empty!");
            return false;
        }else if (!val5.matches(passwordVal)){
            editTextPassword.setError("Password is too weak");
            return false;

        }else {
            editTextPassword.setError(null);
            editTextPassword.setErrorEnabled(false);
            return true;
        }

    }
}