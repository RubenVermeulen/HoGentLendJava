package domein;

import java.util.List;

public class FirmaRepository {

    List<Firma> firmas;

    public FirmaRepository() {
    }

    public List<Firma> getFirmas() {
        return firmas;
    }

    public Firma geefFirma(String naam) {

        for (Firma f : firmas) {
            if (f.getNaam().equals(naam)) {
                return f;
            }
        }

        return null;
    }

    //maakt nieuwe firma aan en returnt hem
    public Firma voegFirmaToe(String naam, String email) {

        Firma firma = new Firma(naam, email);
        firmas.add(firma);
        return firma;

    }

}
