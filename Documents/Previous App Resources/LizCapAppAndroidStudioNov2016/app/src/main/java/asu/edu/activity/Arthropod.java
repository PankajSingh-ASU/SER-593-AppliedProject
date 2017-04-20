package asu.edu.activity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import asu.edu.LizardApplication;
import asu.edu.R;
import asu.edu.database.ArthropodEntry;
import asu.edu.database.Checkdate;
import asu.edu.database.JsonDBAdapter;

public class Arthropod extends Activity {

	/**
	 * @param args
	 */
	private Button backButton;
	private Button verifyButton;
	private Button addDataButton;
	private CheckBox predatorCheckBox;	
	private Spinner fenceSpinner;
	private EditText commentText;
	private String combo1;
	private String combo2;
	private String datetime;
	private String time;
	private String date;
	private ArrayList<List<?>> arthropodArrayList = new ArrayList<List<?>>();
	ArrayList<String> item = new ArrayList<String>();

	private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return arthropodArrayList.size();
		}

		public void addItem(ArrayList<List<?>> msg){
			for (List<?> list : msg) {
				arthropodArrayList.add(list);
			}
			notifyDataSetChanged();
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
				convertView = mInflater.inflate(R.layout.arthropodlistview, null);
				holder = new ViewHolder();
				holder.codeText = (TextView) convertView
				.findViewById(R.id.listCode);
				holder.numberText = (TextView) convertView
				.findViewById(R.id.listNumber);

				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.codeText.setText(arthropodArrayList.get(position).get(0).toString());
			holder.numberText.setText(arthropodArrayList.get(position).get(1).toString());

			return convertView;
		}

		class ViewHolder {
			TextView codeText;
			TextView numberText;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newarthopod);
		setTitle("Arthropod Data");

		this.backButton = (Button) this.findViewById(R.id.arthropodBackButton);
		this.verifyButton = (Button) this.findViewById(R.id.arthropodVerifyButton);
		this.addDataButton = (Button) this.findViewById(R.id.arthropodItemButton);
		this.predatorCheckBox = (CheckBox) this.findViewById(R.id.arthropodPredatorCheckBox);
		this.fenceSpinner = (Spinner) this.findViewById(R.id.arthropodFenceSpinner);
		this.commentText = (EditText) this.findViewById(R.id.arthropodCommentText);

		date = (String) android.text.format.DateFormat.format("MM/dd/yyyy", new java.util.Date());
		time = (String) android.text.format.DateFormat.format("hh:mm", new java.util.Date());
		combo1 = (String) android.text.format.DateFormat.format("MMddyy", new java.util.Date());
		combo2 = (String) android.text.format.DateFormat.format("hhmm", new java.util.Date());

		datetime = combo1 + combo2;

		ListView arthropodList = (ListView) findViewById(R.id.arthropodListView);

		arthropodList.setAdapter(new EfficientAdapter(this));        

		arthropodList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				final int arrayIndex = arg2;
				new AlertDialog.Builder(Arthropod.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Update Current Entry or Delete Entry")
						.setMessage("Species Code: " + arthropodArrayList.get(arrayIndex).get(0).toString()
								+ "\nNumber: " + arthropodArrayList.get(arrayIndex).get(1).toString())
								.setNegativeButton("Cancel", null)
								.setPositiveButton("Update",
										new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										String mCode = arthropodArrayList.get(arrayIndex).get(0).toString();
										String mNumber = arthropodArrayList.get(arrayIndex).get(1).toString();
										Intent changeIntent = new Intent(Arthropod.this, ArthropodOld.class);	
										String arrayNumber = Integer.toString(arrayIndex);
										changeIntent.putExtra("ArrayIndex", arrayNumber);
										changeIntent.putExtra("Code", mCode);
										changeIntent.putExtra("Number", mNumber);
										startActivityForResult(changeIntent, 8);		                                   
									}
								})
								.setNeutralButton("Delete",
										new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

										arthropodArrayList.remove(arrayIndex);
										ListView arthropodList = (ListView) findViewById(R.id.arthropodListView);
										arthropodList.setAdapter(new EfficientAdapter(Arthropod.this));
									}
								})
								.show();
			}
		});        

		this.addDataButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent arthropodIntent = new Intent(Arthropod.this, AddArthropod.class);			
				startActivityForResult(arthropodIntent, 7);
			}
		});

		this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(Arthropod.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Exit")
						.setMessage("Information Entered will be Lost")
						.setNegativeButton("No", null)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Exit the activity
								Arthropod.this.finish();
							}
						}).show();
			}
		});

		this.verifyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				boolean testOnErrors = errorCheck();
				if(testOnErrors){
					new AlertDialog.Builder(Arthropod.this)
					.setTitle("Choice")
					.setMessage("Do you want to enter another animal?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							storeData();
							Intent intent = new Intent(Arthropod.this, TaxaSelection.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							dialog.cancel();
						}

					})

					.setNeutralButton("No", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int whichButton) {
							storeData();
							Intent intent = new Intent(Arthropod.this, LocationComments.class);
							Arthropod.this.startActivity(intent);
							dialog.cancel();
						}

					})

					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int whichButton) {
							dialog.cancel();
						}

					}).show();									
				}		
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 7){
			@SuppressWarnings("unchecked")
			ArrayList<List<?>> msg = (ArrayList<List<?>>) data.getSerializableExtra("returnedData");             
			EfficientAdapter effAd = new EfficientAdapter(Arthropod.this);
			ArrayList<String> duplicates = new ArrayList<String>();
			ArrayList<List<?>> number = new ArrayList<List<?>>();

			for (List<?> msglist : msg) {
				for (List<?> arthlist : arthropodArrayList) {
					if(msglist.get(0).equals(arthlist.get(0))){
						number.add(msglist);
						duplicates.add(msglist.get(0).toString());
					}	
				}				
			}

			for (List<?> integer : number) {
				msg.remove(integer);
			}

			if(!msg.isEmpty()){
				effAd.addItem(msg);	
			}
			if(!duplicates.isEmpty()){
				String dupError = "";
				for (String string : duplicates) {
					dupError += string + " ";
				}

				new AlertDialog.Builder(Arthropod.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Error")
						.setMessage("Duplicates found " + dupError + "\nPlease use Update or Delete to make changes.")
						.setNegativeButton("Ok", null)
						.show();
			}


			ListView arthropodList = (ListView) findViewById(R.id.arthropodListView);
			arthropodList.setAdapter(new EfficientAdapter(this));
		}
		if(resultCode == RESULT_OK && requestCode == 8){
			String arrayIndex = data.getStringExtra("ArrayNumber");
			String nCode = data.getStringExtra("Code");
			String nNumber = data.getStringExtra("Number");

			int arrayNum = Integer.parseInt(arrayIndex);

			ArrayList<String> nUpdateArray = new ArrayList<String>();
			nUpdateArray.add(nCode);
			nUpdateArray.add(nNumber);

			arthropodArrayList.set(arrayNum, nUpdateArray);

			ListView arthropodList = (ListView) findViewById(R.id.arthropodListView);
			arthropodList.setAdapter(new EfficientAdapter(this));
		}

	}

	private boolean errorCheck(){
		boolean finalCheck = false;
		String checkFence = (String) fenceSpinner.getSelectedItem();
		int size = arthropodArrayList.size();
		if(checkFence.equals("Select")){
			new AlertDialog.Builder(Arthropod.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("Fence Trap can Not be Select. Please choose a Fence Trap.")
					.setNegativeButton("Ok", null)
					.show();
		}

		else if(size > 0){
			finalCheck = true;
		}else{
			new AlertDialog.Builder(Arthropod.this).setIcon(
					android.R.drawable.ic_dialog_alert).setTitle("Error")
					.setMessage("There are no Arthropods to record. Please Add Arthropods.")
					.setNegativeButton("Ok", null)
					.show();
		}

		return finalCheck;
	}

	private void storeData() {
		JsonDBAdapter dbHelperJson = new JsonDBAdapter(this);
		dbHelperJson.open();

		// Only solution to work with new way of loading information to the database possibly check to make sure of order of items and 
		// then we can loose all if else statements we can just go through the list one at a time.

		int mant = 0;
		int unki = 0;
		int thys = 0;
		int soli = 0;
		int scor = 0;
		int pseu = 0;
		int orth = 0;
		int lepi = 0;
		int hymb = 0;
		int hyma = 0;
		int hete = 0;
		int dipt = 0;
		int diel = 0;
		int derm = 0;
		int crus = 0;
		int cole = 0;
		int chil = 0;
		int blat = 0;
		int auch = 0;
		int aran = 0;
		for (List<?> codeName : arthropodArrayList) {
			if (codeName.get(0).equals("MANT")) {
				mant = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("UNKI")) {
				unki = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("THYS")) {
				thys = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("SOLI")) {
				soli = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("SCOR")) {
				scor = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("PSEU")) {
				pseu = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("ORTH")) {
				orth = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("LEPI")) {
				lepi = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("HYMB")) {
				hymb = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("HYMA")) {
				hyma = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("HETE")) {
				hete = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("DIPT")) {
				dipt = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("DIEL")) {
				diel = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("DERM")) {
				derm = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("CRUS")) {
				crus = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("COLE")) {
				cole = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("CHIL")) {
				chil = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("BLAT")) {
				blat = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("AUCH")) {
				auch = Integer.parseInt((String) codeName.get(1));
			} else if (codeName.get(0).equals("ARAN")) {
				aran = Integer.parseInt((String) codeName.get(1));
			}
		}

		String dbComment = commentText.getText().toString();
		String dbFence = (String) fenceSpinner.getSelectedItem();
		String predator;
		
		if (predatorCheckBox.isChecked()){
			predator = "Y";
		} else {
			predator = "N";
		}

		dbHelperJson.close();

		ArthropodEntry arthropod = 
				new ArthropodEntry(mant, unki, thys, soli, scor, pseu, orth, lepi, hymb,
								   hyma, hete, dipt, diel, derm, crus, cole, chil, blat,
								   auch, aran, dbFence, predator, dbComment);
		
		LizardApplication app = (LizardApplication) getApplicationContext();
		Checkdate checkdateobj = app.getCheckdateObj();
		checkdateobj.addArthropodEntry(arthropod);
		checkdateobj.flushObjectsToDB();
		
	}
}
