package com.whitepowder.skier.map;

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
import com.google.gson.Gson;
import com.whitepowder.userManagement.User;
import com.whitepowder.utils.ApplicationError;

public class SlopeDownloaderThread extends Thread {

    
	private final String SlopeDownloadURL = "http://whitetavros.com/Sandbox/web/internalApi/slope/all";
	private Context mContext;
	
	public  SlopeDownloaderThread(Context ctx){
		mContext = ctx;
	};
	
	@Override
    public void run() {
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
				
				Gson gson = new Gson();
			/*	mSlopes = gson.fromJson(response,SlopeContainer.class);
				
				if(mSlopes!=null){
					if((mSlopes.code)==200){
						SharedPreferences sp = mContext.getSharedPreferences(SPStorage.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_MULTI_PROCESS);
						SharedPreferences.Editor editor = sp.edit();
						editor.putString(SPStorage.SIMPLIFIED_SLOPES, response);
						editor.commit();
					};
				};		*/		
		    }
		    
		    else{
		    	new ApplicationError(100,"Error","Error en la conexión con el Servidor");
		    };
		}
	    
		catch (MalformedURLException e) {
			new ApplicationError(601,"Error","MalformedURLException en módulo de reconocimiento de pista");
		}
		catch (IOException e) {
			new ApplicationError(602,"Error","IOException en módulo de reconocimiento de pista");
		}
		catch (JSONException e) {
			new ApplicationError(603,"Error","JSonException en módulo de reconocimiento de pista");
		}
		
		finally{
			if(connection!=null){
				connection.disconnect();
			};

		};
		
	};
	
}
