package com.example.password_keychain_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText password,email;

    private Button login;
    private TextView register;

    private FirebaseAuth mAuth;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        progress = (ProgressBar) findViewById(R.id.progressBarL);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this,register.class));
                break;
            case R.id.login:
                userLogin();
                break;
        }

    }

    private void userLogin() {
        String emai = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(emai.isEmpty()){
            email.setError("email is empty!");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emai).matches()){
            email.setError("email is invalid!");
            email.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            password.setError("password is required");
            password.requestFocus();
            return;
        }

        progress.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(emai,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(Login.this,MainActivity.class));
                    progress.setVisibility(View.GONE);

                }else{
                    Toast.makeText(Login.this, "Failed to Login!", Toast.LENGTH_LONG).show();
                    progress.setVisibility(View.GONE);
                }
            }
        });


    }
}