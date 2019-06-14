package com.edidevteste.whatsappjava.Business;

import com.edidevteste.whatsappjava.Repository.MensagemRepository;
import com.edidevteste.whatsappjava.entity.ConversaEntity;
import com.edidevteste.whatsappjava.entity.MensagemEntity;

public class MensagemBusiness {

    private MensagemRepository mensagemRepository;

    public MensagemBusiness(){
        mensagemRepository = new MensagemRepository();
    }

    public void salvaMensagem(MensagemEntity mensagemEntity, String identificadorDestinatario, String identificadorRemetente){
        mensagemRepository.salvaMensagem(mensagemEntity, identificadorDestinatario, identificadorRemetente);
    }

    public void salvarConversa(ConversaEntity conversaEntity, String identificadorDestinatario, String identificadorRemetente){
        mensagemRepository.salvarConversa(conversaEntity, identificadorDestinatario, identificadorRemetente);
    }
}
