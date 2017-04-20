package asu.edu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import asu.edu.R;
import asu.edu.database.JsonDBAdapter;

/*
 Copyright (c) 2013 LizCapApp Project of Arizona State University
    Heather Bateman, Richard Whitehouse, and Timothy Lindquist

 This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation version 2
of the License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should review the GNU General Public License at:
http://www.gnu.org/licenses/gpl-2.0.html
so you are aware of the terms and your rights with regard
to this software. Or, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

See also the GNU FAQ page at:
https://www.gnu.org/licenses/gpl-faq.html
 */


public class AddArthropod extends Activity {

	/**
	 * @param args
	 */
	private Button backButton;
	private Button verifyButton;
	private Button codeButton;
	private Button minARANButton;
	private Button minAUCHButton;
	private Button minBLATButton;
	private Button minCHILButton;
	private Button minCOLEButton;
	private Button minCRUSButton;
	private Button minDERMButton;
	private Button minDIELButton;
	private Button minDIPTButton;
	private Button minHETEButton;
	private Button minHYMAButton;
	private Button minHYMBButton;
	private Button minLEPIButton;
	private Button minMANTButton;
	private Button minORTHButton;
	private Button minPSEUButton;
	private Button minSCORButton;
	private Button minSOLIButton;
	private Button minTHYSButton;
	private Button minUNKIButton;
	private Button maxARANButton;
	private Button maxAUCHButton;
	private Button maxBLATButton;
	private Button maxCHILButton;
	private Button maxCOLEButton;
	private Button maxCRUSButton;
	private Button maxDERMButton;
	private Button maxDIELButton;
	private Button maxDIPTButton;
	private Button maxHETEButton;
	private Button maxHYMAButton;
	private Button maxHYMBButton;
	private Button maxLEPIButton;
	private Button maxMANTButton;
	private Button maxORTHButton;
	private Button maxPSEUButton;
	private Button maxSCORButton;
	private Button maxSOLIButton;
	private Button maxTHYSButton;
	private Button maxUNKIButton;	
	private EditText aranText;
	private EditText auchText;
	private EditText blatText;
	private EditText chilText;
	private EditText coleText;
	private EditText crusText;
	private EditText dermText;
	private EditText dielText;
	private EditText diptText;
	private EditText heteText;
	private EditText hymaText;
	private EditText hymbText;
	private EditText lepiText;
	private EditText mantText;
	private EditText orthText;
	private EditText pseuText;
	private EditText scorText;
	private EditText soliText;
	private EditText thysText;
	private EditText unkiText;
	private final ArrayList<String> codeArthropod = new ArrayList<String>();
	private final ArrayList<String> nameArthropod = new ArrayList<String>();
	private final ArrayList<String> descriptionArthropod = new ArrayList<String>();
	// Update: please see commented code at bottom for easier interpretation of this mystery list
	private ArrayList<List<?>> arthropodArrayList = new ArrayList<List<?>>();
	private Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addarthopod);
		setTitle("Add Arthropod");
		intent = getIntent();

		this.backButton = (Button) this.findViewById(R.id.addarthropodBackButton);
		this.verifyButton = (Button) this.findViewById(R.id.addarthropodDoneButton);
		this.codeButton = (Button) this.findViewById(R.id.addarthropodSpeciesButton);

		this.minARANButton = (Button) this.findViewById(R.id.addarthropodMinARANButton);
		this.minAUCHButton = (Button) this.findViewById(R.id.addarthropodMinAUCHButton);
		this.minBLATButton = (Button) this.findViewById(R.id.addarthropodMinBLATButton);
		this.minCHILButton = (Button) this.findViewById(R.id.addarthropodMinCHILButton);
		this.minCOLEButton = (Button) this.findViewById(R.id.addarthropodMinCOLEButton);
		this.minCRUSButton = (Button) this.findViewById(R.id.addarthropodMinCRUSButton);
		this.minDERMButton = (Button) this.findViewById(R.id.addarthropodMinDERMButton);
		this.minDIELButton = (Button) this.findViewById(R.id.addarthropodMinDIELButton);
		this.minDIPTButton = (Button) this.findViewById(R.id.addarthropodMinDIPTButton);
		this.minHETEButton = (Button) this.findViewById(R.id.addarthropodMinHETEButton);
		this.minHYMAButton = (Button) this.findViewById(R.id.addarthropodMinHYMAButton);
		this.minHYMBButton = (Button) this.findViewById(R.id.addarthropodMinHYMBButton);
		this.minLEPIButton = (Button) this.findViewById(R.id.addarthropodMinLEPIButton);
		this.minMANTButton = (Button) this.findViewById(R.id.addarthropodMinMANTButton);
		this.minORTHButton = (Button) this.findViewById(R.id.addarthropodMinORTHButton);
		this.minPSEUButton = (Button) this.findViewById(R.id.addarthropodMinPSEUButton);
		this.minSCORButton = (Button) this.findViewById(R.id.addarthropodMinSCORButton);
		this.minSOLIButton = (Button) this.findViewById(R.id.addarthropodMinSOLIButton);
		this.minTHYSButton = (Button) this.findViewById(R.id.addarthropodMinTHYSButton);
		this.minUNKIButton = (Button) this.findViewById(R.id.addarthropodMinUNKIButton);

		this.maxARANButton = (Button) this.findViewById(R.id.addarthropodMaxARANButton);
		this.maxAUCHButton = (Button) this.findViewById(R.id.addarthropodMaxAUCHButton);
		this.maxBLATButton = (Button) this.findViewById(R.id.addarthropodMaxBLATButton);
		this.maxCHILButton = (Button) this.findViewById(R.id.addarthropodMaxCHILButton);
		this.maxCOLEButton = (Button) this.findViewById(R.id.addarthropodMaxCOLEButton);
		this.maxCRUSButton = (Button) this.findViewById(R.id.addarthropodMaxCRUSButton);
		this.maxDERMButton = (Button) this.findViewById(R.id.addarthropodMaxDERMButton);
		this.maxDIELButton = (Button) this.findViewById(R.id.addarthropodMaxDIELButton);
		this.maxDIPTButton = (Button) this.findViewById(R.id.addarthropodMaxDIPTButton);
		this.maxHETEButton = (Button) this.findViewById(R.id.addarthropodMaxHETEButton);
		this.maxHYMAButton = (Button) this.findViewById(R.id.addarthropodMaxHYMAButton);
		this.maxHYMBButton = (Button) this.findViewById(R.id.addarthropodMaxHYMBButton);
		this.maxLEPIButton = (Button) this.findViewById(R.id.addarthropodMaxLEPIButton);
		this.maxMANTButton = (Button) this.findViewById(R.id.addarthropodMaxMANTButton);
		this.maxORTHButton = (Button) this.findViewById(R.id.addarthropodMaxORTHButton);
		this.maxPSEUButton = (Button) this.findViewById(R.id.addarthropodMaxPSEUButton);
		this.maxSCORButton = (Button) this.findViewById(R.id.addarthropodMaxSCORButton);
		this.maxSOLIButton = (Button) this.findViewById(R.id.addarthropodMaxSOLIButton);
		this.maxTHYSButton = (Button) this.findViewById(R.id.addarthropodMaxTHYSButton);
		this.maxUNKIButton = (Button) this.findViewById(R.id.addarthropodMaxUNKIButton);

		this.aranText = (EditText) this.findViewById(R.id.addarthropodARANText);
		this.auchText = (EditText) this.findViewById(R.id.addarthropodAUCHText);
		this.blatText = (EditText) this.findViewById(R.id.addarthropodBLATText);
		this.chilText = (EditText) this.findViewById(R.id.addarthropodCHILText);
		this.coleText = (EditText) this.findViewById(R.id.addarthropodCOLEText);
		this.crusText = (EditText) this.findViewById(R.id.addarthropodCRUSText);
		this.dermText = (EditText) this.findViewById(R.id.addarthropodDERMText);
		this.dielText = (EditText) this.findViewById(R.id.addarthropodDIELText);
		this.diptText = (EditText) this.findViewById(R.id.addarthropodDIPTText);
		this.heteText = (EditText) this.findViewById(R.id.addarthropodHETEText);
		this.hymaText = (EditText) this.findViewById(R.id.addarthropodHYMAText);
		this.hymbText = (EditText) this.findViewById(R.id.addarthropodHYMBText);
		this.lepiText = (EditText) this.findViewById(R.id.addarthropodLEPIText);
		this.mantText = (EditText) this.findViewById(R.id.addarthropodMANTText);
		this.orthText = (EditText) this.findViewById(R.id.addarthropodORTHText);
		this.pseuText = (EditText) this.findViewById(R.id.addarthropodPSEUText);
		this.scorText = (EditText) this.findViewById(R.id.addarthropodSCORText);
		this.soliText = (EditText) this.findViewById(R.id.addarthropodSOLIText);
		this.thysText = (EditText) this.findViewById(R.id.addarthropodTHYSText);
		this.unkiText = (EditText) this.findViewById(R.id.addarthropodUNKIText);
		
		JsonDBAdapter jsondbHelper = new JsonDBAdapter(this);
		jsondbHelper.open();

		Cursor anthropod = jsondbHelper.fetchSelectAnthropod();
		while (anthropod.moveToNext()){
			codeArthropod.add(anthropod.getString(anthropod.getColumnIndexOrThrow(JsonDBAdapter.ORDERCODE_TEXT)));
			nameArthropod.add(anthropod.getString(anthropod.getColumnIndexOrThrow(JsonDBAdapter.ORDERNAME_TEXT)));
			descriptionArthropod.add(anthropod.getString(anthropod.getColumnIndexOrThrow(JsonDBAdapter.DESCRIPTION_TEXT)));
		}
		anthropod.close();

		jsondbHelper.close();

		this.backButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				new AlertDialog.Builder(AddArthropod.this).setIcon(
						android.R.drawable.ic_dialog_alert).setTitle("Exit")
						.setMessage("Information Entered will be Lost")
						.setNegativeButton("No", null)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Exit the activity
								AddArthropod.this.finish();
							}
						}).show();
			}
		});



		this.verifyButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				checkForData();
				int size = arthropodArrayList.size();
				if(size > 0){
					intent.putExtra("returnedData", arthropodArrayList);       
					setResult(RESULT_OK, intent);
					finish(); 
				}else{
					new AlertDialog.Builder(AddArthropod.this).setIcon(
							android.R.drawable.ic_dialog_alert).setTitle("Error")
							.setMessage("None of the Species are above the number 0")
							.setNegativeButton("Ok", null)
							.show();
				}					
			}


		});

		this.codeButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent arthropodIntent = new Intent(AddArthropod.this, SpeciesList.class);
				arthropodIntent.putStringArrayListExtra("Code", codeArthropod);
				arthropodIntent.putStringArrayListExtra("Genus", nameArthropod);
				arthropodIntent.putStringArrayListExtra("Species", descriptionArthropod);				
				startActivityForResult(arthropodIntent, 1);
			}
		});


		this.minARANButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String aranString = aranText.getText().toString();
				if(!aranString.equals("")){
					int aranInt = Integer.parseInt(aranString);
					if(aranInt >= 1){
						aranInt -= 1;
						String newARAN = Integer.toString(aranInt);
						aranText.setText(newARAN);
					}

				}


			}
		});

		this.maxARANButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String aranString = aranText.getText().toString();
				if(!aranString.equals("")){
					int aranInt = Integer.parseInt(aranString);
					aranInt += 1;
					String newARAN = Integer.toString(aranInt);
					aranText.setText(newARAN);					
				}
			}
		});

		this.minAUCHButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String auchString = auchText.getText().toString();
				if(!auchString.equals("")){
					int auchInt = Integer.parseInt(auchString);
					if(auchInt >= 1){
						auchInt -= 1;
						String newAUCH = Integer.toString(auchInt);
						auchText.setText(newAUCH);
					}

				}


			}
		});

		this.maxAUCHButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String auchString = auchText.getText().toString();
				if(!auchString.equals("")){
					int auchInt = Integer.parseInt(auchString);
					auchInt += 1;
					String newAUCH = Integer.toString(auchInt);
					auchText.setText(newAUCH);					
				}
			}
		});

		this.minBLATButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String blatString = blatText.getText().toString();
				if(!blatString.equals("")){
					int blatInt = Integer.parseInt(blatString);
					if(blatInt >= 1){
						blatInt -= 1;
						String newBLAT = Integer.toString(blatInt);
						blatText.setText(newBLAT);
					}

				}


			}
		});

		this.maxBLATButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String blatString = blatText.getText().toString();
				if(!blatString.equals("")){
					int blatInt = Integer.parseInt(blatString);
					blatInt += 1;
					String newBLAT = Integer.toString(blatInt);
					blatText.setText(newBLAT);					
				}
			}
		});

		this.minCHILButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String chilString = chilText.getText().toString();
				if(!chilString.equals("")){
					int chilInt = Integer.parseInt(chilString);
					if(chilInt >= 1){
						chilInt -= 1;
						String newchil = Integer.toString(chilInt);
						chilText.setText(newchil);
					}

				}


			}
		});

		this.maxCHILButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String chilString = chilText.getText().toString();
				if(!chilString.equals("")){
					int chilInt = Integer.parseInt(chilString);
					chilInt += 1;
					String newchil = Integer.toString(chilInt);
					chilText.setText(newchil);					
				}
			}
		});

		this.minCOLEButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String coleString = coleText.getText().toString();
				if(!coleString.equals("")){
					int coleInt = Integer.parseInt(coleString);
					if(coleInt >= 1){
						coleInt -= 1;
						String newcole = Integer.toString(coleInt);
						coleText.setText(newcole);
					}

				}


			}
		});

		this.maxCOLEButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String coleString = coleText.getText().toString();
				if(!coleString.equals("")){
					int coleInt = Integer.parseInt(coleString);
					coleInt += 1;
					String newcole = Integer.toString(coleInt);
					coleText.setText(newcole);					
				}
			}
		});

		this.minCRUSButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String crusString = crusText.getText().toString();
				if(!crusString.equals("")){
					int crusInt = Integer.parseInt(crusString);
					if(crusInt >= 1){
						crusInt -= 1;
						String newcrus = Integer.toString(crusInt);
						crusText.setText(newcrus);
					}

				}


			}
		});

		this.maxCRUSButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String crusString = crusText.getText().toString();
				if(!crusString.equals("")){
					int crusInt = Integer.parseInt(crusString);
					crusInt += 1;
					String newcrus = Integer.toString(crusInt);
					crusText.setText(newcrus);					
				}
			}
		});

		this.minDERMButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String dermString = dermText.getText().toString();
				if(!dermString.equals("")){
					int dermInt = Integer.parseInt(dermString);
					if(dermInt >= 1){
						dermInt -= 1;
						String newderm = Integer.toString(dermInt);
						dermText.setText(newderm);
					}

				}


			}
		});

		this.maxDERMButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String dermString = dermText.getText().toString();
				if(!dermString.equals("")){
					int dermInt = Integer.parseInt(dermString);
					dermInt += 1;
					String newderm = Integer.toString(dermInt);
					dermText.setText(newderm);					
				}
			}
		});

		this.minDIELButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String dielString = dielText.getText().toString();
				if(!dielString.equals("")){
					int dielInt = Integer.parseInt(dielString);
					if(dielInt >= 1){
						dielInt -= 1;
						String newdiel = Integer.toString(dielInt);
						dielText.setText(newdiel);
					}

				}


			}
		});

		this.maxDIELButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String dielString = dielText.getText().toString();
				if(!dielString.equals("")){
					int dielInt = Integer.parseInt(dielString);
					dielInt += 1;
					String newdiel = Integer.toString(dielInt);
					dielText.setText(newdiel);					
				}
			}
		});

		this.minDIPTButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String diptString = diptText.getText().toString();
				if(!diptString.equals("")){
					int diptInt = Integer.parseInt(diptString);
					if(diptInt >= 1){
						diptInt -= 1;
						String newdipt = Integer.toString(diptInt);
						diptText.setText(newdipt);
					}

				}


			}
		});

		this.maxDIPTButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String diptString = diptText.getText().toString();
				if(!diptString.equals("")){
					int diptInt = Integer.parseInt(diptString);
					diptInt += 1;
					String newdipt = Integer.toString(diptInt);
					diptText.setText(newdipt);					
				}
			}
		});

		this.minHETEButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String heteString = heteText.getText().toString();
				if(!heteString.equals("")){
					int heteInt = Integer.parseInt(heteString);
					if(heteInt >= 1){
						heteInt -= 1;
						String newhete = Integer.toString(heteInt);
						heteText.setText(newhete);
					}

				}


			}
		});

		this.maxHETEButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String heteString = heteText.getText().toString();
				if(!heteString.equals("")){
					int heteInt = Integer.parseInt(heteString);
					heteInt += 1;
					String newhete = Integer.toString(heteInt);
					heteText.setText(newhete);					
				}
			}
		});

		this.minHYMAButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String hymaString = hymaText.getText().toString();
				if(!hymaString.equals("")){
					int hymaInt = Integer.parseInt(hymaString);
					if(hymaInt >= 1){
						hymaInt -= 1;
						String newhyma = Integer.toString(hymaInt);
						hymaText.setText(newhyma);
					}

				}


			}
		});

		this.maxHYMAButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String hymaString = hymaText.getText().toString();
				if(!hymaString.equals("")){
					int hymaInt = Integer.parseInt(hymaString);
					hymaInt += 1;
					String newhyma = Integer.toString(hymaInt);
					hymaText.setText(newhyma);					
				}
			}
		});

		this.minHYMBButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String hymbString = hymbText.getText().toString();
				if(!hymbString.equals("")){
					int hymbInt = Integer.parseInt(hymbString);
					if(hymbInt >= 1){
						hymbInt -= 1;
						String newhymb = Integer.toString(hymbInt);
						hymbText.setText(newhymb);
					}

				}


			}
		});

		this.maxHYMBButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String hymbString = hymbText.getText().toString();
				if(!hymbString.equals("")){
					int hymbInt = Integer.parseInt(hymbString);
					hymbInt += 1;
					String newhymb = Integer.toString(hymbInt);
					hymbText.setText(newhymb);					
				}
			}
		});

		this.minLEPIButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String lepiString = lepiText.getText().toString();
				if(!lepiString.equals("")){
					int lepiInt = Integer.parseInt(lepiString);
					if(lepiInt >= 1){
						lepiInt -= 1;
						String newlepi = Integer.toString(lepiInt);
						lepiText.setText(newlepi);
					}

				}


			}
		});

		this.maxLEPIButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String lepiString = lepiText.getText().toString();
				if(!lepiString.equals("")){
					int lepiInt = Integer.parseInt(lepiString);
					lepiInt += 1;
					String newlepi = Integer.toString(lepiInt);
					lepiText.setText(newlepi);					
				}
			}
		});

		this.minMANTButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String mantString = mantText.getText().toString();
				if(!mantString.equals("")){
					int mantInt = Integer.parseInt(mantString);
					if(mantInt >= 1){
						mantInt -= 1;
						String newmant = Integer.toString(mantInt);
						mantText.setText(newmant);
					}

				}


			}
		});

		this.maxMANTButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String mantString = mantText.getText().toString();
				if(!mantString.equals("")){
					int mantInt = Integer.parseInt(mantString);
					mantInt += 1;
					String newmant = Integer.toString(mantInt);
					mantText.setText(newmant);					
				}
			}
		});

		this.minORTHButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String orthString = orthText.getText().toString();
				if(!orthString.equals("")){
					int orthInt = Integer.parseInt(orthString);
					if(orthInt >= 1){
						orthInt -= 1;
						String neworth = Integer.toString(orthInt);
						orthText.setText(neworth);
					}

				}


			}
		});

		this.maxORTHButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String orthString = orthText.getText().toString();
				if(!orthString.equals("")){
					int orthInt = Integer.parseInt(orthString);
					orthInt += 1;
					String neworth = Integer.toString(orthInt);
					orthText.setText(neworth);					
				}
			}
		});

		this.minPSEUButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String pseuString = pseuText.getText().toString();
				if(!pseuString.equals("")){
					int pseuInt = Integer.parseInt(pseuString);
					if(pseuInt >= 1){
						pseuInt -= 1;
						String newpseu = Integer.toString(pseuInt);
						pseuText.setText(newpseu);
					}

				}


			}
		});

		this.maxPSEUButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String pseuString = pseuText.getText().toString();
				if(!pseuString.equals("")){
					int pseuInt = Integer.parseInt(pseuString);
					pseuInt += 1;
					String newpseu = Integer.toString(pseuInt);
					pseuText.setText(newpseu);					
				}
			}
		});

		this.minSCORButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String scorString = scorText.getText().toString();
				if(!scorString.equals("")){
					int scorInt = Integer.parseInt(scorString);
					if(scorInt >= 1){
						scorInt -= 1;
						String newscor = Integer.toString(scorInt);
						scorText.setText(newscor);
					}

				}


			}
		});

		this.maxSCORButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String scorString = scorText.getText().toString();
				if(!scorString.equals("")){
					int scorInt = Integer.parseInt(scorString);
					scorInt += 1;
					String newscor = Integer.toString(scorInt);
					scorText.setText(newscor);					
				}
			}
		});

		this.minSOLIButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String soliString = soliText.getText().toString();
				if(!soliString.equals("")){
					int soliInt = Integer.parseInt(soliString);
					if(soliInt >= 1){
						soliInt -= 1;
						String newsoli = Integer.toString(soliInt);
						soliText.setText(newsoli);
					}

				}


			}
		});

		this.maxSOLIButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String soliString = soliText.getText().toString();
				if(!soliString.equals("")){
					int soliInt = Integer.parseInt(soliString);
					soliInt += 1;
					String newsoli = Integer.toString(soliInt);
					soliText.setText(newsoli);					
				}
			}
		});

		this.minTHYSButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String thysString = thysText.getText().toString();
				if(!thysString.equals("")){
					int thysInt = Integer.parseInt(thysString);
					if(thysInt >= 1){
						thysInt -= 1;
						String newthys = Integer.toString(thysInt);
						thysText.setText(newthys);
					}

				}


			}
		});

		this.maxTHYSButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String thysString = thysText.getText().toString();
				if(!thysString.equals("")){
					int thysInt = Integer.parseInt(thysString);
					thysInt += 1;
					String newthys = Integer.toString(thysInt);
					thysText.setText(newthys);					
				}
			}
		});

		this.minUNKIButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String unkiString = unkiText.getText().toString();
				if(!unkiString.equals("")){
					int unkiInt = Integer.parseInt(unkiString);
					if(unkiInt >= 1){
						unkiInt -= 1;
						String newunki = Integer.toString(unkiInt);
						unkiText.setText(newunki);
					}

				}


			}
		});

		this.maxUNKIButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String unkiString = unkiText.getText().toString();
				if(!unkiString.equals("")){
					int unkiInt = Integer.parseInt(unkiString);
					unkiInt += 1;
					String newunki = Integer.toString(unkiInt);
					unkiText.setText(newunki);					
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode == 1){
			//String msg = data.getStringExtra("returnedData");
		}
	}

	private void checkForData() {
		String aranCheck = aranText.getText().toString();
		if(!aranCheck.equals("")){
			int aran = Integer.parseInt(aranCheck);
			if(aran > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("ARAN");
				item.add(aranCheck);				
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String auchCheck = auchText.getText().toString();
		if(!auchCheck.equals("")){
			int auch = Integer.parseInt(auchCheck);
			if(auch > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("AUCH");
				item.add(auchCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String blatCheck = blatText.getText().toString();
		if(!blatCheck.equals("")){
			int blat = Integer.parseInt(blatCheck);
			if(blat > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("BLAT");
				item.add(blatCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String chilCheck = chilText.getText().toString();
		if(!chilCheck.equals("")){
			int chil = Integer.parseInt(chilCheck);
			if(chil > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("CHIL");
				item.add(chilCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String coleCheck = coleText.getText().toString();
		if(!coleCheck.equals("")){
			int cole = Integer.parseInt(coleCheck);
			if(cole > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("COLE");
				item.add(coleCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String crusCheck = crusText.getText().toString();
		if(!crusCheck.equals("")){
			int crus = Integer.parseInt(crusCheck);
			if(crus > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("CRUS");
				item.add(crusCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String dermCheck = dermText.getText().toString();
		if(!dermCheck.equals("")){
			int derm = Integer.parseInt(dermCheck);
			if(derm > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("DERM");
				item.add(dermCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String dielCheck = dielText.getText().toString();
		if(!dielCheck.equals("")){
			int diel = Integer.parseInt(dielCheck);
			if(diel > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("DIEL");
				item.add(dielCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String diptCheck = diptText.getText().toString();
		if(!diptCheck.equals("")){
			int dipt = Integer.parseInt(diptCheck);
			if(dipt > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("DIPT");
				item.add(diptCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String heteCheck = heteText.getText().toString();
		if(!heteCheck.equals("")){
			int hete = Integer.parseInt(heteCheck);
			if(hete > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("HETE");
				item.add(heteCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String hymaCheck = hymaText.getText().toString();
		if(!hymaCheck.equals("")){
			int hyma = Integer.parseInt(hymaCheck);
			if(hyma > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("HYMA");
				item.add(hymaCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String hymbCheck = hymbText.getText().toString();
		if(!hymbCheck.equals("")){
			int hymb = Integer.parseInt(hymbCheck);
			if(hymb > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("HYMB");
				item.add(hymbCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String lepiCheck = lepiText.getText().toString();
		if(!lepiCheck.equals("")){
			int lepi = Integer.parseInt(lepiCheck);
			if(lepi > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("LEPI");
				item.add(lepiCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String mantCheck = mantText.getText().toString();
		if(!mantCheck.equals("")){
			int mant = Integer.parseInt(mantCheck);
			if(mant > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("MANT");
				item.add(mantCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String orthCheck = orthText.getText().toString();
		if(!orthCheck.equals("")){
			int orth = Integer.parseInt(orthCheck);
			if(orth > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("ORTH");
				item.add(orthCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String pseuCheck = pseuText.getText().toString();
		if(!pseuCheck.equals("")){
			int pseu = Integer.parseInt(pseuCheck);
			if(pseu > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("PSEU");
				item.add(pseuCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String scorCheck = scorText.getText().toString();
		if(!scorCheck.equals("")){
			int scor = Integer.parseInt(scorCheck);
			if(scor > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("SCOR");
				item.add(scorCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String soliCheck = soliText.getText().toString();
		if(!soliCheck.equals("")){
			int soli = Integer.parseInt(soliCheck);
			if(soli > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("SOLI");
				item.add(soliCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String thysCheck = thysText.getText().toString();
		if(!thysCheck.equals("")){
			int thys = Integer.parseInt(thysCheck);
			if(thys > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("THYS");
				item.add(thysCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}
		String unkiCheck = unkiText.getText().toString();
		if(!unkiCheck.equals("")){
			int unki = Integer.parseInt(unkiCheck);
			if(unki > 0){
				ArrayList<String> item = new ArrayList<String>();
				item.add("UNKI");
				item.add(unkiCheck);
				arthropodArrayList.add(new ArrayList<String>(item));
			}
		}

	}


}

/**
 * Below is a piece of code I've written that will convert "arthropodArrayList" to an ArthropodEntry, which is more readable
 * I ended up not using this code, but didn't want it to go to waste. Hopefully this will help you.
 */

///**
// * Magically extracts information from a convoluted list
// * @param arthropodArrayList
// * @return An ArtropodEntry object that contains the same information as the convolutedList, except in a form that is readable by humans
// */
//private ArthropodEntry convolutedDataExtracter (ArrayList<List<?>> arthropodArrayList) {
//	int MANT = 0, UNKI = 0, THYS = 0, SOLI = 0, SCOR = 0, PSEU = 0, ORTH = 0, LEPI = 0, HYMB = 0;
//	int HYMA = 0, HETE = 0, DIPT = 0, DIEL = 0, DERM = 0, CRUS = 0, COLE = 0, CHIL = 0, BLAT = 0;
//	int AUCH = 0, ARAN = 0;
//	
//	for (List<?> arthropodEntry : mysteryList) {
//		String speciesCode  = (String) arthropodEntry.get(0);
//		String amountString = (String) arthropodEntry.get(1);
//		int amount = Integer.parseInt(amountString);
//		
//		if (speciesCode.equals("MANT")) {
//			MANT = amount;
//		}
//		if (speciesCode.equals("UNKI")) {
//			UNKI = amount;
//		}
//		if (speciesCode.equals("THYS")) {
//			THYS = amount;
//		}
//		if (speciesCode.equals("SOLI")) {
//			SOLI = amount;
//		}
//		if (speciesCode.equals("SCOR")) {
//			SCOR = amount;
//		}
//		if (speciesCode.equals("PSEU")) {
//			PSEU = amount;
//		}
//		if (speciesCode.equals("ORTH")) {
//			ORTH = amount;
//		}
//		if (speciesCode.equals("LEPI")) {
//			LEPI = amount;
//		}
//		if (speciesCode.equals("HYMB")) {
//			HYMB = amount;
//		}
//		if (speciesCode.equals("HYMA")) {
//			HYMA = amount;
//		}
//		if (speciesCode.equals("HETE")) {
//			HETE = amount;
//		}
//		if (speciesCode.equals("DIPT")) {
//			DIPT = amount;
//		}
//		if (speciesCode.equals("DIEL")) {
//			DIEL = amount;
//		}
//		if (speciesCode.equals("DERM")) {
//			DERM = amount;
//		}
//		if (speciesCode.equals("CRUS")) {
//			CRUS = amount;
//		}
//		if (speciesCode.equals("COLE")) {
//			COLE = amount;
//		}
//		if (speciesCode.equals("CHIL")) {
//			CHIL = amount;
//		}
//		if (speciesCode.equals("BLAT")) {
//			BLAT = amount;
//		}
//		if (speciesCode.equals("AUCH")) {
//			AUCH = amount;
//		}
//		if (speciesCode.equals("ARAN")) {
//			ARAN = amount;
//		}
//	}
//	
//	return new ArthropodEntry(MANT, UNKI, THYS, SOLI, SCOR, PSEU, ORTH, LEPI, HYMB,
//			                  HYMA, HETE, DIPT, DIEL, DERM, CRUS, COLE, CHIL, BLAT,
//			                  AUCH, ARAN, null, null, null);
//}

