/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ruben
 */
public class JPAUtil {

    private final static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence");

    private JPAUtil() {
    }

    public static JPAUtil getInstance() {
        return JPAUtilHolder.INSTANCE;
    }

    private static class JPAUtilHolder {

        private static final JPAUtil INSTANCE = new JPAUtil();
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
