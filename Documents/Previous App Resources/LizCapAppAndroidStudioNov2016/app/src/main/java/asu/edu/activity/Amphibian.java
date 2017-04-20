package asu.edu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
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

public class Amphibian extends Activity {

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
	private EditText commentsText;
	private EditText hdbodyText;
	private EditText massText;
	private String dead;
	final ArrayList<String> codeAmphibian = new ArrayList<String>();
	final ArrayList<String> nameAmphibian = new ArrayList<String>();
	final ArrayList<String> descriptionAmphibian = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.amphibian);
		setTitle("Amphibian Data");

		this.backButton = (Button) this.findViewById(R.id.amphibianBackButton);
		this.verifyButton = (Button) this.findViewById(R.id.amphibianVerifyButton);
		//this.historyButton = (Button) this.findViewById(R.id.amphibianHistoryButton);
		this.codeButton = (Button) this.findViewById(R.id.amphibianSpeciesButton);
		this.fenceButton = (Button) this.findViewById(R.id.amphibianMapButton);
		this.deadCheckBox = (CheckBox) this.findViewById(R.id.amphibianDeadCheckBox);
		this.sexSpinner = (Spinner) this.findViewById(R.id.amphibianSexSpinner);
		this.fenceSpinner = (Spinner) this.findViewById(R.id.amphibianFenceSpinner);
		this.commentsText = (EditText) this.findViewById(R.id.amphibianCommentsText);
		this.hdbodyText = (EditText) this.findViewById(R.id.amphibianHDBodyText);
		this.massText = (EditText) this.findViewById(R.id.amphibianMassText);
		this.speciesCodeText = (AutoCompleteTextView) this.findViewById(R.id.amphibianSpeciesCodeAutoCompleteTextView);

		//backHit = false;
		JsonDBAdapter jsonDBHelper = new JsonDBAdapter(this);
		jsonDBHelper.open();

		Cursor herpsCursor = jsonDBHelper.fetchSelectHerps("Amphibian");
		while(herpsCursor.moveToNext()){
			codeAmphibian.add(herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.SPPCODE_TEXT)));
			nameAmphibian.add(herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.GENUS_TEXT)));
			descriptionAmphibian.add(herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.SPECIES_TEXT)));
		}
		herpsCursor.close();
		jsonDBHelper.close();
		ArrayAdapter<String> adapterAmphibian = new ArrayAdapter<String>(Amphibian.this, 
				android.R.layout.simple_dropdown_item_1line, codeAmphibian);

		speciesCodeText.setAdapter(adapterAmphibian);

		this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {				
				boolean deadCheck = deadCheckBox.isChecked();
				if(deadCheck){
					dead = "Yes";
				}else{
					dead = "No";
				}
				
				Intent intent = new Intent(Amphibian.this, TaxaSelection.class); 
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				//Bundle bundle = new Bundle();        
				//bundle.putBoolean("BackHit", backHit);
				/*bundle.putString("SpeciesCode", speciesCodeText.getText().toString());
        		bundle.putString("Fence", (String) fenceSpinner.getSelectedItem());
        		bundle.putString("HdBody", hdbodyText.getText().toString());
        		bundle.putString("Mass", massText.getText().toString());
        		bundle.putString("Sex", (String) sexSpinner.getSelectedItem());
        		bundle.putString("Dead", dead);
        		bundle.putString("Comments", commentsText.getText().toString());
        		bundle.putString("TrapClosed", trapclosed);*/
				//intent.putExtras(bundle);
				Amphibian.this.startActivity(intent);
			}
		});

		this.verifyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				boolean testOnErrors = errorCheck();
				if(testOnErrors){
					boolean deadCheck = deadCheckBox.isChecked();
					if(deadCheck){
						dead = "Yes";
					}else{
						dead = "No";
					}

					Intent amphibianIntent = new Intent(Amphibian.this, VerifyAmphibian.class);

					Bundle bundle = new Bundle();
					bundle.putString("SpeciesCode", speciesCodeText.getText().toString());
					bundle.putString("Fence", (String) fenceSpinner.getSelectedItem());
					bundle.putString("HdBody", hdbodyText.getText().toString());
					bundle.putString("Mass", massText.getText().toString());
					bundle.putString("Sex", (String) sexSpinner.getSelectedItem());
					bundle.putString("Dead", dead);
					bundle.putString("Comments", commentsText.getText().toString());
					bundle.putString("TrapClosed", null);
					amphibianIntent.putExtras(bundle);
					Amphibian.this.startActivity(amphibianIntent);
				}				
			}
		});

		/*this.historyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent historyIntent = new Intent(Amphibian.this, History.class);
				Amphibian.this.startActivity(historyIntent);
			}
		});*/

		this.codeButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {				
				Intent amphibianIntent = new Intent(Amphibian.this, SpeciesList.class);
				amphibianIntent.putStringArrayListExtra("Code", codeAmphibian);
				amphibianIntent.putStringArrayListExtra("Genus", nameAmphibian);
				amphibianIntent.putStringArrayListExtra("Species", descriptionAmphibian);				
				startActivityForResult(amphibianIntent, 1);
			}
		});

		this.fenceButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent fenceIntent = new Intent(Amphibian.this, FenceArray.class);							
				startActivityForResult(fenceIntent, 2);
			}
		});
	}

	/*
	private void checkForRestart() {
		try{
			Bundle bundle = getIntent().getExtras();
			backHit = bundle.getBoolean("backHit");		
	        speciescode = bundle.getString("SpeciesCode");
	        fence = bundle.getString("Fence");
	        hdbody= bundle.getString("HdBody");
	        mass = bundle.getString("Mass");
	        sex = bundle.getString("Sex");
	        deadcode = bundle.getString("Dead");
	        comments = bundle.getString("Comments");
	        trapclosedcode = bundle.getString("TrapClosed");
	    }catch(Exception e){}

        if(backHit == true){        
        	ArrayList<String> fenceTemp = new ArrayList<String>();        	
        	fenceTemp.add("A1");
        	fenceTemp.add("A2");
		    fenceTemp.add("A3");
		    fenceTemp.add("A4");
		    fenceTemp.add("A-Fence");
		    fenceTemp.add("B2");
		    fenceTemp.add("B3");
		    fenceTemp.add("B4");
		    fenceTemp.add("B-Fence");
		    fenceTemp.add("C2");
		    fenceTemp.add("C3");
		    fenceTemp.add("C4");
		    fenceTemp.add("C-Fence");
		    ArrayList<String> sexTemp = new ArrayList<String>();        	
        	sexTemp.add("Select");
        	sexTemp.add("Female");
        	sexTemp.add("Male");
        	sexTemp.add("Unknown");

            int sexInt = sexTemp.indexOf(sex);
            int fenceInt = fenceTemp.indexOf(fence);

            speciesCodeText.setText(speciescode);
            fenceSpinner.setSelection(fenceInt);
            hdbodyText.setText(hdbody);
            massText.setText(mass);
            sexSpinner.setSelection(sexInt);
            if(deadcode.equals("Yes")){
            	deadCheckBox.setChecked(true);
            }
            commentsText.setText(comments);
            if(trapclosedcode.equals("Yes")){
            	trapclosedCheck.setChecked(true);
            }
        }
	}
	 */

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

		if(codeCheck() && hdbodyCheck() && massCheck() && sexCheck()){
			finalCheck = true;
		}
		return finalCheck;
	}

	private boolean codeCheck(){
		String tempUserInput = speciesCodeText.getText().toString().trim();
		boolean codeResult = false;

		if(codeAmphibian.contains(tempUserInput) && !tempUserInput.equals("")){
			codeResult = true;										
		}else{
			new AlertDialog.Builder(Amphibian.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Species Code must have 4 letter existing code.\n\nRefer to the 'Look up Code' button for a list of species.")
					.setNegativeButton("Ok", null)
					.show();
		}
		return codeResult;
	}

	private boolean hdbodyCheck(){
		String errorhdbody = hdbodyText.getText().toString().trim();
		boolean hdbodyResult = false;

		if(!errorhdbody.equals("")){
			if(errorhdbody.matches("[0-9]*")){
				hdbodyResult = true;
			}else{
				new AlertDialog.Builder(Amphibian.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Error")
						.setMessage("Hd-body needs to be a number or blank")
						.setNegativeButton("Ok", null)
						.show();
			}
		}else{
			hdbodyResult = true;
		}

		return hdbodyResult;
	}

	private boolean massCheck(){
		String errormass = massText.getText().toString().trim();
		boolean massResult = false;

		if(!errormass.equals("")){
			if((errormass.matches("[0-9]*")) || (errormass.matches("[0-9]*[.][0-9]*")) ){
				massResult = true;
			}else{
				new AlertDialog.Builder(Amphibian.this).setIcon(
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
			new AlertDialog.Builder(Amphibian.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Sex needs to be Chosen")
					.setNegativeButton("Ok", null)
					.show();
		}

		return sexResult;
	}
}

