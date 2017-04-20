package asu.edu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import asu.edu.R;
import asu.edu.database.JsonDBAdapter;

public class HerpDataPartOne extends Activity {

	/**
	 * @param args
	 */
	private Button historyButton;
	private Button toeclipButton;
	private Button backButton;
	private Button nextButton;
	private Button codeButton;
	private Button fenceButton;
	private Spinner trapArraySpinner;
	private CheckBox recaptureCheckBox;
	private AutoCompleteTextView speciesCodeText;
	private TextView toeclipLabel;
	private String recap;
	private String species;
	private final ArrayList<String> codeLizard = new ArrayList<String>();
	private final ArrayList<String> nameLizard = new ArrayList<String>();
	private final ArrayList<String> descriptionLizard = new ArrayList<String>();

	// Might need to remove
	ArrayList<String> toeclipArray = new ArrayList<String>(); // TODO remove / why is this package-private?

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.herpdata);
		setTitle("Lizard Data");

		this.backButton = (Button) this.findViewById(R.id.herpdataoneBackButton);
		this.nextButton = (Button) this.findViewById(R.id.herpdataoneNextButton);
		this.toeclipButton = (Button) this.findViewById(R.id.herpdataoneToeClipButton);
		this.historyButton = (Button) this.findViewById(R.id.herpdataoneHistoryButton);
		this.codeButton = (Button) this.findViewById(R.id.herpdataoneSpeciesButton);
		this.fenceButton = (Button) this.findViewById(R.id.herpdataoneMapButton);
		this.toeclipLabel = (TextView) this.findViewById(R.id.herpdataoneToeClipLabel);
		this.trapArraySpinner = (Spinner) findViewById(R.id.herpdataoneTrapArraySpinner);
		this.recaptureCheckBox = (CheckBox) findViewById(R.id.herpdataoneRecaptureCheckBox);
		this.speciesCodeText = (AutoCompleteTextView) this.findViewById(R.id.herpdataoneSpeciesCodeAutoCompleteTextView);

		historyButton.setVisibility(View.GONE);
		JsonDBAdapter jsonDBHelper = new JsonDBAdapter(this);
		jsonDBHelper.open();

		Cursor herpsCursor = jsonDBHelper.fetchSelectHerps("Lizard");
		while(herpsCursor.moveToNext()){
			codeLizard.add(herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.SPPCODE_TEXT)));
			nameLizard.add(herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.GENUS_TEXT)));
			descriptionLizard.add(herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.SPECIES_TEXT)));
		}
		herpsCursor.close();

		jsonDBHelper.close();

		ArrayAdapter<String> adapterLizard = new ArrayAdapter<String>(HerpDataPartOne.this, 
				android.R.layout.simple_dropdown_item_1line, codeLizard);

		speciesCodeText.setAdapter(adapterLizard);

		this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(HerpDataPartOne.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Exit")
						.setMessage("Information Entered will be Lost")
						.setNegativeButton("No", null)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Exit the activity
								HerpDataPartOne.this.finish();
							}
						}).show();
			}
		});

		this.nextButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				boolean testOnErrors = errorCheck();
				if(testOnErrors){
					boolean recapCheck = recaptureCheckBox.isChecked(); 
					if(recapCheck == true){
						recap = "Yes";
					}else{
						recap = "No";
					}
					
					String toeClipCode = toeclipLabel.getText().toString();
					
					if (toeClipCode.equals("")) {
						new AlertDialog.Builder(HerpDataPartOne.this).setIcon(
								android.R.drawable.ic_dialog_alert).setTitle("No Toe Clip Code")
								.setMessage("Proceed without a toe-clip code?")
								.setNegativeButton("No", null)
								.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
										startPartTwoIntent();
									}
								}).show();
					}
					else {
						startPartTwoIntent();
					}
				}
			}
		});

		this.toeclipButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				boolean speciesCheck = speciescodeCheck();
				if(speciesCheck){
					species = speciesCodeText.getText().toString();
					if(recaptureCheckBox.isChecked()){
						recap = "Yes";
					}else{
						recap = "No";
					}
					Intent toeclipIntent = new Intent(HerpDataPartOne.this, ToeClipCode.class);
					toeclipIntent.putExtra("Recap", recap);
					toeclipIntent.putExtra("Species", species);
					startActivityForResult(toeclipIntent, 3);
				}
			}


		});

		this.historyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent historyIntent = new Intent(HerpDataPartOne.this, RecaptureList.class);
				Bundle bundle = new Bundle();
				bundle.putString("SpeciesCode", speciesCodeText.getText().toString());
				bundle.putString("ToeClipCode", toeclipLabel.getText().toString());        		
				historyIntent.putExtras(bundle);
				HerpDataPartOne.this.startActivity(historyIntent);
			}
		});

		this.codeButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent herpIntent = new Intent(HerpDataPartOne.this, SpeciesList.class);
				herpIntent.putStringArrayListExtra("Code", codeLizard);
				herpIntent.putStringArrayListExtra("Genus", nameLizard);
				herpIntent.putStringArrayListExtra("Species", descriptionLizard);				
				startActivityForResult(herpIntent, 1);
			}
		});

		this.fenceButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent fenceIntent = new Intent(HerpDataPartOne.this, FenceArray.class);							
				startActivityForResult(fenceIntent, 2);
			}
		});
	}
	
	private void startPartTwoIntent () {
		Intent herpDataTwoIntent = new Intent(HerpDataPartOne.this, HerpDataPartTwo.class);
		Bundle bundle = new Bundle();
		bundle.putString("SpeciesCode", speciesCodeText.getText().toString());
		bundle.putString("Recapture", recap);
		bundle.putString("ToeClipCode", toeclipLabel.getText().toString());
		bundle.putString("Fence", (String) trapArraySpinner.getSelectedItem());
		herpDataTwoIntent.putExtras(bundle);
		HerpDataPartOne.this.startActivity(herpDataTwoIntent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 1){
			String msg = data.getStringExtra("returnedData");
			speciesCodeText.setText(msg);
		}
		if(resultCode == RESULT_OK && requestCode == 2){
			String msg = data.getStringExtra("returnedData");            
			int value = Integer.parseInt(msg);
			trapArraySpinner.setSelection(value);
		}
		if(resultCode == RESULT_OK && requestCode == 3){
			String msg = data.getStringExtra("returnedData");            
			toeclipLabel.setText(msg.toString());

			boolean recapCheck = recaptureCheckBox.isChecked(); 
			if(recapCheck == true){
				recap = "Yes";
			}else{
				recap = "No";
			}
			if(recap.equals("Yes")){
				Intent historyIntent = new Intent(HerpDataPartOne.this, RecaptureList.class);
				Bundle bundle = new Bundle();
				bundle.putString("SpeciesCode", speciesCodeText.getText().toString());
				bundle.putString("ToeClipCode", toeclipLabel.getText().toString());        		
				historyIntent.putExtras(bundle);
				HerpDataPartOne.this.startActivity(historyIntent);
				historyButton.setVisibility(View.VISIBLE);
			}else{
				historyButton.setVisibility(View.GONE);
			}
		}
	}

	private boolean errorCheck(){
		boolean finalCheck = false;

		if(codeCheck()){
			finalCheck = true;
		}
		return finalCheck;
	}

	private boolean speciescodeCheck() {
		String tempUserInput = speciesCodeText.getText().toString().trim();
		boolean codeResult = false;

		if(codeLizard.contains(tempUserInput) && !tempUserInput.equals("")){
			codeResult = true;										
		}else{
			new AlertDialog.Builder(HerpDataPartOne.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Species Code must have 4 letter existing code.\n\nRefer to the 'Look up Code' button for a list of species.")
					.setNegativeButton("Ok", null)
					.show();
		}
		return codeResult;
	}

	private boolean codeCheck(){
		String tempUserInput = speciesCodeText.getText().toString().trim();
		boolean codeResult = false;

		if(codeLizard.contains(tempUserInput) && !tempUserInput.equals("")){
			codeResult = true;										
		}else{
			new AlertDialog.Builder(HerpDataPartOne.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Species Code must have 4 letter existing code.\n\nRefer to the 'Look up Code' button for a list of species.")
					.setNegativeButton("Ok", null)
					.show();
		}
		return codeResult;
	}
}
