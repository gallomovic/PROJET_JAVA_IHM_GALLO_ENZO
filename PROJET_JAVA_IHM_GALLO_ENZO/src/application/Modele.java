package application;



public class Modele {

	private boolean btnQuadri;
	private boolean btnHisto;
	private boolean btnStart;
	private boolean btnPause;
	private boolean btnStop;
	private boolean btnGraphique;
	
	/**
	 * Constructeur
	 * @param btnQuadri
	 * @param btnHisto
	 * @param btnStart
	 * @param btnPause
	 * @param btnStop
	 * @param btnGraphique
	 */
	public Modele(boolean btnQuadri, boolean btnHisto, 
					boolean btnStart, boolean btnPause, boolean btnStop, boolean btnGraphique ) {

		this.btnQuadri = btnQuadri;
		this.btnHisto = btnHisto;
		this.btnStart = btnStart;
		this.btnPause = btnPause;
		this.btnStop = btnStop;
		this.btnGraphique = btnGraphique;
	}
	

	//getters
	
	public boolean getbtnQuadri() {
		return this.btnQuadri;
	}
	
	public boolean getbtnHisto() {
		return this.btnHisto;
	}
	
	public boolean getbtnStart() {
		return this.btnStart;
	}
	
	public boolean getbtnPause() {
		return this.btnPause;
	}
	
	public boolean getbtnStop() {
		return this.btnStop;
	}
	
	public boolean getbtnGraphique() {
		return this.btnGraphique;
	}
	
	
	
	//setters
	
	public void setbtnQuadri(boolean btnQuadri) {
		this.btnQuadri = btnQuadri;
	}
	
	public void setbtnHisto(boolean btnHisto) {
		this.btnHisto = btnHisto;
	}
	
	public void setbtnStart(boolean btnStart) {
		this.btnStart = btnStart;
	}	
	
	public void setbtnPause(boolean btnPause) {
		this.btnPause = btnPause;
	}
	
	public void setbtnStop(boolean btnStop) {
		this.btnStop = btnStop;
	}
	
	public void setbtnGraphique(boolean btnGraphique) {
		this.btnGraphique = btnGraphique;
	}
	

	
}
