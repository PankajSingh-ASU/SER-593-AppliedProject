package asu.edu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import asu.edu.LizardApplication;
import asu.edu.R;
import asu.edu.database.Checkdate;
import asu.edu.database.JsonDBAdapter;

public class ToeClipCode extends Activity {

	private Button nextButton;
	private Button backButton;
	private Button clearButton;
	private Spinner aSpinner;
	private Spinner bSpinner;
	private Spinner cSpinner;
	private Spinner dSpinner;
	private TextView codeText;
	private TextView checkText;
	private TextView suggestText;
	private TextView suggestLabel;
	private TextView recapLabel;
	private String species;
	private String recap;
	private Intent intent;
	private ArrayList<String> aList = new ArrayList<String>();
	private ArrayList<String> bList = new ArrayList<String>();
	private ArrayList<String> cList = new ArrayList<String>();
	private ArrayList<String> dList = new ArrayList<String>();
	private ArrayList<String> usedCodeList = new ArrayList<String>();
	private ArrayList<String> suggestCodeList = new ArrayList<String>();
	private boolean A1;
	private boolean A2;
	private boolean A3;
	private boolean A4;
	private boolean A5;
	private boolean B1;
	private boolean B2;
	private boolean B3;
	private boolean B4;
	private boolean B5;
	private boolean C1;
	private boolean C2;
	private boolean C3;
	private boolean C4;
	private boolean C5;
	private boolean D1;
	private boolean D2;
	private boolean D3;
	private boolean D4;
	private boolean D5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		fillArrays();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.toeclip);
		setTitle("Toe-Clip Code");

		intent = getIntent();
		species = intent.getStringExtra("Species");
		recap = intent.getStringExtra("Recap");
		
		clearCodeFlags();

		this.nextButton = (Button) this.findViewById(R.id.toeclipNextButton);
		this.backButton = (Button) this.findViewById(R.id.toeclipBackButton);
		this.clearButton = (Button) this.findViewById(R.id.toeclipClearButton);
		this.aSpinner = (Spinner) this.findViewById(R.id.toeclipAFenceSpinner);
		this.bSpinner = (Spinner) this.findViewById(R.id.toeclipBFenceSpinner);
		this.cSpinner = (Spinner) this.findViewById(R.id.toeclipCFenceSpinner);
		this.dSpinner = (Spinner) this.findViewById(R.id.toeclipDFenceSpinner);
		this.codeText = (TextView) this.findViewById(R.id.toeclipinputText);
		this.checkText = (TextView) this.findViewById(R.id.toeclipCheckText);
		this.suggestText = (TextView) this.findViewById(R.id.toeclipSuggestionText);
		this.suggestLabel = (TextView) this.findViewById(R.id.toeclipSuggestion);
		this.recapLabel = (TextView) this.findViewById(R.id.toeclipRecap);

		JsonDBAdapter jsonDBHelper = new JsonDBAdapter(this);
		jsonDBHelper.open();
		
		LizardApplication app = (LizardApplication) getApplicationContext();
		Checkdate checkdateobj = app.getCheckdateObj();
		
		Cursor queryUsedClips = jsonDBHelper.rawQuery("Select Identifiers From (Locations INNER JOIN CheckDates ON [LocationID] = [Location_LocationID]) INNER JOIN (Herps INNER JOIN HerpCapture ON [HerpID] = [Herps_HerpID]) ON [CheckDateID] = [CheckDates_CheckdateID] Where SiteName = '" + checkdateobj.site + "' AND Spp_Code = '" + species + "' AND Identifiers IS NOT NULL AND Identifiers <> ''", null);

		while (queryUsedClips.moveToNext()) {
			usedCodeList.add(queryUsedClips.getString(0));			
		}
		queryUsedClips.close();

		jsonDBHelper.close();

		ArrayAdapter<String> alistAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, aList);
		alistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		aSpinner.setAdapter(alistAdapter);

		ArrayAdapter<String> blistAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, bList);
		blistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bSpinner.setAdapter(blistAdapter);	

		ArrayAdapter<String> clistAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, cList);
		clistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cSpinner.setAdapter(clistAdapter);	

		ArrayAdapter<String> dlistAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dList);
		dlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dSpinner.setAdapter(dlistAdapter);

		if(recap.equals("Yes")){
			suggestLabel.setVisibility(View.GONE);
			suggestText.setVisibility(View.GONE);
			recapLabel.setVisibility(View.VISIBLE);
		}else{
			String suggestValue = suggestion();
			suggestLabel.setVisibility(View.VISIBLE);
			suggestText.setVisibility(View.VISIBLE);
			recapLabel.setVisibility(View.GONE);
			suggestText.setText(suggestValue);		
		}


		this.nextButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {				
				if(recap.equals("Yes")){
					String newCode = checkRecap();
					String checkForVerify = checkText.getText().toString();

					if(!checkForVerify.equals("")){
						if(checkForVerify.equals("Available")){
							intent.putExtra("returnedData", newCode);
							setResult(RESULT_OK, intent);
							finish();
						}else{
							new AlertDialog.Builder(ToeClipCode.this).setIcon(
									android.R.drawable.ic_dialog_alert).setTitle("Error")
									.setMessage("Toe Clip Code must be 'Available' in order to continue on.")
									.setNegativeButton("Ok", null)
									.setNeutralButton("Override", new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface dialog, int whichButton) {
											String newCode = codeText.getText().toString();
											intent.putExtra("returnedData", newCode);
											setResult(RESULT_OK, intent);
											finish();
										}

									})
									.show();
						}
					}else{
						new AlertDialog.Builder(ToeClipCode.this).setIcon(
								android.R.drawable.ic_dialog_alert).setTitle("Error")
								.setMessage("A ToeClip Code can not be blank.")
								.setNegativeButton("Ok", null)
								.show();
					}
				}

				if(recap.equals("No")){
					String checkForVerify = checkText.getText().toString();

					if(!checkForVerify.equals("")){
						String newCode = splitString();
						if(checkForVerify.equals("Available")){
							intent.putExtra("returnedData", newCode);
							setResult(RESULT_OK, intent);
							finish();
						}else{
							new AlertDialog.Builder(ToeClipCode.this).setIcon(
									android.R.drawable.ic_dialog_alert).setTitle("Error")
									.setMessage("Toe Clip Code must be 'Available' in order to continue on.\n Try a new Toe Clip Code.")
									.setNegativeButton("Ok", null)
									.setNeutralButton("Override", new DialogInterface.OnClickListener() {

										public void onClick(DialogInterface dialog, int whichButton) {
											String newCode = codeText.getText().toString();
											intent.putExtra("returnedData", newCode);
											setResult(RESULT_OK, intent);
											finish();
										}

									})
									.show();
						}
					}else{
						new AlertDialog.Builder(ToeClipCode.this).setIcon(
								android.R.drawable.ic_dialog_alert).setTitle("Error")
								.setMessage("A ToeClip Code can not be blank.")
								.setNegativeButton("Ok", null)
								.show();
					}
				}

			}
		});

		this.backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		this.clearButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				codeText.setText("");
				aSpinner.setSelection(0);
				bSpinner.setSelection(0);
				cSpinner.setSelection(0);
				dSpinner.setSelection(0);
				checkText.setText("");
				clearCodeFlags();
			}
		});

		aSpinner.setOnItemSelectedListener(new OnToeClipSelectedListener());
		bSpinner.setOnItemSelectedListener(new OnToeClipSelectedListener());
		cSpinner.setOnItemSelectedListener(new OnToeClipSelectedListener());
		dSpinner.setOnItemSelectedListener(new OnToeClipSelectedListener());

		checkText.setText("");	

		Handler handler = new Handler(); 
		handler.postDelayed(new Runnable() { 
			public void run() { 
				String suggestValue = suggestion();
				codeText.setText(suggestValue);
				String cCodetoAvail =  codeText.getText().toString();

				if((!cCodetoAvail.equals("")) && usedCodeList.contains(cCodetoAvail)){
					checkText.setText("Not Available");
				}
				else if(!cCodetoAvail.equals("") && !usedCodeList.contains(cCodetoAvail)){
					checkText.setText("Available");
				}

			} 
		}, 1000);
	}

	class OnToeClipSelectedListener implements OnItemSelectedListener {
		@SuppressWarnings("rawtypes")
		public void onItemSelected(AdapterView parentView, View childView, int position, long id) {
			updateToeClipCode();
		}

		@SuppressWarnings("rawtypes")
		public void onNothingSelected(AdapterView parentView) {

		}
	}

	protected void updateToeClipCode() {
		String a = (String) aSpinner.getSelectedItem();
		String b = (String) bSpinner.getSelectedItem();
		String c = (String) cSpinner.getSelectedItem();
		String d = (String) dSpinner.getSelectedItem();
		
		if (a.equals("A1")) A1 = true;
		if (a.equals("A2")) A2 = true;
		if (a.equals("A3")) A3 = true;
		if (a.equals("A4")) A4 = true;
		if (a.equals("A5")) A5 = true;
		
		if (b.equals("B1")) B1 = true;
		if (b.equals("B2")) B2 = true;
		if (b.equals("B3")) B3 = true;
		if (b.equals("B4")) B4 = true;
		if (b.equals("B5")) B5 = true;
		
		if (c.equals("C1")) C1 = true;
		if (c.equals("C2")) C2 = true;
		if (c.equals("C3")) C3 = true;
		if (c.equals("C4")) C4 = true;
		if (c.equals("C5")) C5 = true;
		
		if (d.equals("D1")) D1 = true;
		if (d.equals("D2")) D2 = true;
		if (d.equals("D3")) D3 = true;
		if (d.equals("D4")) D4 = true;
		if (d.equals("D5")) D5 = true;
		
		String code = assembleToeCode();
		
		if (usedCodeList.contains(code) || code.equals("")) {
			checkText.setText("Not Available");
		}
		else {
			checkText.setText("Available");
		}
		
		codeText.setText(code);
	}
	
	private String assembleToeCode () {
		StringBuilder sb = new StringBuilder();
		
		if (A1) sb.append("A1");
		if (A2) sb.append("A2");
		if (A3) sb.append("A3");
		if (A4) sb.append("A4");
		if (A5) sb.append("A5");

		if (B1) sb.append("B1");
		if (B2) sb.append("B2");
		if (B3) sb.append("B3");
		if (B4) sb.append("B4");
		if (B5) sb.append("B5");

		if (C1) sb.append("C1");
		if (C2) sb.append("C2");
		if (C3) sb.append("C3");
		if (C4) sb.append("C4");
		if (C5) sb.append("C5");

		if (D1) sb.append("D1");
		if (D2) sb.append("D2");
		if (D3) sb.append("D3");
		if (D4) sb.append("D4");
		if (D5) sb.append("D5");
		
		return sb.toString();
	}
	
	private void clearCodeFlags () {
		A1 = A2 = A3 = A4 = A5 = false;
		B1 = B2 = B3 = B4 = B5 = false;
		C1 = C2 = C3 = C4 = C5 = false;
		D1 = D2 = D3 = D4 = D5 = false;
	}
		
	private String suggestion(){
		String suggestion = "";
		for (String suggest : suggestCodeList) {
			if(!usedCodeList.contains(suggest)){
				suggestion = suggest;
				break;
			}
		}
		return suggestion;
	}

	private void fillArrays() {
		aList.add("Select");
		aList.add("A1");
		aList.add("A2");
		aList.add("A3");
		aList.add("A4");
		aList.add("A5");

		bList.add("Select");
		bList.add("B1");
		bList.add("B2");
		bList.add("B3");
		bList.add("B4");
		bList.add("B5");

		cList.add("Select");
		cList.add("C1");
		cList.add("C2");
		cList.add("C3");
		cList.add("C4");
		cList.add("C5");

		dList.add("Select");
		dList.add("D1");
		dList.add("D2");
		dList.add("D3");
		dList.add("D4");
		dList.add("D5");

		suggestCodeList.add("A1B1");
		suggestCodeList.add("A1B2");
		suggestCodeList.add("A1B3");
		suggestCodeList.add("A1B4");
		suggestCodeList.add("A1B5");
		suggestCodeList.add("A1C1");
		suggestCodeList.add("A1C2");
		suggestCodeList.add("A1C3");		
		suggestCodeList.add("A1C5");
		suggestCodeList.add("A1D1");
		suggestCodeList.add("A1D2");
		suggestCodeList.add("A1D3");
		suggestCodeList.add("A1D5");
		suggestCodeList.add("A1D1");
		suggestCodeList.add("A2B1");
		suggestCodeList.add("A2B2");
		suggestCodeList.add("A2B3");
		suggestCodeList.add("A2B4");
		suggestCodeList.add("A2B5");
		suggestCodeList.add("A2C1");
		suggestCodeList.add("A2C2");
		suggestCodeList.add("A2C3");
		suggestCodeList.add("A2C5");
		suggestCodeList.add("A2D1");
		suggestCodeList.add("A2D2");
		suggestCodeList.add("A2D3");
		suggestCodeList.add("A2D5");
		suggestCodeList.add("A3B1");
		suggestCodeList.add("A3B2");
		suggestCodeList.add("A3B3");
		suggestCodeList.add("A3B4");
		suggestCodeList.add("A3B5");
		suggestCodeList.add("A3C1");
		suggestCodeList.add("A3C2");
		suggestCodeList.add("A3C3");
		suggestCodeList.add("A3C5");
		suggestCodeList.add("A3D1");
		suggestCodeList.add("A3D2");
		suggestCodeList.add("A3D3");
		suggestCodeList.add("A3D5");
		suggestCodeList.add("A4B1");
		suggestCodeList.add("A4B2");
		suggestCodeList.add("A4B3");
		suggestCodeList.add("A4B4");
		suggestCodeList.add("A4B5");
		suggestCodeList.add("A4C1");
		suggestCodeList.add("A4C2");
		suggestCodeList.add("A4C3");
		suggestCodeList.add("A4C5");
		suggestCodeList.add("A4D1");
		suggestCodeList.add("A4D2");
		suggestCodeList.add("A4D3");
		suggestCodeList.add("A4D5");
		suggestCodeList.add("A5B1");
		suggestCodeList.add("A5B2");
		suggestCodeList.add("A5B3");
		suggestCodeList.add("A5B4");
		suggestCodeList.add("A5B5");
		suggestCodeList.add("A5C1");
		suggestCodeList.add("A5C2");
		suggestCodeList.add("A5C3");
		suggestCodeList.add("A5C5");
		suggestCodeList.add("A5D1");
		suggestCodeList.add("A5D2");
		suggestCodeList.add("A5D3");
		suggestCodeList.add("A5D5");
		suggestCodeList.add("B1C1");
		suggestCodeList.add("B1C2");
		suggestCodeList.add("B1C3");
		suggestCodeList.add("B1C5");
		suggestCodeList.add("B1D1");
		suggestCodeList.add("B1D2");
		suggestCodeList.add("B1D3");
		suggestCodeList.add("B1D5");
		suggestCodeList.add("B2C1");
		suggestCodeList.add("B2C2");
		suggestCodeList.add("B2C3");
		suggestCodeList.add("B2C5");
		suggestCodeList.add("B2D1");
		suggestCodeList.add("B2D2");
		suggestCodeList.add("B2D3");
		suggestCodeList.add("B2D5");
		suggestCodeList.add("B3C1");
		suggestCodeList.add("B3C2");
		suggestCodeList.add("B3C3");
		suggestCodeList.add("B3C5");
		suggestCodeList.add("B3D1");
		suggestCodeList.add("B3D2");
		suggestCodeList.add("B3D3");
		suggestCodeList.add("B3D5");
		suggestCodeList.add("B4C1");
		suggestCodeList.add("B4C2");
		suggestCodeList.add("B4C3");
		suggestCodeList.add("B4C5");
		suggestCodeList.add("B4D1");
		suggestCodeList.add("B4D2");
		suggestCodeList.add("B4D3");
		suggestCodeList.add("B4D5");
		suggestCodeList.add("B5C1");
		suggestCodeList.add("B5C2");
		suggestCodeList.add("B5C3");
		suggestCodeList.add("B5C5");
		suggestCodeList.add("B5D1");
		suggestCodeList.add("B5D2");
		suggestCodeList.add("B5D3");
		suggestCodeList.add("B5D5");
		suggestCodeList.add("C1D1");
		suggestCodeList.add("C1D2");
		suggestCodeList.add("C1D3");
		suggestCodeList.add("C1D5");
		suggestCodeList.add("C2D1");
		suggestCodeList.add("C2D2");
		suggestCodeList.add("C2D3");
		suggestCodeList.add("C2D5");
		suggestCodeList.add("C3D1");
		suggestCodeList.add("C3D2");
		suggestCodeList.add("C3D3");
		suggestCodeList.add("C3D5");
		suggestCodeList.add("C5D1");
		suggestCodeList.add("C5D2");
		suggestCodeList.add("C5D3");
		suggestCodeList.add("C5D5");
		suggestCodeList.add("A1B1C1");
		suggestCodeList.add("A1B1C2");
		suggestCodeList.add("A1B1C3");
		suggestCodeList.add("A1B1C5");
		suggestCodeList.add("A1B1D1");
		suggestCodeList.add("A1B1D2");
		suggestCodeList.add("A1B1D3");
		suggestCodeList.add("A1B1D5");
		suggestCodeList.add("A1B2C1");
		suggestCodeList.add("A1B2C2");
		suggestCodeList.add("A1B2C3");
		suggestCodeList.add("A1B2C5");
		suggestCodeList.add("A1B2D1");
		suggestCodeList.add("A1B2D2");
		suggestCodeList.add("A1B2D3");
		suggestCodeList.add("A1B2D5");
		suggestCodeList.add("A1B3C1");
		suggestCodeList.add("A1B3C2");
		suggestCodeList.add("A1B3C3");
		suggestCodeList.add("A1B3C5");
		suggestCodeList.add("A1B3D1");
		suggestCodeList.add("A1B3D2");
		suggestCodeList.add("A1B3D3");
		suggestCodeList.add("A1B3D5");
		suggestCodeList.add("A1B4C1");
		suggestCodeList.add("A1B4C2");
		suggestCodeList.add("A1B4C3");
		suggestCodeList.add("A1B4C5");
		suggestCodeList.add("A1B4D1");
		suggestCodeList.add("A1B4D2");
		suggestCodeList.add("A1B4D3");
		suggestCodeList.add("A1B4D5");
		suggestCodeList.add("A1B5C1");
		suggestCodeList.add("A1B5C2");
		suggestCodeList.add("A1B5C3");
		suggestCodeList.add("A1B5C5");
		suggestCodeList.add("A1B5D1");
		suggestCodeList.add("A1B5D2");
		suggestCodeList.add("A1B5D3");
		suggestCodeList.add("A1B5D5");
		suggestCodeList.add("A2B1C1");
		suggestCodeList.add("A2B1C2");
		suggestCodeList.add("A2B1C3");
		suggestCodeList.add("A2B1C5");
		suggestCodeList.add("A2B1D1");
		suggestCodeList.add("A2B1D2");
		suggestCodeList.add("A2B1D3");
		suggestCodeList.add("A2B1D5");
		suggestCodeList.add("A2B2C1");
		suggestCodeList.add("A2B2C2");
		suggestCodeList.add("A2B2C3");
		suggestCodeList.add("A2B2C5");
		suggestCodeList.add("A2B2D1");
		suggestCodeList.add("A2B2D2");
		suggestCodeList.add("A2B2D3");
		suggestCodeList.add("A2B2D5");
		suggestCodeList.add("A2B3C1");
		suggestCodeList.add("A2B3C2");
		suggestCodeList.add("A2B3C3");
		suggestCodeList.add("A2B3C5");
		suggestCodeList.add("A2B3D1");
		suggestCodeList.add("A2B3D2");
		suggestCodeList.add("A2B3D3");
		suggestCodeList.add("A2B3D5");
		suggestCodeList.add("A2B4C1");
		suggestCodeList.add("A2B4C2");
		suggestCodeList.add("A2B4C3");
		suggestCodeList.add("A2B4C5");
		suggestCodeList.add("A2B4D1");
		suggestCodeList.add("A2B4D2");
		suggestCodeList.add("A2B4D3");
		suggestCodeList.add("A2B4D5");
		suggestCodeList.add("A2B5C1");
		suggestCodeList.add("A2B5C2");
		suggestCodeList.add("A2B5C3");
		suggestCodeList.add("A2B5C5");
		suggestCodeList.add("A2B5D1");
		suggestCodeList.add("A2B5D2");
		suggestCodeList.add("A2B5D3");
		suggestCodeList.add("A2B5D5");
		suggestCodeList.add("A3B1C1");
		suggestCodeList.add("A3B1C2");
		suggestCodeList.add("A3B1C3");
		suggestCodeList.add("A3B1C5");
		suggestCodeList.add("A3B1D1");
		suggestCodeList.add("A3B1D2");
		suggestCodeList.add("A3B1D3");
		suggestCodeList.add("A3B1D5");
		suggestCodeList.add("A3B2C1");
		suggestCodeList.add("A3B2C2");
		suggestCodeList.add("A3B2C3");
		suggestCodeList.add("A3B2C5");
		suggestCodeList.add("A3B2D1");
		suggestCodeList.add("A3B2D2");
		suggestCodeList.add("A3B2D3");
		suggestCodeList.add("A3B2D5");
		suggestCodeList.add("A3B3C1");
		suggestCodeList.add("A3B3C2");
		suggestCodeList.add("A3B3C3");
		suggestCodeList.add("A3B3C5");
		suggestCodeList.add("A3B3D1");
		suggestCodeList.add("A3B3D2");
		suggestCodeList.add("A3B3D3");
		suggestCodeList.add("A3B3D5");
		suggestCodeList.add("A3B4C1");
		suggestCodeList.add("A3B4C2");
		suggestCodeList.add("A3B4C3");
		suggestCodeList.add("A3B4C5");
		suggestCodeList.add("A3B4D1");
		suggestCodeList.add("A3B4D2");
		suggestCodeList.add("A3B4D3");
		suggestCodeList.add("A3B4D5");
		suggestCodeList.add("A3B5C1");
		suggestCodeList.add("A3B5C2");
		suggestCodeList.add("A3B5C3");
		suggestCodeList.add("A3B5C5");
		suggestCodeList.add("A3B5D1");
		suggestCodeList.add("A3B5D2");
		suggestCodeList.add("A3B5D3");
		suggestCodeList.add("A3B5D5");
		suggestCodeList.add("A4B1C1");
		suggestCodeList.add("A4B1C2");
		suggestCodeList.add("A4B1C3");
		suggestCodeList.add("A4B1C5");
		suggestCodeList.add("A4B1D1");
		suggestCodeList.add("A4B1D2");
		suggestCodeList.add("A4B1D3");
		suggestCodeList.add("A4B1D5");
		suggestCodeList.add("A4B2C1");
		suggestCodeList.add("A4B2C2");
		suggestCodeList.add("A4B2C3");
		suggestCodeList.add("A4B2C5");
		suggestCodeList.add("A4B2D1");
		suggestCodeList.add("A4B2D2");
		suggestCodeList.add("A4B2D3");
		suggestCodeList.add("A4B2D5");
		suggestCodeList.add("A4B3C1");
		suggestCodeList.add("A4B3C2");
		suggestCodeList.add("A4B3C3");
		suggestCodeList.add("A4B3C5");
		suggestCodeList.add("A4B3D1");
		suggestCodeList.add("A4B3D2");
		suggestCodeList.add("A4B3D3");
		suggestCodeList.add("A4B3D5");
		suggestCodeList.add("A4B4C1");
		suggestCodeList.add("A4B4C2");
		suggestCodeList.add("A4B4C3");
		suggestCodeList.add("A4B4C5");
		suggestCodeList.add("A4B4D1");
		suggestCodeList.add("A4B4D2");
		suggestCodeList.add("A4B4D3");
		suggestCodeList.add("A4B4D5");
		suggestCodeList.add("A4B5C1");
		suggestCodeList.add("A4B5C2");
		suggestCodeList.add("A4B5C3");
		suggestCodeList.add("A4B5C5");
		suggestCodeList.add("A4B5D1");
		suggestCodeList.add("A4B5D2");
		suggestCodeList.add("A4B5D3");
		suggestCodeList.add("A4B5D5");
		suggestCodeList.add("A5B1C1");
		suggestCodeList.add("A5B1C2");
		suggestCodeList.add("A5B1C3");
		suggestCodeList.add("A5B1C5");
		suggestCodeList.add("A5B1D1");
		suggestCodeList.add("A5B1D2");
		suggestCodeList.add("A5B1D3");
		suggestCodeList.add("A5B1D5");
		suggestCodeList.add("A5B2C1");
		suggestCodeList.add("A5B2C2");
		suggestCodeList.add("A5B2C3");
		suggestCodeList.add("A5B2C5");
		suggestCodeList.add("A5B2D1");
		suggestCodeList.add("A5B2D2");
		suggestCodeList.add("A5B2D3");
		suggestCodeList.add("A5B2D5");
		suggestCodeList.add("A5B3C1");
		suggestCodeList.add("A5B3C2");
		suggestCodeList.add("A5B3C3");
		suggestCodeList.add("A5B3C5");
		suggestCodeList.add("A5B3D1");
		suggestCodeList.add("A5B3D2");
		suggestCodeList.add("A5B3D3");
		suggestCodeList.add("A5B3D5");
		suggestCodeList.add("A5B4C1");
		suggestCodeList.add("A5B4C2");
		suggestCodeList.add("A5B4C3");
		suggestCodeList.add("A5B4C5");
		suggestCodeList.add("A5B4D1");
		suggestCodeList.add("A5B4D2");
		suggestCodeList.add("A5B4D3");
		suggestCodeList.add("A5B4D5");
		suggestCodeList.add("A5B5C1");
		suggestCodeList.add("A5B5C2");
		suggestCodeList.add("A5B5C3");
		suggestCodeList.add("A5B5C5");
		suggestCodeList.add("A5B5D1");
		suggestCodeList.add("A5B5D2");
		suggestCodeList.add("A5B5D3");
		suggestCodeList.add("A5B5D5");
		suggestCodeList.add("B1C1D1");
		suggestCodeList.add("B1C1D2");
		suggestCodeList.add("B1C1D3");
		suggestCodeList.add("B1C1D5");
		suggestCodeList.add("B1C2D1");
		suggestCodeList.add("B1C2D2");
		suggestCodeList.add("B1C2D3");
		suggestCodeList.add("B1C2D5");
		suggestCodeList.add("B1C3D1");
		suggestCodeList.add("B1C3D2");
		suggestCodeList.add("B1C3D3");
		suggestCodeList.add("B1C3D5");
		suggestCodeList.add("B1C5D1");
		suggestCodeList.add("B1C5D2");
		suggestCodeList.add("B1C5D3");
		suggestCodeList.add("B1C5D5");
		suggestCodeList.add("B2C1D1");
		suggestCodeList.add("B2C1D2");
		suggestCodeList.add("B2C1D3");
		suggestCodeList.add("B2C1D5");
		suggestCodeList.add("B2C2D1");
		suggestCodeList.add("B2C2D2");
		suggestCodeList.add("B2C2D3");
		suggestCodeList.add("B2C2D5");
		suggestCodeList.add("B2C3D1");
		suggestCodeList.add("B2C3D2");
		suggestCodeList.add("B2C3D3");
		suggestCodeList.add("B2C3D5");
		suggestCodeList.add("B2C5D1");
		suggestCodeList.add("B2C5D2");
		suggestCodeList.add("B2C5D3");
		suggestCodeList.add("B2C5D5");
		suggestCodeList.add("B3C1D1");
		suggestCodeList.add("B3C1D2");
		suggestCodeList.add("B3C1D3");
		suggestCodeList.add("B3C1D5");
		suggestCodeList.add("B3C2D1");
		suggestCodeList.add("B3C2D2");
		suggestCodeList.add("B3C2D3");
		suggestCodeList.add("B3C2D5");
		suggestCodeList.add("B3C3D1");
		suggestCodeList.add("B3C3D2");
		suggestCodeList.add("B3C3D3");
		suggestCodeList.add("B3C3D5");
		suggestCodeList.add("B3C5D1");
		suggestCodeList.add("B3C5D2");
		suggestCodeList.add("B3C5D3");
		suggestCodeList.add("B3C5D5");
		suggestCodeList.add("B4C1D1");
		suggestCodeList.add("B4C1D2");
		suggestCodeList.add("B4C1D3");
		suggestCodeList.add("B4C1D5");
		suggestCodeList.add("B4C2D1");
		suggestCodeList.add("B4C2D2");
		suggestCodeList.add("B4C2D3");
		suggestCodeList.add("B4C2D5");
		suggestCodeList.add("B4C3D1");
		suggestCodeList.add("B4C3D2");
		suggestCodeList.add("B4C3D3");
		suggestCodeList.add("B4C3D5");
		suggestCodeList.add("B4C5D1");
		suggestCodeList.add("B4C5D2");
		suggestCodeList.add("B4C5D3");
		suggestCodeList.add("B4C5D5");
		suggestCodeList.add("B5C1D1");
		suggestCodeList.add("B5C1D2");
		suggestCodeList.add("B5C1D3");
		suggestCodeList.add("B5C1D5");
		suggestCodeList.add("B5C2D1");
		suggestCodeList.add("B5C2D2");
		suggestCodeList.add("B5C2D3");
		suggestCodeList.add("B5C2D5");
		suggestCodeList.add("B5C3D1");
		suggestCodeList.add("B5C3D2");
		suggestCodeList.add("B5C3D3");
		suggestCodeList.add("B5C3D5");
		suggestCodeList.add("B5C5D1");
		suggestCodeList.add("B5C5D2");
		suggestCodeList.add("B5C5D3");
		suggestCodeList.add("B5C5D5");			
	}

	private String checkRecap(){
		String checkCode = codeText.getText().toString();
		if(usedCodeList.contains(checkCode)){
			checkText.setText("Available");
		}else{
			checkText.setText("Not Available");
		}
		return checkCode;
	}

	private String splitString(){
		String codeCheck = codeText.getText().toString();		
		int size = codeCheck.length();		
		ArrayList<String> testString = new ArrayList<String>();
		ArrayList<String> newCode = new ArrayList<String>();

		if(!codeCheck.equals(null)){
			String part1 = codeCheck.substring(0, 2);
			testString.add(part1);			

			if(size > 2){
				String part2 = codeCheck.substring(2, 4);
				testString.add(part2);				
			}			
			if(size > 4){
				String part3 = codeCheck.substring(4, 6);
				testString.add(part3);				
			}
			if(size > 6){
				String part4 = codeCheck.substring(6, 8);
				testString.add(part4);				
			}
		}

		ArrayList<String> codeWithA = new ArrayList<String>();
		ArrayList<String> codeWithB = new ArrayList<String>();
		ArrayList<String> codeWithC = new ArrayList<String>();
		ArrayList<String> codeWithD = new ArrayList<String>();

		for (String string : testString) {
			if(string.charAt(0) == 'A'){
				codeWithA.add(string);
			}
			else if(string.charAt(0) == 'B'){
				codeWithB.add(string);
			}
			else if(string.charAt(0) == 'C'){
				codeWithC.add(string);
			}
			else if(string.charAt(0) == 'D'){
				codeWithD.add(string);
			}
		}

		if(!codeWithA.isEmpty()){										
			boolean swap = true;
			int number = 1;

			while(swap){				
				if(number == 6){
					swap = false;
				}

				for (String checkA : codeWithA) {
					String numberExtracted = checkA.substring(1, 2);
					int numberA = Integer.parseInt(numberExtracted);
					if(numberA == number){
						newCode.add(checkA);
					}
				}				

				number++;
			}

		}

		if(!codeWithB.isEmpty()){
			boolean swap = true;
			int number = 1;

			while(swap){				
				if(number == 6){
					swap = false;
				}

				for (String checkB : codeWithB) {
					String numberExtracted = checkB.substring(1, 2);
					int numberB = Integer.parseInt(numberExtracted);
					if(numberB == number){
						newCode.add(checkB);
					}
				}				

				number++;
			}
		}

		if(!codeWithC.isEmpty()){
			boolean swap = true;
			int number = 1;

			while(swap){				
				if(number == 6){
					swap = false;
				}

				for (String checkC : codeWithC) {
					String numberExtracted = checkC.substring(1, 2);
					int numberC = Integer.parseInt(numberExtracted);
					if(numberC == number){
						newCode.add(checkC);
					}
				}				

				number++;
			}
		}

		if(!codeWithD.isEmpty()){
			boolean swap = true;
			int number = 1;

			while(swap){				
				if(number == 6){
					swap = false;
				}

				for (String checkD : codeWithD) {
					String numberExtracted = checkD.substring(1, 2);
					int numberD = Integer.parseInt(numberExtracted);
					if(numberD == number){
						newCode.add(checkD);
					}
				}				

				number++;
			}
		}
		String alignedCode = "";
		for (String string : newCode) {
			alignedCode += string;
		}

		if(usedCodeList.contains(alignedCode)){
			checkText.setText("Not Available");
		}

		return alignedCode;
	}

}
