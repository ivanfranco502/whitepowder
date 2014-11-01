package com.whitepowder.rescuer;

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

public class SetAccidentAsAttendedThread extends Thread {
	

	private Context mContext;
	private int alertId=-1;
	private String SetAccidentAsAttendedURL = BaseTavrosURI.getBaseURI()+"alert/read/";
	private Boolean success = false;
	private int TIME_SLEEP_BEFORE_RETRY = 1500;
	
	public SetAccidentAsAttendedThread(int id,Context ctx) {
		mContext = ctx;
		alertId = id;
	};
	
	@Override
	public void run() {
		super.run();
		SetAccidentAsAttendedURL +=alertId;
		HttpURLConnection connection=null;	
		
		for(int i=0;((success!=true)&&(i<10));i++){
			
			try {
				JSONObject request = new JSONObject();
				request.put("_token", User.getUserInstance().getToken());
				URL url = new URL(SetAccidentAsAttendedURL);
				connection = (HttpURLConnection)url. openConnection();
				
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
			    	new ApplicationError(1103, "Warining", "Error HTTP al marcar accidente como leido");
			    };
			}
			
			catch (IOException e) {
				new ApplicationError(1104, "Warining", "IO Exception al marcar accidente como leido");
			}
			catch (JSONException e) {
				new ApplicationError(1105, "Warining", "JSON Exception al marcar accidente como leido");
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
					new ApplicationError(1106, "Warining", "InterruuptedException al marcar accidente como leido");
				};
			};
		};		
		
	}
	
	private void parseResponse(String response){
		try {
			if(response!=null){
				JSONObject msj = new JSONObject(response);	
				if(msj.getInt("code")==200){
				}
				else if(msj.getInt("code")==110){
					Logout.logout(mContext, false);
				};
				success=true;

			};

			
		} 
		catch (JSONException e) {
			new ApplicationError(1105, "Warining", "JSON Exception al descargar alertas sin leer en rescatista");
		};
		
	};
	

}