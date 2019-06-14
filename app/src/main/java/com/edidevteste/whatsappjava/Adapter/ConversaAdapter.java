package com.edidevteste.whatsappjava.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.entity.ConversaEntity;

import java.util.ArrayList;

public class ConversaAdapter extends ArrayAdapter<ConversaEntity> {

    private ArrayList<ConversaEntity> mListaCoversa;
    private Context mContext;

    public ConversaAdapter(Context context, ArrayList<ConversaEntity> objects) {
        super(context, 0, objects);
        mContext = context;
        mListaCoversa = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if(mListaCoversa != null){
            //inicializa um objeto para montar uma view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_conversa, parent, false);
            TextView nomeContato = view.findViewById(R.id.textViewConversaListaNome);
            TextView mensagemContato = view.findViewById(R.id.textViewConversaMensagem);
            nomeContato.setText(mListaCoversa.get(position).getNome());
            mensagemContato.setText(mListaCoversa.get(position).getMensagem());
        }

        return view;
    }
}
