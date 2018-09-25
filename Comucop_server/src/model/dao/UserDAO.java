/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.io.Serializable;
import java.util.List;
import model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Rodrigo Maia
 */
public class UserDAO extends GeneralDAO{    
   
    public static Object auth(Session sess,Class ent,String login, String pass) {        
        Criteria c = sess.createCriteria(User.class,"u");
        c.add(Restrictions.eq( "u.usLogin",login));
        c.add(Restrictions.eq("u.usPassword", pass));
        return c.list();  
    }
}
