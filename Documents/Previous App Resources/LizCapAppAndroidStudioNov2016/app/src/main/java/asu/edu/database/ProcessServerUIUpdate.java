package asu.edu.database;

import asu.edu.activity.Sync;


public class ProcessServerUIUpdate  implements Runnable {
	private Sync syncActivity;
	String errMsg;
	
	ProcessServerUIUpdate( String errMsg, Sync newActivity) {
		this.errMsg = errMsg;
		this.syncActivity = newActivity;
	}

	public void run() {
		syncActivity.finishedUpdate(errMsg);
	}
}
