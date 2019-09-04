/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.monitoraCatraca.utils;

import br.com.monitorCatraca.classes.ObjectCatraca;

/**
 *
 * @author Claudemir Rtools
 */
public class TemplateMonitor {

    private String servidor;
    private String cliente;
    private String catraca;

    private Integer numeroTemplate;

    private ObjectCatraca catraca01;
    private ObjectCatraca catraca02;
    private ObjectCatraca catraca03;
    private ObjectCatraca catraca04;

    public TemplateMonitor() {
        this.servidor = null;
        this.cliente = null;
        this.catraca = null;
        this.numeroTemplate = null;
        this.catraca01 = null;
        this.catraca02 = null;
        this.catraca03 = null;
        this.catraca04 = null;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getCatraca() {
        return catraca;
    }

    public void setCatraca(String catraca) {
        this.catraca = catraca;
    }

    public Integer getNumeroTemplate() {
        return numeroTemplate;
    }

    public void setNumeroTemplate(Integer numeroTemplate) {
        this.numeroTemplate = numeroTemplate;
    }

    public ObjectCatraca getCatraca01() {
        return catraca01;
    }

    public void setCatraca01(ObjectCatraca catraca01) {
        this.catraca01 = catraca01;
    }

    public ObjectCatraca getCatraca02() {
        return catraca02;
    }

    public void setCatraca02(ObjectCatraca catraca02) {
        this.catraca02 = catraca02;
    }

    public ObjectCatraca getCatraca03() {
        return catraca03;
    }

    public void setCatraca03(ObjectCatraca catraca03) {
        this.catraca03 = catraca03;
    }

    public ObjectCatraca getCatraca04() {
        return catraca04;
    }

    public void setCatraca04(ObjectCatraca catraca04) {
        this.catraca04 = catraca04;
    }

}
