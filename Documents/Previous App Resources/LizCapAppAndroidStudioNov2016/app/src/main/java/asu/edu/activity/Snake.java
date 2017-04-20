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

public class Snake extends Activity {

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
	private EditText vtlText;
	private EditText svlText;
	private EditText massText;
	private String dead;
	private final ArrayList<String> codeSnake = new ArrayList<String>();
	private final ArrayList<String> nameSnake = new ArrayList<String>();
	private final ArrayList<String> descriptionSnake = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.snake);
		setTitle("Snake Data");

		this.backButton = (Button) this.findViewById(R.id.snakeBackButton);
		this.verifyButton = (Button) this.findViewById(R.id.snakeVerifyButton);
		//this.historyButton = (Button) this.findViewById(R.id.snakeHistoryButton);
		this.codeButton = (Button) this.findViewById(R.id.snakeSpeciesButton);
		this.fenceButton = (Button) this.findViewById(R.id.snakeMapButton);
		this.deadCheckBox = (CheckBox) this.findViewById(R.id.snakeDeadCheckBox);
		this.sexSpinner = (Spinner) this.findViewById(R.id.snakeSexSpinner);
		this.fenceSpinner = (Spinner) this.findViewById(R.id.snakeFenceSpinner);
		this.commentsText = (EditText) this.findViewById(R.id.snakeCommentsText);
		this.svlText = (EditText) this.findViewById(R.id.snakeSVLText);
		this.vtlText = (EditText) this.findViewById(R.id.snakeVTLText);
		this.massText = (EditText) this.findViewById(R.id.snakeMassText);
		this.speciesCodeText = (AutoCompleteTextView) this.findViewById(R.id.snakeSpeciesCodeAutoCompleteTextView);

		JsonDBAdapter jsonDBHelper = new JsonDBAdapter(this);
		jsonDBHelper.open();

		Cursor herpsCursor = jsonDBHelper.fetchSelectHerps("Snake");
		while(herpsCursor.moveToNext()){
			codeSnake.add(herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.SPPCODE_TEXT)));
			nameSnake.add(herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.GENUS_TEXT)));
			descriptionSnake.add(herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.SPECIES_TEXT)));
		}
		herpsCursor.close();

		jsonDBHelper.close();

		ArrayAdapter<String> adapterSnake = new ArrayAdapter<String>(Snake.this, 
				android.R.layout.simple_dropdown_item_1line, codeSnake);

		speciesCodeText.setAdapter(adapterSnake);

		this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(Snake.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Exit")
						.setMessage("Information Entered will be Lost")
						.setNegativeButton("No", null)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Exit the activity
								Snake.this.finish();
							}
						}).show();
			}
		});

		this.verifyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				boolean checkForErrors = errorCheck();
				if(checkForErrors){
					boolean deadCheck = deadCheckBox.isChecked();
					if(deadCheck){
						dead = "Yes";
					}else{
						dead = "No";
					}

					Intent amphibianIntent = new Intent(Snake.this, VerifySnake.class);

					Bundle bundle = new Bundle();
					bundle.putString("SpeciesCode", speciesCodeText.getText().toString());
					bundle.putString("Fence", (String) fenceSpinner.getSelectedItem());
					bundle.putString("SVL", svlText.getText().toString());
					bundle.putString("VTL", vtlText.getText().toString());
					bundle.putString("Mass", massText.getText().toString());
					bundle.putString("Sex", (String) sexSpinner.getSelectedItem());
					bundle.putString("Dead", dead);
					bundle.putString("Comments", commentsText.getText().toString());
					bundle.putString("TrapClosed", null);
					amphibianIntent.putExtras(bundle);
					startActivity(amphibianIntent);
				}		
			}
		});

		/*this.historyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent historyIntent = new Intent(Snake.this, History.class);
				Snake.this.startActivity(historyIntent);
			}
		});*/

		this.codeButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent snakeIntent = new Intent(Snake.this, SpeciesList.class);
				snakeIntent.putStringArrayListExtra("Code", codeSnake);
				snakeIntent.putStringArrayListExtra("Genus", nameSnake);
				snakeIntent.putStringArrayListExtra("Species", descriptionSnake);				
				startActivityForResult(snakeIntent, 1);
			}
		});

		this.fenceButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent fenceIntent = new Intent(Snake.this, FenceArray.class);							
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

		if(codeCheck() && svlCheck() && vtlCheck() && massCheck() && sexCheck()){
			finalCheck = true;
		}
		return finalCheck;
	}

	private boolean codeCheck(){
		String tempUserInput = speciesCodeText.getText().toString().trim();
		boolean codeResult = false;

		if(codeSnake.contains(tempUserInput) && !tempUserInput.equals("")){
			codeResult = true;										
		}else{
			new AlertDialog.Builder(Snake.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Species Code must have 4 letter existing code.\n\nRefer to the 'Look up Code' button for a list of species.")
					.setNegativeButton("Ok", null)
					.show();
		}
		return codeResult;
	}

	private boolean svlCheck(){
		String errorsvl = svlText.getText().toString().trim();
		boolean svlResult = false;

		if(!errorsvl.equals("")){
			if(errorsvl.matches("[0-9]*")){
				svlResult = true;
			}else{
				new AlertDialog.Builder(Snake.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Error")
						.setMessage("SVL needs to be a number or blank")
						.setNegativeButton("Ok", null)
						.show();
			}
		}else{
			svlResult = true;
		}

		return svlResult;
	}

	private boolean vtlCheck(){
		String errorvtl = vtlText.getText().toString().trim();
		boolean vtlResult = false;

		if(!errorvtl.equals("")){
			if(errorvtl.matches("[0-9]*")){
				vtlResult = true;
			}else{
				new AlertDialog.Builder(Snake.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Error")
						.setMessage("VTL needs to be a number or blank")
						.setNegativeButton("Ok", null)
						.show();
			}
		}else{
			vtlResult = true;
		}

		return vtlResult;
	}

	private boolean massCheck(){
		String errormass = massText.getText().toString().trim();
		boolean massResult = false;

		if(!errormass.equals("")){
			if((errormass.matches("[0-9]*")) || (errormass.matches("[0-9]*[.][0-9]*")) ){
				massResult = true;
			}else{
				new AlertDialog.Builder(Snake.this).setIcon(
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
			new AlertDialog.Builder(Snake.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Sex needs to be Chosen")
					.setNegativeButton("Ok", null)
					.show();
		}

		return sexResult;
	}
}