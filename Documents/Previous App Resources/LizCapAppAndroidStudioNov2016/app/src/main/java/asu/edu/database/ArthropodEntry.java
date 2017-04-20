package asu.edu.database;

public class ArthropodEntry {
	public final int MANT;
	public final int UNKI;
	public final int THYS;
	public final int SOLI;
	public final int SCOR;
	public final int PSEU;
	public final int ORTH;
	public final int LEPI;
	public final int HYMB;
	public final int HYMA;
	public final int HETE;
	public final int DIPT;
	public final int DIEL;
	public final int DERM;
	public final int CRUS;
	public final int COLE;
	public final int CHIL;
	public final int BLAT;
	public final int AUCH;
	public final int ARAN;
	
	public final String fenceTrap;
	public final String predator;
	public final String comment;
	
	public ArthropodEntry 
		(int MANT, int UNKI, int THYS, int SOLI, int SCOR, int PSEU, int ORTH, int LEPI, int HYMB,
		 int HYMA, int HETE, int DIPT, int DIEL, int DERM, int CRUS, int COLE, int CHIL, int BLAT,
		 int AUCH, int ARAN, String fenceTrap, String predator, String comment)
	{
		this.MANT = MANT;
		this.UNKI = UNKI;
		this.THYS = THYS;
		this.SOLI = SOLI;
		this.SCOR = SCOR;
		this.PSEU = PSEU;
		this.ORTH = ORTH;
		this.LEPI = LEPI;
		this.HYMB = HYMB;
		this.HYMA = HYMA;
		this.HETE = HETE;
		this.DIPT = DIPT;
		this.DIEL = DIEL;
		this.DERM = DERM;
		this.CRUS = CRUS;
		this.COLE = COLE;
		this.CHIL = CHIL;
		this.BLAT = BLAT;
		this.AUCH = AUCH;
		this.ARAN = ARAN;
		
		this.fenceTrap = fenceTrap;
		this.predator  = predator;
		this.comment   = comment;
	}
}
