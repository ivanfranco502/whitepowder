package com.whitepowder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;

public class PasswordChangeThread extends AsyncTask<String, Void, Void> {

	private final String PasswordChangeURL = "";
	private ApplicationError mError = null;
	private PasswordChangeActivity mContext;
	
	public PasswordChangeThread(PasswordChangeActivity ctxt) {
		
		mContext = ctxt;	
	}
	
	@Override
	protected Void doInBackground(String... registerInput) {
		HttpURLConnection connection= null;
		String username = registerInput[0];
		String currentPassword = registerInput[1];
		String newPassword = registerInput[2];
		String repeatedNewPassword = registerInput[3];
		
		if(!validateInput(username, currentPassword, newPassword, repeatedNewPassword)){
			return null;
		};
		
		try {		
			//Generates request
			currentPassword = SHA1Manager.SHA1(currentPassword);
			newPassword = SHA1Manager.SHA1(newPassword);
			String params = "username="+URLEncoder.encode(username, "utf-8")+"&current_password="+URLEncoder.encode(currentPassword, "utf-8")+"&new_password="+URLEncoder.encode(newPassword, "utf-8");
			URL url = new URL(PasswordChangeURL);
			connection = (HttpURLConnection)url.openConnection();
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
				connection.disconnect();
				parseResponse(response);
		    }
		    
		    else{
		    	mError = new ApplicationError(100,"Error","Error en la conexión con el Servidor");
		    };
		}
	    
		catch (MalformedURLException e) {
			mError = new ApplicationError(201,"Error","MalformedURLException en módulo de registro");
		}
		catch (IOException e) {
			mError = new ApplicationError(202,"Error","IOException en módulo de registro");
		}
		catch (NoSuchAlgorithmException e) {
			mError = new ApplicationError(209,"Error","NoSuchAlgorithmException en módulo de registro");
		}
		finally{
			if(connection!=null){
				connection.disconnect();
			};
		};
		
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void unused) {	
	}
	
	@Override
	protected void onPreExecute(){
		
	}
	
	private boolean validateInput(String user, String currentPassword, String newPassword, String repeatedNewPassword){
		
		if(!(user.length()>0&&currentPassword.length()>0&&newPassword.length()>0&&repeatedNewPassword.length()>0)){
			//TODO Raiserror
			return false;
		};
		
		if(newPassword.length()<=6){
			//TODO Raiserror
			return false;
		};
		
		if(!newPassword.equals(repeatedNewPassword)){
			//TODO Raiserror
			return false;
		};
		
		
		return true;
	}
	
	private void parseResponse(String response){
		try {
			JSONObject jsonObject = new JSONObject(response);
			
			//Gets code from json response
	        int code = jsonObject.getInt("code");

	        if(code!=200){
	        	//TODO Check errors
	        };		
		} 
		
		catch (JSONException e) {
			//TODO Raiserror
		};
	}

}
