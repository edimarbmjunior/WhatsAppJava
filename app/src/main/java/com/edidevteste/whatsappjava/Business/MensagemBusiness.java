package com.edidevteste.whatsappjava.Business;

import android.widget.ArrayAdapter;

import com.edidevteste.whatsappjava.Repository.MensagemRepository;
import com.edidevteste.whatsappjava.entity.MensagemEntity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MensagemBusiness {

    private MensagemRepository mensagemRepository;

    public MensagemBusiness(){
        mensagemRepository = new MensagemRepository();
    }

    public void salvaMensagem(MensagemEntity mensagemEntity, String identificadorDestinatario, String identificadorRemetente){
        mensagemRepository.salvaMensagem(mensagemEntity, identificadorDestinatario, identificadorRemetente);
    }
}
