package asu.edu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import asu.edu.R;

public class TrapArray extends Activity {

	private Button A4Button;
	private Button B4Button;
	private Button C4Button;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.traparray);
		setTitle("Trap");
		
		this.A4Button = (Button) this.findViewById(R.id.trapA4);
		this.B4Button = (Button) this.findViewById(R.id.trapB4);
		this.C4Button = (Button) this.findViewById(R.id.trapC4);
		
		this.A4Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "1";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
		this.B4Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "2";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});

		this.C4Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "3";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
	}
}
