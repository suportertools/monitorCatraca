/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.monitoraCatraca.controller;

import br.com.monitoraCatraca.utils.WSSocket;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Claudemir Rtools
 */
@ManagedBean
@SessionScoped
public class EnviaAtualizacaoController implements Serializable {

    public void enviaAtualizacao() {

        // METODO QUE ENVIA PARA A SOCKET O COMANDO PARA ATUALIZAR A TELA
        // PROJETO DA CATRACA ( catraca v5.5 ) QUE CHAMA ESTE LINK
        // QUALQUER PÁGINA PODE CHAMAR ESSE LINK
        // http://localhost:8080/monitorCatraca/envia_atualizacao.xhtml
        // ---------------------------------------------------------------------
        if (!FacesContext.getCurrentInstance().isPostback()) {

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//
//            Integer n_catraca = null;
//            String servidorString = null;

            if (request.getParameter("cliente") != null && request.getParameter("servidor") != null) {
                WSSocket.send(request.getParameter("cliente") + "_servidor_" + request.getParameter("servidor"), "ok");
            }

//            if (request.getParameter("cliente") != null && request.getParameter("catraca") != null) {
//                n_catraca = Integer.valueOf(request.getParameter("catraca"));
//            }
//            
//            if (request.getParameter("servidor") != null && !request.getParameter("servidor").isEmpty()) {
//                servidorString = request.getParameter("servidor");
//            }
//            if (n_catraca != null && servidorString == null) {
//                WSSocket.send(request.getParameter("cliente") + "_monitora_" + n_catraca, "ok");
//            }
//            
//            if (n_catraca != null && servidorString != null) {
//                WSSocket.send(request.getParameter("cliente") + "_monitora_" + n_catraca + "_servidor_" + servidorString, "ok");
//            }
        }

    }

}
