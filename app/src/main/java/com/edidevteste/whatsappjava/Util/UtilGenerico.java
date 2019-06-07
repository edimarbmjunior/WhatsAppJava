package com.edidevteste.whatsappjava.Util;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UtilGenerico {

    public static void msgGenerrica(Context contexto, String msg){
        Toast.makeText(contexto, msg, Toast.LENGTH_LONG).show();
    }

    public static final String MD5_Hash(String senha) {
        final String MD5 = "MD5";
        MessageDigest m = null;
        String retornoString = "";
        try{
            m = MessageDigest.getInstance(MD5);
            m.update(senha.getBytes(),0,senha.length());
            retornoString = new BigInteger(1, m.digest()).toString(16);
        }catch (java.security.NoSuchAlgorithmException e){
            retornoString = "";
            throw new NoSuchAlgorithmException("Error(hexMd5): >" + e);
        } finally{
            return retornoString;
        }
    }

    public static Boolean isValidEmail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
