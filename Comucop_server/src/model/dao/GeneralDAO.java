/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;


import java.io.Serializable;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;

/**
 *
 * @author Rodrigo Maia
 */
public class GeneralDAO {

    public static void create(Session sess,Object ent) {
        sess.save(ent);
    }

    public static Object load(Session sess,Class ent, Serializable id) {
        return sess.load(ent, id);
    }

    public static void update(Session sess, Object ent) {
        sess.update(ent);
    }

    public static void del(Session sess, Object ent) {
        sess.delete(ent);
    }

    public static List all(Session sess,Class table) {
        Criteria c = sess.createCriteria(table);
        return c.list();
    }
}
