/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.monitoraCatraca.factory;

import br.com.monitorCatraca.classes.ObjectCatraca;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Claudemir Rtools
 */
public class DaoObject extends DB {

    private final String cliente;

    public DaoObject(String cliente) {
        this.cliente = cliente;
    }

    public void begin() {
        getEntityManager(cliente).getTransaction().begin();
    }

    public void commit() {
        getEntityManager(cliente).getTransaction().commit();
    }

    public void rollback() {
        getEntityManager(cliente).getTransaction().rollback();
    }

    public boolean remove(Object object) {
        return remove(object, false);
    }

    public boolean remove(Object object, Boolean commit) {
        try {
            if (commit) {
                begin();
                getEntityManager(cliente).remove(find(object));
                getEntityManager(cliente).flush();
                commit();
                return true;
            } else {
                getEntityManager(cliente).remove(find(object));
                getEntityManager(cliente).flush();
                return true;
            }

        } catch (Exception e) {
            e.getMessage();
            if (commit) {
                rollback();
            }
        }
        return false;
    }

    public boolean save(Object object) {
        return save(object, false);
    }

    public boolean save(Object object, Boolean commit) {
        try {
            if (commit) {
                begin();
                getEntityManager(cliente).persist(object);
                getEntityManager(cliente).flush();
                commit();
                return true;
            } else {
                getEntityManager(cliente).persist(object);
                getEntityManager(cliente).flush();
                return true;
            }

        } catch (Exception e) {
            e.getMessage();
            if (commit) {
                rollback();
            }
        }
        return false;
    }

    public boolean update(Object object) {
        return update(object, false);
    }

    public boolean update(Object object, Boolean commit) {
        try {
            if (commit) {
                begin();
                getEntityManager(cliente).merge(object);
                getEntityManager(cliente).flush();
                commit();
                return true;
            } else {
                getEntityManager(cliente).merge(object);
                getEntityManager(cliente).flush();
                return true;
            }

        } catch (Exception e) {
            e.getMessage();
            if (commit) {
                rollback();
            }
        }
        return false;
    }

    public Object find(Object object) {
        try {
            Class classe = object.getClass();
            Method metodo = classe.getMethod("getId", new Class[]{});
            Integer id = (Integer) metodo.invoke(object, (Object[]) null);
            return getEntityManager(cliente).find(object.getClass(), id);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.getMessage();
        }
        return null;
    }

    public Object find(Object object, Object id) {
        try {
            return getEntityManager(cliente).find(object.getClass(), id);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }

    public Boolean queryExecute(String textQuery) {
        try {
            // NÃO FUNCIONA QUANDO A QUERY EXECUTADA RETORNA UM VALOR ( ALGUM REGISTRO )
//            begin();
//            int valor = getEntityManager(cliente).createNativeQuery(textQuery).executeUpdate();
//            commit();
            // SOLUÇÃO TEMPORÁRIA PARA ESTE PROBLEMA

            List<Object> result = getEntityManager(cliente).createNativeQuery(textQuery).getResultList();
            // NÃO ADIANTA USAR result.isEmpty() PORQUE A CLASSE NÃO RECONHECE QUANDO VEM OU NÃO VALOR
            // BASTA APENAS EXECUTAR A QUERY DANDO UM getResultList()
            
        } catch (Exception e) {
            e.getMessage();
        }
        return true;
    }

    public List<ObjectCatraca> listaCatraca(Integer servidor) {

        try {

            Query qry = getEntityManager(cliente).createNativeQuery(
                    " SELECT c.id, \n "
                    + "      c.ds_ip, \n "
                    + "      cm.nr_ping, \n "
                    + "      cm.is_ativo, \n "
                    + "      cm.ds_mensagem, \n "
                    + "      cm.ds_status, \n "
                    + "      cm.nr_pessoa, \n "
                    + "      cm.ds_nome, \n "
                    + "      cm.nr_codigo_erro, \n "
                    + "      cm.ds_foto, \n "
                    + "      cm.ds_observacao, \n "
                    + "      cm.nr_via, \n "
                    + "      cm.is_liberado, \n "
                    + "      c.ds_servidor_foto, \n "
                    + "      c.nr_numero, \n "
                    + "      c.ds_numero \n "
                    + "  FROM soc_catraca_monitora cm \n "
                    + " INNER JOIN soc_catraca c ON c.id = cm.id_catraca \n "
                    + " WHERE c.nr_servidor = " + servidor, ObjectCatraca.class
            );

            return qry.getResultList();

        } catch (Exception e) {
            e.getMessage();
        }

        return new ArrayList();
    }

    public ObjectCatraca pesquisaCatraca(Integer servidor, Integer numero_catraca) {
        try {

            Query qry = getEntityManager(cliente).createNativeQuery(
                    " SELECT c.id, \n "
                    + "      c.ds_ip, \n "
                    + "      cm.nr_ping, \n "
                    + "      cm.is_ativo, \n "
                    + "      cm.ds_mensagem, \n "
                    + "      cm.ds_status, \n "
                    + "      cm.nr_pessoa, \n "
                    + "      cm.ds_nome, \n "
                    + "      cm.nr_codigo_erro, \n "
                    + "      cm.ds_foto, \n "
                    + "      cm.ds_observacao, \n "
                    + "      cm.nr_via, \n "
                    + "      cm.is_liberado, \n "
                    + "      c.ds_servidor_foto, \n "
                    + "      c.nr_numero, \n "
                    + "      c.ds_numero \n "
                    + "  FROM soc_catraca_monitora cm \n "
                    + " INNER JOIN soc_catraca c ON c.id = cm.id_catraca \n "
                    + " WHERE c.nr_servidor = " + servidor
                    + "   AND c.nr_numero = " + numero_catraca, ObjectCatraca.class
            );

            return (ObjectCatraca) qry.getSingleResult();

        } catch (Exception e) {
            e.getMessage();
        }

        return null;
    }

}
