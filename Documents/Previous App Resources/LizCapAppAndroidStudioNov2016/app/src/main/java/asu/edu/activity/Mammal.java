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
import asu.edu.database.Checkdate;
import asu.edu.database.JsonDBAdapter;

public class Mammal extends Activity {

	/**
	 * @param args
	 */
	private Button backButton;
	private Button verifyButton;
	private Button codeButton;
	private Button fenceButton;
	private Spinner sexSpinner;
	private Spinner fenceSpinner;
	private CheckBox deadCheckBox;
	private AutoCompleteTextView speciesCodeText;
	private EditText massText;
	private EditText commentsText;
	private String dead;
	private final ArrayList<String> codeMammal = new ArrayList<String>();
	private final ArrayList<String> nameMammal = new ArrayList<String>();
	private final ArrayList<String> descriptionMammal = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mammal);
		setTitle("Mammal Data");

		this.backButton = (Button) this.findViewById(R.id.mammalBackButton);
		this.verifyButton = (Button) this.findViewById(R.id.mammalVerifyButton);
		//this.historyButton = (Button) this.findViewById(R.id.mammalHistoryButton);
		this.codeButton = (Button) this.findViewById(R.id.mammalSpeciesButton);
		this.fenceButton = (Button) this.findViewById(R.id.mammalMapButton);
		this.massText = (EditText) this.findViewById(R.id.mammalMassText);
		this.deadCheckBox = (CheckBox) this.findViewById(R.id.mammalDeadCheckBox);
		this.sexSpinner = (Spinner) this.findViewById(R.id.mammalSexSpinner);		
		this.fenceSpinner = (Spinner) this.findViewById(R.id.mammalFenceSpinner);
		this.commentsText = (EditText) this.findViewById(R.id.mammalCommentsText);
		this.speciesCodeText = (AutoCompleteTextView) this.findViewById(R.id.mammalSpeciesCodeAutoCompleteTextView);

		JsonDBAdapter jsonDBHelper = new JsonDBAdapter(this);
		jsonDBHelper.open();

		Cursor herpsCursor = jsonDBHelper.fetchSelectHerps("Mammal");
		while(herpsCursor.moveToNext()){
			codeMammal.add(herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.SPPCODE_TEXT)));
			nameMammal.add(herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.GENUS_TEXT)));
			descriptionMammal.add(herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.SPECIES_TEXT)));
		}
		herpsCursor.close();

		jsonDBHelper.close();

		ArrayAdapter<String> adapterMammal = new ArrayAdapter<String>(Mammal.this, 
				android.R.layout.simple_dropdown_item_1line, codeMammal);

		speciesCodeText.setAdapter(adapterMammal);

		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(Mammal.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Exit")
						.setMessage("Information Entered will be Lost")
						.setNegativeButton("No", null)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Exit the activity
								Mammal.this.finish();
							}
						}).show();
			}
		});

		verifyButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean checkForErrors = errorCheck();
				if(checkForErrors){
					boolean deadCheck = deadCheckBox.isChecked();
					if(deadCheck){
						dead = "Yes";
					}else{
						dead = "No";
					}

					Intent arthopodTwoIntent = new Intent(Mammal.this, VerifyMammal.class);					
					Bundle bundle = new Bundle();
					bundle.putString("SpeciesCode", speciesCodeText.getText().toString());
					bundle.putString("Fence", (String) fenceSpinner.getSelectedItem());
					bundle.putString("Mass", massText.getText().toString());
					bundle.putString("Sex", (String) sexSpinner.getSelectedItem());
					bundle.putString("Dead", dead);
					bundle.putString("Comments", commentsText.getText().toString());
					bundle.putString("TrapClosed", null);
					arthopodTwoIntent.putExtras(bundle);
					Mammal.this.startActivity(arthopodTwoIntent);
				}	
			}			
		});

		codeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent mammalIntent = new Intent(Mammal.this, SpeciesList.class);
				mammalIntent.putStringArrayListExtra("Code", codeMammal);
				mammalIntent.putStringArrayListExtra("Genus", nameMammal);
				mammalIntent.putStringArrayListExtra("Species", descriptionMammal);				
				startActivityForResult(mammalIntent, 1);
			}
		});

		fenceButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent fenceIntent = new Intent(Mammal.this, FenceArray.class);							
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

		if(codeCheck() && massCheck() && sexCheck()){
			finalCheck = true;
		}
		return finalCheck;
	}

	private boolean codeCheck(){
		String tempUserInput = speciesCodeText.getText().toString().trim();
		boolean codeResult = false;

		if(codeMammal.contains(tempUserInput) && !tempUserInput.equals("")){
			codeResult = true;										
		}else{
			new AlertDialog.Builder(Mammal.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Species Code must have 4 letter existing code.\n\nRefer to the 'Look up Code' button for a list of species.")
					.setNegativeButton("Ok", null)
					.show();
		}
		return codeResult;
	}

	private boolean massCheck() {
		String errormass = massText.getText().toString().trim();
		boolean massResult = false;

		if(!errormass.equals("")){
			if((errormass.matches("[0-9]*")) || (errormass.matches("[0-9]*[.][0-9]*")) ){
				massResult = true;
			}else{
				new AlertDialog.Builder(Mammal.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Error")
						.setMessage("Mass needs to be a decimal number or blank")
						.setNegativeButton("Ok", null)
						.show();
			}
		}else{
			massResult = true;
		}

		return massResult;

	}

	private boolean sexCheck(){
		String errorsex = sexSpinner.getSelectedItem().toString().trim();
		boolean sexResult = false;

		if(!errorsex.equals("Select")){
			sexResult = true;
		}else{
			sexResult = false;
			new AlertDialog.Builder(Mammal.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Sex needs to be Chosen")
					.setNegativeButton("Ok", null)
					.show();
		}

		return sexResult;
	}
}
