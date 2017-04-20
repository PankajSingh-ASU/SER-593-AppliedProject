package asu.edu.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
//import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import asu.edu.activity.Sync;

public class SyncJsonDatabase extends Thread {
	private static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms
	static final String LOG_TAG = "JSONCALC";
	private DefaultHttpClient httpClient = null;
	private Handler uiHandler;
	private Sync classActivity;
	private JsonDBAdapter myDbHelper;

	public SyncJsonDatabase(JsonDBAdapter dbHelper, Handler handle, Sync newClass){
		classActivity = newClass;
		myDbHelper = dbHelper;
		uiHandler = handle;
	}

	public void run() {
		String errMsg = process();
		ProcessServerUIUpdate plu = new ProcessServerUIUpdate(errMsg, classActivity);
		uiHandler.post( plu );
	}

	private String process() {
		String errMsg = null;
		JSONObject elements = null;
		try {
			elements = databaseToJSON();
			classActivity.updateProgressLevel(1);
		} catch( JSONException ex ) {
			Log.e( LOG_TAG, "JSONException", ex );
		}
		String request = elements.toString();
		Log.d( LOG_TAG, "request: "+request );
		String response = null;
		try {
			response = sendToServer( request );
			//classActivity.updateProgressLevel(2, "Parsing Response");
			Log.d( LOG_TAG, "response: "+response );
			JSONTokener tokener = new JSONTokener( response );
			Object o = tokener.nextValue();
			if( o instanceof JSONObject ) {
				parseJSONToDatabase((JSONObject)o);
			} else
				throw new JSONException( "Top element is not a JSONObject" );
		} catch( IOException ex ) {
			if (ex.toString().contains("HTTP status: ")) {
				errMsg = "Server Response " + ex.toString();
			} else {
				errMsg = "Sorry There was a Problem Connecting to the Server";
			}
			Log.e( LOG_TAG, "IOException", ex );
		} catch (JSONException e) {
			errMsg = "Error Parsing Data From Server";
			e.printStackTrace();
		}
		return errMsg;
	}

	private String sendToServer( String request ) throws IOException {
		String result = null;
		maybeCreateHttpClient();

		String url = classActivity.serverString + "/mobilesync?format=json";
		HttpPost post = new HttpPost( url );
		StringEntity se = new StringEntity(request);
		post.setEntity(se);
		post.setHeader("Content-Type","application/json");  

		HttpResponse resp = httpClient.execute( post );
		// Execute the POST transaction and read the results
		int status = resp.getStatusLine().getStatusCode(); 
		if( status != HttpStatus.SC_OK ) {
			throw new IOException( "HTTP status: "+Integer.toString( status ) );
		} else {
			result = getResponse(resp.getEntity());
		}
		return result;
	}

	private void maybeCreateHttpClient() {
		
		
		if ( httpClient == null) {
			try {
		        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		        trustStore.load(null, null);

		        SSLSocketFactory sf = new LizCapAppSSLSocketFactory(trustStore);
		        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		        HttpParams params = new BasicHttpParams();
		        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

		        SchemeRegistry registry = new SchemeRegistry();
		        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		        registry.register(new Scheme("https", sf, 443));

		        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

		        httpClient=new DefaultHttpClient(ccm, params);
		    } catch (Exception e) {
		    	httpClient=new DefaultHttpClient();
		    }
		}
		
		
		
	}

