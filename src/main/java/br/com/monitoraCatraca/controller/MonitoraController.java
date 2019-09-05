/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.monitoraCatraca.controller;

import br.com.monitorCatraca.classes.ObjectCatraca;
import br.com.monitoraCatraca.factory.DaoObject;
import br.com.monitoraCatraca.factory.Default;
import br.com.monitoraCatraca.utils.Datas;
import br.com.monitoraCatraca.utils.TemplateMonitor;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Claudemir Rtools
 */
@ManagedBean
@SessionScoped
public class MonitoraController implements Serializable {

    private TemplateMonitor templateMonitor = null;
    private String caminhoWebSocket = "";

    public void loadConfig() {
        try {
            // NÃO LEMBRO PORQUE ESTAVA BLOQUEANDO EVENTOS EM AJAX -------------
            //if (!FacesContext.getCurrentInstance().isPostback()) {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

            // SE FOR NULL FOI UMA REQUISIÇÃO EM AJAX, NESSE CASO NÃO TEM REQUEST, templateMonitor JÁ DEVE ESTAR PREENCHIDA
            if (templateMonitor == null || (request.getParameter("cliente") != null && request.getParameter("servidor") != null)) {

                // INICIO DAS CONFIGURAÇÕES
                templateMonitor = new TemplateMonitor();
                // SE NÃO ENCONTRAR CLIENTE E SERVIDOR -------------------------
                if (request.getParameter("cliente") == null || request.getParameter("servidor") == null) {
                    // ERRO DE SERVIDOR
                    redirect("erroServidor");
                    return;
                }

                // SE ENTRAR NA TELA PASSANDO OS PARAMETROS
                templateMonitor.setCliente(request.getParameter("cliente"));
                templateMonitor.setServidor(request.getParameter("servidor"));
                templateMonitor.setCatraca(request.getParameter("catraca"));

            }

            DaoObject dao = new DaoObject(templateMonitor.getCliente());

            // CASO NÃO TENHA UMA CATRACA ESPECÍFICA -----------------------
            if (templateMonitor.getCatraca() == null) {
                List<ObjectCatraca> result = dao.listaCatraca(Integer.valueOf(templateMonitor.getServidor()));

                if (result.isEmpty()) {
                    redirect("erroServidor");
                    return;
                }

                switch (result.size()) {
                    case 1:
                        templateMonitor.setNumeroTemplate(1);
                        templateMonitor.setCatraca01(dao.pesquisaCatraca(Integer.valueOf(templateMonitor.getServidor()), 1));

                        break;
                    case 2:
                        templateMonitor.setNumeroTemplate(2);
                        templateMonitor.setCatraca01(dao.pesquisaCatraca(Integer.valueOf(templateMonitor.getServidor()), 1));
                        templateMonitor.setCatraca02(dao.pesquisaCatraca(Integer.valueOf(templateMonitor.getServidor()), 2));

                        break;
                    case 3:
                        templateMonitor.setNumeroTemplate(3);
                        templateMonitor.setCatraca01(dao.pesquisaCatraca(Integer.valueOf(templateMonitor.getServidor()), 1));
                        templateMonitor.setCatraca02(dao.pesquisaCatraca(Integer.valueOf(templateMonitor.getServidor()), 2));
                        templateMonitor.setCatraca03(dao.pesquisaCatraca(Integer.valueOf(templateMonitor.getServidor()), 3));

                        break;
                    case 4:
                        templateMonitor.setNumeroTemplate(4);
                        templateMonitor.setCatraca01(dao.pesquisaCatraca(Integer.valueOf(templateMonitor.getServidor()), 1));
                        templateMonitor.setCatraca02(dao.pesquisaCatraca(Integer.valueOf(templateMonitor.getServidor()), 2));
                        templateMonitor.setCatraca03(dao.pesquisaCatraca(Integer.valueOf(templateMonitor.getServidor()), 3));
                        templateMonitor.setCatraca04(dao.pesquisaCatraca(Integer.valueOf(templateMonitor.getServidor()), 4));

                        break;
                    default:
                        // ERRO DE SERVIDOR
                        redirect("erroServidor");
                        return;
                }

            } else {
                // CASO PASSE O PARAMETRO DE VISUALIZAR UMA CATRACA ESPECÍFICA 
                ObjectCatraca ob_catraca = dao.pesquisaCatraca(Integer.valueOf(templateMonitor.getServidor()), Integer.parseInt(templateMonitor.getCatraca()));

                if (ob_catraca == null) {
                    // ERRO DE SERVIDOR
                    redirect("erroServidor");
                    return;
                }

                templateMonitor.setNumeroTemplate(1);
                templateMonitor.setCatraca01(ob_catraca);
            }

            caminhoWebSocket = retornaCaminhoCliente(templateMonitor.getCliente(), templateMonitor.getServidor());

        } catch (NumberFormatException e) {
            e.getMessage();
        }
    }

    public void redirectTemplate(String template) {
        try {
            FacesContext conext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
            session.invalidate();
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/templates/monitor/" + template + ".xhtml");
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public void redirect(String pagina) {
        try {
            FacesContext conext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
            session.invalidate();
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/" + pagina + ".xhtml");
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public String retornaCaminhoCliente(String cliente, String servidor) {
        //ws://192.168.15.109:8080/monitorCatraca/ws/monitora_#{monitoraController.numeroPagina}
        //ws://192.168.15.109:8080/monitorCatraca/ws/ComercioSorocaba_servidor_1
        Default def = new Default();

        def.loadJson();

        switch (cliente) {
            case "ComercioRP":
                return "ws://" + def.getUrl_server() + "/monitorCatraca/ws/ComercioRP_servidor_" + servidor;
            case "Sindical":
                return "ws://" + def.getUrl_server() + "/monitorCatraca/ws/Sindical_servidor_" + servidor;
            default:
                return "ws://" + def.getUrl_server() + "/monitorCatraca/ws/" + cliente + "_servidor_" + servidor;
        }

    }

    public TemplateMonitor getTemplateMonitor() {
        return templateMonitor;
    }

    public void setTemplateMonitor(TemplateMonitor templateMonitor) {
        this.templateMonitor = templateMonitor;
    }

    public String getCaminhoWebSocket() {
        return caminhoWebSocket;
    }

    public void setCaminhoWebSocket(String caminhoWebSocket) {
        this.caminhoWebSocket = caminhoWebSocket;
    }

    public String datahora() {
        return Datas.data();
    }

}
