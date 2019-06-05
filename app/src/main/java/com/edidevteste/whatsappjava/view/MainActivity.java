package com.edidevteste.whatsappjava.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.edidevteste.whatsapp.R;
import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mReferenceFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mReferenceFirebase = ConfiguracaoFirebase.getFirebase();
    }

    public void abrirCadastroUsuario(View view){
        startActivity(new Intent(MainActivity.this, CadastroUsuarioActivity.class));
    }
}
