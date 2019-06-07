package com.edidevteste.whatsappjava.Repository;

import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.edidevteste.whatsappjava.entity.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class UsuarioRepository {

    private static DatabaseReference mDatabaseReference;
    private static FirebaseAuth mFirebaseAuth;
    public UsuarioRepository(){
        getMDatabaseReference();
        getMFirebaseAuth();
    }

    public void salvarUsuario(Usuario usuario){
        mDatabaseReference.child("usuarios").child(usuario.getId()).setValue(usuario);
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

    public Boolean emailExiste(String email){
        return mFirebaseAuth.isSignInWithEmailLink(email);
    }
}
