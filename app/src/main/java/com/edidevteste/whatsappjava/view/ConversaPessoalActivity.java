package com.edidevteste.whatsappjava.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.Business.MensagemBusiness;
import com.edidevteste.whatsappjava.Security.PreferenceSecurity;
import com.edidevteste.whatsappjava.Util.Base64Custom;
import com.edidevteste.whatsappjava.Util.UtilConstantes;
import com.edidevteste.whatsappjava.Util.UtilGenerico;
import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.edidevteste.whatsappjava.entity.MensagemEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConversaPessoalActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mEditTextConversaMensagem;
    private ImageButton mImageButtonConversaSend;
    private ListView mListViewConversasPessoal;

    private String mNomeUsuarioDestinatario;
    private String mIdentificadorDestinatario;
    private String mIdUsuarioRemetente;
    private ArrayList<String> mListaMensagens;
    private ArrayAdapter arrayAdapterMensagens;

    private DatabaseReference mFirebaseReference;
    private ValueEventListener mValueEventListenerMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa_pessoal);

        inicializar();
        setlisteners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseReference.addValueEventListener(mValueEventListenerMensagem);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseReference.removeEventListener(mValueEventListenerMensagem);
    }

    private void inicializar(){
        mToolbar = findViewById(R.id.toolbar_conversa);
        mEditTextConversaMensagem = findViewById(R.id.editTextConversaMensagem);
        mImageButtonConversaSend = findViewById(R.id.imageButtonConversaSend);
        mListViewConversasPessoal = findViewById(R.id.listViewConversasPessoal);

        Bundle extra = getIntent().getExtras();
        if(extra!=null){
            mNomeUsuarioDestinatario = extra.getString(UtilConstantes.CONTATO_CONVERSA.getColuna1());
            mIdentificadorDestinatario = extra.getString(UtilConstantes.CONTATO_CONVERSA.getColuna2());
        }

        mIdUsuarioRemetente = new PreferenceSecurity(getApplicationContext()).recuperarUsuarioEmail64();

        mToolbar.setTitle(mNomeUsuarioDestinatario);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_left_24dp);
        setSupportActionBar(mToolbar);

        mFirebaseReference = ConfiguracaoFirebase.getFirebaseDataBase();

        inicializaMensagens();
    }

    private void inicializaMensagens(){
        mListaMensagens = new ArrayList<>();
        arrayAdapterMensagens = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,mListaMensagens);
        mListViewConversasPessoal.setAdapter(arrayAdapterMensagens);

        //Recupera as mensagens do FireBase
        mFirebaseReference = mFirebaseReference.child("mensagens").child( mIdUsuarioRemetente ).child( Base64Custom.CodificaTo64(mIdentificadorDestinatario) );

        //Criando o listner de mensagem
        mValueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListaMensagens.clear();
                for(DataSnapshot dado : dataSnapshot.getChildren()){
                    MensagemEntity mensagemEntity = dado.getValue(MensagemEntity.class);
                    if(mensagemEntity!=null){
                        if(mensagemEntity.getMensagem()!=null){
                            mListaMensagens.add(mensagemEntity.getMensagem());
                        }
                    }
                }
                arrayAdapterMensagens.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void setlisteners(){
        mImageButtonConversaSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String textoMensagem = mEditTextConversaMensagem.getText().toString();
                if(textoMensagem.isEmpty()){
                    UtilGenerico.msgGenerrica(getApplicationContext(), "Digite uma mensagem para Enviar!");
                }else{
                    MensagemEntity mensagemEntity = new MensagemEntity(mIdUsuarioRemetente, textoMensagem);

                    Log.i("SalvarMensagem", "mIdUsuarioRemetente: " + Base64Custom.DecodificaTo64(mIdUsuarioRemetente) + "/" + mIdentificadorDestinatario);

                    Boolean statusEnviar = salvarMensagem(mensagemEntity, Base64Custom.CodificaTo64(mIdentificadorDestinatario), mIdUsuarioRemetente);
                    if(statusEnviar){
                        mEditTextConversaMensagem.setText("");
                    }
                }

            }
        });
    }

    private Boolean salvarMensagem(MensagemEntity mensagemEntity, String identificadorDestinatario, String identificadorRemetente){
        try{

            new MensagemBusiness().salvaMensagem(mensagemEntity, identificadorDestinatario, identificadorRemetente);

            return true;
        }catch (Exception e){
            Log.e("ErrorSalvarMensagem", "Error: >" + e);
            return false;
        }
    }
}
