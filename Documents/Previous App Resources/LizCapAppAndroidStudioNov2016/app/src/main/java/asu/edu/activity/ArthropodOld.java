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
import android.widget.EditText;
import asu.edu.R;
import asu.edu.database.JsonDBAdapter;

public class ArthropodOld extends Activity {

	/**
	 * @param args
	 */
	private Button backButton;
	private Button verifyButton;
	private Button codeButton;
	private AutoCompleteTextView speciesCodeText;
	private EditText numberText;
	private String code;
	private String number;
	private String arrayIndex;
	private final ArrayList<String> codeArthropod = new ArrayList<String>();
	private final ArrayList<String> nameArthropod = new ArrayList<String>();
	private final ArrayList<String> descriptionArthropod = new ArrayList<String>();
	private Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatearthopoddata);
		setTitle("Update Arthropod");

		this.backButton = (Button) this.findViewById(R.id.arthropodBackButton);
		this.verifyButton = (Button) this.findViewById(R.id.arthropodVerifyButton);
		//this.historyButton = (Button) this.findViewById(R.id.arthropodHistoryButton);
		this.codeButton = (Button) this.findViewById(R.id.arthropodSpeciesButton);
		this.numberText = (EditText) this.findViewById(R.id.arthropodNumberText);
		this.speciesCodeText = (AutoCompleteTextView) this.findViewById(R.id.arthropodSpeciesCodeAutoCompleteTextView);

		intent = getIntent();
		arrayIndex = intent.getStringExtra("ArrayIndex");
		code = intent.getStringExtra("Code");
		number = intent.getStringExtra("Number");

		speciesCodeText.setText(code);
		numberText.setText(number);

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

		ArrayAdapter<String> adapterArthropod = new ArrayAdapter<String>(ArthropodOld.this, 
				android.R.layout.simple_dropdown_item_1line, codeArthropod);

		speciesCodeText.setAdapter(adapterArthropod);

		this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(ArthropodOld.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Exit")
						.setMessage("Information Entered will be Lost")
						.setNegativeButton("No", null)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Exit the activity
								ArthropodOld.this.finish();
							}
						}).show();
			}
		});

		this.verifyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				boolean testOnErrors = errorCheck();
				if(testOnErrors){
					intent.putExtra("ArrayNumber", arrayIndex);
					intent.putExtra("Code", speciesCodeText.getText().toString());
					intent.putExtra("Number", numberText.getText().toString());
					setResult(RESULT_OK, intent);
					finish(); 

				}		
			}
		});

		this.codeButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent arthropodIntent = new Intent(ArthropodOld.this, SpeciesList.class);
				arthropodIntent.putStringArrayListExtra("Code", codeArthropod);
				arthropodIntent.putStringArrayListExtra("Genus", nameArthropod);
				arthropodIntent.putStringArrayListExtra("Species", descriptionArthropod);				
				startActivityForResult(arthropodIntent, 1);
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
			new AlertDialog.Builder(ArthropodOld.this).setIcon(
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
				new AlertDialog.Builder(ArthropodOld.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Error")
						.setMessage("Number needs to be a digit or set it to zero")
						.setNegativeButton("Ok", null)
						.show();
			}
		}else{
			numberResult = false;
			new AlertDialog.Builder(ArthropodOld.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Number can not be blank. Set it to zero or greater")
					.setNegativeButton("Ok", null)
					.show();
		}

		return numberResult;
	}

}