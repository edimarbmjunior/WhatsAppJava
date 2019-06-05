package com.edidevteste.whatsappjava.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edidevteste.whatsapp.R;
import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.edidevteste.whatsappjava.entity.Usuario;
import com.edidevteste.whatsappjava.repository.UsuarioRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button buttonSalvar;
    private Usuario usuario;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        //Botaão voltar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextNome = findViewById(R.id.editCadastroNome);
        editTextEmail = findViewById(R.id.editCadastroEmail);
        editTextSenha = findViewById(R.id.editCadastroSenha);
        buttonSalvar = findViewById(R.id.buttonCadastro);

        setListeners();

    }

    //Setando a função do click do button voltar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item!=null && item.getItemId() == android.R.id.home) {
            Log.i("Finish", "Finalizado CadastroUsuarioActivity");
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListeners(){
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarUsuario();
            }
        });
    }

    private void cadastrarUsuario(){
        if(validaEntrada()){
            usuario = new Usuario();
            usuario.setNome(editTextNome.getText().toString());
            usuario.setEmail(editTextEmail.getText().toString());
            usuario.setSenha(editTextSenha.getText().toString());
            try{
                mFirebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
                mFirebaseAuth.createUserWithEmailAndPassword( usuario.getEmail(),usuario.getSenha())
                        .addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Usuário cadastrado!", Toast.LENGTH_LONG).show();
                                    usuario.setId(task.getResult().getUser().getUid());
                                }else{
                                    Toast.makeText(getApplicationContext(), "Usuário não cadastrado!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                new UsuarioRepository().salvarUsuario(usuario);
            }catch (Exception e){
                Log.e("Error(cadastrarUsuario)", "Error:>" + e);
                Toast.makeText(this, "Error ao processar dados. Tente novamente!", Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this, "Dados inválidos!", Toast.LENGTH_LONG).show();
        }
    }

    private Boolean validaEntrada(){
        if(!editTextNome.getText().toString().isEmpty() &&
                !editTextEmail.getText().toString().isEmpty() &&
                !editTextSenha.getText().toString().isEmpty()){
            return false;
        }
        return true;
    }
}
