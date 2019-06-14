package com.edidevteste.whatsappjava.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.Adapter.ConversaAdapter;
import com.edidevteste.whatsappjava.Repository.UsuarioRepository;
import com.edidevteste.whatsappjava.Security.PreferenceSecurity;
import com.edidevteste.whatsappjava.Util.Base64Custom;
import com.edidevteste.whatsappjava.Util.UtilConstantes;
import com.edidevteste.whatsappjava.Util.UtilGenerico;
import com.edidevteste.whatsappjava.entity.ConversaEntity;
import com.edidevteste.whatsappjava.view.ConversaPessoalActivity;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private ListView mListViewConversa;
    private ArrayAdapter arrayAdapter;
    private ArrayList<ConversaEntity> mlistaConversa;
    private DatabaseReference mFirebaseDatabase;

    public ConversasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        mlistaConversa = new ArrayList<>();
        mListViewConversa = view.findViewById(R.id.listViewFragmentConversa);

        arrayAdapter = new ConversaAdapter(getActivity(), mlistaConversa);
        mListViewConversa.setAdapter(arrayAdapter);

        String email = new PreferenceSecurity(getActivity()).recuperarUsuarioEmail64();

        mFirebaseDatabase = new UsuarioRepository().getDadosConversa(email);

        mListViewConversa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConversaEntity conversaEntity = mlistaConversa.get(position);
                Intent intent = new Intent(getActivity(), ConversaPessoalActivity.class);
                intent.putExtra(UtilConstantes.CONTATO_CONVERSA.getColuna1(), conversaEntity.getNome());
                intent.putExtra(UtilConstantes.CONTATO_CONVERSA.getColuna2(), Base64Custom.DecodificaTo64(conversaEntity.getIdUsuario()));
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseDatabase.addValueEventListener(UsuarioRepository.getDadosConversaEvent(mlistaConversa, arrayAdapter));
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseDatabase.removeEventListener(UsuarioRepository.getDadosConversaEvent(mlistaConversa, arrayAdapter));
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_conversa, menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.tabItemConversa){
            UtilGenerico.msgGenerrica(getActivity(), "Clicou na opção de conversa");
        }
        return true;
    }
}
