/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.monitoraCatraca.factory;

import java.io.File;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Claudemir Rtools
 */
public class Default {

    private String url_server;
    private String url_postgres;
    private String user_postgres;
    private String pass_postgres;

    public Default() {
        url_server = "";
        url_postgres = "";
        user_postgres = "";
        pass_postgres = "";
    }

    public void loadJson() {
        FacesContext faces = FacesContext.getCurrentInstance();
        try {
            File file = new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/resources/config/default.json"));
            if (!file.exists()) {
                return;
            }
            String json = null;
            try {
                json = FileUtils.readFileToString(file);
            } catch (IOException ex) {
                ex.getMessage();
            }
            JSONObject jSONObject = new JSONObject(json);
            try {
                url_server = jSONObject.getString("url_server");
            } catch (JSONException e) {
                e.getMessage();
            }
            try {
                url_postgres = jSONObject.getString("url_postgres");
            } catch (JSONException e) {
                e.getMessage();
            }
            try {
                user_postgres = jSONObject.getString("user_postgres");
            } catch (JSONException e) {
                e.getMessage();
            }
            try {
                pass_postgres = jSONObject.getString("pass_postgres");
            } catch (JSONException e) {
                e.getMessage();
            }
        } catch (JSONException ex) {
            ex.getMessage();
        }
    }

    public String getUrl_server() {
        return url_server;
    }

    public void setUrl_server(String url_server) {
        this.url_server = url_server;
    }

    public String getUrl_postgres() {
        return url_postgres;
    }

    public void setUrl_postgres(String url_postgres) {
        this.url_postgres = url_postgres;
    }

    public String getUser_postgres() {
        return user_postgres;
    }

    public void setUser_postgres(String user_postgres) {
        this.user_postgres = user_postgres;
    }

    public String getPass_postgres() {
        return pass_postgres;
    }

    public void setPass_postgres(String pass_postgres) {
        this.pass_postgres = pass_postgres;
    }
}
