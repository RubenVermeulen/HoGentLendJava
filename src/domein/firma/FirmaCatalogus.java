package domein.firma;

import domein.materiaal.Materiaal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import shared.MateriaalView;

public class FirmaCatalogus {

    private final List<Firma> firmas;

    public FirmaCatalogus(List<Firma> firmas) {
        this.firmas = firmas;
    }

    /**
     * Geeft alle firmas alfabetisch gesorteerd terug.
     *
     * @return De gesorteerde firmas.
     */
    public List<Firma> getAllFirmasSorted() {
        Comparator<Firma> comparator = (f1, f2) -> f1.getNaam().compareToIgnoreCase(f2.getNaam());
        Collections.sort(firmas, comparator);
        return firmas;
    }

    /**
     * Zal de firma terug geven met de geven naam.
     *
     * @param naam De naam van de te zoeken firma.
     * @return De optionele gevonden firma.
     */
    public Optional<Firma> geefFirma(String naam) {
        if (naam == null || naam.isEmpty()) {
            return Optional.empty();
        }
        for (Firma f : firmas) {
            if (f.getNaam().equals(naam)) {
                return Optional.of(f);
            }
        }
        return Optional.empty();
    }

    /**
     * Zal een nieuwe firma toevoegen met de gegeven naam en email.
     *
     * @param naam De naam van de toe te voegen firme.
     * @param email Het contact email adres van de toe te voegen firma.
     * @return De nieuwe toegevoegde firma.
     *
     * @throws IllegalArgumentException
     */
    public Firma voegFrimaToe(String naam, String email) {
        Optional<Firma> optFirma = geefFirma(naam);
        if (optFirma.isPresent()) {
            throw new IllegalArgumentException("Firma naam is al in gebruik");
        }
        Firma firma = new Firma(naam);
        firma.setEmail(email);

        firmas.add(firma);

        return firma;
    }

    /**
     * Haalt de firma met de gegeven <code>naam</code> op en controleert of het
     * nog ergens is gebruikt in een materiaal van <code>materialen</code>.
     * Alleen indien hij niet meer in een materiaal zit zal de firma verwijderd
     * worden.
     *
     * @param firmaNaam De naam van de te verwijderen firma.
     * @param materialen Alle materialen waarin de firma nog kan zitten.
     * @return De verwijderde firma.
     * @throws IllegalArgumentException *
     */
    public Firma verwijderFirma(String firmaNaam, List<MateriaalView> materialen) {
        Optional<Firma> optFirma = geefFirma(firmaNaam);
        if (!optFirma.isPresent()) {
            throw new IllegalArgumentException("De firma bestaat niet.");
        }
        Firma firma = optFirma.get();

        for (MateriaalView m : materialen) {
            if (m.getFirma().equals(firma.getNaam())) {
                throw new IllegalArgumentException("Er bestaat nog een materiaal met deze firma.");
            }
        }

        firmas.remove(firma);

        return firma;
    }

    /**
     * Wijzigt het firma object van de firma repository en wijzigt 
     * alle materialen hun firma object zodat deze overeen komen met 
     * de nieuwe waarden.
     * 
     * @param firma Het firma object dat zal worden gewijzigd
     * @param nieuweNaam De nieuwe naam voor de firma
     * @param nieuwEmailadres Het nieuwe e-mailadres voor de firma
     * @param materialen Alle materialen
     */
    public void wijzigFirmas(Firma firma, String nieuweNaam, String nieuwEmailadres, List<Materiaal> materialen) {
        // Wijzigt voor elk materiaal object de firma naam en eventueel het firme e-mailadres.
        for (Materiaal m : materialen) {
            if (m.getFirma().getNaam().equals(firma.getNaam())) {
                m.getFirma().setNaam(nieuweNaam);
                m.getFirma().setEmail(nieuwEmailadres);
            }    
        }
        
        // Firma object komt van firma repository,
        // deze wijzigigen moeten gebeuren om dat dan correct
        // weg te schrijven naar de database.
        firma.setNaam(nieuweNaam);
        firma.setEmail(nieuwEmailadres);
    }
}
