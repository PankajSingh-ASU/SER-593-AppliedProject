package asu.edu.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import asu.edu.R;

public class HerpDataPartThree extends Activity {

	/**
	 * @param args
	 */
	private Button backButton;
	private Button verifyButton;
	private EditText commentsText;
	private String speciesCode;
	private String toeclipCode;
	private String recap;
	private String fence;
	private String svl;
	private String vtl;
	private String mass;
	private String hatchling;
	private String otl;
	private String sex;
	private String dead;
	private String regen;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.herpdatapart3);
		setTitle("Lizard Data");
		
		this.backButton = (Button) this.findViewById(R.id.herpdatathreeBackButton);
		this.verifyButton = (Button) this.findViewById(R.id.herpdatathreeVerifyButton);
		//this.historyButton = (Button) this.findViewById(R.id.herpdatathreeHistoryButton);
		this.commentsText = (EditText) this.findViewById(R.id.herpdatathreeCommentText);
		
		Bundle bundle = getIntent().getExtras();
        speciesCode = bundle.getString("SpeciesCode");
        recap = bundle.getString("Recapture");
        toeclipCode = bundle.getString("ToeClipCode");
        fence = bundle.getString("Fence");
        svl = bundle.getString("SVL");
        vtl = bundle.getString("VTL");
        mass = bundle.getString("Mass");
        hatchling = bundle.getString("Hatchling");
        otl = bundle.getString("OTL");
        sex = bundle.getString("Sex");
        dead = bundle.getString("Dead");
        regen = bundle.getString("Regen");
        
        this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(HerpDataPartThree.this).setIcon(
	                    android.R.drawable.ic_dialog_alert).setTitle("Exit")
	                    .setMessage("Information Entered will be Lost")
	                    .setNegativeButton("No", null)
	                    .setPositiveButton("Yes",
	                            new DialogInterface.OnClickListener() {
	                                public void onClick(DialogInterface dialog,
	                                        int which) {
	                                    // Exit the activity
	                                    HerpDataPartThree.this.finish();
	                                }
	                            }).show();
			}
		});
		
		this.verifyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent herpDataTwoIntent = new Intent(HerpDataPartThree.this, VerifyHerp.class);
				Bundle bundle = new Bundle();
				bundle.putString("SpeciesCode", speciesCode);
				bundle.putString("ToeClipCode", toeclipCode);       		        	        		
        		bundle.putString("Recapture", recap);        		
        		bundle.putString("Fence", fence);
        		bundle.putString("SVL", svl);
        		bundle.putString("VTL", vtl);
        		bundle.putString("Mass", mass);
        		bundle.putString("Hatchling", hatchling);
        		bundle.putString("OTL", otl);        	
        		bundle.putString("Sex", sex);
        		bundle.putString("Dead", dead);
        		bundle.putString("Regen", regen);
        		bundle.putString("Comments", commentsText.getText().toString());
        		herpDataTwoIntent.putExtras(bundle);
				HerpDataPartThree.this.startActivity(herpDataTwoIntent);
			}
		});
	}
}
