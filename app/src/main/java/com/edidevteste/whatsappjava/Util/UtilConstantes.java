package com.edidevteste.whatsappjava.Util;

public enum UtilConstantes {

    USUARIO_DADOS ("USUARIO_TOKEN", "USUARIO_NOME", "USUARIO_TELEFONE", "TOKEN_VALIDADO"),
    USUARIO_DADOS_FIREBASE ("USUARIO_ID", "USUARIO_EMAIL", "USUARIO_SENHA", "USUARIO_EMAIL_64"),
    SECURITY_PREFERENCES ("WHATSAPP"),
    CONTATO_CONVERSA("NOME_CONTATO_CONVERSA", "EMAIL_CONTATO_CONVERSA");

    private String coluna1;
    private String coluna2;
    private String coluna3;
    private String coluna4;

    private UtilConstantes(String coluna1) {
        this.coluna1 = coluna1;
    }

    private UtilConstantes(String coluna1, String coluna2) {
        this.coluna1 = coluna1;
        this.coluna2 = coluna2;
    }

    private UtilConstantes(String coluna1, String coluna2, String coluna3) {
        this.coluna1 = coluna1;
        this.coluna2 = coluna2;
        this.coluna3 = coluna3;
    }

    private UtilConstantes(String coluna1, String coluna2, String coluna3, String coluna4) {
        this.coluna1 = coluna1;
        this.coluna2 = coluna2;
        this.coluna3 = coluna3;
        this.coluna4 = coluna4;
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
    public String getColuna4() {
        return coluna4;
    }
}
