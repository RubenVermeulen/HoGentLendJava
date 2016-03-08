package domein.groep;

import domein.materiaal.Materiaal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GroepCatalogus {

    private List<Groep> groepen;

    public GroepCatalogus(List<Groep> groepen) {
        this.groepen = groepen;
    }

    /**
     * Geeft alle leergebieden.
     *
     * @return de leergebieden
     */
    public List<Groep> getLeerGebieden() {
        return groepen.stream().filter(g -> g.isLeerGroep()).collect(Collectors.toList());
    }

    /**
     * Geeft alle doelgroepen
     *
     * @return de doelgroepen
     */
    public List<Groep> getDoelGroepen() {
        return groepen.stream().filter(g -> !g.isLeerGroep()).collect(Collectors.toList());
    }

    /**
     * Overloopt de gegeven string lijst. Iedere groep die gevonden wordt met
     * een naam die in de lijst voorkomt word terug gegeven.
     *
     * @param groepenStr al de namen van de groepen die je wilt terugkrijgen
     * @param isLeerGebied of het gaat om doelgroepen of leergebieden
     * @return de groepen
     */
    public List<Groep> convertStringListToGroepList(List<String> groepenStr, boolean isLeerGebied) {
        if (groepenStr == null) {
            return null;
        }
        List<Groep> inTeZoekenList = getInTeZoekenGroepen(isLeerGebied);

        List<Groep> result = inTeZoekenList.stream()
                .filter(g
                        -> groepenStr.stream()
                        .anyMatch(gStr -> g.getGroep().equalsIgnoreCase(gStr))
                ).collect(Collectors.toList());

        return result;
    }

    /**
     * Voegt een nieuwe leergebied/doelgroep toe met de gegeven naam.
     *
     * @param groepNaam de naam van de groep
     * @param isLeerGebied of het een leergebied of doelgroep is
     * @return De toegevoegde groep
     */
    public Groep voegGroepToe(String groepNaam, boolean isLeerGebied) {
        Groep result = new Groep(groepNaam, isLeerGebied);
        List<Groep> inTeZoekenList = getInTeZoekenGroepen(isLeerGebied);

        if (inTeZoekenList.stream().map(g -> g.getGroep()).anyMatch(s -> s.equalsIgnoreCase(groepNaam))) {
            throw new IllegalArgumentException(isLeerGebied ? "Dit leergebied bestaat al." : "Deze doelgroep bestaat al.");
        }

        groepen.add(result);
        return result;
    }

    /**
     * Geeft de groep met de gegeven groepnaam.
     *
     * @param groepNaam de naam van de te zoeken groep
     * @param isLeerGebied of je een leergebied of doelgroep wilt vinden
     * @return De Optional groep die leeg is als geen groep gevonden werd.
     */
    public Optional<Groep> geefGroep(String groepNaam, boolean isLeerGebied) {
        Optional<Groep> groepOpt;
        List<Groep> inTeZoekenList = getInTeZoekenGroepen(isLeerGebied);

        groepOpt = inTeZoekenList.stream().filter(s -> s.getGroep().equalsIgnoreCase(groepNaam)).findFirst();

        return groepOpt;
    }

    /**
     * Verwijder de groep met de gegeve groepStr e isleergroep, maar alleen als
     * de groep niet meer in de materialen voorkomt.
     *
     * @param groepStr de naam van de groep
     * @param isLeerGroep of het een leergebied of doelgroep is
     * @param materialen alle materialen die niet de te verwijderen groep mag
     * bevatten
     * @return de verwijderde groep
     * @throws IllegalArgumentException indien groepsnaam niet bestaat of
     * ongeldig is of indien de groep nog voorkomt in de materialen lijst
     */
    public Groep verwijderGroep(String groepStr, boolean isLeerGroep, List<Materiaal> materialen) {
        if (groepStr == null || groepStr.isEmpty()) {
            throw new IllegalArgumentException("Je hebt geen " + (isLeerGroep ? "leergebied" : "doelgroep") + " geselecteerd.");
        }
        Optional<Groep> groepOpt = geefGroep(groepStr, isLeerGroep);

        if (!groepOpt.isPresent()) {
            throw new IllegalArgumentException(isLeerGroep ? "Het leergebied bestaat niet." : "Het doelgroep bestaat niet.");
        }
        Groep groep = groepOpt.get();
        String expMsg = isLeerGroep ? "Er is nog een materiaal met dit leergebied." : "Er is nog een materiaal met deze doelgroep.";
        for (Materiaal m : materialen) {
            List<Groep> teZoekenGroep = isLeerGroep ? m.getLeergebieden() : m.getDoelgroepen();
            if (teZoekenGroep.stream().anyMatch(g -> g.getId() == groep.getId())) {
                throw new IllegalArgumentException(expMsg);
            }
        }
        groepen.remove(groep);
        return groep;
    }

    private List<Groep> getInTeZoekenGroepen(boolean isLeerGroep) {
        return isLeerGroep ? getLeerGebieden() : getDoelGroepen();
    }
}
