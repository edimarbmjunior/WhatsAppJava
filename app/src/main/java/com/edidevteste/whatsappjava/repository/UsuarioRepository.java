package com.edidevteste.whatsappjava.Repository;

import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.edidevteste.whatsappjava.entity.Contato;
import com.edidevteste.whatsappjava.entity.ConversaEntity;
import com.edidevteste.whatsappjava.entity.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsuarioRepository {

    private static DatabaseReference mDatabaseReference;
    private static FirebaseAuth mFirebaseAuth;

    public UsuarioRepository(){
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

    public void salvarUsuario(Usuario usuario){
        mDatabaseReference.child("usuarios").child(usuario.getId()).setValue(usuario);
    }

    public DatabaseReference getRecuperaInstanciaUsuario(String identificadorEmailBase64){
        return mDatabaseReference.child("usuarios").child(identificadorEmailBase64);
    }

    public Boolean emailExiste(String email){
        return mFirebaseAuth.isSignInWithEmailLink(email);
    }

    public void salvarContato(String usuarioLogado, String usuarioContato, Contato contato){
        mDatabaseReference.child("contatos").child(usuarioLogado).child(usuarioContato).setValue(contato);
    }

    public DatabaseReference getDadosContatos(String email64){
        return mDatabaseReference.child("contatos").child(email64);
    }

    public DatabaseReference getDadosConversa(String email64){
        return mDatabaseReference.child("conversas").child(email64);
    }

    public static ValueEventListener getDadosContatosEvent(final ArrayList<Contato> mlistaContatos, final ArrayAdapter arrayAdapter){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlistaContatos.clear();
                for(DataSnapshot dado : dataSnapshot.getChildren()){
                    Contato contato = dado.getValue(Contato.class);
                    mlistaContatos.add(contato);
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        return valueEventListener;
    }

    public static ValueEventListener getDadosConversaEvent(final ArrayList<ConversaEntity> mlistaConversa, final ArrayAdapter arrayAdapter){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlistaConversa.clear();
                for(DataSnapshot dado : dataSnapshot.getChildren()){
                    ConversaEntity conversaEntity = dado.getValue(ConversaEntity.class);
                    mlistaConversa.add(conversaEntity);
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        return valueEventListener;
    }

    public static ValueEventListener getDadosusuario(final String usuarioId, final Usuario dadosUsuario){
        mDatabaseReference = mDatabaseReference.child("usuarios").child(usuarioId);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                dadosUsuario.setSenha(usuario.getSenha());
                dadosUsuario.setNome(usuario.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        return valueEventListener;
    }
}
