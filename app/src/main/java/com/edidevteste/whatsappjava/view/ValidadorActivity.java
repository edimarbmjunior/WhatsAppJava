package com.edidevteste.whatsappjava.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.Security.PreferenceSecurity;
import com.edidevteste.whatsappjava.Util.UtilConstantes;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ValidadorActivity extends AppCompatActivity {

    private EditText codigoValidador;
    private Button buttonValidador;
    PreferenceSecurity mPreferenceSecurity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        inicializar();
        setListeners();
    }

    private void inicializar(){
        codigoValidador = findViewById(R.id.editTextValidadorCodigo);
        buttonValidador = findViewById(R.id.buttonValidadorValidar);

        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(codigoValidador, simpleMaskFormatter);

        codigoValidador.addTextChangedListener(maskTextWatcher);

        mPreferenceSecurity = new PreferenceSecurity(this);
    }

    private void setListeners(){
        buttonValidador.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                validaToken();

            }
        });
    }

    private void validaToken(){
        List<String> valoresUsuarioToken = new ArrayList();
        valoresUsuarioToken.add(UtilConstantes.USUARIO_DADOS.getColuna1());
        valoresUsuarioToken.add(UtilConstantes.USUARIO_DADOS.getColuna2());
        valoresUsuarioToken.add(UtilConstantes.USUARIO_DADOS.getColuna3());
        HashMap<String, String> sharedUsuario = mPreferenceSecurity.recuperarValoresUnicoPrefences(valoresUsuarioToken);

        String token = sharedUsuario.get(UtilConstantes.USUARIO_DADOS.getColuna1());
        String tokenDigitado = codigoValidador.getText().toString();

        if(!tokenDigitado.isEmpty() && tokenDigitado.equals(token)){
            mPreferenceSecurity.salvarValorPreferences(UtilConstantes.USUARIO_DADOS.getColuna4(), "S");
            Toast.makeText(this, "Token valdiado!", Toast.LENGTH_LONG).show();
        }else{
            Log.i("Token", "Token:>" + token);
            Log.i("Token", "TokenDigitado:>" + tokenDigitado);
            Toast.makeText(this, "Token não valdiado!", Toast.LENGTH_LONG).show();
        }
    }
}
