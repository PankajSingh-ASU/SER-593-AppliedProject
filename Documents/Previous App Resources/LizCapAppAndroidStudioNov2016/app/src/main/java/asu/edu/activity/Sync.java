package asu.edu.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import asu.edu.R;
import asu.edu.database.Area;
import asu.edu.database.JsonDBAdapter;
import asu.edu.database.SyncJsonAreas;
import asu.edu.database.SyncJsonDatabase;

public class Sync extends Activity {
	/** Called when the activity is first created. */
	private TextView lastUpdate;
	private Spinner areaNamesSpinner;
	private Spinner serverNamesSpinner;
	private Handler uiHandler;
	private ProgressDialog myProgressDialog;
	public String serverString;
	public String currentServer;
	public Area currentArea;
	ArrayList<String> serverNamesArray;
	ArrayList<String> serverAddresses;
	public HashMap<String, ArrayList<Area>> areaList;
	JsonDBAdapter myJsonDB;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync);
		myJsonDB = new JsonDBAdapter(this);
		setTitle("Update Database");
		uiHandler = new Handler();
		// declaring variables to my xml id's
		this.lastUpdate = (TextView) this.findViewById(R.id.syncLastUpdated);
		this.areaNamesSpinner = (Spinner) this.findViewById(R.id.spinner1);
		this.serverNamesSpinner = (Spinner) this.findViewById(R.id.spinner2);

		serverNamesArray = new ArrayList<String>();
		serverNamesArray.add("Live");
		serverNamesArray.add("Demo");
		//serverNamesArray.add("Justin Local");
		
		serverAddresses = new ArrayList<String>();
		serverAddresses.add("https://lizcapapp.poly.asu.edu/lizcap_live");
		serverAddresses.add("https://lizcapapp.poly.asu.edu/lizcap_demo");
		//serverAddresses.add("http://192.168.2.28:3000");
		
		areaList = new HashMap<String,  ArrayList<Area>>();
		
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, serverNamesArray);
		serverNamesSpinner.setAdapter(spinnerArrayAdapter);

		SharedPreferences settings = getSharedPreferences("LizPref", 0);
		currentServer = settings.getString("currentServer", "Never Synced");

		for (int i = 0; i< serverNamesArray.size(); i++){
			if (currentServer.compareTo(serverNamesArray.get(i)) == 0){
				serverNamesSpinner.setSelection(i);
				i = serverNamesArray.size();
			}
		}
		if (serverNamesArray.size()<=1){
			serverNamesSpinner.setVisibility(View.INVISIBLE);
		}
		
		updateIfSyncText();
		
		serverNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				loadAreaArray();
			}
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}
	
	/** TODO: Button Actions*/
	
	public void syncDatabaseAction(View view){
		myJsonDB.open();
		String selectedItem = (String)serverNamesSpinner.getSelectedItem();
		int rowSelection = serverNamesArray.indexOf(selectedItem);
		serverString = serverAddresses.get(rowSelection);
		ArrayList<Area> areas = areaList.get(selectedItem);
		String stringAreaName = (String)areaNamesSpinner.getSelectedItem();
		currentArea = null;
		if (areas != null){
			for (int i=0; i<areas.size(); i++){
				if (areas.get(i).areaName.compareTo(stringAreaName)==0){
					currentArea = areas.get(i);
					i = areas.size();
				}
			}
		}
		
		SharedPreferences settings = getSharedPreferences("LizPref", 0);
		String oldServer = settings.getString("currentServer", "Never Synced");
		currentServer = selectedItem;
		if (oldServer.compareTo(currentServer)!=0 && myJsonDB.anyToUpdate()){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(true);
			builder.setMessage("Warning data not yet updated to other database do you wish to continue and discard data");
			builder.setTitle("Warning");
			builder.setInverseBackgroundForced(true);
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					syncJson();
				}
			});
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		} else {
			syncJson();
		}
	}
	
	public void backAction(View view){
		finish();
	}

	/** TODO: Private Methods*/
	
	private void syncJson(){
		myProgressDialog = new ProgressDialog(this);
		myProgressDialog.setTitle("Loading...");
		myProgressDialog.setMessage("Updating Database");
		myProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		myProgressDialog.setMax(3);
		myProgressDialog.setProgress(0);
		myProgressDialog.setCancelable(false);
		myProgressDialog.show();
		SyncJsonDatabase syncJsonDB = new SyncJsonDatabase(myJsonDB, uiHandler, this);
		syncJsonDB.start();
	}

	private void updateIfSyncText(){
		SharedPreferences settings = getSharedPreferences("LizPref", 0);
		String date = settings.getString("lastupdate", null);
		currentServer = settings.getString("currentServer", "");
		String currentAreaName = settings.getString("areaname", "Never Synced");
		String listServerText;
		if (serverNamesArray.size()<=1){
			listServerText = "Last Updated With:\n" + currentAreaName;
		} else {
			listServerText = "Last Updated With:\n" + currentServer + "\n" + currentAreaName;
		}
		if (date != null){
			try {
				SimpleDateFormat curFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				curFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				Date myDate = curFormat.parse(date.trim());
				listServerText = listServerText + "\n\n" + "Last Updated:\n" + myDate.toLocaleString();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		lastUpdate.setText(listServerText);
	}
	
	private void loadAreaArray(){
		String selectedItem = (String)serverNamesSpinner.getSelectedItem();
		int rowSelection = serverNamesArray.indexOf(selectedItem);
		serverString = serverAddresses.get(rowSelection);
		ArrayList<Area> areas = areaList.get(selectedItem);
		if (areas == null){
			myJsonDB.open();
			myProgressDialog = ProgressDialog.show(this, "Loading Areas...", "Loading the Available Areas", true, false);
			SyncJsonAreas syncArea = new SyncJsonAreas(uiHandler, this, selectedItem);
			syncArea.start();
		} else {
			resetAreaSpinner(areas);
		}
	}
	
	private void resetAreaSpinner(ArrayList<Area> areaListing){
		ArrayList<String> areaNameList = new ArrayList<String>();
		if (areaListing != null){
			for (int i=0; i<areaListing.size(); i++){
				areaNameList.add(areaListing.get(i).areaName);
			}
		}
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, areaNameList);
		areaNamesSpinner.setAdapter(spinnerArrayAdapter);
		String selectedItem = (String)serverNamesSpinner.getSelectedItem();
		SharedPreferences settings = getSharedPreferences("LizPref", 0);
		currentServer = settings.getString("currentServer", "Never Synced");
		String areaCurrentString = settings.getString("areaid", "Empty");
		if (currentServer.compareTo(selectedItem)==0 && areaListing != null){
			for (int i=0; i<areaListing.size(); i++){
				if (areaListing.get(i).areaID.compareTo(areaCurrentString)==0){
					areaNamesSpinner.setSelection(i);
					i = areaListing.size();
				}
			}
		}
	}

	/** TODO: Public Methods*/
	
	public void updateProgressLevel(int level){
		myProgressDialog.setProgress(level);
	}
	
	public void setProgressSize(int size){
		myProgressDialog.setMax(size);
		myProgressDialog.setProgress(0);
	}
	
	public void finishedUpdate(String errMsg){
		myJsonDB.close();
		updateIfSyncText();
		String selectedItem = (String)serverNamesSpinner.getSelectedItem();
		int rowSelection = serverNamesArray.indexOf(selectedItem);
		serverString = serverAddresses.get(rowSelection);
		ArrayList<Area> areas = areaList.get(selectedItem);
		resetAreaSpinner(areas);
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				myProgressDialog.dismiss();
			}}, 2000);  // 2000 milliseconds
		
		if( errMsg != null ) {
			if (errMsg.compareTo("Areas")!=0){
				Toast.makeText( this, errMsg, Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText( this, "Finished", Toast.LENGTH_LONG).show();
		}

	}
}
