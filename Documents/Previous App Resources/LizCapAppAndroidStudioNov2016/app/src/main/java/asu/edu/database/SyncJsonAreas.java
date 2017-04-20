package asu.edu.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
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

import android.os.Handler;
import android.util.Log;
import asu.edu.activity.Sync;

public class SyncJsonAreas extends Thread {
	private static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms
	static final String LOG_TAG = "JSONAREA";
	private DefaultHttpClient httpClient = null;
	private Handler uiHandler;
	private Sync classActivity;
	private String serverName;

	public SyncJsonAreas(Handler handle, Sync newClass, String newServerName){
		classActivity = newClass;
		uiHandler = handle;
		serverName = newServerName;
	}

	public void run() {
		String errMsg = process();
		ProcessServerUIUpdate plu = new ProcessServerUIUpdate(errMsg, classActivity);
		uiHandler.post( plu );
	}

	private String process() {
		String errMsg = "Areas";
		String response = null;
		try {
			response = sendToServer();
			Log.d( LOG_TAG, "response: "+response );
			JSONTokener tokener = new JSONTokener( response );
			Object o = tokener.nextValue();
			if( o instanceof JSONObject ) {
				parseJSONToAreas((JSONObject)o);
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

	private String sendToServer() throws IOException {
		String result = null;
		maybeCreateHttpClient();

		String url = classActivity.serverString + "/mobileareas?format=json";
		HttpGet get = new HttpGet(url);
		get.setHeader("Content-Type","application/json");  

		HttpResponse resp = httpClient.execute( get );
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
		/*if ( httpClient == null) {
			httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, REGISTRATION_TIMEOUT);
			ConnManagerParams.setTimeout(params, REGISTRATION_TIMEOUT);
		}
		
		if ( httpClient == null) {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, REGISTRATION_TIMEOUT);
			ConnManagerParams.setTimeout(params, REGISTRATION_TIMEOUT);
			
			
			X509HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

			//DefaultHttpClient client = new DefaultHttpClient();

			SchemeRegistry registry = new SchemeRegistry();
			SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
			socketFactory.setHostnameVerifier(hostnameVerifier);
			//socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
			registry.register(new Scheme("https", socketFactory, 443));
			SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
			httpClient = new DefaultHttpClient(mgr, client.getParams());

			// Set verifier     
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

			
			
			
			
			
			
		}*/
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

	private void parseJSONToAreas(JSONObject object){
		try {
			ArrayList<Area> areaArray = new ArrayList<Area>();
			JSONArray areaJSON = object.getJSONArray("areas");
			for (int i = 0; i< areaJSON.length(); i++){
				JSONObject nextArea = areaJSON.getJSONObject(i);
				areaArray.add(new Area(nextArea));
			}
			classActivity.areaList.put(serverName, areaArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}