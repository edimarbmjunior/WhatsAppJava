package com.edidevteste.whatsappjava.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.Security.PreferenceSecurity;
import com.edidevteste.whatsappjava.entity.MensagemEntity;

import java.util.ArrayList;
import java.util.List;

public class MensagemAdapter extends ArrayAdapter<MensagemEntity> {

    private Context mContext;
    private ArrayList<MensagemEntity> mListaMensagem;


    public MensagemAdapter(Context context, ArrayList<MensagemEntity> objects) {
        super(context, 0, objects);
        this.mContext = context;
        this.mListaMensagem = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(mListaMensagem!=null){

            //Recupera os dados do usuario remetente
            String idUsuarioRemetente = new PreferenceSecurity(mContext).recuperarUsuarioEmail64();

            //Recupera a Mensagem
            MensagemEntity mensagemEntity = mListaMensagem.get(position);

            //Inicializa o objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

            //Monta a view a partir de um XML
            if(idUsuarioRemetente.equals(mensagemEntity.getIdUsuario())){
                view = inflater.inflate(R.layout.item_mensagem_direita, parent, false);
                //Recupera o elemento para exibição
                TextView textMensagem = view.findViewById(R.id.textViewMensagemDireita);
                textMensagem.setText(mensagemEntity.getMensagem());
            }else{
                view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
                //Recupera o elemento para exibição
                TextView textMensagem = view.findViewById(R.id.textViewMensagemEsquerda);
                textMensagem.setText(mensagemEntity.getMensagem());
            }
        }

        return view;
    }
}
