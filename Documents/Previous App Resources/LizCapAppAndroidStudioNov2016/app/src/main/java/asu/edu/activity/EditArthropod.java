package asu.edu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import asu.edu.R;

public class EditArthropod extends Activity {
	private Button b_lookUpCode;
	private String array;
	private String speciesCode;
	private int numOfArthropods;
	private EditText et_numOfArthropods;
	private AutoCompleteTextView actv_speciesCodeText;
	private Button b_back;
	private Button b_update;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updatearthopoddata);
		
		Bundle bundle = getIntent().getExtras();
		array = bundle.getString("ArrayIndex");
		speciesCode = bundle.getString("Code");
		numOfArthropods = bundle.getInt("Number");
		
		b_lookUpCode = (Button) findViewById(R.id.arthropodSpeciesButton);
		b_back = (Button) findViewById(R.id.arthropodBackButton);
		b_update = (Button) findViewById(R.id.arthropodVerifyButton);
		et_numOfArthropods = (EditText) findViewById(R.id.arthropodNumberText);
		actv_speciesCodeText = (AutoCompleteTextView) findViewById(R.id.arthropodSpeciesCodeAutoCompleteTextView);
		
		b_lookUpCode.setVisibility(View.INVISIBLE);
		
		b_back.setOnClickListener(new OnClickListener () {
			public void onClick(View v) {
				finish();
			}
		});
		
		b_update.setOnClickListener(new UpdateButtonListener ());
		
		actv_speciesCodeText.setKeyListener(null);
		actv_speciesCodeText.setText(speciesCode);
		
		et_numOfArthropods.setText(String.valueOf(numOfArthropods));
		et_numOfArthropods.requestFocus();
	}
	
	private class UpdateButtonListener implements OnClickListener {
		public void onClick(View v) {
			// TODO
		}
	}
}
