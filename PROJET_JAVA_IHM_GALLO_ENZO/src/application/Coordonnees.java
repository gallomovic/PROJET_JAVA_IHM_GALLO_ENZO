package application;

import java.util.HashMap;

public class Coordonnees {

	private int latitude;
	private int longitude;
	
	public HashMap<Integer, Float> listeAnneeAno = new HashMap<Integer, Float>(); //hashmap contenant les 
																				 //anomalies avec comme clé l'année
	
	
	
	/**
	 * Constructeur
	 * @param latitude
	 * @param longitude
	 */
	public Coordonnees (int latitude, int longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.listeAnneeAno = new HashMap<Integer, Float>();
	}
	
	/**
	 * Méthode qui permet de vérifier l'égalité entre 2 coordonnées
	 * @param c1
	 * @param c2
	 * @return
	 */
	public boolean equals(Coordonnees c1, Coordonnees c2) {
		if( (c1.latitude == c2.latitude)&&(c1.longitude == c2.longitude) ) {
			return true;
		}
		else {return false;}
	}
	
	/**
	 * getter latitude
	 * @return
	 */
	public int getLat() {
		return this.latitude;
	}
	
	/**
	 * getter longitude
	 * @return
	 */
	public int getLongi() {
		return this.longitude;
	}
	
	/**
	 * setter latitude
	 * @param lat
	 */
	public void setLat(int lat) {
		this.latitude = lat;
	}
	
	/**
	 * setter longitude
	 * @param lon
	 */
	public void setLong(int lon) {
		this.longitude = lon;
	}
}


	
