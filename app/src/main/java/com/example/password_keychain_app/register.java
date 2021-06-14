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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText name, password, email;
    private Button register;
    private ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        register = (Button) findViewById(R.id.registerR);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        name = (EditText) findViewById(R.id.nameR);
        email = (EditText) findViewById(R.id.emailR);
        password = (EditText) findViewById(R.id.passwordR);

        progress = (ProgressBar) findViewById(R.id.progressBarR);

    }


    private void registerUser(){

        String name1 = name.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String emai = email.getText().toString().trim();

        if(name1.isEmpty()){
            name.setError("Name Is Required!");
            name.requestFocus();
            return;
        }

        if(pass.isEmpty()){
            password.setError("Password Is Required!");
            password.requestFocus();
            return;
        }
        else if(pass.length()<8){
            password.setError("Password needs to have at least 8 characters!");
            password.requestFocus();
            return;
        }
        else if(!pass.matches(".*\\d.*")){
            password.setError("Password needs at least 1 number!");
            password.requestFocus();
            return;
        }
        else if (!pass.matches(".*[A-Z].*")){
            password.setError("Password needs at least 1 Uppercase Letter!");
            password.requestFocus();
            return;
        }
        else if (!pass.matches(".*[a-z].*")){
            password.setError("Password needs at least 1 Lowercase Letter!");
            password.requestFocus();
            return;
        }

        if(emai.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emai).matches()){
            email.setError("Not a valid email");
            email.requestFocus();
            return;
        }

        progress.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emai,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull  Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name1,emai,"dummy");
                            FirebaseDatabase.getInstance("https://passwordkeychain-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(register.this,"User has been registered!",Toast.LENGTH_LONG).show();
                                        progress.setVisibility(View.GONE);
                                        startActivity(new Intent(register.this,Login.class));
                                    } else{
                                        Toast.makeText(register.this,"registration failed! Try again",Toast.LENGTH_LONG).show();
                                        progress.setVisibility(View.GONE);
                                        System.out.println("no");
                                    }
                                }
                            });


                        } else{
                            Toast.makeText(register.this,"registration failed! Try again",Toast.LENGTH_LONG).show();
                            progress.setVisibility(View.GONE);
                            System.out.println("yes");

                        }
                    }
                });



    }
}