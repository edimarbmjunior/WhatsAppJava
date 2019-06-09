package com.edidevteste.whatsappjava.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.edidevteste.whatsappjava.Fragments.ContatoFragment;
import com.edidevteste.whatsappjava.Fragments.ConversasFragment;

//FragmentPagerAdapter      - Para Fragment que usa pouca memória, com pouca carga de dados
//FragmentStatePagerAdapter - Se for uma listagem de dados
public class TabAdapter extends FragmentStatePagerAdapter {

    private  int numTabsLocal;

    public TabAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabsLocal = numTabs;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;

        switch (i){
            case 0:
                fragment = new ConversasFragment();
                break;
            case 1:
                fragment = new ContatoFragment();
                break;
            default:
                return null;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return numTabsLocal;
    }
}
