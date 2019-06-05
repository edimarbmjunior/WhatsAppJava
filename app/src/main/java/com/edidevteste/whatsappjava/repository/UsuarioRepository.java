package com.edidevteste.whatsappjava.repository;

import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.edidevteste.whatsappjava.entity.Usuario;
import com.google.firebase.database.DatabaseReference;

import java.security.PublicKey;

public class UsuarioRepository {

    private static DatabaseReference mDatabaseReference = ConfiguracaoFirebase.getFirebase();

    public UsuarioRepository(){}

    public void salvarUsuario(Usuario usuario){
        mDatabaseReference.child("usuarios").child(usuario.getId()).setValue(usuario);
    }
}
