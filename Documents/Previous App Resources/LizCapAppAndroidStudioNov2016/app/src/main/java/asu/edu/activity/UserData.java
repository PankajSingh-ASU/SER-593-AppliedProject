package asu.edu.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import asu.edu.LizardApplication;
import asu.edu.R;
import asu.edu.database.Checkdate;
import asu.edu.database.JsonDBAdapter;

public class UserData extends Activity {
	private EditText recorderText;
	private EditText handlerText;
	private Spinner siteSpinner;
	private Spinner theArraySpinner;
	private Button backButton;
	private Button nextButton;
	private CheckBox cb_noCaptures;
	boolean backHit = false;
	private JsonDBAdapter jsonDBHelper;
	ArrayList<String> siteName = new ArrayList<String>();
	ArrayList<String> arrayNumber = new ArrayList<String>();
	ArrayAdapter<String> arrayAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userdata);
		setTitle("Location");

		recorderText    = (EditText) findViewById(R.id.userDataRecorderText);
		handlerText     = (EditText) findViewById(R.id.userDataHandlerText);
		siteSpinner     = (Spinner)  findViewById(R.id.userDataSiteSpinner);
		theArraySpinner = (Spinner)  findViewById(R.id.userDataArraySpinner);
		backButton      = (Button)   findViewById(R.id.userDataBackButton);
		nextButton      = (Button)   findViewById(R.id.userDataNextButton);
		cb_noCaptures   = (CheckBox) findViewById(R.id.cb_userDataNoCapture);

		siteName = new ArrayList<String>();
		arrayNumber = new ArrayList<String>();
		siteName.add("Select");
		arrayNumber.add("Select");

		jsonDBHelper = new JsonDBAdapter(this);
		jsonDBHelper.open();

		Cursor querySite = jsonDBHelper.rawQuery("Select Distinct SiteName from locations order by SiteName asc", null);

		while (querySite.moveToNext()) {
			siteName.add(querySite.getString(0));
		}

		querySite.close();
		jsonDBHelper.close();

		ArrayAdapter<String> siteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, siteName);
		siteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		siteSpinner.setAdapter(siteAdapter);

		siteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String siteName = (String) siteSpinner.getSelectedItem();

				if(!siteName.equals("Select")){
					jsonDBHelper.open();
					ArrayList<String> arrayNumber = new ArrayList<String>();
					arrayNumber.add("Select");

					Cursor queryArray = jsonDBHelper.rawQuery("Select ArrayName from Locations where SiteName = '" + siteName + "' order by ArrayName asc", null);

					while(queryArray.moveToNext()){
						arrayNumber.add(queryArray.getString(0));
					}
					queryArray.close();

					jsonDBHelper.close();

					arrayAdapter.clear();

					for (String item : arrayNumber) {
						arrayAdapter.add(item);
					}

					arrayAdapter.setNotifyOnChange(true);
					theArraySpinner.setAdapter(arrayAdapter);
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {
				//toast message error check
			}

		});


		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, arrayNumber);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		theArraySpinner.setAdapter(arrayAdapter);

		backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(UserData.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Exit")
						.setMessage("The information you entered so far will be lost")
						.setNegativeButton("Cancel", null)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Exit the activity
								UserData.this.finish();
							}
						}).show();
			}
		});

		nextButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean resultOnError = errorCheck();
				if (resultOnError) {
					LizardApplication app = (LizardApplication) getApplicationContext();
					Checkdate checkdateobj = createCheckDate();
					app.setCheckdateObj(checkdateobj);

					String recorderName = recorderText.getText().toString();
					String newRecorder = recorderName.toUpperCase();
					String handlerName = handlerText.getText().toString();
					String newHandler = handlerName.toUpperCase();
					
					Intent intent;
					
					if (cb_noCaptures.isChecked()) {
						intent = new Intent(UserData.this, LocationComments.class);
					}
					else {
						intent = new Intent(UserData.this, TaxaSelection.class);
					}
					
					startActivity(intent);
				}
			}
		});
	}

	private Checkdate createCheckDate () {
		JsonDBAdapter db = new JsonDBAdapter(this);
		db.open();

		String checkID    = UUID.randomUUID().toString();
		String date       = DateFormat.format("MM/dd/yyyy", new Date()).toString();
		String time       = new SimpleDateFormat("kk:mm").format(new Date());	
		String locationID = getLocationID(db);
		String noCaptures = "";
		String handler    = handlerText.getText().toString().toUpperCase();
		String recorder   = recorderText.getText().toString().toUpperCase();
		String site       = siteSpinner.getSelectedItem().toString();
		String array      = theArraySpinner.getSelectedItem().toString();

		if (cb_noCaptures.isChecked()) {
			noCaptures = "No Captures";
		}

		db.close();
		
		return new Checkdate(checkID, handler, recorder, date, time, noCaptures, locationID, site, array);
	}

	private String getLocationID (JsonDBAdapter db) {
		String site  = siteSpinner.getSelectedItem().toString();
		String array = theArraySpinner.getSelectedItem().toString();

		Cursor result = db.rawQuery(
				"select * from locations where SiteName = '" + site
				+ "' and ArrayName = '" + array + "'", null);

		result.moveToLast();
		String locationID = result.getString(0);

		result.close();

		return locationID;
	}

	private boolean errorCheck() {
		boolean finalCheck = false;

		if (checkRecorder() && checkHandler() && checkSite() && checkArray())
			finalCheck = true;
		else
			finalCheck = false;

		return finalCheck;
	}

	private boolean checkArray() {
		String arrayCheck = (String) theArraySpinner.getSelectedItem();
		boolean arrayResult = false;

		if (arrayCheck.equals("Select")) {
			new AlertDialog.Builder(UserData.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Array can not be 'Select'. Please choose an option.")
					.setNegativeButton("Ok", null)
					.show();
		}
		else {
			arrayResult = true;
		}
		return arrayResult;
	}

	private boolean checkSite() {
		String siteCheck = (String) siteSpinner.getSelectedItem();
		boolean siteResult = false;

		if (siteCheck.equals("Select")) {
			new AlertDialog.Builder(UserData.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Site can not be 'Select'. Please choose an option.")
					.setNegativeButton("Ok", null)
					.show();
		}
		else {
			siteResult = true;
		}
		return siteResult;
	}

	private boolean checkHandler() {
		String errorhandler = handlerText.getText().toString().trim();
		boolean handlerResult = false;

		if (errorhandler.matches("[A-Za-z]{2,3}")) {
			handlerResult = true;
		}
		else {
			new AlertDialog.Builder(UserData.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Handler needs 2 or 3 Initials of a person's name\n\n Example: HLB or HB")
					.setNegativeButton("Ok", null)
					.show();
		}

		return handlerResult;
	}

	private boolean checkRecorder() {
		String errorrecorder = recorderText.getText().toString().trim();
		boolean recorderResult = false;

		if (errorrecorder.matches("[A-Za-z]{2,3}")) {
			recorderResult = true;
		}
		else {
			new AlertDialog.Builder(UserData.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Recorder needs 2 or 3 Initials of a person's name\n\n Example: HLB or HB")
					.setNegativeButton("Ok", null)
					.show();
		}

		return recorderResult;
	}
}
