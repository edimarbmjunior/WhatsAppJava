package com.edidevteste.whatsappjava.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class ConfiguracaoFirebase {

    private static DatabaseReference mReferenceFirebase;
    private static FirebaseAuth mFirebaseAuth;

    private ConfiguracaoFirebase(){}

    public static DatabaseReference getFirebase(){
        if(mReferenceFirebase==null){
            mReferenceFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return mReferenceFirebase;
    }

    public static FirebaseAuth getFirebaseAuth(){
        if(mFirebaseAuth==null){
            mFirebaseAuth = FirebaseAuth.getInstance();
        }
        return mFirebaseAuth;
    }
}
