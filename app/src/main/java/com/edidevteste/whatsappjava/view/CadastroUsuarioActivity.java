package com.edidevteste.whatsappjava.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.Repository.UsuarioRepository;
import com.edidevteste.whatsappjava.Util.UtilGenerico;
import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.edidevteste.whatsappjava.entity.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button buttonSalvar;
    private Usuario usuario;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

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
                cadastrarUsuario();
            }
        });
    }

    private void cadastrarUsuario(){
        usuario = new Usuario();
        usuario.setNome(editTextNome.getText().toString());
        usuario.setEmail(editTextEmail.getText().toString());
        usuario.setSenha(editTextSenha.getText().toString());

        if(validaEntrada(usuario)){
            try{
                mFirebaseAuth.createUserWithEmailAndPassword( usuario.getEmail(),usuario.getSenha())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    usuario.setId(task.getResult().getUser().getUid());
                                    if(usuario.getId()!=null && usuario.getId().length()>0){
                                        try{
                                            new UsuarioRepository(). salvarUsuario(usuario);
                                        }catch (Exception e){
                                            Log.i("Error(cadastrarUsuario)", "Error: >" + e);
                                            UtilGenerico.msgGenerrica(CadastroUsuarioActivity.this, getString(R.string.erro_salvar_dados).toString());
                                        }
                                        UtilGenerico.msgGenerrica(CadastroUsuarioActivity.this, getString(R.string.usuario_cadastrado) +  "Identificador. " + usuario.getId());
                                    }
                                }else{
                                    Log.i("Error(cadastrarUsuario)", "Error: >" + task.getException());
                                    UtilGenerico.msgGenerrica(CadastroUsuarioActivity.this, getString(R.string.erro_processar_dados) + "Tente novamente!");
                                }
                            }
                        });
            }catch (Exception e){
                Log.e("Error(cadastrarUsuario)", "Error:>" + e);
                UtilGenerico.msgGenerrica(this, "Error ao processar dados. Tente novamente!");
            }
        }else{
            UtilGenerico.msgGenerrica(this, "Dados inválidos!");
        }

    }

    private Boolean validaEntrada(Usuario usuario){
        if(usuario == null){
            return false;
        }
        if(usuario.getNome().isEmpty() &&
                usuario.getEmail().isEmpty() &&
                usuario.getSenha().isEmpty()){
            return false;
        }
        return true;
    }
}
