package com.example.password_keychain_app;

public class User {
    public String name,email,pk;

    public User(){

    }
    public User(String name, String email,String pk){
        this.name = name;
        this.email = email;
        this.pk = pk;

    }

    public void setPk(String publickey){
        this.pk = publickey;
    }
    public String getPk(){
        return this.pk;
    }


}
