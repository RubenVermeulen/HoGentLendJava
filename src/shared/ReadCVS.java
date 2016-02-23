/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alexa_000
 */
public class ReadCVS {
    
     public ArrayList<String[]> run(String CSVFile) {

	String csvFile = CSVFile;
	BufferedReader br = null;
	String line = "";
	String cvsSplitBy = ",";
        
        ArrayList <String[]> materialen=new ArrayList<String[]>();

	try {

		ArrayList<String[]> nieuweMaterialen = new ArrayList<String[]>();

		br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {

			// use comma as separator
			String[] materiaal = line.split(cvsSplitBy);

			nieuweMaterialen.add(materiaal);

		}

		materialen= nieuweMaterialen;
		

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
     return materialen;
     }
    
}
