package domein.config;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import shared.ConfigView;
import util.JPAUtil;

public class ConfigLoader {
    private EntityManager em;
    
    public ConfigLoader() {
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
    }
    
    public Config load() {
        Query q = em.createQuery("SELECT c FROM Config c");
        Config config = (Config) q.getSingleResult();
        
        return config;
    }
    
    public void save() {
         em.getTransaction().begin();
         em.getTransaction().commit();
    }
}
