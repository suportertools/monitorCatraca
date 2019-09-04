package br.com.monitoraCatraca.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAO_backup {

    private Statement statement;
    private Boolean conectado;

    public DAO_backup(String cliente) {
        switch (cliente) {
            case "ComercioRP":
                conectaComercioRP();
                break;
            case "ComercioLimeira":
                conectaComercioLimeira();
                break;
            default:
                conectaDefault(cliente);
                break;
        }
    }

    public final void conectaComercioRP() {
        try {
            String conexao = "jdbc:postgresql://200.152.187.241:5432/Sindical";
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(conexao, "postgres", "989899");

            statement = con.createStatement();
            conectado = true;
        } catch (ClassNotFoundException | SQLException e) {
            e.getMessage();
            conectado = false;
        }
    }

    public void conectaComercioLimeira() {
        try {
            String conexao = "jdbc:postgresql://sinecol.ddns.net:5432/ComercioLimeira";
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection(conexao, "climeira", "X_tub4r40#*");

            statement = con.createStatement();
            conectado = true;
        } catch (ClassNotFoundException | SQLException e) {
            e.getMessage();
            conectado = false;
        }
    }

    public final void conectaDefault(String cliente) {
        try {
            String conexao;
            Connection con;
            Class.forName("org.postgresql.Driver");
            
            if (cliente.equals("Sindical")) {
                conexao = "jdbc:postgresql://192.168.15.35:5432/ComercioRP";
                con = DriverManager.getConnection(conexao, "postgres", "*4qu4r10-");
            } else {
                conexao = "jdbc:postgresql://192.168.15.100:5432/" + cliente;
                con = DriverManager.getConnection(conexao, "postgres", "sisrt**ls989899#@");
            }
            
            statement = con.createStatement();
            conectado = true;
        } catch (ClassNotFoundException | SQLException e) {
            e.getMessage();
            conectado = false;
        }
    }

//    public void disconect() {
//        try {
//            getStatement().close();
//        } catch (SQLException ex) {
//            ex.getMessage();
//        }
//    }

    public ResultSet query(String qry) {
        try {
            ResultSet rs = getStatement().executeQuery(qry);
            getStatement().getConnection().close();
            return rs;
        } catch (SQLException e) {
            e.getMessage();
        }
        return null;
    }
    
    public void query_execute(String qry) {
        try {
            Integer x = getStatement().executeUpdate(qry);
            getStatement().getConnection().close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Boolean getConectado() {
        return conectado;
    }

    public void setConectado(Boolean conectado) {
        this.conectado = conectado;
    }
}
