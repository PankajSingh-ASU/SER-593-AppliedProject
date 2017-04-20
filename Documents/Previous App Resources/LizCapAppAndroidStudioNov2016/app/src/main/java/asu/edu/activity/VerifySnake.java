/**
 * 
 */
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

/**
 * @author Silver
 *
 */
public class VerifySnake extends Activity {

	private Button backButton;
	private Button correctButton;
	private Button editButton;
	private TextView fenceText;
	private TextView speciesCodeText;
	private TextView massText;
	private TextView svlText;
	private TextView vtlText;
	private TextView sexText;
	private TextView deadText;
	private TextView commentsText;
	private String combo1;
	private String combo2;
	private String datetime;
	private String time;
	private String date;
	private String speciesCode;
	private String svl;
	private String vtl;
	private String mass;
	private String sex;
	private String dead;
	private String fence;
	private String comments;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verifysnake);

		this.backButton = (Button) this.findViewById(R.id.verifysnakeBackButton);
		this.correctButton = (Button) this.findViewById(R.id.verifysnakeCorrectButton);
		this.fenceText = (TextView) this.findViewById(R.id.verifysnakeFenceText);
		this.svlText = (TextView) this.findViewById(R.id.verifysnakeSVLText);
		this.vtlText = (TextView) this.findViewById(R.id.verifysnakeVTLText);
		this.massText = (TextView) this.findViewById(R.id.verifysnakeMassText);
		this.sexText = (TextView) this.findViewById(R.id.verifysnakeSexText);
		this.deadText = (TextView) this.findViewById(R.id.verifysnakeDeadText);
		this.commentsText = (TextView) this.findViewById(R.id.verifysnakeCommentsText);			
		this.speciesCodeText = (TextView) this.findViewById(R.id.verifysnakeSpeciesCodeText);
		this.editButton = (Button) this.findViewById(R.id.verifysnakeEditButton);

		Bundle bundle = getIntent().getExtras();
		fence = bundle.getString("Fence");
		speciesCode = bundle.getString("SpeciesCode");
		mass = bundle.getString("Mass");
		svl = bundle.getString("SVL");
		vtl = bundle.getString("VTL");
		sex = bundle.getString("Sex");
		dead = bundle.getString("Dead");
		comments = bundle.getString("Comments");

		LizardApplication app = (LizardApplication) getApplicationContext();
		Checkdate checkdateobj = app.getCheckdateObj();
		
		setTitle("Site: " + checkdateobj.site + "      Array: " + checkdateobj.array);

		date = (String) android.text.format.DateFormat.format("MM/dd/yyyy", new java.util.Date());
		time = (String) android.text.format.DateFormat.format("hh:mm", new java.util.Date());
		combo1 = (String) android.text.format.DateFormat.format("MMddyy", new java.util.Date());
		combo2 = (String) android.text.format.DateFormat.format("hhmm", new java.util.Date());

		datetime = combo1 + combo2;

		fenceText.setText(fence);
		speciesCodeText.setText(speciesCode);
		massText.setText(mass);
		svlText.setText(svl);
		vtlText.setText(vtl);
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
				new AlertDialog.Builder(VerifySnake.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Exit")
						.setMessage("Information Entered will be Lost")
						.setNegativeButton("No", null)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Exit the activity
								VerifySnake.this.finish();
							}
						}).show();
			}
		});

		this.correctButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				callDialog();
			}

			private void callDialog() {
				new AlertDialog.Builder(VerifySnake.this)
				.setTitle("Choice")
				.setMessage("Do you want to enter another animal?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						storeData();

						Intent restartIntent = new Intent(VerifySnake.this, TaxaSelection.class);
						restartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(restartIntent);
						dialog.cancel();
					}

				})

				.setNeutralButton("No", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						storeData();

						Intent intent = new Intent(VerifySnake.this, LocationComments.class);
						VerifySnake.this.startActivity(intent);
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
		if((!speciesCode.equals("")) || (!speciesCode.equals(null))){
			Cursor queryHerpID = dbHelperJson.rawQuery("select * from herps where Spp_Code = '" + speciesCode + "'", null);
			while (queryHerpID.moveToNext()) {
				herpID = queryHerpID.getString(0);
			}
			queryHerpID.close();
		}else{
			herpID = "";
		}

		if (svl.compareTo("")==0){
			svl = "0";
		}
		if (vtl.compareTo("")==0){
			vtl = "0";
		}
		if (mass.compareTo("")==0){
			mass = "0";
		}
		
		HerpEntry herp = new HerpEntry(fence, null, Integer.valueOf(svl), Integer.valueOf(vtl), 0, Float.valueOf(mass), Character.toString(sex.charAt(0)), Character.toString(dead.charAt(0)), null, comments, herpID, null);
		
		LizardApplication app = (LizardApplication) getApplicationContext();
		Checkdate checkdateobj = app.getCheckdateObj();
		
		checkdateobj.addHerpEntry(herp);
		checkdateobj.flushObjectsToDB();
		
		dbHelperJson.close();		
	}
}
