package com.edidevteste.whatsappjava.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.Security.Permissao;
import com.edidevteste.whatsappjava.Security.PreferenceSecurity;
import com.edidevteste.whatsappjava.Util.UtilConstantes;
import com.edidevteste.whatsappjava.Util.UtilGenerico;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    private EditText nome;
    private EditText telDdi;
    private EditText telDdd;
    private EditText telefone;
    private ImageButton buttonCadstrar;

    //Salvar dados na SharedPreference
    PreferenceSecurity sharedPreference;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.INTERNET
    };
    private int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        validaExistenciaUsuario();

        //RequestCode - Para verificar se a sua activity tem permissão, cada activity de seu request
        Permissao.validaPermissoes(requestCode, this, permissoesNecessarias);

        montaMascara();
        setListeners();
    }

    private void setListeners(){
        buttonCadstrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fazCritica()){
                    String[] valorDigitado = valoreString();
                    validaUsuario(valorDigitado);
                }
            }
        });
    }

    private void montaMascara(){
        nome = findViewById(R.id.editNomeCadastro);
        telDdi = findViewById(R.id.editDDITelefoneCadastro);
        telDdd = findViewById(R.id.editDDDTelefoneCadastro);
        telefone = findViewById(R.id.editNumTelefoneCadastro);
        buttonCadstrar = findViewById(R.id.buttonSalvarCadastro);

        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("+NN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(telDdi, simpleMaskFormatter);

        telDdi.addTextChangedListener(maskTextWatcher);

        simpleMaskFormatter = new SimpleMaskFormatter("NN");
        maskTextWatcher = new MaskTextWatcher(telDdd, simpleMaskFormatter);

        telDdd.addTextChangedListener(maskTextWatcher);

        simpleMaskFormatter = new SimpleMaskFormatter("NNNN-NNNNN");
        maskTextWatcher = new MaskTextWatcher(telefone, simpleMaskFormatter);

        telefone.addTextChangedListener(maskTextWatcher);
    }

    private void validaExistenciaUsuario(){
        sharedPreference = new PreferenceSecurity(getApplicationContext());

        if(sharedPreference.recuperarValorUnicoPrefences(UtilConstantes.USUARIO_DADOS.getColuna4()) == "S"){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private String[] valoreString(){
        String telefoneSemFormatação = "";
        String nome = "";
        try{
            nome = getNome().toString();
            String telefoneCompleto = telDdi.getText().toString() + telDdd.getText().toString() + telefone.getText().toString();
            telefoneSemFormatação = telefoneCompleto.replace("+", "")
                                                    .replace("-", "")
                                                    .replace(" ", "");

            Log.i("Valores", "V:" + nome+"/"+telefoneSemFormatação);
        }catch (Exception e){
            Toast.makeText(this, "Error na recuperação dos dados!", Toast.LENGTH_LONG).show();
            Log.e("Error(valoresString())", "Error:" + e);
        }
        String[] valores = {nome, telefoneSemFormatação};

        return valores;
    }

    private boolean fazCritica(){
        if(getNome().toString().isEmpty() ||
                telDdi.getText().toString().isEmpty() ||
                telDdd.getText().toString().isEmpty() ||
                telefone.getText().toString().isEmpty()){
            UtilGenerico.msgGenerrica(this, "Existe campo(s) vazios!");
            return false;
        }
        if(getNome().toString().matches("[0-9]+")){
            UtilGenerico.msgGenerrica(this, "Por favor, verificar os campos!");
            return false;
        }
        if((telDdi.getText().toString().length() != 3 && telDdi.getText().toString().length() != 2) ||
                (telDdd.getText().toString().length() != 2 && telDdd.getText().toString().length() != 3) ||
                telefone.getText().toString().length() != 10){
            UtilGenerico.msgGenerrica(this, "O telefone repassado contém erro na quantidade de números!");
            return false;
        }

        Log.i("Valida", "Validou os dados.");
        return true;
    }

    private void validaUsuario(String[] valorDigitado){

        //Gerar Tpken
        Random randomico = new Random();
        //Gerar um numero entre 1000 e 9999
        int numeroRandomico = randomico.nextInt( 9999 - 1000 ) + 1000;

        String token = String.valueOf(numeroRandomico);
        String mensagem = "WhatsApp - Código de verificação: " + token;

        Log.i("Token", "T: >" + token);

        //Salvar dados na SharedPreference
        sharedPreference = new PreferenceSecurity(getApplicationContext());
        sharedPreference.salvarValorPreferences(UtilConstantes.USUARIO_DADOS.getColuna1(), token );
        sharedPreference.salvarValorPreferences(UtilConstantes.USUARIO_DADOS.getColuna2(), valorDigitado[0] );
        sharedPreference.salvarValorPreferences(UtilConstantes.USUARIO_DADOS.getColuna3(), valorDigitado[1] );

        //TODO Mock para numero do celular no emulator do celular
        /*String numeroMockEmulador = String.valueOf(5554);
        valorDigitado[1] = numeroMockEmulador;*/

        //Enviar SMS
        /*Log.i("Telefone", "Numero:" + numeroMockEmulador);*/
        boolean statusSms = enviaSMS("+" + valorDigitado[1], mensagem);

        Log.i("SMS", "SMS:" + statusSms);

        if(statusSms){
            startActivity(new Intent(LoginActivity.this, ValidadorActivity.class));
            finish();
        }else{
            Toast.makeText(this, "Problema ao enviar o SMS, tente novamente!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean enviaSMS(String telefone, String mensagem){
        boolean statusEnvio = false;
        try{

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);
            statusEnvio = true;

        }catch (Exception e){
            Log.e("Error(enviaSMS)", "Error: >" + e);
            e.printStackTrace();
            statusEnvio = false;
        }finally {
            return statusEnvio;
        }
    }

    //Tratamento da negação das permissões do aplicativo
    public void onRequestPermissionsResult(int requestCode, String[] permissoes, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissoes, grantResults);

        for(int resultado : grantResults){
            if(resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaPermissao();
            }
        }
    }

    private void alertaValidacaPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar o app, é necessário autorizar as permissões.");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public EditText getNome() {
        return nome;
    }
}
