package asu.edu.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import asu.edu.R;

public class TaxaSelection extends Activity {
	private Button b_next;
	private Button b_noMoreAnimals;
	private Spinner sp_taxa;

	private Class<?> taxaChoice = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taxaselection);

		b_next          = (Button)  findViewById(R.id.taxaSelectionNextButton);
		b_noMoreAnimals = (Button)  findViewById(R.id.taxaSelectionNoMoreAnimalsButton);
		sp_taxa         = (Spinner) findViewById(R.id.sp_taxa);

		b_next.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (checkTaxa()) {
					Intent intent = new Intent(TaxaSelection.this, taxaChoice);
					startActivity(intent);
				}
				else {
					new AlertDialog.Builder(TaxaSelection.this)
							.setIcon(android.R.drawable.ic_dialog_alert)
							.setTitle("Error")
							.setMessage("Please select a taxa.")
							.setNegativeButton("Ok", null).show();
				}
			}
		});
		
		b_noMoreAnimals.setOnClickListener(new OnClickListener () {
			public void onClick(View v) {			
				Intent intent = new Intent(TaxaSelection.this, LocationComments.class);
				startActivity(intent);
			}
		});
		
		sp_taxa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 1:
					taxaChoice = Arthropod.class;
					break;
				case 2:
					taxaChoice = Amphibian.class;
					break;
				case 3:
					taxaChoice = HerpDataPartOne.class;
					break;
				case 4:
					taxaChoice = Mammal.class;
					break;
				case 5:
					taxaChoice = Snake.class;
					break;
				}
			}

			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private boolean checkTaxa () {
		String taxa = (String) sp_taxa.getSelectedItem();
		
		return !taxa.equals("Select");
	}
}
