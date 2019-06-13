package com.edidevteste.whatsappjava.Repository;

import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.edidevteste.whatsappjava.Util.Base64Custom;
import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.edidevteste.whatsappjava.entity.MensagemEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MensagemRepository {

    private static DatabaseReference mDatabaseReference;
    private static FirebaseAuth mFirebaseAuth;

    public MensagemRepository(){
        getMDatabaseReference();
        getMFirebaseAuth();
    }

    public DatabaseReference getMDatabaseReference(){
        if(mDatabaseReference == null){
            mDatabaseReference = ConfiguracaoFirebase.getFirebaseDataBase();
        }
        return mDatabaseReference;
    }

    public FirebaseAuth getMFirebaseAuth(){
        if(mFirebaseAuth == null){
            mFirebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        }
        return mFirebaseAuth;
    }

    public void salvaMensagem(MensagemEntity mensagemEntity, String identificadorDestinatario, String identificadorRemetente){
        mDatabaseReference.child("mensagens")
                .child( identificadorRemetente )
                .child( identificadorDestinatario )
                .push()
                .setValue(mensagemEntity);
    }
}
