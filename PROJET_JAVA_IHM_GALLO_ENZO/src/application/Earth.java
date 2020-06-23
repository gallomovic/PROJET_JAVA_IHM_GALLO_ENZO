package application;

import java.util.ArrayList;

import data.*;

public class Earth {
	
	public static ArrayList<Integer> listeAnnees = new ArrayList<Integer>(); //contient la liste des années
	
	public static ArrayList<Coordonnees> listeZones = new ArrayList<Coordonnees>(); //contient les coordonnées des 
																					//zones traitées

	
	/**
	 * Méthode permettant de récupérer la valeur minimale des anomalies
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
	 * Méthode permettant de récupérer la valeur maximale des anomalies
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
	 * Méthode qui renvoie le nombre total d'années traitées (faite pour le SimpleTest)
	 * @return 
	 */
	public static int yearNumber() {
		return listeAnnees.size();
	}
	
	
	/**
	 * Méthode qui permet de récupérer l'anomalie correspondant à une zone (latitude,longitude) et 
	 * à une année donnée
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
	 * Méthode qui permet de récupérer la liste des anomalies de toutes les zones pour une année donnée
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
	 * Méthode qui permet de récupérer la liste des anomalies de toutes les années pour une zone donnée
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
