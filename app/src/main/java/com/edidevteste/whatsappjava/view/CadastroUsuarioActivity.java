package com.edidevteste.whatsappjava.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.Business.UsuarioBusiness;
import com.edidevteste.whatsappjava.Repository.UsuarioRepository;
import com.edidevteste.whatsappjava.Util.UtilGenerico;
import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.edidevteste.whatsappjava.entity.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Timer;
import java.util.TimerTask;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button buttonSalvar;
    private Usuario usuario;

    private FirebaseAuth mFirebaseAuth;
    UsuarioBusiness mUsuarioBusiness = new UsuarioBusiness();
    private Integer processarSalvamento = 0;
    private Usuario usuarioSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        //Botaão voltar
        /*if(this.getSupportActionBar()!=null){
            this.getSupportActionBar().setDisplayShowHomeEnabled(true);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }*/

        editTextNome = findViewById(R.id.editCadastroNome);
        editTextEmail = findViewById(R.id.editCadastroEmail);
        editTextSenha = findViewById(R.id.editCadastroSenha);
        buttonSalvar = findViewById(R.id.buttonCadastro);
        mFirebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        setListeners();
        Timer();
    }

    //Setando a função do click do button voltar
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item!=null && item.getItemId() == android.R.id.home) {
            Log.i("Finish", "Finalizado CadastroUsuarioActivity");
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void setListeners(){
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Error(buttonSalvar)", "Inicializando o cadastro");
                cadastrarUsuario();
                Log.i("Error(buttonSalvar)", getString(R.string.finalizando_cadastro));
            }
        });
    }

    private void cadastrarUsuario(){
        usuario = new Usuario();
        usuario.setNome(editTextNome.getText().toString());
        usuario.setEmail(editTextEmail.getText().toString());
        usuario.setSenha(editTextSenha.getText().toString());
        String retorno = mUsuarioBusiness.validaUsuarioAntesDaInclusao(usuario);

        if(retorno=="0"){
            try{
                mFirebaseAuth.createUserWithEmailAndPassword( usuario.getEmail(),usuario.getSenha())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    usuario.setId(task.getResult().getUser().getUid());
                                    if(usuario.getId()!=null && usuario.getId().length()>0){
                                        usuarioSalvar = usuario;
                                        usuario = new Usuario();
                                        processarSalvamento = 1;
                                    }
                                }else{
                                    String erroExcecao="";
                                    try{
                                        throw task.getException();
                                    }catch (FirebaseAuthWeakPasswordException eWeakPassword){
                                        erroExcecao = "Digite uma senha mais forte, com letras e numeros!";
                                    }catch (FirebaseAuthInvalidCredentialsException eInvalidCredential){
                                        erroExcecao = "O e-mail digitado é inválido, digite um novo!";
                                    }catch (FirebaseAuthUserCollisionException eUserCollision){
                                        erroExcecao = "Esse e-mail já está em uso no app!";
                                    }catch (Exception e){
                                        Log.e("Error(buttonSalvar)", "Error(Generico): >" + e);
                                        erroExcecao = "Erro no cadastro, se continuar entre em contato!";
                                    }

                                    Log.e("Error(buttonSalvar)", "Error: >" + erroExcecao);
                                    UtilGenerico.msgGenerrica(CadastroUsuarioActivity.this, getString(R.string.dados_invalidos) + erroExcecao);
                                }
                            }
                        });
                        /*.continueWith(new Continuation<AuthResult, Object>() {
                            @Override
                            public Object then(@NonNull Task<AuthResult> task) throws Exception {
                                if(task.isSuccessful()){
                                    usuario.setId(task.getResult().getUser().getUid());
                                }else{
                                    Log.i("Error(buttonSalvar-1)", "Error: >" + task.getException());
                                    UtilGenerico.msgGenerrica(CadastroUsuarioActivity.this, getString(R.string.erro_processar_dados) + "Tente novamente!");
                                }
                                return null;
                            }
                        });*/
            }catch (Exception e){
                Log.e("Error(buttonSalvar-2)", "Error:>" + e);
                UtilGenerico.msgGenerrica(this, getString(R.string.erro_processar_dados) + "Tente novamente!");
            }
        }else{
            Log.e("Error(buttonSalvar-3)", "Erro na validação do usuário. " + retorno);
            UtilGenerico.msgGenerrica(this, getString(R.string.dados_invalidos) + retorno);
        }

    }

    public void Timer(){
        Timer timer = new Timer();
        Task task = new Task();

        timer.schedule(task, 1000, 1000);
    }

    class Task extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(processarSalvamento == 1){
                        processarSalvamento = 0;
                        try{
                            new UsuarioRepository().salvarUsuario(usuarioSalvar);
                            mFirebaseAuth.signOut();
                            UtilGenerico.msgGenerrica(CadastroUsuarioActivity.this, getString(R.string.usuario_cadastrado) /*+  "Identificador. " + usuarioSalvar.getId()*/);
                            Log.i("Error(buttonSalvar)", getString(R.string.finalizando_cadastro));
                            finish();
                        }catch (Exception e){
                            Log.e("Error(buttonSalvar)", "Error: >" + e);
                            UtilGenerico.msgGenerrica(CadastroUsuarioActivity.this, getString(R.string.erro_salvar_dados));
                        }
                    }
                }
            });
        }
    }
}
