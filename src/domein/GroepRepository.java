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
        return groepen.stream().filter(g -> g.isLeerGroep()).collect(Collectors.toList());
    }

    public List<Groep> getDoelgroepen() {
        return groepen.stream().filter(g -> !g.isLeerGroep()).collect(Collectors.toList());
    }

    public List<Groep> geefLeergroepen(List<String> groep) {
        if (groep == null) {
            return null;
        }
        List<Groep> result = new ArrayList<>();
        for (Groep lg : getLeergebieden()) {
            for (String str : groep) {
                if (str.equalsIgnoreCase(lg.getGroep())) {
                    result.add(lg);
                }
            }
        }
        return result;
    }

    public List<Groep> geefDoelgroep(List<String> groep) {
        if (groep == null) {
            return null;
        }
        List<Groep> result = new ArrayList<>();
        for (Groep lg : getDoelgroepen()) {
            for (String str : groep) {
                if (str.equalsIgnoreCase(lg.getGroep())) {
                    result.add(lg);
                }
            }
        }
        return result;
    }

    public Groep voegGroepToe(String groep, boolean isLeergroep) {
        Groep result = new Groep(groep, isLeergroep);
        if (isLeergroep) {
            if (getLeergebieden().stream().map(g -> g.getGroep()).anyMatch(s -> s.equalsIgnoreCase(groep))) {
                throw new IllegalArgumentException("Dit leergebied bestaat al.");
            }
        } else if (getDoelgroepen().stream().map(g -> g.getGroep()).anyMatch(s -> s.equalsIgnoreCase(groep))) {
            throw new IllegalArgumentException("Deze doelgroep bestaat al.");
        }

        em.getTransaction().begin();
        em.persist(result);
        em.getTransaction().commit();
        groepen.add(result);
        return result;
    }

    public void verwijderGroep(String groepStr, boolean isLeerGroep) {
        if (groepStr == null || groepStr.isEmpty()) {
            throw new IllegalArgumentException("Je hebt geen " + (isLeerGroep ? "leergebied" : "doelgroep") + " geselecteerd.");
        }
        Optional<Groep> groepOpt;
        if (isLeerGroep) {
            groepOpt = getLeergebieden().stream().filter(s -> s.getGroep().equalsIgnoreCase(groepStr)).findFirst();
            if (!groepOpt.isPresent()) {
                throw new IllegalArgumentException("Die leergebied bestaat niet.");
            }
        } else {
            groepOpt = getDoelgroepen().stream().filter(s -> s.getGroep().equalsIgnoreCase(groepStr)).findFirst();
            if (!groepOpt.isPresent()) {
                throw new IllegalArgumentException("Die doelgroep bestaat niet.");
            }
        }
        
        em.getTransaction().begin();
        em.remove(groepOpt.get());
        em.getTransaction().commit();
        groepen.remove(groepOpt.get());
    }
}
