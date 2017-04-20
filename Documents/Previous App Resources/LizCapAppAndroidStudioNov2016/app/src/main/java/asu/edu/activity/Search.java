package asu.edu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import asu.edu.R;

public class Search extends Activity{

	/**
	 * @param args
	 */
	private Button backButton;
	private Spinner siteSpinner;
	private Spinner arraySpinner;
	ArrayList<String> siteArray = new ArrayList<String>();
	ArrayList<String> theArray = new ArrayList<String>();
	ArrayList<String> speciesArray = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		fillArrays();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		setTitle("Search");
		
		this.backButton = (Button) this.findViewById(R.id.historyBackButton);
		//this.searchButton = (Button) this.findViewById(R.id.historySearch);
		//this.dateButton = (Button) this.findViewById(R.id.historyDateButton);
		//this.recorderText = (EditText) this.findViewById(R.id.historyRecorderText);
		//this.handlerText = (EditText) this.findViewById(R.id.historyHandlerText);
		this.siteSpinner = (Spinner) this.findViewById(R.id.historySiteSpinner);
		this.arraySpinner = (Spinner) this.findViewById(R.id.historyArraySpinner);
		//this.speciesText = (AutoCompleteTextView) this.findViewById(R.id.historySpeciesCodeAutoCompleteTextView);
		
		ArrayAdapter<String> siteAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, siteArray);
        siteSpinner.setAdapter(siteAdapter);
        
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, theArray);
        arraySpinner.setAdapter(arrayAdapter);
        
        this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		
	}

	private void fillArrays() {
		//Filling out my arrays for my Spinners
		siteArray.add("WG1");
		siteArray.add("WG2");
		siteArray.add("WG3");
		siteArray.add("WG4");
		
		theArray.add("1");
		theArray.add("2");
		
		//Need to add Species
	}

}
