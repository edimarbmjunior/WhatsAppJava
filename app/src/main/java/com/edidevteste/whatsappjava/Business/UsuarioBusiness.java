package com.edidevteste.whatsappjava.Business;

import com.edidevteste.whatsappjava.Repository.UsuarioRepository;
import com.edidevteste.whatsappjava.Util.UtilGenerico;
import com.edidevteste.whatsappjava.entity.Usuario;

public class UsuarioBusiness {

    public UsuarioBusiness(){

    }

    public String validaUsuarioAntesDaInclusao(Usuario usuario){
        String retorno = "0";
        if(usuario == null){
            return "Erro na criação do usuário.";
        }
        if(usuario.getNome().isEmpty() &&
                usuario.getEmail().isEmpty() &&
                usuario.getSenha().isEmpty()){
            return "Dados não preenchidos.";
        }
        if(!UtilGenerico.isValidEmail(usuario.getEmail())){
            return "O e-mail é inválido.";
        }
        /*if(new UsuarioRepository().emailExiste(usuario.getEmail())){
            return false;
        }*/
        return retorno;
    }
}
