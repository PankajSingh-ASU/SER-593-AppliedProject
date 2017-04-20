package asu.edu.activity;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import asu.edu.LizardApplication;
import asu.edu.R;
import asu.edu.database.Checkdate;

public class LocationComments extends Activity {
	private Button b_next;
	private EditText et_comments;
	private Spinner sp_locationTraps;
	private Checkdate checkdateobj;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationcomments);
		
		LizardApplication app = (LizardApplication) getApplicationContext();
		checkdateobj = app.getCheckdateObj();
		
		b_next           = (Button)   findViewById(R.id.b_locationNext);
		et_comments      = (EditText) findViewById(R.id.et_locationComments);
		sp_locationTraps = (Spinner)  findViewById(R.id.sp_locationTraps);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.traps_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp_locationTraps.setAdapter(adapter);
	    setSpinnerAccordingToDayOfWeek();
		
		b_next.setOnClickListener(new NextButtonListener());
		
		if ("No Captures".equals(checkdateobj.noCaptures)) {
			et_comments.setText("No Capture");
		}
	}
	
	private void setSpinnerAccordingToDayOfWeek () {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		// If current day is Sunday, then day=1
		// Spinner 0=OPEN, 1=CHECKED, 2=CHECKED&CLOSED
		
		// Monday
		if (day == 2) {
			sp_locationTraps.setSelection(0);
		}
		
		// Friday
		else if (day == 6) {
			sp_locationTraps.setSelection(2);
		}
		
		// All other days
		else {
			sp_locationTraps.setSelection(1);
		}
	}

	private class NextButtonListener implements OnClickListener {
		public void onClick (View v) {
			String trapState = sp_locationTraps.getSelectedItem().toString();
			String locationComments = et_comments.getText().toString();

			checkdateobj.flushObjectsToDB();
			checkdateobj.updateCheckdateEntryWithLocationInformation(trapState, locationComments);
			
			gotoMainMenu();
		}
	}
	
	private void gotoMainMenu () {
		Intent intent = new Intent(LocationComments.this, MainMenu.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
