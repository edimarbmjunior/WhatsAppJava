package com.edidevteste.whatsappjava.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.edidevteste.javawhatsapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void abrirCadastroUsuario(View view){
        startActivity(new Intent(this, CadastroUsuarioActivity.class));
    }
}
