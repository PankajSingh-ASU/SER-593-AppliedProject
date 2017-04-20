package asu.edu;

import android.app.Application;
import android.content.Context;
import asu.edu.database.Checkdate;

public class LizardApplication extends Application {
	private static LizardApplication instance;
	private Checkdate currentCheckdate = null;
	
	public LizardApplication () {
		instance = this;
	}
	
	public static Context getContext () {
		return instance;
	}
	
	public void setCheckdateObj (Checkdate checkdate) {
		currentCheckdate = checkdate;
	}
	
	public Checkdate getCheckdateObj () {
		return currentCheckdate;
	}
}
