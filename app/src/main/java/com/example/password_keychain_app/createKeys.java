package com.example.password_keychain_app;

import java.security.*;

public class createKeys{

    private PrivateKey privateKey;
    private PublicKey publicKey;
    //constructor

    public createKeys(){
        //using RSA algorithm to generate keys
        try{
            KeyPairGenerator key = KeyPairGenerator.getInstance("RSA");

            //using key size pf 2048-bit (recomended in NIST)
            key.initialize(2048);

            KeyPair kPair = key.genKeyPair();
            this.privateKey = kPair.getPrivate();
            this.publicKey = kPair.getPublic();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }

    }

    //get public key
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    //get private key
    public PublicKey getPublicKey() {
        return publicKey;
    }


}
