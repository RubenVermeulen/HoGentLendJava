package config;


import java.util.HashMap;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ruben
 */
public class DatabaseConfig {
    private static Map<String, String> persistenceMap = null;
    
    public static Map<String, String> getPersistenceMap() {
        if (persistenceMap != null) return persistenceMap;
        
        Map<String, String> map = new HashMap<>();

        map.put("javax.persistence.jdbc.url", "<url>");
        map.put("javax.persistence.jdbc.user", "<user>");
        map.put("javax.persistence.jdbc.password", "<password>");
        map.put("javax.persistence.jdbc.driver", "<driver>");
        
        return map;
    }
}
