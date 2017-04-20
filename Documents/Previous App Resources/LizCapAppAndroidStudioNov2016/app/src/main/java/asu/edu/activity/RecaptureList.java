package asu.edu.activity;


import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import asu.edu.LizardApplication;
import asu.edu.R;
import asu.edu.database.Checkdate;
import asu.edu.database.JsonDBAdapter;

public class RecaptureList extends Activity {

	/*	 private class EfficientAdapter extends BaseAdapter {
	 private LayoutInflater mInflater;

	 public EfficientAdapter(Context context) {
	 mInflater = LayoutInflater.from(context);

}

public int getCount() {
	 return lizardData.size();
}

public Object getItem(int position) {
	 return position;
}

public long getItemId(int position) {
	 return position;
}

public View getView(int position, View convertView, ViewGroup parent) {
	 ViewHolder holder;
	 if (convertView == null) {
	 convertView = mInflater.inflate(R.layout.recapturelistview, null);
	 holder = new ViewHolder();
	 holder.codeText = (TextView) convertView.findViewById(R.id.recaplistviewDateLabel);

	 convertView.setTag(holder);
	 } else {
	 holder = (ViewHolder) convertView.getTag();
	 }

	 holder.codeText.setText(lizardData.get(position));

	 return convertView;
}

class ViewHolder {
TextView codeText;
}
}
	 */
	private ArrayList<String> lizardDate = new ArrayList<String>();
	private ArrayList<String> lizardArray = new ArrayList<String>();
	private ArrayList<String> lizardSVL = new ArrayList<String>();
	private ArrayList<String> lizardVTL = new ArrayList<String>();
	private ArrayList<String> lizardOTL = new ArrayList<String>();
	private ArrayList<String> lizardMass = new ArrayList<String>();
	private ArrayList<String> lizardSex = new ArrayList<String>();
	private ArrayList<String> checkIndividual = new ArrayList<String>();
	private String speciesCode;
	private String toeclipCode;
	private TextView toeclipCodeText;
	private TextView speciesCodeText;
	private TextView siteCodeText;
	private TableLayout container;
	private TableRow row;
	private TableRow row2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lizardhistory);
		setTitle("Lizard History");

		container = (TableLayout)findViewById(R.id.lizardhistoryLayout);

		Bundle bundle = getIntent().getExtras();	 
		speciesCode = bundle.getString("SpeciesCode");
		toeclipCode = bundle.getString("ToeClipCode");
		
		LizardApplication app = (LizardApplication) getApplicationContext();
		Checkdate checkdateobj = app.getCheckdateObj();
		
		String site = checkdateobj.site;
		//svl = bundle.getString("SVL");
		//vtl = bundle.getString("VTL");
		//otl = bundle.getString("OTL");
		//mass = bundle.getString("Mass");               
		//sex = bundle.getString("Sex");

		this.toeclipCodeText = (TextView) this.findViewById(R.id.lizardhistoryToeClipCodeLabel);
		this.siteCodeText = (TextView) this.findViewById(R.id.lizardhistorySiteCodeLabel);
		this.speciesCodeText = (TextView) this.findViewById(R.id.lizardhistorySpeciesCodeLabel);

		toeclipCodeText.setText(toeclipCode);
		siteCodeText.setText(site);
		speciesCodeText.setText(speciesCode);
		JsonDBAdapter jsonDBHelper = new JsonDBAdapter(this);
		jsonDBHelper.open();

		Cursor querySite = jsonDBHelper.rawQuery("SELECT Date, ArrayName as Array, SVL, VTL, OTL, Mass, Sex, Notes FROM (((Locations INNER JOIN CheckDates ON LocationID = Location_LocationID) INNER JOIN HerpCapture ON CheckdateID = CheckDates_CheckdateID) INNER JOIN Herps ON HerpID = Herps_HerpID) WHERE Identifiers = '"+ toeclipCode + "' AND SiteName = '"+ site +"' AND Spp_Code = '" + speciesCode + "' ORDER BY cast(substr(CheckdateID, -10, 4) as Integer)", null);

		while(querySite.moveToNext()){
			lizardDate.add(querySite.getString(0));
			lizardArray.add(querySite.getString(1));
			lizardSVL.add(querySite.getString(2));
			lizardVTL.add(querySite.getString(3));
			lizardOTL.add(querySite.getString(4));
			lizardMass.add(querySite.getString(5));
			lizardSex.add(querySite.getString(6));
			checkIndividual.add(querySite.getString(7));
		}
		querySite.close();
		jsonDBHelper.close();

		boolean check = false;

		for (String wordsearch : checkIndividual) {
			if(wordsearch != null){
				if(wordsearch.contains("Individual")){
					check = true;
				} 
			}

		}

		if(check){
			new AlertDialog.Builder(RecaptureList.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Alert")
					.setMessage("Correction: Animal needs to be re-clipped")
					.setNegativeButton("OK", null)
					.show();
		}

		for (int i = 0; i < lizardDate.size(); i++) {
			TableLayout.LayoutParams layoutParms = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT);
			layoutParms.gravity = Gravity.CENTER_HORIZONTAL;
			row = new TableRow(this);

			TextView date = new TextView(this);
			date.setTextColor(Color.BLACK);
			date.setGravity(Gravity.CENTER);
			date.setTextSize(TypedValue.COMPLEX_UNIT_PX, 18);
			date.setTypeface(null, 1);
			date.setPadding(20, 0, 20, 0);
			if(lizardDate.get(i) == null){
				date.append("        ");
			}else if(lizardDate.get(i).equals("")){
				date.append("");
			}else{
				date.append(lizardDate.get(i));
			}

			TextView array = new TextView(this);
			array.setTextColor(Color.BLACK);
			array.setGravity(Gravity.CENTER);
			array.setTextSize(TypedValue.COMPLEX_UNIT_PX, 18);
			array.setTypeface(null, 1);
			array.setPadding(20, 0, 20, 0);
			if(lizardArray.get(i) == null){
				array.append(" ");
			}else if(lizardArray.get(i).equals("")){
				array.append("");
			}else{
				array.append(lizardArray.get(i));
			}

			TextView svl = new TextView(this);
			svl.setTextColor(Color.BLACK);
			svl.setGravity(Gravity.CENTER);
			svl.setTextSize(TypedValue.COMPLEX_UNIT_PX, 18);
			svl.setTypeface(null, 1);
			svl.setPadding(30, 0, 30, 0);
			if(lizardSVL.get(i) == null){
				svl.append("   ");
			}else if(lizardSVL.get(i).equals("")){
				svl.append("");
			}else{
				svl.append(lizardSVL.get(i)); 
			}


			TextView vtl = new TextView(this);
			vtl.setTextColor(Color.BLACK);
			vtl.setGravity(Gravity.CENTER);
			vtl.setTextSize(TypedValue.COMPLEX_UNIT_PX, 18);
			vtl.setTypeface(null, 1);
			vtl.setPadding(1, 0, 1, 0);
			if(lizardVTL.get(i) == null){
				vtl.append("   ");
			}else if(lizardVTL.get(i).equals("")){
				vtl.append("");
			}else{
				vtl.append(lizardVTL.get(i));
			}

			TextView otl = new TextView(this);
			otl.setTextColor(Color.BLACK);
			otl.setGravity(Gravity.CENTER);
			otl.setTextSize(TypedValue.COMPLEX_UNIT_PX, 18);
			otl.setTypeface(null, 1);
			otl.setPadding(30, 0, 30, 0);
			if(lizardOTL.get(i) == null){
				otl.append("   ");
			}else if(lizardOTL.get(i).equals("")){
				otl.append("");
			}else{
				otl.append(lizardOTL.get(i));
			}

			TextView mass = new TextView(this);
			mass.setTextColor(Color.BLACK);
			mass.setGravity(Gravity.CENTER);
			mass.setTextSize(TypedValue.COMPLEX_UNIT_PX, 18);
			mass.setTypeface(null, 1);
			mass.setPadding(2, 0, 2, 0);
			if(lizardMass.get(i) == null){
				mass.append("    ");
			}else if(lizardMass.get(i).equals("")){
				mass.append("");
			}else{
				mass.append(lizardMass.get(i));
			}

			TextView sex = new TextView(this);
			sex.setTextColor(Color.BLACK);
			sex.setGravity(Gravity.CENTER);
			sex.setTextSize(TypedValue.COMPLEX_UNIT_PX, 18);
			sex.setTypeface(null, 1);
			sex.setPadding(40, 0, 20, 0);
			if(lizardSex.get(i) == null){
				sex.append(" ");
			}else if(lizardSex.get(i).equals("")){
				sex.append("");
			}else{
				sex.append(lizardSex.get(i));
			}

			TextView notes = new TextView(this);
			notes.setTextColor(Color.BLACK);
			notes.setGravity(Gravity.LEFT);
			notes.setTextSize(TypedValue.COMPLEX_UNIT_PX, 18);
			notes.setTypeface(null, 1);
			notes.setPadding(20, 0, 20, 0);
			if(checkIndividual.get(i) == null){
				notes.append("   ");
			}else if(checkIndividual.get(i).equals("")){
				notes.append("");
			}else{
				notes.append(checkIndividual.get(i));
			}

			row.addView(date);
			row.addView(array);
			row.addView(svl);
			row.addView(vtl);
			row.addView(otl);
			row.addView(mass);
			row.addView(sex);
			row.addView(notes);
			container.addView(row, layoutParms);
		}

		row2 = new TableRow(this);
		Button backButton = new Button(this);
		backButton.append("Back");


		OnClickListener btnLtlstener  = new Button.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		};

		backButton.setOnClickListener(btnLtlstener);
		row2.addView(backButton);
		container.addView(row2, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));

		/*}


	  ListView speciesList = (ListView) findViewById(R.id.recaptureListView);

	 speciesList.setAdapter(new EfficientAdapter(this));

	 speciesList.setOnItemClickListener(new OnItemClickListener() {
		 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {			
	        finish(); 
		 }
	 });

		 */
	}

}