package domein;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import util.JPAUtil;

public class FirmaRepository {

    private List<Firma> firmas;
    private EntityManager em;

    public FirmaRepository() {
        this.em = JPAUtil.getEntityManagerFactory().createEntityManager();
        loadFirmas();
    }
    
    private void loadFirmas() {
        Query q = em.createQuery("SELECT f FROM Firma f ORDER BY f.naam");
        firmas = (List<Firma>) q.getResultList();
    }

    public List<Firma> getFirmas() {
        Collections.sort(firmas, new Comparator<Firma>() {
            @Override
            public int compare(Firma o1, Firma o2) {
                return o1.getNaam().compareToIgnoreCase(o2.getNaam());
            }
            
        });
        
        return firmas;
    }

    public Firma geefFirma(String naam) {
        
        for (Firma f : firmas) {
            
            System.out.println("xp" + f.getNaam());
                
            if (f.getNaam().equals(naam)) {
                System.out.println("xd" + f.getNaam());
                return f;
            }
        }
        
        return null;
    }

    //maakt nieuwe firma aan en returnt hem
    public Firma voegFirmaToe(String naam, String email) {

        if (naam == null || naam.isEmpty())
            throw new IllegalArgumentException("Firma naam is verplicht");
        
        if (email == null || email.isEmpty()) 
            throw new IllegalArgumentException("Firma email is verplicht");
        
        if (geefFirma(naam) != null)
            throw new IllegalArgumentException("Firma naam is al in gebruik");
        
        if (! email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"))
            throw new IllegalArgumentException("Geef een geldig e-mailadres op");
        
        Firma firma = new Firma(naam);
        firma.setEmail(email);
        
        
        em.getTransaction().begin();
        em.persist(firma);
        em.getTransaction().commit();
        
        firmas.add(firma);
        
        System.out.println("MOIJSDMOFIJSDMOIFJ" + firmas);
        System.out.println(geefFirma(naam));
        
        return firma;

    }
    
}
