package com.edidevteste.whatsappjava.Repository;

import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.edidevteste.whatsappjava.entity.ConversaEntity;
import com.edidevteste.whatsappjava.entity.MensagemEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

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

    public void salvarConversa(ConversaEntity conversaEntity, String identificadorDestinatario, String identificadorRemetente){
        mDatabaseReference.child("conversas")
                .child( identificadorDestinatario )
                .child( identificadorRemetente )
                .setValue(conversaEntity);
    }
}
