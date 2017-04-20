package asu.edu.activity;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import asu.edu.DateTimeSlider;
import asu.edu.LizardApplication;
import asu.edu.R;
import asu.edu.database.Checkdate;
import asu.edu.database.HerpEntry;
import asu.edu.database.JsonDBAdapter;
import asu.edu.dialog.DateSlider;
import asu.edu.labeler.TimeLabeler;

public class VerifyHerp extends Activity {
	/**
	 * @param args
	 */
	private TextView toeclipCodeText;
	private TextView speciesCodeText;
	private TextView fenceText;	
	private TextView svlText;
	private TextView vtlText;
	private TextView otlText;
	private TextView massText;
	private TextView hatchlingText;
	private TextView sexText;
	private TextView deadText;
	private TextView commentsText;
	private Button editButton;
	private Button backButton;
	private Button correctButton;
	private Button historyButton;
	private String combo1;
	private String combo2;
	private String datetime;
	private String time;
	private String date;
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
	private String comments;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verifyherp);

		this.backButton = (Button) this.findViewById(R.id.verifyherpBackButton);
		this.correctButton = (Button) this.findViewById(R.id.verifyherpCorrectButton);
		this.toeclipCodeText = (TextView) this.findViewById(R.id.verifyherpToeText);
		this.speciesCodeText = (TextView) this.findViewById(R.id.verifyherpSpeciesCodeText);
		this.fenceText = (TextView) this.findViewById(R.id.verifyherpFenceText);
		this.svlText = (TextView) this.findViewById(R.id.verifyherpSVLText);
		this.vtlText = (TextView) this.findViewById(R.id.verifyherpVTLText);
		this.otlText = (TextView) this.findViewById(R.id.verifyherpOTLText);
		this.massText = (TextView) this.findViewById(R.id.verifyherpMassText);
		this.hatchlingText = (TextView) this.findViewById(R.id.verifyherpHatchlingText);
		this.sexText = (TextView) this.findViewById(R.id.verifyherpSexText);
		this.deadText = (TextView) this.findViewById(R.id.verifyherpDeadText);
		this.commentsText = (TextView) this.findViewById(R.id.verifyherpCommentsText);
		this.editButton = (Button) this.findViewById(R.id.verifyherpEditButton);
		this.historyButton = (Button) this.findViewById(R.id.verifyherpHistoryButton);

		Bundle bundle = getIntent().getExtras();
		speciesCode = bundle.getString("SpeciesCode");
		toeclipCode = bundle.getString("ToeClipCode");
		recap = bundle.getString("Recapture");        
		fence = bundle.getString("Fence");
		svl = bundle.getString("SVL");
		vtl = bundle.getString("VTL");
		otl = bundle.getString("OTL");
		mass = bundle.getString("Mass");               
		hatchling = bundle.getString("Hatchling");
		sex = bundle.getString("Sex");
		dead = bundle.getString("Dead");
		comments = bundle.getString("Comments");
		
		LizardApplication app = (LizardApplication) getApplicationContext();
		Checkdate checkdateobj = app.getCheckdateObj();
		
		setTitle("Site: " + checkdateobj.site + "      Array: " + checkdateobj.array);

		toeclipCodeText.setText(toeclipCode);
		speciesCodeText.setText(speciesCode);
		fenceText.setText(fence);
		svlText.setText(svl);
		vtlText.setText(vtl);
		otlText.setText(otl);
		massText.setText(mass);
		hatchlingText.setText(hatchling);
		sexText.setText(sex);
		deadText.setText(dead);
		commentsText.setText(comments);

		if(recap.equals("Yes")){
			historyButton.setVisibility(View.VISIBLE);			
		}else{
			historyButton.setVisibility(View.GONE);			
		}

		date = (String) android.text.format.DateFormat.format("MM/dd/yyyy", new java.util.Date());
		time = (String) android.text.format.DateFormat.format("hh:mm", new java.util.Date());
		combo1 = (String) android.text.format.DateFormat.format("MMddyy", new java.util.Date());
		combo2 = (String) android.text.format.DateFormat.format("hhmm", new java.util.Date());

		datetime = combo1 + combo2;

		editButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// call the internal showDialog method using the predefined ID
				showDialog(5);
			}
		});

		historyButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent historyIntent = new Intent(VerifyHerp.this, RecaptureList.class);
				Bundle bundle = new Bundle();
				bundle.putString("SpeciesCode", speciesCode);
				bundle.putString("ToeClipCode", toeclipCode);
				bundle.putString("SVL", svl);
				bundle.putString("VTL", vtl);
				bundle.putString("Mass", mass);
				bundle.putString("OTL", otl);        	
				bundle.putString("Sex", sex);
				historyIntent.putExtras(bundle);
				VerifyHerp.this.startActivity(historyIntent);
			}
		});

		this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(VerifyHerp.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Exit")
						.setMessage("Information Entered will be Lost")
						.setNegativeButton("No", null)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Exit the activity
								VerifyHerp.this.finish();
							}
						}).show();
			}
		});

		this.correctButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				callDialog();
			}

			private void callDialog() {
				new AlertDialog.Builder(VerifyHerp.this)
				.setTitle("Choice")
				.setMessage("Do you want to enter another animal?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						storeData();

						Intent homeIntent = new Intent(VerifyHerp.this, TaxaSelection.class);
						homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Bundle bundle = new Bundle();
						homeIntent.putExtras(bundle);
						VerifyHerp.this.startActivity(homeIntent);
						dialog.cancel();
					}

				})

				.setNeutralButton("No", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						storeData();

						Intent intent = new Intent(VerifyHerp.this, LocationComments.class);
						VerifyHerp.this.startActivity(intent);
						dialog.cancel();
					}
				})

				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}

				}).show();
			}
		});
	}

	// define the listener which is called once a user selected the date.
	private DateSlider.OnDateSetListener mDateTimeSetListener =
		new DateSlider.OnDateSetListener() {
		public void onDateSet(DateSlider view, Calendar selectedDate) {
			// update the dateText view with the corresponding date
			int minute = selectedDate.get(Calendar.MINUTE) /
			TimeLabeler.MINUTEINTERVAL*TimeLabeler.MINUTEINTERVAL;
			int month = selectedDate.get(Calendar.MONTH)+1;
			int day = selectedDate.get(Calendar.DAY_OF_MONTH);
			int year = selectedDate.get(Calendar.YEAR);
			int hour = selectedDate.get(Calendar.HOUR);
			String newmonth = Integer.toString(month);
			String newday = Integer.toString(day);
			String newhour = Integer.toString(hour);
			String newminute = Integer.toString(minute);

			if(hour == 0){
				hour = 12;
			}
			if(newhour.length() < 2){
				newhour = "0" + newhour;
			}
			if(newminute.length() < 2){
				newminute = "0" + newminute;
			}
			if(newmonth.length() < 2){
				newmonth = "0" + newmonth;
			}
			if(newday.length() < 2){
				newday = "0" + newday;
			}
			String changeyear = Integer.toString(year);
			String newyear = changeyear.substring(2);
			String date1 = newmonth + "/" + newday + "/" + year;
			String time1 = newhour + ":" + newminute;
			String date2 = newmonth + newday + newyear;
			String time2 = newhour + newminute;    

			date = date1;
			time = time1;
			combo1 = date2;
			combo2 = time2;

			datetime = combo1 + combo2;
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		// this method is called after invoking 'showDialog' for the first time
		// here we initiate the corresponding DateSlideSelector and return the dialog to its caller

		// get today's date and time
		final Calendar c = Calendar.getInstance();

		switch (id) {
		case 5:
			return new DateTimeSlider(this,mDateTimeSetListener,c);
		}
		return null;
	}

	private void storeData() {
		JsonDBAdapter dbHelperJson = new JsonDBAdapter(this);
		dbHelperJson.open();

		String herpID = null;

		if ((!speciesCode.equals("")) || (!speciesCode.equals(null))) {
			Cursor queryHerpID = dbHelperJson.rawQuery(
					"select * from herps where Spp_Code = '" + speciesCode
					+ "'", null);

			while (queryHerpID.moveToNext()) {
				herpID = queryHerpID.getString(0);
			}
			queryHerpID.close();
		} else {
			herpID = "";
		}

		if (svl.compareTo("") == 0){
			svl = "0";
		}
		if (mass.compareTo("") == 0){
			mass = "0";
		}
		if (vtl.compareTo("") == 0){
			vtl = "0";
		}
		if (otl == null) {
			otl = "0";
		}
		
		HerpEntry herp =
				new HerpEntry(fence, Character.toString(recap.charAt(0)), Integer.valueOf(svl), Integer.valueOf(vtl),
							  Integer.valueOf(otl), Float.valueOf(mass), Character.toString(sex.charAt(0)),
							  Character.toString(dead.charAt(0)), Character.toString(hatchling.charAt(0)),
							  comments, herpID, toeclipCode);
		
		LizardApplication app = (LizardApplication) getApplicationContext();
		Checkdate checkdateobj = app.getCheckdateObj();
		
		checkdateobj.addHerpEntry(herp);
		checkdateobj.flushObjectsToDB();
		

		dbHelperJson.close();
	}
}
