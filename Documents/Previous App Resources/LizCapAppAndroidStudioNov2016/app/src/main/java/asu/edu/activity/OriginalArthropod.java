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
import android.widget.EditText;
import android.widget.Spinner;
import asu.edu.R;
import asu.edu.database.JsonDBAdapter;

public class OriginalArthropod extends Activity {

	/**
	 * @param args
	 */
	private Button backButton;
	private Button verifyButton;
	private Button codeButton;
	private Button fenceButton;
	private CheckBox predatorCheckBox;	
	private AutoCompleteTextView speciesCodeText;
	private Spinner fenceSpinner;
	private EditText numberText;
	private EditText notesText;
	private String recorder;
	private String handler;
	private String site;
	private String array;
	private String taxa;
	private String predator;
	private final ArrayList<String> codeArthropod = new ArrayList<String>();
	private final ArrayList<String> nameArthropod = new ArrayList<String>();
	private final ArrayList<String> descriptionArthropod = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arthopoddata);
		setTitle("Arthropod Data");

		this.backButton = (Button) this.findViewById(R.id.arthropodBackButton);
		this.verifyButton = (Button) this.findViewById(R.id.arthropodVerifyButton);
		//this.historyButton = (Button) this.findViewById(R.id.arthropodHistoryButton);
		this.codeButton = (Button) this.findViewById(R.id.arthropodSpeciesButton);
		this.fenceButton = (Button) this.findViewById(R.id.arthropodMapButton);
		this.predatorCheckBox = (CheckBox) this.findViewById(R.id.arthropodPredatorCheckBox);
		this.numberText = (EditText) this.findViewById(R.id.arthropodNumberText);
		this.fenceSpinner = (Spinner) this.findViewById(R.id.arthropodFenceSpinner);
		this.notesText = (EditText) this.findViewById(R.id.arthropodNotesText);		
		this.speciesCodeText = (AutoCompleteTextView) this.findViewById(R.id.arthropodSpeciesCodeAutoCompleteTextView);

		Bundle bundle = getIntent().getExtras();
		recorder = bundle.getString("Recorder");
		handler = bundle.getString("Handler");
		site = bundle.getString("Site");
		array = bundle.getString("Array");
		taxa = bundle.getString("Taxa");

		JsonDBAdapter jsonDBHelper = new JsonDBAdapter(this);
		jsonDBHelper.open();

		Cursor anthropod = jsonDBHelper.fetchSelectAnthropod();
		while (anthropod.moveToNext()){
			codeArthropod.add(anthropod.getString(anthropod.getColumnIndexOrThrow(JsonDBAdapter.ORDERCODE_TEXT)));
			nameArthropod.add(anthropod.getString(anthropod.getColumnIndexOrThrow(JsonDBAdapter.ORDERNAME_TEXT)));
			descriptionArthropod.add(anthropod.getString(anthropod.getColumnIndexOrThrow(JsonDBAdapter.DESCRIPTION_TEXT)));
		}
		anthropod.close();

		jsonDBHelper.close();

		ArrayAdapter<String> adapterArthropod = new ArrayAdapter<String>(OriginalArthropod.this, 
				android.R.layout.simple_dropdown_item_1line, codeArthropod);

		speciesCodeText.setAdapter(adapterArthropod);

		this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(OriginalArthropod.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Exit")
						.setMessage("Information Entered will be Lost")
						.setNegativeButton("No", null)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Exit the activity
								OriginalArthropod.this.finish();
							}
						}).show();
			}
		});

		this.verifyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				boolean testOnErrors = errorCheck();
				if(testOnErrors){
					boolean predatorCheck = predatorCheckBox.isChecked(); 
					if(predatorCheck == true){
						predator = "Yes";
					}else{
						predator = "No";
					}

					Intent arthopodTwoIntent = new Intent(OriginalArthropod.this, VerifyArthropod.class);

					Bundle bundle = new Bundle();        
					bundle.putString("Recorder", recorder);
					bundle.putString("Handler", handler);
					bundle.putString("Site", site);
					bundle.putString("Array", array);
					bundle.putString("Taxa", taxa);
					bundle.putString("SpeciesCode", speciesCodeText.getText().toString());
					bundle.putString("Predator", predator);
					bundle.putString("Fence", (String) fenceSpinner.getSelectedItem());
					bundle.putString("Number", numberText.getText().toString());
					bundle.putString("Notes", notesText.getText().toString());
					arthopodTwoIntent.putExtras(bundle);
					OriginalArthropod.this.startActivity(arthopodTwoIntent);
				}		
			}
		});

		/*this.historyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent historyIntent = new Intent(Arthropod.this, History.class);
				Arthropod.this.startActivity(historyIntent);
			}
		});*/

		this.codeButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent arthropodIntent = new Intent(OriginalArthropod.this, SpeciesList.class);
				arthropodIntent.putStringArrayListExtra("Code", codeArthropod);
				arthropodIntent.putStringArrayListExtra("Genus", nameArthropod);
				arthropodIntent.putStringArrayListExtra("Species", descriptionArthropod);				
				startActivityForResult(arthropodIntent, 1);
			}
		});

		this.fenceButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent fenceIntent = new Intent(OriginalArthropod.this, TrapArray.class);							
				startActivityForResult(fenceIntent, 2);
			}
		});
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
			fenceSpinner.setSelection(value);
		}
	}

	private boolean errorCheck(){
		boolean finalCheck = false;

		if(codeCheck() && numberCheck()){
			finalCheck = true;
		}
		return finalCheck;
	}

	private boolean codeCheck(){
		String tempUserInput = speciesCodeText.getText().toString().trim();
		boolean codeResult = false;

		if(codeArthropod.contains(tempUserInput) && !tempUserInput.equals("")){
			codeResult = true;										
		}else{
			new AlertDialog.Builder(OriginalArthropod.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Species Code must have 4 letter existing code.\n\nRefer to the 'Look up Code' button for a list of species.")
					.setNegativeButton("Ok", null)
					.show();
		}
		return codeResult;
	}

	private boolean numberCheck(){
		String errornumber = numberText.getText().toString().trim();
		boolean numberResult = false;

		if(!errornumber.equals("")){
			if(errornumber.matches("[0-9]*")){
				numberResult = true;
			}else{
				new AlertDialog.Builder(OriginalArthropod.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Error")
						.setMessage("Number needs to be a digit or set it to zero")
						.setNegativeButton("Ok", null)
						.show();
			}
		}else{
			numberResult = false;
			new AlertDialog.Builder(OriginalArthropod.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Number can not be blank. Set it to zero or greater")
					.setNegativeButton("Ok", null)
					.show();
		}

		return numberResult;
	}

}