package com.edidevteste.whatsappjava.Security;

import android.content.Context;
import android.content.SharedPreferences;

import com.edidevteste.whatsappjava.Util.UtilConstantes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PreferenceSecurity {

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferenceSecurity(Context contextParametro){
        mContext = contextParametro;
        mSharedPreferences = mContext.getSharedPreferences(UtilConstantes.SECURITY_PREFERENCES.getColuna1(), Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public void salvarValorPreferences(String key, String dado){
        editor.putString(key, dado);
        editor.commit();
    }

    public void salvarValoresPreferences(HashMap<String, String> dados){
        for (Iterator<Map.Entry<String, String>> dado = dados.entrySet().iterator(); dado.hasNext();) {
            Map.Entry<String, String> valores = dado.next();
            editor.putString(valores.getKey(), valores.getValue());
            editor.commit();
        }
    }

    public String recuperarValorUnicoPrefences(String key){
        return mSharedPreferences.getString(key, "");
    }

    public HashMap<String, String> recuperarValoresUnicoPrefences(List<String> keys){
        HashMap<String, String> retorno = new HashMap();

        for (String key : keys){
            String valor = mSharedPreferences.getString(key, "");
            if(valor.length()>1){
                retorno.put(key, valor);
            }
        }
        return retorno;
    }

    public HashMap<String, String> salvarValoresPreferencesUsuario(){
        HashMap<String, String> retorno = new HashMap();
        retorno.put(UtilConstantes.USUARIO_DADOS.getColuna1(), mSharedPreferences.getString(UtilConstantes.USUARIO_DADOS.getColuna1(), ""));
        retorno.put(UtilConstantes.USUARIO_DADOS.getColuna2(), mSharedPreferences.getString(UtilConstantes.USUARIO_DADOS.getColuna2(), ""));
        retorno.put(UtilConstantes.USUARIO_DADOS.getColuna3(), mSharedPreferences.getString(UtilConstantes.USUARIO_DADOS.getColuna3(), ""));
        return retorno;
    }

    public void removerValorPreferencesUsuario(String key){
        editor.remove(key);
    }

    public void removerValoresPreferencesUsuario(List<String> keys){
        for (String key : keys){
            editor.remove(key);
        }
    }
}
