package com.edidevteste.whatsappjava.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;

import com.edidevteste.javawhatsapp.R;
import com.edidevteste.whatsappjava.Adapter.TabAdapter;
import com.edidevteste.whatsappjava.Security.PreferenceSecurity;
import com.edidevteste.whatsappjava.Util.UtilConstantes;
import com.edidevteste.whatsappjava.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private PreferenceSecurity mPreferenceSecurity;

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabItem mTabItemConversa;
    private TabItem mTabItemContatos;

    private Integer timerProcessamento = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        inicializar();

        setLinerters();
        Timer();
    }

    //Vinculando o menu com layout construido
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    //Evento de click no menu que está no toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sair:
                //deslogar do sistema
                timerProcessamento = 2;
                return true;
            case R.id.item_configuracoes:
                return true;
            default:
                Log.i("toolbarMain", "Nada selecionado!");
                return super.onOptionsItemSelected(item);
        }
    }

    private void inicializar() {
        //buttonSair = findViewById(R.id.buttonPrincipalSair);

        //Criação do toolbar e adicionando no contexto
        mToolbar = findViewById(R.id.toolbar_principal);
        mToolbar.setTitle("WhatsApp");

        setSupportActionBar(mToolbar);

        mTabLayout = findViewById(R.id.slidingTabLayout);
        mTabItemConversa = findViewById(R.id.tabItemConversa);
        mTabItemContatos = findViewById(R.id.tabItemContatos);
        //mTabLayout.setTabGravity(TableLayout.TEXT_ALIGNMENT_GRAVITY);
        //mTabLayout.setSelectedTabIndicatorColor(0);

        mViewPager = findViewById(R.id.viewPagerPrincipal);
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(tabAdapter);

        tabSelected();

        mFirebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        mPreferenceSecurity = new PreferenceSecurity(this);
    }

    private void tabSelected() {
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                /*if (tab.getPosition() == 1) {
                    mToolbar.setBackgroundColor(ContextCompat.getColor(PrincipalActivity.this,
                            R.color.colorAccent));
                    mTabLayout.setBackgroundColor(ContextCompat.getColor(PrincipalActivity.this,
                            R.color.colorAccent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(PrincipalActivity.this,
                                R.color.colorAccent));
                    }
                } else if (tab.getPosition() == 2) {
                    mToolbar.setBackgroundColor(ContextCompat.getColor(PrincipalActivity.this,
                            R.color.colorPrimary));
                    mTabLayout.setBackgroundColor(ContextCompat.getColor(PrincipalActivity.this,
                            R.color.colorPrimary));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(PrincipalActivity.this,
                                R.color.colorPrimaryDark));
                    }
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    private void setLinerters() {
       /* buttonSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Logout
                timerProcessamento = 2;
            }
        });*/
    }

    public void Timer() {
        Timer timer = new Timer();
        PrincipalActivity.Task task = new PrincipalActivity.Task();
        timer.schedule(task, 500, 500);
    }

    class Task extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (timerProcessamento > 0) {
                        switch (timerProcessamento) {
                            case 2:
                                logout();
                                break;
                            default:
                                timerProcessamento = 0;
                                break;
                        }
                    }
                }
            });
        }
    }

    private void logout() {
        List<String> dadosUsuario = new ArrayList<>();
        dadosUsuario.add(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna1());
        dadosUsuario.add(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna2());
        dadosUsuario.add(UtilConstantes.USUARIO_DADOS_FIREBASE.getColuna3());
        mPreferenceSecurity.removerValoresPreferencesUsuario(dadosUsuario);
        mFirebaseAuth.signOut();
        timerProcessamento = 0;
        startActivity(new Intent(PrincipalActivity.this, MainActivity.class));
        finish();
    }
}
