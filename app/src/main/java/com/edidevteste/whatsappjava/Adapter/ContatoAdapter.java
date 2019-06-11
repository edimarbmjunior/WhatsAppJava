package com.edidevteste.whatsappjava.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.Util.Base64Custom;
import com.edidevteste.whatsappjava.entity.Contato;

import java.util.ArrayList;

public class ContatoAdapter extends ArrayAdapter<Contato> {

    private ArrayList<Contato> mListacontatos;
    private Context mContext;

    public ContatoAdapter(Context context, ArrayList<Contato> objects) {
        super(context, 0, objects);
        this.mListacontatos = objects;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;

        if(mListacontatos!=null){
            //Montar view

            //inicializa um objeto para montar uma view
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_contato, parent, false);
            TextView nomeContato = view.findViewById(R.id.textViewListaNome);
            TextView emailContato = view.findViewById(R.id.textViewListaEmail);
            nomeContato.setText(mListacontatos.get(position).getNome());
            emailContato.setText(Base64Custom.DecodificaTo64(mListacontatos.get(position).getIdentificadorUsuario()));
        }

        return view;
    }
}
