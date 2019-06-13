package com.edidevteste.whatsappjava.entity;

public class MensagemEntity {

    private String idUsuario;
    private String mensagem;

    public MensagemEntity() {
    }

    public MensagemEntity(String idUsuario, String mensagem) {
        this.idUsuario = idUsuario;
        this.mensagem = mensagem;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
