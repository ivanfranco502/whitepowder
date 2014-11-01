package com.whitepowder.rescuer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.whitepowder.userManagement.User;
import com.whitepowder.utils.ApplicationError;
import com.whitepowder.utils.BaseTavrosURI;
import com.whitepowder.utils.Logout;

public class AccidentDownloaderThread extends AsyncTask<Void, Void, Void> {
	
	
	private final String AccidentDownloadURL = BaseTavrosURI.getBaseURI()+"alert/allUnread";
	private Gson gson;
	private int TIME_SLEEP_BEFORE_RETRY = 1500;
	private RescuerActivity rescuerActivity;
	private ArrayList<Victim> victims=null;
	private Boolean success = false;
	
	
	public AccidentDownloaderThread(RescuerActivity act) {
		rescuerActivity = act;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		
		if(victims!=null){
			rescuerActivity.adapter.clear();
			rescuerActivity.adapter.addAll(victims);
		};

	}

	@Override
	protected Void doInBackground(Void... params) {
		HttpURLConnection connection=null;
		gson = new Gson();		
		for(int i=0;(success!=true)&&(i<5);i++){
			
			try {
				JSONObject request = new JSONObject();
				request.put("_token", User.getUserInstance().getToken());
				URL url = new URL(AccidentDownloadURL);
				connection = (HttpURLConnection)url.openConnection();
			    connection.setRequestMethod("POST");
				
			    connection.setUseCaches (false);
			    connection.setDoInput(true);
			    connection.setDoOutput(true);
			    
			    //Send request
			    DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
			    wr.writeBytes(request.toString());
			    wr.flush ();
			    wr.close ();
			        
			    if(connection.getResponseCode()==200){
			    	
			    	//Get Response
					InputStream is = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(is));		
					String response = reader.readLine();
					
					parseResponse(response);

			    }
			    else{
			    	new ApplicationError(1103, "Warining", "Error HTTP al descargar alertas sin leer en rescatista");
			    };
			}
			
			catch (IOException e) {
				new ApplicationError(1104, "Warining", "IO Exception al descargar alertas sin leer en rescatista");
			}
			catch (JSONException e) {
				new ApplicationError(1105, "Warining", "JSON Exception al descargar alertas sin leer en rescatista");
			}
			
			finally{
				if(connection!=null){
					connection.disconnect();
				};

			};
			
			if(success){
				break;
			}
			else{
				try {
					Thread.sleep(TIME_SLEEP_BEFORE_RETRY);
				} 
				catch (InterruptedException e) {
					new ApplicationError(1106, "Warining", "InterruuptedException al descargar alertas sin leer en rescatista");
				};
			};
		};		
		return null;
	};
	
	private void parseResponse(String response){
		try {
			if(response!=null){
				JSONObject msj = new JSONObject(response);	
				if(msj.getInt("code")==200){
					JSONObject payload = msj.getJSONObject("payload");
					if(payload.has("alerts")){
						JSONArray alerts = payload.getJSONArray("alerts");
						victims = gson.fromJson(alerts.toString(), new TypeToken<ArrayList<Victim>>(){}.getType());	
					};
					success=true;
				}
				else if(msj.getInt("code")==110){
					Logout.logout(rescuerActivity, false);
				};

			};

			
		} 
		catch (JSONException e) {
			new ApplicationError(1105, "Warining", "JSON Exception al descargar alertas sin leer en rescatista");
		};
		
	};
	

}
