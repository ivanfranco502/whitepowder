package com.whitepowder.gcmModule;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.whitepowder.userManagement.User;
import com.whitepowder.utils.ApplicationError;
import com.whitepowder.utils.BaseTavrosURI;
import com.whitepowder.utils.Logout;

public class GCM {
	
	private int TIME_SLEEP_BEFORE_RETRY = 1000;
	private final String SET_REGISTRATION_ID_URL = BaseTavrosURI.getBaseURI()+"skier/setGCMRegistrationCode";
	Context mContext;
	String SENDER_ID = "1025941805069";
	GoogleCloudMessaging gcm;

	
	public GCM(Context ctx) {
		mContext = ctx;
		gcm = GoogleCloudMessaging.getInstance(mContext);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					String registrationId = null;
					HttpURLConnection connection=null;
					Boolean success = false;
					URL url=null;
					
					//Reintento hasta tener éxito en la registración
					
					while(registrationId==null){
						try {
							url = new URL(SET_REGISTRATION_ID_URL);
							registrationId = gcm.register(SENDER_ID);
						} 
						catch (MalformedURLException e1) {
							new ApplicationError(1100, "Warining", "MalformedURLException al registrarse a GCM");
						} 
						catch (IOException e) {
							new ApplicationError(1101, "Warining", "IOException al registrarse a GCM");
							
							try {
								Thread.sleep(TIME_SLEEP_BEFORE_RETRY);
							} 
							catch (InterruptedException e1) {
								new ApplicationError(1102, "Warining", "InterruptedException al dormir threla registración fallida de GCM");
							};
						};
					};
										
					//Reintento hasta tener éxito
					
					while(success!=true){
						
						try {
							JSONObject request = new JSONObject();
							request.put("_token", User.getUserInstance().getToken());
							request.put("registrationCode", registrationId);
							
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
						    	new ApplicationError(1103, "Warining", "Error HTTP al informar registrationID en GCM");
						    };
						}
						
						catch (IOException e) {
							new ApplicationError(1104, "Warining", "IO Exception al informar registrationID en GCM");
						}
						catch (JSONException e) {
							new ApplicationError(1105, "Warining", "JSON Exception al informar registrationID en GCM");
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
								new ApplicationError(1106, "Warining", "InterruuptedException al informar registrationID en GCM");
							};
						};
					}
				} 

				
				private Boolean parseResponse(String response){
					Boolean ret=false;
									
					try {
						JSONObject responseJO = new JSONObject(response);
						int code = responseJO.getInt("code");
						
						if(code==200){
							ret = true;
						}
						else if(code==110){
		        			new ApplicationError(1107,"Error","Token inválido al informar registrationID en GCM");
		        			Logout.logout(mContext, false);
		        			ret=true;
						}
						else{
							new ApplicationError(1108,"Warning","Mensaje inesperado al informar registrationID en GCM");
						};
						

					} 
					catch (JSONException e) {
						new ApplicationError(1105, "Warining", "JSON Exception al informar registrationID en GCM");
					};
					return ret;
					
				};
			}).start();			
	};
	
	
}
