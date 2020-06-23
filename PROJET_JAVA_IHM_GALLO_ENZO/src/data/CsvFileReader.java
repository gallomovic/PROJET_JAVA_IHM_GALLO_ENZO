package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

import application.*;

public class CsvFileReader {

	
	
	
	public static void readCSV() {
		
		try {
			FileReader file = new FileReader("src/data/tempanomaly_4x4grid.csv");
			BufferedReader bufRead = new BufferedReader(file);

			String line = bufRead.readLine();
			line = bufRead.readLine();
			
			//initialisation manuelle de listeAnnees
			for (int i=1880; i<=2020; i++) {
				Earth.listeAnnees.add(i);
			}
			
			while ( line != null) {
			   	String[] array = line.split(","); //separateur
			   
			    int latitude = Integer.parseInt(array[0]);
			    int longitude = Integer.parseInt(array[1]);

			    Coordonnees c = new Coordonnees(latitude,longitude);

			    
			    for (int i=2; i<=142; i++) { //nombre d'années
			    	
			    	if(!array[i].equals("NA")) {
			    		
			    		c.listeAnneeAno.put(1878+i, Float.parseFloat(array[i]));
			    		
			    	}
			    	
			    	else {
			    		c.listeAnneeAno.put(1878+i, Float.NaN);
			    	}
			    	
			    }
			    
			    
			    Earth.listeZones.add(c);
			        		
			    line = bufRead.readLine();
			}

			bufRead.close();
			file.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}


	}	
		
}


	
	
