package com.example.password_keychain_app;

public class LoginInfo {
    private String username,password,service;

    public LoginInfo(){

    }

    public LoginInfo(String username,String password, String service){
        this.username = username;
        this.password = password;
        this.service = service;
    }

    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public String getService(){
        return this.service;
    }
}
