package asu.edu.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import asu.edu.LizardApplication;
import asu.edu.R;
import asu.edu.database.Checkdate;

public class HerpDataPartTwo extends Activity {

	/**
	 * @param args
	 */
	private Button backButton;
	private Button nextButton;
	private TextView otlLabel;
	private EditText svlText;
	private EditText vtlText;
	private EditText massText;
	private EditText otlText;	
	private Spinner sexSpinner;
	private CheckBox regenCheckBox;
	private CheckBox hatchlingCheckBox;
	private Spinner sp_deadHdp2;
	private CheckBox otlCheckbox;
	private String speciesCode;
	private String toeclipCode;
	private String recap;
	private String fence;
	private String dead;
	private String hatchling;
	private String regen;
	private String otl;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.herpdatapart2);
		
		Bundle bundle = getIntent().getExtras();
        speciesCode= bundle.getString("SpeciesCode");
        recap = bundle.getString("Recapture");
        toeclipCode = bundle.getString("ToeClipCode");
        fence = bundle.getString("Fence");
		
		setTitle("Species Code: " + speciesCode);
		
		this.backButton = (Button) this.findViewById(R.id.herpdatatwoBackButton);
		this.nextButton = (Button) this.findViewById(R.id.herpdatatwoNextButton);
		//this.historyButton = (Button) this.findViewById(R.id.herpdatatwoHistoryButton);
		this.svlText = (EditText) this.findViewById(R.id.herpdatatwoSVLText);
		this.vtlText = (EditText) this.findViewById(R.id.herpdatatwoVTLText);
		this.massText = (EditText) this.findViewById(R.id.herpdatatwoMassText);
		this.otlText = (EditText) this.findViewById(R.id.herpdatatwoOTLText);		
		this.sexSpinner = (Spinner) this.findViewById(R.id.herpdatatwoSexSpinner);
		this.regenCheckBox = (CheckBox) this.findViewById(R.id.herpdatatwoRegenCheckBox);
		this.hatchlingCheckBox = (CheckBox) this.findViewById(R.id.herpdatatwoHatchlingCheckBox);
		this.sp_deadHdp2 = (Spinner) this.findViewById(R.id.sp_deadHdp2);
		this.otlCheckbox = (CheckBox) this.findViewById(R.id.herpdatatwoRegenCheckBox);
		this.otlLabel = (TextView) this.findViewById(R.id.herpdatatwoOTLLabel);
				
		otlLabel.setVisibility(View.INVISIBLE);
		otlText.setVisibility(View.INVISIBLE);

        this.otlCheckbox.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(otlCheckbox.isChecked()){
					otlLabel.setVisibility(View.VISIBLE);
					otlText.setVisibility(View.VISIBLE);
					vtlText.setNextFocusDownId(R.id.herpdatatwoOTLText);
				}
				else{
					otlLabel.setVisibility(View.INVISIBLE);
					otlText.setVisibility(View.INVISIBLE);
					vtlText.setNextFocusDownId(R.id.herpdatatwoMassText);
				}
			}
		});

        
        this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(HerpDataPartTwo.this).setIcon(
	                    android.R.drawable.ic_dialog_alert).setTitle("Exit")
	                    .setMessage("Information Entered will be Lost")
	                    .setNegativeButton("No", null)
	                    .setPositiveButton("Yes",
	                            new DialogInterface.OnClickListener() {
	                                public void onClick(DialogInterface dialog,
	                                        int which) {
	                                    // Exit the activity
	                                    HerpDataPartTwo.this.finish();
	                                }
	                            }).show();
			}
		});
		
		this.nextButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				boolean regenCheck = regenCheckBox.isChecked();				
				if(regenCheck){
					regen = "Yes";
					otl = otlText.getText().toString();					
				}else{
					regen = "No";
					String otl = "";
					otlText.setText(otl);
				}
				
				dead = sp_deadHdp2.getSelectedItem().toString().trim();
				
				if (dead.equals("Live")) {
					dead = "No";
				}
				else {
					dead = "Yes";
				}
				
				boolean hatchlingCheck = hatchlingCheckBox.isChecked();
				if(hatchlingCheck){
					hatchling = "Yes";
				}else{
					hatchling = "No";
				}
				
				boolean checkForErrors = errorCheck();
				if(checkForErrors){
					Intent intent = new Intent(HerpDataPartTwo.this, HerpDataPartThree.class);
					Bundle bundle = new Bundle();
	        		bundle.putString("SpeciesCode", speciesCode);
	        		bundle.putString("Recapture", recap);
	        		bundle.putString("ToeClipCode", toeclipCode);
	        		bundle.putString("Fence", fence);
	        		bundle.putString("SVL", svlText.getText().toString());
	        		bundle.putString("VTL", vtlText.getText().toString());
	        		bundle.putString("Mass", massText.getText().toString());
	        		bundle.putString("Hatchling", hatchling);
	        		bundle.putString("OTL", otl);
	        		bundle.putString("Sex", (String) sexSpinner.getSelectedItem());
	        		bundle.putString("Dead", dead);
	        		bundle.putString("Regen", regen);
	        		intent.putExtras(bundle);
					HerpDataPartTwo.this.startActivity(intent);
				}
			}
		});
		
		/*this.historyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent historyIntent = new Intent(HerpDataPartTwo.this, History.class);
				HerpDataPartTwo.this.startActivity(historyIntent);
			}
		});*/
	}
	
	private boolean errorCheck(){
		boolean finalCheck = false;
		
		if(svlCheck() && vtlCheck() && massCheck() && otlCheck() && sexCheck()){
			String tempVTL = vtlText.getText().toString().trim();
			String tempOTL = otlText.getText().toString().trim();
					
			if(tempVTL.equals("") || tempOTL.equals("")){
				finalCheck = true;
			}
			else{
				int tempV = Integer.parseInt(tempVTL);
				int tempO = Integer.parseInt(tempOTL);			
				
				if(tempO > tempV){
					finalCheck = false;
					new AlertDialog.Builder(HerpDataPartTwo.this).setIcon(
		                    android.R.drawable.ic_dialog_alert).setTitle("Error")
		                    .setMessage("OTL can Not be bigger than VTL")
		                    .setNegativeButton("Ok", null)
		                    .show();
				}else{
					finalCheck = true;
				}
			}		
		}else{
				finalCheck = false;
		}
		
		return finalCheck;
	}
	
	private boolean svlCheck(){
		String errorsvl = svlText.getText().toString().trim();
		boolean svlResult = false;
		
		if(!errorsvl.equals("")){
			if(errorsvl.matches("[0-9]*")){
				svlResult = true;
			}else{
				new AlertDialog.Builder(HerpDataPartTwo.this).setIcon(
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
				new AlertDialog.Builder(HerpDataPartTwo.this).setIcon(
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
				new AlertDialog.Builder(HerpDataPartTwo.this).setIcon(
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
	
	private boolean otlCheck(){
		String errorotl = otlText.getText().toString().trim();
		boolean otlResult = false;
		
		if(!errorotl.equals("")){
			if(errorotl.matches("[0-9]*")){
				otlResult = true;
			}else{
				new AlertDialog.Builder(HerpDataPartTwo.this).setIcon(
	                    android.R.drawable.ic_dialog_alert).setTitle("Error")
	                    .setMessage("OTL needs to be a number or blank")
	                    .setNegativeButton("Ok", null)
	                    .show();
			}
		}else{
			otlResult = true;
		}
		
		return otlResult;
	}
	
	private boolean sexCheck(){
		String errorsex = sexSpinner.getSelectedItem().toString().trim();
		boolean sexResult = false;
		
		if(!errorsex.equals("Select")){
			sexResult = true;
		}else{
			sexResult = false;
			new AlertDialog.Builder(HerpDataPartTwo.this).setIcon(
                    android.R.drawable.ic_dialog_alert).setTitle("Error")
                    .setMessage("Sex needs to be Chosen")
                    .setNegativeButton("Ok", null)
                    .show();
		}
		
		return sexResult;
	}
}
