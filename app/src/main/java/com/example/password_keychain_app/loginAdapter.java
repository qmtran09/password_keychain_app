package com.example.password_keychain_app;

import androidx.annotation.NonNull;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

//https://www.geeksforgeeks.org/how-to-populate-recyclerview-with-firebase-data-using-firebaseui-in-android-studio/

public class loginAdapter extends FirebaseRecyclerAdapter<LoginInfo, loginAdapter.loginViewholder> {

    private String privateKey;

    public loginAdapter(
            @NonNull FirebaseRecyclerOptions<LoginInfo> options,String privateKey)

    {
        super(options);
        System.out.println("1");
        this.privateKey= privateKey;

    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")

    @Override
    protected void
    onBindViewHolder(@NonNull loginViewholder holder,
                     int position, @NonNull LoginInfo model)
    {

        decryptPassword decryptor = new decryptPassword();
        String service = model.getService();
        String username = model.getUsername();
        String password = model.getPassword();
        try{
            service = decryptor.decrypt(model.getService(), privateKey);
            username = decryptor.decrypt(model.getUsername(), privateKey);
            password = decryptor.decrypt(model.getPassword(), privateKey);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("CATCHED EXECPTION");
        }
        System.out.println(service+username+password);
        System.out.println("inisde adapter"+privateKey);
        // Add firstname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.service.setText(service);

        // Add lastname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.username.setText(username);

        // Add age from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.password.setText(password);
    }


    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public loginViewholder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.login, parent, false);
        return new loginAdapter.loginViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class loginViewholder
            extends RecyclerView.ViewHolder {
        TextView service, username, password;
        public loginViewholder(@NonNull View itemView)
        {
            super(itemView);


            service  = itemView.findViewById(R.id.serviceName);
            username = itemView.findViewById(R.id.username);
            password = itemView.findViewById(R.id.password);
        }
    }
}
