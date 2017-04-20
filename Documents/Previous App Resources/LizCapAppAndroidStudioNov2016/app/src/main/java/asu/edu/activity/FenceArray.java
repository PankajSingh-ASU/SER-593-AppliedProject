package asu.edu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import asu.edu.R;

public class FenceArray extends Activity {

	private Button A1Button;
	private Button A2Button;
	private Button A3Button;
	private Button A4Button;
	private Button AFenceButton;
	private Button B2Button;
	private Button B3Button;
	private Button B4Button;
	private Button BFenceButton;
	private Button C2Button;
	private Button C3Button;
	private Button C4Button;
	private Button CFenceButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fencearray);
		setTitle("Trap");
		
		this.A1Button = (Button) this.findViewById(R.id.fenceA1);
		this.A2Button = (Button) this.findViewById(R.id.fenceA2);
		this.A3Button = (Button) this.findViewById(R.id.fenceA3);
		this.A4Button = (Button) this.findViewById(R.id.fenceA4);
		this.AFenceButton = (Button) this.findViewById(R.id.fenceAFence);
		this.B2Button = (Button) this.findViewById(R.id.fenceB2);
		this.B3Button = (Button) this.findViewById(R.id.fenceB3);
		this.B4Button = (Button) this.findViewById(R.id.fenceB4);
		this.BFenceButton = (Button) this.findViewById(R.id.fenceBFence);
		this.C2Button = (Button) this.findViewById(R.id.fenceC2);
		this.C3Button = (Button) this.findViewById(R.id.fenceC3);
		this.C4Button = (Button) this.findViewById(R.id.fenceC4);
		this.CFenceButton = (Button) this.findViewById(R.id.fenceCFence);
		
		this.A1Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "0";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
		this.A2Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "1";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
		this.A3Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "2";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
		this.A4Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "3";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
		this.AFenceButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "4";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
		this.B2Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "5";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
		this.B3Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "6";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
		this.B4Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "7";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
		this.BFenceButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "8";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
		this.C2Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "9";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
		this.C3Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "10";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
		this.C4Button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "11";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
		
		this.CFenceButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String value = "12";
				Intent intent = getIntent();
				intent.putExtra("returnedData", value);
		        setResult(RESULT_OK, intent);
		        finish();
			}
		});
	}
}
