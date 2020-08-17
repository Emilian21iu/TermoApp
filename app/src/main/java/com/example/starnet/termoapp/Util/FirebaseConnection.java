package com.example.starnet.termoapp.Util;

import com.firebase.client.Firebase;

public class FirebaseConnection extends Firebase {

    private static FirebaseConnection instance;

    private FirebaseConnection(String url) {
        super(url);
    }

    public static FirebaseConnection getInstance(){
        if(instance == null){
            instance = new FirebaseConnection("https://termoapp-d995c.firebaseio.com");
        }
        return instance;
    }
}
