package com.edidevteste.whatsappjava.entity;

public class Contato {

    private String identificadorUsuario;
    private String nome;
    private String email;

    public Contato() {}

    public Contato(String identificadorUsuario, String nome, String email) {
        this.identificadorUsuario = identificadorUsuario;
        this.nome = nome;
        this.email = email;
    }

    public String getIdentificadorUsuario() {
        return identificadorUsuario;
    }

    public void setIdentificadorUsuario(String identificadorUsuario) {
        this.identificadorUsuario = identificadorUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
