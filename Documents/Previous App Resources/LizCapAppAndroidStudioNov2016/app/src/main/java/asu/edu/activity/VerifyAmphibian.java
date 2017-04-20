package asu.edu.activity;

import java.util.Calendar;
import asu.edu.DateTimeSlider;
import asu.edu.LizardApplication;
import asu.edu.R;
import asu.edu.database.Checkdate;
import asu.edu.database.HerpEntry;
import asu.edu.database.JsonDBAdapter;
import asu.edu.dialog.DateSlider;
import asu.edu.labeler.TimeLabeler;

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

public class VerifyAmphibian extends Activity {

	private Button backButton;
	private Button correctButton;
	private Button editButton;
	private TextView fenceText;
	private TextView speciesCodeText;
	private TextView massText;
	private TextView hdbodyText;
	private TextView sexText;
	private TextView deadText;
	private TextView commentsText;
	private String combo1;
	private String combo2;
	private String datetime;
	private String time;
	private String date;
	private String fence;
	private String speciesCode;
	private String hdbody;
	private String mass;
	private String sex;
	private String dead;
	private String comments;
	private String trapclosed;
	private Checkdate checkdateobj;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verifyamphibian);
		
		LizardApplication app = (LizardApplication) getApplicationContext();
		checkdateobj = app.getCheckdateObj();

		this.backButton = (Button) this.findViewById(R.id.verifyamphibianBackButton);
		this.correctButton = (Button) this.findViewById(R.id.verifyamphibianCorrectButton);
		this.fenceText = (TextView) this.findViewById(R.id.verifyamphibianFenceText);
		this.hdbodyText = (TextView) this.findViewById(R.id.verifyamphibianHdBodyText);
		this.massText = (TextView) this.findViewById(R.id.verifyamphibianMassText);
		this.sexText = (TextView) this.findViewById(R.id.verifyamphibianSexText);
		this.deadText = (TextView) this.findViewById(R.id.verifyamphibianDeadText);
		this.commentsText = (TextView) this.findViewById(R.id.verifyamphibianCommentsText);
		this.speciesCodeText = (TextView) this.findViewById(R.id.verifyamphibianSpeciesCodeText);
		this.editButton = (Button) this.findViewById(R.id.verifyamphibianEditButton);

		Bundle bundle = getIntent().getExtras();
		fence = bundle.getString("Fence");
		speciesCode = bundle.getString("SpeciesCode");
		mass = bundle.getString("Mass");
		hdbody = bundle.getString("HdBody");
		sex = bundle.getString("Sex");
		dead = bundle.getString("Dead");
		comments = bundle.getString("Comments");
		trapclosed = bundle.getString("TrapClosed");

		setTitle("Site: " + checkdateobj.site + "      Array: " + checkdateobj.array);

		date = (String) android.text.format.DateFormat.format("MM/dd/yyyy", new java.util.Date());
		time = (String) android.text.format.DateFormat.format("hh:mm", new java.util.Date());
		combo1 = (String) android.text.format.DateFormat.format("MMddyy", new java.util.Date());
		combo2 = (String) android.text.format.DateFormat.format("hhmm", new java.util.Date());

		datetime = combo1 + combo2;

		fenceText.setText(fence);
		speciesCodeText.setText(speciesCode);
		massText.setText(mass);
		hdbodyText.setText(hdbody);
		sexText.setText(sex);
		deadText.setText(dead);
		commentsText.setText(comments);

		editButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// call the internal showDialog method using the predefined ID
				showDialog(5);
			}
		});

		this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(VerifyAmphibian.this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("Exit")
				.setMessage("Information Entered will be Lost")
				.setNegativeButton("No", null)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						// Exit the activity
						VerifyAmphibian.this.finish();
					}
				}).show();
			}
		});

		this.correctButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				callDialog();
			}

			private void callDialog() {
				new AlertDialog.Builder(VerifyAmphibian.this)
				.setTitle("Choice")
				.setMessage("Do you want to enter another animal?")
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						storeData();

						Intent intent = new Intent(VerifyAmphibian.this, TaxaSelection.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						dialog.cancel();
					}

				})

				.setNeutralButton("No",
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int whichButton) {
						storeData();

						Intent intent = new Intent(VerifyAmphibian.this, LocationComments.class);
						VerifyAmphibian.this.startActivity(intent);
						dialog.cancel();
					}
				})

				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int whichButton) {
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
		String herpID = null;
		JsonDBAdapter dbHelperJson = new JsonDBAdapter(this);
		dbHelperJson.open();
//		String checkID = dbHelperJson.fetchLatestCheckdateUUID();

		if ((!speciesCode.equals("")) || (!speciesCode.equals(null))) {
			Cursor queryHerpID = dbHelperJson.rawQuery("select * from herps where Spp_Code = '" + speciesCode + "'", null);

			while (queryHerpID.moveToNext()) {
				herpID = queryHerpID.getString(0);
			}
			queryHerpID.close();
		}
		else {
			herpID = "";
		}

		//		Cursor queryLocationID = dbHelperJson.rawQuery("select * from locations where SiteName = '" + site + "' and ArrayName = '" + array +"'", null);
		//
		//		String locationID = null;
		//		while (queryLocationID.moveToNext()) {
		//			locationID = queryLocationID.getString(0);
		//		}
		//		queryLocationID.close();

		if (mass.compareTo("")==0){
			mass = "0";
		}
		if (hdbody.compareTo("")==0){
			hdbody = "0";
		}
		
		HerpEntry herp = new HerpEntry(fence, Float.valueOf(mass), Integer.valueOf(hdbody), Character.toString(sex.charAt(0)), Character.toString(dead.charAt(0)), comments, herpID);
		checkdateobj.addHerpEntry(herp);
		checkdateobj.flushObjectsToDB();
		

		dbHelperJson.close();
	}
}