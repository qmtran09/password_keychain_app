package com.example.password_keychain_app;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String name,email,pk;
    //public HashMap<String,LoginInfo> loginsMap;

    public User(){

    }
    public User(String name, String email,String pk){
        //LoginInfo dummy = new LoginInfo("dummy","dummy");
        //loginsMap.put("dummy",dummy);
        this.name = name;
        this.email = email;
        this.pk = pk;


    }





}
