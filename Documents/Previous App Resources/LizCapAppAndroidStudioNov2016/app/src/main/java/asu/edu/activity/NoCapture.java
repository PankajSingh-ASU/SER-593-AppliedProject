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
import android.widget.CheckBox;
import android.widget.EditText;
import asu.edu.DateTimeSlider;
import asu.edu.R;
import asu.edu.database.JsonDBAdapter;
import asu.edu.dialog.DateSlider;
import asu.edu.labeler.TimeLabeler;

public class NoCapture extends Activity {

	/**
	 * @param args
	 */
	private Button backButton;
	private Button verifyButton;
	private Button editButton;
	private EditText commentsText;
	private CheckBox trapclosedCheck;
	private String recorder;
	private String handler;
	private String combo1;
	private String combo2;
	private String datetime;
	private String time;
	private String date;
	private String site;
	private String array;
	private String trapclosed;
	private String comments;


	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nocapture);
		setTitle("No Capture");

		this.backButton = (Button) this.findViewById(R.id.nocaptureBackButton);
		this.verifyButton = (Button) this.findViewById(R.id.nocaptureVerifyButton);
		this.commentsText = (EditText) this.findViewById(R.id.nocaptureCommentText);
		this.trapclosedCheck = (CheckBox) this.findViewById(R.id.nocaptureTrapClosedCheckBox);
		this.editButton = (Button) this.findViewById(R.id.nocaptureEditButton);

		Bundle bundle = getIntent().getExtras();
		recorder = bundle.getString("Recorder");
		handler = bundle.getString("Handler");
		site = bundle.getString("Site");
		array = bundle.getString("Array");
		//taxa = bundle.getString("Taxa");
		//restart = false;

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

		this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(NoCapture.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Exit")
						.setMessage("Information Entered will be Lost")
						.setNegativeButton("No", null)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Exit the activity
								NoCapture.this.finish();
							}
						}).show();
			}
		});

		this.verifyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String editComments = commentsText.getText().toString();
				String changeComments = editComments.replaceAll("\n", ", ");
				comments = changeComments;

				boolean deadCheck = trapclosedCheck.isChecked();				
				if(deadCheck){
					trapclosed = "Y";
				}else{
					trapclosed = "N";
				}				

				callDialog();
			}

			private void callDialog() {
					storeData();

				//restart = true;
				Intent homeIntent = new Intent(NoCapture.this, MainMenu.class );
				homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				/*Bundle bundle = new Bundle();
		        bundle.putBoolean("Restart", restart);
				bundle.putString("Recorder", recorder);
        		bundle.putString("Handler", handler);	
        		bundle.putString("Site", site);
				bundle.putString("Array", array);
				bundle.putString("Taxa", taxa);
        		homeIntent.putExtras(bundle);*/
				NoCapture.this.startActivity(homeIntent);				      
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
		Cursor queryLocationID = dbHelperJson.rawQuery("select * from locations where SiteName = '" + site + "' and ArrayName = '" + array +"'", null);
		String locationID = null;
		while (queryLocationID.moveToNext()) {
			locationID = queryLocationID.getString(0);
		}
		queryLocationID.close();

		String editComments = commentsText.getText().toString().replaceAll("\n", ", ");

		boolean deadCheck = trapclosedCheck.isChecked();	
		String trapclosed;
		if(deadCheck){
			trapclosed = "Y";
		}else{
			trapclosed = "N";
		}

		dbHelperJson.createCheckdates(null, handler, recorder, date, time, trapclosed, "N", editComments, 1, locationID);
		dbHelperJson.close();
	}
}
