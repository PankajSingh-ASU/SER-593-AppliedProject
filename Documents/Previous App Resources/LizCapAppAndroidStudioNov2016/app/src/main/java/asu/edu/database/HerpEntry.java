package asu.edu.database;

public class HerpEntry {
	public final String fenceTrap;
	public final String recapture;
	public final int SVL;
	public final int VTL;
	public final int OTL;
	public final float mass;
	public final int hdBody;
	public final String sex;
	public final String dead;
	public final String hatchling;
	public final String comments;
	public final String herpId;
	public final String identifiers;
	
	// Used by VerifyAmphibian, Verify Mammal
	public HerpEntry (String fenceTrap, float mass, int hdBody, String sex, String dead, String comments, String herpId) {
		this.fenceTrap = fenceTrap;
		this.mass      = mass;
		this.hdBody    = hdBody;
		this.sex       = sex;
		this.dead      = dead;
		this.comments  = comments;
		this.herpId    = herpId;
		
		this.SVL         = 0;
		this.VTL         = 0;
		this.OTL         = 0;
		this.recapture   = null;
		this.identifiers = null;
		this.hatchling   = null;
	}
	
	// Used by VerifyHerp, VerifySnake
	public HerpEntry (String fenceTrap, String recapture, int SVL, int VTL, int OTL, float mass, String sex,
					  String dead, String hatchling, String comments, String herpId, String toeClipCode)
	{
		this.fenceTrap = fenceTrap;
		this.recapture = recapture;
		this.SVL       = SVL;
		this.VTL       = VTL;
		this.OTL       = OTL;
		this.mass      = mass;
		this.sex       = sex;
		this.dead      = dead;
		this.hatchling = hatchling;
		this.comments  = comments;
		this.herpId    = herpId;
		identifiers    = toeClipCode;
		
		hdBody = 0;
	}
}
