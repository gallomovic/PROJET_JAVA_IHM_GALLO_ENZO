package application;

import java.util.ArrayList;

import data.*;

public class Earth {
	
	public static ArrayList<Integer> listeAnnees = new ArrayList<Integer>(); //contient la liste des ann�es
	
	public static ArrayList<Coordonnees> listeZones = new ArrayList<Coordonnees>(); //contient les coordonn�es des 
																					//zones trait�es

	
	/**
	 * M�thode permettant de r�cup�rer la valeur minimale des anomalies
	 * @return min
	 */
	public static float valeurMinAno() {
		
		float min = 0;
		
		for (int i=1880; i<=2020 ; i++) {
			for(int j=0; j<listeZones.size(); j++) {
				
				
				if ((listeZones.get(j).listeAnneeAno.get(i)<min) && (!listeZones.get(j).listeAnneeAno.get(i).equals(Float.NaN))) {
					min = listeZones.get(j).listeAnneeAno.get(i);
				}			
				
			}
		}
		
		return min;
		
	}
	
	
	/**
	 * M�thode permettant de r�cup�rer la valeur maximale des anomalies
	 * @return min
	 */
	public static float valeurMaxAno() {
		float max = 0;
		
		for (int i=1880;i<=2020 ; i++) {
			for(int j=0; j<listeZones.size(); j++) {
				
				
				if ((listeZones.get(j).listeAnneeAno.get(i)>max) && (!listeZones.get(j).listeAnneeAno.get(i).equals(Float.NaN))) {
					max = listeZones.get(j).listeAnneeAno.get(i);
				}			
				
			}
		}
		
		return max;
		
	}
	
	/**
	 * M�thode qui renvoie le nombre total d'ann�es trait�es (faite pour le SimpleTest)
	 * @return 
	 */
	public static int yearNumber() {
		return listeAnnees.size();
	}
	
	
	/**
	 * M�thode qui permet de r�cup�rer l'anomalie correspondant � une zone (latitude,longitude) et 
	 * � une ann�e donn�e
	 * @param lat
	 * @param lon
	 * @param annee
	 * @return 
	 */
	public static float getAno(int lat, int lon, int annee) {

		for (int i=0; i<listeZones.size();i++) {
			if (listeZones.get(i).getLat() == lat && listeZones.get(i).getLongi() == lon) {
				return listeZones.get(i).listeAnneeAno.get(annee);
			}
		}
		
		return 0;
	}
	
	
	/**
	 * M�thode qui permet de r�cup�rer la liste des anomalies de toutes les zones pour une ann�e donn�e
	 * @param annee
	 * @return
	 */
	public static Float[] getAnoAnnee(int annee) {
		
		Float[] array = new Float[4050];

			for (int i=0 ; i<4050 ; i++) {
					array[i]=listeZones.get(i).listeAnneeAno.get(annee);
			}
		
		return array;
	}
	
	/**
	 * M�thode qui permet de r�cup�rer la liste des anomalies de toutes les ann�es pour une zone donn�e
	 * @param lat
	 * @param lon
	 * @return
	 */
	public static Float[] getAnoZones(int lat, int lon) {
		
		Float[] array = new Float[141];
		

		for(int i=0;i<141 ; i++) {
			for (int j=0; j<listeZones.size();j++) {
				if (listeZones.get(j).getLat() == lat && listeZones.get(j).getLongi() == lon) {
					array[i] = listeZones.get(j).listeAnneeAno.get(i+1880);
				}
			}
		}
		
		return array;
		
	}
	
	
	
	
	
	public static void main(String[] args) {
		
		
		CsvFileReader.readCSV();	
		
		
	}
	
}
