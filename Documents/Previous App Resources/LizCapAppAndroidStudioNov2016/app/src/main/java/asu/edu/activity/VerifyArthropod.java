package asu.edu.activity;










/**
 * 3-4-2012 Nathan Nelson: I can't find any instance of this Activity actually being used. Should we delete it? 
 */















import java.util.UUID;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import asu.edu.R;
import asu.edu.database.JsonDBAdapter;

public class VerifyArthropod extends Activity {

	private Button backButton;
	private Button correctButton;
	private TextView fenceText;
	private TextView speciesCodeText;
	private TextView predatorText;
	private TextView numberText;
	private TextView notesText;
	private String recorder;
	private String handler;
	private String combo1;
	private String combo2;
	private String datetime;
	private String time;
	private String date;
	private String site;
	private String array;
	private String taxa;
	private String speciesCode;
	private String predator;
	private String number;
	private String fence;
	private String notes;
	private boolean restart;
	private final Context myMainContext = getApplicationContext();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verifyarthropod);


		this.backButton = (Button) this.findViewById(R.id.verifyarthropodBackButton);
		this.correctButton = (Button) this.findViewById(R.id.verifyarthropodCorrectButton);
		this.fenceText = (TextView) this.findViewById(R.id.verifyarthropodFenceText);
		this.predatorText = (TextView) this.findViewById(R.id.verifyarthropodPredatorText);
		this.numberText = (TextView) this.findViewById(R.id.verifyarthropodNumberText);
		this.notesText = (TextView) this.findViewById(R.id.verifyarthropodNotesText);			
		this.speciesCodeText = (TextView) this.findViewById(R.id.verifyarthropodSpeciesCodeText);

		Bundle bundle = getIntent().getExtras();
		recorder = bundle.getString("Recorder");
		handler = bundle.getString("Handler");
		site = bundle.getString("Site");
		array = bundle.getString("Array");
		taxa = bundle.getString("Taxa");
		fence = bundle.getString("Fence");
		speciesCode = bundle.getString("SpeciesCode");
		predator = bundle.getString("Predator");
		number = bundle.getString("Number");
		notes = bundle.getString("Notes");
		restart = false;

		setTitle("Site: " + site + "      Array: " + array);

		date = (String) android.text.format.DateFormat.format("MM/dd/yyyy", new java.util.Date());
		time = (String) android.text.format.DateFormat.format("hh:mm", new java.util.Date());
		combo1 = (String) android.text.format.DateFormat.format("MMddyy", new java.util.Date());
		combo2 = (String) android.text.format.DateFormat.format("hhmm", new java.util.Date());

		datetime = combo1 + combo2;

		fenceText.setText(fence);
		speciesCodeText.setText(speciesCode);
		predatorText.setText(predator);
		numberText.setText(number);
		notesText.setText(notes);

		this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(myMainContext).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Exit")
						.setMessage("Information Entered will be Lost")
						.setNegativeButton("No", null)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Exit the activity
								VerifyArthropod.this.finish();
							}
						}).show();
			}
		});

		this.correctButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				callDialog();
			}

			private void callDialog() {
				new AlertDialog.Builder(VerifyArthropod.this)
				.setTitle("Choice")
				.setMessage("Do you want to enter another Arthropod?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						storeData();

						restart = true;
						Intent homeIntent = new Intent(VerifyArthropod.this, UserData.class );
						homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Bundle bundle = new Bundle();
						bundle.putBoolean("Restart", restart);
						bundle.putString("Recorder", recorder);
						bundle.putString("Handler", handler);
						bundle.putString("Site", site);
						bundle.putString("Array", array);
						bundle.putString("Taxa", taxa);
						homeIntent.putExtras(bundle);
						VerifyArthropod.this.startActivity(homeIntent);
						dialog.cancel();
					}

				})

				.setNeutralButton("No", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {
						storeData();

						Intent homeIntent = new Intent(VerifyArthropod.this, MainMenu.class);
						homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						VerifyArthropod.this.startActivity(homeIntent);
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

	private void storeData() {
		JsonDBAdapter dbHelperJson = new JsonDBAdapter(this);
		dbHelperJson.open();

		Cursor queryLocationID = dbHelperJson.rawQuery("select * from locations where SiteName = '" + site + "' and ArrayName = '" + array +"'", null);
		String locationID = null;
		while (queryLocationID.moveToNext()) {
			locationID = queryLocationID.getString(0);
		}
		queryLocationID.close();

		String checkID = UUID.randomUUID().toString();

		dbHelperJson.createAnthropodCapture(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, checkID, fence, Character.toString(predator.charAt(0)), notes, null);
		dbHelperJson.createCheckdates(checkID, handler, recorder, date, time, "N", "Y", notes, 1, locationID);

		dbHelperJson.close();
	}
}
