package asu.edu.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import asu.edu.database.JsonDBAdapter;

public class CheckdatesHistory extends Activity {
	private TableLayout tl_main;
	private JsonDBAdapter db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Unsynced Checkdates");
		
		tl_main = new TableLayout(this);
		tl_main.setPadding(10, 0, 10, 0);
		tl_main.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		tl_main.setStretchAllColumns(true);
		setContentView(tl_main);
		
		db = new JsonDBAdapter(this);
		db.open();
		addUnsyncedCheckdates();
		db.close();
		
		int numOfCheckdateRows = tl_main.getChildCount();
		if (numOfCheckdateRows == 0) {
			TextView tv = new TextView(this);
			tv.setText("There are no unsynched changes");
			tl_main.addView(tv);
		}
	}
	
	private void addUnsyncedCheckdates () {
		Cursor cursor = db.fetchAllCheckdates();
		
		// Find the column indexes that we will be using
		int changedIndex  = cursor.getColumnIndexOrThrow(JsonDBAdapter.CHANGED_NUM);
		int recorderIndex = cursor.getColumnIndexOrThrow(JsonDBAdapter.RECORDER_TEXT);
		int handlerIndex  = cursor.getColumnIndexOrThrow(JsonDBAdapter.HANDLER_TEXT);
		int dateIndex     = cursor.getColumnIndexOrThrow(JsonDBAdapter.DATE_TEXT);
		int timeIndex     = cursor.getColumnIndexOrThrow(JsonDBAdapter.TIME_TEXT);
		int closedIndex   = cursor.getColumnIndexOrThrow(JsonDBAdapter.CLOSED_TEXT);
		int locationIndex = cursor.getColumnIndexOrThrow(JsonDBAdapter.LOCATIONID_TEXT);
		int checkIdIndex  = cursor.getColumnIndexOrThrow(JsonDBAdapter.CHECKDATEID_TEXT);
		
		// Iterate through all checkdate rows
		for (int i=0; i<cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			
			// Check if the row is unsynced
			if (cursor.getInt(changedIndex) > 0) {
				// Get the name of the site and array
				String locationId = cursor.getString(locationIndex);
				String[] siteAndArray = retrieveLocationInformation(locationId);
				String site  = siteAndArray[0];
				String array = siteAndArray[1];
				
				String recorder = cursor.getString(recorderIndex);
				String handler  = cursor.getString(handlerIndex);
				String date     = cursor.getString(dateIndex);
				String time     = cursor.getString(timeIndex);
				String closed   = cursor.getString(closedIndex);
				String checkId  = cursor.getString(checkIdIndex);
				
				// Add the checkdate row
				TableRow cdRow = createCheckdatesRow(recorder, handler, site, array, date, time, closed);
				cdRow.setPadding(0, 10, 0, 10);
				cdRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				tl_main.addView(cdRow);
				
				cdRow.setOnClickListener(new CheckdateOnClickListener(checkId, site, array));
				
				// Create a horizontal ruler
				View ruler = new View(this);
				ruler.setBackgroundColor(0xFFAAAAAA);
				ruler.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 2));
				tl_main.addView(ruler);
			}
		}
		
		cursor.close();
	}

	/**
	 * Returns the site and array based on the locationId
	 * @return String[0] => site, String[1] => array
	 */
	private String[] retrieveLocationInformation (String locationId) {
		String[] siteAndArray = new String[2];
		siteAndArray[0] = "Unknown Site";
		siteAndArray[1] = "Unknown Array";
		
		Cursor cursor = db.fetchAllLocations();
		
		int siteIndex       = cursor.getColumnIndexOrThrow(JsonDBAdapter.SITENAME_TEXT);
		int arrayIndex      = cursor.getColumnIndexOrThrow(JsonDBAdapter.ARRAYNAME_TEXT);
		int locationIdIndex = cursor.getColumnIndexOrThrow(JsonDBAdapter.LOCATIONID_NUM);
		
		// Iterate through all location rows
		for (int i=0; i<cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			
			// Check if this is the location we are looking for
			String currentLocationId = cursor.getString(locationIdIndex);
			if (currentLocationId.equals(locationId)) {
				siteAndArray[0] = cursor.getString(siteIndex);
				siteAndArray[1] = cursor.getString(arrayIndex);
			}
		}
		
		cursor.close();
		
		return siteAndArray;
	}
	
	private TableRow createCheckdatesRow (String recorder, String handler, String site, String array, String date, String time, String closed) {
		TableLayout tl = new TableLayout(this);
		tl.setStretchAllColumns(true);
		
		TableRow tr1 = new TableRow(this);
		TableRow tr2 = new TableRow(this);
		TableRow tr3 = new TableRow(this);
		TableRow tr4 = new TableRow(this);
		
		TextView tv_recorder = new TextView(this);
		TextView tv_handler  = new TextView(this);
		TextView tv_site     = new TextView(this);
		TextView tv_array    = new TextView(this);
		TextView tv_date     = new TextView(this);
		TextView tv_time     = new TextView(this);
		TextView tv_closed   = new TextView(this);
		
		tv_recorder.setText("Recorder: " + recorder);
		tv_handler.setText("Handler: " + handler);
		tv_site.setText("Site: " + site);
		tv_array.setText("Array: " + array);
		tv_date.setText("Date: " + date);
		tv_time.setText("Time: " + time);
		tv_closed.setText("Closed: " + closed);
		
		tr1.addView(tv_recorder);
		tr1.addView(tv_handler);
		tr2.addView(tv_site);
		tr2.addView(tv_array);
		tr3.addView(tv_date);
		tr3.addView(tv_time);
		tr4.addView(tv_closed);
		
		tl.addView(tr1);
		tl.addView(tr2);
		tl.addView(tr3);
		tl.addView(tr4);
		
		TableRow tr_checkdate = new TableRow(this);
		tr_checkdate.addView(tl);
		
		return tr_checkdate;
	}
	
	private class CheckdateOnClickListener implements OnClickListener {
		private final String checkId;
		private final String site;
		private final String array;
		
		public CheckdateOnClickListener (String checkId, String site, String array) {
			this.checkId = checkId;
			this.site    = site;
			this.array   = array;
		}
		
		public void onClick(View v) {
			Intent intent = new Intent(CheckdatesHistory.this, AnimalHistory.class);
			intent.putExtra("checkId", checkId);
			intent.putExtra("site", site);
			intent.putExtra("array", array);
			startActivity(intent);
		}
	}
}
