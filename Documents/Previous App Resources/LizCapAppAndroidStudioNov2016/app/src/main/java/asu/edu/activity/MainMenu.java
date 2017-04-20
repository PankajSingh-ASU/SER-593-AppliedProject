package asu.edu.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import asu.edu.R;

public class MainMenu extends Activity {
	private Button collectDataButton;
	private Button b_mainHistory;
	private Button syncButton;
	private Button mainExitButton;
	private Button b_mainAboutMe;
	private TextView tv_version;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		collectDataButton = (Button) findViewById(R.id.mainNewAnimalButton);
		b_mainHistory     = (Button) findViewById(R.id.b_mainHistory);
		syncButton        = (Button) findViewById(R.id.mainSyncButton);
		mainExitButton    = (Button) findViewById(R.id.mainExitButton);
		b_mainAboutMe     = (Button) findViewById(R.id.b_mainAboutMe);
		tv_version        = (TextView) findViewById(R.id.tv_mainVersion);
		
		String versionName;
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			versionName = "Unknown";
		}
		tv_version.setText("Build: " + versionName);
		
		collectDataButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainMenu.this, UserData.class);
				startActivity(intent);							
			}
		});

		b_mainHistory.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainMenu.this, CheckdatesHistory.class);
				startActivity(intent);
			}
		});
		
		syncButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainMenu.this, Sync.class);
				startActivity(intent);
			}
		});
		
		mainExitButton.setOnClickListener(new OnClickListener() {
			public void onClick (View v) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		
		b_mainAboutMe.setOnClickListener(new OnClickListener () {
			public void onClick(View v) {
				Intent intent = new Intent(MainMenu.this, AboutApplication.class);
				startActivity(intent);
			}
		});
	}
}
