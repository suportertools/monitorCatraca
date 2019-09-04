/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.monitorCatraca.classes;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Claudemir Rtools
 */
@Entity
public class ObjectCatraca implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "ds_ip")
    private String ds_ip;

    @Column(name = "nr_ping")
    private Integer nr_ping;

    @Column(name = "is_ativo")
    private Boolean is_ativo;

    @Column(name = "ds_mensagem")
    private String ds_mensagem;

    @Column(name = "ds_status")
    private String ds_status;

    @Column(name = "nr_pessoa")
    private Integer nr_pessoa;

    @Column(name = "ds_nome")
    private String ds_nome;

    @Column(name = "nr_codigo_erro")
    private Integer nr_codigo_erro;

    @Column(name = "ds_foto")
    private String ds_foto;

    @Column(name = "ds_observacao")
    private String ds_observacao;

    @Column(name = "nr_via")
    private Integer nr_via;

    @Column(name = "is_liberado")
    private Boolean is_liberado;

    @Column(name = "ds_servidor_foto")
    private String ds_servidor_foto;

    @Column(name = "nr_numero")
    private Integer nr_numero;

    @Column(name = "ds_numero")
    private String ds_numero;

    public ObjectCatraca() {
        this.id = null;
        this.ds_ip = "";
        this.nr_ping = 0;
        this.is_ativo = false;
        this.ds_mensagem = "";
        this.ds_status = "Inativo";
        this.nr_pessoa = 0;
        this.ds_nome = "";
        this.nr_codigo_erro = 0;
        this.ds_foto = "";
        this.ds_observacao = "";
        this.nr_via = 0;
        this.is_liberado = false;
        this.ds_servidor_foto = "";
        this.nr_numero = null;
        this.ds_numero = "";
    }

    public ObjectCatraca(Integer id, String ds_ip, Integer nr_ping, Boolean is_ativo, String ds_mensagem, String ds_status, Integer nr_pessoa, String ds_nome, Integer nr_codigo_erro, String ds_foto, String ds_observacao, Integer nr_via, Boolean is_liberado, String ds_servidor_foto, Integer nr_numero, String ds_numero) {
        this.id = id;
        this.ds_ip = ds_ip;
        this.nr_ping = nr_ping;
        this.is_ativo = is_ativo;
        this.ds_mensagem = ds_mensagem;
        this.ds_status = ds_status;
        this.nr_pessoa = nr_pessoa;
        this.ds_nome = ds_nome;
        this.nr_codigo_erro = nr_codigo_erro;
        this.ds_foto = ds_foto;
        this.ds_observacao = ds_observacao;
        this.nr_via = nr_via;
        this.is_liberado = is_liberado;
        this.ds_servidor_foto = ds_servidor_foto;
        this.nr_numero = nr_numero;
        this.ds_numero = ds_numero;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDs_ip() {
        return ds_ip;
    }

    public void setDs_ip(String ds_ip) {
        this.ds_ip = ds_ip;
    }

    public Integer getNr_ping() {
        return nr_ping;
    }

    public void setNr_ping(Integer nr_ping) {
        this.nr_ping = nr_ping;
    }

    public Boolean getIs_ativo() {
        return is_ativo;
    }

    public void setIs_ativo(Boolean is_ativo) {
        this.is_ativo = is_ativo;
    }

    public String getDs_mensagem() {
        return ds_mensagem;
    }

    public void setDs_mensagem(String ds_mensagem) {
        this.ds_mensagem = ds_mensagem;
    }

    public String getDs_status() {
        return ds_status;
    }

    public void setDs_status(String ds_status) {
        this.ds_status = ds_status;
    }

    public Integer getNr_pessoa() {
        return nr_pessoa;
    }

    public void setNr_pessoa(Integer nr_pessoa) {
        this.nr_pessoa = nr_pessoa;
    }

    public String getDs_nome() {
        return ds_nome;
    }

    public void setDs_nome(String ds_nome) {
        this.ds_nome = ds_nome;
    }

    public Integer getNr_codigo_erro() {
        return nr_codigo_erro;
    }

    public void setNr_codigo_erro(Integer nr_codigo_erro) {
        this.nr_codigo_erro = nr_codigo_erro;
    }

    public String getDs_foto() {
        // NA PESQUISA NO BANCO ds_foto VEM VAZIO, TRATO ESTE CASO AQUI CASO NÃO VENHA PARA 
        // CRIAR O CAMINHO DA IMAGEM NO SERVIDOR, LOGO QUE NO BANCO DE DADOS ESTÁ APENAS O 
        // NOME DA IMAGEM
        if (ds_foto == null || ds_foto.isEmpty()) {
            ds_foto = "/resources/img/sem_foto.png";
        } else {

            String ext[] = new String[3];

            ext[0] = "png";
            ext[1] = "jpg";
            ext[2] = "gif";

            if (nr_via == null || nr_via != 99) {
                for (String ext1 : ext) {
                    try {
                        String teste = "http://" + ds_servidor_foto + "/pessoa/" + nr_pessoa + "/" + ds_foto + "." + ext1;
                        HttpURLConnection.setFollowRedirects(false);

                        HttpURLConnection con = (HttpURLConnection) new URL(teste).openConnection();
                        con.setRequestMethod("HEAD");

                        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            ds_foto = teste;
                            break;
                        }
                    } catch (IOException ex) {
                        ex.getMessage();
                    }
                }
            } else {
                for (String ext1 : ext) {
                    //String teste = "http://200.152.187.241:8052/Sindical/resources/cliente/sindical/imagens/sispessoa/" + rs.getInt("pessoa") + "/perfil/" + rs.getString("foto") + "." + ext1;
                    String teste = "http://" + ds_servidor_foto + "/sispessoa/" + nr_pessoa + "/perfil/" + ds_foto + "." + ext1;
                    File file = new File(teste);
                    if (file.exists()) {
                        ds_foto = teste;
                    }
                    break;
                }
            }

            // SE MESMO COM OS CASOS ACIMA A FOTO FOR VAZIA
            if (ds_foto == null || ds_foto.isEmpty()) {
                ds_foto = "/resources/img/sem_foto.png";
            }
        }
        return ds_foto;
    }

    public void setDs_foto(String ds_foto) {
        this.ds_foto = ds_foto;
    }

    public String getDs_observacao() {
        return ds_observacao;
    }

    public void setDs_observacao(String ds_observacao) {
        this.ds_observacao = ds_observacao;
    }

    public Integer getNr_via() {
        return nr_via;
    }

    public void setNr_via(Integer nr_via) {
        this.nr_via = nr_via;
    }

    public Boolean getIs_liberado() {
        return is_liberado;
    }

    public void setIs_liberado(Boolean is_liberado) {
        this.is_liberado = is_liberado;
    }

    public String getDs_servidor_foto() {
        return ds_servidor_foto;
    }

    public void setDs_servidor_foto(String ds_servidor_foto) {
        this.ds_servidor_foto = ds_servidor_foto;
    }

    public Integer getNr_numero() {
        return nr_numero;
    }

    public void setNr_numero(Integer nr_numero) {
        this.nr_numero = nr_numero;
    }

    public String getDs_numero() {
        return ds_numero;
    }

    public void setDs_numero(String ds_numero) {
        this.ds_numero = ds_numero;
    }

}
