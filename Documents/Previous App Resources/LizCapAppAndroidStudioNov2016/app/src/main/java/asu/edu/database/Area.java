package asu.edu.database;

import org.json.JSONException;
import org.json.JSONObject;

public class Area {
	public String areaName;
	public String areaID;
	
	public Area (JSONObject object) {
		try {
			areaName = object.getString("AreaName");
			areaID = object.getString("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
