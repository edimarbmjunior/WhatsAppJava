package com.edidevteste.whatsappjava.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CacheGenerico {

    private static List<String> estadosCache;
    private static HashMap<String, String> estadosCacheMap;

    /*private static final CacheGenerico ourInstance = new CacheGenerico();
    public static CacheGenerico getInstance() {
        return ourInstance;
    }*/

    private CacheGenerico() {
    }

    private static void setCacheListaEstados(ArrayList<String> estados, ArrayList<String> siglas){
        estadosCache.clear();
        for(String estado : estados){
            estadosCache.add(estado);
        }

        estadosCacheMap.clear();
        int i = 0;
        for(String sigla : siglas){
            estadosCacheMap.put(sigla, estados.get(i));
            i++;
        }
    }

    public List<String> getEstadosCache() {
        if(estadosCache.isEmpty()){
            return new ArrayList();
        }
        return estadosCache;
    }

    public HashMap<String, String> getEstadosCacheMap() {
        if(estadosCache.isEmpty()){
            return new HashMap<>();
        }
        return estadosCacheMap;
    }

    public String getEstadoPorSigla(String sigla) {
        if(estadosCache.isEmpty()){
            return "";
        }
        return estadosCacheMap.get(sigla);
    }
}
