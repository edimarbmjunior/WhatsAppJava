package com.edidevteste.whatsappjava.Util;

public enum UtilConstantes {

    USUARIO_DADOS ("USUARIO_TOKEN", "USUARIO_NOME", "USUARIO_TELEFONE"),
    SECURITY_PREFERENCES ("WHATSAPP");

    private String coluna1;
    private String coluna2;
    private String coluna3;

    private UtilConstantes(String coluna1) {
        this.coluna1 = coluna1;
    }
    private UtilConstantes(String coluna1, String coluna2, String coluna3) {
        this.coluna1 = coluna1;
        this.coluna2 = coluna2;
        this.coluna3 = coluna3;
    }

    public String getColuna1() {
        return coluna1;
    }
    public String getColuna2() {
        return coluna2;
    }
    public String getColuna3() {
        return coluna3;
    }
}
