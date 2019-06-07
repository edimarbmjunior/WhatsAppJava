package com.edidevteste.whatsappjava.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.Security.PreferenceSecurity;
import com.edidevteste.whatsappjava.Util.UtilConstantes;
import com.edidevteste.whatsappjava.Util.UtilGenerico;
import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.edidevteste.whatsappjava.entity.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private EditText editLoginEmail;
    private EditText editLoginSenha;
    private ImageButton buttonLoginCadastro;
    private Usuario usuario;

    private FirebaseAuth mAutenticacaoFirebaseAuth;
    private PreferenceSecurity mPreferenceSecurity;
    private Integer timerProcessamento = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializar();
        validaUsuarioLogado();

        /*Usuario usuario = new Usuario("1112244", "Teste", "edimar@gmail.com", "123456");
        new UsuarioRepository().salvarUsuario(usuario);
        Log.i("FirebaseDatabase", "Teste de inclusão");*/
        setlisteners();
        Timer();
    }

    public void abrirCadastroUsuario(View view){
        startActivity(new Intent(this, CadastroUsuarioActivity.class));
    }

    private void inicializar(){
        editLoginEmail = findViewById(R.id.editLoginEmail);
        editLoginSenha = findViewById(R.id.editLoginSenha);
        buttonLoginCadastro = findViewById(R.id.buttonLoginCadastro);
        mAutenticacaoFirebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        mPreferenceSecurity = new PreferenceSecurity(this);
    }

    private void validaUsuarioLogado(){
        if(mAutenticacaoFirebaseAuth.getCurrentUser()!=null){
            chamarTelaPrincipal();
        }else{
            List<String> dadosUsuario = new ArrayList<>();
            dadosUsuario.add(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna1());
            dadosUsuario.add(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna2());
            dadosUsuario.add(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna3());
            HashMap<String, String> dadosRecuperados = mPreferenceSecurity.recuperarValoresUnicoPrefences(dadosUsuario);
            if(!dadosRecuperados.isEmpty()){
                usuario = new Usuario(dadosRecuperados.get(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna1()), null, dadosRecuperados.get(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna2()), dadosRecuperados.get(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna3()));
                timerProcessamento = 3;
            }
        }
    }

    private void setlisteners(){
        buttonLoginCadastro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                logar();
            }
        });
    }

    public void Timer(){
        Timer timer = new Timer();
        MainActivity.Task task = new MainActivity.Task();

        timer.schedule(task, 1000, 1000);
    }

    private void logar(){
        usuario = new Usuario();
        usuario.setEmail(editLoginEmail.getText().toString());
        usuario.setSenha(editLoginSenha.getText().toString());

        mAutenticacaoFirebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        UtilGenerico.msgGenerrica(MainActivity.this, "Sucesso no login!");
                        timerProcessamento = 2;
                    }else{
                        String erroSign = "";
                        try{
                            throw task.getException();
                        }catch (FirebaseAuthInvalidUserException firebaseAuthInvalidUserException){
                            erroSign = "O e-mail usado é inválido!";
                        }catch (FirebaseAuthInvalidCredentialsException fireInvalidCredential){
                            erroSign = "A senha é inválida!";
                        }
                        catch (Exception e){
                            Log.e("Error(logar)", "Error(Generico): > " + e);
                            erroSign = "Erro na comunicação com servidor, caso repita entre em contato!";
                        }
                        Log.e("Error(logar)", "Error: > " + erroSign);
                        UtilGenerico.msgGenerrica(MainActivity.this, "Error no login!");
                    }
                }
            });
    }

    private void recupedadosFirebase(){
        mAutenticacaoFirebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha());
    }

    private void chamarTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
    }

    private void salvarDadosSharedOreferences(){
        HashMap dadosUsuario = new HashMap();
        dadosUsuario.put(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna1(), usuario.getId());
        dadosUsuario.put(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna2(), usuario.getEmail());
        dadosUsuario.put(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna3(), usuario.getSenha());
        mPreferenceSecurity.salvarValoresPreferences(dadosUsuario);
    }

    class Task extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(timerProcessamento > 0){
                        switch(timerProcessamento){
                            case 1:
                                chamarTelaPrincipal();
                                timerProcessamento = 0;
                                break;
                            case 2:
                                salvarDadosSharedOreferences();
                                timerProcessamento = 1;
                                break;
                            case 3:
                                recupedadosFirebase();
                                timerProcessamento = 1;
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
}
