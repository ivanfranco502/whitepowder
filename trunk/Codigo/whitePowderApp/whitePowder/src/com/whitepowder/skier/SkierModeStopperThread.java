package com.whitepowder.skier;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import com.whitepowder.userManagement.User;
import com.whitepowder.utils.ApplicationError;
import com.whitepowder.utils.BaseTavrosURI;
import com.whitepowder.utils.Logout;

public class SkierModeStopperThread extends Thread {
	private String STOP_SKIER_MODE_URL = BaseTavrosURI.getBaseURI()+"skier/offSkiMode";
	private int TIME_SLEEP_BEFORE_RETRY = 2000;
	private Context mContext;
	
	public SkierModeStopperThread(Context ctx) {
		mContext = ctx;
	};
	
	@Override
	public void run() {
		Boolean success = false;
		
		while(success!=true){
			HttpURLConnection connection=null;
			URL url=null;
			
			
			try {
				url = new URL(STOP_SKIER_MODE_URL);
				
				JSONObject request = new JSONObject();
				request.put("_token", User.getUserInstance().getToken());
				
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
					
					success = parseResponse(response);

			    }
			    else{
			    	new ApplicationError(1201, "Warining", "Error HTTP al informar stop en el Skier Mode");
			    };
			}
			
			catch (IOException e) {
				new ApplicationError(1202, "Warining", "IO Exception al informar stop en el Skier Mode");
			}
			catch (JSONException e) {
				new ApplicationError(1203, "Warining", "JSON Exception al informar stop en el Skier Mode");
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
					new ApplicationError(1204, "Warining", "InterruuptedException al informar stop en el Skier Mode");
				};
			};
		};
	};
	
	private Boolean parseResponse(String response){
		Boolean ret=false;
		
		if(response!=null){
		
			try {
				JSONObject responseJO = new JSONObject(response);
				int code = responseJO.getInt("code");
				
				if(code==200){
					ret = true;
				}
				else if(code==110){
	    			new ApplicationError(3,"Error","Token inválido al informar stop en el Skier Mode");
	    			Logout.logout(mContext, false);
	    			ret=true;
				}
				else{
					new ApplicationError(1206,"Warning","Mensaje inesperado al informar stop en el Skier Mode");
				};
				
	
			} 
			catch (JSONException e) {
				new ApplicationError(1203, "Warining", "JSON Exception al informar stop en el Skier Mode");
			};
		}
		else{
			new ApplicationError(1206,"Warning","Mensaje inesperado al informar stop en el Skier Mode");
		};
		
		return ret;
	}
	
}
