package com.example.password_keychain_app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView banner;
    private String uID;
    private DatabaseReference db;
    private User u;
    private String name;
    private String email;
    private String pk;
    private EditText passwordS,usernameS,nameOfService;
    private Button addLogin;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance("https://passwordkeychain-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        banner = (TextView) findViewById(R.id.banner);
        passwordS = (EditText) findViewById(R.id.passwordS);
        usernameS = (EditText) findViewById(R.id.usernameS);
        nameOfService = (EditText) findViewById(R.id.nameOfService);
        addLogin = (Button) findViewById(R.id.addLogin);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uID = user.getUid();

        }else{
            startActivity(new Intent(this,Login.class));
        }

        db.child("Users").child(uID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    u = task.getResult().getValue(User.class);
                    name = u.name;
                    email = u.email;
                    pk = u.pk;


                    banner.setText("Hi "+u.name+"!");
                    //if first time user generate public and private key
                    if(pk.equals("dummy")) {
                        createKeys genKeys = new createKeys();
                        String publicKey = Base64.getEncoder().encodeToString(genKeys.getPublicKey().getEncoded());
                        String privatekey = Base64.getEncoder().encodeToString(genKeys.getPrivateKey().getEncoded());


                        HashMap map = new HashMap();
                        map.put("email", name);
                        map.put("name", email);
                        map.put("pk", publicKey);

                        db.child("Users").child(uID).updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(MainActivity.this, "Successfully added public key to user", Toast.LENGTH_LONG).show();
                            }
                        });

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle("This is your private key, please store securely. This key will not be shown again!");
                        builder.setMessage(privatekey);

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }

                    addLogin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String username = usernameS.getText().toString().trim();
                            String password = passwordS.getText().toString().trim();
                            String service = nameOfService.getText().toString().trim();
                            if(username.isEmpty()){
                                usernameS.setError("need username");
                                usernameS.requestFocus();
                                return;
                            }
                            if(password.isEmpty()){
                                usernameS.setError("need password");
                                usernameS.requestFocus();
                                return;
                            }
                            if(service.isEmpty()){
                                usernameS.setError("need name of service");
                                usernameS.requestFocus();
                                return;
                            }
                            encryptPassword encryptor =  new encryptPassword();


                            db.child("Users").child(uID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull  Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    }
                                    else {
                                        Log.d("firebase", String.valueOf(task.getResult().getValue()));
                                        u = task.getResult().getValue(User.class);
                                        HashMap map2 = new HashMap();
                                        String pk = u.pk;
                                        String eUser = null;
                                        String ePass = null;
                                        try{
                                            ePass = Base64.getEncoder().encodeToString(encryptor.encrypt(password, pk));
                                            eUser = Base64.getEncoder().encodeToString(encryptor.encrypt(username, pk));
                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        LoginInfo login = new LoginInfo(eUser,ePass);
                                        map2.put(service,login);

                                        db.child("Users").child(uID).child("loginsMap").updateChildren(map2).addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                Toast.makeText(MainActivity.this, "Successfully added login", Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                }
                            });

                        }
                    });



                }
            }
        });
















    }
}