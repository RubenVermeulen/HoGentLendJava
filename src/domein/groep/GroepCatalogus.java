package domein.groep;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GroepCatalogus {

    private List<Groep> groepen;

    public GroepCatalogus(List<Groep> groepen) {
        this.groepen = groepen;
    }

    public List<Groep> getLeerGebieden() {
        return groepen.stream().filter(g -> g.isLeerGroep()).collect(Collectors.toList());
    }

    public List<Groep> getDoelGroepen() {
        return groepen.stream().filter(g -> !g.isLeerGroep()).collect(Collectors.toList());
    }

    public List<Groep> convertStringListToLeerGebiedenList(List<String> leerGebiedenStr) {
        if (leerGebiedenStr == null) {
            return null;
        }
        List<Groep> result = new ArrayList<>();
        for (Groep lg : getLeerGebieden()) {
            for (String str : leerGebiedenStr) {
                if (str.equalsIgnoreCase(lg.getGroep())) {
                    result.add(lg);
                }
            }
        }
        return result;
    }

    public List<Groep> convertStringListToDoelGroepenList(List<String> doelGroepenStr) {
        if (doelGroepenStr == null) {
            return null;
        }
        List<Groep> result = new ArrayList<>();
        for (Groep lg : getDoelGroepen()) {
            for (String str : doelGroepenStr) {
                if (str.equalsIgnoreCase(lg.getGroep())) {
                    result.add(lg);
                }
            }
        }
        return result;
    }

    public Groep voegGroepToe(String groepNaam, boolean isLeerGebied) {
        Groep result = new Groep(groepNaam, isLeerGebied);

        if (isLeerGebied) {
            if (getLeerGebieden().stream().map(g -> g.getGroep()).anyMatch(s -> s.equalsIgnoreCase(groepNaam))) {
                throw new IllegalArgumentException("Dit leergebied bestaat al.");
            }
        } else if (getDoelGroepen().stream().map(g -> g.getGroep()).anyMatch(s -> s.equalsIgnoreCase(groepNaam))) {
            throw new IllegalArgumentException("Deze doelgroep bestaat al.");
        }

        groepen.add(result);

        return result;
    }

    public Optional<Groep> geefGroep(String groepStr, boolean isLeerGebied) {
        Optional<Groep> groepOpt;
        if (isLeerGebied) {
            groepOpt = getLeerGebieden().stream().filter(s -> s.getGroep().equalsIgnoreCase(groepStr)).findFirst();
        } else {
            groepOpt = getDoelGroepen().stream().filter(s -> s.getGroep().equalsIgnoreCase(groepStr)).findFirst();
        }
        return groepOpt;
    }

    public void verwijderGroep(Groep groep) {
        groepen.remove(groep);
    }

}
