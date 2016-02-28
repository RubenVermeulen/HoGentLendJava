
package domein;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import util.JPAUtil;

public class GroepRepository {
    
    private List<Groep> groepen;
    private EntityManager em;

    public GroepRepository() {
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadFirmas();
    }
    
    private void loadFirmas() {
        Query q = em.createQuery("SELECT g FROM Groep g");
        groepen = (List<Groep>) q.getResultList();
    }

    public List<Groep> getLeergebieden() {
        return groepen.stream().filter(g -> g.isIsLeerGroep()).collect(Collectors.toList());
    }    
    
    public List<Groep> getDoelgroepen() {
        return groepen.stream().filter(g -> !g.isIsLeerGroep()).collect(Collectors.toList());
    }

    public List<Groep> geefLeergroepen(List<String> groep) {
        List<Groep> result = new ArrayList<>();
        for(Groep lg : getLeergebieden()){
            for (String str : groep){
                if (str.equalsIgnoreCase(lg.getGroep())){
                    result.add(lg);
                }
            }
        }
        return result;
    } 
    
    public List<Groep> geefDoelgroep(List<String> groep) {
         List<Groep> result = new ArrayList<>();
        for(Groep lg : getDoelgroepen()){
            for (String str : groep){
                if (str.equalsIgnoreCase(lg.getGroep())){
                    result.add(lg);
                }
            }
        }
        return result;
    }

    public Groep voegLeergroepToe(String groep) {
        return voegGroepToe(groep, true);
    }
    
    public Groep voegDoelgroepToe(String groep) {
        return voegGroepToe(groep, false);
    }
    
    private Groep voegGroepToe(String groep, boolean isLeergroep){
        Groep g = new Groep(groep,isLeergroep);
        
        em.getTransaction().begin();
        em.persist(g);
        em.getTransaction().commit();
        
        return g;
    }
}
