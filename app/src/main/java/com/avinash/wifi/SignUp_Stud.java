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
import com.google.firebase.database.FirebaseDatabase;

public class SignUp_Stud extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextInputLayout editTextName,editTextEmail,editTextPassword,editTextC2k2,editTextRollNo;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_stud);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        editTextName = findViewById(R.id.reg_username);
        editTextEmail =  findViewById(R.id.reg_Email);
        editTextPassword = findViewById(R.id.reg_Password);
        editTextRollNo =  findViewById(R.id.reg_RollNo);
        editTextC2k2 = findViewById(R.id.reg_C2K2);
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
        String C2k2 = editTextC2k2.getEditText().getText().toString();
        String RollNo = editTextRollNo.getEditText().getText().toString();

        if (!validateName() | !validateEmail() | !validatePassword() | !validateC2k2() | !validateRollNo()){
            return;

        }
        progressBar.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Student student = new Student(fullName,RollNo,C2k2,email);
                            FirebaseDatabase.getInstance().getReference("Student").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(student).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(SignUp_Stud.this, "Register Successfully !", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(SignUp_Stud.this,Login.class));
                                    }else {
                                        Toast.makeText(SignUp_Stud.this, "Failed to register! try again ", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }

                                }
                            });

                        }else {
                            Toast.makeText(SignUp_Stud.this, "Failed to register! try again ", Toast.LENGTH_LONG).show();
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

    private Boolean validateRollNo(){
        String val4 = editTextRollNo.getEditText().getText().toString();

        if (val4.isEmpty()){
            editTextRollNo.setError("Field can't be empty!");
            return false;
        }else {
            editTextRollNo.setError(null);
            editTextRollNo.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateC2k2(){
        String val4 = editTextC2k2.getEditText().getText().toString();

        if (val4.isEmpty()){
            editTextC2k2.setError("Field can't be empty!");
            return false;
        }else {
            editTextC2k2.setError(null);
            editTextC2k2.setErrorEnabled(false);
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