/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.monitoraCatraca.controller;

import br.com.monitoraCatraca.factory.DAO;
import br.com.monitoraCatraca.factory.Default;
import br.com.monitoraCatraca.utils.Datas;
import br.com.monitoraCatraca.utils.WSSocket;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class monitoraController implements Serializable {

    private String numeroPagina = "";
    private String caminhoPagina = "";
    private String cliente = "";
    private String mac = "";

    private ObjectCatraca catraca = new ObjectCatraca();

    public String datahora() {
        return Datas.data();
    }

    public String retornaPanelFoto() {
        if (!catraca.getNome().isEmpty() || !catraca.getMensagem().isEmpty()) {
            if (catraca.getLiberado()) {
                return "#389709";
            } else {
                return "red";
            }
        }
        return "";
    }

    public void mostra_tela() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//            if (request.getParameter("cliente") == null) {
//                return;
//            }

            //cliente = request.getParameter("cliente");
            DAO db = new DAO(cliente);

            ResultSet rs = db.query(
                    "SELECT c.ds_ip AS ip, \n"
                    + "     cm.nr_ping AS ping, \n"
                    + "     cm.is_ativo AS ativo, \n"
                    + "     cm.ds_mensagem AS mensagem, \n"
                    + "     cm.ds_status AS status, \n"
                    + "     cm.nr_pessoa AS pessoa, \n"
                    + "     cm.ds_nome AS nome, \n"
                    + "     cm.nr_codigo_erro AS codigo_erro, \n"
                    + "     cm.ds_foto AS foto, \n"
                    + "     cm.ds_observacao AS observacao, \n"
                    + "     cm.nr_via AS via, \n"
                    + "     cm.is_liberado AS liberado, \n"
                    + "     c.ds_servidor_foto AS caminho_foto \n"
                    + "  FROM soc_catraca_monitora cm \n"
                    + " INNER JOIN soc_catraca c ON c.id = cm.id_catraca \n"
                    + " WHERE c.nr_numero = " + numeroPagina
                    + "   AND c.ds_mac = '" + mac + "'"
            );

            rs.next();
            if (rs.getObject("nome") == null) {
                catraca = new ObjectCatraca();
                catraca.setAtivo(rs.getBoolean("ativo"));
                catraca.setStatus(rs.getString("status"));
                return;
            }
            String caminho_foto = "";

            String ext[] = new String[3];
            ext[0] = "png";
            ext[1] = "jpg";
            ext[2] = "gif";

            if (rs.getObject("via") == null || rs.getInt("via") != 99) {
                for (String ext1 : ext) {
                    try {
                        String teste = "http://" + rs.getString("caminho_foto") + "/pessoa/" + rs.getInt("pessoa") + "/" + rs.getString("foto") + "." + ext1;
                        HttpURLConnection.setFollowRedirects(false);
                        // note : you may also need
                        //        HttpURLConnection.setInstanceFollowRedirects(false)
                        HttpURLConnection con = (HttpURLConnection) new URL(teste).openConnection();
                        con.setRequestMethod("HEAD");
                        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            caminho_foto = teste;
                            break;
                        }
                    } catch (SQLException | IOException e) {
                        e.getMessage();
                    }
                }
            } else {
                for (String ext1 : ext) {
                    try {
                        //String teste = "http://200.152.187.241:8052/Sindical/resources/cliente/sindical/imagens/sispessoa/" + rs.getInt("pessoa") + "/perfil/" + rs.getString("foto") + "." + ext1;
                        String teste = "http://" + rs.getString("caminho_foto") + "/sispessoa/" + rs.getInt("pessoa") + "/perfil/" + rs.getString("foto") + "." + ext1;
                        File file = new File(caminho_foto);
                        if (file.exists()) {
                            caminho_foto = teste;
                        }
                        break;
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }

            if (caminho_foto.isEmpty()) {
                // captura a url do servidor local
                //request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                //String url = request.getScheme() + "://" + request.getServerName() + ":" + String.valueOf(request.getServerPort()) + "/";
                String url = request.getServerName() + ":" + String.valueOf(request.getServerPort());

                caminho_foto = "http://" + url + "/monitorCatraca/resources/img/sem_foto.png";
            }

            catraca = new ObjectCatraca(
                    rs.getBoolean("ativo"),
                    rs.getString("status"),
                    rs.getInt("pessoa"),
                    rs.getString("nome"),
                    caminho_foto,
                    rs.getString("observacao"),
                    rs.getInt("codigo_erro"),
                    rs.getString("mensagem"),
                    rs.getBoolean("liberado")
            );
        } catch (Exception e) {
            e.getMessage();
            catraca = new ObjectCatraca();
        }
    }

    public void limpa_tela() {
        catraca = new ObjectCatraca();
    }

    public String get_pagina() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            if (request.getParameter("cliente") == null || request.getParameter("catraca") == null || request.getParameter("mac") == null) {
                return "";
            }

            cliente = request.getParameter("cliente");

            if ((!numeroPagina.equals(request.getParameter("catraca")) && !mac.equals(request.getParameter("mac"))) || caminhoPagina.isEmpty()) {

                numeroPagina = request.getParameter("catraca");

                mac = request.getParameter("mac");
                mac = mac.replace("-", "_");

                caminhoPagina = retornaCaminhoCliente(cliente);
                mostra_tela();

                return numeroPagina;
            }
        }
        return "";
    }

    public void load_pagina() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            Integer n_catraca = null;
            String macString = null;
            if (request.getParameter("cliente") != null && request.getParameter("catraca") != null) {
                n_catraca = Integer.valueOf(request.getParameter("catraca"));
            }
            if (request.getParameter("mac") != null && !request.getParameter("mac").isEmpty()) {
                macString = request.getParameter("mac");
            }
            if (n_catraca != null && macString == null) {
                WSSocket.send(request.getParameter("cliente") + "_monitora_" + n_catraca, "ok");
            }
            if (n_catraca != null && macString != null) {
                macString = macString.replace("-", "_");
                WSSocket.send(request.getParameter("cliente") + "_monitora_" + n_catraca + "_mac_" + macString, "ok");
            }
        }
    }

    public String retornaCaminhoCliente(String cliente) {
        //ws://192.168.1.108:8084/monitorCatraca/ws/monitora_#{monitoraController.numeroPagina}
        Default def = new Default();
        def.loadJson();
        switch (cliente) {
            case "ComercioRP":
                return "ws://" + def.getUrl_server() + "/monitorCatraca/ws/ComercioRP_monitora_" + numeroPagina + "_mac_" + mac;
            case "Sindical":
                return "ws://" + def.getUrl_server() + "/monitorCatraca/ws/Sindical_monitora_" + numeroPagina + "_mac_" + mac;
            default:
                return "ws://" + def.getUrl_server() + "/monitorCatraca/ws/" + cliente + "_monitora_" + numeroPagina + "_mac_" + mac;
        }
    }

    public String getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(String numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public ObjectCatraca getCatraca() {
        return catraca;
    }

    public void setCatraca(ObjectCatraca catraca) {
        this.catraca = catraca;
    }

    public String getCaminhoPagina() {
        return caminhoPagina;
    }

    public void setCaminhoPagina(String caminhoPagina) {
        this.caminhoPagina = caminhoPagina;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public class ObjectCatraca implements Serializable {

        private Boolean ativo;
        private String status;
        private Integer nr_pessoa;
        private String nome;
        private String foto;
        private String observacao;
        private Integer nr_codigo_erro;
        private String mensagem;
        private Boolean liberado;

        public ObjectCatraca() {
            this.ativo = false;
            this.status = "Inativa";
            this.nr_pessoa = null;
            this.nome = "";
            this.foto = "";
            this.observacao = "";
            this.nr_codigo_erro = null;
            this.mensagem = "";
            this.liberado = false;
        }

        public ObjectCatraca(Boolean ativo, String status, Integer nr_pessoa, String nome, String foto, String observacao, Integer nr_codigo_erro, String mensagem, Boolean liberado) {
            this.ativo = ativo;
            this.status = status;
            this.nr_pessoa = nr_pessoa;
            this.nome = nome;
            this.foto = foto;
            this.observacao = observacao;
            this.nr_codigo_erro = nr_codigo_erro;
            this.mensagem = mensagem;
            this.liberado = liberado;
        }

        public Boolean getAtivo() {
            return ativo;
        }

        public void setAtivo(Boolean ativo) {
            this.ativo = ativo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getNr_pessoa() {
            return nr_pessoa;
        }

        public void setNr_pessoa(Integer nr_pessoa) {
            this.nr_pessoa = nr_pessoa;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getFoto() {
            return foto;
        }

        public void setFoto(String foto) {
            this.foto = foto;
        }

        public String getObservacao() {
            return observacao;
        }

        public void setObservacao(String observacao) {
            this.observacao = observacao;
        }

        public Integer getNr_codigo_erro() {
            return nr_codigo_erro;
        }

        public void setNr_codigo_erro(Integer nr_codigo_erro) {
            this.nr_codigo_erro = nr_codigo_erro;
        }

        public String getMensagem() {
            return mensagem;
        }

        public void setMensagem(String mensagem) {
            this.mensagem = mensagem;
        }

        public Boolean getLiberado() {
            return liberado;
        }

        public void setLiberado(Boolean liberado) {
            this.liberado = liberado;
        }

    }

}
