/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.monitoraCatraca.factory;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.eclipse.persistence.config.CacheType;

/**
 *
 * @author Claudemir Rtools
 */
public class DB {

    private EntityManager entityManager;

    protected EntityManager getEntityManager(String cliente) {
        try {
            if (entityManager == null) {

                // FIXO PARA TESTES CLIENTE COMERCIO SOROCABA ------------------
                Map properties = new HashMap();
                properties.put(PersistenceUnitProperties.CACHE_TYPE_DEFAULT, CacheType.SoftWeak);
                properties.put(PersistenceUnitProperties.TRANSACTION_TYPE, "RESOURCE_LOCAL");
                properties.put(PersistenceUnitProperties.JDBC_DRIVER, "org.postgresql.Driver");

                switch (cliente) {
                    case "ComercioRP":
                        properties.put(PersistenceUnitProperties.JDBC_USER, "postgres");
                        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, "989899");
                        properties.put(PersistenceUnitProperties.JDBC_URL, "jdbc:postgresql://45.225.168.34:5432/Sindical");
                        break;
                    case "ComercioLimeira":
                        properties.put(PersistenceUnitProperties.JDBC_USER, "climeira");
                        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, "X_tub4r40#*");
                        properties.put(PersistenceUnitProperties.JDBC_URL, "jdbc:postgresql://sinecol.ddns.net:5432/ComercioLimeira");
                        break;
                    case "Sindical":
                    case "ComercioBarretos": // APENAS EXEMPLO DE CONEXÃO, NÃO TEM CATRACA EM BARRETOS ------------------------------
                    case "ComercioBebedouro": // APENAS EXEMPLO DE CONEXÃO, NÃO TEM CATRACA EM BEBEDOURO ----------------------------
                        properties.put(PersistenceUnitProperties.JDBC_USER, "postgres");
                        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, "*4qu4r10-");
                        properties.put(PersistenceUnitProperties.JDBC_URL, "jdbc:postgresql://192.168.15.35:5432/ComercioRP");
                        break;
                    default:
                        // COMERCIO SOROCABA SE CONECTA AQUI ----------------------------
                        properties.put(PersistenceUnitProperties.JDBC_USER, "postgres");
                        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, "sisrt**ls989899#@");
                        properties.put(PersistenceUnitProperties.JDBC_URL, "jdbc:postgresql://192.168.15.100:5432/" + cliente);
                }

                //properties.put(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_UNITS, "Persistence");
                // -------------------------------------------------------------
                // -------------------------------------------------------------
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistence", properties);
                emf.getCache().evictAll(); // LIMPA O CACHE PARA SEMPRE PEGAR OS REGISTROS ATUALIZADOS DO BANCO DE DADOS ( EX. QUANDO FAZ UM UPDATE NO BD O SISTEMA NÃO ATUALIZAVA )
                entityManager = emf.createEntityManager();

            }
        } catch (Exception e) {
            e.getMessage();
            System.err.println(e.getMessage() + " \n " + e.getCause().getMessage());
        }
        return entityManager;
    }

    protected void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
