package com.edidevteste.whatsappjava.Util;

import android.util.Base64;

public class Base64Custom {

    public static String CodificaTo64(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("\\n|\\r", "");
    }

    public static String DecodificaTo64(String textCodificado){
        return new String(Base64.decode(textCodificado, Base64.DEFAULT));
    }
}
