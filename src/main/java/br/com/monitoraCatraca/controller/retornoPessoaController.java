/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.monitoraCatraca.controller;

import br.com.monitoraCatraca.factory.DAO;
import br.com.monitoraCatraca.utils.WSRetorno;
import java.io.Serializable;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.xml.ws.WebServiceContext;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;

/**
 *
 * @author Claudemir Rtools
 */
@ManagedBean
@RequestScoped
public class retornoPessoaController implements Serializable {

    @Resource
    WebServiceContext wsContext;

    public void liberar_pessoa() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/json");
        externalContext.setResponseCharacterEncoding("UTF-8");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            if (request.getParameter("cliente") != null && request.getParameter("pessoa") != null && request.getParameter("catraca") != null) {
                String cliente = request.getParameter("cliente");
                Integer nr_pessoa = Integer.parseInt(request.getParameter("pessoa"));
                Integer nr_catraca = Integer.parseInt(request.getParameter("catraca"));

                new DAO(cliente).query_execute(
                    "DELETE FROM soc_catraca_liberada WHERE nr_pessoa = " + nr_pessoa + " AND id_catraca = " + nr_catraca + ";"
                );
                
                new DAO(cliente).query_execute(
                        "INSERT INTO soc_catraca_liberada (nr_pessoa, id_catraca) VALUES (" + nr_pessoa + ", " + nr_catraca + "); \n"
                        + "SELECT setval('soc_catraca_liberada_id_seq', max(id)) FROM soc_catraca_liberada;"
                );
                
                ResultSet rs = new DAO(cliente).query(
                        "SELECT COUNT(*) AS qnt FROM soc_catraca_liberada WHERE nr_pessoa = " + nr_pessoa + " AND id_catraca = " + nr_catraca + ";"
                );
                
                rs.next();
                
                if (rs.getInt("qnt") > 0){
                    WSRetorno ws = new WSRetorno(true, "Catraca Liberada");
                    externalContext.getResponseOutputWriter().write(new JSONObject(ws).toString());
                    facesContext.responseComplete();
                    return;
                }
                
                rs.close();
            }
            WSRetorno ws = new WSRetorno(false, "Erro ao liberar Catraca");
            externalContext.getResponseOutputWriter().write(new JSONObject(ws).toString());
            facesContext.responseComplete();
        } catch (NumberFormatException | IOException e) {
            WSRetorno ws = new WSRetorno(false, e.getMessage());
            externalContext.getResponseOutputWriter().write(new JSONObject(ws).toString());
            facesContext.responseComplete();
        } catch (SQLException ex) {
            Logger.getLogger(retornoPessoaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Resource
    WebServiceContext wsContext2;

    public void liberar_cartao() throws IOException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/json");
        externalContext.setResponseCharacterEncoding("UTF-8");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            if (request.getParameter("cliente") != null && request.getParameter("cartao") != null && request.getParameter("catraca") != null) {
                String cliente = request.getParameter("cliente");
                String st_cartao = request.getParameter("cartao");
                Integer nr_catraca = Integer.parseInt(request.getParameter("catraca"));

                ResultSet rs = new DAO(cliente).query(
                        "INSERT INTO soc_catraca_liberada (ds_cartao, id_catraca) VALUES ('" + st_cartao + "', " + nr_catraca + "); \n"
                        + "SELECT setval('soc_catraca_liberada_id_seq', max(id)) FROM soc_catraca_liberada;"
                );
                
                if (rs != null){
                    WSRetorno ws = new WSRetorno(true, "Catraca Liberada");
                    externalContext.getResponseOutputWriter().write(new JSONObject(ws).toString());
                    facesContext.responseComplete();
                    return;
                }
            }
            WSRetorno ws = new WSRetorno(false, "Erro ao liberar Catraca");
            externalContext.getResponseOutputWriter().write(new JSONObject(ws).toString());
            facesContext.responseComplete();
        } catch (NumberFormatException | IOException e) {
            WSRetorno ws = new WSRetorno(false, e.getMessage());
            externalContext.getResponseOutputWriter().write(new JSONObject(ws).toString());
            facesContext.responseComplete();
        }
    }
