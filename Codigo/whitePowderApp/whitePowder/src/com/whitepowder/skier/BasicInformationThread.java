package com.whitepowder.skier;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whitepowder.R;
import com.google.gson.Gson;
import com.whitepowder.ApplicationError;
import com.whitepowder.Logout;
import com.whitepowder.storage.SPStorage;
import com.whitepowder.user.management.User;

public class BasicInformationThread extends Thread {

	private final String BasicInformationURL = "http://whitetavros.com/Sandbox/web/internalApi/info/basic";
	private ApplicationError mError = null;
	private Context mContext;
	private BasicInformationResponse basicInformationResponse = null;
	private BasicInformation basicInformation;
	
	public BasicInformationThread(Context context) {
		mContext = context;	
	}
	
		
	
	@Override
	public void run() {
		HttpURLConnection connection = null;
			
		try {		
			String token = User.getUserInstance().getToken();
			
			//Generates request
						
			JSONObject request= new JSONObject();
			request.put("_token", token);
			
			URL url = new URL(BasicInformationURL);
			connection = (HttpURLConnection)url.openConnection();
		    connection.setRequestMethod("POST");
		    
		    connection.setUseCaches(false);
		    connection.setDoInput(true);
		    connection.setDoOutput(true);
		    
		    //Send request
		    DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
		    wr.writeBytes(request.toString());
		    wr.flush();
		    wr.close();
		        
		    if(connection.getResponseCode()==200){
		    	//Get Response
				InputStream is = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));		
				String response = reader.readLine();
				
				Gson gson = new Gson();
				basicInformationResponse = gson.fromJson(response, BasicInformationResponse.class);
				if(basicInformationResponse != null){
					if(basicInformationResponse.getCode() == 200){
					SharedPreferences sp = mContext.getSharedPreferences(SPStorage.GENERAL_STORAGE_SHARED_PREFS, Context.MODE_MULTI_PROCESS);
					SharedPreferences.Editor editor = sp.edit();
					editor.putString(SPStorage.BASIC_INFORMATION, response);
					editor.commit();
					};
				};
		    }
		    
		    else{
		    	mError = new ApplicationError(100,"Error","Error en la conexión con el Servidor");
		    };
		}
	    
		catch (MalformedURLException e) {
			mError = new ApplicationError(501,"Error","MalformedURLException en descarga de información básica");
		}
		catch (IOException e) {
			mError = new ApplicationError(502,"Error","IOException en descarga de información básica");
		}
		catch (JSONException e){
			mError = new ApplicationError(503, "Error", "JSONException en descarga de información básica");
		}
		
		finally{
			if(connection!=null){
				connection.disconnect();
			};
		};
	};
}

