package com.whitepowder.slope.recognizer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.whitepowder.ApplicationError;
import com.whitepowder.user.management.User;
import com.google.gson.Gson;

import android.os.AsyncTask;

public class SlopeDownloaderThread extends AsyncTask<Void, Void, Void> {

	private final String SlopeDownloadURL = "http://whitetavros.com/Sandbox/web/internalApi/slope/all";
	private ApplicationError mError = null;
	private SlopeRecognizerActivity mContext;
	
	public SlopeDownloaderThread(SlopeRecognizerActivity ctxt) {
		
		mContext = ctxt;	
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		HttpURLConnection connection=null;
		
		try {		
			JSONObject request= new JSONObject();
			request.put("_token", User.getUserInstance().getToken());
			
			URL url = new URL(SlopeDownloadURL);
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
		    	mError = new ApplicationError(100,"Error","Error en la conexión con el Servidor");
		    };
		}
	    
		catch (MalformedURLException e) {
			mError = new ApplicationError(301,"Error","MalformedURLException en módulo de reseteo de contraseña");
		}
		catch (IOException e) {
			mError = new ApplicationError(302,"Error","IOException en módulo de reseteo de contraseña");
		}
		catch (JSONException e) {
			
		}
		
		finally{
			if(connection!=null){
				connection.disconnect();
			};

		};
		
		return null;
	}

	private void parseResponse(String response){
		try {
			JSONObject jsonObject = new JSONObject(response);	
			JSONArray slopes = jsonObject.getJSONArray("payload");
			//TODO falta algo
			Gson gson = new Gson();
			SimplifiedSlope sl = gson.fromJson(response,SimplifiedSlope.class);
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
