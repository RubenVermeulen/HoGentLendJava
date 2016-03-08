package domein.materiaal;

import com.opencsv.CSVReader;
import exceptions.BulkToevoegenMisluktException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.math.NumberUtils;
import shared.MateriaalView;

public class BulkToevoeger {

    private MateriaalRepository matRepo;
    private MateriaalCatalogus matCat;

    public BulkToevoeger(MateriaalRepository matRepo, MateriaalCatalogus matCat) {
        this.matRepo = matRepo;
        this.matCat = matCat;
    }

    public void voegMaterialenToeInBulk(String csvFile) throws BulkToevoegenMisluktException {
        ArrayList<String[]> materialen;
        materialen = readCsvFile(csvFile);

        for (String[] materiaal : materialen) {
            String naam = materiaal[1];

            String fotoUrl = materiaal[0];

            String omschrijving = materiaal[2];
            String artikelNummer = materiaal[3];
            double prijs = NumberUtils.toDouble(materiaal[4], 0);
            int aantal;

            aantal = NumberUtils.toInt(materiaal[5], 0);
            boolean uitleenbaarheid;
            if (!materiaal[6].isEmpty()) {
                uitleenbaarheid = Boolean.parseBoolean(materiaal[6]);
            } else {
                uitleenbaarheid = true;
            }

            String plaats = materiaal[7];
            String firma = materiaal[8];
            String emailFirma = materiaal[9];
            String doelgroepen = materiaal[10];
            String leergebieden = materiaal[11];

            MateriaalView matView = new MateriaalView(naam, aantal);

            matView.setAantalOnbeschikbaar(0);   //zelf ingevuld
            matView.setArtikelNummer(artikelNummer);

            List<String> doelGroepkes = new ArrayList<>(Arrays.asList(doelgroepen.split(",")));
            List<String> leerGroepkes = new ArrayList<>(Arrays.asList(leergebieden.split(",")));
            matView.setDoelgroepen(doelGroepkes);

            matView.setEmailFirma(emailFirma);

            try {
                if (!firma.isEmpty()) {
                    matCat.geefFirmaRepo().voegFirmaToe(firma, emailFirma);
                }
            } catch (IllegalArgumentException e) {
                // ignore
            }

            if (!leergebieden.isEmpty()) {
                for (String leergroep : leerGroepkes) {
                    try {
                        matCat.voegGroepToe(leergroep, true);
                    } catch (IllegalArgumentException e) {
                        // ignore
                    }
                }

            }
            if (!doelgroepen.isEmpty()) {
                for (String doelgroep : doelGroepkes) {
                    try {
                        matCat.voegGroepToe(doelgroep, false);
                    } catch (IllegalArgumentException e) {
                        // ignore
                    }
                }
            }

            matView.setFirma(firma);
            matView.setFotoUrl(fotoUrl);
            matView.setLeergebieden(leerGroepkes);
            matView.setOmschrijving(omschrijving);
            matView.setPlaats(plaats);
            matView.setPrijs(prijs);
            matView.setUitleenbaarheid(uitleenbaarheid);

            matRepo.voegMateriaalToe(matView);
        }
    }

    public ArrayList<String[]> readCsvFile(String CSVFile) throws BulkToevoegenMisluktException {
        ArrayList<String[]> materialen = new ArrayList<>();

        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(CSVFile), ';', '\"', 1);
        } catch (FileNotFoundException ex) {
            throw new BulkToevoegenMisluktException("Het bestand kon niet gevonden worden.");
        }
        String[] nextLine;
        try {
            while ((nextLine = reader.readNext()) != null) {
                materialen.add(nextLine);
                for (int i = 0; i < nextLine.length; i++) {

                    System.out.println(nextLine[i]);

                }

            }
        } catch (IOException ex) {
            throw new BulkToevoegenMisluktException("Er liep iets fout tijdens het lezen van het bestand.");
        }

        return materialen;
    }

}
