package asu.edu.activity;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import asu.edu.R;
import asu.edu.database.JsonDBAdapter;

public class AnimalHistory extends Activity {
	private JsonDBAdapter db;
	private final int arthopodColumns = 20;
	private String checkId;
	private String site;
	private String array;
	private TableLayout tl_arthropodTable;
	private TableLayout tl_herpTable;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animalhistory);
		
		Bundle bundle = getIntent().getExtras();
		checkId = bundle.getString("checkId");
		site    = bundle.getString("site");
		array   = bundle.getString("array");

		tl_herpTable      = (TableLayout) findViewById(R.id.tl_aniHistoryHerpTable); 
		tl_arthropodTable = (TableLayout) findViewById(R.id.tl_aniHistoryArthropodTable);
		TextView tv_site  = (TextView) findViewById(R.id.tv_aniHistorySite);
		TextView tv_array = (TextView) findViewById(R.id.tv_aniHistoryArray);
		
		tl_herpTable.setStretchAllColumns(true);
		tl_arthropodTable.setStretchAllColumns(true);
		tv_site.setText(site);
		tv_array.setText(array);
		
		db = new JsonDBAdapter(this);
		db.open();
		
		setupArthropodTable();
		setupHerpTable();
		
		db.close();
	}
	
	private void setupArthropodTable () {
		boolean printedAtLeastOnce = false;
		Cursor cursor = db.fetchAllAnthropodCaptures();
		
		for (int i=0; i<cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			
			int checkIdIndex = cursor.getColumnIndexOrThrow(JsonDBAdapter.CHECKDATESID_TEXT);
			String currentCheckId = cursor.getString(checkIdIndex);
			
			// Checks if the row matches our checkdate
			if (currentCheckId.equals(checkId)) {
				
				// Iterate through each column
				for (int col=0; col<cursor.getColumnCount(); col++) {
					int arthopodsInThisColumn = cursor.getInt(col);
					
					// Checks if the column has some arthropods in it
					// TODO we need a better arthropod filter than this
					if (arthopodsInThisColumn > 0 && col < arthopodColumns) {
						printedAtLeastOnce = true;
						
						String columnName = cursor.getColumnName(col);

						TableRow tr = new TableRow(this);
						TextView tv_columnName = new TextView(this);
						TextView tv_numOfArthopods = new TextView(this);
						
						tv_columnName.setText(columnName);
						tv_numOfArthopods.setText(String.valueOf(arthopodsInThisColumn));
						
						tv_columnName.setGravity(Gravity.CENTER_HORIZONTAL);
						tv_numOfArthopods.setGravity(Gravity.CENTER_HORIZONTAL);
						
						tr.addView(tv_columnName);
						tr.addView(tv_numOfArthopods);
						
						tr.setOnTouchListener(new ArthropodTouchListener());
						tr.setOnLongClickListener(new ArthropodLongClickListener(columnName, arthopodsInThisColumn));
						
						tl_arthropodTable.addView(tr);
					}
				}
			}
		}
		
		if (!printedAtLeastOnce) {
			TextView tv_nothing = new TextView(this);
			tv_nothing.setText("No unsynced arthropod captures.");
			tl_arthropodTable.addView(tv_nothing);
		}
		
		cursor.close();
	}
	
	private void setupHerpTable () {
		boolean printedAtLeastOnce = false;
		Cursor cursor = db.fetchAllHerpcapture();
		
		for (int i=0; i<cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			
			int checkIdIndex = cursor.getColumnIndexOrThrow(JsonDBAdapter.CHECKDATESID_TEXT);
			String currentCheckId = cursor.getString(checkIdIndex);
			
			// Checks if the row matches our checkdate
			if (currentCheckId.equals(checkId)) {
				printedAtLeastOnce = true;
												
				int herpId = cursor.getColumnIndex(JsonDBAdapter.HERPSID_TEXT);
				int identifierIndex = cursor.getColumnIndex(JsonDBAdapter.IDENTIFIERS_TEXT);
				int sexIndex = cursor.getColumnIndex(JsonDBAdapter.SEX_TEXT);
				
				String speciesCode = getHerpSpeciesCode(cursor.getString(herpId));
				String sex = cursor.getString(sexIndex);
				String identifier = cursor.getString(identifierIndex);
				
				if (identifier == null) {
					identifier = "";
				}
				
				if (sex.equals("M")) {
					sex = "Male";
				}
				if (sex.equals("F")) {
					sex = "Female";
				}
				if (sex.equals("U")) {
					sex = "Unknown";
				}
				
				TableRow tr = new TableRow(this);
				
				TextView tv_speciesCode = new TextView(this);
				TextView tv_identifier  = new TextView(this);
				TextView tv_sex         = new TextView(this);
				
				tv_speciesCode.setText(speciesCode);
				tv_identifier.setText(identifier);
				tv_sex.setText(sex);
				
				tv_speciesCode.setGravity(Gravity.CENTER_HORIZONTAL);
				tv_identifier.setGravity(Gravity.CENTER_HORIZONTAL);
				tv_sex.setGravity(Gravity.CENTER_HORIZONTAL);
				
				tr.addView(tv_speciesCode);
				tr.addView(tv_identifier);
				tr.addView(tv_sex);
								
				tl_herpTable.addView(tr);
			}
		}
		
		if (!printedAtLeastOnce) {
			TextView tv_nothing = new TextView(this);
			tv_nothing.setText("No unsynced herp captures.");
			tl_herpTable.addView(tv_nothing);
		}
		
		cursor.close();
	}

	private String getHerpSpeciesCode (String herpId) {
		String speciesCode = null;
		
		Cursor cursor = db.fetchAllHerps();
		for (int i=0; i<cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			
			int herpIdIndex = cursor.getColumnIndex(JsonDBAdapter.HERPID_NUM);
			String currentHerpId = cursor.getString(herpIdIndex);
			
			// Checks if this row is the Herp we are looking for
			if (currentHerpId.equals(herpId)) {
				int speciesCodeIndex = cursor.getColumnIndex(JsonDBAdapter.SPPCODE_TEXT);
				speciesCode = cursor.getString(speciesCodeIndex);
			}
		}
		
		return speciesCode;
	}
	
	private class ArthropodLongClickListener implements OnLongClickListener {
		private final String speciesCode;
		private final int amount;
		ArthropodLongClickListener (String speciesCode, int amount) {
			this.speciesCode = speciesCode;
			this.amount = amount;
		}
		public boolean onLongClick (View v) {
//			Intent intent = new Intent(AnimalHistory.this, EditArthropod.class);
//			intent.putExtra("ArrayIndex", array);
//			intent.putExtra("Code", speciesCode);
//			intent.putExtra("Number", amount);
//			startActivity(intent);
////			startActivityForResult(intent, 1);
			return false;
		}
	}

	private class ArthropodTouchListener implements OnTouchListener {
		public boolean onTouch (View view, MotionEvent event) {
			if (event.getActionMasked () == MotionEvent.ACTION_DOWN) {
				view.setBackgroundColor(Color.GRAY);
			}
			if (event.getActionMasked() == MotionEvent.ACTION_UP) {
				view.setBackgroundColor(Color.WHITE);
			}
			return false;
		}
	}
	
//	@Override
//	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
//		if (requestCode == 1) {
//			String speciesCode = data.getStringExtra("Code");
//			String amountStr = data.getStringExtra("Number");
//			int amount = Integer.parseInt(amountStr);
//			updateArthropodEntry(speciesCode, amount);
//		}
//	}
//	
//	private void updateArthropodEntry (String speciesCode, int amount) {
//		db.open();
//		ContentValues updateValues = new ContentValues();
//		updateValues.put(speciesCode, amount);
//		db.update(JsonDBAdapter.ARTHROPODCAPTURE_TABLE, updateValues, JsonDBAdapter.ANTHROPODCAPTUEID_TEXT + "='" + checkId + "'", null);
//		db.close();
//		reload();
//	}
//	
//	public void reload () {
//	    Intent intent = getIntent();
//	    overridePendingTransition(0, 0);
//	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//	    finish();
//
//	    overridePendingTransition(0, 0);
//	    startActivity(intent);
//	}
}
