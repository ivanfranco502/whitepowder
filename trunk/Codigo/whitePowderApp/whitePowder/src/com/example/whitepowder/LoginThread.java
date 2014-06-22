package com.example.whitepowder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.os.AsyncTask;
import android.util.Log;

public class LoginThread extends AsyncTask<String, Void, Void> {
	
	private final String LoginURL = "http://whitetavros.com/Symfony/web/api/login";
	
	protected void onPreExecute() {
	// NOTE: You can call UI Element here.       
	}
	
	@Override
	protected Void doInBackground(String... loginInput) {
		
		try {
			String params = "username="+loginInput[0]+"&password="+loginInput[1];
			URL url = new URL(LoginURL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("POST");
		    
		    connection.setUseCaches (false);
		    connection.setDoInput(true);
		    connection.setDoOutput(true);
		    
		    //Send request
		    DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
		    wr.writeBytes(params);
		    wr.flush ();
		    wr.close ();
		        
		    if(connection.getResponseCode()==200){
			    
		    	//Get Response
				InputStream is = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));		
				String response = reader.readLine();
				//TODO parse response
		    }
		    
		} 
		catch (MalformedURLException e) {
			Log.i("Error","Malformed URL in login module");
		} 
		catch (IOException e) {
			Log.i("Error","IO exception in login module");
		}
		
		return null;
	}
	
    protected void onPostExecute(Void unused) {	

   }

}
