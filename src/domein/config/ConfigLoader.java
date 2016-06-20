package domein.config;

import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
        Config config;
        try {
            config = (Config) q.getSingleResult();
        } catch (NoResultException e) {
            config = new Config();
            config.setStandaardIndientijd(LocalDateTime.of(2000,2,2, LocalTime.NOON.getHour(), LocalTime.NOON.getMinute()));
            config.setStandaardOphaaltijd(LocalDateTime.of(2000,2,2, LocalTime.MIDNIGHT.getHour(), LocalTime.MIDNIGHT.getMinute()));
            config.setStandaardOphaalDag("maandag");
            config.setStandaardIndienDag("vrijdag");

        }

        return config;
    }

    public void save() {
        em.getTransaction().begin();
        em.getTransaction().commit();
    }
}
