package com.avraam.smartlist.models;

import com.google.firebase.firestore.FirebaseFirestore;

public class FireStoreDb {

    private static FirebaseFirestore fireStoreDb_instance;

    public static FirebaseFirestore FireStoreDb(){

        if( fireStoreDb_instance == null){
            fireStoreDb_instance = FirebaseFirestore.getInstance();
        }
        return fireStoreDb_instance;
    }



}