//    @Resource
//    WebServiceContext wsContext;
//
//    public void response() {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ExternalContext externalContext = facesContext.getExternalContext();
//        externalContext.setResponseContentType("application/json");
//        externalContext.setResponseCharacterEncoding("UTF-8");
//        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        try {
//            WSRetornoPessoa wsr = new WSRetornoPessoa();
//            if (request.getParameter("pessoa") != null && request.getParameter("catraca") != null) {
//                Integer nr_pessoa = Integer.parseInt(request.getParameter("pessoa"));
//                Integer nr_catraca = Integer.parseInt(request.getParameter("catraca"));
//
//                ResultSet rs_catraca = new DAO().query("SELECT c.id_departamento AS departamento FROM soc_catraca c WHERE c.nr_numero = " + nr_catraca);
//                rs_catraca.next();
//
//                if (nr_pessoa >= 0) {
//                    if (nr_pessoa == 0) {
//                        //  LIBERA A CATRACA PARA VISITANTE
//                        wsr = new WSRetornoPessoa(
//                                nr_pessoa,
//                                "VISITANTE",
//                                "",
//                                "",
//                                null,
//                                "Catraca Liberada",
//                                true
//                        );
//                    } else {
//                        // FUNÇÃO PARA VERIFICAÇÃO DA PESSOA (SE FOR LIBERADA OU NÃO)
//                        ResultSet rs_funcao = new DAO().query("SELECT func_catraca(" + nr_pessoa + ", " + rs_catraca.getInt("departamento") + ", 2, null) AS retorno");
//                        try {
//                            rs_funcao.next();
//                        } catch (Exception e) {
//                            wsr = new WSRetornoPessoa(
//                                    nr_pessoa,
//                                    "PESSOA NÃO ENCONTRADA",
//                                    "",
//                                    "",
//                                    -1,
//                                    "Erro ao pesquisar pessoa na função",
//                                    false
//                            );
//                            externalContext.getResponseOutputWriter().write(new JSONObject(wsr).toString());
//                            facesContext.responseComplete();
//                            return;
//                        }
//
//                        ResultSet rs_pessoa = new DAO().query(
//                                "SELECT p.ds_nome AS nome, \n "
//                                + "     f.ds_foto AS foto \n "
//                                + "FROM pes_pessoa p \n "
//                                + "INNER JOIN pes_fisica f ON f.id_pessoa = p.id \n "
//                                + "WHERE p.id = " + nr_pessoa);
//                        rs_pessoa.next();
//
//                        try {
//                            //  VERIFICA SE O CÓDIGO ENVIADO É VÁLIDO
//                            rs_pessoa.getString("nome");
//                        } catch (Exception e) {
//                            wsr = new WSRetornoPessoa(
//                                    nr_pessoa,
//                                    "",
//                                    "",
//                                    "",
//                                    -1,
//                                    "Código da Pessoa não Encontrado",
//                                    false
//                            );
//                            externalContext.getResponseOutputWriter().write(new JSONObject(wsr).toString());
//                            facesContext.responseComplete();
//                            return;
//                        }
//
//                        // SE O RETORNO DA FUNÇÃO FOR LIBERADA
//                        if (rs_funcao.getInt("retorno") > 0) {
//                            wsr = new WSRetornoPessoa(
//                                    nr_pessoa,
//                                    rs_pessoa.getString("nome"),
//                                    rs_pessoa.getString("foto"),
//                                    "",
//                                    null,
//                                    "Catraca Liberada",
//                                    true
//                            );
//                        } else {
//                            // SE O RETORNO DA FUNÇÃO NÃO FOR LIBERADA
//                            ResultSet rs_erro = new DAO().query(
//                                    "  SELECT ce.id AS id, \n "
//                                    + "       ce.ds_descricao AS descricao_erro \n "
//                                    + "  FROM soc_catraca_erro ce \n "
//                                    + " WHERE ce.nr_codigo = " + rs_funcao.getInt("retorno")
//                            );
//
//                            rs_erro.next();
//
//                            wsr = new WSRetornoPessoa(
//                                    nr_pessoa,
//                                    rs_pessoa.getString("nome"),
//                                    rs_pessoa.getString("foto"),
//                                    "",
//                                    rs_erro.getInt("id"),
//                                    rs_erro.getString("descricao_erro"),
//                                    false
//                            );
//
//                            externalContext.getResponseOutputWriter().write(new JSONObject(wsr).toString());
//                            facesContext.responseComplete();
//                            return;
//                        }
//                    }
//                }
//
//                if (nr_pessoa < 0) {
//                    ResultSet rs_erro = new DAO().query(
//                            "  SELECT ce.id AS id, \n "
//                            + "       ce.ds_descricao AS descricao_erro \n "
//                            + "  FROM soc_catraca_erro ce \n "
//                            + " WHERE ce.nr_codigo = " + nr_pessoa
//                    );
//
//                    rs_erro.next();
//
//                    wsr = new WSRetornoPessoa(
//                            nr_pessoa,
//                            "",
//                            "",
//                            "",
//                            rs_erro.getInt("id"),
//                            rs_erro.getString("descricao_erro"),
//                            false
//                    );
//                }
//            }
//            externalContext.getResponseOutputWriter().write(new JSONObject(wsr).toString());
//            facesContext.responseComplete();
//        } catch (NumberFormatException | IOException | SQLException e) {
//            e.getMessage();
//        }
//    }
//
//    @Resource
//    WebServiceContext wsContext2;
//
//    public void response2() {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        ExternalContext externalContext = facesContext.getExternalContext();
//        externalContext.setResponseContentType("application/json");
//        externalContext.setResponseCharacterEncoding("UTF-8");
//        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//        try {
//            WSRetornoPessoa wsr = new WSRetornoPessoa();
//            if (request.getParameter("cartao") != null && request.getParameter("catraca") != null) {
//                String st_cartao = request.getParameter("cartao");
//
//                Integer nr_catraca = Integer.parseInt(request.getParameter("catraca"));
//                ResultSet rs_catraca = new DAO().query("SELECT c.id_departamento AS departamento FROM soc_catraca c WHERE c.nr_numero = " + nr_catraca);
//                rs_catraca.next();
//
//                String codigo_string;
//
//                // 8 É O NÚMERO MÍNIMO DE CARACTÉRES PARA O CÓDIGO
//                // 8 ESTA DEFINIDO 8 TAMBÉM NO SINDICAL WEB
//                // INDICE FINAL SEMPRE - 1 ex 8 - 1 = 7
//                ResultSet rs_cartao = new DAO().query(
//                        "SELECT nr_cartao_posicao_via AS via, \n "
//                        + "     nr_cartao_posicao_codigo AS codigo \n "
//                        + " FROM conf_social"
//                );
//                rs_cartao.next();
//
//                // 1 OU 2 PARA VIA = 99
//                int _via = rs_cartao.getInt("via"), _codigo = rs_cartao.getInt("codigo");
//
//                String via_string = st_cartao.substring(_via, _via + 2);
//                codigo_string = st_cartao.substring(_codigo, _codigo + 8);
//
//                Integer nr_cartao = Integer.parseInt(codigo_string);
//                st_cartao = codigo_string;
//                Integer numero_via = Integer.valueOf(via_string);
//
//                ResultSet rs_funcao;
//                if (numero_via != 99) {
//                    rs_funcao = new DAO().query("SELECT func_catraca(" + nr_cartao + "," + rs_catraca.getInt("departamento") + ", 1, " + numero_via + ") AS retorno");
//                } else {
//                    rs_funcao = new DAO().query("SELECT func_catraca(" + nr_cartao + "," + rs_catraca.getInt("departamento") + ", 3, null) AS retorno");
//                }
//                rs_funcao.next();
//
//                Integer nr_retorno = rs_funcao.getInt("retorno");
//
//                if (nr_retorno >= 0) {
//                    if (nr_retorno == 0) {
//                        //  LIBERA A CATRACA PARA VISITANTE
//                        wsr = new WSRetornoPessoa(
//                                nr_retorno,
//                                "VISITANTE",
//                                "",
//                                "",
//                                null,
//                                "Catraca Liberada",
//                                true
//                        );
//                    } else {
//                        ResultSet rs_pessoa = new DAO().query(
//                                "SELECT p.ds_nome AS nome, \n "
//                                + "     f.ds_foto AS foto \n "
//                                + "FROM pes_pessoa p \n "
//                                + "INNER JOIN pes_fisica f ON f.id_pessoa = p.id \n "
//                                + "WHERE p.id = " + nr_retorno);
//                        rs_pessoa.next();
//
//                        try {
//                            //  VERIFICA SE O CÓDIGO ENVIADO É VÁLIDO
//                            rs_pessoa.getString("nome");
//                        } catch (Exception e) {
//                            wsr = new WSRetornoPessoa(
//                                    nr_retorno,
//                                    "",
//                                    "",
//                                    "",
//                                    -1,
//                                    "Código da Pessoa não Encontrado",
//                                    false
//                            );
//                            externalContext.getResponseOutputWriter().write(new JSONObject(wsr).toString());
//                            facesContext.responseComplete();
//                            return;
//                        }
//
//                        wsr = new WSRetornoPessoa(
//                                nr_retorno,
//                                rs_pessoa.getString("nome"),
//                                rs_pessoa.getString("foto"),
//                                "",
//                                null,
//                                "Catraca Liberada",
//                                true
//                        );
//
//                        externalContext.getResponseOutputWriter().write(new JSONObject(wsr).toString());
//                        facesContext.responseComplete();
//                        return;
//                    }
//                }
//
//                if (nr_retorno < 0) {
//                    ResultSet rs_carteirinha = new DAO().query(
//                            "SELECT id_pessoa FROM soc_carteirinha WHERE nr_cartao = " + nr_cartao
//                    );
//                    rs_carteirinha.next();
//
//                    Integer nr_pessoa = rs_carteirinha.getInt("id_pessoa");
//                    
//                    ResultSet rs_pessoa = new DAO().query(
//                            "SELECT p.ds_nome AS nome, \n "
//                            + "     f.ds_foto AS foto \n "
//                            + "FROM pes_pessoa p \n "
//                            + "INNER JOIN pes_fisica f ON f.id_pessoa = p.id \n "
//                            + "WHERE p.id = " + nr_pessoa);
//                    rs_pessoa.next();
//
//                    ResultSet rs_erro = new DAO().query(
//                            "  SELECT ce.id AS id, \n "
//                            + "       ce.ds_descricao AS descricao_erro \n "
//                            + "  FROM soc_catraca_erro ce \n "
//                            + " WHERE ce.nr_codigo = " + nr_retorno
//                    );
//
//                    rs_erro.next();
//
//                    wsr = new WSRetornoPessoa(
//                            nr_pessoa,
//                            rs_pessoa.getString("nome"),
//                            rs_pessoa.getString("foto"),
//                            "",
//                            rs_erro.getInt("id"),
//                            rs_erro.getString("descricao_erro"),
//                            false
//                    );
//                }
//            }
//            externalContext.getResponseOutputWriter().write(new JSONObject(wsr).toString());
//            facesContext.responseComplete();
//        } catch (NumberFormatException | IOException | SQLException e) {
//            e.getMessage();
//        }
//    }

}
