package asu.edu.database;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import asu.edu.LizardApplication;

public class Checkdate implements Serializable {
	private static final long serialVersionUID = -8564072554670531098L;
	
	public final String checkID;
	public final String handler;
	public final String recorder;
	public final String date;
	public final String time;
	public final String noCaptures;
	public final String locationID;
	public final String site;
	public final String array;
	
	private final ArrayList<ArthropodEntry> arthropodList;
	private final ArrayList<HerpEntry> herpList;
	
	private boolean checkdateFlushed = false;
	
	public Checkdate (String checkID, String handler, String recorder, String date, String time, String noCaptures, String locationID, String site, String array) {
		this.checkID    = checkID;
		this.handler    = handler;
		this.recorder   = recorder;
		this.date       = date;
		this.time       = time;
		this.noCaptures = noCaptures;
		this.locationID = locationID;
		this.site       = site;
		this.array      = array;
		
		arthropodList = new ArrayList<ArthropodEntry>();
		herpList      = new ArrayList<HerpEntry>();
	}
	
	public void addArthropodEntry (ArthropodEntry arthropod) {
		arthropodList.add(arthropod);
	}
	
	public void addHerpEntry (HerpEntry herp) {
		herpList.add(herp);
	}

	public void updateCheckdateEntryWithLocationInformation (String trapState, String locationComments) {
		JsonDBAdapter db = new JsonDBAdapter(LizardApplication.getContext());
		db.open();
		
		if (checkdateFlushed) {
			db.updateCheckdateLocationInfo(checkID, trapState, locationComments);
		}
		else {
			db.createCheckdates(checkID, handler, recorder,	date, time, trapState, noCaptures, locationComments, 1,	locationID);
			checkdateFlushed = true;
		}
		
		db.close();
	}
	
	public void flushObjectsToDB () {		
		JsonDBAdapter db = new JsonDBAdapter(LizardApplication.getContext());
		db.open();
		
		if (!checkdateFlushed) {
			db.createCheckdates(checkID, handler, recorder,	date, time, null, noCaptures, null, 1,	locationID);
			checkdateFlushed = true;
		}
		
		for (ArthropodEntry arthropod : arthropodList) {
			db.createAnthropodCapture(
					arthropod.MANT, arthropod.UNKI, arthropod.THYS, arthropod.SOLI, arthropod.SCOR, arthropod.PSEU,
					arthropod.ORTH, arthropod.LEPI, arthropod.HYMB, arthropod.HYMA, arthropod.HETE, arthropod.DIPT,
					arthropod.DIEL, arthropod.DERM, arthropod.CRUS, arthropod.COLE, arthropod.CHIL, arthropod.BLAT,
					arthropod.AUCH, arthropod.ARAN, 1, checkID, arthropod.fenceTrap, arthropod.predator, arthropod.comment, null);
		}
		
		for (HerpEntry herp : herpList) {
			db.createHerpcapture(
					null, herp.fenceTrap, herp.recapture, herp.SVL, herp.VTL, herp.mass, herp.hdBody, herp.sex, herp.OTL,
					herp.dead, herp.hatchling, null, herp.comments, checkID, herp.herpId, herp.identifiers, 1);
		}
		
		db.close();
		
		arthropodList.clear();
		herpList.clear();
	}
}
