package com.edidevteste.whatsappjava.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.Util.UtilGenerico;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatoFragment extends Fragment {


    public ContatoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_contato, container, false);
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