	private String getResponse(HttpEntity entity) {
		StringBuilder sb = new StringBuilder();
		try {
			InputStream is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return sb.toString();
	}

	private JSONObject databaseToJSON() throws JSONException {
		JSONObject fullList = new JSONObject();

		SharedPreferences settings = classActivity.getSharedPreferences("LizPref", 0);
		String oldServer = settings.getString("currentServer", "Never Synced");
		Log.d("Testing", "Old Server :" + oldServer);
		if (oldServer.compareTo(classActivity.currentServer)==0){
			JSONArray anthropodCaptureArray = new JSONArray();
			Log.d("Testing", "Entered Area");
			Cursor antrhopodCaptureCursor = myDbHelper.fetchAllAnthropodCaptures();
			for (int i = 0 ; i<antrhopodCaptureCursor.getCount(); i++){
				antrhopodCaptureCursor.moveToPosition(i);
				if (antrhopodCaptureCursor.getInt(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.CHANGED_NUM)) != 0){
					JSONObject obj = new JSONObject();
					Log.e("Found", "Sending....");
					obj.put(JsonDBAdapter.MANT_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.MANT_NUM)));
					obj.put(JsonDBAdapter.UNKI_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.UNKI_NUM)));
					obj.put(JsonDBAdapter.THYS_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.THYS_NUM)));
					obj.put(JsonDBAdapter.SOLI_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.SOLI_NUM)));
					obj.put(JsonDBAdapter.SCOR_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.SCOR_NUM)));
					obj.put(JsonDBAdapter.PSEU_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.PSEU_NUM)));
					obj.put(JsonDBAdapter.ORTH_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.ORTH_NUM)));
					obj.put(JsonDBAdapter.LEPI_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.LEPI_NUM)));
					obj.put(JsonDBAdapter.HYMB_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.HYMB_NUM)));
					obj.put(JsonDBAdapter.HYMA_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.HYMA_NUM)));
					obj.put(JsonDBAdapter.HETE_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.HETE_NUM)));
					obj.put(JsonDBAdapter.DIPT_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.DIPT_NUM)));
					obj.put(JsonDBAdapter.DIEL_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.DIEL_NUM)));
					obj.put(JsonDBAdapter.DERM_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.DERM_NUM)));
					obj.put(JsonDBAdapter.CRUS_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.CRUS_NUM)));
					obj.put(JsonDBAdapter.COLE_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.COLE_NUM)));
					obj.put(JsonDBAdapter.CHIL_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.CHIL_NUM)));
					obj.put(JsonDBAdapter.BLAT_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.BLAT_NUM)));
					obj.put(JsonDBAdapter.AUCH_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.AUCH_NUM)));
					obj.put(JsonDBAdapter.ARAN_NUM, antrhopodCaptureCursor.getDouble(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.ARAN_NUM)));
					obj.put(JsonDBAdapter.CHECKDATES_TEXT, antrhopodCaptureCursor.getString(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.CHECKDATES_TEXT)));
					obj.put(JsonDBAdapter.TRAP_TEXT, antrhopodCaptureCursor.getString(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.TRAP_TEXT)));
					obj.put(JsonDBAdapter.PREDATOR_TEXT, antrhopodCaptureCursor.getString(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.PREDATOR_TEXT)));
					obj.put(JsonDBAdapter.NOTES_TEXT, antrhopodCaptureCursor.getString(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.NOTES_TEXT)));
					obj.put(JsonDBAdapter.ANTHROPODCAPTUEID_TEXT, antrhopodCaptureCursor.getString(antrhopodCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.ANTHROPODCAPTUEID_TEXT)));
					anthropodCaptureArray.put(obj);
				}
			}
			antrhopodCaptureCursor.close();
			fullList.put("anthropodCapture", anthropodCaptureArray);

			JSONArray anthropodArray = new JSONArray();
			Cursor antrhopodCursor = myDbHelper.fetchAllAnthropod();
			for (int i = 0 ; i<antrhopodCursor.getCount(); i++){
				antrhopodCursor.moveToPosition(i);
				if (antrhopodCursor.getInt(antrhopodCursor.getColumnIndexOrThrow(JsonDBAdapter.CHANGED_NUM)) != 0){
					JSONObject obj = new JSONObject();
					obj.put(JsonDBAdapter.ANTHROPODID_NUM, antrhopodCursor.getString(antrhopodCursor.getColumnIndexOrThrow(JsonDBAdapter.ANTHROPODID_NUM)));
					obj.put(JsonDBAdapter.CLASS_TEXT, antrhopodCursor.getString(antrhopodCursor.getColumnIndexOrThrow(JsonDBAdapter.CLASS_TEXT)));
					obj.put(JsonDBAdapter.ORDERNAME_TEXT, antrhopodCursor.getString(antrhopodCursor.getColumnIndexOrThrow(JsonDBAdapter.ORDERNAME_TEXT)));
					obj.put(JsonDBAdapter.ORDERCODE_TEXT, antrhopodCursor.getString(antrhopodCursor.getColumnIndexOrThrow(JsonDBAdapter.ORDERCODE_TEXT)));
					obj.put(JsonDBAdapter.DESCRIPTION_TEXT, antrhopodCursor.getString(antrhopodCursor.getColumnIndexOrThrow(JsonDBAdapter.DESCRIPTION_TEXT)));
					anthropodArray.put(obj);
				}
			}
			antrhopodCursor.close();
			fullList.put("arthropod", anthropodArray);

			JSONArray checkDatesArray = new JSONArray();
			Cursor checkDatesCursor = myDbHelper.fetchAllCheckdates();
			for (int i = 0 ; i<checkDatesCursor.getCount(); i++){
				checkDatesCursor.moveToPosition(i);
				if (checkDatesCursor.getInt(checkDatesCursor.getColumnIndexOrThrow(JsonDBAdapter.CHANGED_NUM)) != 0){
					JSONObject obj = new JSONObject();
					obj.put(JsonDBAdapter.CHECKDATEID_TEXT, checkDatesCursor.getString(checkDatesCursor.getColumnIndexOrThrow(JsonDBAdapter.CHECKDATEID_TEXT)));
					obj.put(JsonDBAdapter.HANDLER_TEXT, checkDatesCursor.getString(checkDatesCursor.getColumnIndexOrThrow(JsonDBAdapter.HANDLER_TEXT)));
					obj.put(JsonDBAdapter.RECORDER_TEXT, checkDatesCursor.getString(checkDatesCursor.getColumnIndexOrThrow(JsonDBAdapter.RECORDER_TEXT)));
					obj.put(JsonDBAdapter.DATE_TEXT, checkDatesCursor.getString(checkDatesCursor.getColumnIndexOrThrow(JsonDBAdapter.DATE_TEXT)));
					obj.put(JsonDBAdapter.TIME_TEXT, checkDatesCursor.getString(checkDatesCursor.getColumnIndexOrThrow(JsonDBAdapter.TIME_TEXT)));
					obj.put(JsonDBAdapter.CLOSED_TEXT, checkDatesCursor.getString(checkDatesCursor.getColumnIndexOrThrow(JsonDBAdapter.CLOSED_TEXT)));
					obj.put(JsonDBAdapter.NOCAPTURE_TEXT, checkDatesCursor.getString(checkDatesCursor.getColumnIndexOrThrow(JsonDBAdapter.NOCAPTURE_TEXT)));
					obj.put(JsonDBAdapter.COMMENTS_TEXT, checkDatesCursor.getString(checkDatesCursor.getColumnIndexOrThrow(JsonDBAdapter.COMMENTS_TEXT)));
					obj.put(JsonDBAdapter.LOCATIONID_TEXT, checkDatesCursor.getString(checkDatesCursor.getColumnIndexOrThrow(JsonDBAdapter.LOCATIONID_TEXT)));
					checkDatesArray.put(obj);
				}
			}
			checkDatesCursor.close();
			fullList.put("checkdate", checkDatesArray);

			JSONArray herpsArray = new JSONArray();
			Cursor herpsCursor = myDbHelper.fetchAllHerps();
			for (int i = 0 ; i<herpsCursor.getCount(); i++){
				herpsCursor.moveToPosition(i);
				if (herpsCursor.getInt(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.CHANGED_NUM)) != 0){
					JSONObject obj = new JSONObject();
					obj.put(JsonDBAdapter.HERPID_NUM, herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.HERPID_NUM)));
					obj.put(JsonDBAdapter.TAXA_TEXT, herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.TAXA_TEXT)));
					obj.put(JsonDBAdapter.GENUS_TEXT, herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.GENUS_TEXT)));
					obj.put(JsonDBAdapter.SPECIES_TEXT, herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.SPECIES_TEXT)));
					obj.put(JsonDBAdapter.SPPCODE_TEXT, herpsCursor.getString(herpsCursor.getColumnIndexOrThrow(JsonDBAdapter.SPPCODE_TEXT)));
					herpsArray.put(obj);
				}
			}
			herpsCursor.close();
			fullList.put("herp", herpsArray);

			JSONArray locationsArray = new JSONArray();
			Cursor locationsCursor = myDbHelper.fetchAllLocations();
			for (int i = 0 ; i<locationsCursor.getCount(); i++){
				locationsCursor.moveToPosition(i);
				if (locationsCursor.getInt(locationsCursor.getColumnIndexOrThrow(JsonDBAdapter.CHANGED_NUM)) != 0){
					JSONObject obj = new JSONObject();
					obj.put(JsonDBAdapter.LOCATIONID_NUM, locationsCursor.getString(locationsCursor.getColumnIndexOrThrow(JsonDBAdapter.LOCATIONID_NUM)));
					obj.put(JsonDBAdapter.SITENAME_TEXT, locationsCursor.getString(locationsCursor.getColumnIndexOrThrow(JsonDBAdapter.SITENAME_TEXT)));
					obj.put(JsonDBAdapter.ARRAYNAME_TEXT, locationsCursor.getString(locationsCursor.getColumnIndexOrThrow(JsonDBAdapter.ARRAYNAME_TEXT)));
					obj.put(JsonDBAdapter.LATITIUDE_REAL, locationsCursor.getFloat(locationsCursor.getColumnIndexOrThrow(JsonDBAdapter.LATITIUDE_REAL)));
					obj.put(JsonDBAdapter.LONGITUDE_REAL, locationsCursor.getFloat(locationsCursor.getColumnIndexOrThrow(JsonDBAdapter.LONGITUDE_REAL)));
					locationsArray.put(obj);
				}
			}
			locationsCursor.close();
			fullList.put("location", locationsArray);

			JSONArray herpCaptureArray = new JSONArray();
			Cursor herpCaptureCursor = myDbHelper.fetchAllHerpcapture();
			for (int i = 0 ; i<herpCaptureCursor.getCount(); i++){
				herpCaptureCursor.moveToPosition(i);
				if (herpCaptureCursor.getInt(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.CHANGED_NUM)) != 0){
					JSONObject obj = new JSONObject();
					obj.put(JsonDBAdapter.HERPCAPTUREID_TEXT, herpCaptureCursor.getString(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.HERPCAPTUREID_TEXT)));
					obj.put(JsonDBAdapter.TRAP_TEXT, herpCaptureCursor.getString(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.TRAP_TEXT)));
					obj.put(JsonDBAdapter.RECAPTURE_TEXT, herpCaptureCursor.getString(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.RECAPTURE_TEXT)));
					obj.put(JsonDBAdapter.SVL_NUM, herpCaptureCursor.getInt(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.SVL_NUM)));
					obj.put(JsonDBAdapter.VTL_NUM, herpCaptureCursor.getInt(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.VTL_NUM)));
					obj.put(JsonDBAdapter.MASS_REAL, herpCaptureCursor.getDouble(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.MASS_REAL)));
					obj.put(JsonDBAdapter.HDBODY_NUM, herpCaptureCursor.getInt(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.HDBODY_NUM)));
					obj.put(JsonDBAdapter.SEX_TEXT, herpCaptureCursor.getString(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.SEX_TEXT)));
					obj.put(JsonDBAdapter.OTL_NUM, herpCaptureCursor.getInt(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.OTL_NUM)));
					obj.put(JsonDBAdapter.DEAD_TEXT, herpCaptureCursor.getString(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.DEAD_TEXT)));
					obj.put(JsonDBAdapter.HATCHLING_TEXT, herpCaptureCursor.getString(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.HATCHLING_TEXT)));
					obj.put(JsonDBAdapter.IDNEEDED_TEXT, herpCaptureCursor.getString(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.IDNEEDED_TEXT)));
					obj.put(JsonDBAdapter.NOTES_TEXT, herpCaptureCursor.getString(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.NOTES_TEXT)));
					obj.put(JsonDBAdapter.CHECKDATESID_TEXT, herpCaptureCursor.getString(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.CHECKDATESID_TEXT)));
					obj.put(JsonDBAdapter.HERPSID_TEXT, herpCaptureCursor.getString(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.HERPSID_TEXT)));
					obj.put(JsonDBAdapter.IDENTIFIERS_TEXT, herpCaptureCursor.getString(herpCaptureCursor.getColumnIndexOrThrow(JsonDBAdapter.IDENTIFIERS_TEXT)));
					herpCaptureArray.put(obj);
				}
			}
			herpCaptureCursor.close();
			fullList.put("herpcapture", herpCaptureArray);

			settings = classActivity.getSharedPreferences("LizPref", 0);
			
			String oldAreaID = settings.getString("areaid", "empty");
			if (oldAreaID.compareTo(classActivity.currentArea.areaID)==0){
				String date = settings.getString("lastupdate", null);
				if (date != null){
					fullList.put("lastupdate", date);
				}
			}
			
		}
		
		fullList.put("areaid", classActivity.currentArea.areaID);
		JSONObject tableObject = new JSONObject();
		tableObject.put("tables", fullList);
		return tableObject;
	}

	private void parseJSONToDatabase(JSONObject object) {
		Boolean deleted = true;
		SharedPreferences settings = classActivity.getSharedPreferences("LizPref", 0);
		String oldServer = settings.getString("currentServer", "Never Synced");
		String oldAreaID = settings.getString("areaid", "empty");
		if (oldServer.compareTo(classActivity.currentServer)!=0 || oldAreaID.compareTo(classActivity.currentArea.areaID)!=0){
			myDbHelper.deleteall();
			deleted = false;
		}
		try {
			int size = 0;
			int numCompleted = 0;
			JSONArray antCaptures = object.getJSONArray("anthropodcaptures");
			size = antCaptures.length()+size;
			JSONArray anthropods = object.getJSONArray("anthropods");
			size = anthropods.length()+size;
			JSONArray checkdates = object.getJSONArray("checkdates");
			size = checkdates.length()+size;
			JSONArray herpcaptures = object.getJSONArray("herpcaptures");
			size = herpcaptures.length()+size;
			JSONArray herps = object.getJSONArray("herps");
			size = herps.length()+size;
			JSONArray locations = object.getJSONArray("locations");
			size = locations.length()+size;
			if (!object.isNull("deleteditems")){
				JSONArray delitions = object.getJSONArray("deleteditems");
				size = delitions.length()+size;
			}
			if (size == 0){
				size = 3;
				numCompleted = 2;
			}
			classActivity.setProgressSize(size);
			classActivity.updateProgressLevel(numCompleted);
			for (int i = 0; i < antCaptures.length(); i++) {
				numCompleted++;
				try {
					JSONObject antCapture = antCaptures.getJSONObject(i);
					String checkdateId = antCapture.getString("CheckdateID"); 
					String arhropodCaptureId = antCapture.getString("arthropodcaptureid");
					int mant = 0;
					if (!antCapture.isNull("MANT")){
						mant = antCapture.getInt("MANT");
					} 
					int unki = 0;
					if (!antCapture.isNull("UNKI")){
						unki = antCapture.getInt("UNKI");
					} 
					int thys = 0;
					if (!antCapture.isNull("THYS")){
						thys = antCapture.getInt("THYS");
					} 
					int soli = 0;
					if (!antCapture.isNull("SOLI")){
						soli = antCapture.getInt("SOLI");
					} 
					int scor = 0;
					if (!antCapture.isNull("SCOR")){
						scor = antCapture.getInt("SCOR");
					} 
					int pseu = 0;
					if (!antCapture.isNull("PSEU")){
						pseu = antCapture.getInt("PSEU");
					} 
					int orth = 0;
					if (!antCapture.isNull("ORTH")){
						orth = antCapture.getInt("ORTH");
					} 
					int lepi = 0;
					if (!antCapture.isNull("LEPI")){
						lepi = antCapture.getInt("LEPI");
					} 
					int hymb = 0;
					if (!antCapture.isNull("HYMB")){
						hymb = antCapture.getInt("HYMB");
					} 
					int hyma = 0;
					if (!antCapture.isNull("HYMA")){
						hyma = antCapture.getInt("HYMA");
					} 
					int hete = 0;
					if (!antCapture.isNull("HETE")){
						hete = antCapture.getInt("HETE");
					} 
					int dipt = 0;
					if (!antCapture.isNull("DIPT")){
						dipt = antCapture.getInt("DIPT");
					} 
					int diel = 0;
					if (!antCapture.isNull("DIEL")){
						diel = antCapture.getInt("DIEL");
					} 
					int derm = 0;
					if (!antCapture.isNull("DERM")){
						derm = antCapture.getInt("DERM");
					} 
					int crus = 0;
					if (!antCapture.isNull("CRUS")){
						crus = antCapture.getInt("CRUS");
					} 
					int cole = 0;
					if (!antCapture.isNull("COLE")){
						cole = antCapture.getInt("COLE");
					} 
					int chil = 0;
					if (!antCapture.isNull("CHIL")){
						chil = antCapture.getInt("CHIL");
					} 
					int blat = 0;
					if (!antCapture.isNull("BLAT")){
						blat = antCapture.getInt("BLAT");
					} 
					int auch = 0;
					if (!antCapture.isNull("AUCH")){
						auch = antCapture.getInt("AUCH");
					} 
					int aran = 0;
					if (!antCapture.isNull("ARAN")){
						aran = antCapture.getInt("ARAN");
					} 
					String trap = null;
					if (!antCapture.isNull("Trap")){
						trap = antCapture.getString("Trap");
					};
					String predator = null;
					if (!antCapture.isNull("Predator")){
						predator = antCapture.getString("Predator");
					};
					String notes = null;
					if (!antCapture.isNull("Notes")){
						notes = antCapture.getString("Notes");
					};
					//Log.e("anthropodcaptures", ":" + mant + " :" + unki + " :" + thys + " :" + soli + " :" + scor + " :" + pseu + " :" + orth + " :" + lepi + " :" + hymb + " :" + hyma + " :" + hete + " :" + dipt + " :" + diel  + " :" + derm + " :" + crus + " :" + cole + " :" + chil + " :" + blat + " :" + auch + " :" + aran  + " :" + checkdateId + " :" + trap + " :" + predator + " :" + notes + " :" + arhropodCaptureId);
					if (deleted && myDbHelper.anthropodCaptureExists(arhropodCaptureId)){
						myDbHelper.updateAnthropodCapture(mant, unki, thys, soli, scor, pseu, orth, lepi, hymb, hyma, hete, dipt, diel, derm, crus, cole, chil, blat, auch, aran, 0, checkdateId, trap, predator, notes, arhropodCaptureId);
					} else {
						myDbHelper.createAnthropodCapture(mant, unki, thys, soli, scor, pseu, orth, lepi, hymb, hyma, hete, dipt, diel, derm, crus, cole, chil, blat, auch, aran, 0, checkdateId, trap, predator, notes, arhropodCaptureId);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (i%25==0){
					classActivity.updateProgressLevel(numCompleted);
				}
			}
			classActivity.updateProgressLevel(numCompleted);

			for (int i = 0; i < anthropods.length(); i++) {
				numCompleted++;
				try {
					JSONObject anthropod = anthropods.getJSONObject(i);
					String anthropodId = anthropod.getString("ArthropodID");
					String newClass = null;
					if (!anthropod.isNull("Class")){
						newClass = anthropod.getString("Class");
					};
					String orderName = null;
					if (!anthropod.isNull("OrderName")){
						orderName = anthropod.getString("OrderName");
					};
					String orderCode = null;
					if (!anthropod.isNull("OrderCode")){
						orderCode = anthropod.getString("OrderCode");
					};
					String description = null;
					if (!anthropod.isNull("Description")){
						description = anthropod.getString("Description");
					};
					//Log.e("anthropods", ":" + anthropodId + " :" + newClass + " :" + orderName + " :" + orderCode + " :" + description);
					if (deleted && myDbHelper.anthropodExists(anthropodId)){
						myDbHelper.updateAnthropod(anthropodId, newClass, orderName, orderCode, description, 0);
					} else {
						myDbHelper.createAnthropod(anthropodId, newClass, orderName, orderCode, description, 0);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (i%25==0){
					classActivity.updateProgressLevel(numCompleted);
				}
			}
			classActivity.updateProgressLevel(numCompleted);

			for (int i = 0; i < checkdates.length(); i++) {
				numCompleted++;
				try {
					JSONObject checkdate = checkdates.getJSONObject(i);
					String checkdateId = checkdate.getString("CheckdateID");
					String locationID = checkdate.getString("LocationID");
					String handler = null;
					if (!checkdate.isNull("Handler")){
						handler = checkdate.getString("Handler");
					};
					String recorder = null;
					if (!checkdate.isNull("Recorder")){
						recorder = checkdate.getString("Recorder");
					};
					String date = null;
					if (!checkdate.isNull("Date")){
						date = checkdate.getString("Date");
					};
					String time = null;
					if (!checkdate.isNull("Time")){
						time = checkdate.getString("Time");
					};
					String closed = null;
					if (!checkdate.isNull("Closed")){
						closed = checkdate.getString("Closed");
					};
					String noCapture = null;
					if (!checkdate.isNull("NoCapture")){
						noCapture = checkdate.getString("NoCapture");
					};
					String comments = null;
					if (!checkdate.isNull("Comments")){
						comments = checkdate.getString("Comments");
					};
					//Log.e("checkdates", ":" + checkdateId + " :" + handler + " :" + recorder + " :" + date + " :" + time + " :" + closed + " :" + noCapture + " :" + comments + " :" + locationID);
					if (deleted && myDbHelper.checkdateExists(checkdateId)){
						myDbHelper.updateCheckdates(checkdateId, handler, recorder, date, time, closed, noCapture, comments, 0, locationID);
					} else {
						myDbHelper.createCheckdates(checkdateId, handler, recorder, date, time, closed, noCapture, comments, 0, locationID);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (i%25==0){
					classActivity.updateProgressLevel(numCompleted);
				}
			}
			classActivity.updateProgressLevel(numCompleted);

			for (int i = 0; i < herpcaptures.length(); i++) {
				numCompleted++;
				try {
					JSONObject herpcapture = herpcaptures.getJSONObject(i);
					String herpCaptureId = herpcapture.getString("HerpCaptureID");
					String checkdateId = herpcapture.getString("CheckdateID");
					String herpId = herpcapture.getString("HerpID");
					String trap = null;
					if (!herpcapture.isNull("Trap")){
						trap = herpcapture.getString("Trap");
					};
					String recapture = null;
					if (!herpcapture.isNull("Recapture")){
						recapture = herpcapture.getString("Recapture");
					};
					String sex = null;
					if (!herpcapture.isNull("Sex")){
						sex = herpcapture.getString("Sex");
					};
					String dead = null;
					if (!herpcapture.isNull("Dead")){
						dead = herpcapture.getString("Dead");
					};
					String hatcling = null;
					if (!herpcapture.isNull("Hatchling")){
						hatcling = herpcapture.getString("Hatchling");
					};
					String idneeded = null;
					if (!herpcapture.isNull("IdNeeded")){
						idneeded = herpcapture.getString("IdNeeded");
					};
					String notes = null;
					if (!herpcapture.isNull("Notes")){
						notes = herpcapture.getString("Notes");
					};
					String identifiers = null;
					if (!herpcapture.isNull("Identifiers")){
						identifiers = herpcapture.getString("Identifiers");
					};
					int svl = 0;
					if (!herpcapture.isNull("SVL")){
						svl = herpcapture.getInt("SVL");
					} 
					int vtl = 0;
					if (!herpcapture.isNull("VTL")){
						vtl = herpcapture.getInt("VTL");
					} 
					int hdbody = 0;
					if (!herpcapture.isNull("HDBody")){
						hdbody = herpcapture.getInt("HDBody");
					} 
					int otl = 0;
					if (!herpcapture.isNull("OTL")){
						otl = herpcapture.getInt("OTL");
					} 
					long mass = 0;
					if (!herpcapture.isNull("Mass")){
						mass = herpcapture.getLong("Mass");
					} 
					//Log.e("herpcaptures", ":" + herpCaptureId + " :" + trap + " :" + recapture + " :" + sex + " :" + dead + " :" + hatcling + " :" + idneeded + " :" + notes + " :" + checkdateId + " :" + herpId + " :" + identifiers + " :" + svl + " :" + vtl + " :" + hdbody + " :" + otl + " :" + mass);
					if (deleted && myDbHelper.herpCaptureExists(herpCaptureId)){
						myDbHelper.updateHerpcapture(herpCaptureId, trap, recapture, svl, vtl, mass, hdbody, sex, otl, dead, hatcling, idneeded, notes, checkdateId, herpId, identifiers, 0);
					} else {
						myDbHelper.createHerpcapture(herpCaptureId, trap, recapture, svl, vtl, mass, hdbody, sex, otl, dead, hatcling, idneeded, notes, checkdateId, herpId, identifiers, 0);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (i%25==0){
					classActivity.updateProgressLevel(numCompleted);
				}
			}
			classActivity.updateProgressLevel(numCompleted);

			for (int i = 0; i < herps.length(); i++) {
				numCompleted++;
				try{
					JSONObject herp = herps.getJSONObject(i);
					String herpId = herp.getString("HerpID");
					String taxa = null;
					if (!herp.isNull("Taxa")){
						taxa = herp.getString("Taxa");
					};
					String genus = null;
					if (!herp.isNull("Genus")){
						genus = herp.getString("Genus");
					};
					String species = null;
					if (!herp.isNull("Species")){
						species = herp.getString("Species");
					};
					String sppCode = null;
					if (!herp.isNull("SppCode")){
						sppCode = herp.getString("SppCode");
					};
					//Log.e("herps", ":" + herpId + " :" + taxa + " :" + genus + " :" + species + " :" + sppCode);
					if (deleted && myDbHelper.herpExists(herpId)){
						myDbHelper.updateHerps(herpId, taxa, genus, species, sppCode, 0);
					} else {
						myDbHelper.createHerps(herpId, taxa, genus, species, sppCode, 0);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (i%25==0){
					classActivity.updateProgressLevel(numCompleted);
				}
			}
			classActivity.updateProgressLevel(numCompleted);

			for (int i = 0; i < locations.length(); i++) {
				numCompleted++;
				try {
					JSONObject location = locations.getJSONObject(i);
					String locationid = location.getString("LocationID");
					String siteName = null;
					if (!location.isNull("SiteName")){
						siteName = location.getString("SiteName");
					};
					String arrayName = null;
					if (!location.isNull("ArrayName")){
						arrayName = location.getString("ArrayName");
					};
					long latitude = 0;
					if (!location.isNull("Latitude")){
						latitude = location.getLong("Latitude");
					} 
					long longitude = 0;
					if (!location.isNull("Longitude")){
						longitude = location.getLong("Longitude");
					} 
					//Log.e("locations", ":" + locationid + " :" + siteName + " :" + arrayName + " :" + latitude + " :" + longitude);
					if (deleted && myDbHelper.locationExists(locationid)){
						myDbHelper.updateLocations(locationid, siteName, arrayName, latitude, longitude, 0);
					} else {
						myDbHelper.createLocations(locationid, siteName, arrayName, latitude, longitude, 0);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (i%25==0){
					classActivity.updateProgressLevel(numCompleted);
				}
			}
			classActivity.updateProgressLevel(numCompleted);
			
			if (!object.isNull("deleteditems")){
				JSONArray delitions = object.getJSONArray("deleteditems");
				for (int i = 0; i < delitions.length(); i++) {
					numCompleted++;
					try{
						JSONObject deletion = delitions.getJSONObject(i);
						String tablename = deletion.getString("tablename");
						String itemId = deletion.getString("itemId");
						if (tablename.compareTo("checkdates")==0){
							myDbHelper.deleteCheckdate(itemId);
						} else if (tablename.compareTo("herpcaptures")==0){
							myDbHelper.deleteHerpCapture(itemId);
						} else if (tablename.compareTo("arthropod_captures")==0){
							myDbHelper.deleteArthropodCapture(itemId);
						} else if (tablename.compareTo("location")==0){
							myDbHelper.deleteLocation(itemId);
						} else if (tablename.compareTo("herps")==0){
							myDbHelper.deleteHerp(itemId);
						} else if (tablename.compareTo("arthropods")==0){
							myDbHelper.deleteArthropod(itemId);
						}
						//Log.e("deleteditems", ":" + tablename + " :" + itemId);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (i%25==0){
						classActivity.updateProgressLevel(numCompleted);
					}
				}
			}
			classActivity.updateProgressLevel(size);
			String date = object.getString("lastupdate");
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("lastupdate", date);
			editor.putString("currentServer",classActivity.currentServer);
			editor.putString("areaid",classActivity.currentArea.areaID);
			editor.putString("areaname",classActivity.currentArea.areaName);
			editor.commit();

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}