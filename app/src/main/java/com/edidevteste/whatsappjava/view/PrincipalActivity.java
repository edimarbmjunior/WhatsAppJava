package com.edidevteste.whatsappjava.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.Security.PreferenceSecurity;
import com.edidevteste.whatsappjava.Util.UtilConstantes;
import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PrincipalActivity extends AppCompatActivity {

    private Button buttonSair;
    private FirebaseAuth mFirebaseAuth;
    private PreferenceSecurity mPreferenceSecurity;
    private Integer timerProcessamento = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        mFirebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        inicializar();

        setLinerters();
        Timer();
    }

    private void inicializar(){
        buttonSair = findViewById(R.id.buttonPrincipalSair);
        mPreferenceSecurity = new PreferenceSecurity(this);
    }

    private void setLinerters(){
        buttonSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Logout
                timerProcessamento = 2;
            }
        });
    }

    public void Timer(){
        Timer timer = new Timer();
        PrincipalActivity.Task task = new PrincipalActivity.Task();
        timer.schedule(task, 1000, 1000);
    }

    class Task extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(timerProcessamento > 0){
                        switch (timerProcessamento){
                            case 2:
                                logout();
                                break;
                            default:
                                timerProcessamento = 0;
                                break;
                        }
                    }
                }
            });
        }
    }

    private void logout(){
        List<String> dadosUsuario = new ArrayList<>();
        dadosUsuario.add(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna1());
        dadosUsuario.add(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna2());
        dadosUsuario.add(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna3());
        mPreferenceSecurity.removerValoresPreferencesUsuario(dadosUsuario);
        mFirebaseAuth.signOut();
        timerProcessamento = 0;
        startActivity(new Intent(PrincipalActivity.this, MainActivity.class));
        finish();
    }
}
