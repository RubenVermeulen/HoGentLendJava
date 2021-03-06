package domein.materiaal;

import com.opencsv.CSVReader;
import exceptions.BulkToevoegenMisluktException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
            String naam = materiaal[0];

            String omschrijving = materiaal[1];
            String artikelNummer = materiaal[2];
            double prijs = NumberUtils.toDouble(materiaal[3], 0);
            int aantal;

            aantal = NumberUtils.toInt(materiaal[4], 0);
            boolean uitleenbaarheid;
            if (!materiaal[6].isEmpty()) {
                uitleenbaarheid = Boolean.parseBoolean(materiaal[5]);
            } else {
                uitleenbaarheid = true;
            }

            String plaats = materiaal[6];
            String firma = materiaal[7];
            String emailFirma = materiaal[8];
            String doelgroepen = materiaal[9];
            String leergebieden = materiaal[10];

            MateriaalView matView = new MateriaalView(naam, aantal);

            matView.setAantalOnbeschikbaar(0);   //zelf ingevuld
            matView.setArtikelNummer(artikelNummer);

            List<String> doelGroepkes = new ArrayList<>(Arrays.asList(doelgroepen.split(",")));
            List<String> leerGroepkes = new ArrayList<>(Arrays.asList(leergebieden.split(",")));
            matView.setDoelgroepen(doelGroepkes);

            matView.setEmailFirma(emailFirma);

            try {
                if (!firma.isEmpty()) {
                    matCat.voegFirmaToe(firma, emailFirma);
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
