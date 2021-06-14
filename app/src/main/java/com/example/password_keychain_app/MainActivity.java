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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance("https://passwordkeychain-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        banner = (TextView) findViewById(R.id.banner);





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
                        map.put("publicKey", publicKey);

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

                }
            }
        });
















    }
}