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
import com.edidevteste.whatsappjava.Adapter.ContatoAdapter;
import com.edidevteste.whatsappjava.Repository.UsuarioRepository;
import com.edidevteste.whatsappjava.Security.PreferenceSecurity;
import com.edidevteste.whatsappjava.Util.Base64Custom;
import com.edidevteste.whatsappjava.Util.UtilConstantes;
import com.edidevteste.whatsappjava.Util.UtilGenerico;
import com.edidevteste.whatsappjava.entity.Contato;
import com.edidevteste.whatsappjava.view.ConversaPessoalActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatoFragment extends Fragment {

    private ListView mListViewContatos;
    private ArrayAdapter arrayAdapter;
    private ArrayList<Contato> mlistaContatos;
    private DatabaseReference mFirebaseDatabase;
    private ValueEventListener mValueEventListener;

    public ContatoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseDatabase.addValueEventListener(UsuarioRepository.getDadosContatosEvent(mlistaContatos, arrayAdapter));
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirebaseDatabase.removeEventListener(UsuarioRepository.getDadosContatosEvent(mlistaContatos, arrayAdapter));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contato, container, false);

        mlistaContatos = new ArrayList<>();

        mListViewContatos = view.findViewById(R.id.listViewFragmentContatos);
        /*arrayAdapter = new ArrayAdapter(
                getActivity(),
                *//*android.R.layout.simple_list_item_1,*//*R.layout.lista_contato,
                mlistaContatos
        );*/

        arrayAdapter = new ContatoAdapter(getActivity(), mlistaContatos);

        mListViewContatos.setAdapter(arrayAdapter);

        PreferenceSecurity preferenceSecurity = new PreferenceSecurity(getActivity());
        String email = preferenceSecurity.recuperarUsuarioEmail64();

        mFirebaseDatabase = new UsuarioRepository().getDadosContatos(email);

        //Código colocado no override do "onStart()" para carregar somente um pouco antes de inicializar a tela
        //No override do "onStop()" foi alterado para retirar o eventListener, para parar sua execução e parar de consumir recurso.
        /*mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mlistaContatos.clear();
                for(DataSnapshot dado : dataSnapshot.getChildren()){
                    Contato contato = dado.getValue(Contato.class);
                    mlistaContatos.add(contato.getNome());
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        mListViewContatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contato = mlistaContatos.get(position);
                Intent intent = new Intent(getActivity(), ConversaPessoalActivity.class);
                intent.putExtra(UtilConstantes.CONTATO_CONVERSA.getColuna1(), contato.getNome());
                intent.putExtra(UtilConstantes.CONTATO_CONVERSA.getColuna2(), Base64Custom.DecodificaTo64(contato.getIdentificadorUsuario()));
                startActivity(intent);
            }
        });

        return view;
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_contato, menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.tabItemContatos){
            UtilGenerico.msgGenerrica(getActivity(), "Clicou na opção de Contato");
        }
        return true;
    }
}
