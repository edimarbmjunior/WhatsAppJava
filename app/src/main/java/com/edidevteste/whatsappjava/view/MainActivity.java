package com.edidevteste.whatsappjava.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.Repository.UsuarioRepository;
import com.edidevteste.whatsappjava.Security.PreferenceSecurity;
import com.edidevteste.whatsappjava.Util.Base64Custom;
import com.edidevteste.whatsappjava.Util.UtilConstantes;
import com.edidevteste.whatsappjava.Util.UtilGenerico;
import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.edidevteste.whatsappjava.entity.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
    private DatabaseReference mFirebaseDatabase;
    private PreferenceSecurity mPreferenceSecurity;
    private Integer timerProcessamento = 0;
    private ValueEventListener mValueEventListener;

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

    @Override
    protected void onStop() {
        super.onStop();
        //mFirebaseDatabase.removeEventListener(mValueEventListener);
    }

    public void abrirCadastroUsuario(View view){
        startActivity(new Intent(this, CadastroUsuarioActivity.class));
    }

    private void inicializar(){
        editLoginEmail = findViewById(R.id.editLoginEmail);
        editLoginSenha = findViewById(R.id.editLoginSenha);
        buttonLoginCadastro = findViewById(R.id.buttonLoginCadastro);
        mAutenticacaoFirebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        mFirebaseDatabase = ConfiguracaoFirebase.getFirebaseDataBase();
        mPreferenceSecurity = new PreferenceSecurity(this);
        usuario = new Usuario();
    }

    private void validaUsuarioLogado(){
        if(mAutenticacaoFirebaseAuth.getCurrentUser()!=null && mPreferenceSecurity.recuperarValorUnicoPrefences(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna4())!=null){
            Log.i("LoginL", "Usuario Existe na autenticação - " + mPreferenceSecurity.recuperarUsuarioEmail64());
            Log.i("LoginL", "Usuario Existe na autenticação - " + Base64Custom.DecodificaTo64(mPreferenceSecurity.recuperarUsuarioEmail64()));
            usuario.setEmail(Base64Custom.DecodificaTo64(mPreferenceSecurity.recuperarUsuarioEmail64()));
            timerProcessamento = 4;
        }else{
            List<String> dadosUsuario = new ArrayList<>();
            dadosUsuario.add(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna4());
            dadosUsuario.add(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna2());
            dadosUsuario.add(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna3());
            HashMap<String, String> dadosRecuperados = mPreferenceSecurity.recuperarValoresUnicoPrefences(dadosUsuario);
            if(!dadosRecuperados.isEmpty()){
                //recuperaUsuario();
                //usuario = new Usuario(dadosRecuperados.get(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna4()), dadosRecuperados.get(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna2()), Base64Custom.DecodificaTo64(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna4()), dadosRecuperados.get(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna3()));
                Log.i("LoginL", "Usuario Existe na SharedPreferences.");
                usuario.setEmail(Base64Custom.DecodificaTo64(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna4()));
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
                        Log.i("LoginL", "Usuario Existe na SharedPreferences.");
                        timerProcessamento = 4;
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
                        UtilGenerico.msgGenerrica(MainActivity.this, erroSign);
                    }
                }
            });
    }

    private void recupedadosFirebase(){
        mAutenticacaoFirebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha());
        timerProcessamento = 1;
    }

    private void chamarTelaPrincipal(){
        startActivity(new Intent(this, PrincipalActivity.class));
        finish();
        timerProcessamento = 0;
    }

    private void salvarDadosSharedOreferences(){
        HashMap dadosUsuario = new HashMap();
        dadosUsuario.put(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna1(), mAutenticacaoFirebaseAuth.getCurrentUser().getUid());
        dadosUsuario.put(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna2(), usuario.getNome());
        dadosUsuario.put(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna4(), Base64Custom.CodificaTo64(usuario.getEmail()));
        Log.i("LoginL", "IdUsuario: " + mAutenticacaoFirebaseAuth.getCurrentUser().getUid());
        Log.i("LoginL", "email:     " + mAutenticacaoFirebaseAuth.getCurrentUser().getEmail());
        Log.i("LoginL", "Usuario:   " + usuario.toString());
        Log.i("LoginL", "Email64:   " + Base64Custom.CodificaTo64(usuario.getEmail()));
        mPreferenceSecurity.salvarValoresPreferences(dadosUsuario);
        timerProcessamento = 1;
    }

    private void inicializaValueEvent(){
        mValueEventListener = UsuarioRepository.getDadosusuario(Base64Custom.CodificaTo64(usuario.getEmail()), usuario);
    }

    private void recuperaUsuario(){
        //inicializaValueEvent();
        mFirebaseDatabase = ConfiguracaoFirebase.getFirebaseDataBase().child("usuarios").child(Base64Custom.CodificaTo64(usuario.getEmail()));

        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuarioRecuperado = dataSnapshot.getValue(Usuario.class);

                usuario.setNome(usuarioRecuperado.getNome());
                Log.i("LoginL", "UsuarioRecuperado: " + usuarioRecuperado);
                Log.i("LoginL", "Usuario: " + usuario);
                timerProcessamento = 2;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void verificarProcessamento(){
        if(timerProcessamento > 0){
            switch(timerProcessamento){
                case 1:
                    Log.i("LoginL", "Chama a tela principal.");
                    chamarTelaPrincipal();
                    Log.i("LoginL", "Próximo passo " + 0);
                    break;
                case 2:
                    Log.i("LoginL", "Salva dados no SharedPreferences.");
                    salvarDadosSharedOreferences();
                    Log.i("LoginL", "Próximo passo " + 1);
                    break;
                case 3:
                    Log.i("LoginL", "Vai no Firebase recuperar dados já existentes.");
                    recupedadosFirebase();
                    Log.i("LoginL", "Próximo passo " + 1);
                    break;
                case 4:
                    Log.i("LoginL", "Recupera usuário.");
                    recuperaUsuario();
                    Log.i("LoginL", "Próximo passo " + 2);
                default:
                    Log.i("LoginL", "Próximo passo " + 0);
                    timerProcessamento = 0;
                    break;
            }
        }
    }

    class Task extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    verificarProcessamento();
                }
            });
        }
    }
}
