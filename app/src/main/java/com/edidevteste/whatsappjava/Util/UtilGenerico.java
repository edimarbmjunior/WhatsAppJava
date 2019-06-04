package com.edidevteste.whatsappjava.Util;

import android.content.Context;
import android.widget.Toast;

public class UtilGenerico {

    public static void msgGenerrica(Context contexto, String msg){
        Toast.makeText(contexto, msg, Toast.LENGTH_LONG).show();
    }
}
