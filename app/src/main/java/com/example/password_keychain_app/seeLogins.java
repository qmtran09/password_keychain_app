package com.example.password_keychain_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class seeLogins extends AppCompatActivity {

    private RecyclerView recyclerView;
    loginAdapter adapter;
    DatabaseReference db;
    private User u;
    private String uID,privateKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_logins);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uID = user.getUid();

        }else{
            startActivity(new Intent(this,Login.class));
        }
        db = FirebaseDatabase.getInstance("https://passwordkeychain-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(uID).child("loginsMap");

        EditText pkField = new EditText(seeLogins.this);


        AlertDialog.Builder builder = new AlertDialog.Builder(seeLogins.this);
        builder.setCancelable(false);
        builder.setTitle("Enter your Private Key");
        builder.setView(pkField);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                privateKey = pkField.getText().toString().trim();
               // System.out.println(privateKey);

                dialog.cancel();
                recyclerView = findViewById(R.id.loginList);
                // To display the Recycler view linearly
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(seeLogins.this));

                // It is a class provide by the FirebaseUI to make a
                // query in the database to fetch appropriate data
                FirebaseRecyclerOptions<LoginInfo> options
                        = new FirebaseRecyclerOptions.Builder<LoginInfo>()
                        .setQuery(db, LoginInfo.class)
                        .build();



                // Connecting object of required Adapter class to
                // the Adapter class itself
                adapter = new loginAdapter(options,privateKey);
                // Connecting Adapter class with the Recycler view*/
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }
        });
        builder.show();








    }
    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();

    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}