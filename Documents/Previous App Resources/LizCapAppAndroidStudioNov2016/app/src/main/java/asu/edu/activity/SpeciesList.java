package asu.edu.activity;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import asu.edu.R;

public class SpeciesList extends Activity {
	
	 private class EfficientAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		
		public EfficientAdapter(Context context) {
			 mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			 return code.size();
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
				 convertView = mInflater.inflate(R.layout.listview, null);
				 holder = new ViewHolder();
				 holder.codeText = (TextView) convertView.findViewById(R.id.TextView01);
				 holder.genusText = (TextView) convertView.findViewById(R.id.TextView02);
				 holder.speciesText = (TextView) convertView.findViewById(R.id.TextView03);
				
				 convertView.setTag(holder);
			 }else {
				 holder = (ViewHolder) convertView.getTag();
			 }
			
			 holder.codeText.setText(code.get(position));
			 holder.genusText.setText(genus.get(position));
			 holder.speciesText.setText(species.get(position));
			
			 return convertView;
		}

		class ViewHolder {
			TextView codeText;
			TextView genusText;
			TextView speciesText;
		}
	 }

private ArrayList<String> code = new ArrayList<String>();
private ArrayList<String> genus = new ArrayList<String>();
private ArrayList<String> species = new ArrayList<String>();
private Intent intent;
private String speciescode;

@Override
public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
	 setContentView(R.layout.editbutton);
	 setTitle("Species List");
	 intent = getIntent();
	
	 code = intent.getStringArrayListExtra("Code");		
     genus = intent.getStringArrayListExtra("Genus");
     species = intent.getStringArrayListExtra("Species");
	 
	 ListView speciesList = (ListView) findViewById(R.id.speciesListView);
	 
	 speciesList.setAdapter(new EfficientAdapter(this));

	 speciesList.setOnItemClickListener(new OnItemClickListener() {
		 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			speciescode = code.get(arg2);
			intent.putExtra("returnedData", speciescode);
	        setResult(RESULT_OK, intent);
	        finish(); 
		 }
	 });


}

}